package com.oceanbase.odc.service.flow.instance;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {

    @Test
    public void testBindServiceTask() {
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        
        flowTaskInstance.bindServiceTask(target);
        
        assertEquals(target, flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testBatchCreate() {
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        List<FlowTaskInstance> instances = new ArrayList<>();
        instances.add(flowTaskInstance);
        
        when(serviceTaskInstanceRepository.batchCreate(instances)).thenReturn(instances);
        
        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);
        
        assertEquals(instances, createdInstances);
    }

    @Test
    public void testCreate() {
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        flowTaskInstance.create();
        
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testUpdate() {
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        flowTaskInstance.update();
        
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

}