package com.oceanbase.odc.service.flow.instance;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        flowTaskInstance = new FlowTaskInstance();
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertNotNull(flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testCreate() {
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        flowTaskInstance.create();
        verify(serviceTaskRepository, times(1)).save(any());
    }

    @Test
    public void testUpdate() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setTargetTaskId(2L);
        entity.setTaskType(TaskType.TYPE_A);
        flowTaskInstance.update();
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        assertEquals(2L, flowTaskInstance.getTargetTaskId().longValue());
        assertEquals(TaskType.TYPE_A, flowTaskInstance.getTaskType());
    }

    // Add more test methods for other functionalities

}