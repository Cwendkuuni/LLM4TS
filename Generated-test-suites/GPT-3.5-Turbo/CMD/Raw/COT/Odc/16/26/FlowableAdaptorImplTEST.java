
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    private FlowInstanceEntity mockFlowInstanceEntity;

    @Before
    public void setUp() {
        mockFlowInstanceEntity = new FlowInstanceEntity();
        mockFlowInstanceEntity.setId(1L);
    }

    @Test
    public void testSetProcessInstanceId() {
        Mockito.when(flowInstanceRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(mockFlowInstanceEntity));
        flowableAdaptorImpl.setProcessInstanceId(1L, "processId");
        assertEquals("processId", mockFlowInstanceEntity.getProcessInstanceId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        Mockito.when(flowInstanceRepository.findAll(Mockito.any())).thenReturn(java.util.Arrays.asList(mockFlowInstanceEntity));
        assertTrue(flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("processId").isPresent());
        assertFalse(flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("nonExistingId").isPresent());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Mockito.when(flowInstanceRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(mockFlowInstanceEntity));
        flowableAdaptorImpl.setProcessDefinitionId(1L, "definitionId");
        assertEquals("definitionId", mockFlowInstanceEntity.getProcessDefinitionId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        Mockito.when(flowInstanceRepository.findAll(Mockito.any())).thenReturn(java.util.Arrays.asList(mockFlowInstanceEntity));
        assertTrue(flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId("definitionId").isPresent());
        assertFalse(flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId("nonExistingId").isPresent());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Mockito.when(flowInstanceRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(mockFlowInstanceEntity));
        assertEquals("1", flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(1L).get());
    }

    // Add more test cases for other methods

}
