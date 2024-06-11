
package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;
    
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    
    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> baseRuntimeFlowableDelegate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(execution.getCurrentActivityId()).thenReturn("test");
        when(execution.getProcessDefinitionId()).thenReturn("test");
    }

    @Test
    public void testExecute() {
        baseRuntimeFlowableDelegate.execute(execution);
        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test(expected = ServiceTaskError.class)
    public void testRun_whenInitFails() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.empty());
        baseRuntimeFlowableDelegate.run(execution);
    }

    @Test(expected = Exception.class)
    public void testRun_whenCallableFails() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(Optional.of(1L)));
        baseRuntimeFlowableDelegate.run(execution);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.execute(execution);
        Object result = baseRuntimeFlowableDelegate.get();
        assertNull(result);
    }

    @Test(expected = TimeoutException.class)
    public void testGet_whenTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
    }
}
