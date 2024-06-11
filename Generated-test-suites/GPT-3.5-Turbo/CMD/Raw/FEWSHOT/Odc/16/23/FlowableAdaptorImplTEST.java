/*
 * Copyright (c) 2023 OceanBase.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
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

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;

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
    private GateWayInstanceRepository gateWayInstanceRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private FormService formService;
    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "process-1";
        Optional<FlowInstanceEntity> optional = Optional.of(new FlowInstanceEntity());
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(optional);
        Mockito.when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId)).thenReturn(1);
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId_NotFound() {
        String processInstanceId = "process-2";
        Specification<FlowInstanceEntity> specification =
                Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId));
        when(flowInstanceRepository.findAll(specification)).thenReturn(Collections.emptyList());

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 2L;
        String processDefinitionId = "definition-1";
        Optional<FlowInstanceEntity> optional = Optional.of(new FlowInstanceEntity());
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(optional);
        Mockito.when(flowInstanceRepository.updateProcessDefinitionIdById(flowInstanceId, processDefinitionId)).thenReturn(1);
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId_NotFound() {
        String processDefinitionId = "definition-2";
        Specification<FlowInstanceEntity> specification =
                Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId));
        when(flowInstanceRepository.findAll(specification)).thenReturn(Collections.emptyList());

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 3L;
        Optional<FlowInstanceEntity> optional = Optional.of(new FlowInstanceEntity());
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(optional);

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId_NotFound() {
        Long flowInstanceId = 4L;
        Optional<FlowInstanceEntity> optional = Optional.empty();
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(optional);

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 5L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT_TYPE;
        
        List<NodeInstanceEntity> entities = new ArrayList<>();
        entities.add(new NodeInstanceEntity());
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(entities);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetFlowableElementByType_EmptyResult() {
        Long instanceId = 6L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT_TYPE;

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Collections.emptyList());

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activity-1";
        Long flowInstanceId = 7L;

        Optional<ServiceTaskInstanceEntity> optional = Optional.of(new ServiceTaskInstanceEntity());
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId))
                .thenReturn(optional);

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testGetTaskInstanceByActivityId_NotFound() {
        String activityId = "activity-2";
        Long flowInstanceId = 8L;

        Optional<ServiceTaskInstanceEntity> optional = Optional.empty();
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId))
                .thenReturn(optional);

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activity-3";
        Long flowInstanceId = 9L;

        Optional<UserTaskInstanceEntity> optional = Optional.of(new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId))
                .thenReturn(optional);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId_NotFound() {
        String activityId = "activity-4";
        Long flowInstanceId = 10L;

        Optional<UserTaskInstanceEntity> optional = Optional.empty();
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId))
                .thenReturn(optional);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activity-5";
        Long flowInstanceId = 11L;

        Optional<GateWayInstanceEntity> optional = Optional.of(new GateWayInstanceEntity());
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId))
                .thenReturn(optional);

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId_NotFound() {
        String activityId = "activity-6";
        Long flowInstanceId = 12L;

        Optional<GateWayInstanceEntity> optional = Optional.empty();
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId))
                .thenReturn(optional);

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
        FlowableElement flowableElement = new FlowableElement("activity-7", "name-7", FlowableElementType.ELEMENT_TYPE);

        List<NodeInstanceEntity> entities = new ArrayList<>();
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setActivityId(flowableElement.getActivityId());
        entity.setName(flowableElement.getName());
        entity.setFlowableElementType(flowableElement.getType());
        entities.add(entity);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstance.getId(),
                nodeInstance.getNodeType(), flowableElement.getType())).thenReturn(entities);

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        Mockito.verify(nodeInstanceRepository, Mockito.times(1)).save(Mockito.any(NodeInstanceEntity.class));
    }

    @Test
    public void testInnerConvert() {
        Optional<UserTaskInstanceEntity> optional = Optional.of(new UserTaskInstanceEntity());
        Optional<FlowApprovalInstance> result = flowableAdaptor.innerConvert(optional, flowableAdaptor,
                (entity, flowService) -> new FlowApprovalInstance(entity, flowService, taskService, formService,
                        eventPublisher, authenticationFacade, nodeRepository, sequenceRepository,
                        userTaskInstanceRepository));

        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }
}