package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
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
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowableElementTestUtil;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.test.TestUtil;

@SuppressWarnings("unchecked")
public class FlowableAdaptorImplTest {

    private FlowableAdaptor adaptor;
    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;
    private FlowInstanceRepository flowInstanceRepository;
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private TaskService taskService;
    private FormService formService;
    private EventPublisher eventPublisher;
    private GateWayInstanceRepository gateWayInstanceRepository;
    private AuthenticationFacade authenticationFacade;
    private FlowInstanceEntity flowInstanceEntity;

    @Before
    public void setup() {
        this.adaptor = new FlowableAdaptorImpl();
        this.nodeRepository = Mockito.mock(NodeInstanceEntityRepository.class);
        this.sequenceRepository = Mockito.mock(SequenceInstanceRepository.class);
        this.flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        this.userTaskInstanceRepository = Mockito.mock(UserTaskInstanceRepository.class);
        this.taskService = Mockito.mock(TaskService.class);
        this.formService = Mockito.mock(FormService.class);
        this.eventPublisher = Mockito.mock(EventPublisher.class);
        this.gateWayInstanceRepository = Mockito.mock(GateWayInstanceRepository.class);
        this.authenticationFacade = Mockito.mock(AuthenticationFacade.class);

        this.adaptor.setNodeInstanceRepository(this.nodeRepository);
        this.adaptor.setSequenceInstanceRepository(this.sequenceRepository);
        this.adaptor.setFlowInstanceRepository(this.flowInstanceRepository);
        this.adaptor.setUserTaskInstanceRepository(this.userTaskInstanceRepository);
        this.adaptor.setTaskService(this.taskService);
        this.adaptor.setFormService(this.formService);
        this.adaptor.setEventPublisher(this.eventPublisher);
        this.adaptor.setGateWayInstanceRepository(this.gateWayInstanceRepository);
        this.adaptor.setAuthenticationFacade(this.authenticationFacade);

        this.flowInstanceEntity = new FlowInstanceEntity();
        this.flowInstanceEntity.setId(1L);
        this.flowInstanceEntity.setProcessInstanceId("test-process-instance-id");
        this.flowInstanceEntity.setProcessDefinitionId("test-process-definition-id");
        this.flowInstanceRepository.save(this.flowInstanceEntity);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = this.flowInstanceEntity.getId();
        String processInstanceId = "test-process-instance-id";

        this.adaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Optional<FlowInstanceEntity> optional = this.flowInstanceRepository.findById(flowInstanceId);
        assertEquals(processInstanceId, optional.get().getProcessInstanceId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        Optional<Long> flowInstanceIdOptional = this.adaptor.getFlowInstanceIdByProcessInstanceId("test-process-instance-id");
        assertTrue(flowInstanceIdOptional.isPresent());
        assertEquals(this.flowInstanceEntity.getId(), flowInstanceIdOptional.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        Optional<Long> flowInstanceIdOptional = this.adaptor.getFlowInstanceIdByProcessDefinitionId("test-process-definition-id");
        assertTrue(flowInstanceIdOptional.isPresent());
        assertEquals(this.flowInstanceEntity.getId(), flowInstanceIdOptional.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Optional<String> processInstanceIdOptional = this.adaptor.getProcessInstanceIdByFlowInstanceId(this.flowInstanceEntity.getId());
        assertTrue(processInstanceInstanceIdOptional.isPresent());
        assertEquals("test-process-instance-id", processInstanceIdOptional.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        List<FlowableElement> flowableElements = this.adaptor.getFlowableElementByType(this.flowInstanceEntity.getId(),
                FlowNodeType.APPROVAL_TASK, FlowableElementType.START_EVENT);
        assertTrue(flowableElements.isEmpty());

        this.nodeRepository.save(FlowableElementTestUtil.createNodeInstanceEntity(this.flowInstanceEntity.getId(),
                FlowNodeType.APPROVAL_TASK, FlowableElementType.START_EVENT, "test-name", "test-activity-id"));
        flowableElements = this.adaptor.getFlowableElementByType(this.flowInstanceEntity.getId(),
                FlowNodeType.APPROVAL_TASK, FlowableElementType.START_EVENT);
        assertEquals(1, flowableElements.size());
        FlowableElement flowableElement = flowableElements.get(0);
        assertEquals("test-name", flowableElement.getName());
        assertEquals("test-activity-id", flowableElement.getActivityId());
        assertEquals(FlowableElementType.START_EVENT, flowableElement.getType());

        this.nodeRepository.deleteAll();

        this.nodeRepository.save(FlowableElementTestUtil.createNodeInstanceEntity(this.flowInstanceEntity.getId(),
                FlowNodeType.APPROVAL_TASK, FlowableElementType.END_EVENT, "test-name", "test-activity-id"));
        flowableElements = this.adaptor.getFlowableElementByType(this.flowInstanceEntity.getId(),
                FlowNodeType.APPROVAL_TASK, FlowableElementType.END_EVENT);
        assertEquals(1, flowableElements.size());
        flowableElement = flowableElements.get(0);
        assertEquals("test-name", flowableElement.getName());
        assertEquals("test-activity-id", flowableElement.getActivityId());
        assertEquals(FlowableElementType.END_EVENT, flowableElement.getType());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = UUID.randomUUID().toString();
        Long flowInstanceId = this.flowInstanceEntity.getId();

        Optional<FlowTaskInstance> taskInstanceOptional = this.adaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertFalse(taskInstanceOptional.isPresent());

        ServiceTaskInstanceEntity serviceTaskInstanceEntity = TestUtil.createServiceTaskInstanceEntity(activityId,
                flowInstanceId);
        this.serviceTaskInstanceRepository.save(serviceTaskInstanceEntity);
        taskInstanceOptional = this.adaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(taskInstanceOptional.isPresent());
        FlowTaskInstance taskInstance = taskInstanceOptional.get();
        assertEquals(activityId, taskInstance.getActivityId());
        assertEquals(flowInstanceId, taskInstance.getFlowInstanceId());

        this.serviceTaskInstanceRepository.deleteAll();

        UserTaskInstanceEntity userTaskInstanceEntity = TestUtil.createUserTaskInstanceEntity(activityId, flowInstanceId);
        this.userTaskInstanceRepository.save(userTaskInstanceEntity);
        taskInstanceOptional = this.adaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(taskInstanceOptional.isPresent());
        FlowApprovalInstance taskInstance = (FlowApprovalInstance) taskInstanceOptional.get();
        assertEquals(activityId, taskInstance.getActivityId());
        assertEquals(flowInstanceId, taskInstance.getFlowInstanceId());

        this.userTaskInstanceRepository.deleteAll();
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = UUID.randomUUID().toString();
        Long flowInstanceId = this.flowInstanceEntity.getId();

        Optional<FlowApprovalInstance> approvalInstanceOptional = this.adaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);
        assertFalse(approvalInstanceOptional.isPresent());

        UserTaskInstanceEntity userTaskInstanceEntity = TestUtil.createUserTaskInstanceEntity(activityId, flowInstanceId);
        this.userTaskInstanceRepository.save(userTaskInstanceEntity);
        approvalInstanceOptional = this.adaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(approvalInstanceOptional.isPresent());
        FlowApprovalInstance approvalInstance = approvalInstanceOptional.get();
        assertEquals(activityId, approvalInstance.getActivityId());
        assertEquals(flowInstanceId, approvalInstance.getFlowInstanceId());

        this.userTaskInstanceRepository.deleteAll();
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "test-name";
        Long flowInstanceId = this.flowInstanceEntity.getId();

        UserTaskInstanceEntity userTaskInstanceEntity = TestUtil.createUserTaskInstanceEntity(name, flowInstanceId);
        this.userTaskInstanceRepository.save(userTaskInstanceEntity);

        Optional<FlowApprovalInstance> approvalInstanceOptional = this.adaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertTrue(approvalInstanceOptional.isPresent());
        FlowApprovalInstance approvalInstance = approvalInstanceOptional.get();
        assertEquals(name, approvalInstance.getName());
        assertEquals(flowInstanceId, approvalInstance.getFlowInstanceId());

        this.userTaskInstanceRepository.deleteAll();
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = UUID.randomUUID().toString();
        Long flowInstanceId = this.flowInstanceEntity.getId();

        Optional<FlowGatewayInstance> gatewayInstanceOptional = this.adaptor.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);
        assertFalse(gatewayInstanceOptional.isPresent());

        GateWayInstanceEntity gateWayInstanceEntity = TestUtil.createGateWayInstanceEntity(activityId, flowInstanceId);
        this.gateWayInstanceRepository.save(gateWayInstanceEntity);
        gatewayInstanceOptional = this.adaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(gatewayInstanceOptional.isPresent());
        FlowGatewayInstance gatewayInstance = gatewayInstanceOptional.get();
        assertEquals(activityId, gatewayInstance.getActivityId());
        assertEquals(flowInstanceId, gatewayInstance.getFlowInstanceId());

        this.gateWayInstanceRepository.deleteAll();
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowableElement flowableElement = new FlowableElement("test-name", "test-activity-id", FlowableElementType.START_EVENT);

        this.adaptor.setFlowableElement(new BaseFlowNodeInstance(nodeInstanceId, FlowNodeType.APPROVAL_TASK),
                flowableElement);

        Optional<NodeInstanceEntity> nodeInstanceOptional = this.nodeRepository.findById(nodeInstanceId);
        assertEquals(flowableElement.getName(), nodeInstanceOptional.get().getName());
        assertEquals(flowableElement.getType(), nodeInstanceOptional.get().getFlowableElementType());
        assertEquals(flowableElement.getActivityId(), nodeInstanceOptional.get().getActivityId());

        this.nodeRepository.deleteAll();
    }
}