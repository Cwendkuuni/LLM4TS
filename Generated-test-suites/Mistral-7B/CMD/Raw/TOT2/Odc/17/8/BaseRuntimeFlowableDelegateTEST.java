package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

 private BaseRuntimeFlowableDelegate<Object> delegate;
 private EventPublisher eventPublisher;
 private FlowInstanceRepository flowInstanceRepository;
 private FlowableAdaptor flowableAdaptor;
 private RetryExecutor retryExecutor;
 private CountDownLatch latch;

 @Before
 public void setUp() {
 eventPublisher = mock(EventPublisher.class);
 flowInstanceRepository = mock(FlowInstanceRepository.class);
 flowableAdaptor = mock(FlowableAdaptor.class);
 retryExecutor = mock(RetryExecutor.class);
 latch = new CountDownLatch(1);
 delegate = new BaseRuntimeFlowableDelegate<Object>() {
 @Autowired
 public BaseRuntimeFlowableDelegate(EventPublisher eventPublisher, FlowInstanceRepository flowInstanceRepository,
 FlowableAdaptor flowableAdaptor, RetryExecutor retryExecutor) {
 super();
 this.eventPublisher = eventPublisher;
 this.flowInstanceRepository = flowInstanceRepository;
 this.flowableAdaptor = flowableAdaptor;
 this.retryExecutor = retryExecutor;
 }

 @Override
 protected Callable<Object> initCallable(DelegateExecution execution) {
 throw new UnsupportedOperationException("Method not implemented.");
 }
 };
 }

 @Test
 public void testInit() throws InterruptedException, ExecutionException, TimeoutException {
 DelegateExecution execution = mock(DelegateExecution.class);
 delegate.init(eventPublisher);
 delegate.run(execution);
 verify(eventPublisher).addEventListener(argThat(a -> a instanceof ActiveTaskStatisticsListener));
 verify(eventPublisher).addEventListener(argThat(a -> a instanceof TaskInstanceCreatedListener));
 }

 @Test(expected = ServiceTaskError.class)
 public void testInitializationFailure() throws InterruptedException, ExecutionException, TimeoutException {
 DelegateExecution execution = mock(DelegateExecution.class);
 when(execution.getCurrentActivityId()).thenThrow(new Exception("Initialization error"));
 delegate.run(execution);
 verify(eventPublisher).publishEvent(argThat(e -> e instanceof ServiceTaskStartedEvent));
 assertNull(delegate.get());
 }

 @Test
 public void testExecution() throws InterruptedException, ExecutionException {
 DelegateExecution execution = mock(DelegateExecution.class);
 when(execution.getCurrentActivityId()).thenReturn("activityId");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
 when(flowInstanceRepository.updateStatusById(1L, any(FlowStatus.class))).thenReturn(true);
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(new FlowTaskInstance());
 Callable<Object> callable = mock(Callable.class);
 delegate.bindToFlowTaskInstance(mock(FlowTaskInstance.class));
 delegate.preHandle(execution);
 delegate.updateFlowInstanceStatus(FlowStatus.STARTED);
 assertEquals(FlowStatus.STARTED, delegate.get());

 ArgumentCaptor<ServiceTaskStartedEvent> startEventArgumentCaptor = ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);
 verify(eventPublisher).publishEvent(startEventArgumentCaptor.capture());
 ServiceTaskStartedEvent serviceTaskStartedEvent = startEventArgumentCaptor.getValue();
 assertSame(Thread.currentThread(), serviceTaskStartedEvent.getThread());
 assertSame(delegate, serviceTaskStartedEvent.getTask());
 }

 @Test
 public void testIsDone() throws InterruptedException {
 delegate.done = true;
 assertTrue(delegate.isDone());
 }

 @Test
 public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
 delegate.latch.await();
 assertNull(delegate.get());
 }

 @Test
 public void testGetWithReturnValue() throws InterruptedException, ExecutionException, TimeoutException {
 Object returnObject = new Object();
 delegate.returnObject = returnObject;
 delegate.latch.await();
 assertEquals(returnObject, delegate.get());
 }

 @Test(expected = InterruptedException.class)
 public void testGetInterruptedException() throws InterruptedException, ExecutionException, TimeoutException {
 delegate.latch.await(1, TimeUnit.SECONDS);
 }

 @Test(expected = TimeoutException.class)
 public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
 delegate.latch.await(1, TimeUnit.MILLISECONDS);
 }

 @Test(expected = ExecutionException.class)
 public void testGetExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
 delegate.thrown = new Exception("Test exception");
 delegate.latch.await();
 assertEquals("Test exception", delegate.get().getCause().getMessage());
 }
}