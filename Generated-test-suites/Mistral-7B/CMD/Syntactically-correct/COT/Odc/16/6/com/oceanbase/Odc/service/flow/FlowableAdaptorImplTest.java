package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.FlowInstanceSpecs;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowNodeInstance;
import com.oceanbase.odc.service.flow.FlowableElementInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

/**
 * Test class for {@link FlowableAdaptorImpl}
 *
 * @author yh263208
 * @date 2022-02-17 20:16
 * @since ODC_release_3.3.0
 */
public class FlowableAdaptorImplTest {

    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository instanceRepository;
    private GateWayInstanceRepository gatewayRepository;
    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private AuthenticationFacade authenticationFacade;
    private OdcRuntimeDelegateMapper delegateMapper;

    @Before
    public void setup() {
        instanceRepository = Mockito.mock(FlowInstanceRepository.class);
        gatewayRepository = Mockito.mock(GateWayInstanceRepository.class);
        nodeRepository = Mockito.mock(NodeInstanceEntityRepository.class);
        sequenceRepository = Mockito.mock(SequenceInstanceRepository.class);
        serviceTaskInstanceRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
        userTaskInstanceRepository = Mockito.mock(UserTaskInstanceRepository.class);
        authenticationFacade = Mockito.mock(AuthenticationFacade.class);
        delegateMapper = Mockito.mock(OdcRuntimeDelegateMapper.class);
        flowableAdaptor = new FlowableAdaptorImpl(instanceRepository, gatewayRepository, nodeRepository,
                sequenceRepository, serviceTaskInstanceRepository, userTaskInstanceRepository,
                authenticationFacade, delegateMapper);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        Mockito.verify(instanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(1L);
        entity.setProcessInstanceId(processInstanceId);
        Mockito.when(instanceRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(Collections.singletonList(entity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(1L);
        entity.setProcessDefinitionId(processDefinitionId);
        Mockito.when(instanceRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(Collections.singletonList(entity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        Mockito.when(instanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        Mockito.when(instanceRepository.findById(flowInstanceId).get().getProcessInstanceId())
                .thenReturn(processInstanceId);

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<NodeInstanceEntity> entities = Collections.singletonList(new NodeInstanceEntity());
        Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                nodeType, flowableElementType))
                .thenReturn(entities);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, nodeType,
                flowableElementType);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        Mockito.when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK,
                activityId, flowInstanceId)).thenReturn(Optional.of(entity));

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId,
                flowInstanceId);
        assertTrue(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK,
                activityId, flowInstanceId)).thenReturn(Optional.of(entity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);
        assertTrue(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name,
                flowInstanceId)).thenReturn(Optional.of(entity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertTrue(result.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity entity = new GateWayInstanceEntity();
        Mockito.when(gatewayRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(entity));

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);
        assertTrue(result.isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.END_EVENT;
        FlowableElement flowableElement = new FlowableElement("activityId", "name", flowableElementType);
        FlowNodeInstance nodeInstance = Mockito.mock(BaseFlowNodeInstance.class);
        Mockito.when(nodeInstance.getId()).thenReturn(nodeInstanceId);
        Mockito.when(nodeInstance.getNodeType()).thenReturn(FlowNodeType.USER_TASK);

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        Mockito.verify(nodeRepository, Mockito.times(1))
                .save(Mockito.argThat(arg -> Objects.equals(arg.getFlowableElementType(), flowableElementType)
                        && Objects.equals(arg.getInstanceId(), nodeInstanceId)
                        && Objects.equals(arg.getName(), flowableElement.getName())
                        && Objects.equals(arg.getActivityId(), flowableElement.getActivityId())));
    }

    @Test
    public void testEmptyList_getFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<NodeInstanceEntity> entities = Collections.emptyList();

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, nodeType,
                flowableElementType);
        assertEquals(0, result.size());
    }

    @Test
    public void testNullNodeInstance_setFlowableElement() {
        BaseFlowNodeInstance nodeInstance = null;
        FlowableElement flowableElement = Mockito.mock(FlowableElement.class);

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        Mockito.verify(nodeRepository, Mockito.never()).save(Mockito.any(NodeInstanceEntity.class));
    }

    @Test
    public void testNullFlowableElement_setFlowableElement() {
        BaseFlowNodeInstance nodeInstance = Mockito.mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = null;

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        Mockito.verify(nodeRepository, Mockito.never()).save(Mockito.any(NodeInstanceEntity.class));
    }
}