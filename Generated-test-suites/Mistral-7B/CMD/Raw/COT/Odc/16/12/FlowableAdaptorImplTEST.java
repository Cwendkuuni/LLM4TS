import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableElement;
import com.oceanbase.odc.service.flow.FlowableElementType;
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.FlowableAdaptor.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.FlowableAdaptor.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowableAdaptor.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowableAdaptor.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstanceTest;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstanceTest;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstanceTest;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstanceTest;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapperTest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

 @Mock
 private NodeInstanceEntityRepository nodeInstanceRepository;
 @Mock
 private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
 @Mock
 private UserTaskInstanceRepository userTaskInstanceRepository;
 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private NodeInstanceEntityRepository nodeRepository;
 @Mock
 private SequenceInstanceRepository sequenceRepository;
 @Mock
 private FlowInstanceRepository flowInstanceRepository;
 @Mock
 private TaskService taskService;
 @Mock
 private FormService formService;
 @Mock
 private AuthenticationFacade authenticationFacade;
 @Mock
 private GateWayInstanceRepository gateWayInstanceRepository;

 @InjectMocks
 private FlowableAdaptorImpl flowableAdaptorImpl;

 @Test
 public void testSetProcessInstanceId() {
 // Arrange
 Long flowInstanceId = 1L;
 String processInstanceId = "processInstanceId";

 // Act
 flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

 // Assert
 Mockito.verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId() {
 // Arrange
 String processInstanceId = "processInstanceId";
 List<FlowInstanceEntity> flowInstanceEntityList = Collections.singletonList(new FlowInstanceEntity());
 Mockito.when(flowInstanceRepository.findAll(Mockito.any(Specification.class)))
 .thenReturn(flowInstanceEntityList);

 // Act
 Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

 // Assert
 Mockito.verify(flowInstanceRepository, Mockito.times(1)).findAll(Mockito.any(Specification.class));
 Assert.assertEquals(Optional.of(flowInstanceEntityList.get(0).getId()), result);
 }

 @Test
 public void testSetProcessDefinitionId() {
 // Arrange
 Long flowInstanceId = 1L;
 String processDefinitionId = "processDefinitionId";

 // Act
 flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);

 // Assert
 Mockito.verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionId() {
 // Arrange
 String processDefinitionId = "processDefinitionId";
 List<FlowInstanceEntity> flowInstanceEntityList = Collections.singletonList(new FlowInstanceEntity());
 Mockito.when(flowInstanceRepository.findAll(Mockito.any(Specification.class)))
 .thenReturn(flowInstanceEntityList);

 // Act
 Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

 // Assert
 Mockito.verify(flowInstanceRepository, Mockito.times(1)).findAll(Mockito.any(Specification.class));
 Assert.assertEquals(Optional.of(flowInstanceEntityList.get(0).getId()), result);
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceId() {
 // Arrange
 Long flowInstanceId = 1L;
 String processInstanceId = "processInstanceId";
 Mockito.when(flowInstanceRepository.findById(flowInstanceId))
 .thenReturn(Optional.of(new FlowInstanceEntity()));

 // Act
 Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

 // Assert
 Mockito.verify(flowInstanceRepository, Mockito.times(1)).findById(flowInstanceId);
 Assert.assertEquals(Optional.of(processInstanceId), result);
 }

 @Test
 public void testGetFlowableElementByType() {
 // Arrange
 Long instanceId = 1L;
 Long flowInstanceId = 1L;
 FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
 FlowableElementType nodeType = FlowNodeType.START_EVENT;
 List<NodeInstanceEntity> nodeInstanceEntityList = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
 Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
 flowableElementType))
 .thenReturn(nodeInstanceEntityList);
 List<FlowableElement> expectedFlowableElementList = Arrays.asList(new FlowableElement("activityId1", "name1",
 flowableElementType), new FlowableElement("activityId2", "name2", flowableElementType));

 // Act
 List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(instanceId, nodeType,
 flowableElementType);

 // Assert
 Mockito.verify(nodeInstanceRepository, Mockito.times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
 nodeType, flowableElementType);
 Assert.assertEquals(expectedFlowableElementList, result);
 }

 @Test
 public void testGetTaskInstanceByActivityId() {
 // Arrange
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
 Mockito.when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
 flowInstanceId))
 .thenReturn(Optional.of(serviceTaskInstanceEntity));
 FlowTaskInstance expectedFlowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity,
 new OdcRuntimeDelegateMapper(), taskService, eventPublisher, nodeRepository, sequenceRepository,
 serviceTaskInstanceRepository);

 // Act
 Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId, flowInstanceId);

 // Assert
 Mockito.verify(serviceTaskInstanceRepository, Mockito.times(1)).findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK,
 activityId, flowInstanceId);
 Assert.assertEquals(Optional.of(expectedFlowTaskInstance), result);
 }

 @Test
 public void testGetApprovalInstanceByActivityId() {
 // Arrange
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
 flowInstanceId))
 .thenReturn(Optional.of(userTaskInstanceEntity));
 FlowApprovalInstance expectedFlowApprovalInstance = new FlowApprovalInstance(userTaskInstanceEntity, flowService,
 taskService, formService, eventPublisher, authenticationFacade, nodeRepository, sequenceRepository,
 userTaskInstanceRepository);

 // Act
 Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId, flowInstanceId);

 // Assert
 Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK,
 activityId, flowInstanceId);
 Assert.assertEquals(Optional.of(expectedFlowApprovalInstance), result);
 }

 @Test
 public void testGetApprovalInstanceByName() {
 // Arrange
 String name = "name";
 Long flowInstanceId = 1L;
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
 .thenReturn(Optional.of(userTaskInstanceEntity));
 FlowApprovalInstance expectedFlowApprovalInstance = new FlowApprovalInstance(userTaskInstanceEntity, flowService,
 taskService, formService, eventPublisher, authenticationFacade, nodeRepository, sequenceRepository,
 userTaskInstanceRepository);

 // Act
 Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName(name, flowInstanceId);

 // Assert
 Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name,
 flowInstanceId);
 Assert.assertEquals(Optional.of(expectedFlowApprovalInstance), result);
 }

 @Test
 public void testGetGatewayInstanceByActivityId() {
 // Arrange
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
 Mockito.when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
 flowInstanceId))
 .thenReturn(Optional.of(gateWayInstanceEntity));
 FlowGatewayInstance expectedFlowGatewayInstance = new FlowGatewayInstance(gateWayInstanceEntity, flowService,
 nodeRepository, sequenceRepository, gateWayInstanceRepository);

 // Act
 Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId, flowInstanceId);

 // Assert
 Mockito.verify(gateWayInstanceRepository, Mockito.times(1)).findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY,
 activityId, flowInstanceId);
 Assert.assertEquals(Optional.of(expectedFlowGatewayInstance), result);
 }

 @Test
 public void testSetFlowableElement() {
 // Arrange
 Long nodeInstanceId = 1L;
 Long flowInstanceId = 1L;
 FlowableElementType flowableElementType = FlowableElementType.END_EVENT;
 FlowableElement flowableElement = new FlowableElement("activityId", "name", flowableElementType);
 BaseFlowNodeInstance baseFlowNodeInstance = Mockito.mock(BaseFlowNodeInstance.class);
 Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstanceId,
 FlowNodeType.START_EVENT, flowableElementType))
 .thenReturn(Collections.singletonList(new NodeInstanceEntity()));

 // Act & Assert
 Assert.assertFalse(flowableAdaptorImpl.setFlowableElement(baseFlowNodeInstance, flowableElement));
 }

 @Test
 public void testSetFlowableElement_success() {
 // Arrange
 Long nodeInstanceId = 1L;
 Long flowInstanceId = 1L;
 FlowableElementType flowableElementType = FlowableElementType.END_EVENT;
 FlowableElement flowableElement = new FlowableElement("activityId", "name", flowableElementType);
 BaseFlowNodeInstance baseFlowNodeInstance = Mockito.mock(BaseFlowNodeInstance.class);
 Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstanceId,
 FlowNodeType.START_EVENT, FlowableElementType.END_EVENT))
 .thenReturn(Collections.emptyList());

 // Act & Assert
 Assert.assertTrue(flowableAdaptorImpl.setFlowableElement(baseFlowNodeInstance, flowableElement));
 }
}