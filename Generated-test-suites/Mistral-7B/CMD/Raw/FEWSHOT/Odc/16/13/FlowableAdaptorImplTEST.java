package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
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
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowNodeTypeConstants;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@Slf4j
public class FlowableAdaptorImplTest {

    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;
    private GateWayInstanceRepository gateWayInstanceRepository;
    private NodeInstanceEntityRepository nodeInstanceRepository;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private TaskService taskService;
    private FormService formService;
    private AuthenticationFacade authenticationFacade;
    private EventPublisher eventPublisher;
    private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

    @Before
    public void setup() {
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        gateWayInstanceRepository = mock(GateWayInstanceRepository.class);
        nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
        serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        taskService = mock(TaskService.class);
        formService = mock(FormService.class);
        authenticationFacade = mock(AuthenticationFacade.class);
        eventPublisher = mock(EventPublisher.class);
        odcRuntimeDelegateMapper = mock(OdcRuntimeDelegateMapper.class);

        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.setFlowInstanceRepository(flowInstanceRepository);
        flowableAdaptor.setGateWayInstanceRepository(gateWayInstanceRepository);
        flowableAdaptor.setNodeInstanceRepository(nodeInstanceRepository);
        flowableAdaptor.setServiceTaskInstanceRepository(serviceTaskInstanceRepository);
        flowableAdaptor.setUserTaskInstanceRepository(userTaskInstanceRepository);
        flowableAdaptor.setTaskService(taskService);
        flowableAdaptor.setFormService(formService);
        flowableAdaptor.setAuthenticationFacade(authenticationFacade);
        flowableAdaptor.setEventPublisher(eventPublisher);
        flowableAdaptor.setOdcRuntimeDelegateMapper(odcRuntimeDelegateMapper);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        Mockito.doReturn(Optional.of(new FlowInstanceEntity())).when(flowInstanceRepository).findById(flowInstanceId);

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Mockito.verify(flowInstanceRepository, Mockito.times(1))
                .updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        Long flowInstanceId = 1L;

        Mockito.doReturn(Optional.of(new FlowInstanceEntity().setId(flowInstanceId)))
                .when(flowInstanceRepository).findAll(Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";

        Mockito.doReturn(Optional.of(new FlowInstanceEntity())).when(flowInstanceRepository).findById(flowInstanceId);

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Mockito.verify(flowInstanceRepository, Mockito.times(1))
                .updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        Long flowInstanceId = 1L;

        Mockito.doReturn(Optional.of(new FlowInstanceEntity().setId(flowInstanceId)))
                .when(flowInstanceRepository).findAll(Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId)));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        Mockito.doReturn(Optional.of(new FlowInstanceEntity().setProcessInstanceId(processInstanceId)))
                .when(flowInstanceRepository).findById(flowInstanceId);

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeTypeConstants.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;

        List<NodeInstanceEntity> nodeInstanceEntities = Collections.singletonList(new NodeInstanceEntity());

        Mockito.doReturn(nodeInstanceEntities)
                .when(nodeInstanceRepository)
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                        flowableElementType);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);

        assertFalse(result.isEmpty());
        assertTrue(result.get(0) instanceof FlowableElement);
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        ServiceTaskInstanceEntity serviceTaskInstanceEntity = mock(ServiceTaskInstanceEntity.class);
        Mockito.when(serviceTaskInstanceEntity.getActivityId()).thenReturn(activityId);

        Optional<ServiceTaskInstanceEntity> optionalServiceTaskInstanceEntity = Optional.of(serviceTaskInstanceEntity);

        Mockito.doReturn(optionalServiceTaskInstanceEntity).when(serviceTaskInstanceRepository)
                .findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId);

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertNotNull(result.get().getFlowableElement());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = mock(UserTaskInstanceEntity.class);
        Mockito.when(userTaskInstanceEntity.getName()).thenReturn(name);

        Optional<UserTaskInstanceEntity> optionalUserTaskInstanceEntity = Optional.of(userTaskInstanceEntity);

        Mockito.doReturn(optionalUserTaskInstanceEntity).when(userTaskInstanceRepository)
                .findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(result.isPresent());
        assertNotNull(result.get().getFormService());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = mock(FlowableElement.class);

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setActivityId(flowableElement.getActivityId());
        nodeInstanceEntity.setName(flowableElement.getName());
        nodeInstanceEntity.setFlowableElementType(flowableElement.getType());
        nodeInstanceEntity.setInstanceType(nodeInstance.getNodeType());
        nodeInstanceEntity.setInstanceId(nodeInstance.getId());
        nodeInstanceEntity.setFlowInstanceId(nodeInstance.getFlowInstanceId());

        Mockito.doReturn(Optional.of(nodeInstanceEntity)).when(nodeInstanceRepository)
                .save(Mockito.any(NodeInstanceEntity.class));

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        Mockito.verify(nodeInstanceRepository, Mockito.times(1)).save(Mockito.any(NodeInstanceEntity.class));
    }
}