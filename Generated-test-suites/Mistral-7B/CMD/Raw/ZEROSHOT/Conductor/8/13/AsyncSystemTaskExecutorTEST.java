package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;import org.mockito.Mock;import org.mockito.MockitoAnnotations;import org.springframework.beans.factory.annotation.AutowiredAnnotatedFieldUtils;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.QueuePosition;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.WorkflowDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.Json;

public class AsyncSystemTaskExecutorTest {

 private static final String TASK_ID = "taskId";
 private static final String WORKFLOW_ID = "workflowId";
 private static final String WORKFLOW_TYPE = "workflowType";
 private static final String TASK_TYPE = "taskType";
 private static final String QUEUE_NAME = "queueName";
 private static final int WORKFLOW_PRIORITY = 1;
 private static final long WAIT_TIME = 1000L;

 @Mock
 private ExecutionDAOFacade executionDAOFacade;

 @Mock
 private QueueDAO queueDAO;

 @Mock
 private MetadataDAO metadataDAO;

 @Mock
 private ConductorProperties conductorProperties;

 @Mock
 private WorkflowDAO workflowDAO;

 @Mock
 private Monitors monitors;

 @Mock
 private WorkflowExecutor workflowExecutor;

 @InjectMocks
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 new AutowiredAnnotatedFieldUtils().autowire(asyncSystemTaskExecutor);
 }

 @Test
 public void testExecuteWithNonExistentTask() {
 // Given
 TaskModel task = mock(TaskModel.class);
 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(Optional.empty());

 // When
 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

 // Then
 verify(executionDAOFacade).getTaskModel(TASK_ID);
 verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
 }

 @Test
 public void testExecuteWithTaskInProgress() {
 // Given
 TaskModel task = mock(TaskModel.class);
 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(Optional.of(task));
 when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

 // When
 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

 // Then
 verify(executionDAOFacade).getTaskModel(TASK_ID);
 verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
 }

 @Test
 public void testExecuteWithTaskCompleted() {
 // Given
 TaskModel task = mock(TaskModel.class);
 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(Optional.of(task));
 when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

 // When
 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

 // Then
 verify(executionDAOFacade).getTaskModel(TASK_ID);
 verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
 }

 @Test
 public void testExecuteWithTaskFailed() {
 // Given
 TaskModel task = mock(TaskModel.class);
 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(Optional.of(task));
 when(task.getStatus()).thenReturn(ExecutionStatus.FAILED);

 // When
 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

 // Then
 verify(executionDAOFacade).getTaskModel(TASK_ID);
 verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
 }

 @Test
 public void testExecuteWithTaskTerminal() {
 // Given
 TaskModel task = mock(TaskModel.class);
 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(Optional.of(task));
 when(task.getStatus()).thenReturn(TaskModel.Status.FAILED);
 when(task.getReasonForIncompletion()).thenReturn("Workflow is in FAILED state");

 // When
 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

 // Then
 verify(executionDAOFacade).getTaskModel(TASK_ID);
 verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
 }

 @Test
 public void testExecuteWithTaskScheduled() {
 // Given
 TaskModel task = mock(TaskModel.class);
 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(Optional.of(task));
 when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
 when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(true);

 // When
 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

 // Then
 ArgumentCaptor<QueuePosition> queuePositionArgumentCaptor =
 ArgumentCaptor.forClass(QueuePosition.class);
 verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, WORKFLOW_PRIORITY, WAIT_TIME);
 verify(workflowExecutor).decide(WORKFLOW_ID);
 }

 @Test
 public void testExecuteWithTaskRateLimitExceeded() {
 // Given
 TaskModel task = mock(TaskModel.class);
 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(Optional.of(task));
 when(task.getTaskDefName()).thenReturn("taskDefName");
 when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(mock(WorkflowModel.class));
 when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(mock(WorkflowModel.class));
 when(task.getRateLimitPerFrequency()).thenReturn(1);

 // When
 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

 // Then
 verify(executionDAOFacade).getTaskModel(TASK_ID);
 verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
 }

 @Test
 public void testExecuteWithTaskExecutionLimitExceeded() {
 // Given
 TaskModel task = mock(TaskModel.class);
 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(Optional.of(task));
 when(task.getTaskDefName()).thenReturn("taskDefName");
 when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

 // When
 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

 // Then
 verify(executionDAOFacade).getTaskModel(TASK_ID);
 verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
 }

 @Test
 public void testExecuteWithTaskWorkflowTerminal() {
 // Given
 TaskModel task = mock(TaskModel.class);
 WorkflowModel workflow = mock(WorkflowModel.class);
 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(Optional.of(task));
 when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(Optional.of(workflow));
 when(workflow.getStatus()).thenReturn(ExecutionStatus.TERMINATED);

 // When
 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

 // Then
 ArgumentCaptor<TaskModel> taskArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);
 verify(executionDAOFacade).updateTask(taskArgumentCaptor.capture());
 assertEquals( TaskModel.Status.CANCELED, taskArgumentCaptor.getValue().getStatus() );
 }

 @Test
 public void testPostponeQuietlyWithTask() {
 // Given
 TaskModel task = mock(TaskModel.class);
 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(Optional.of(task));

 // When
 asyncSystemTaskExecutor.postponeQuietly(QUEUE_NAME, task);

 // Then
 verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, WORKFLOW_PRIORITY, WAIT_TIME);
 }

 @Test
 public void testPostponeQuietlyWithNullTask() {
 // Given

 // When
 asyncSystemTaskExecutor.postponeQuietly(QUEUE_NAME, null);

 // Then
 // No interaction with queueDAO should occur
 verify(queueDAO, never()).postpone(QUEUE_NAME, null, WORKFLOW_PRIORITY, WAIT_TIME);
 }
}