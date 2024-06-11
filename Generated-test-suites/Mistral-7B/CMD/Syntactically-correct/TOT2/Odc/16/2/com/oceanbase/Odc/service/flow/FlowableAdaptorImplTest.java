package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
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
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FlowableAdaptorImplTest {

    @Autowired
    private FlowableAdaptor flowableAdaptor;

    @Autowired
    private FlowInstanceRepository flowInstanceRepository;

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Autowired
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    private FlowableAdaptorImpl flowableAdaptorImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        flowableAdaptorImpl = new FlowableAdaptorImpl();
        flowableAdaptorImpl.setFlowInstanceRepository(flowInstanceRepository);
        flowableAdaptorImpl.setNodeInstanceRepository(nodeInstanceRepository);
        flowableAdaptorImpl.setServiceTaskInstanceRepository(serviceTaskInstanceRepository);
        flowableAdaptorImpl.setEventPublisher(eventPublisher);
        flowableAdaptorImpl.setGatewayInstanceRepository(gateWayInstanceRepository);
        flowableAdaptorImpl.setUserTaskInstanceRepository(userTaskInstanceRepository);
        flowableAdaptorImpl.setTaskService(taskService);
        flowableAdaptorImpl.setFormService(formService);
        flowableAdaptorImpl.setAuthenticationFacade(authenticationFacade);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = UUID.randomUUID().toString();

        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

        Optional<FlowInstanceEntity> optional = flowInstanceRepository.findById(flowInstanceId);
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(processInstanceId, optional.get().getProcessInstanceId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = UUID.randomUUID().toString();
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        flowInstanceEntity.setId(1L);
        flowInstanceRepository.save(flowInstanceEntity);

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(1L, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = UUID.randomUUID().toString();

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceRepository.save(flowInstanceEntity);

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.APPROVAL;
        FlowNodeType nodeType = FlowNodeType.APPROVAL_TASK;
        String activityId = UUID.randomUUID().toString();
        String name = "Approval Task";

        List<NodeInstanceEntity> nodeInstanceEntities = List.of(
                new NodeInstanceEntity() {{
                    setInstanceId(instanceId);
                    setInstanceType(nodeType);
                    setFlowableElementType(FlowableElementType.START_EVENT);
                    setName("Start Event");
                    setActivityId("startEvent");
                }},
                new NodeInstanceEntity() {{
                    setInstanceId(instanceId);
                    setInstanceType(nodeType);
                    setFlowableElementType(flowableElementType);
                    setName(name);
                    setActivityId(activityId);
                }},
                new NodeInstanceEntity() {{
                    setInstanceId(instanceId);
                    setInstanceType(nodeType);
                    setFlowableElementType(FlowableElementType.END_EVENT);
                    setName("End Event");
                    setActivityId("endEvent");
                }}
        );

        nodeInstanceRepository.saveAll(nodeInstanceEntities);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, nodeType, flowableElementType);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(name, result.get(0).getName());
        Assertions.assertEquals(activityId, result.get(0).getActivityId());
        Assertions.assertEquals(flowableElementType, result.get(0).getType());

        nodeInstanceRepository.deleteAll();
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = UUID.randomUUID().toString();
        Long flowInstanceId = 1L;

        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setActivityId(activityId);
        serviceTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertInstanceOf(FlowTaskInstance.class, result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = UUID.randomUUID().toString();
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setActivityId(activityId);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertInstanceOf(FlowApprovalInstance.class, result.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "Approval Task";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setName(name);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertInstanceOf(FlowApprovalInstance.class, result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = UUID.randomUUID().toString();
        Long flowInstanceId = 1L;

        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setActivityId(activityId);
        gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertInstanceOf(FlowGatewayInstance.class, result.get());
    }

    @Test
    public void testSetFlowableElement() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.APPROVAL;
        FlowNodeType nodeType = FlowNodeType.APPROVAL_TASK;
        String activityId = UUID.randomUUID().toString();
        String name = "Approval Task";

        BaseFlowNodeInstance baseFlowNodeInstance = new BaseFlowNodeInstance() {{
            setId(instanceId);
            setNodeType(nodeType);
            setFlowInstanceId(1L);
        }};

        FlowableElement flowableElement = new FlowableElement() {{
            setType(flowableElementType);
            setName(name);
            setActivityId(activityId);
        }};

        flowableAdaptor.setFlowableElement(baseFlowNodeInstance, flowableElement);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, nodeType, flowableElementType);

        Assertions.assertEquals(1, nodeInstanceEntities.size());
        Assertions.assertEquals(name, nodeInstanceEntities.get(0).getName());
        Assertions.assertEquals(activityId, nodeInstanceEntities.get(0).getActivityId());
        Assertions.assertEquals(flowableElementType, nodeInstanceEntities.get(0).getFlowableElementType());

        nodeInstanceRepository.deleteAll();
    }
}