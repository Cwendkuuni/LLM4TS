package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks; import org.mockito.Mock; import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.QueueName;
import com.netflix.conductor.core.model.TaskDef;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.TaskStatus;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.execution.WorkflowExecutor;
import com.netflix.conductor.execution.WorkflowExecutorImpl;
import com.netflix.conductor.metrics.Monitors;

public class AsyncSystemTaskExecutorTest {

 @Mock private ExecutionDAOFacade executionDAOFacade;
 @Mock private QueueDAO queueDAO;
 @Mock private MetadataDAO metadataDAO;
 @Mock private ConductorProperties conductorProperties;
 @Mock private WorkflowExecutor workflowExecutor;
 @InjectMocks private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecuteNullTask() {
 // Given
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 String taskId = "taskId";

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Then
 verify(executionDAOFacade).remove(taskId);
 }

 @Test
 public void testExecuteCompletedTask() {
 // Given
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 TaskModel task = mock(TaskModel.class);
 when(task.getStatus()).thenReturn(ExecutionStatus.COMPLETED);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Then
 verify(queueDAO).remove(QueueName.getQueueNameFromTaskAndWorkflow(task), taskId);
 }

 @Test
 public void testExecuteTaskInProgress() {
 // Given
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 TaskModel task = mock(TaskModel.class);
 when(task.getStatus()).thenReturn(TaskStatus.IN_PROGRESS);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 when(workflowExecutor.decide(workflowId)).thenReturn(Optional.empty());

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Then
 verify(queueDAO).postpone(
 QueueName.getQueueNameFromTaskAndWorkflow(task),
 taskId,
 task.getWorkflowPriority(),
 asyncSystemTaskExecutor.queueTaskMessagePostponeSecs);
 }

 @Test
 public void testExecuteTaskScheduled() {
 // Given
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 TaskModel task = mock(TaskModel.class);
 when(task.getStatus()).thenReturn(TaskStatus.SCHEDULED);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 when(workflowExecutor.decide(workflowId)).thenReturn(Optional.empty());

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Then
 verify(queueDAO).remove(QueueName.getQueueNameFromTaskAndWorkflow(task), taskId);
 }

 @Test
 public void testExecuteTaskQueued() {
 // Given
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 TaskModel task = mock(TaskModel.class);
 when(task.getStatus()).thenReturn(TaskStatus.SCHEDULED);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 when(workflowExecutor.decide(workflowId)).thenReturn(Optional.of(new WorkflowModel()));

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Then
 verify(queueDAO).postpone(
 QueueName.getQueueNameFromTaskAndWorkflow(task),
 taskId,
 task.getWorkflowPriority(),
 asyncSystemTaskExecutor.systemTaskCallbackTime);
 }

 @Test
 public void testExecuteTaskWithInProgressLimit() {
 // Given
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 TaskModel task = mock(TaskModel.class);
 when(task.getStatus()).thenReturn(TaskStatus.SCHEDULED);
 when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 when(workflowExecutor.decide(workflowId)).thenReturn(Optional.of(new WorkflowModel()));

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Then
 verify(queueDAO).postpone(
 QueueName.getQueueNameFromTaskAndWorkflow(task),
 taskId,
 task.getWorkflowPriority(),
 asyncSystemTaskExecutor.queueTaskMessagePostponeSecs);
 }

 @Test
 public void testExecuteTaskWithRateLimit() {
 // Given
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 TaskModel task = mock(TaskModel.class);
 when(task.getStatus()).thenReturn(TaskStatus.SCHEDULED);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(task, mock(TaskDef.class))).thenReturn(true);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 when(workflowExecutor.decide(workflowId)).thenReturn(Optional.of(new WorkflowModel()));

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Then
 verify(queueDAO).postpone(
 QueueName.getQueueNameFromTaskAndWorkflow(task),
 taskId,
 task.getWorkflowPriority(),
 asyncSystemTaskExecutor.queueTaskMessagePostponeSecs);
 }

 @Test
 public void testExecuteTaskWithTerminalWorkflow() {
 // Given
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 TaskModel task = mock(TaskModel.class);
 when(task.getStatus()).thenReturn(TaskStatus.SCHEDULED);
 when(task.getWorkflowInstanceId()).thenReturn("workflowId");
 when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(new WorkflowModel());
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 when(workflowExecutor.decide(workflowId)).thenReturn(Optional.empty());

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Then
 verify(queueDAO).remove(QueueName.getQueueNameFromTaskAndWorkflow(task), taskId);
 }

 @Test
 public void testExecuteTaskWithTerminalTask() {
 // Given
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 TaskModel task = mock(TaskModel.class);
 when(task.getStatus()).thenReturn(TaskStatus.SCHEDULED);
 when(task.getStatus()).thenReturn(ExecutionStatus.COMPLETED);
 when(task.getWorkflowInstanceId()).thenReturn("workflowId");
 when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(new WorkflowModel());
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 when(workflowExecutor.decide(workflowId)).thenReturn(Optional.empty());

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Then
 verify(queueDAO).remove(QueueName.getQueueNameFromWorkflow(workflowId), taskId);
 }

 @Test
 public void testExecuteTaskWithCompletedWorkflow() {
 // Given
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 TaskModel task = mock(TaskModel.class);
 when(task.getStatus()).thenReturn(TaskStatus.SCHEDULED);
 when(task.getWorkflowInstanceId()).thenReturn("workflowId");
 when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(new WorkflowModel());
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 when(workflowExecutor.decide(workflowId)).thenReturn(Optional.empty());

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Then
 verify(queueDAO).remove(QueueName.getQueueNameFromWorkflow(workflowId), taskId);
 }

 @Test
 public void testPostponeNullTaskId() {
 // Given
 String queueName = QueueName.getQueueNameFromTaskAndWorkflow(mock(TaskModel.class));

 // When
 asyncSystemTaskExecutor.postponeQuietly(queueName, null);

 // Then
 // Nothing happens since postponeQuietly does not throw an exception for null taskId
 }

 @Test
 public void testPostponeTask() {
 // Given
 String queueName = QueueName.getQueueNameFromTaskAndWorkflow(mock(TaskModel.class));
 TaskModel task = mock(TaskModel.class);
 when(task.getCallbackAfterSeconds()).thenReturn(Duration.ofSeconds(1).toMillis());

 // When
 asyncSystemTaskExecutor.postponeQuietly(queueName, task);

 // Then
 verify(queueDAO).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), task.getCallbackAfterSeconds());
 }

 @Test
 public void testLoadTaskNullTaskId() {
 // Given
 String taskId = null;

 // When
 TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

 // Then
 assertNull(task);
 }

 @Test
 public void testLoadTaskEmptyTaskModel() {
 // Given
 String taskId = "taskId";
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(new TaskModel());

 // When
 TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

 // Then
 assertSame(task, asyncSystemTaskExecutor.loadTaskQuietly(taskId));
 }

 @Test
 public void testLoadTaskNotNull() {
 // Given
 String taskId = "taskId";
 TaskModel task = mock(TaskModel.class);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

 // When
 TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

 // Then
 assertSame(task, loadedTask);
 }
}