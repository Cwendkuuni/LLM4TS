/*
 * Copyright (c) 2023 OceanBase.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @InjectMocks
    private BaseRuntimeFlowableDelegate<?> baseRuntimeFlowableDelegate;

    @Mock
    private DelegateExecution execution;

    @Test
    public void testIsDone() {
        boolean result = baseRuntimeFlowableDelegate.isDone();
        assertEquals(false, result);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        Future<?> future = baseRuntimeFlowableDelegate;
        Object result = future.get();
        assertNull(result);
    }

    @Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        Future<?> future = baseRuntimeFlowableDelegate;
        Object result = future.get(100, TimeUnit.MILLISECONDS);
        assertNull(result);
    }

    @Test
    public void testInitCallable() {
        Callable<?> result = baseRuntimeFlowableDelegate.initCallable(execution);
        assertNotNull(result);
    }
}