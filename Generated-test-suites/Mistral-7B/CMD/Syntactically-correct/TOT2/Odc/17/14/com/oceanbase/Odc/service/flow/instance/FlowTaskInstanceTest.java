import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.TaskService;
import com.oceanbase.odc.service.flow.FlowableAdaptorSpy;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstanceRepository;
import com.oceanbase.odc.service.flow.NodeInstanceEntityRepositorySpy;
import com.oceanbase.odc.service.flow.ServiceTaskInstanceEntitySpy;
import com.oceanbase.odc.service.flow.ServiceTaskInstanceRepositorySpy;

@SuppressWarnings("deprecation")
public class FlowTaskInstanceTest {

 @Mock
 private NodeInstanceEntityRepository nodeRepository;

 @Mock
 private SequenceInstanceRepository sequenceRepository;

 @Mock
 private EventPublisher eventPublisher;

 @Mock
 private TaskService taskService;

 @Mock
 private ServiceTaskInstanceRepository serviceTaskRepository;

 @Mock
 private ServiceTaskInstanceEntity serviceTaskInstanceEntity;

 @Mock
 private ServiceTaskStartedListener listener;

 @Mock
 private RuntimeDelegateMapper delegateConvertor;

 @Mock
 private FlowNodeInstance flowNodeInstance;

 @Mock
 private BaseRuntimeFlowableDelegate<?> targetTaskHandle;

 @InjectMocks
 private FlowTaskInstance flowTaskInstance;

 @BeforeEach
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testCreateFlowTaskInstanceWithValidParameters() {
 // Given
 FlowTaskInstanceRepository flowTaskInstanceRepository = new ServiceTaskInstanceRepositorySpy();
 FlowableAdaptor flowableAdaptor = new FlowableAdaptorSpy();
 FlowableAdaptorSpy flowableAdaptorSpy = (FlowableAdaptorSpy) flowableAdaptor;
 NodeInstanceEntityRepository nodeInstanceEntityRepository = new NodeInstanceEntityRepositorySpy();
 NodeInstanceEntityRepositorySpy nodeInstanceEntityRepositorySpy = (NodeInstanceEntityRepositorySpy) nodeInstanceEntityRepository;
 SequenceInstanceRepository sequenceInstanceRepository = new SequenceInstanceRepository();
 EventPublisher eventPublisher = new EventPublisher();
 TaskService taskService = Mockito.mock(TaskService.class);
 ServiceTaskInstanceRepository serviceTaskRepository = new ServiceTaskInstanceRepositorySpy();
 ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntitySpy();
 ServiceTaskInstanceRepositorySpy serviceTaskInstanceRepositorySpy = (ServiceTaskInstanceRepositorySpy) serviceTaskRepository;

 FlowNodeType flowNodeType = FlowNodeType.SERVICE_TASK;
 Long organizationId = 1L;
 Long flowInstanceId = 1L;
 ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig("strategyName", 5L, 1L);
 boolean startEndpoint = true;
 boolean endEndPoint = false;
 RuntimeDelegateMapper delegateConvertor = Mockito.mock(RuntimeDelegateMapper.class);

 // When
 flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, startEndpoint,
 endEndPoint, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeInstanceEntityRepository,
 sequenceInstanceRepository, serviceTaskRepository);

 // Then
 Assertions.assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
 Assertions.assertEquals(organizationId, flowTaskInstance.getOrganizationId());
 Assertions.assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
 Assertions.assertEquals(strategyConfig, flowTaskInstance.getStrategyConfig());
 Assertions.assertEquals(startEndpoint, flowTaskInstance.isStartEndpoint());
 Assertions.assertEquals(endEndPoint, flowTaskInstance.isEndEndPoint());
 Assertions.assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
 Assertions.assertEquals(flowNodeInstance, flowTaskInstance.getFlowableAdaptor().getFlowableElementManager()
 .getFlowableElement(flowTaskInstance.getId(), flowNodeType, FlowableElementType.SERVICE_TASK));
 Assertions.assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
 Assertions.assertEquals(FlowNodeStatus.CREATED, flowTaskInstance.getStatus());
 Assertions.assertFalse(flowTaskInstance.isPresentOnThisMachine());
 Assertions.assertNull(flowTaskInstance.getTargetTaskHandle());
 Assertions.assertNull(flowTaskInstance.getTargetTaskId());
 Assertions.assertNull(flowTaskInstance.getListener());
 }

 // Add test cases for other methods and edge cases here

 @Test
 public void testDeleteFlowTaskInstance() {
 // Given
 Long instanceId = 1L;
 ServiceTaskInstanceRepositorySpy serviceTaskInstanceRepositorySpy = (ServiceTaskInstanceRepositorySpy) serviceTaskRepository;

 // When
 flowTaskInstance.doDelete(instanceId);

 // Then
 Assertions.assertTrue(serviceTaskInstanceRepositorySpy.deleteCallCount > 0);
 Assertions.assertEquals(instanceId, serviceTaskInstanceRepositorySpy.deleteParams.get(0));
 }

 // Add test cases for other methods and edge cases here

}