import org.apache.commons.lang3.RandomUtils;import org.flowable.engine.common.api.FlowableResourceNotFoundException; import org.flowable.engine.common.api. FlowableRuntimeException; import org.flowable.engine.common.api.FlowableServiceTask; import org.flowable.engine.common.api.FlowableTask; import org.flowable.engine.common.api.FlowableTaskQuery; import org.flowable.engine.common.api.FlowableTaskService; import org.flowable.engine.common.api.FlowableType; import org.flowable.engine.impl.test.AbstractTest; import org.flowable.engine.impl.test.annotation.FlowableTest; import org.flowable.engine.impl.test.annotation.UsesEmbeddedEngine; import org.flowable.task.service.api.TaskQuery; import org.junit.Assert; import org.junit.Before; import org.junit.Test; import org.mockito.Mockito;

import java.util.Collections; import java.util.HashMap; import java.util.List; import java.util.Map; import java.util.Optional; import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.*; import static org.mockito.Mockito.*;

@FlowableTest
@UsesEmbbeddedEngine
public class FlowTaskInstanceTest extends AbstractTest {

 private FlowTaskInstance flowTaskInstance; private FlowableTaskService taskService; private FlowableRuntimeException exception;

 @Before public void setUp() { engine = testEngine; taskService = engine.getTaskService(); flowTaskInstance = new FlowTaskInstance(new TaskType("TestTask"), 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository); }

 @Test public void testCreateFlowTaskInstance() { flowTaskInstance.create(); Assert.assertNotNull(flowTaskInstance.getId()); }

 @Test(expected = FlowableResourceNotFoundException.class) public void testCreateFlowTaskInstanceWithExistingInstance() { flowTaskInstance = new FlowTaskInstance(new TaskType("TestTask"), 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository); flowTaskInstance.create(); flowTaskInstance = new FlowTaskInstance(new TaskType("TestTask"), 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository); verify(taskService, times(1)).saveTaskInstance(any()); verify(taskService, times(1)).getTaskQuery().list().isEmpty(); Assert.assertThrows(FlowableResourceNotFoundException.class, () -> flowTaskInstance.create()); }

 @Test public void testUpdateFlowTaskInstance() { flowTaskInstance.update(); Assert.assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus()); }

 @Test(expected = FlowableRuntimeException.class) public void testUpdateFlowTaskInstanceWithNonExistingInstance() { flowTaskInstance = new FlowTaskInstance(new TaskType("TestTask"), 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository); verify(taskService, times(1)).getTaskQuery().list().isEmpty(); Assert.assertThrows(FlowableRuntimeException.class, () -> flowTaskInstance.update()); }

 @Test public void testBindServiceTask() { BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class); when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId()); flowTaskInstance.bindServiceTask(target); Assert.assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus()); }

 @Test(expected = IllegalArgumentException.class) public void testBindServiceTaskWithInvalidTarget() { BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class); when(target.getTargetTaskInstanceId()).thenReturn(null); Assert.assertThrows(IllegalArgumentException.class, () -> flowTaskInstance.bindServiceTask(target)); }

 @Test public void testCompleteFlowTaskInstance() { flowTaskInstance.complete(false); List<Task> tasks = taskService.createTaskQuery().list(); Task task = tasks.get(0); Assert.assertEquals(FlowNodeStatus.COMPLETED, flowTaskInstance.getStatus()); verify(taskService, times(1)).complete(task.getId()); }

 @Test public void testAbortFlowTaskInstance() { flowTaskInstance.abort(); List<Task> tasks = taskService.createTaskQuery().list(); Task task = tasks.get(0); Assert.assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus()); verify(taskService, times(1)).complete(task.getId()); }

 @Test public void testCancelFlowTaskInstance() { BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class); flowTaskInstance.bindServiceTask(target); Assert.assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus()); flowTaskInstance.cancel(true); verify(taskService, times(1)).cancel(anyLong(), anyBoolean()); }

 @Test public void testIsPresentOnThisMachine() { BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class); flowTaskInstance.bindServiceTask(target); Assert.assertTrue(flowTaskInstance.isPresentOnThisMachine()); }

 @Test public void testIsDone() { BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class); flowTaskInstance.bindServiceTask(target); verify(target, never()).isDone(); Assert.assertFalse(flowTaskInstance.isDone()); }

 @Test public void testGetResult() throws InterruptedException, ExecutionException { BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class); flowTaskInstance.bindServiceTask(target); when(target.get()).thenReturn("Test Result"); Assert.assertEquals("Test Result", flowTaskInstance.getResult()); }

 @Test public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException { BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class); flowTaskInstance.bindServiceTask(target); when(target.get(30, TimeUnit.SECONDS)).thenThrow(new InterruptedException()); Assert.assertThrows(TimeoutException.class, () -> flowTaskInstance.getResult(30, TimeUnit.SECONDS)); }

 @Test public void testGetTargetDelegateClass() { Assert.assertEquals(BaseServiceTaskDelegate.class, flowTaskInstance.getTargetDelegateClass()); }

}