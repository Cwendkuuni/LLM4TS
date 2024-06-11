package com.oceanbase.odc.service.flow.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.flowable.engine.FormService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.DefaultFormService;
import org.flowable.engine.impl.DefaultRuntimeService;
import org.flowable.engine.impl.DefaultTaskService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.impl.persistence.entity.TaskEntity;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableAdaptorImplTest;
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.FlowableElementType;
import com.oceanbase.odc.service.flow.FlowableElement;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

import lombok.SneakyThrows;

@SpringBootTest
public class FlowableAdaptorImplTest {

 private static final int EXISTING_INSTANCE_ID = 1;
 private static final String EXISTING_ACTIVITY_ID = "existingActivityId";
 private static final String EXISTING_NAME = "existingName";
 private static final long EXISTING_FLOW_INSTANCE_ID = 1L;
 private static final String EXISTING_PROCESS_INSTANCE_ID = "existingProcessInstanceId";
 private static final long EXISTING_PROCESS_DEFINITION_ID = 1L;

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

 @Autowired
 private FlowableAdaptorImpl flowableAdaptor;

 @BeforeEach
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testSetProcessInstanceId() {
 // given
 Long flowInstanceId = 1L;
 String processInstanceId = UUID.randomUUID().toString();

 // when
 flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

 // then
 // needs to be verified with a real runtime service
 // as it modifies the database
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId() {
 // given
 String processInstanceId = EXISTING_PROCESS_INSTANCE_ID;
 ProcessDefinitionEntity processDefinition = new ProcessDefinitionEntity();
 processDefinition.setId(EXISTING_PROCESS_DEFINITION_ID);
 ProcessInstanceEntity processInstance = new ProcessInstanceEntity();
 processInstance.setProcessDefinitionId(processDefinition.getId());
 processInstance.setId(processInstanceId);

 // when
 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

 // then
 assertTrue(result.isPresent());
 assertEquals(EXISTING_FLOW_INSTANCE_ID, result.get());
 }

 @Test
 public void testSetProcessDefinitionId() {
 // given
 Long flowInstanceId = 1L;
 String processDefinitionId = UUID.randomUUID().toString();

 // when
 flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

 // then
 // needs to be verified with a real runtime service
 // as it modifies the database
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionId() {
 // given
 String processDefinitionId = EXISTING_PROCESS_DEFINITION_ID.toString();

 // when
 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

 // then
 assertTrue(result.isPresent());
 assertEquals(EXISTING_FLOW_INSTANCE_ID, result.get());
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceId() {
 // given
 Long flowInstanceId = EXISTING_FLOW_INSTANCE_ID;
 String expectedProcessInstanceId = EXISTING_PROCESS_INSTANCE_ID;

 // when
 Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

 // then
 assertTrue(result.isPresent());
 assertEquals(expectedProcessInstanceId, result.get());
 }

 @Test
 public void testGetFlowableElementByType() {
 // given
 Long instanceId = EXISTING_INSTANCE_ID;
 FlowNodeType nodeType = FlowNodeType.USER_TASK;
 FlowableElementType elementType = FlowableElementType.START_EVENT;

 List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, nodeType,
 elementType);

 // then
 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetTaskInstanceByActivityId() {
 // given
 String activityId = EXISTING_ACTIVITY_ID;
 Long flowInstanceId = EXISTING_FLOW_INSTANCE_ID;
 ExecutionEntity execution = new ExecutionEntity();
 execution.setId(EXISTING_INSTANCE_ID);
 TaskEntity task = new TaskEntity();
 task.setId(EXISTING_INSTANCE_ID);
 task.setActivityId(activityId);
 FormService formService = new DefaultFormService();
 RuntimeService runtimeService = new DefaultRuntimeService();
 TaskService taskService = new DefaultTaskService();
 AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
 NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
 SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
 ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);

 // when
 Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId,
 flowInstanceId);

 // then
 assertTrue(result.isPresent());
 assertEquals(activityId, result.get().getActivityId());
 // need to verify the inner bean creation
 }

 @Test
 public void testGetApprovalInstanceByActivityId() {
 // given
 String activityId = EXISTING_ACTIVITY_ID;
 Long flowInstanceId = EXISTING_FLOW_INSTANCE_ID;
 UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
 userTaskInstance.setId(EXISTING_INSTANCE_ID);
 FormService formService = mock(FormService.class);
 RuntimeService runtimeService = mock(RuntimeService.class);
 TaskService taskService = mock(TaskService.class);
 AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
 NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
 SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
 UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);

 // when
 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
 flowInstanceId);

 // then
 assertTrue(result.isPresent());
 assertEquals(activityId, result.get().getActivityId());
 // need to verify the inner bean creation
 }

 @Test
 public void testGetApprovalInstanceByName() {
 // given
 String name = EXISTING_NAME;
 Long flowInstanceId = EXISTING_FLOW_INSTANCE_ID;
 UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
 userTaskInstance.setId(EXISTING_INSTANCE_ID);
 userTaskInstance.setName(name);
 FormService formService = mock(FormService.class);
 RuntimeService runtimeService = mock(RuntimeService.class);
 TaskService taskService = mock(TaskService.class);
 AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
 NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
 SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
 UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);

 // when
 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name,
 flowInstanceId);

 // then
 assertTrue(result.isPresent());
 assertEquals(name, result.get().getName());
 // need to verify the inner bean creation
 }

 @Test
 public void testGetGatewayInstanceByActivityId() {
 // given
 String activityId = EXISTING_ACTIVITY_ID;
 Long flowInstanceId = EXISTING_FLOW_INSTANCE_ID;
 GateWayInstanceEntity gateway = new GateWayInstanceEntity();
 gateway.setId(EXISTING_INSTANCE_ID);
 gateway.setActivityId(activityId);
 FormService formService = mock(FormService.class);
 RuntimeService runtimeService = mock(RuntimeService.class);
 NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
 SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
 GateWayInstanceRepository gateWayInstanceRepository = mock(GateWayInstanceRepository.class);

 // when
 Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
 flowInstanceId);

 // then
 assertTrue(result.isPresent());
 assertEquals(activityId, result.get().getActivityId());
 // need to verify the inner bean creation
 }

 @Test
 public void testSetFlowableElement() {
 // given
 Long instanceId = EXISTING_INSTANCE_ID;
 FlowNodeType nodeType = FlowNodeType.USER_TASK;
 FlowableElementType elementType = FlowableElementType.START_EVENT;
 FlowableElement flowableElement = new FlowableElement(EXISTING_ACTIVITY_ID, EXISTING_NAME,
 elementType);
 BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
 when(nodeInstance.getId()).thenReturn(instanceId);
 when(nodeInstance.getNodeType()).thenReturn(nodeType);
 when(nodeInstance.getFlowInstanceId()).thenReturn(EXISTING_FLOW_INSTANCE_ID);

 // when
 flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

 // then
 // needs to be verified with a real repository
 // as it modifies the database
 }
}