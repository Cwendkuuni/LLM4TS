/*
 * Copyright (c) 2024 OceanBase.
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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.service.flow.exception.ServiceTaskError;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;
    private BaseRuntimeFlowableDelegateTestDelegate delegate;
    private ExecutorService executor;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        delegate = new BaseRuntimeFlowableDelegateTestDelegate();
        executor = Executors.newSingleThreadExecutor();
    }

    @After
    public void teardown() {
        executor.shutdown();
    }

    @Test
    public void testFlowTaskAllScenarios() throws Exception {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.doReturn("myActivityId").when(delegateExecution).getCurrentActivityId();
        Future<?> result = executor.submit(() -> delegate.execute(delegateExecution));
        
        Thread.sleep(1000); // give some time for the method to run
        Assert.assertTrue(delegate.isDone());
        
        try {
            delegate.get(60, TimeUnit.SECONDS); // allow long enough to ensure the method has returned
        } catch (InterruptedException | ExecutionException e) {
            Assert.assertTrue(e.getCause() instanceof ServiceTaskError);
            Mockito.verify(delegateExecution).getCurrentActivityId();
            Mockito.verify(delegateExecution).getProcessDefinitionId();
        }
        
        Assert.assertEquals("myActivityId", delegate.getActivityId());
    }

    private class BaseRuntimeFlowableDelegateTestDelegate extends BaseRuntimeFlowableDelegate<Object> {
        @Override
        protected Callable<Object> initCallable(DelegateExecution execution) {
            return () -> null;
        }
    }
}