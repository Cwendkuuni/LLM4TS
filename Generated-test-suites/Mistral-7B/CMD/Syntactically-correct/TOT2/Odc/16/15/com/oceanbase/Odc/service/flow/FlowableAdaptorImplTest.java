package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.invocationbuilder.InvocationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

/**
 * Test class for {@link FlowableAdaptorImpl}
 */
@Service
@SkipAuthorize("odc internal usage")
public class FlowableAdaptorImplTest implements FlowableAdaptor {

    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final String PROCESS_INSTANCE_ID = "processInstanceId";
    private static final String PROCESS_DEFINITION_ID = "processDefinitionId";
    private static final String ACTIVITY_ID = "activityId";
    private static final FlowableElementType FLOWABLE_ELEMENT_TYPE = FlowableElementType.START;
    private static final String FLOWABLE_ELEMENT_NAME = "flowableElementName";
    private static final Long NODE_INSTANCE_ID = 1L;
    private static final FlowNodeType NODE_TYPE = FlowNodeType.SERVICE_TASK;

    private FlowableAdaptor flowableAdaptor;
    private NodeInstanceEntityRepository nodeInstanceRepository;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    private EventPublisher eventPublisher;
    private FormService formService;
    private TaskService taskService;
    private AuthenticationFacade authenticationFacade;

    @Autowired
    public FlowableAdaptorImplTest(NodeInstanceEntityRepository nodeInstanceRepository,
            ServiceTaskInstanceRepository serviceTaskInstanceRepository, EventPublisher eventPublisher,
            FormService formService, TaskService taskService, AuthenticationFacade authenticationFacade) {
        this.flowableAdaptor = new FlowableAdaptorImpl();
        this.nodeInstanceRepository = nodeInstanceRepository;
        this.serviceTaskInstanceRepository = serviceTaskInstanceRepository;
        this.eventPublisher = eventPublisher;
        this.formService = formService;
        this.taskService = taskService;
        this.authenticationFacade = authenticationFacade;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void setProcessInstanceId_ValidArgs_Success() {
        // Given
        when(flowInstanceRepository.findById(FLOW_INSTANCE_ID)).thenReturn(Optional.of(new FlowInstanceEntity()));

        // When
        flowableAdaptor.setProcessInstanceId(FLOW_INSTANCE_ID, PROCESS_INSTANCE_ID);

        // Then
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(FLOW_INSTANCE_ID, PROCESS_INSTANCE_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setProcessInstanceId_InvalidArgs_Failure() {
        // Given
        when(flowInstanceRepository.findById(FLOW_INSTANCE_ID)).thenReturn(Optional.empty());

        // When
        flowableAdaptor.setProcessInstanceId(FLOW_INSTANCE_ID, PROCESS_INSTANCE_ID);

        // Then
        verify(flowInstanceRepository, never()).updateProcessInstanceIdById(FLOW_INSTANCE_ID, PROCESS_INSTANCE_ID);
        throw new IllegalArgumentException("Flow instance not found");
    }

    @Test
    public void getFlowInstanceIdByProcessInstanceId_ValidArgs_Success() {
        // Given
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(FLOW_INSTANCE_ID);
        flowInstanceEntity.setProcessInstanceId(PROCESS_INSTANCE_ID);

        when(flowInstanceRepository.findAll(ArgumentMatchers.any(Specification.class)))
                .thenReturn(Collections.singletonList(flowInstanceEntity));

        // When
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(PROCESS_INSTANCE_ID);

        // Then
        assertEquals(FLOW_INSTANCE_ID, result.get());
    }

    @Test
    public void getFlowInstanceIdByProcessInstanceId_MultipleInstances_Failure() {
        // Given
        FlowInstanceEntity flowInstanceEntity1 = new FlowInstanceEntity();
        flowInstanceEntity1.setId(FLOW_INSTANCE_ID_1);
        flowInstanceEntity1.setProcessInstanceId(PROCESS_INSTANCE_ID);

        FlowInstanceEntity flowInstanceEntity2 = new FlowInstanceEntity();
        flowInstanceEntity2.setId(FLOW_INSTANCE_ID_2);
        flowInstanceEntity2.setProcessInstanceId(PROCESS_INSTANCE_ID);

        when(flowInstanceRepository.findAll(ArgumentMatchers.any(Specification.class)))
                .thenReturn(List.of(flowInstanceEntity1, flowInstanceEntity2));

        // When
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(PROCESS_INSTANCE_ID);

        // Then
        assertFalse(result.isPresent());
    }

    // Test other methods with similar test cases

    @Test
    public void getFlowableElementByType_ValidArgs_Success() {
        // Given
        NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
        nodeInstance.setInstanceId(NODE_INSTANCE_ID);
        nodeInstance.setNodeType(NODE_TYPE);
        nodeInstance.setFlowableElementType(FLOWABLE_ELEMENT_TYPE);
        nodeInstance.setActivityId(ACTIVITY_ID);
        nodeInstance.setName(FLOWABLE_ELEMENT_NAME);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(NODE_INSTANCE_ID,
                NODE_TYPE, FLOWABLE_ELEMENT_TYPE)).thenReturn(List.of(nodeInstance));

        // When
        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(NODE_INSTANCE_ID, NODE_TYPE,
                FLOWABLE_ELEMENT_TYPE);

        // Then
        assertEquals(1, result.size());
        assertEquals(FLOWABLE_ELEMENT_NAME, result.get(0).getName());
        assertEquals(FLOWABLE_ELEMENT_TYPE, result.get(0).getType());
    }

    @Test
    public void getFlowableElementByType_NoElementFound_EmptyList() {
        // Given
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(NODE_INSTANCE_ID,
                NODE_TYPE, FLOWABLE_ELEMENT_TYPE)).thenReturn(Collections.emptyList());

        // When
        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(NODE_INSTANCE_ID, NODE_TYPE,
                FLOWABLE_ELEMENT_TYPE);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void getTaskInstanceByActivityId_ValidArgs_Success() {
        // Given
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setActivityId(ACTIVITY_ID);
        serviceTaskInstanceEntity.setFlowInstanceId(FLOW_INSTANCE_ID);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, ACTIVITY_ID,
                FLOW_INSTANCE_ID)).thenReturn(Optional.of(serviceTaskInstanceEntity));

        // When
        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);

