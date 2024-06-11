package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.FlowableElement;
import com.oceanbase.odc.service.flow.instance.FlowNodeInstanceRepository;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@Slf4j
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

    @Mock
    private FlowInstanceSpecs flowInstanceSpecs;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    private Long FLOW_INSTANCE_ID = 1L;
    private Long NODE_INSTANCE_ID = 1L;
    private String PROCESS_INSTANCE_ID = "processInstanceId";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        when(flowInstanceRepository.findById(FLOW_INSTANCE_ID)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptorImpl.setProcessInstanceId(FLOW_INSTANCE_ID, PROCESS_INSTANCE_ID);

        ArgumentCaptor<Specification<FlowInstanceEntity>> argument = ArgumentCaptor.forClass(Specification.class);
        verify(flowInstanceRepository).updateProcessInstanceIdById(eq(FLOW_INSTANCE_ID), eq(PROCESS_INSTANCE_ID),
                argument.capture());
        Specification<FlowInstanceEntity> spec = argument.getValue();
        verify(flowInstanceRepository).saveAndFlush(eq(spec.get()));
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        List<FlowInstanceEntity> entityList = Collections.singletonList(new FlowInstanceEntity());
        when(flowInstanceRepository.findAll(eq(flowInstanceSpecs.processInstanceIdEquals(PROCESS_INSTANCE_ID)))).thenReturn(
                entityList);

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(PROCESS_INSTANCE_ID);
        assertEquals(Optional.of(FLOW_INSTANCE_ID), result);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        List<FlowInstanceEntity> entityList = Collections.singletonList(new FlowInstanceEntity());
        when(flowInstanceRepository.findAll(eq(flowInstanceSpecs.processDefinitionIdEquals(PROCESS_INSTANCE_ID)))).thenReturn(
                entityList);

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(PROCESS_INSTANCE_ID);
        assertEquals(Optional.of(FLOW_INSTANCE_ID), result);
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        when(flowInstanceRepository.findById(FLOW_INSTANCE_ID)).thenReturn(Optional.of(new FlowInstanceEntity()));

        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID);
        assertEquals(Optional.of(PROCESS_INSTANCE_ID), result);
    }

    @Test
    public void testGetFlowableElementByType() {
        List<NodeInstanceEntity> nodeList = Collections.singletonList(new NodeInstanceEntity());
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(NODE_INSTANCE_ID,
                FlowNodeType.SERVICE_TASK, FlowableElementType.START_EVENT)).thenReturn(nodeList);

        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(NODE_INSTANCE_ID,
                FlowNodeType.SERVICE_TASK, FlowableElementType.START_EVENT);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, "activityId",
                FLOW_INSTANCE_ID)).thenReturn(Optional.of(new ServiceTaskInstanceEntity()));

        Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId("activityId", FLOW_INSTANCE_ID);
        assertNotNull(result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, "activityId",
                FLOW_INSTANCE_ID)).thenReturn(Optional.of(new UserTaskInstanceEntity()));

        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId("activityId",
                FLOW_INSTANCE_ID);
        assertNotNull(result.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, "name", FLOW_INSTANCE_ID))
                .thenReturn(Optional.of(new UserTaskInstanceEntity()));

        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName("name", FLOW_INSTANCE_ID);
        assertNotNull(result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, "activityId",
                FLOW_INSTANCE_ID)).thenReturn(Optional.of(new GateWayInstanceEntity()));

        Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId("activityId", FLOW_INSTANCE_ID);
        assertNotNull(result.get());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = mock(FlowableElement.class);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstance.getId(),
                nodeInstance.getNodeType(), flowableElement.getType())).thenReturn(Collections.emptyList());

        flowableAdaptorImpl.setFlowableElement(nodeInstance, flowableElement);

        verify(nodeInstanceRepository).save(argThat(nodeInstance -> {
            NodeInstanceEntity node = (NodeInstanceEntity) nodeInstance;
            return Objects.equals(node.getFlowableElementType(), flowableElement.getType())
                    && Objects.equals(node.getName(), flowableElement.getName());
        }));
    }
}