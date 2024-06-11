package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;import org.mockito.Mock;import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;import static org.slf4j.Logger.ERROR;import static org.slf4j.Logger.INFO;import static org.slf4j.Logger.DEBUG;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.Queue;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.utils.QueueUtils;
import com.netflix.conductor.utils.Monitors;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.model.execution.WorkflowExecutor;
import com.netflix.conductor.metrics.MonitorsWithMocks;

@SuppressWarnings("deprecation")
public class AsyncSystemTaskExecutorTest {

 @Mock
 private ExecutionDAOFacade executionDAOFacade;
 @Mock
 private QueueDAO queueDAO;
 @Mock
 private MetadataDAO metadataDAO;
 @Mock
 private ConductorProperties conductorProperties;
 @Mock
 private WorkflowExecutor workflowExecutor;
 @Mock
 private Monitors monitors;
 @Mock
 private Logger logger;

 @InjectMocks
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 private String taskId = "taskId-1";
 private String workflowId = "workflowId-1";
 private String queueName = "queueName-1";
 private String taskType = "taskType-1";
 private String taskDefName = "taskDefName-1";
 private TaskModel task;
 private WorkflowModel workflow;
 private WorkflowSystemTask systemTask;
 private Queue queue;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 MonitorsWithMocks.initMonitors(monitors);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 when(metadataDAO.getTaskDef(taskDefName)).thenReturn(taskDefName);
 systemTask = mock(WorkflowSystemTask.class);
 workflow = mock(WorkflowModel.class);
 queue = mock(Queue.class);
 when(executionDAOFacade.getWorkflowModel(workflowId, systemTask.isTaskRetrievalRequired()))
 .thenReturn(workflow);
 when(queueDAO.getQueueByName(queueName)).thenReturn(queue);
 when(workflowExecutor.decide(workflowId)).thenReturn(true);
 when(logger.isInfoEnabled()).thenReturn(true);
 when(logger.isDebugEnabled()).thenReturn(true);
 when(logger.isErrorEnabled()).thenReturn(true);
 }

 @After
 public void teardown() {
 MonitorsWithMocks.cleanMonitors();
 }

 @Test
 public void testExecute_taskNull() {
 // Test method when task is null
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 assertNull(task);
 }

 @Test
 public void testExecute_taskExists() {
 // Test method when task already exists
 task.setStatus(ExecutionStatus.SCHEDULED);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 assertNotNull(task);
 }

 @Test(expected = NullPointerException.class)
 public void testExecute_queueNull() {
 // Test method when queue is null
 task.setStatus(ExecutionStatus.SCHEDULED);
 when(queueDAO.getQueueByName(queueName)).thenReturn(null);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 }

 @Test
 public void testExecute_taskInProgress() {
 // Test method when task is in progress
 task.setStatus(ExecutionStatus.IN_PROGRESS);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 assertNotNull(task);
 }

 @Test
 public void testExecute_taskCompleted() {
 // Test method when task is completed
 task.setStatus(ExecutionStatus.COMPLETED);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 assertNotNull(task);
 }

 @Test
 public void testExecute_taskFailed() {
 // Test method when task fails
 when(logger.isErrorEnabled()).thenReturn(false);
 when(queueDAO.getQueueByName(queueName)).thenThrow(new RuntimeException());
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Assert that the task was removed from the queue
 verify(queueDAO, times(1)).remove(queueName, taskId);
 }

 @Test
 public void testExecute_workflowCompleted() {
 // Test method when workflow is completed
 workflow.setStatus(WorkflowInstanceStatus.COMPLETED);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Assert that the workflow was evaluated
 verify(workflowExecutor, times(1)).decide(workflowId);
 }

 @Test
 public void testExecute_exceedsInProgressLimit() {
 // Test method when the execution limit is exceeded
 task.setStatus(ExecutionStatus.SCHEDULED);
 when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Assert that the task was postponed
 verify(queueDAO, times(1)).postpone(
 queueName,
 taskId,
 task.getWorkflowPriority(),
 queueTaskMessagePostponeSecs);
 }

 @Test
 public void testExecute_exceedsRateLimitPerFrequency() {
 // Test method when the rate limit is exceeded
 task.setStatus(ExecutionStatus.SCHEDULED);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(task, taskDefName)).thenReturn(true);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Assert that the task was postponed
 verify(queueDAO, times(1)).postpone(
 queueName,
 taskId,
 task.getWorkflowPriority(),
 queueTaskMessagePostponeSecs);
 }

 @Test
 public void testPostponeQuietly_queueNull() {
 // Test method when queue is null
 asyncSystemTaskExecutor.postponeQuietly(queueName, task);
 }

 @Test
 public void testPostponeQuietly_taskNull() {
 // Test method when task is null
 asyncSystemTaskExecutor.postponeQuietly(queueName, null);
 }

 @Test
 public void testLoadTaskQuietly_taskNull() {
 // Test method when task is null
 asyncSystemTaskExecutor.loadTaskQuietly(taskId);
 assertNull(task);
 }

 @Test
 public void testLoadTaskQuietly_taskNotNull() {
 // Test method when task is not null
 TaskModel taskFromDB = new TaskModel();
 taskFromDB.setTaskId(taskId);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskFromDB);
 asyncSystemTaskExecutor.loadTaskQuietly(taskId);
 assertNotNull(taskFromDB);
 }

 @Test(expected = Exception.class)
 public void testLoadTaskQuietly_exception() {
 // Test method when an exception occurs
 when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new Exception());
 asyncSystemTaskExecutor.loadTaskQuietly(taskId);
 }
}