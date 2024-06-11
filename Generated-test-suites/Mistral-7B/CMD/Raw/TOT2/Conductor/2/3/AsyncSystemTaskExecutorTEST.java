package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.MonitorRegistry;
import com.netflix.conductor.model.WorkflowInstanceStatus;

public class AsyncSystemTaskExecutorTest {

 private AsyncSystemTaskExecutor executor;
 private ExecutionDAOFacade executionDAOFacadeMock;
 private QueueDAO queueDAOMock;
 private MetadataDAO metadataDAOMock;
 private ConductorProperties conductorPropertiesMock;
 private WorkflowExecutor workflowExecutorMock;
 private QueueDAOImpl queueDAO;
 private MonitorRegistry monitorRegistryMock;

 @Before
 public void setup() {
 executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
 queueDAOMock = mock(QueueDAO.class);
 metadataDAOMock = mock(MetadataDAO.class);
 conductorPropertiesMock = mock(ConductorProperties.class);
 workflowExecutorMock = mock(WorkflowExecutor.class);
 queueDAO = new QueueDAOImpl();
 monitorRegistryMock = mock(MonitorRegistry.class);

 executor = new AsyncSystemTaskExecutor(
 executionDAOFacadeMock,
 queueDAOMock,
 metadataDAOMock,
 conductorPropertiesMock,
 workflowExecutorMock);
 }

 @Test
 public void testExecute_taskNotFound() {
 // Given
 String taskId = "123";
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(null);

 // When
 executor.execute(systemTask, taskId);

 // Then
 verify(queueDAOMock).remove(eq(QueueUtils.getQueueName(null)), eq(taskId));
 }

 @Test
 public void testExecute_taskInProgress() {
 // Given
 String taskId = "123";
 TaskModel task = Mockito.mock(TaskModel.class);
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

 // When
 executor.execute(systemTask, taskId);

 // Then
 verify(executionDAOFacadeMock, never()).updateTask(task);
 }

 @Test
 public void testExecute_taskCompleted() {
 // Given
 String taskId = "123";
 TaskModel task = Mockito.mock(TaskModel.class);
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

 // When
 executor.execute(systemTask, taskId);

 // Then
 verify(queueDAOMock).remove(eq(QueueUtils.getQueueName(task)), eq(taskId));
 verify(workflowExecutorMock).decide(eq(task.getWorkflowInstanceId()));
 }

 @Test
 public void testExecute_taskTerminal() {
 // Given
 String taskId = "123";
 TaskModel task = Mockito.mock(TaskModel.class);
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 when(task.getStatus()).thenReturn(TaskModel.Status.FAILED);
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

 // When
 executor.execute(systemTask, taskId);
 // Then
 verify(queueDAOMock).remove(eq(QueueUtils.getQueueName(task)), eq(taskId));
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_queueRemoveException() {
 // Given
 String taskId = "123";
 TaskModel task = Mockito.mock(TaskModel.class);
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

 when(queueDAOMock.remove(anyString(), eq(taskId))).thenThrow(new RuntimeException());

 // When
 executor.execute(systemTask, taskId);

 // Then
 verify(executionDAOFacadeMock).updateTask(task);
 }

 @Test
 public void testExecute_exceedsInProgressLimit() {
 // Given
 String taskId = "123";
 String taskType = "taskType";
 TaskModel task = Mockito.mock(TaskModel.class);
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
 when(executionDAOFacadeMock.exceedsInProgressLimit(task)).thenReturn(true);
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

 // When
 executor.execute(systemTask, taskId);

 // Then
 verify(queueDAOMock).postpone(
 eq(QueueUtils.getQueueName(task)),
 eq(taskId),
 eq(task.getWorkflowPriority()),
 eq(queueTaskMessagePostponeSecs));
 }

 @Test
 public void testExecute_exceedsRateLimitPerFrequency() {
 // Given
 String taskId = "123";
 String taskType = "taskType";
 TaskModel task = Mockito.mock(TaskModel.class);
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
 when(task.getRateLimitPerFrequency()).thenReturn(1);
 when(metadataDAOMock.getTaskDef(task.getTaskDefName())).thenReturn(new WorkflowModel());
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

 // When
 executor.execute(systemTask, taskId);

 // Then
 verify(queueDAOMock).postpone(
 eq(QueueUtils.getQueueName(task)),
 eq(taskId),
 eq(task.getWorkflowPriority()),
 eq(queueTaskMessagePostponeSecs));
 }

 @Test
 public void testExecute_executionCompleted() {
 // Given
 String taskId = "123";
 String taskType = "taskType";
 TaskModel task = Mockito.mock(TaskModel.class);
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
 WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
 when(executionDAOFacadeMock.getWorkflowModel(task.getWorkflowInstanceId(), false)).thenReturn(workflow);
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

 // When
 executor.execute(systemTask, taskId);

 // Then
 verify(workflowExecutorMock).decide(task.getWorkflowInstanceId());
 }

 @Test
 public void testExecute_workflowCompleted() {
 // Given
 String taskId = "123";
 String workflowId = "workflowId";
 String taskType = "taskType";
 TaskModel task = Mockito.mock(TaskModel.class);
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
 WorkflowModel workflow = new WorkflowModel();
 when(workflow.getStatus()).thenReturn(WorkflowInstanceStatus.SUCCEEDED);
 when(executionDAOFacadeMock.getWorkflowModel(task.getWorkflowInstanceId(), true)).thenReturn(workflow);
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

 // When
 executor.execute(systemTask, taskId);

 // Then
 verify(queueDAOMock).remove(eq(QueueUtils.getQueueName(task)), eq(taskId));
 }

 @Test
 public void testExecute_workflowFailed() {
 // Given
 String taskId = "123";
 String workflowId = "workflowId";
 String taskType = "taskType";
 TaskModel task = Mockito.mock(TaskModel.class);
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
 WorkflowModel workflow = new WorkflowModel();
 when(workflow.getStatus()).thenReturn(WorkflowInstanceStatus.FAILED);
 when(executionDAOFacadeMock.getWorkflowModel(task.getWorkflowInstanceId(), true)).thenReturn(workflow);
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

 // When
 executor.execute(systemTask, taskId);

 // Then
 verify(queueDAOMock).remove(eq(QueueUtils.getQueueName(task)), eq(taskId));
 }

 @Test
 public void testExecute_workflowTerminal() {
 // Given
 String taskId = "123";
 String workflowId = "workflowId";
 String taskType = "taskType";
 TaskModel task = Mockito.mock(TaskModel.class);
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
 WorkflowModel workflow = new WorkflowModel();
 when(workflow.getStatus()).thenReturn(WorkflowInstanceStatus.TERMINATED);
 when(executionDAOFacadeMock.getWorkflowModel(task.getWorkflowInstanceId(), true)).thenReturn(workflow);
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

 // When
 executor.execute(systemTask, taskId);

 // Then
 verify(queueDAOMock).remove(eq(QueueUtils.getQueueName(task)), eq(taskId));
 }
}