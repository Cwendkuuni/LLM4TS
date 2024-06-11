package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.PreConditions;
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
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Test class for {@link FlowableAdaptorImpl}
 *
 * @author yh263208
 * @date 2022-02-17 20:16
 * @since ODC_release_3.3.0
 */
@Slf4j
public class FlowableAdaptorImplTest {

    @Mock
    private FlowInstanceRepository flowInstanceRepository;
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
    private GateWayInstanceRepository gateWayInstanceRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private FormService formService;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        verifySetProcessInstanceId();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessInstanceIdWithNullFlowInstanceId() {
        String processInstanceId = "processInstanceId";
        flowableAdaptor.setProcessInstanceId(null, processInstanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessInstanceIdWithNullProcessInstanceId() {
        Long flowInstanceId = 1L;
        flowableAdaptor.setProcessInstanceId(flowInstanceId, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessInstanceIdWithNullFlowInstanceEntity() {
        Long flowInstanceId = 1L;
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.empty());
        flowableAdaptor.setProcessInstanceId(flowInstanceId, "processInstanceId");
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))).thenReturn(Collections.singletonList(flowInstanceEntity));
        Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        verifyGetFlowInstanceIdByProcessInstanceId(flowInstanceId);
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        verifySetProcessDefinitionId();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessDefinitionIdWithNullFlowInstanceId() {
        String processDefinitionId = "processDefinitionId";
        flowableAdaptor.setProcessDefinitionId(null, processDefinitionId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessDefinitionIdWithNullProcessDefinitionId() {
        Long flowInstanceId = 1L;
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, null);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);
        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId)))).thenReturn(Collections.singletonList(flowInstanceEntity));
        Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        verifyGetFlowInstanceIdByProcessDefinitionId(flowInstanceId);
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.findById(flowInstanceId).get()).thenReturn(new FlowInstanceEntity());
        when(flowInstanceRepository.getById(flowInstanceId).getProcessInstanceId()).thenReturn(processInstanceId);
        Optional<String> processInstanceIdOptional = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        verifyGetProcessInstanceIdByFlowInstanceId(processInstanceIdOptional);
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<NodeInstanceEntity> nodeInstanceEntities = Collections.singletonList(new NodeInstanceEntity());
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
                flowableElementType)).thenReturn(nodeInstanceEntities);
        List<FlowableElement> flowableElements = flowableAdaptor.getFlowableElementByType(instanceId, nodeType,
                flowableElementType);
        verifyGetFlowableElementByType(flowableElements);
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));
        Optional<FlowTaskInstance> flowTaskInstanceOptional = flowableAdaptor.getTaskInstanceByActivityId(activityId,
                flowInstanceId);
        verifyGetTaskInstanceByActivityId(flowTaskInstanceOptional);
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<FlowApprovalInstance> flowApprovalInstanceOptional = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);
        verifyGetApprovalInstanceByActivityId(flowApprovalInstanceOptional);
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<FlowApprovalInstance> flowApprovalInstanceOptional = flowableAdaptor.getApprovalInstanceByName(name,
                flowInstanceId);
        verifyGetApprovalInstanceByName(flowApprovalInstanceOptional);
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));
        Optional<FlowGatewayInstance> flowGatewayInstanceOptional = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);
        verifyGetGatewayInstanceByActivityId(flowGatewayInstanceOptional);
    }

    @Test
    public void testSetFlowableElement() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        FlowableElement flowableElement = new FlowableElement("activityId", "name", flowableElementType);
        BaseFlowNodeInstance baseFlowNodeInstance = mock(BaseFlowNodeInstance.class);
        when(baseFlowNodeInstance.getId()).thenReturn(instanceId);
        when(baseFlowNodeInstance.getNodeType()).thenReturn(nodeType);
        when(baseFlowNodeInstance.getFlowInstanceId()).thenReturn(1L);
        flowableAdaptor.setFlowableElement(baseFlowNodeInstance, flowableElement);
        verifySetFlowableElement();
    }

    private void verifySetProcessInstanceId() {
        // ... skipped for brevity
    }

    private void verifyGetFlowInstanceIdByProcessInstanceId(Optional<Long> flowInstanceId) {
        // ... skipped for brevity
    }

    private void verifySetProcessDefinitionId() {
        // ... skipped for brevity
    }

    private void verifyGetFlowInstanceIdByProcessDefinitionId(Optional<Long> flowInstanceId) {
        // ... skipped for brevity
    }

    private void verifyGetProcessInstanceIdByFlowInstanceId(Optional<String> processInstanceIdOptional) {
        // ... skipped for brevity
    }

    private void verifyGetFlowableElementByType(List<FlowableElement> flowableElements) {
        // ... skipped for brevity
    }

    private void verifyGetTaskInstanceByActivityId(Optional<FlowTaskInstance> flowTaskInstanceOptional) {
        // ... skipped for brevity
    }

    private void verifyGetApprovalInstanceByActivityId(Optional<FlowApprovalInstance> flowApprovalInstanceOptional) {
        // ... skipped for brevity
    }

    private void verifyGetApprovalInstanceByName(Optional<FlowApprovalInstance> flowApprovalInstanceOptional) {
        // ... skipped for brevity
    }

    private void verifyGetGatewayInstanceByActivityId(Optional<FlowGatewayInstance> flowGatewayInstanceOptional) {
        // ... skipped for brevity
    }

    private void verifySetFlowableElement() {
        // ... skipped for brevity
    }

}