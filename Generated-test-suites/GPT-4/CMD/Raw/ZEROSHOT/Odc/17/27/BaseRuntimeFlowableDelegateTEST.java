
package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.delegate.ExecutionListenerImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    DelegateExecution delegateExecution;

    @InjectMocks
    BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "TEST";
        }
    };

    @Before
    public void setup() {
        ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "done", true);
    }

    @Test
    public void testIsDone() {
        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        assertEquals("TEST", baseRuntimeFlowableDelegate.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGet_withException() throws InterruptedException, ExecutionException {
        ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "thrown", new Exception("TESTEXCEPTION"));
        try {
            baseRuntimeFlowableDelegate.get();
        } finally {
            ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "thrown", null);
        }
    }

    @Test
    public void testGet_withTimeout() throws InterruptedException, ExecutionException, java.util.concurrent.TimeoutException {
        assertEquals("TEST", baseRuntimeFlowableDelegate.get(5, java.util.concurrent.TimeUnit.SECONDS));
    }

    @Test(expected = ExecutionException.class)
    public void testGet_withTimeout_withException() throws InterruptedException, ExecutionException, java.util.concurrent.TimeoutException {
        ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "thrown", new Exception("TESTEXCEPTION"));
        try {
            baseRuntimeFlowableDelegate.get(5, java.util.concurrent.TimeUnit.SECONDS);
        } finally {
            ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "thrown", null);
        }
    }

    @Test(expected = java.util.concurrent.TimeoutException.class)
    public void testGet_withTimeoutFail() throws InterruptedException, ExecutionException, java.util.concurrent.TimeoutException {
        ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "done", false);
        baseRuntimeFlowableDelegate.get(5, java.util.concurrent.TimeUnit.MICROSECONDS);
    }

    @Test
    public void testExecute() {
        baseRuntimeFlowableDelegate.execute(delegateExecution);
        verify(delegateExecution, times(1)).setVariable(eq("event"), any(ExecutionListenerImpl.class));
    }
}
