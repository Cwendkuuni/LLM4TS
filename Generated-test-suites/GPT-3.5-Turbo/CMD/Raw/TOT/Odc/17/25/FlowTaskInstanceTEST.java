import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {

    @Test
    public void testCreateFlowTaskInstance() {
        // Typical use case - create a FlowTaskInstance
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        when(entity.getId()).thenReturn(1L);
        when(entity.getOrganizationId()).thenReturn(1L);
        when(entity.getFlowInstanceId()).thenReturn(1L);
        when(entity.isStartEndpoint()).thenReturn(true);
        when(entity.isEndEndpoint()).thenReturn(false);
        when(entity.getStatus()).thenReturn(FlowNodeStatus.PENDING);
        
        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, 
                                                                eventPublisher, taskService, 
                                                                nodeRepository, sequenceRepository, 
                                                                serviceTaskRepository);
        
        assertNotNull(flowTaskInstance);
        assertEquals(1L, flowTaskInstance.getId().longValue());
        assertEquals(1L, flowTaskInstance.getOrganizationId().longValue());
        assertEquals(1L, flowTaskInstance.getFlowInstanceId().longValue());
        assertTrue(flowTaskInstance.isStartEndpoint());
        assertFalse(flowTaskInstance.isEndEndpoint());
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test
    public void testUpdateFlowTaskInstance() {
        // Typical use case - update a FlowTaskInstance
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.setId(1L);
        flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);
        flowTaskInstance.setTargetTaskId(2L);
        flowTaskInstance.setTaskType(TaskType.USER_TASK);

        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        
        int affectRows = 1;
        when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class))).thenReturn(affectRows);
        
        flowTaskInstance.update();
        
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelTaskNotOnMachine() {
        // Error scenario - attempting to cancel a task that is not present on the machine
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.cancel(true);
    }

    @Test
    public void testCompleteTask() {
        // Typical use case - complete a task
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.setStatus(FlowNodeStatus.PENDING);
        TaskService taskService = mock(TaskService.class);
        
        int taskCount = 1;
        when(taskService.createTaskQuery().processInstanceId(anyString()).list()).thenReturn(Collections.nCopies(taskCount, mock(Task.class)));
        
        flowTaskInstance.complete(false);
        
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }
}