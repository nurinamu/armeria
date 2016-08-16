/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.armeria.common;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.linecorp.armeria.common.stream.StreamMessage;

/**
 * A response stream or a holder of the future result value.
 * It must implement {@link StreamMessage} or {@link CompletionStage}, but not both.
 */
public interface Response {

    /**
     * Returns a {@link CompletableFuture} which completes when
     * 1) the response stream has been closed (the {@link StreamMessage} has been closed) or
     * 2) the result value is set (the {@link CompletionStage} has completed.)
     */
    default CompletableFuture<?> closeFuture() {
        if (this instanceof StreamMessage) {
            return ((StreamMessage<?>) this).closeFuture();
        }

        if (this instanceof CompletionStage) {
            return ((CompletionStage<?>) this).toCompletableFuture();
        }

        throw new IllegalStateException(
                "response must be a " + StreamMessage.class.getSimpleName() + " or a " +
                CompletionStage.class.getSimpleName() + ": " + getClass().getName());
    }
}
