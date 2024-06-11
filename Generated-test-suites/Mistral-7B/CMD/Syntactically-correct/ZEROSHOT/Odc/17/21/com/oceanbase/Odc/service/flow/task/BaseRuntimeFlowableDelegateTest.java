package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

 private BaseRuntimeFlowableDelegate<String> testDelegate;
 private DelegateExecution mockExecution;
 private EventPublisher mockEventPublisher;
 private FlowInstanceRepository mockFlowInstanceRepository;
 private FlowableAdaptor mockFlowableAdaptor;
 private RetryExecutor mockRetryExecutor;
 private CountDownLatch latch;

 @Before
 public void setUp() {
 mockExecution = mock(Execution.class);
 mockEventPublisher = mock(EventPublisher.class);
 mockFlowInstanceRepository = mock(FlowInstanceRepository.class);
 mockFlowableAdaptor = mock(FlowableAdaptor.class);
 mockRetryExecutor = mock(RetryExecutor.class);
 testDelegate = new BaseRuntimeFlowableDelegate<>() {
 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 throw new UnsupportedOperationException("initCallable is not implemented");
 }
 };
 testDelegate.setFlowableAdaptor(mockFlowableAdaptor);
 testDelegate.setEventPublisher(mockEventPublisher);
 testDelegate.setFlowInstanceRepository(mockFlowInstanceRepository);
 testDelegate.setRetryExecutor(mockRetryExecutor);
 testDelegate.init(mockEventPublisher);
 this.latch = new CountDownLatch(1);
 }

 @Test
 public void testInitTargetTaskInstanceId() {
 // given
 String processDefinitionId = "processDefinitionId";
 String activityId = "activityId";
 when(mockFlowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenReturn(1L);
 when(mockFlowableAdaptor.getTaskInstanceByActivityId(activityId, 1L)).thenReturn(Mockito.mock(FlowTaskInstance.class));

 // when
 testDelegate.initTargetTaskInstanceId(mockExecution);

 // then
 assertEquals(1L, testDelegate.getFlowInstanceId());
 assertEquals(1L, testDelegate.getTargetTaskInstanceId());
 assertEquals(TaskType.TASK_TYPE_1, testDelegate.getTaskType());
 assertEquals(ExecutionStrategyConfig.builder().build(), testDelegate.getStrategyConfig());
 }

 @Test(expected = ServiceTaskError.class)
 public void testInitTargetTaskInstanceIdWithException() {
 // given
 doThrow(new RuntimeException("Init exception")).when(mockFlowableAdaptor).getFlowInstanceIdByProcessDefinitionId(anyString());

 // when, then
 testDelegate.initTargetTaskInstanceId(mockExecution);
 fail("Init exception should be thrown.");
 }

 @Test
 public void testBindToFlowTaskInstance() {
 // given
 FlowTaskInstance mockFlowTaskInstance = Mockito.mock(FlowTaskInstance.class);
 when(mockFlowTaskInstance.getId()).thenReturn(1L);
 when(mockFlowTaskInstance.getStrategyConfig()).thenReturn(ExecutionStrategyConfig.builder().build());

 // when
 testDelegate.bindToFlowTaskInstance(mockFlowTaskInstance);

 // then
 verify(mockFlowTaskInstance, times(1)).bindServiceTask(isA(BaseRuntimeFlowableDelegate.class));
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 // given
 when(mockFlowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class.getName())))
 .thenReturn(true);

 // when
 testDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

 // then
 verify(mockFlowInstanceRepository, times(1)).updateStatusById(testDelegate.getFlowInstanceId(), eq(FlowStatus.COMPLETED));
 }

 @Test
 public void testRun() throws Exception {
 // given
 when(mockEventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenAnswer(invocation -> {});
 when(mockFlowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
 when(mockFlowableAdaptor.getTaskInstanceByActivityId(eq("activityId"), eq(1L))).thenReturn(Mockito.mock(FlowTaskInstance.class));
 when(mockEventPublisher.removeEventListener(any(TaskInstanceCreatedListener.class))).thenAnswer(invocation -> {});
 when(mockEventPublisher.removeEventListener(any(ActiveTaskStatisticsListener.class))).thenAnswer(invocation -> {});

 testDelegate.preHandle(mockExecution);
 testDelegate.run(mockExecution);

 // then
 verify(mockEventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
 verify(mockFlowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
 verify(mockFlowableAdaptor, times(1)).getTaskInstanceByActivityId(eq("activityId"), eq(1L));
 verify(mockEventPublisher, times(2)).removeEventListener(any(TaskInstanceCreatedListener.class));
 verify(mockEventPublisher, times(2)).removeEventListener(any(ActiveTaskStatisticsListener.class));
 verify(mockExecution, never()).setVariable(anyString(), any());
 }

 @Test
 public void testIsDone() {
 // given
 testDelegate.done = true;

 // when
 assertTrue(testDelegate.isDone());
 }

 @Test
 public void testGet() throws InterruptedException, ExecutionException {
 // given
 testDelegate.returnObject = "testResult";
 testDelegate.done = true;
 testDelegate.latch.countDown();

 // when
 assertEquals("testResult", testDelegate.get());
 }

 @Test(expected = InterruptedException.class)
 public void testGetInterruptedException() throws InterruptedException, ExecutionException {
 // given
 testDelegate.done = false;

 // when, then
 testDelegate.get();
 fail("InterruptedException should be thrown.");
 }

 @Test(expected = ExecutionException.class)
 public void testGetExecutionException() throws InterruptedException, ExecutionException {
 // given
 when(mockExecution.getVariable(eq("thrown"))).thenReturn(new RuntimeException("Test exception"));
 testDelegate.done = true;
 testDelegate.thrown = new RuntimeException("Test exception");
 testDelegate.latch.countDown();

 // when, then
 assertThrows(ExecutionException.class, () -> testDelegate.get());
 }

 @Test(expected = TimeoutException.class)
 public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
 // given
 testDelegate.done = false;
 testDelegate.latch.await(1, TimeUnit.MILLISECONDS);

 // when, then
 testDelegate.get(1, TimeUnit.MILLISECONDS);
 fail("TimeoutException should be thrown.");
 }

 @Test
 public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 // given
 testDelegate.returnObject = "testResult";
 testDelegate.done = true;
 testDelegate.latch.countDown();

 // when
 assertEquals("testResult", testDelegate.get(1, TimeUnit.MINUTES));
 }

}