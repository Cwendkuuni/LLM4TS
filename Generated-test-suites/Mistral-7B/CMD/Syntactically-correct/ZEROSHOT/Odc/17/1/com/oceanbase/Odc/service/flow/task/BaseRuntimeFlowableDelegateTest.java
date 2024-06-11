package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

 private BaseRuntimeFlowableDelegate<String> delegate;
 private EventPublisher eventPublisher;
 private FlowableAdaptor flowableAdaptor;
 private FlowInstanceRepository flowInstanceRepository;

 @Before
 public void setup() {
 eventPublisher = mock(EventPublisher.class);
 flowableAdaptor = mock(FlowableAdaptor.class);
 flowInstanceRepository = mock(FlowInstanceRepository.class);
 delegate = new BaseRuntimeFlowableDelegate<>() {
 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 throw new UnsupportedOperationException("initCallable should be overridden in subclass");
 }
 };
 delegate.setFlowableAdaptor(flowableAdaptor);
 delegate.setEventPublisher(eventPublisher);
 delegate.setFlowInstanceRepository(flowInstanceRepository);
 }

 @Test
 public void testRun() throws Exception {
 // Given
 DelegateExecution execution = mock(DelegateExecution.class);
 when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(execution.getCurrentActivityId()).thenReturn("activityId");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(mock(FlowTaskInstance.class));

 // When
 delegate.run(execution);

 // Then
 verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
 }

 @Test
 public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
 // Given
 DelegateExecution execution = mock(DelegateExecution.class);
 when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(execution.getCurrentActivityId()).thenReturn("activityId");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(mock(FlowTaskInstance.class));

 delegate.run(execution);

 // When & Then
 assertFalse(delegate.isDone());

 delegate.get();

 assertTrue(delegate.isDone());
 assertNull(delegate.get());
 }

 @Test
 public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 // Given
 DelegateExecution execution = mock(DelegateExecution.class);
 when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(execution.getCurrentActivityId()).thenReturn("activityId");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(mock(FlowTaskInstance.class));

 delegate.run(execution);

 // When & Then
 assertFalse(delegate.isDone());

 delegate.get(1, TimeUnit.SECONDS);

 assertTrue(delegate.isDone());
 assertNull(delegate.get());
 }

 @Test(expected = InterruptedException.class)
 public void testInterruptedGet() throws InterruptedException, ExecutionException, TimeoutException {
 // Given
 DelegateExecution execution = mock(DelegateExecution.class);
 when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(execution.getCurrentActivityId()).thenReturn("activityId");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(mock(FlowTaskInstance.class));

 // When
 delegate.run(execution);
 Thread.currentThread().interrupt();

 // Then
 delegate.get();
 }

 @Test(expected = ExecutionException.class)
 public void testGetWithThrowable() throws InterruptedException, ExecutionException, TimeoutException {
 // Given
 DelegateExecution execution = mock(DelegateExecution.class);
 when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(execution.getCurrentActivityId()).thenReturn("activityId");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenThrow(new RuntimeException("test"));

 // When & Then
 delegate.run(execution);
 assertFalse(delegate.isDone());

 try {
 delegate.get();
 fail("Should throw ExecutionException");
 } catch (ExecutionException e) {
 assertTrue(e.getCause() instanceof RuntimeException);
 }

 assertTrue(delegate.isDone());
 assertNull(delegate.get());
 }

 @Test
 public void testIsDone() {
 // Given
 DelegateExecution execution = mock(DelegateExecution.class);
 when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(executation.getCurrentActivityId()).thenReturn("activityId");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(mock(FlowTaskInstance.class));

 delegate.run(execution);

 // When
 assertFalse(delegate.isDone());

 // When the result is available
 delegate.setDone(true);
 delegate.setReturnObject("result");

 // Then
 assertTrue(delegate.isDone());
 assertSame("result", delegate.get());
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 // Given
 delegate.setFlowInstanceId(1L);

 // When
 delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

 // Then
 verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.COMPLETED);
 }

 @Test
 public void testBindToFlowTaskInstance() {
 // Given
 FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);

 // When
 delegate.bindToFlowTaskInstance(taskInstance);

 // Then
 verify(taskInstance).bindServiceTask(delegate);
 }

}