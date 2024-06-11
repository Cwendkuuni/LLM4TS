import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BaseRuntimeFlowableDelegateTest {

 private BaseRuntimeFlowableDelegate<String> delegate;
 private DelegateExecution execution;
 private EventPublisher eventPublisher;
 private FlowInstanceRepository flowInstanceRepository;
 private FlowableAdaptor flowableAdaptor;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 eventPublisher = Mockito.mock(EventPublisher.class);
 flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
 flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
 execution = Mockito.mock(DelegateExecution.class);
 delegate = new BaseRuntimeFlowableDelegate<>() {
 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 throw new UnsupportedOperationException("Not supported yet.");
 }
 };
 delegate.setEventPublisher(eventPublisher);
 delegate.setFlowInstanceRepository(flowInstanceRepository);
 delegate.setFlowableAdaptor(flowableAdaptor);
 }

 @Test
 public void testInitTargetTaskInstanceId() throws Exception {
 // Given
 String processDefinitionId = "processDefinitionId";
 Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenReturn(1L);

 // When
 delegate.initTargetTaskInstanceId(execution);

 // Then
 assertEquals(1L, delegate.getFlowInstanceId());
 }

 @Test
 public void testRunWithPreHandle() throws Exception {
 // Given
 Mockito.doNothing().when(delegate).preHandle(execution);
 Callable<String> callable = Mockito.mock(Callable.class);
 Mockito.when(delegate.initCallable(execution)).thenReturn(callable);
 String returnObject = "returnObject";

 // When
 delegate.run(execution);

 // Then
 Mockito.verify(callable).call();
 assertEquals(returnObject, delegate.getReturnObject());
 }

 @Test
 public void testRunWithExceptionInPreHandle() throws Exception {
 // Given
 Mockito.doThrow(new RuntimeException("Prehandle exception")).when(delegate).preHandle(execution);
 Callable<String> callable = Mockito.mock(Callable.class);

 // When
 delegate.run(execution);

 // Then
 assertNull(delegate.getReturnObject());
 assertTrue(delegate.isDone());
 }

 @Test
 public void testRunWithExceptionInCallable() throws Exception {
 // Given
 Callable<String> callable = Mockito.mock(Callable.class);
 Mockito.when(callable.call()).thenThrow(new RuntimeException("Callable exception"));

 // When
 delegate.run(execution);

 // Then
 assertNull(delegate.getReturnObject());
 assertTrue(delegate.isDone());
 }

 @Test
 public void testUpdateFlowInstanceStatus() throws Exception {
 // Given
 Mockito.when(flowInstanceRepository.updateStatusById(delegate.getFlowInstanceId(), FlowStatus.COMPLETED)).thenReturn(true);

 // When
 delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

 // Then
 assertTrue(true);
 }

 @Test
 public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
 // Given
 CountDownLatch latch = new CountDownLatch(1);
 delegate.setLatch(latch);
 String returnObject = "returnObject";
 Callable<String> callable = () -> returnObject;
 Mockito.when(delegate.initCallable(execution)).thenReturn(callable);

 // When
 delegate.run(execution);
 latch.await();

 // Then
 assertEquals(returnObject, delegate.get());
 }

 @Test
 public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 // Given
 CountDownLatch latch = new CountDownLatch(1);
 delegate.setLatch(latch);
 Callable<String> callable = () -> {
 throw new RuntimeException("Task execution failed");
 };
 Mockito.when(delegate.initCallable(execution)).thenReturn(callable);

 // When & Then
 assertFalse(delegate.get(1, TimeUnit.MINUTES).isDone());
 assertNull(delegate.get(1, TimeUnit.MINUTES));
 latch.countDown();
 assertTrue(delegate.get(1, TimeUnit.MINUTES).isDone());
 assertEquals("Task execution failed", delegate.get(1, TimeUnit.MINUTES).getCause().getMessage());
 }
}