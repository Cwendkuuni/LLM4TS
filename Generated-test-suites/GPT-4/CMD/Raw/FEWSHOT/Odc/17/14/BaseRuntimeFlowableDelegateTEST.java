
package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @InjectMocks
    private BaseRuntimeFlowableDelegate mockBaseRuntimeFlowableDelegate;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setUp() {
        //optional setup methods
    }

    @Test
    public void testIsDone() {
        mockBaseRuntimeFlowableDelegate.isDone();
        verify(mockBaseRuntimeFlowableDelegate, times(1)).isDone();
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        mockBaseRuntimeFlowableDelegate.get();
        verify(mockBaseRuntimeFlowableDelegate, times(1)).get();
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        mockBaseRuntimeFlowableDelegate.get(100L, TimeUnit.MILLISECONDS);
        verify(mockBaseRuntimeFlowableDelegate, times(1)).get(anyLong(), any(TimeUnit.class));
    }

    @Test
    public void testExecute() {
        mockBaseRuntimeFlowableDelegate.execute(delegateExecution);
        verify(mockBaseRuntimeFlowableDelegate, times(1)).execute(delegateExecution);
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        when(delegateExecution.getProcessDefinitionId()).thenReturn("someDefinition");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("someDefinition")).thenReturn(Optional.of(10L));
        when(delegateExecution.getCurrentActivityId()).thenReturn("someId");
        when(flowableAdaptor.getTaskInstanceByActivityId("someId", 10L)).thenReturn(Optional.of(new FlowTaskInstance()));
        //invoke the private method through reflection or make it protected and override it.
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.setId(mockBaseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        mockBaseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);
        verify(mockBaseRuntimeFlowableDelegate, times(1)).bindToFlowTaskInstance(flowTaskInstance);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        mockBaseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETE);
        verify(mockBaseRuntimeFlowableDelegate, times(1)).updateFlowInstanceStatus(FlowStatus.COMPLETE);
    }
}
