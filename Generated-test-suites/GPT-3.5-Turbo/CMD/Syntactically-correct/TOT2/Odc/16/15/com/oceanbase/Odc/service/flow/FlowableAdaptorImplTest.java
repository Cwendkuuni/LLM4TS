import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "123456";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId)).thenReturn(1);

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "123456";
        FlowInstanceEntity entity = new FlowInstanceEntity();
        List<FlowInstanceEntity> entityList = Collections.singletonList(entity);

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(entityList);

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertTrue(result.isPresent());
        assertEquals(entity.getId(), result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "7891011";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessDefinitionIdById(flowInstanceId, processDefinitionId)).thenReturn(1);

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "7891011";
        FlowInstanceEntity entity = new FlowInstanceEntity();
        List<FlowInstanceEntity> entityList = Collections.singletonList(entity);

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(entityList);

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertTrue(result.isPresent());
        assertEquals(entity.getId(), result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setProcessInstanceId("123456");

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(entity));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(entity.getProcessInstanceId(), result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.APPROVAL_TASK;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setActivityId("activity123");
        entity.setName("Task1");
        entity.setFlowableElementType(flowableElementType);

        List<NodeInstanceEntity> entityList = Collections.singletonList(entity);

        when(flowableAdaptor.nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(entityList);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);
        assertFalse(result.isEmpty());
        assertEquals(entity.getFlowableElementType(), result.get(0).getType());
        assertEquals(entity.getName(), result.get(0).getName());
        assertEquals(entity.getActivityId(), result.get(0).getActivityId());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "task123";
        Long flowInstanceId = 1L;

        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setActivityId(activityId);

        when(flowableAdaptor.serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(entity));

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "approval123";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setActivityId(activityId);

        when(flowableAdaptor.userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(entity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "approvalTask1";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setName(name);

        when(flowableAdaptor.userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(entity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "gateway123";
        Long flowInstanceId = 1L;

        GateWayInstanceEntity entity = new GateWayInstanceEntity();
        entity.setActivityId(activityId);

        when(flowableAdaptor.gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId))
                .thenReturn(Optional.of(entity));

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = new FlowableElement("activity123", "Task1", FlowableElementType.TASK);

        when(flowableAdaptor.nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Collections.emptyList());

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        verify(flowableAdaptor.nodeInstanceRepository).save(any(NodeInstanceEntity.class));
    }
}