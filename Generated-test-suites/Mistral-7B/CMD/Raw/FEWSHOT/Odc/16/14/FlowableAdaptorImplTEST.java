package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.assertEmpty;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import com.oceanbase.odc.metadb.flow.FlowInstanceSpecs.FlowInstanceSpecifications;
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
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
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
    private FlowInstanceEntity flowInstance;

    @Mock
    private FlowInstanceSpecifications flowInstanceSpecifications;

    @Mock
    private ServiceTaskInstanceEntity serviceTaskInstance;

    @Mock
    private UserTaskInstanceEntity userTaskInstance;

    @Mock
    private NodeInstanceEntity nodeInstance;

    @Mock
    private GateWayInstanceEntity gateWayInstance;

    @Mock
    private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Before
    public void setUp() {
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(),
                eq(FlowNodeType.SERVICE_TASK), any(FlowableElementType.class))).thenReturn(Collections.emptyList());
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(),
                eq(FlowNodeType.APPROVAL_TASK), any(FlowableElementType.class))).thenReturn(Collections.emptyList());
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(),
                eq(FlowNodeType.GATEWAY), any(FlowableElementType.class))).thenReturn(Collections.emptyList());
        when(flowInstanceRepository.findById(anyLong())).thenReturn(Optional.of(flowInstance));
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), anyString()))
                .thenReturn(Optional.of(userTaskInstance));
        when(userTaskInstanceRepository.findByInstanceTypeAndName(any(FlowNodeType.class), anyString(),
                anyLong())).thenReturn(Optional.of(userTaskInstance));
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), anyString(),
                anyLong())).thenReturn(Optional.of(gateWayInstance));
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), anyString(),
                anyLong())).thenReturn(Optional.of(serviceTaskInstance));
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());
        when(flowInstanceRepository.findById(anyLong())).thenReturn(Optional.of(flowInstance));
        when(flowInstanceRepository.updateProcessInstanceIdById(anyLong(), anyString())).thenReturn(1);
        when(flowInstanceRepository.updateProcessDefinitionIdById(anyLong(), anyString())).thenReturn(1);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);
        assertEquals(processInstanceId, flowInstance.getProcessInstanceId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        Long flowInstanceId = 1L;
        when(flowInstanceRepository.findAll(flowInstanceSpecifications.processInstanceIdEquals(processInstanceId)))
                .thenReturn(List.of(flowInstance));
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        assertEquals(processDefinitionId, flowInstance.getProcessDefinitionId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        Long flowInstanceId = 1L;
        when(flowInstanceRepository.findAll(flowInstanceSpecifications.processDefinitionIdEquals(processDefinitionId)))
                .thenReturn(List.of(flowInstance));
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstance));
        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START;
        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(instanceId, nodeType,
                flowableElementType);
        assertEmpty(result);
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstance));
        Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId,
                flowInstanceId);
        assertNotNull(result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstance));
        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);
        assertNotNull(result.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstance));
        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName(name, flowInstanceId);
        assertNotNull(result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstance));
        Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);
        assertNotNull(result.get());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.END);
        flowableAdaptorImpl.setFlowableElement(nodeInstance, flowableElement);
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setActivityId(flowableElement.getActivityId());
        nodeInstanceEntity.setName(flowableElement.getName());
        nodeInstanceEntity.setFlowableElementType(flowableElement.getType());
        nodeInstanceEntity.setInstanceType(nodeInstance.getNodeType());
        nodeInstanceEntity.setInstanceId(nodeInstance.getId());
        nodeInstanceEntity.setFlowInstanceId(nodeInstance.getFlowInstanceId());
        verify(nodeInstanceRepository).save(nodeInstanceEntity);
    }
}