package com.oceanbase.odc.service.flow.task;

import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.*;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

public class BaseRuntimeFlowableDelegateTest {
    private BaseRuntimeFlowableDelegate<Object> delegate;

    private EventPublisher eventPublisher = mock(EventPublisher.class);
    private FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
    private FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
    private DelegateExecution execution = mock(DelegateExecution.class);
    private Callable<Object> callable = mock(Callable.class);
    private FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);

    @Before
    public void setup() {
        delegate = mock(BaseRuntimeFlowableDelegate.class, withSettings()
                .useConstructor()
                .defaultAnswer(CALLS_REAL_METHODS));
        when(delegate.initCallable(any(DelegateExecution.class))).thenReturn(callable);
        delegate.eventPublisher = eventPublisher;
        delegate.flowableAdaptor = flowableAdaptor;
        delegate.flowInstanceRepository = flowInstanceRepository;
    }

    @Test
    public void testRun() throws Exception {
        when(execution.getCurrentActivityId()).thenReturn("testActivityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(Optional.of(1L)));
        when(execution.getProcessDefinitionId()).thenReturn("testProcessDefId");
        when(flowableAdaptor.getTaskInstanceByActivityId(any(), any())).thenReturn(Optional.of(flowTaskInstance));
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(Optional.of(1L)));
        when(flowTaskInstance.getId()).thenReturn(1L);
        when(callable.call()).thenReturn(null);

        delegate.run(execution);

        verify(delegate, times(1)).initTargetTaskInstanceId(execution);
        verify(delegate, times(1)).initCallable(execution);
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(callable, times(1)).call();
        verify(delegate, times(1)).preHandle(execution);
    }

    @Test
    public void testExecute() {
        delegate.execute(execution);

        verify(delegate, times(1)).super.execute(execution);
        verify(eventPublisher, times(2)).removeEventListener(any());
    }

    @Test
    public void testIsDone() {
        delegate.isDone();
        boolean done = false;

        verify(delegate, times(1)).isDone();
        assert(done == delegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        Object obj = new Object();

        delegate.returnObject = obj;
        Object result = delegate.get();
        
        verify(delegate, times(1)).get();
        assert(obj.equals(result));
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        Object obj = new Object();

        delegate.returnObject = obj;
        Object result = delegate.get(1000, TimeUnit.MILLISECONDS);
        
        verify(delegate, times(1)).get(anyLong(), any(TimeUnit.class));
        assert(obj.equals(result));
    }

    @Test
    public void testBindToFlowTaskInstance() {
        delegate.targetTaskInstanceId = 1L;
        when(flowTaskInstance.getId()).thenReturn(1L);
        
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        
        verify(flowTaskInstance, times(1)).bindServiceTask(any());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);
        
        delegate.flowInstanceId = 1L;
        delegate.updateFlowInstanceStatus(FlowStatus.INIT);

        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), any(FlowStatus.class));
    }
}
