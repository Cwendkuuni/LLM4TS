
package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlowableAdaptorImplTest {

    @Autowired
    private FlowableAdaptorImpl flowableAdaptor;

    @MockBean
    private FlowInstanceRepository flowInstanceRepository;
    
    // Mock other repositories as needed

    @Before
    public void setUp() {
    }

    @Test
    public void setProcessInstanceId_ValidData_Success() {
        // Given
        Long flowInstanceId = 1234L;
        String processInstanceId = "testId";
        FlowInstanceEntity instanceEntity = new FlowInstanceEntity();
        instanceEntity.setId(flowInstanceId);

        // When
        when(flowInstanceRepository.findById(anyLong())).thenReturn(Optional.of(instanceEntity));
        when(flowInstanceRepository.updateProcessInstanceIdById(anyLong(), anyString())).thenReturn(1);

        // Do test
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setProcessInstanceId_NullFlowInstanceId_ThrowsException() {
        flowableAdaptor.setProcessInstanceId(null, "processId");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setProcessInstanceId_NullProcessInstanceId_ThrowsException() {
        flowableAdaptor.setProcessInstanceId(1L, null);
    }

    @Test
    public void getFlowInstanceIdByProcessInstanceId_ValidData_Success() {
        // Given
        String processInstanceId = "testId";
        Long expectedFlowInstanceId = 123L;
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(expectedFlowInstanceId);
        List<FlowInstanceEntity> entities = new ArrayList<>();
        entities.add(flowInstanceEntity);

        // When
        when(flowInstanceRepository.findAll(any())).thenReturn(entities);

        // Do test
        Optional<Long> flowInstanceId =
            flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        // Then
        assertTrue(flowInstanceId.isPresent());
        assertEquals(expectedFlowInstanceId, flowInstanceId.get());
    }

    // Similar tests for all other public methods in the class,
    // including edge cases and exceptions (Null/empty values, invalid inputs, etc.)

}
