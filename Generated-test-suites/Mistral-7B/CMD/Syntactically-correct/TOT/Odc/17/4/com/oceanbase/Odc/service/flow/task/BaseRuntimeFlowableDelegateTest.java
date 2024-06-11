import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class BaseRuntimeFlowableDelegateTest {

 private BaseRuntimeFlowableDelegate<Object> delegate;
 private DelegateExecution execution;

 @Captor
 private ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventCaptor;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 this.execution = mock(DelegateExecution.class);
 this.delegate = new BaseRuntimeFlowableDelegate<Object>() {
 @Override
 protected Callable<Object> initCallable(DelegateExecution execution) {
 throw new UnsupportedOperationException();
 }
 };
 }

 @Test
 public void testRun() throws Exception {
 // Given
 DelegateExecution execution = Mockito.spy(this.execution);
 ProcessInstance processInstance = mock(ProcessInstance.class);
 Mockito.when(execution.getProcessInstance()).thenReturn(processInstance);

 // When
 delegate.run(execution);

 // Then
 verify(execution).getCurrentActivityId();
 verify(execution).getProcessDefinitionId();
 verify(processInstance).getId();

 // When & Then - ServiceTaskStartedEvent is published
 delegate.run(execution);
 verify(eventPublisher).publishEvent(serviceTaskStartedEventCaptor.capture());
 ServiceTaskStartedEvent event = serviceTaskStartedEventCaptor.getValue();
 assertThat(event.getDelegate()).isSameAs(delegate);
 assertThat(event.getThread()).isSameAs(Thread.currentThread());
 }

 @Test
 public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
 // Given
 Callable<Object> callable = mock(Callable.class);
 when(callable.call()).thenReturn(new Object());
 delegate.returnObject = new Object();

 // When
 delegate.done = true;
 when(delegate.call()).thenReturn(callable);

 // Then
 Object result = delegate.get();
 assertThat(result).isSameAs(delegate.returnObject);
 verify(callable, never()).call();
 }

 @Test
 public void testGet_InterruptedException() throws InterruptedException, ExecutionException {
 // Given
 Callable<Object> callable = mock(Callable.class);
 when(callable.call()).thenThrow(new InterruptedException("Interrupted"));
 delegate.done = false;
 when(delegate.call()).thenReturn(callable);

 // Then
 assertThatThrownBy(() -> delegate.get()).isInstanceOf(InterruptedException.class);
 }

 @Test
 public void testGet_ExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
 // Given
 Callable<Object> callable = mock(Callable.class);
 when(callable.call()).thenThrow(new ExecutionException("ExecutionException"));
 delegate.done = false;
 when(delegate.call()).thenReturn(callable);

 // Then
 assertThatThrownBy(() -> delegate.get()).isInstanceOf(ExecutionException.class);
 }

 @Test
 public void testGet_TimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
 // Given
 Callable<Object> callable = mock(Callable.class);
 when(callable.call()).thenThrow(new TimeoutException("TimeoutException"));
 delegate.done = false;
 when(delegate.call()).thenReturn(callable);

 // Then
 assertThatThrownBy(() -> delegate.get(1, TimeUnit.MILLISECONDS)).isInstanceOf(TimeoutException.class);
 }

}