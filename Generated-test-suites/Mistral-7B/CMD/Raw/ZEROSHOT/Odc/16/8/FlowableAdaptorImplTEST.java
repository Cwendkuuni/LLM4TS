package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.*;
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
import com.oceanbase.odc.service.flow.*;
import com.oceanbase.odc.service.flow.instance.*;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@Slf4j
public class FlowableAdaptorImplTest {

 private static final Long FLOW_INSTANCE_ID = 1L;
 private static final String PROCESS_INSTANCE_ID = "processInstanceId";
 private static final String PROCESS_DEFINITION_ID = "processDefinitionId";
 private static final String ACTIVITY_ID = "activityId";
 private static final FlowableElementType FLOWABLE_ELEMENT_TYPE = FlowableElementType.START;
 private static final FlowNodeType NODE_TYPE = FlowNodeType.SERVICE_TASK;

 @Mock
 private FormService formService;
 @Mock
 private TaskService taskService;
 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private FlowInstanceRepository flowInstanceRepository;
 @Mock
 private NodeInstanceEntityRepository nodeInstanceRepository;
 @Mock
 private SequenceInstanceRepository sequenceRepository;
 @Mock
 private GateWayInstanceRepository gateWayInstanceRepository;
 @Mock
 private UserTaskInstanceRepository userTaskInstanceRepository;
 @Mock
 private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
 @Mock
 private AuthenticationFacade authenticationFacade;
 @Mock
 private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

 @InjectMocks
 private FlowableAdaptorImpl flowableAdaptorImpl;

 @BeforeEach
 void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 void testSetProcessInstanceId() {
 // given
 when(flowInstanceRepository.findById(FLOW_INSTANCE_ID)).thenReturn(Optional.of(new FlowInstanceEntity()));

 // when
 flowableAdaptorImpl.setProcessInstanceId(FLOW_INSTANCE_ID, PROCESS_INSTANCE_ID);

 // then
 verify(flowInstanceRepository).updateProcessInstanceIdById(FLOW_INSTANCE_ID, PROCESS_INSTANCE_ID);
 }

 @Test
 void testGetFlowInstanceIdByProcessInstanceId() {
 // given
 when(flowInstanceRepository.findAll(any(Specification.class)))
 .thenReturn(Collections.singletonList(new FlowInstanceEntity()));

 // when
 Optional<Long> flowInstanceId = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(PROCESS_INSTANCE_ID);

 // then
 assertEquals(FLOW_INSTANCE_ID, flowInstanceId.get());
 }

 @Test
 void testGetFlowInstanceIdByProcessDefinitionId() {
 // given
 when(flowInstanceRepository.findAll(any(Specification.class)))
 .thenReturn(Collections.singletonList(new FlowInstanceEntity()));

 // when
 Optional<Long> flowInstanceId = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID);

 // then
 assertEquals(FLOW_INSTANCE_ID, flowInstanceId.get());
 }

 @Test
 void testGetProcessInstanceIdByFlowInstanceId() {
 // given
 when(flowInstanceRepository.findById(FLOW_INSTANCE_ID)).thenReturn(Optional.of(new FlowInstanceEntity()));

 // when
 Optional<String> processInstanceId = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID);

 // then
 assertEquals(PROCESS_INSTANCE_ID, processInstanceId.get());
 }

 @Test
 void testGetFlowableElementByType() {
 // given
 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(FLOW_INSTANCE_ID, NODE_TYPE, FLOWABLE_ELEMENT_TYPE))
 .thenReturn(Collections.singletonList(new NodeInstanceEntity()));

 // when
 List<FlowableElement> flowableElements = flowableAdaptorImpl.getFlowableElementByType(FLOW_INSTANCE_ID, NODE_TYPE, FLOWABLE_ELEMENT_TYPE);

 // then
 assertEquals(1, flowableElements.size());
 }

 @Test
 void testGetTaskInstanceByActivityId() {
 // given
 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(NODE_TYPE, ACTIVITY_ID, FLOW_INSTANCE_ID))
 .thenReturn(Optional.of(new ServiceTaskInstanceEntity()));

 // when
 Optional<FlowTaskInstance> flowTaskInstance = flowableAdaptorImpl.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);

 // then
 assertTrue(flowTaskInstance.isPresent());
 }

 @Test
 void testGetApprovalInstanceByActivityId() {
 // given
 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(NODE_TYPE, ACTIVITY_ID, FLOW_INSTANCE_ID))
 .thenReturn(Optional.of(new UserTaskInstanceEntity()));

 // when
 Optional<FlowApprovalInstance> flowApprovalInstance = flowableAdaptorImpl.getApprovalInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);

 // then
 assertTrue(flowApprovalInstance.isPresent());
 }

 @Test
 void testGetApprovalInstanceByName() {
 // given
 when(userTaskInstanceRepository.findByInstanceTypeAndName(NODE_TYPE, "name", FLOW_INSTANCE_ID))
 .thenReturn(Optional.of(new UserTaskInstanceEntity()));

 // when
 Optional<FlowApprovalInstance> flowApprovalInstance = flowableAdaptorImpl.getApprovalInstanceByName("name", FLOW_INSTANCE_ID);

 // then
 assertTrue(flowApprovalInstance.isPresent());
 }

 @Test
 void testGetGatewayInstanceByActivityId() {
 // given
 when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(NODE_TYPE, ACTIVITY_ID, FLOW_INSTANCE_ID))
 .thenReturn(Optional.of(new GateWayInstanceEntity()));

 // when
 Optional<FlowGatewayInstance> flowGatewayInstance = flowableAdaptorImpl.getGatewayInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);

 // then
 assertTrue(flowGatewayInstance.isPresent());
 }

 @Test
 void testSetFlowableElement() {
 // given
 NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
 nodeInstance.setId(1L);
 nodeInstance.setInstanceId(FLOW_INSTANCE_ID);
 nodeInstance.setInstanceType(NODE_TYPE);
 nodeInstance.setFlowInstanceId(FLOW_INSTANCE_ID);
 nodeInstance.setFlowableElementType(FLOWABLE_ELEMENT_TYPE);
 nodeInstanceRepository.save(nodeInstance);

 // when
 FlowableElement flowableElement = new FlowableElement("name", FLOWABLE_ELEMENT_TYPE);
 flowableAdaptorImpl.setFlowableElement(nodeInstance, flowableElement);

 // then
 assertEquals("name", nodeInstance.getName());
 }
}