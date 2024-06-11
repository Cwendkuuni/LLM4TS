package com.oceanbase.odc.service.flow.instance;

import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    @Test
    public void testFlowTaskInstanceMethods() {
        // Mock required objects
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = mock(ServiceTaskInstanceEntity.class);
        RuntimeDelegateMapper runtimeDelegateMapper = mock(RuntimeDelegateMapper.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceInstanceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);

        // Create FlowTaskInstance object
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(
                serviceTaskInstanceEntity,
                runtimeDelegateMapper,
                flowableAdaptor,
                eventPublisher,
                taskService,
                nodeInstanceEntityRepository,
                sequenceInstanceRepository,
                serviceTaskInstanceRepository
        );

        // Test create method
        flowTaskInstance.create();
        verify(serviceTaskInstanceRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));

        // Test update method
        flowTaskInstance.update();
        verify(serviceTaskInstanceRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));

        // Test delete method
        flowTaskInstance.delete();
        verify(serviceTaskInstanceRepository, times(1)).deleteById(anyLong());

        // Exception handling scenarios
        try {
            flowTaskInstance.cancel(true);
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Task isn't on this machine", e.getMessage());
        }

        try {
            flowTaskInstance.isDone();
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Task isn't on this machine", e.getMessage());
        }

        try {
            flowTaskInstance.getResult();
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Task isn't on this machine", e.getMessage());
        }

        try {
            flowTaskInstance.getResult(1, TimeUnit.MINUTES);
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Task isn't on this machine", e.getMessage());
        }
    }
}
