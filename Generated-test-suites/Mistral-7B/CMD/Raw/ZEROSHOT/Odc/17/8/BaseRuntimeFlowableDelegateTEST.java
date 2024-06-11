package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;

@SuppressWarnings("unchecked")
public class BaseRuntimeFlowableDelegateTest {

 private BaseRuntimeFlowableDelegate<String> delegate;
 @Mock
 private DelegateExecution execution;
 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private FlowableAdaptor flowableAdaptor;
 @Mock
 private FlowInstanceRepository flowInstanceRepository;
 @Mock
 private CountDownLatch latch;
 @Mock
 private RetryExecutor retryExecutor;
 @InjectMocks
 private BaseRuntimeFlowableDelegateTest.TestCallable callable;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 delegate = new BaseRuntimeFlowableDelegate<>();
 delegate.flowableAdaptor = flowableAdaptor;
 delegate.eventPublisher = eventPublisher;
 delegate.flowInstanceRepository = flowInstanceRepository;
 delegate.retryExecutor = retryExecutor;
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(mock(FlowTaskInstance.class));
 when(eventPublisher.publishEvent(any())).thenReturn(null);
 when(retryExecutor.run(any(), any())).thenReturn(true);
 }

 @Test
 public void testInit() {
 delegate.init(eventPublisher);
 verify(eventPublisher).addEventListener(any());
 verify(eventPublisher).addEventListener(any());
 }

 @Test
 public void testRunSuccess() throws Exception {
 delegate.activityId = "activityId";
 delegate.targetTaskInstanceId = 1L;
 delegate.taskType = TaskType.TASK_TYPE_1;
 delegate.flowInstanceId = 1L;
 delegate.strategyConfig = new ExecutionStrategyConfig();
 when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(execution.getCurrentActivityId()).thenReturn("activityId");
 when(flowInstanceRepository.updateStatusById(anyLong(), any())).thenReturn(1);
 delegate.preHandle(execution);
 delegate.run(execution);
 assertEquals(1, delegate.get().hashCode());
 verify(eventPublisher).publishEvent(new ServiceTaskStartedEvent(delegate, any()));
 }

 @Test(expected = ServiceTaskError.class)
 public void testRunFailure() throws Exception {
 delegate.activityId = "activityId";
 delegate.targetTaskInstanceId = 1L;
 delegate.taskType = TaskType.TASK_TYPE_1;
 delegate.flowInstanceId = 1L;
 delegate.strategyConfig = new ExecutionStrategyConfig();
 when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(execution.getCurrentActivityId()).thenReturn("activityId");
 when(flowInstanceRepository.updateStatusById(anyLong(), any())).thenThrow(new RuntimeException());
 delegate.preHandle(execution);
 delegate.run(execution);
 fail("Should throw ServiceTaskError");
 }

 @Test
 public void testGet() throws InterruptedException, ExecutionException {
 delegate.activityId = "activityId";
 delegate.targetTaskInstanceId = 1L;
 delegate.taskType = TaskType.TASK_TYPE_1;
 delegate.flowInstanceId = 1L;
 delegate.strategyConfig = new ExecutionStrategyConfig();
 when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(execution.getCurrentActivityId()).thenReturn("activityId");
 when(flowInstanceRepository.updateStatusById(anyLong(), any())).thenReturn(1);
 delegate.preHandle(execution);
 delegate.initCallable = callable;
 delegate.execute(execution);
 delegate.latch = latch;
 latch.await();
 assertEquals("result", delegate.get());
 }

 @Test(expected = InterruptedException.class)
 public void testGetInterrupted() throws InterruptedException, ExecutionException {
 delegate.activityId = "activityId";
 delegate.targetTaskInstanceId = 1L;
 delegate.taskType = TaskType.TASK_TYPE_1;
 delegate.flowInstanceId = 1L;
 delegate.strategyConfig = new ExecutionStrategyConfig();
 when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(execution.getCurrentActivityId()).thenReturn("activityId");
 when(flowInstanceRepository.updateStatusById(anyLong(), any())).thenReturn(1);
 delegate.preHandle(execution);
 delegate.initCallable = callable;
 delegate.execute(execution);
 latch.countDown();
 assertThrows(InterruptedException.class, () -> delegate.get());
 }

 @Test(expected = ExecutionException.class)
 public void testGetExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
 delegate.activityId = "activityId";
 delegate.targetTaskInstanceId = 1L;
 delegate.taskType = TaskType.TASK_TYPE_1;
 delegate.flowInstanceId = 1L;
 delegate.strategyConfig = new ExecutionStrategyConfig();
 when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(execution.getCurrentActivityId()).thenReturn("activityId");
 when(flowInstanceRepository.updateStatusById(anyLong(), any())).thenReturn(1);
 when(execution.get()).thenThrow(new RuntimeException());
 delegate.preHandle(execution);
 delegate.initCallable = callable;
 delegate.execute(execution);
 latch.await();
 fail("Should throw ExecutionException");
 }

 @Test(expected = TimeoutException.class)
 public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
 delegate.activityId = "activityId";
 delegate.targetTaskInstanceId = 1L;
 delegate.taskType = TaskType.TASK_TYPE_1;
 delegate.flowInstanceId = 1L;
 delegate.strategyConfig = new ExecutionStrategyConfig();
 when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(execution.getCurrentActivityId()).thenReturn("activityId");
 when(flowInstanceRepository.updateStatusById(anyLong(), any())).thenReturn(1);
 when(execution.get(anyLong(), any())).thenThrow(new TimeoutException());
 delegate.preHandle(execution);
 delegate.initCallable = callable;
 delegate.execute(execution);
 latch.await();
 fail("Should throw TimeoutException");
 }

 @Test
 public void testBindToFlowTaskInstance() {
 FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
 delegate.bindToFlowTaskInstance(flowTaskInstance);
 verify(flowTaskInstance).bindServiceTask(delegate);
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 delegate.updateFlowInstanceStatus(FlowStatus.STARTED);
 verify(retryExecutor).run(() -> flowInstanceRepository.updateStatusById(delegate.flowInstanceId, FlowStatus.STARTED),
         Objects::nonNull);
 }

 class TestCallable implements Callable<String> {
 @Override
 public String call() throws Exception {
 return "result";
 }
 }

}