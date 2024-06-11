import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class BaseRuntimeFlowableDelegateTest {

 private BaseRuntimeFlowableDelegate<String> delegate;
 private Execution execution;
 private CountDownLatch latch;

 @Value("${test.activityId}")
 private String activityId;
 @Value("${test.flowInstanceId}")
 private Long flowInstanceId;
 @Value("${test.targetTaskInstanceId}")
 private Long targetTaskInstanceId;
 @Value("${test.taskType}")
 private TaskType taskType;
 @Autowired
 private EventPublisher eventPublisher;
 @Autowired
 private FlowInstanceRepository flowInstanceRepository;

 @PostConstruct
 public void init() {
 this.execution = mock(Execution.class);
 this.latch = new CountDownLatch(1);
 this.delegate = new BaseRuntimeFlowableDelegate<>() {
 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 throw new UnsupportedOperationException();
 }

 @Override
 protected void bindToFlowTaskInstance(FlowTaskInstance taskInstance) {
 super.bindToFlowTaskInstance(taskInstance);
 }

 @Override
 protected void preHandle(DelegateExecution execution) {
 // Do nothing
 }

 @Override
 protected void updateFlowInstanceStatus(FlowStatus flowStatus) {
 // Do nothing
 }
 };
 this.delegate.init(eventPublisher);
 }

 @Before
 public void setup() {
 when(execution.getId()).thenReturn(1L);
 when(execution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");
 when(execution.getCurrentActivityId()).thenReturn(activityId);
 when(execution.getVariable("flowInstanceId")).thenReturn(flowInstanceId);
 when(execution.getVariable("targetTaskInstanceId")).thenReturn(targetTaskInstanceId);
 when(execution.getVariable("taskType")).thenReturn(taskType);
 }

 @Test
 public void testRun() throws Exception {
 delegate.run(execution);
 assertThat(delegate.isDone()).isTrue();
 }

 @Test
 public void testGet() throws InterruptedException, ExecutionException {
 delegate.get();
 latch.await(1, TimeUnit.MINUTES);
 assertThat(delegate.isDone()).isTrue();
 }

 @Test
 public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 delegate.get(1, TimeUnit.MILLISECONDS);
 }

 @Test(expected = TimeoutException.class)
 public void testGetWithTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
 delegate.get(1, TimeUnit.SECONDS);
 }

 @Test(expected = ServiceTaskError.class)
 public void testThrownException() throws Exception {
 when(execution.getVariable("thrown")).thenThrow(new RuntimeException("Test Exception"));
 delegate.run(execution);
 assertThat(delegate.isDone()).isTrue();
 assertThat(delegate.get()).isInstanceOf(RuntimeException.class);
 }

 @Test
 public void testBindToFlowTaskInstance() {
 FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
 delegate.bindToFlowTaskInstance(taskInstance);
 assertThat(taskInstance.isBound()).isTrue();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBindToFlowTaskInstanceWithNullTaskInstance() {
 delegate.bindToFlowTaskInstance(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testRunTwice() throws Exception {
 delegate.run(execution);
 delegate.run(execution);
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateFlowInstanceStatusWithNullStatus() {
 delegate.updateFlowInstanceStatus(null);
 }
}