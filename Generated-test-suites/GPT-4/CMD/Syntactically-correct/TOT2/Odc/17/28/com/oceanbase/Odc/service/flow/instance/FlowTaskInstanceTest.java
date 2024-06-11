package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;


public class FlowTaskInstanceTest {

    private TaskService taskServiceMock;
    private ServiceTaskInstanceRepository serviceTaskRepositoryMock;
    private EventPublisher eventPublisherMock;
    private RuntimeDelegateMapper delegateConverterMock;
    private BaseRuntimeFlowableDelegate targetTaskHandleMock;
    private ServiceTaskReturnEntity entityMock;
    private FlowTaskInstance flowtaskInstance;

    @Before
    public void setUp() {
        taskServiceMock = mock(TaskService.class);
        serviceTaskRepositoryMock = mock(ServiceTaskInstanceRepository.class);
        eventPublisherMock = mock(EventPublisher.class);
        delegateConverterMock = mock(RuntimeDelegateMapper.class);
        targetTaskHandleMock = mock(BaseRuntimeFlowableDelegate.class);
        entityMock = mock(ServiceTaskReturnEntity.class);

        flowtaskInstance = new FlowTaskInstance(
            entityMock,
            delegateConverterMock, 
            flowableAdaptorMock, 
            eventPublisherMock, 
            taskServiceMock, 
            mock(NodeInstanceEntityRepository.class), 
            mock(SequenceInstanceRepository.class), 
            serviceTaskRepositoryMock);

        when(entityMock.getId()).thenReturn(1L);
        when(delegateConverterMock.map(Mockito.any())).thenReturn((Class<? extends BaseRuntimeFlowableDelegate<?>>) targetTaskHandleMock.getClass());
    }

    // Typical use case
    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> expected = targetTaskHandleMock.getClass();
        Class<? extends BaseRuntimeFlowableDelegate<?>> actual = flowtaskInstance.getTargetDelegateClass();
        assertEquals(expected, actual);
    }

    //Edge Case: Updating non-existing task instance causes IllegalStateException
    @Test(expected = IllegalStateException.class)
    public void testUpdateNonExistingTaskInstance() {
      when(serviceTaskRepositoryMock.update(any(ServiceTaskInstanceEntity.class))).thenReturn(0);
      flowtaskInstance.update();
    }

    //Error scenario: Abort operation is unsupported for automatic tasks, should throw UnsupportedOperationException
    @Test(expected = UnsupportedOperationException.class)
    public void testAbortOnAutomaticTask() {
      when(entityMock.getStrategy()).thenReturn(FlowTaskExecutionStrategy.AUTO);
      flowtaskInstance.abort();
    }

}

