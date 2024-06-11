import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

 private BaseRuntimeFlowableDelegate<String> delegate;
 private DelegateExecution execution;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 this.execution = Mockito.mock(DelegateExecution.class);
 this.delegate = new BaseRuntimeFlowableDelegate<String>() {
 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 return Mockito.mock(Callable.class);
 }
 };
 }

 @Test
 public void testInitTaskInstanceId() {
 // given
 String processDefinitionId = "process_definition_id";
 when(delegate.flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenReturn(Optional.of(1L));

 // when
 delegate.initTargetTaskInstanceId(execution);

 // then
 assertEquals(1L, delegate.getFlowInstanceId());
 }

 @Test(expected = Exception.class)
 public void testInitTaskInstanceIdWithNoFlowInstanceFound() {
 // given
 when(delegate.flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.empty());

 // when
 delegate.initTargetTaskInstanceId(execution);

 // then
 fail("Should have thrown an exception");
 }

 @Test
 public void testGet() throws Exception {
 // given
 Callable<String> callable = Mockito.mock(Callable.class);
 delegate.returnObject = "result";
 when(callable.call()).thenReturn("result");
 when(callable.get()).thenReturn("result");

 // when
 Future<String> future = delegate;
 String result = future.get();

 // then
 assertEquals("result", result);
 }

 @Test(expected = InterruptedException.class)
 public void testGetWithInterrupt() throws Exception {
 // given
 Callable<String> callable = Mockito.mock(Callable.class);
 delegate.done = false;
 when(callable.call()).thenReturn("result");

 // when
 Future<String> future = delegate;
 Thread thread = new Thread(() -> future.get());
 thread.interrupt();

 // then
 fail("Should have thrown an InterruptedException");
 }

 @Test(expected = ExecutionException.class)
 public void testGetWithException() throws Exception {
 // given
 Callable<String> callable = Mockito.mock(Callable.class);
 delegate.thrown = new RuntimeException("test exception");
 when(callable.call()).thenThrow(new RuntimeException("test exception"));

 // when
 Future<String> future = delegate;
 String result = future.get();

 // then
 fail("Should have thrown an ExecutionException");
 }

 @Test(expected = TimeoutException.class)
 public void testGetWithTimeout() throws Exception {
 // given
 Callable<String> callable = Mockito.mock(Callable.class);
 delegate.done = false;
 when(callable.call()).thenAnswer(invocation -> {
 Thread.sleep(100);
 return null;
 });

 // when
 Future<String> future = delegate;
 String result = future.get(1, TimeUnit.SECONDS);

 // then
 fail("Should have thrown a TimeoutException");
 }

 @Test
 public void testUpdateFlowInstanceStatus() throws Exception {
 // given
 when(delegate.flowInstanceRepository.updateStatusById(anyLong(), any())).thenReturn(true);

 // when
 delegate.updateFlowInstanceStatus(FlowStatus.EXECUTED);

 // then
 verify(delegate.flowInstanceRepository, times(1)).updateStatusById(anyLong(), eq(FlowStatus.EXECUTED));
 }

 @Test
 public void testBindToFlowTaskInstance() {
 // given
 FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);

 // when
 delegate.bindToFlowTaskInstance(taskInstance);

 // then
 verify(taskInstance, times(1)).bindServiceTask(delegate);
 }
}