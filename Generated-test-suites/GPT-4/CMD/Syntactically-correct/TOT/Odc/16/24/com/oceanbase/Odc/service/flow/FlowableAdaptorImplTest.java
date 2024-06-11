package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class FlowableAdaptorImplTest{
    @InjectMocks
    FlowableAdaptorImpl flowableAdaptor;

    @Mock
    FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 5L;
        String processInstanceId = "abc123";

        // Mock the FlowInstanceEntity.
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(flowInstanceId);
        entity.setProcessInstanceId(processInstanceId);

        // Return the entity when the method findById is called.
        when(flowInstanceRepository.findById(anyLong())).thenReturn(Optional.of(entity));

        // Invoke the method to test.
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        // Verify that the method updateProcessInstanceIdById was called.
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }
}
