package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.FlowInstanceSpecs;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
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
import com.oceanbase.odc.service.flow.instance.TestFlowInstance;
import com.oceanbase.odc.service.flow.instance.TestFlowNodeInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@Slf4j
public class FlowableAdaptorImplTest {
    private FlowInstanceRepository flowInstanceRepository;
    private NodeInstanceEntityRepository nodeInstanceRepository;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    private EventPublisher eventPublisher;
    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;
    private GateWayInstanceRepository gateWayInstanceRepository;
    private FlowableAdaptor adaptor;
    private FormService formService;
    private TaskService taskService;
    private AuthenticationFacade authenticationFacade;
    private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

    @Before
    public void setup() {
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
        serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        eventPublisher = mock(EventPublisher.class);
        nodeRepository = mock(NodeInstanceEntityRepository.class);
        sequenceRepository = mock(SequenceInstanceRepository.class);
        gateWayInstanceRepository = mock(GateWayInstanceRepository.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        adaptor = new FlowableAdaptorImpl();
        formService = mock(FormService.class);
        taskService = mock(TaskService.class);
        authenticationFacade = mock(AuthenticationFacade.class);
        odcRuntimeDelegateMapper = mock(OdcRuntimeDelegateMapper.class);

        when(flowInstanceRepository.findById(ArgumentMatchers.anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            if (id == null) {
                return Optional.empty();
            }
            FlowInstanceEntity entity = new FlowInstanceEntity();
            entity.setId(id);
            return Optional.of(entity);
        });

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                ArgumentMatchers.anyLong(), ArgumentMatchers.eq(FlowNodeType.SERVICE_TASK),
                ArgumentMatchers.eq(FlowableElementType.START_EVENT))).thenReturn(Collections.emptyList());
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = RandomStringUtils.randomAlphanumeric(10);

        adaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Verify.verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = RandomStringUtils.randomAlphanumeric(10);
        Long flowInstanceId = 1L;

        when(flowInstanceRepository.findAll(ArgumentMatchers.any(Specification.class))).thenReturn(Collections
                .singletonList(new FlowInstanceEntity()));

        Optional<Long> result = adaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        Verify.verify(result.isPresent());
        Verify.verify(result.get().equals(flowInstanceId));
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = RandomStringUtils.randomAlphanumeric(10);

        adaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Verify.verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = RandomStringUtils.randomAlphanumeric(10);
        Long flowInstanceId = 1L;

        when(flowInstanceRepository.findAll(ArgumentMatchers.any(Specification.class))).thenReturn(Collections
                .singletonList(new FlowInstanceEntity()));

        Optional<Long> result = adaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        Verify.verify(result.isPresent());
        Verify.verify(result.get().equals(flowInstanceId));
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = RandomStringUtils.randomAlphanumeric(10);

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        Optional<String> result = adaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        Verify.verify(result.isPresent());
        Verify.verify(result.get().equals(processInstanceId));
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        FlowableElementType elementType = FlowableElementType.START_EVENT;

        List<NodeInstanceEntity> entities = Collections.singletonList(new NodeInstanceEntity());
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
                elementType)).thenReturn(entities);

        List<FlowableElement> result = adaptor.getFlowableElementByType(instanceId, nodeType, elementType);

        Verify.verify(result.size() == 1);
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = RandomStringUtils.randomAlphanumeric(10);
        Long flowInstanceId = 1L;

        Optional<ServiceTaskInstanceEntity> serviceTaskInstanceEntityOptional = Optional.of(new ServiceTaskInstanceEntity());
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(serviceTaskInstanceEntityOptional);

        Optional<FlowTaskInstance> result = adaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        Verify.verify(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = RandomStringUtils.randomAlphanumeric(10);
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = Optional.of(new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(userTaskInstanceEntityOptional);

        Optional<FlowApprovalInstance> result = adaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        Verify.verify(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = RandomStringUtils.randomAlphanumeric(10);
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = Optional.of(new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(userTaskInstanceEntityOptional);

        Optional<FlowApprovalInstance> result = adaptor.getApprovalInstanceByName(name, flowInstanceId);

        Verify.verify(result.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = RandomStringUtils.randomAlphanumeric(10);
        Long flowInstanceId = 1L;

        Optional<GateWayInstanceEntity> gateWayInstanceEntityOptional = Optional.of(new GateWayInstanceEntity());
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(gateWayInstanceEntityOptional);

        Optional<FlowGatewayInstance> result = adaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        Verify.verify(result.isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowableElementType elementType = FlowableElementType.START_EVENT;
        String name = RandomStringUtils.randomAlphanumeric(10);

        adaptor.setFlowableElement(new TestFlowNodeInstance(nodeInstanceId, FlowNodeType.USER_TASK),
                new FlowableElement(name, elementType));

        List<NodeInstanceEntity> entities = nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                nodeInstanceId, FlowNodeType.USER_TASK, elementType);
        Verify.verify(entities.size() > 0);
    }
}