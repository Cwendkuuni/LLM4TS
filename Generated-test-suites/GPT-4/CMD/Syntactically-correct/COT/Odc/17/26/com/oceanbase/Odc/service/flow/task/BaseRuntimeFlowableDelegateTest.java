package com.oceanbase.odc.service.flow.task;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;
    
    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;
    
    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRun() throws Exception {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class, CALLS_REAL_METHODS);
        when(execution.getCurrentActivityId()).thenReturn("1");
        doReturn(Optional.of(1L)).when(delegate).initTargetTaskInstanceId(execution);
        delegate.run(execution);
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class, CALLS_REAL_METHODS);
        delegate.run(execution);
        assertNotNull(delegate.get());
    }

    @Test
    public void testIsDone() throws Exception {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class, CALLS_REAL_METHODS);
        delegate.run(execution);
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class, CALLS_REAL_METHODS);
        delegate.run(execution);
        assertNotNull(delegate.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void testExecute() {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class, CALLS_REAL_METHODS);
        delegate.execute(execution);
        verify(eventPublisher).removeEventListener(delegate);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        BaseRuntimeFlowableDelegate<FlowTaskInstance> delegate = mock(BaseRuntimeFlowableDelegate.class, CALLS_REAL_METHODS);
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class, CALLS_REAL_METHODS);
        when(taskInstance.getId()).thenReturn(1L);
        delegate.setTargetTaskInstanceId(1L);
        delegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class, CALLS_REAL_METHODS);
        delegate.updateFlowInstanceStatus(FlowStatus.STARTED);
        verify(flowInstanceRepository).updateStatusById(delegate.getFlowInstanceId(), FlowStatus.STARTED);
    }
}
