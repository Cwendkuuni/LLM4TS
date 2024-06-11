import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

 private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;
 private DelegateExecution delegateExecution;
 private Callable<String> callable;

 @BeforeEach
 public void setup() {
 baseRuntimeFlowableDelegate = Mockito.spy(new BaseRuntimeFlowableDelegate<String>() {
 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 callable = Mockito.mock(Callable.class);
 return callable;
 }
 });
 delegateExecution = Mockito.mock(DelegateExecution.class);
 }

 @Test
 public void testRun() throws Exception {
 // Given
 when(callable.call()).thenReturn("test result");
 when(delegateExecution.getCurrentActivityId()).thenReturn("test activity id");
 baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
 // When
 baseRuntimeFlowableDelegate.run(delegateExecution);
 // Then
 verify(callable, times(1)).call();
 verify(delegateExecution, times(1)).setVariable("returnObject", "test result");
 verify(delegateExecution, times(1)).setVariable("done", true);
 }

 @Test
 public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
 // Given
 when(callable.call()).thenReturn("test result");
 when(delegateExecution.getCurrentActivityId()).thenReturn("test activity id");
 baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
 baseRuntimeFlowableDelegate.run(delegateExecution);
 latch.await(); // Simulate the task completion
 // When
 String result = baseRuntimeFlowableDelegate.get();
 // Then
 Assertions.assertEquals("test result", result);
 }

 @Test
 public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 // Given
 when(callable.call()).thenReturn("test result");
 when(delegateExecution.getCurrentActivityId()).thenReturn("test activity id");
 baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
 // When
 Assertions.assertThrows(TimeoutException.class, () -> baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS));
 }

 @Test
 public void testIsDone() {
 // Given
 baseRuntimeFlowableDelegate.run(delegateExecution);
 // When
 Assertions.assertTrue(baseRuntimeFlowableDelegate.isDone());
 }

 @Test
 public void testExceptionScenario() throws Exception {
 // Given
 when(callable.call()).thenThrow(new RuntimeException("test exception"));
 when(delegateExecution.getCurrentActivityId()).thenReturn("test activity id");
 baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
 // When
 Assertions.assertThrows(ServiceTaskError.class, () -> baseRuntimeFlowableDelegate.run(delegateExecution));
 }

 @Test
 public void testExecutionStrategyConfig() {
 // Given
 ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
 strategyConfig.setStrategyName("test strategy");
 baseRuntimeFlowableDelegate.strategyConfig = strategyConfig;
 // When
 String strategyName = baseRuntimeFlowableDelegate.getStrategyConfig().getStrategyName();
 // Then
 Assertions.assertEquals("test strategy", strategyName);
 }
}