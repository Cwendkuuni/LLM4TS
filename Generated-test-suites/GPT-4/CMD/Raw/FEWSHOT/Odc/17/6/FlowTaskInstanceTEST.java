package com.oceanbase.odc.service.flow.instance;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;

public class FlowTaskInstanceTest {
    
    @Mock
    TaskService taskService;

    @Mock
    FlowableAdaptor flowableAdaptor;

    @Mock
    RuntimeDelegateMapper delegateConvertor;

    @Mock
    ServiceTaskInstanceRepository repository;

    @Mock
    EventPublisher eventPublisher;
    
    @Test
    public void testFlowTaskInstanceCreation() throws Exception {
        ExecutionStrategyConfig config = new ExecutionStrategyConfig();
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        FlowTaskInstance instance = new FlowTaskInstance(entity, delegateConvertor, 
                flowableAdaptor, eventPublisher, taskService, repository, repository, repository);
        Assert.assertNotNull(instance);
        Mockito.verify(eventPublisher, Mockito.times(1)).publishEvent(Mockito.any(TaskInstanceCreatedEvent.class));
    }
    
    @Test
    public void testFlowTaskInstanceUpdate() throws Exception {
        ExecutionStrategyConfig config = new ExecutionStrategyConfig();
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        FlowTaskInstance instance = new FlowTaskInstance(entity, delegateConvertor, 
                flowableAdaptor, eventPublisher, taskService, repository, repository, repository);
        instance.update();
        Mockito.verify(repository, Mockito.times(1)).update(Mockito.any(ServiceTaskInstanceEntity.class));
    }
    
    // Similar test cases need to be developed for all public methods of the FlowTaskInstance class.
    
}