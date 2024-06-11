package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;import org.mockito.Mock;import org.mockito.junit.jupiter.MockitoExtensions;import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.oceanbase.odc.common.constant.FlowStatus;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegateTest.TestCallable;
import com.oceanbase.odc.service.flow.FlowableTestUtils;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@SpringJUnitConfig(classes = BaseRuntimeFlowableDelegate.class)
public class BaseRuntimeFlowableDelegateTest {

 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private FlowableAdaptor flowableAdaptor;
 @Mock
 private FlowInstanceRepository flowInstanceRepository;
 @Mock
 private RetryExecutor retryExecutor;
 @InjectMocks
 private BaseRuntimeFlowableDelegate<Object> testee;
 @Mock
 private DelegateExecution execution;
 @Mock
 private FlowTaskInstance taskInstance;
 @Mock
 private ServiceTaskStartedEvent serviceTaskStartedEvent;

 @BeforeEach
 public void setup() {
 MockitoAnnotations.initMocks(this);
 Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
 Mockito.when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(taskInstance);
 }

 @Test
 public void testInit() {
 testee.init(eventPublisher);
 verify(eventPublisher).addEventListener(argThat(isInstanceOf(TaskInstanceCreatedListener.class)));
 verify(eventPublisher).addEventListener(argThat(isInstanceOf(ActiveTaskStatisticsListener.class)));
 }

 @Test
 public void testRun_Success() throws Exception {
 when(execution.getCurrentActivityId()).thenReturn("activityId1");
 when(taskInstance.getId()).thenReturn(1L);
 when(taskInstance.getTaskType()).thenReturn(TaskType.TASK1);
 when(taskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());

 testee.run(execution);

 assertEquals("activityId1", testee.getActivityId());
 assertEquals(1L, testee.getTargetTaskInstanceId());
 assertEquals(TaskType.TASK1, testee.getTaskType());
 assertEquals(1L, testee.getFlowInstanceId());
 assertEquals(new ExecutionStrategyConfig(), testee.getStrategyConfig());

 ArgumentCaptor<ServiceTaskStartedEvent> eventCaptor = ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);
 verify(eventPublisher).publishEvent(eventCaptor.capture());
 assertEquals(testee, eventCaptor.getValue().getTask());
 }

 @Test
 public void testRun_Exception() throws Exception {
 when(execution.getCurrentActivityId()).thenThrow(new Exception("Test Exception"));

 testee.run(execution);
 assertTrue(testee.isDone());
 assertNull(testee.get());
 assertNotNull(testee.getThrown());
 assertNotNull(testee.getThrown().getMessage());

 ArgumentCaptor<ServiceTaskError> eventCaptor = ArgumentCaptor.forClass(ServiceTaskError.class);
 verify(eventPublisher).publishEvent(eventCaptor.capture());
 assertEquals("Test Exception", eventCaptor.getValue().getCause().getMessage());
 }

 @Test
 public void testGet_Success() throws InterruptedException, ExecutionException, TimeoutException {
 when(execution.getCurrentActivityId()).thenReturn("activityId1");
 when(taskInstance.getId()).thenReturn(1L);
 when(taskInstance.getTaskType()).thenReturn(TaskType.TASK1);
 when(taskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());

 TestCallable callable = new TestCallable();
 when(callable.call()).thenReturn("Test Result");
 when(testee.initCallable(execution)).thenReturn(callable);

 testee.bindToFlowTaskInstance(taskInstance);
 testee.initTargetTaskInstanceId(execution);

 testee.get();
 assertEquals("Test Result", testee.get());
 }

 @Test
 public void testGet_Timeout() throws InterruptedException, ExecutionException, TimeoutException {
 when(execution.getCurrentActivityId()).thenReturn("activityId1");
 when(taskInstance.getId()).thenReturn(1L);
 when(taskInstance.getTaskType()).thenReturn(TaskType.TASK1);
 when(taskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());

 TestCallable callable = new TestCallable();
 when(callable.call()).thenThrow(new Exception("Test Exception"));
 when(testee.initCallable(execution)).thenReturn(callable);

 testee.bindToFlowTaskInstance(taskInstance);
 testee.initTargetTaskInstanceId(execution);

 testee.get(1, TimeUnit.SECONDS);
 fail("Should receive TimeoutException");
 }

 @Test
 public void testBindToFlowTaskInstance() {
 testee.bindToFlowTaskInstance(taskInstance);
 verify(taskInstance).bindServiceTask(testee);
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 testee.updateFlowInstanceStatus(FlowStatus.COMPLETED);
 verify(retryExecutor).run(() -> flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED), isNull());
 }

 @Test
 public void testPreHandle() throws Exception {
 testee.preHandle(execution);
 verifyNoInteractions(flowableAdaptor);
 }

 @Test
 public void testInitTargetTaskInstanceId_FlowInstanceNotFound() {
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(null);

 testee.initTargetTaskInstanceId(execution);
 fail("Should throw Exception");
 }

 @Test
 public void testInitTargetTaskInstanceId_TaskInstanceNotFound() {
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(null);

 testee.initTargetTaskInstanceId(execution);
 fail("Should throw Exception");
 }
}

class TestCallable implements Callable<String> {
 @Override
 public String call() {
 return "Test Result";
 }
}