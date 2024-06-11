package com.oceanbase.odc.service.flow.task;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BaseRuntimeFlowableDelegateTest {

 private BaseRuntimeFlowableDelegate<String> delegate;
 private DelegateExecution execution;
 private FlowInstanceRepository flowInstanceRepository;
 private CountDownLatch latch;
 private Callable<String> callable;

 @Before
 public void setup() {
 flowInstanceRepository = mock(FlowInstanceRepository.class);
 execution = mock(DelegateExecution.class);
 delegate = new BaseRuntimeFlowableDelegate<String>() {
 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 return callable = () -> "Task Execution Result";
 }
 };
 delegate.init(Mockito.mock(EventPublisher.class));
 delegate.setFlowInstanceRepository(flowInstanceRepository);
 latch = new CountDownLatch(1);
 }

 @Test
 public void testInit() {
 delegate.init(Mockito.mock(EventPublisher.class));
 }

 @Test
 public void testRunWithSuccess() throws Exception {
 delegate.run(execution);
 assertEquals("Task Execution Result", delegate.get());
 verify(flowInstanceRepository).updateStatusById(delegate.getFlowInstanceId(), FlowStatus.COMPLETED);
 }

 @Test
 public void testRunWithException() throws Exception {
 delegate.run(execution);
 thrown.printStackTrace();
 verify(flowInstanceRepository).updateStatusById(delegate.getFlowInstanceId(), FlowStatus.FAILED);
 }

 @Test
 public void testGet() throws InterruptedException, ExecutionException {
 delegate.run(execution);
 latch.await();
 assertEquals("Task Execution Result", delegate.get());
 }

 @Test
 public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 delegate.run(execution);
 latch.await(1, TimeUnit.SECONDS);
 assertNull(delegate.get());
 }

 @Test
 public void testBindToFlowTaskInstance() throws Exception {
 delegate.bindToFlowTaskInstance(Mockito.mock(FlowTaskInstance.class));
 }

 @Test(expected = ServiceTaskError.class)
 public void testRunWithError() throws Exception {
 thrown = new Exception("Test Exception");
 delegate.run(execution);
 assertEquals("Test Exception", thrown.getMessage());
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 Optional<Boolean> result = delegate.retryExecutor.run(() -> flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED), Objects::nonNull);
 verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.COMPLETED);
 assertEquals(true, result.isPresent());
 }
}