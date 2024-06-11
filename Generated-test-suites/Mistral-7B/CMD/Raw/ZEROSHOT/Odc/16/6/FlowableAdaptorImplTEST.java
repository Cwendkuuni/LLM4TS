package com.oceanbase.odc.service.flow.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
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
import org.springframework.beans.factory.annotation.AutowiredAnnotatedGenericContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
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
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.FlowableAdaptor.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.FlowableAdaptor.FlowNodeType;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstanceTestHelper;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@ContextConfiguration(classes = {FlowableAdaptorImpl.class, AutowiredTestConfiguration.class})
@SpringJUnitConfig
public class FlowableAdaptorImplTest {

 private FlowableAdaptor flowableAdaptor;
 private FlowInstanceRepository flowInstanceRepository;
 private NodeInstanceEntityRepository nodeInstanceRepository;
 private SequenceInstanceRepository sequenceRepository;
 private UserTaskInstanceRepository userTaskInstanceRepository;
 private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
 private GateWayInstanceRepository gateWayInstanceRepository;
 private EventPublisher eventPublisher;
 private FormService formService;
 private TaskService taskService;
 private AuthenticationFacade authenticationFacade;

 @Before
 public void setup() {
 AutowiredTestConfiguration config = new AutowiredTestConfiguration();
 new AnnotatedBeanUtilsBeanPostProcessor(config.getApplicationContext())
 .register(FlowableAdaptorImpl.class.getName(), FlowableAdaptorImpl.class);

 flowableAdaptor = config.getApplicationContext().getBean(FlowableAdaptorImpl.class);
 flowInstanceRepository = config.getApplicationContext().getBean(FlowInstanceRepository.class);
 nodeInstanceRepository = config.getApplicationContext().getBean(NodeInstanceEntityRepository.class);
 sequenceRepository = config.getApplicationContext().getBean(SequenceInstanceRepository.class);
 userTaskInstanceRepository = config.getApplicationContext().getBean(UserTaskInstanceRepository.class);
 serviceTaskInstanceRepository = config.getApplicationContext().getBean(ServiceTaskInstanceRepository.class);
 gateWayInstanceRepository = config.getApplicationContext().getBean(GateWayInstanceRepository.class);
 eventPublisher = config.getApplicationContext().getBean(EventPublisher.class);
 formService = mock(FormService.class);
 taskService = mock(TaskService.class);
 authenticationFacade = mock(AuthenticationFacade.class);
 }

 @Test
 public void testSetProcessInstanceId() {
 // given
 Long flowInstanceId = 1L;
 String processInstanceId = "processInstanceId";

 // when
 flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

 // then
 Optional<FlowInstanceEntity> optional = flowInstanceRepository.findById(flowInstanceId);
 PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId, optional::isPresent);
 int affectRows = optional.get().getProcessInstanceId()
 .equals(processInstanceId) ? 0 : 1;
 assertThat(affectRows, is(1));
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId() {
 // given
 String processInstanceId = "processInstanceId";
 Long flowInstanceId = 1L;
 FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
 flowInstanceEntity.setId(flowInstanceId);
 flowInstanceEntity.setProcessInstanceId(processInstanceId);
 when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

 // when
 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

 // then
 assertThat(result.isPresent(), is(true));
 assertThat(result.get(), is(flowInstanceId));
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionId() {
 // given
 String processDefinitionId = "processDefinitionId";
 Long flowInstanceId = 1L;
 FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
 flowInstanceEntity.setId(flowInstanceId);
 flowInstanceEntity.setProcessDefinitionId(processDefinitionId);
 when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

 // when
 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

 // then
 assertThat(result.isPresent(), is(true));
 assertThat(result.get(), is(flowInstanceId));
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceId() {
 // given
 Long flowInstanceId = 1L;
 String processInstanceId = "processInstanceId";
 FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
 flowInstanceEntity.setId(flowInstanceId);
 flowInstanceEntity.setProcessInstanceId(processInstanceId);
 when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

 // when
 Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

 // then
 assertThat(result.isPresent(), is(true));
 assertThat(result.get(), is(processInstanceId));
 }

 @Test
 public void testGetFlowableElementByType() {
 // given
 Long instanceId = 1L;
 FlowNodeType nodeType = FlowNodeType.USER_TASK;
 FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
 List<NodeInstanceEntity> nodeInstances = Collections.singletonList(
 new NodeInstanceEntity(instanceId, nodeType, flowableElementType, "name", "activityId"));
 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
 flowableElementType))
 .thenReturn(nodeInstances);

 // when
 List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, nodeType, flowableElementType);

