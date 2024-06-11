package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@Slf4j
@SkipAuthorize
public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    private Long instanceId = 1L;

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Autowired
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Autowired
    private FlowInstanceRepository flowInstanceRepository;

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 2L;
        String processInstanceId = "processInstanceId";
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId,
                Optional.of(new FlowInstanceEntity()));

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Optional<FlowInstanceEntity> optional = flowInstanceRepository.findById(flowInstanceId);
        assertEquals(processInstanceId, optional.get().getProcessInstanceId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        Optional<Long> flowInstanceIdOptional = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertEquals(flowInstanceIdOptional.get(), 1L);
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 2L;
        String processDefinitionId = "processDefinitionId";
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId,
                Optional.of(new FlowInstanceEntity()));

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Optional<FlowInstanceEntity> optional = flowInstanceRepository.findById(flowInstanceId);
        assertEquals(processDefinitionId, optional.get().getProcessDefinitionId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        Optional<Long> flowInstanceIdOptional = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertEquals(flowInstanceIdOptional.get(), 1L);
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        Optional<String> processInstanceIdOptional = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertEquals(processInstanceIdOptional.get(), "processInstanceId");
    }

    @Test
    public void testGetFlowableElementByType() {
        List<FlowableElement> flowableElements = flowableAdaptor.getFlowableElementByType(instanceId, FlowNodeType.SERVICE_TASK,
                FlowableElementType.START_EVENT);
        assertNull(flowableElements);

        List<NodeInstanceEntity> entities = nodeInstanceRepository.saveAll(List.of(
                new NodeInstanceEntity(instanceId, FlowNodeType.SERVICE_TASK, FlowableElementType.START_EVENT, "name"),
                new NodeInstanceEntity(instanceId, FlowNodeType.SERVICE_TASK, FlowableElementType.END_EVENT, "name")
        ));
        List<FlowableElement> flowableElements2 = flowableAdaptor.getFlowableElementByType(instanceId, FlowNodeType.SERVICE_TASK,
                FlowableElementType.START_EVENT);
        assertEquals(1, flowableElements2.size());
        assertEquals(entities.get(0).getId(), flowableElements2.get(0).getId());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        Long flowInstanceId = 1L;
        Optional<FlowTaskInstance> flowTaskInstanceOptional = flowableAdaptor.getTaskInstanceByActivityId("activityId", flowInstanceId);
        assertNull(flowTaskInstanceOptional.get());

        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setActivityId("activityId");
        serviceTaskInstanceEntity.setInstanceId(flowInstanceId);
        serviceTaskInstanceRepository.save(serviceTaskInstanceEntity);

        Optional<FlowTaskInstance> flowTaskInstanceOptional2 = flowableAdaptor.getTaskInstanceByActivityId("activityId", flowInstanceId);
        assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstanceOptional2.get().getNodeType());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testGetApprovalInstanceByActivityIdWhenApprovalInstanceDoesNotExist() {
        Long flowInstanceId = 1L;
        Optional<FlowApprovalInstance> flowApprovalInstanceOptional = flowableAdaptor.getApprovalInstanceByActivityId("activityId", flowInstanceId);
        assertNull(flowApprovalInstanceOptional.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        Long flowInstanceId = 1L;
        Optional<FlowApprovalInstance> flowApprovalInstanceOptional = flowableAdaptor.getApprovalInstanceByName("name", flowInstanceId);
        assertNull(flowApprovalInstanceOptional.get());

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setActivityId("activityId");
        userTaskInstanceEntity.setName("name");
        userTaskInstanceEntity.setInstanceId(flowInstanceId);
        userTaskInstanceRepository.save(userTaskInstanceEntity);

        Optional<FlowApprovalInstance> flowApprovalInstanceOptional2 = flowableAdaptor.getApprovalInstanceByName("name", flowInstanceId);
        assertEquals(FlowNodeType.APPROVAL_TASK, flowApprovalInstanceOptional2.get().getNodeType());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        Long flowInstanceId = 1L;
        Optional<FlowGatewayInstance> flowGatewayInstanceOptional = flowableAdaptor.getGatewayInstanceByActivityId("activityId", flowInstanceId);
        assertNull(flowGatewayInstanceOptional.get());

        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setActivityId("activityId");
        gateWayInstanceEntity.setInstanceId(flowInstanceId);
        gateWayInstanceEntity.setInstanceType(FlowNodeType.GATEWAY);
        gateWayInstanceRepository.save(gateWayInstanceEntity);

        Optional<FlowGatewayInstance> flowGatewayInstanceOptional2 = flowableAdaptor.getGatewayInstanceByActivityId("activityId", flowInstanceId);
        assertEquals(FlowNodeType.GATEWAY, flowGatewayInstanceOptional2.get().getNodeType());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 2L;
        Long nodeInstanceFlowableElementTypeId = 1L;
        FlowNodeType nodeInstanceNodeType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;

        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", instanceId,
                Optional.of(new NodeInstanceEntity()));

        flowableAdaptor.setFlowableElement(new BaseFlowNodeInstance() {
            @Override
            public Long getId() {
                return nodeInstanceId;
            }

            @Override
            public NodeType getNodeType() {
                return nodeInstanceNodeType;
            }

            @Override
            public Long getFlowInstanceId() {
                return instanceId;
            }
        }, new FlowableElement() {
            @Override
            public FlowableElementType getType() {
                return flowableElementType;
            }

            @Override
            public String getName() {
                return "name";
            }

            @Override
            public String getActivityId() {
                return "activityId";
            }
        });

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceRepository.findAll();
        NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntities.stream()
                .filter(entity -> Objects.equals(entity.getInstanceId(), instanceId) &&
                        Objects.equals(entity.getNodeType(), nodeInstanceNodeType) &&
                        Objects.equals(entity.getFlowableElementType(), flowableElementType))
                .collect(Collectors.toList()).get(0);
        assertEquals("activityId", nodeInstanceEntity.getActivityId());
    }
}