package com.oceanbase.odc.service.flow.instance;


import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class FlowTaskInstanceTest {

    @Test
    public void testFlowTaskInstanceConstructor() {
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        
        when(entity.getTaskType()).thenReturn(TaskType.DUMMY);
        when(entity.getId()).thenReturn(1L);
        when(entity.getOrganizationId()).thenReturn(1L);
        when(entity.getStatus()).thenReturn(FlowNodeStatus.EXECUTING);
        when(entity.getFlowInstanceId()).thenReturn(1L);
        when(entity.getStartEndpoint()).thenReturn(true);
        when(entity.getEndEndpoint()).thenReturn(true);
        
        FlowTaskInstance instance = new FlowTaskInstance(
            entity, convertor, flowableAdaptor, eventPublisher, taskService,
            nodeRepository, sequenceRepository, serviceTaskRepository
        );
        
        assertNotNull(instance);
        assertEquals(1L, instance.getId().longValue());
        assertEquals(TaskType.DUMMY, instance.getTaskType());
        assertEquals(FlowNodeStatus.EXECUTING, instance.getStatus());
    }
}
