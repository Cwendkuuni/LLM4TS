
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

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.concurrent.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate testDelegate;

    @Mock
    private DelegateExecution delegateExecution;
    
    @Mock
    private Callable callable;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testDelegate = new BaseRuntimeFlowableDelegate() {
            @Override
            protected Callable initCallable(DelegateExecution execution) {
                return callable;
            }
        };
    }

    @Test
    public void testExecute() throws Exception {
        testDelegate.execute(delegateExecution);
        assertEquals(true, testDelegate.isDone());
    }

    @Test
    public void testRun() throws Exception {
        Whitebox.setInternalState(testDelegate, "activityId", "activityId");
        Whitebox.setInternalState(testDelegate, "done", false);
        Whitebox.setInternalState(testDelegate, "thrown", new Exception("test exception"));
        testDelegate.run(delegateExecution);
    }

    @Test
    public void testGet() throws Exception {
        Whitebox.setInternalState(testDelegate, "latch", new CountDownLatch(0));
        Whitebox.setInternalState(testDelegate, "thrown", null);
        Whitebox.setInternalState(testDelegate, "returnObject", "test");
        assertEquals("test", testDelegate.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGet_withException() throws Exception {
        Whitebox.setInternalState(testDelegate, "latch", new CountDownLatch(0));
        Whitebox.setInternalState(testDelegate, "thrown", new Exception("test exception"));
        testDelegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGet_withTimeout_andException() throws Exception {
        Whitebox.setInternalState(testDelegate, "latch", new CountDownLatch(0));
        Whitebox.setInternalState(testDelegate, "done", true);
        Whitebox.setInternalState(testDelegate, "thrown", new Exception("test exception"));
        testDelegate.get(1, TimeUnit.SECONDS);
    }
}
