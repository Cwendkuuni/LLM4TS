package com.oceanbase.odc.service.flow.test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowNodeTypeEnum;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.repository.ServiceTaskInstanceRepository;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private NodeInstanceEntityRepository nodeRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private FormService formService;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "process_instance_id";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId)).thenReturn(1);

        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository, times(1)).findById(flowInstanceId);
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "process_instance_id";
        FlowInstanceEntity flowInstanceEntity1 = new FlowInstanceEntity();
        flowInstanceEntity1.setProcessInstanceId(processInstanceId);
        FlowInstanceEntity flowInstanceEntity2 = new FlowInstanceEntity();

        List<FlowInstanceEntity> flowInstanceEntityList = Arrays.asList(flowInstanceEntity1, flowInstanceEntity2);
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(flowInstanceEntityList);

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        verify(flowInstanceRepository, times(1)).findAll(any(Specification.class));
        Assert.assertEquals(Optional.of(flowInstanceEntity1.getId()), result);
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "process_definition_id";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
        when(flowInstanceRepository.updateProcessDefinitionIdById(flowInstanceId, processDefinitionId)).thenReturn(1);

        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        verify(flowInstanceRepository, times(1)).findById(flowInstanceId);
        verify(flowInstanceRepository, times(1)).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "process_definition_id";
        FlowInstanceEntity flowInstanceEntity1 = new FlowInstanceEntity();
        flowInstanceEntity1.setProcessDefinitionId(processDefinitionId);
        FlowInstanceEntity flowInstanceEntity2 = new FlowInstanceEntity();

        List<FlowInstanceEntity> flowInstanceEntityList = Arrays.asList(flowInstanceEntity1, flowInstanceEntity2);
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(flowInstanceEntityList);

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        verify(flowInstanceRepository, times(1)).findAll(any(Specification.class));
        Assert.assertEquals(Optional.of(flowInstanceEntity1.getId()), result);
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "process_instance_id";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        flowInstanceEntity.setId(flowInstanceId);

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        verify(flowInstanceRepository, times(1)).findById(flowInstanceId);
        Assert.assertEquals(Optional.of(processInstanceId), result);
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeTypeEnum.START;
        FlowableElementType flowableElementType = FlowableElementType.END;
        List<NodeInstanceEntity> nodeInstanceEntityList = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(nodeInstanceEntityList);

        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);

        verify(nodeInstanceRepository, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType);
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activity_id";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setActivityId(activityId);
        serviceTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId, flowInstanceId);

        verify(serviceTaskInstanceRepository, times(1)).findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK,
                activityId, flowInstanceId);
        Assert.assertNotNull(result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activity_id";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setActivityId(activityId);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);

        verify(userTaskInstanceRepository, times(1)).findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK,
                activityId, flowInstanceId);
        Assert.assertNotNull(result.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setName(name);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName(name, flowInstanceId);

        verify(userTaskInstanceRepository, times(1)).findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name,
                flowInstanceId);
        Assert.assertNotNull(result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activity_id";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setActivityId(activityId);
        gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);

        verify(gateWayInstanceRepository, times(1)).findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId);
        Assert.assertNotNull(result.get());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowNodeType nodeType = FlowNodeTypeEnum.START;
        FlowableElementType flowableElementType = FlowableElementType.END;
        FlowableElement flowableElement = new FlowableElement("activity_id", "name", flowableElementType);
        BaseFlowNodeInstance baseFlowNodeInstance = new BaseFlowNodeInstance();
        baseFlowNodeInstance.setId(nodeInstanceId);
        baseFlowNodeInstance.setNodeType(nodeType);
        baseFlowNodeInstance.setFlowInstanceId(nodeInstanceId);

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setActivityId("activity_id");
        nodeInstanceEntity.setName("name");
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        nodeInstanceEntity.setInstanceId(nodeInstanceId);
        nodeInstanceEntity.setInstanceType(nodeType.getType());
        nodeInstanceEntity.setFlowInstanceId(nodeInstanceId);

        doAnswer(i -> {
            nodeInstanceRepository.save(nodeInstanceEntity);
            return null;
        }).when(nodeInstanceRepository).save(any());

        flowableAdaptorImpl.setFlowableElement(baseFlowNodeInstance, flowableElement);

        verify(nodeInstanceRepository, times(1)).save(nodeInstanceEntity);
    }
}