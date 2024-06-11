package com.oceanbase.odc.service.flow.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {
    ServiceTaskInstanceRepository mockServiceTaskInstanceRepository;
    FlowTaskInstance flowTaskInstance;

    @Before
    public void setup() {
        // Here, we setup mock objects and initialize the flowTaskInstance
        mockServiceTaskInstanceRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
        // initialize your flowTaskInstance here...
    }

    @Test
    public void testBatchCreateWithNull() {
        assertTrue(FlowTaskInstance.batchCreate(null, mockServiceTaskInstanceRepository).isEmpty());
    }

    @Test
    public void testBatchCreateWithEmptyList() {
        assertTrue(FlowTaskInstance.batchCreate(new ArrayList<>(), mockServiceTaskInstanceRepository).isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testBatchCreateWithNullEntity() {
        List<FlowTaskInstance> listWithNulls = new ArrayList<>();
        listWithNulls.add(null);
        FlowTaskInstance.batchCreate(listWithNulls, mockServiceTaskInstanceRepository);
    }

    @Test
    public void testIsDone() {
        try {
            flowTaskInstance.isDone();
        }
        catch (UnsupportedOperationException e){
            assertEquals("Task isn't on this machine", e.getMessage());
        }
    }
}