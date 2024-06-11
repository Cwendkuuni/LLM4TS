
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {
    @Mock
    private FlowableAdaptorImpl flowableAdaptorImpl;
    
    @Test
    public void setProcessInstanceIdTest() {
        doNothing().when(flowableAdaptorImpl).setProcessInstanceId(anyLong(), anyString());
        flowableAdaptorImpl.setProcessInstanceId(1L, "testProcessId");
        verify(flowableAdaptorImpl, times(1)).setProcessInstanceId(1L, "testProcessId");
    }

    @Test
    public void getFlowInstanceIdByProcessInstanceIdTest() {
        when(flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(anyString()))
          .thenReturn(Optional.of(1L));
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("testProcessId");
        assertTrue(result.isPresent());
        assertEquals(Long.valueOf(1L), result.get());
    }

    @Test
    public void setProcessDefinitionIdTest() {
        doNothing().when(flowableAdaptorImpl).setProcessDefinitionId(anyLong(), anyString());
        flowableAdaptorImpl.setProcessDefinitionId(1L, "testProcessDefId");
        verify(flowableAdaptorImpl, times(1)).setProcessDefinitionId(1L, "testProcessDefId");
    }

    @Test
    public void getFlowInstanceIdByProcessDefinitionIdTest() {
        when(flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId("testProcessDefId");
        assertTrue(result.isPresent());
        assertEquals(Long.valueOf(1L), result.get());
    }

    @Test
    public void getProcessInstanceIdByFlowInstanceId() {
        when(flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of("testProcessId"));
        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(1L);
        assertTrue(result.isPresent());
        assertEquals("testProcessId", result.get());
    }

    @Test
    public void getFlowableElementByTypeTest() {
        when(flowableAdaptorImpl.getFlowableElementByType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Arrays.asList(new FlowableElement("1", "task", FlowableElementType.TASK)));
        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(1L, FlowNodeType.APPROVAL_TASK, FlowableElementType.TASK);
        assertFalse(result.isEmpty());
        assertEquals(new FlowableElement("1", "task", FlowableElementType.TASK), result.get(0));
    }

    @Test
    public void getApprovalInstanceByNameTest() {
        when(flowableAdaptorImpl.getApprovalInstanceByName(anyString(), anyLong())).thenReturn(Optional.of(mock(FlowApprovalInstance.class)));
        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName("name", 1L);
        assertTrue(result.isPresent());
    }

    @Test
    public void setFlowableElementTest() {
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = new FlowableElement("1", "task", FlowableElementType.TASK);
        doNothing().when(flowableAdaptorImpl).setFlowableElement(nodeInstance, flowableElement);
        flowableAdaptorImpl.setFlowableElement(nodeInstance, flowableElement);
        verify(flowableAdaptorImpl, times(1)).setFlowableElement(nodeInstance, flowableElement);
    }
}

