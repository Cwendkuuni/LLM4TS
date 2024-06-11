package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.service.flow.instance.FlowNodeType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private FlowInstanceEntity flowInstanceEntity;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() throws Exception {
        when(flowInstanceEntity.getId()).thenReturn(1L);
    }

    @Test
    public void testSetProcessInstanceId() {
        flowableAdaptor.setProcessInstanceId(1L, "1");
        verify(flowInstanceRepository,times(1)).updateProcessInstanceIdById(1L, "1");
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        when(flowInstanceRepository.findById(1L)).thenReturn(Optional.of(flowInstanceEntity));
        Optional<Long> longOptional = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("1");
        assertEquals(1L, longOptional.get().longValue());
    }
    
    @Test
    public void testSetProcessDefinitionId() {
        flowableAdaptor.setProcessDefinitionId(1L, "1");
        verify(flowInstanceRepository,times(1)).updateProcessDefinitionIdById(1L, "1");
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        when(flowInstanceEntity.getProcessDefinitionId()).thenReturn("1");
        Optional<Long> longOptional = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("1");
        assertEquals(1L, longOptional.get().longValue());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        when(flowInstanceEntity.getProcessInstanceId()).thenReturn("1");
        Optional<String> stringOptional = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(1L);
        assertEquals("1", stringOptional.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        FlowableElementType elementType = FlowableElementType.START_EVENT;
        list = flowableAdaptor.getFlowableElementByType(1L, FlowNodeType.SERVICE_TASK, elementType);
        assertEquals(0, list.size());
    }
    
    // ...Complete the rest of the tests in a similar manner...
}
