package com.oceanbase.odc.service.flow.instance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {
   
    //You would have to mock dependencies of FlowTaskInstance here. Illustratively
    
    @Mock
    NodeInstanceEntityRepository nodeInstanceEntityRepository;
    @Mock
    RuntimeDelegateMapper delegateMapper;
    @Mock
    FlowableAdaptor flowableAdaptor;
    @Mock
    EventPublisher eventPublisher;
    @Mock
    TaskService taskService;
    @Mock
    SequenceInstanceRepository sequenceRepository;
    @Mock
    ServiceTaskInstanceEntity serviceTaskInstanceEntity;
    @Mock
    BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate;
    

    @InjectMocks
    FlowTaskInstance flowTaskInstance;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = new ArrayList<>();
        instances.add(flowTaskInstance);
        FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);
        verify(serviceTaskInstanceRepository).batchCreate(anyList());
    }

    //continue with other methods in a similar fashion
}