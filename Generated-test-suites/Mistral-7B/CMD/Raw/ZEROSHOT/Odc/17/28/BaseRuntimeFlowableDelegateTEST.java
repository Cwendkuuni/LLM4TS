package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

 private BaseRuntimeFlowableDelegate<String> delegate;
 private EventPublisher eventPublisher;
 private FlowInstanceRepository flowInstanceRepository;
 private FlowableAdaptor flowableAdaptor;
 private CountDownLatch latch;
 private RetryExecutor retryExecutor;
 private TaskInstanceCreatedListener taskInstanceCreatedlistener;
 private ActiveTaskStatisticsListener activeTaskStatisticsListener;

 @Before
 public void setup() {
 eventPublisher = mock(EventPublisher.class);
 flowInstanceRepository = mock(FlowInstanceRepository.class);
 flowableAdaptor = mock(FlowableAdaptor.class);
 retryExecutor = mock(RetryExecutor.class);
 latch = new CountDownLatch(1);
 taskInstanceCreatedlistener = new TaskInstanceCreatedListener(any(BaseRuntimeFlowableDelegate.class).thenReturn(this.delegate));
 activeTaskStatisticsListener = new ActiveTaskStatisticsListener(any(BaseRuntimeFlowableDelegate.class).thenReturn(this.delegate));
 this.delegate = new BaseRuntimeFlowableDelegate<String>() {
 @Autowired
 @Override
 public void init(EventPublisher eventPublisher) {
 super.init(eventPublisher);
 this.eventPublisher = eventPublisher;
 this.flowInstanceRepository = flowInstanceRepository;
 this.flowableAdaptor = flowableAdaptor;
 this.retryExecutor = retryExecutor;
 }

 @Override
 protected void run(DelegateExecution execution) throws Exception {
 super.run(execution);
 }

 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 throw new UnsupportedOperationException();
 }

 @Override
 protected void preHandle(DelegateExecution execution) throws Exception {
 // Empty
 }
 };

 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
 .thenReturn(Optional.of(1L));
 when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class)))
 .thenReturn(true);
 }

 @Test
 public void testInitializeTaskInstanceId() {
 delegate.init(eventPublisher);
 delegate.initTargetTaskInstanceId(Mockito.mock(DelegateExecution.class));

 verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(anyString());
 verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), any(FlowStatus.class));
 }

 @Test
 public void testBindToFlowTaskInstance() {
 FlowTaskInstance mockTaskInstance = Mockito.mock(FlowTaskInstance.class);
 delegate.bindToFlowTaskInstance(mockTaskInstance);
 verify(mockTaskInstance).bindServiceTask(delegate);
 }

 @Test
 public void testUpdateFlowInstanceStatus() throws Exception {
 delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
 verify(flowInstanceRepository).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testInitCallable() throws Exception {
 delegate.initCallable(Mockito.mock(DelegateExecution.class));
 }

 @Test(expected = ServiceTaskError.class)
 public void testHandleException() throws Exception {
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
 .thenThrow(new RuntimeException("TestException"));
 delegate.init(eventPublisher);

 try {
 delegate.run(Mockito.mock(DelegateExecution.class));
 fail("Should have thrown ServiceTaskError");
 } catch (ServiceTaskError e) {
 // Expected
 }
 }

 @Test
 public void testPublishEvent() {
 ServiceTaskStartedEvent event = new ServiceTaskStartedEvent(delegate, Mockito.mock(Thread.class));
 delegate.init(eventPublisher);
 delegate.run(Mockito.mock(DelegateExecution.class));

 verify(eventPublisher).publishEvent(event);
 }

 @Test
 public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
 delegate.init(eventPublisher);
 delegate.run(Mockito.mock(DelegateExecution.class));
 latch.await();

 assertTrue(delegate.isDone());
 assertEquals("", delegate.get());
 }

 @Test
 public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 delegate.init(eventPublisher);
 delegate.run(Mockito.mock(DelegateExecution.class));

 assertFalse(delegate.isDone());
 try {
 delegate.get(1, TimeUnit.SECONDS);
 fail("Should have thrown TimeoutException");
 } catch (TimeoutException e) {
 // Expected
 }
 }

 @Test
 public void testIsDone() throws Exception {
 delegate.init(eventPublisher);
 delegate.run(Mockito.mock(DelegateExecution.class));
 delegate.done = true;
 latch.countDown();

 assertTrue(delegate.isDone());
 }

 @Test
 public void testLatch() throws Exception {
 delegate.init(eventPublisher);
 delegate.run(Mockito.mock(DelegateExecution.class));
 latch.await();

 assertTrue(delegate.isDone());
 }

 @After
 public void teardown() {
 eventPublisher.removeEventListener(taskInstanceCreatedlistener);
 eventPublisher.removeEventListener(activeTaskStatisticsListener);
 }
}