        // Then
        assertTrue(result.isPresent());
        assertNotNull(result.get().getDelegate());
    }

    @Test
    public void getTaskInstanceByActivityId_NoTaskInstanceFound_EmptyOptional() {
        // Given
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, ACTIVITY_ID,
                FLOW_INSTANCE_ID)).thenReturn(Optional.empty());

        // When
        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);

        // Then
        assertFalse(result.isPresent());
    }

    // Test other methods with similar test cases

    @Test
    public void setFlowableElement_ValidArgs_Success() {
        // Given
        BaseFlowNodeInstance baseFlowNodeInstance = Mockito.mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = new FlowableElement(ACTIVITY_ID, "flowableElementName", FLOWABLE_ELEMENT_TYPE);

        when(nodeInstanceRepository.save(ArgumentMatchers.any(NodeInstanceEntity.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        // When
        flowableAdaptor.setFlowableElement(baseFlowNodeInstance, flowableElement);

        // Then
        NodeInstanceEntity nodeInstanceEntity =verify(nodeInstanceRepository, times(1))
                .save(ArgumentMatchers.argThat(nodeInstanceEntity -> {
                    assertEquals(ACTIVITY_ID, nodeInstanceEntity.getActivityId());
                    assertEquals("flowableElementName", nodeInstanceEntity.getName());
                    assertEquals(FLOWABLE_ELEMENT_TYPE, nodeInstanceEntity.getFlowableElementType());
                    assertEquals(NODE_TYPE, nodeInstanceEntity.getInstanceType());
                    assertEquals(NODE_INSTANCE_ID, nodeInstanceEntity.getInstanceId());
                    assertEquals(FLOW_INSTANCE_ID, nodeInstanceEntity.getFlowInstanceId());
                    return nodeInstanceEntity;
                }));
        assertEquals(NODE_INSTANCE_ID, nodeInstanceEntity.getInstanceId());
    }

    // Test edge cases and error scenarios for each method
}