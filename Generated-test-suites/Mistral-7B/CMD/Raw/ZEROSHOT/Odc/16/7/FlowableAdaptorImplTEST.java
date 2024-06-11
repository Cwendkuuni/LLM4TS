import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
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
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class FlowableAdaptorImplTest {

    private static final String PROCESS_INSTANCE_ID = "1234567890";
    private static final String PROCESS_DEFINITION_ID = "abcdefg";
    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final String ACTIVITY_ID = "activityId";
    private static final String NAME = "name";
    private static final FlowNodeType NODE_TYPE = FlowNodeType.SERVICE_TASK;
    private static final FlowableElementType FLOWABLE_ELEMENT_TYPE = FlowableElementType.START_EVENT;

    private static final Long SERVICE_TASK_INSTANCE_ID = 1L;
    private static final Long APPROVAL_TASK_INSTANCE_ID = 2L;
    private static final Long GATEWAY_INSTANCE_ID = 3L;

    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private AuthenticationFacade authenticationFacade;
    private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        eventPublisher = Mockito.mock(EventPublisher.class);
        authenticationFacade = new AuthenticationFacadeImpl();
        odcRuntimeDelegateMapper = Mockito.mock(OdcRuntimeDelegateMapper.class);

        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.setEventPublisher(eventPublisher);
        flowableAdaptor.setAuthenticationFacade(authenticationFacade);
        flowableAdaptor.setOdcRuntimeDelegateMapper(odcRuntimeDelegateMapper);
    }

    @Test
    public void testSetProcessInstanceId() {
        flowableAdaptor.setProcessInstanceId(FLOW_INSTANCE_ID, PROCESS_INSTANCE_ID);

        Optional<FlowInstanceEntity> optionalFlowInstanceEntity = Mockito.mock(Optional.class);
        Mockito.when(flowInstanceRepository.findById(FLOW_INSTANCE_ID)).thenReturn(optionalFlowInstanceEntity);

        flowableAdaptor.updateProcessInstanceIdById(FLOW_INSTANCE_ID, PROCESS_INSTANCE_ID);

        Mockito.verify(flowInstanceRepository).updateProcessInstanceIdById(FLOW_INSTANCE_ID, PROCESS_INSTANCE_ID);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        Mockito.when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processInstanceIdEquals(PROCESS_INSTANCE_ID)))).thenReturn(
                Collections.singletonList(new FlowInstanceEntity()));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(PROCESS_INSTANCE_ID);

        Assert.assertEquals(Optional.of(FLOW_INSTANCE_ID), result);
    }

    @Test
    public void testSetProcessDefinitionId() {
        flowableAdaptor.setProcessDefinitionId(FLOW_INSTANCE_ID, PROCESS_DEFINITION_ID);

        Optional<FlowInstanceEntity> optionalFlowInstanceEntity = Mockito.mock(Optional.class);
        Mockito.when(flowInstanceRepository.findById(FLOW_INSTANCE_ID)).thenReturn(optionalFlowInstanceEntity);

        flowableAdaptor.updateProcessDefinitionIdById(FLOW_INSTANCE_ID, PROCESS_DEFINITION_ID);

        Mockito.verify(flowInstanceRepository).updateProcessDefinitionIdById(FLOW_INSTANCE_ID, PROCESS_DEFINITION_ID);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        Mockito.when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(PROCESS_DEFINITION_ID)))).thenReturn(
                Collections.singletonList(new FlowInstanceEntity()));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID);

        Assert.assertEquals(Optional.of(FLOW_INSTANCE_ID), result);
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Mockito.when(flowInstanceRepository.findById(FLOW_INSTANCE_ID)).thenReturn(Optional.of(new FlowInstanceEntity()));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID);

        Assert.assertEquals(Optional.of(PROCESS_INSTANCE_ID), result);
    }

    @Test
    public void testGetFlowableElementByType() {
        Mockito.when(nodeRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(FLOW_INSTANCE_ID, NODE_TYPE, FLOWABLE_ELEMENT_TYPE))
                .thenReturn(Collections.singletonList(new NodeInstanceEntity()));

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(FLOW_INSTANCE_ID, NODE_TYPE, FLOWABLE_ELEMENT_TYPE);

        Assert.assertEquals(Collections.singletonList(new FlowableElement(ACTIVITY_ID, NAME, FLOWABLE_ELEMENT_TYPE)), result);
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        Mockito.when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(NODE_TYPE, ACTIVITY_ID, FLOW_INSTANCE_ID))
                .thenReturn(Optional.of(new ServiceTaskInstanceEntity()));

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);

        Assert.assertTrue(result.isPresent());
        FlowTaskInstance flowTaskInstance = result.get();
        Assert.assertNotNull(flowTaskInstance.getServiceTaskInstance());
        Assert.assertNotNull(flowTaskInstance.getRuntimeService());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(NODE_TYPE, ACTIVITY_ID, FLOW_INSTANCE_ID))
                .thenReturn(Optional.of(new UserTaskInstanceEntity()));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);

        Assert.assertTrue(result.isPresent());
        FlowApprovalInstance flowApprovalInstance = result.get();
        Assert.assertNotNull(flowApprovalInstance.getUserTaskInstance());
        Assert.assertNotNull(flowApprovalInstance.getRuntimeTaskService());
        Assert.assertNotNull(flowApprovalInstance.getFormService());
        Assert.assertNotNull(flowApprovalInstance.getAuthenticationFacade());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(NODE_TYPE, NAME, FLOW_INSTANCE_ID))
                .thenReturn(Optional.of(new UserTaskInstanceEntity()));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(NAME, FLOW_INSTANCE_ID);

        Assert.assertTrue(result.isPresent());
        FlowApprovalInstance flowApprovalInstance = result.get();
        Assert.assertNotNull(flowApprovalInstance.getUserTaskInstance());
        Assert.assertNotNull(flowApprovalInstance.getRuntimeTaskService());
        Assert.assertNotNull(flowApprovalInstance.getFormService());
        Assert.assertNotNull(flowApprovalInstance.getAuthenticationFacade());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        Mockito.when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(NODE_TYPE, ACTIVITY_ID, FLOW_INSTANCE_ID))
                .thenReturn(Optional.of(new GateWayInstanceEntity()));

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);

        Assert.assertTrue(result.isPresent());
        FlowGatewayInstance flowGatewayInstance = result.get();
        Assert.assertNotNull(flowGatewayInstance.getGatewayInstance());
        Assert.assertNotNull(flowGatewayInstance.getFlowService());
        Assert.assertNotNull(flowGatewayInstance.getNodeRepository());
        Assert.assertNotNull(flowGatewayInstance.getSequenceRepository());
    }

    @Test
    public void testSetFlowableElement() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(FLOW_INSTANCE_ID);
        nodeInstanceEntity.setInstanceType(NODE_TYPE.name());
        nodeInstanceEntity.setFlowableElementType(FLOWABLE_ELEMENT_TYPE.name());

        Mockito.when(nodeRepository.save(Mockito.any(NodeInstanceEntity.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        flowableAdaptor.setFlowableElement(new FlowTaskInstance(new ServiceTaskInstanceEntity(), odcRuntimeDelegateMapper, null, null, null, null),
                new FlowableElement(ACTIVITY_ID, NAME, FLOWABLE_ELEMENT_TYPE));

        Mockito.verify(nodeRepository).save(nodeInstanceEntity);
    }
}