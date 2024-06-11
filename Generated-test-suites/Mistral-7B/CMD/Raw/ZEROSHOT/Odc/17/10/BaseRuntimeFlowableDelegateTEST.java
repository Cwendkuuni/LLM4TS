package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.*;

import org.flowable.engine.*;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.flowable.engine.runtime.execution.RuntimeExecution;
import org.flowable.engine.task.Task;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableTestUtil;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

 private static final long PROCESS_INSTANCE_ID = 1L;
 private static final long FLOW_INSTANCE_ID = 2L;
 private static final String ACTIVITY_ID = "activityId";
 private static final long TARGET_TASK_INSTANCE_ID = 3L;
 private static final TaskType TASK_TYPE = TaskType.TASK_TYPE_1;
 private static final ExecutionStrategyConfig STRATEGY_CONFIG = new ExecutionStrategyConfig();

 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private FlowableAdaptor flowableAdaptor;
 @Mock
 private FlowInstanceRepository flowInstanceRepository;
 @Mock
 private Task task;
 @Mock
 private ProcessInstance processInstance;
 @Mock
 private RuntimeExecution runtimeExecution;
 @Mock
 private DelegateExecution delegateExecution;
 @InjectMocks
 private BaseRuntimeFlowableDelegate<String> testee;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
 .thenReturn(FLOW_INSTANCE_ID);
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
 .thenReturn(Optional.of(new FlowTaskInstance()));
 when(flowableAdaptor.getFlowInstanceById(FLOW_INSTANCE_ID)).thenReturn(processInstance);
 when(processInstance.getId()).thenReturn(PROCESS_INSTANCE_ID);
 when(processInstance.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(task.getId()).thenReturn(TARGET_TASK_INSTANCE_ID);
 when(task.getExecution()).thenReturn(runtimeExecution);
 when(runtimeExecution.getId()).thenReturn(1L);
 when(runtimeExecution.getProcessInstanceId()).thenReturn(PROCESS_INSTANCE_ID);
 when(runtimeExecution.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(runtimeExecution.getActivityId()).thenReturn(ACTIVITY_ID);
 when(delegateExecution.getProcessInstanceId()).thenReturn(PROCESS_INSTANCE_ID);
 when(delegateExecution.getId()).thenReturn(1L);
 when(delegateExecution.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(delegateExecution.getCurrentActivityId()).thenReturn(ACTIVITY_ID);
 when(eventPublisher.publishEvent(any())).thenReturn(null);
 }

 @After
 public void tearDown() {
 reset(eventPublisher, flowableAdaptor, flowInstanceRepository);
 }

 @Test
 public void testInit() {
 BaseRuntimeFlowableDelegate testee = new BaseRuntimeFlowableDelegate<>();
 assertNull(testee.getActivityId());
 assertNull(testee.getTargetTaskInstanceId());
 assertNull(testee.getTaskType());
 assertNull(testee.getFlowInstanceId());
 assertNull(testee.getStrategyConfig());
 }

 @Test
 public void testRun() throws Exception {
 testee.init(eventPublisher);
 testee.setFlowableAdaptor(flowableAdaptor);
 testee.setEventPublisher(eventPublisher);
 testee.setFlowInstanceRepository(flowInstanceRepository);
 testee.run(delegateExecution);

 verify(eventPublisher, times(1)).publishEvent(isA(ServiceTaskStartedEvent.class));
 assertEquals(FLOW_INSTANCE_ID, testee.getFlowInstanceId());
 assertEquals(TARGET_TASK_INSTANCE_ID, testee.getTargetTaskInstanceId());
 assertEquals(TASK_TYPE, testee.getTaskType());
 }

 @Test
 public void testPreHandle() throws Exception {
 testee.preHandle(delegateExecution);
 verifyNoInteractions(eventPublisher);
 }

 @Test
 public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
 Callable<String> callable = mock(Callable.class);
 when(testee.initCallable(delegateExecution)).thenReturn(callable);
 testee.init(eventPublisher);
 testee.setFlowableAdaptor(flowableAdaptor);
 testee.setEventPublisher(eventPublisher);
 testee.setFlowInstanceRepository(flowInstanceRepository);
 testee.run(delegateExecution);

 Future<String> future = testee;
 String result = future.get();
 verify(eventPublisher, times(1)).removeEventListener(isA(TaskInstanceCreatedListener.class));
 verify(eventPublisher, times(1)).removeEventListener(isA(ActiveTaskStatisticsListener.class));
 assertEquals("expected result", result);
 }

 @Test
 public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 Callable<String> callable = mock(Callable.class);
 when(testee.initCallable(delegateExecution)).thenReturn(callable);
 testee.init(eventPublisher);
 testee.setFlowableAdaptor(flowableAdaptor);
 testee.setEventPublisher(eventPublisher);
 testee.setFlowInstanceRepository(flowInstanceRepository);
 testee.run(delegateExecution);

 Future<String> future = testee;
 try {
 future.get(500, TimeUnit.MILLISECONDS);
 fail();
 } catch (TimeoutException e) {
 // expected
 }
 }

 @Test
 public void testIsDone() {
 testee.done = true;
 assertTrue(testee.isDone());
 }

 @Test
 public void testBindToFlowTaskInstance() {
 FlowTaskInstance taskInstance = new FlowTaskInstance();
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(taskInstance));
 testee.init(eventPublisher);
 testee.setFlowableAdaptor(flowableAdaptor);
 testee.setEventPublisher(eventPublisher);
 testee.setFlowInstanceRepository(flowInstanceRepository);
 testee.bindToFlowTaskInstance(taskInstance);
 verify(taskInstance, times(1)).bindServiceTask(eq(testee));
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 testee.updateFlowInstanceStatus(FlowStatus.COMPLETED);
 verify(flowInstanceRepository, times(1)).updateStatusById(eq(FLOW_INSTANCE_ID), eq(FlowStatus.COMPLETED));
 }

 @Test
 public void testInitializeWithEmptyConstructor() {
 BaseRuntimeFlowableDelegate emptyTestee = new BaseRuntimeFlowableDelegate<>();
 assertNull(emptyTestee.getActivityId());
 assertNull(emptyTestee.getTargetTaskInstanceId());
 assertNull(emptyTestee.getTaskType());
 assertNull(emptyTestee.getFlowInstanceId());
 assertNull(emptyTestee.getStrategyConfig());
 }

 @Test
 public void testInitializeWithCallable() {
 Callable<String> callable = mock(Callable.class);
 BaseRuntimeFlowableDelegate<String> testee = new BaseRuntimeFlowableDelegate<>(callable);
 assertSame(callable, testee.getCallable());
 assertNull(testee.getActivityId());
 assertNull(testee.getTargetTaskInstanceId());
 assertNull(testee.getTaskType());
 assertNull(testee.getFlowInstanceId());
 assertNull(testee.getStrategyConfig());
 }

}