package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.oceanbase.odc.core.iam.auth.AuthenticationFacade;
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
import com.oceanbase.odc.service.flow.instance.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeImpl;

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

 @InjectMocks
 private FlowableAdaptorImpl flowableAdaptor;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testSetProcessInstanceId() {
 // Given
 Long flowInstanceId = 1L;
 String processInstanceId = "process-instance-id";

 // When
 doNothing().when(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
 flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

 // Then
 verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetProcessInstanceIdWithNullFlowInstanceId() {
 // Given
 Long flowInstanceId = null;
 String processInstanceId = "process-instance-id";

 // When & Then
 assertThrows(IllegalArgumentException.class, () -> flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetProcessInstanceIdWithNullProcessInstanceId() {
 // Given
 Long flowInstanceId = 1L;
 String processInstanceId = null;

 // When & Then
 assertThrows(IllegalArgumentException.class, () -> flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId));
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId() {
 // Given
 String processInstanceId = "process-instance-id";
 FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
 flowInstanceEntity.setProcessInstanceId(processInstanceId);
 flowInstanceEntity.setId(1L);
 when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(flowInstanceEntity));

 // When
 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

 // Then
 assertTrue(result.isPresent());
 assertEquals(1L, result.get());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetFlowInstanceIdByProcessInstanceIdWithNoMatchingFlowInstance() {
 // Given
 String processInstanceId = "process-instance-id";
 when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

 // When & Then
 assertThrows(IllegalStateException.class, () -> flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId));
 }

 // test other methods with similar structure, using mocks to simulate various scenarios

 @Test
 public void testGetFlowableElementByType() {
 // Given
 Long instanceId = 1L;
 FlowNodeType nodeType = FlowNodeType.APPROVAL_TASK;
 FlowableElementType elementType = FlowableElementType.START_EVENT;
 List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
 new NodeInstanceEntity(), new NodeInstanceEntity(), new NodeInstanceEntity());
 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType, elementType))
 .thenReturn(nodeInstanceEntities);

 List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, nodeType, elementType);

 assertEquals(1, result.size());
 }

 // test other methods with similar structure, using mocks to simulate various scenarios

 @Test
 public void testGetTaskInstanceByActivityId() {
 // Given
 String activityId = "activity-id";
 Long flowInstanceId = 1L;
 ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId))
 .thenReturn(Optional.of(serviceTaskInstanceEntity));

 Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

 assertTrue(result.isPresent());
 }

 // test other methods with similar structure, using mocks to simulate various scenarios

 @Test
 public void testGetApprovalInstanceByName() {
 // Given
 String name = "name";
 Long flowInstanceId = 1L;
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
 .thenReturn(Optional.of(userTaskInstanceEntity));

 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

 assertTrue(result.isPresent());
 }

 // test other methods with similar structure, using mocks to simulate various scenarios

 @Test
 public void testGetApprovalInstanceByActivityId() {
 // Given
 String activityId = "activity-id";
 Long flowInstanceId = 1L;
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId))
 .thenReturn(Optional.of(userTaskInstanceEntity));

 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

 assertTrue(result.isPresent());
 }

 // test other methods with similar structure, using mocks to simulate various scenarios

 @Test
 public void testSetFlowableElement() {
 // Given
 Long nodeInstanceId = 1L;
 FlowableElement flowableElement = new FlowableElement("activity-id", "name", FlowableElementType.END_EVENT);
 BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
 when(nodeInstance.getId()).thenReturn(nodeInstanceId);
 when(nodeInstance.getNodeType()).thenReturn(FlowNodeType.END_EVENT);
 when(nodeInstance.getFlowInstanceId()).thenReturn(1L);

 FlowableElementType expectedFlowableElementType = FlowableElementType.END_EVENT;
 String expectedActivityId = "activity-id";
 String expectedName = "name";

 doNothing().when(nodeInstanceRepository).save(any(NodeInstanceEntity.class));

 flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

 NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceRepository.findById(nodeInstanceId).get();

 assertEquals(expectedFlowableElementType, savedNodeInstanceEntity.getFlowableElementType());
 assertEquals(expectedActivityId, savedNodeInstanceEntity.getActivityId());
 assertEquals(expectedName, savedNodeInstanceEntity.getName());
 }

 // test other methods with similar structure, using mocks to simulate various scenarios

}