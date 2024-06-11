import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;

@ActiveProfiles("test")
@ContextConfiguration(classes = BaseRuntimeFlowableDelegate.class)
public class BaseRuntimeFlowableDelegateTest {

 private BaseRuntimeFlowableDelegate<String> delegate;
 @MockBean
 private FlowableAdaptor flowableAdaptor;
 @MockBean
 private EventPublisher eventPublisher;
 @MockBean
 private FlowInstanceRepository flowInstanceRepository;
 @Value("${test.processDefinitionId}")
 private String processDefinitionId;

 @Before
 public void setUp() {
 delegate = new TestBaseRuntimeFlowableDelegate();
 delegate.flowableAdaptor = flowableAdaptor;
 delegate.eventPublisher = eventPublisher;
 delegate.flowInstanceRepository = flowInstanceRepository;
 }

 @Test
 public void testInit() {
 // Basic functionality test
 delegate.init(eventPublisher);
 Mockito.verify(eventPublisher, Mockito.atLeastOnce()).addEventListener(ArgumentMatchers.isInstanceOf(ActiveTaskStatisticsListener.class));
 Mockito.verify(eventPublisher, Mockito.atLeastOnce()).addEventListener(ArgumentMatchers.isInstanceOf(TaskInstanceCreatedListener.class));
 }

 @Test(expected = AssertionError.class)
 public void testInitException() {
 // Test initialization exception
 Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenThrow(new RuntimeException());
 delegate.init(eventPublisher);
 }

 @Test
 public void testBindToFlowTaskInstance() {
 // Test binding to flow task instance
 FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);
 Mockito.when(flowableAdaptor.getTaskInstanceByActivityId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).thenReturn(taskInstance);
 delegate.bindToFlowTaskInstance(taskInstance);
 Mockito.verify(taskInstance).bindServiceTask(ArgumentMatchers.isInstanceOf(BaseRuntimeFlowableDelegate.class));
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 // Test updating flow instance status
 Mockito.when(flowInstanceRepository.updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(FlowStatus.COMPLETED.name()))).thenReturn(true);
 delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
 Mockito.verify(flowInstanceRepository).updateStatusById(ArgumentMatchers.eq(delegate.getFlowInstanceId()), ArgumentMatchers.eq(FlowStatus.COMPLETED.name()));
 }

 @Test(expected = AssertionError.class)
 public void testUpdateFlowInstanceStatusException() {
 // Test updating flow instance status exception
 Mockito.when(flowInstanceRepository.updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(FlowStatus.COMPLETED.name()))).thenThrow(new RuntimeException());
 delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
 }

 @Test
 public void testRun() {
 // Test running method
 DelegateExecution execution = Mockito.mock(DelegateExecution.class);
 Mockito.when(execution.getCurrentActivityId()).thenReturn("testActivityId");
 delegate.initTargetTaskInstanceId(execution);
 Callable<String> callable = Mockito.mock(Callable.class);
 Mockito.when(delegate.initCallable(execution)).thenReturn(callable);
 delegate.preHandle(execution);
 Mockito.verify(delegate, Mockito.times(1)).preHandle(ArgumentMatchito.eq(execution));
 delegate.run(execution);
 Mockito.verify(callable, Mockito.times(1)).call();
 }

 @Test(expected = AssertionError.class)
 public void testRunException() {
 // Test running method exception
 Callable<String> callable = Mockito.mock(Callable.class);
 Mockito.when(delegate.initCallable(Mockito.any())).thenReturn(callable);
 Mockito.when(callable.call()).thenThrow(new RuntimeException());
 delegate.run(Mockito.mock(DelegateExecution.class));
 }

 @Test
 public void testIsDone() {
 // Test is done method
 delegate.done = true;
 Assert.assertTrue(delegate.isDone());
 }

 @Test
 public void testIsDoneFalse() {
 // Test is done method false
 delegate.done = false;
 Assert.assertFalse(delegate.isDone());
 }

 @Test
 public void testGet() throws Exception {
 // Test get method
 delegate.latch.countDown();
 delegate.done = true;
 String result = delegate.get();
 Assert.assertEquals(null, result);
 }

 @Test(expected = InterruptedException.class)
 public void testGetInterrupted() throws Exception {
 // Test get method interrupted
 delegate.latch.countDown();
 Thread.currentThread().interrupt();
 delegate.get();
 }

 @Test(expected = ExecutionException.class)
 public void testGetExecutionException() throws Exception {
 // Test get method execution exception
 Mockito.when(delegate.thrown).thenReturn(new RuntimeException());
 delegate.latch.countDown();
 delegate.done = true;
 delegate.get();
 }

 @Test(expected = TimeoutException.class)
 public void testGetTimeoutException() throws Exception {
 // Test get method timeout exception
 delegate.latch.await(1, TimeUnit.SECONDS);
 delegate.get(1, TimeUnit.SECONDS);
 }
}