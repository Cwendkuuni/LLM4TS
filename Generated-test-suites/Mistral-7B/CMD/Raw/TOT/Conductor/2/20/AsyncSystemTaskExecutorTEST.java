package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class AsyncSystemTaskExecutorTest {

 @MockBean
 ExecutionDAOFacade executionDAOFacade;
 @MockBean
 QueueDAO queueDAO;
 @MockBean
 MetadataDAO metadataDAO;
 @MockBean
 ConductorProperties conductorProperties;
 @Autowired
 AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 private TaskModel task;
 private String taskId = "taskId";
 private String workflowInstanceId = "workflowInstanceId";
 private WorkflowModel workflow;
 private WorkflowSystemTask systemTask;
 private String queueName;
 private String taskType = "TaskType";

 @Before
 public void setup() {
 task = new TaskModel();
 task.setTaskId(taskId);
 task.setWorkflowInstanceId(workflowInstanceId);
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setTaskDefName("TaskDefName");
 task.setTaskDef("TaskDef");
 workflow = new WorkflowModel();
 workflow.setId(workflowInstanceId);
 workflow.setStatus(WorkflowModel.Status.IN_PROGRESS);
 systemTask = mock(WorkflowSystemTask.class);
 queueName = QueueUtils.getQueueName(task);
 when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(null);
 when(executionDAOFacade.getWorkflowModel(workflowInstanceId, false)).thenReturn(workflow);
 when(queueDAO.get(queueName, taskId)).thenReturn(task);
 when(queueDAO.postpone(queueName, taskId, 0, 1000))
         .thenAnswer(invocation -> task.setCallbackAfterSeconds(1000));
 when(queueDAO.remove(queueName, taskId)).thenReturn(true);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 when(executionDAOFacade.updateTask(task)).thenReturn(task);
 when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(false);
 when(systemTask.isAsyncComplete(task)).thenReturn(false);
 }

 @Test
 public void testExecute_TaskNotFound_RemovesDeadTaskFromQueue() {
 // Given
 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Then
 assertFalse(queueDAO.get(queueName, taskId).isPresent());
 }

 @Test
 public void testExecute_TaskNotFound_DoesNotRemoveDeadTaskFromExecutionDAO() {
 // Given
 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Then
 assertNull(executionDAOFacade.getTaskModel(taskId));
 }

 @Test
 public void testExecute_TaskAlreadyCompleted_RemovesTaskFromQueue() {
 // Given
 task.setStatus(TaskModel.Status.COMPLETED);
 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Then
 assertFalse(queueDAO.get(queueName, taskId).isPresent());
 }

 @Test
 public void testExecute_TaskAlreadyCompleted_DoesNotRemoveTaskFromExecutionDAO() {
 // Given
 task.setStatus(TaskModel.Status.COMPLETED);
 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Then
 assertSame(task, executionDAOFacade.getTaskModel(taskId));
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_ConcurrentExecutionLimitExceeded_PostponesTask() {
 // Given
 when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
 // When, Then
 assertThrows(RuntimeException.class, () -> asyncSystemTaskExecutor.execute(systemTask, taskId));
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_RateLimitPerFrequencyExceeded_PostponesTask() {
 // Given
 when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(true);
 // When, Then
 assertThrows(RuntimeException.class, () -> asyncSystemTaskExecutor.execute(systemTask, taskId));
 }

 @Test
 public void testExecute_TaskAsyncComplete_RemovesTaskFromQueue() {
 // Given
 when(systemTask.isAsyncComplete(task)).thenReturn(true);
 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Then
 assertFalse(queueDAO.get(queueName, taskId).isPresent());
 }

 @Test
 public void testExecute_TaskAsyncComplete_DoesNotRemoveTaskFromExecutionDAO() {
 // Given
 when(systemTask.isAsyncComplete(task)).thenReturn(true);
 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Then
 assertSame(task, executionDAOFacade.getTaskModel(taskId));
 }

 @Test
 public void testExecute_WorkflowCompleted_DecidesWorkflow() {
 // Given
 when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Then
 assertFalse(queueDAO.get(queueName, taskId).isPresent());
 // WorkflowExecutor.decide() is not mocked in this test
 }
}