 // then
 assertThat(result.size(), is(1));
 assertThat(result.get(0).getActivityId(), is("activityId"));
 assertThat(result.get(0).getName(), is("name"));
 assertThat(result.get(0).getType(), is(flowableElementType.toString()));
 }

 @Test
 public void testGetTaskInstanceByActivityId() {
 // given
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 ServiceTaskInstanceEntity serviceTaskInstanceEntity = mock(ServiceTaskInstanceEntity.class);
 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
 flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));

 // when
 Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

 // then
 FlowTaskInstance testInstance = new FlowTaskInstanceTestHelper().createTestInstance();
 Optional<FlowTaskInstance> expectedResult = Optional.of(testInstance);
 FlowTaskInstance flowTaskInstance = new OdcRuntimeDelegateMapper().mapToFlowTaskInstance(serviceTaskInstanceEntity);
 FlowableAdaptorImpl flowableAdaptor = new FlowableAdaptorImpl();
 assertThat(result, is(expectedResult));
 assertThat(result.get().getFlowableElement(), is(flowTaskInstance.getFlowableElement()));
 }

 @Test
 public void testGetApprovalInstanceByActivityId() {
 // given
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 UserTaskInstanceEntity userTaskInstanceEntity = mock(UserTaskInstanceEntity.class);
 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
 flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));

 // when
 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
 flowInstanceId);

 // then
 FlowApprovalInstance testInstance = new FlowApprovalInstanceTestHelper().createTestInstance();
 Optional<FlowApprovalInstance> expectedResult = Optional.of(testInstance);
 FlowApprovalInstance flowApprovalInstance = new FlowApprovalInstance(userTaskInstanceEntity, flowableAdaptor,
 taskService, formService, eventPublisher, authenticationFacade, nodeRepository, sequenceRepository,
 userTaskInstanceRepository);
 assertThat(result, is(expectedResult));
 assertThat(result.get().getFlowableElement(), is(flowApprovalInstance.getFlowableElement()));
 }

 @Test
 public void testGetApprovalInstanceByName() {
 // given
 String name = "name";
 Long flowInstanceId = 1L;
 UserTaskInstanceEntity userTaskInstanceEntity = mock(UserTaskInstanceEntity.class);
 when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
 .thenReturn(Optional.of(userTaskInstanceEntity));

 // when
 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

 // then
 FlowApprovalInstance testInstance = new FlowApprovalInstanceTestHelper().createTestInstance();
 Optional<FlowApprovalInstance> expectedResult = Optional.of(testInstance);
 FlowApprovalInstance flowApprovalInstance = new FlowApprovalInstance(userTaskInstanceEntity, flowableAdaptor,
 taskService, formService, eventPublisher, authenticationFacade, nodeRepository, sequenceRepository,
 userTaskInstanceRepository);
 assertThat(result, is(expectedResult));
 assertThat(result.get().getFlowableElement(), is(flowApprovalInstance.getFlowableElement()));
 }

 @Test
 public void testGetGatewayInstanceByActivityId() {
 // given
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 GateWayInstanceEntity gateWayInstanceEntity = mock(GateWayInstanceEntity.class);
 when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId))
 .thenReturn(Optional.of(gateWayInstanceEntity));

 // when
 Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

 // then
 FlowGatewayInstance testInstance = new FlowGatewayInstanceTestHelper().createTestInstance();
 Optional<FlowGatewayInstance> expectedResult = Optional.of(testInstance);
 FlowGatewayInstance flowGatewayInstance = new FlowGatewayInstance(gateWayInstanceEntity, flowableAdaptor,
 flowService, nodeRepository, sequenceRepository, gateWayInstanceRepository);
 assertThat(result, is(expectedResult));
 assertThat(result.get().getFlowableElement(), is(flowGatewayInstance.getFlowableElement()));
 }

 @Test
 public void testSetFlowableElement() {
 // given
 Long instanceId = 1L;
 Long nodeInstanceId = 1L;
 FlowNodeType nodeType = FlowNodeType.USER_TASK;
 FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
 FlowableElement flowableElement = new FlowableElement("name", "activityId", flowableElementType);
 BaseFlowNodeInstance baseFlowNodeInstance = new BaseFlowNodeInstance(instanceId, nodeType, Collections.emptyList());

 // when
 flowableAdaptor.setFlowableElement(baseFlowNodeInstance, flowableElement);

 // then
 List<NodeInstanceEntity> result = nodeInstanceRepository.findAll();
 NodeInstanceEntity nodeInstanceEntity = result.stream().filter(n -> n.getInstanceId().equals(instanceId))
 .findFirst().orElseThrow();
 assertThat(nodeInstanceEntity.getFlowableElementType(), is(flowableElementType.toString()));
 assertThat(nodeInstanceEntity.getName(), is(flowableElement.getName()));
 assertThat(nodeInstanceEntity.getActivityId(), is(flowableElement.getActivityId()));
 }
}

class AutowiredTestConfiguration {

 private org.springframework.context.ApplicationContext applicationContext;

 public ApplicationContext getApplicationContext() {
 return applicationContext;
 }

 @Autowired
 public AutowiredTestConfiguration(ApplicationContext applicationContext) {
 this.applicationContext = applicationContext;
 }
}