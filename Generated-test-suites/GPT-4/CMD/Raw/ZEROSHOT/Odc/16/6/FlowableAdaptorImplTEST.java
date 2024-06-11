import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldSetter;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.*;
import com.oceanbase.odc.service.flow.*;
import com.oceanbase.odc.service.flow.instance.*;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FlowableAdaptorImplTest {

    @InjectMocks
    FlowableAdaptorImpl flowableAdaptorImpl;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;
    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        when(flowInstanceRepository.findById(anyLong())).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessInstanceIdById(anyLong(), anyString())).thenReturn(1);
        flowableAdaptorImpl.setProcessInstanceId(1L, "testId");
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(1L);
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(
                java.util.Collections.singletonList(entity));
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("testId");
        Assert.assertEquals(result.get(), Long.valueOf(1L));
    }

    @Test
    public void testSetProcessDefinitionId() {
        when(flowInstanceRepository.findById(anyLong())).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessDefinitionIdById(anyLong(), anyString())).thenReturn(1);
        flowableAdaptorImpl.setProcessDefinitionId(1L, "testId");
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(1L);
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(
                java.util.Collections.singletonList(entity));
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId("testId");
        Assert.assertEquals(result.get(), Long.valueOf(1L));
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setProcessInstanceId("testId");
        when(flowInstanceRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(1L);
        Assert.assertEquals(result.get(), "testId");
    }

    @Test
    public void testGetFlowableElementByType() {
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(), any()))
                .thenReturn(java.util.Collections.emptyList());
        flowableAdaptorImpl.getFlowableElementByType(1L,null,null);
    }
  
    @Test
    public void testGetTaskInstanceByActivityId() {
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(any(),anyString(),anyLong())).thenReturn(Optional.empty());
        flowableAdaptorImpl.getTaskInstanceByActivityId("test",1L);
    }     
  
    @Test
    public void testGetApprovalInstanceByActivityId() {
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(any(),anyString(),anyLong())).thenReturn(Optional.empty());
        flowableAdaptorImpl.getApprovalInstanceByActivityId("test",1L);
    }   
  
    @Test
    public void testGetApprovalInstanceByName() {
        when(userTaskInstanceRepository.findByInstanceTypeAndName(any(),anyString(),anyLong())).thenReturn(Optional.empty());
        flowableAdaptorImpl.getApprovalInstanceByName("test",1L);
    }   
  
     @Test
    public void testGetGatewayInstanceByActivityId() {
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(any(),anyString(),anyLong())).thenReturn(Optional.empty());
        flowableAdaptorImpl.getGatewayInstanceByActivityId("test",1L);
    }

    @Test
    public void testSetFlowableElement() {
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(), any()))
                .thenReturn(java.util.Collections.emptyList());
        BaseFlowNodeInstance baseFlowNodeInstance = new FlowTaskInstance();
        FieldSetter.setField(baseFlowNodeInstance, baseFlowNodeInstance.getClass().getDeclaredField("num"), 1L);
        flowableAdaptorImpl.setFlowableElement(baseFlowNodeInstance, new FlowableElement("test", "test", null));
    }
}