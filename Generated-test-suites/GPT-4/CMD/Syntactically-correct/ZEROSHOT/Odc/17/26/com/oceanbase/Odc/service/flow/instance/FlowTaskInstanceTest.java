package com.oceanbase.odc.service.flow.instance;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;

public class FlowTaskInstanceTest {

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepositoryMock;

    @Mock
    private TaskService taskServiceMock;

    @Mock
    private EventPublisher eventPublisherMock;

    @Mock
    private RuntimeDelegateMapper delegateConvertorMock;

    @Mock
    private BaseRuntimeFlowableDelegate<?> baseRuntimeFlowableDelegateMock;

    private FlowTaskInstance flowTaskInstanceUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        // Initialize the object under test
        // Assuming valid arguments are passed
        flowTaskInstanceUnderTest = new FlowTaskInstance(
                mock(TaskType.class), 1L, 1L, mock(ExecutionStrategyConfig.class), false, false, 
                delegateConvertorMock, mock(FlowableAdaptor.class), eventPublisherMock, taskServiceMock, 
                mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class), 
                serviceTaskRepositoryMock);
    }

    @Test
    public void testCreate() {
        flowTaskInstanceUnderTest.create();
        verify(serviceTaskRepositoryMock, times(1)).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testUpdateFlowTaskInstance() {
        flowTaskInstanceUnderTest.update();
        verify(serviceTaskRepositoryMock, times(1)).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testDoDelete() {
        flowTaskInstanceUnderTest.doDelete(1L);
        verify(serviceTaskRepositoryMock, times(1)).deleteById(1L);
    }

    @Test
    public void testBindServiceTask_SameInstance() {
        when(baseRuntimeFlowableDelegateMock.getTargetTaskInstanceId()).thenReturn(flowTaskInstanceUnderTest.getId());
        flowTaskInstanceUnderTest.bindServiceTask(baseRuntimeFlowableDelegateMock);
        assertEquals(flowTaskInstanceUnderTest.getStatus(), FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testBindServiceTask_DifferentInstance() {
        when(baseRuntimeFlowableDelegateMock.getTargetTaskInstanceId()).thenReturn(0L);
        flowTaskInstanceUnderTest.bindServiceTask(baseRuntimeFlowableDelegateMock);
        assertEquals(flowTaskInstanceUnderTest.getStatus(), FlowNodeStatus.UNCREATED);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancel() {
        flowTaskInstanceUnderTest.cancel(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDone() {
        flowTaskInstanceUnderTest.isDone();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResult() throws InterruptedException, ExecutionException {
        flowTaskInstanceUnderTest.getResult();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResult_TimeInterval() throws InterruptedException, ExecutionException, TimeoutException {
        flowTaskInstanceUnderTest.getResult(1, TimeUnit.MINUTES);
    }
    
    @Test
    public void testGetTaskType() {
        assertEquals(flowTaskInstanceUnderTest.getTaskType(), mock(TaskType.class));
    }
}