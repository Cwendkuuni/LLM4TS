package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution mockExecution;

    @Mock
    private EventPublisher mockEventPublisher;

    @Mock
    private FlowableAdaptor mockFlowableAdaptor;

    private TestRuntimeFlowableDelegate testDelegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testDelegate = new TestRuntimeFlowableDelegate();
        testDelegate.init(mockEventPublisher);
    }

    @Test
    public void testRun_SuccessfulExecution() throws Exception {
        // Mock
        Callable<String> mockCallable = () -> "TestResult";
        testDelegate.initCallable(mockExecution);
        testDelegate.setMockCallable(mockCallable);

        // Execute
        testDelegate.run(mockExecution);

        // Verify
        assertEquals("TestResult", testDelegate.get());
    }

    @Test(expected = TimeoutException.class)
    public void testGet_TimeoutException() throws Exception {
        // Execute
        testDelegate.get(1, TimeUnit.MILLISECONDS);
    }

    // Custom BaseRuntimeFlowableDelegate for testing purposes
    private static class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        private Callable<String> mockCallable;

        public void setMockCallable(Callable<String> mockCallable) {
            this.mockCallable = mockCallable;
        }

        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return mockCallable;
        }
    }
}