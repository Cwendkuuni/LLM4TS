package com.oceanbase.odc.service.flow.task;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.util.concurrent.ExecutionException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

public class BaseRuntimeFlowableDelegateTest {
    private DelegateExecution executionMock;
    private BaseRuntimeFlowableDelegate<Object> delegate;

    @Before
    public void setup() {
        executionMock = mock(DelegateExecution.class);
        delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        };
    }
    
    @javax.annotation.PostConstruct
    @Test
    public void initTest() {
        delegate.init(null);
        // Since init only adds event listeners, further tests would need to assert those listeners are added but for that, we need to expose the listeners through a getter.
    }

    @Test
    public void isDoneTest() {
        delegate.init(null);
        boolean done = delegate.isDone();
        assertTrue(!done);
    }

    @Test(expected = ExecutionException.class)
    public void getTest_ExceptionThrown() throws Exception {
        doThrow(new RuntimeException()).when(executionMock).setVariable("errMsg", null);
        delegate.get();
    }

    // ...remaining tests

}