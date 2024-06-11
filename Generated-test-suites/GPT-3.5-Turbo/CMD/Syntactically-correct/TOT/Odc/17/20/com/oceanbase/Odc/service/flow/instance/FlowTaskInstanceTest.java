import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        flowTaskInstance = mock(FlowTaskInstance.class);
    }

    @Test
    public void testCreateFlowTaskInstance() {
        // Typical use case
        FlowTaskInstance instance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), convertor, flowableAdaptor,
            eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        assertNotNull(instance);
        
        // Edge case
        instance.setTargetTaskId(1L);
        assertEquals(1L, instance.getTargetTaskId().longValue());

        // Error scenario
        assertThrows(NullPointerException.class, () -> new FlowTaskInstance(null, convertor, flowableAdaptor,
            eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository));
    }

    @Test
    public void testBatchCreateFlowTaskInstance() {
        // Typical use case
        List<FlowTaskInstance> instances = new ArrayList<>();
        instances.add(flowTaskInstance);
        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);
        assertEquals(instances.size(), createdInstances.size());
        
        // Edge case
        assertEquals(instances.get(0).getId(), createdInstances.get(0).getId());
        
        // Error scenario
        assertTrue(createdInstances.isEmpty());
    }

    @Test
    public void testBindServiceTask() {
        // Typical use case
        BaseRuntimeFlowableDelegate target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
        
        // Edge case
        assertNotNull(flowTaskInstance.getTargetTaskHandle());
        
        // Error scenario
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.bindServiceTask(null));
    }

    @Test
    public void testCancel() {
        // Typical use case
        BaseRuntimeFlowableDelegate target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.cancel(true);
        verify(target).cancel(true);
        
        // Edge case
        assertDoesNotThrow(() -> flowTaskInstance.cancel(false));
        
        // Error scenario
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.cancel(false));
    }

    @Test
    public void testIsDone() {
        // Typical use case
        BaseRuntimeFlowableDelegate target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        when(target.isDone()).thenReturn(true);
        assertTrue(flowTaskInstance.isDone());
        
        // Edge case
        when(target.isDone()).thenReturn(false);
        assertFalse(flowTaskInstance.isDone());
        
        // Error scenario
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.isDone());
    }

}