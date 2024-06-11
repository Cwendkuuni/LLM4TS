package com.oceanbase.odc.service.flow.instance;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.flowable.task.api.Task;
import java.util.Arrays;
import java.util.Optional;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    @Mock
    TaskService taskService;

    @Mock
    EventPublisher eventPublisher;

    @Mock
    RuntimeDelegateMapper delegateConvertor;

    @Mock
    ServiceTaskInstanceRepository serviceTaskRepository;

    @Mock
    ServiceTaskInstanceEntity serviceTaskInstanceEntity;

    @InjectMocks
    FlowTaskInstance flowTaskInstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFlowTaskInstanceCreate() {
        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenReturn(serviceTaskInstanceEntity);
        flowTaskInstance.create();
        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testFlowTaskInstanceUpdate() {
        when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class))).thenReturn(1);
        flowTaskInstance.update();
        verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test(expected = UnsupportedOperationException.class) 
    public void testFlowTaskInstanceIsDoneThrowsExceptionWhenTaskIsNotPresent() {
        flowTaskInstance.isDone();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFlowTaskInstanceGetResultThrowsExceptionWhenTaskIsNotPresent() throws InterruptedException, ExecutionException {
        flowTaskInstance.getResult();
    }

    @Test
    public void testFlowTaskInstanceAlloc() {
        flowTaskInstance.alloc();
        verify(eventPublisher, times(1)).addEventListener(any(ServiceTaskStartedListener.class));
    }

    @Test
    public void testFlowTaskInstanceDealloc() {
        flowTaskInstance.alloc(); // Call alloc first to initialize listener
        flowTaskInstance.dealloc();
        verify(eventPublisher, times(1)).removeEventListener(any(ServiceTaskStartedListener.class));
    }

    @Test
    public void testGettingTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> delegateClass = flowTaskInstance.getTargetDelegateClass();
        Assert.assertNotNull(delegateClass);
    }

}
