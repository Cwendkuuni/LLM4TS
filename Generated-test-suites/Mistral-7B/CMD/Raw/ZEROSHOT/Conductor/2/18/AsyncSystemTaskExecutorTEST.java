package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;

@SpringBootTest
@MockitoJUnit public class AsyncSystemTaskExecutorTest {

 @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

 @InjectMocks private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 @Autowired private ExecutionDAOFacade executionDAOFacade;
 @Autowired private QueueDAO queueDAO;
 @Autowired private MetadataDAO metadataDAO;
 @Autowired private ConductorProperties conductorProperties;
 @Autowired private WorkflowExecutor workflowExecutor;

 private WorkflowSystemTask systemTask;
 private TaskModel task;
 private String taskId = "taskId";
 private String workflowId = "workflowId";
 private String queueName;

 @Before public void setup() {
 systemTask = mock(WorkflowSystemTask.class);
 queueName = QueueUtils.getQueueName(task);
 task = new TaskModel();
 task.setTaskId(taskId);
 task.setWorkflowInstanceId(workflowId);
 }

 @After public void tearDown() {
 // Clean up
 queueDAO.remove(queueName, taskId);
 }

 @Test public void testExecuteNullTask() {
 // Test case for null taskId
 asyncSystemTaskExecutor.execute(systemTask, null);
 assertNull(task);
 }

 @Test public void testExecuteNullSystemTask() {
 // Test case for null systemTask
 asyncSystemTaskExecutor.execute(null, taskId);
 }

 @Test public void testExecuteTaskNotFound() {
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 assertFalse(queueDAO.isPresentInQueue(queueName, taskId));
 }

 @Test public void testExecuteCompletedTask() {
 // Set workflow status to COMPLETED
 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.Status.COMPLETED);
 when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 assertFalse(queueDAO.isPresentInQueue(queueName, taskId));
 }

 @Test public void testExecuteScheduledTask() {
 // Set workflow status to SCHEDULED
 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.Status.SCHEDULED);
 when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
 when(executionDAOFacade.isInProgressLimitExceeded(task)).thenReturn(false);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(false);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 assertSame(TaskModel.Status.IN_PROGRESS, task.getStatus());
 }

 @Test public void testExecuteInProgressTask() {
 // Set workflow status to IN_PROGRESS
 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.Status.IN_PROGRESS);
 when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 assertSame(TaskModel.Status.IN_PROGRESS, task.getStatus());
 }

 @Test public void testExecuteTaskPostponed() {
 // Set workflow status to SCHEDULED and task status to IN_PROGRESS
 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.Status.SCHEDULED);
 TaskModel taskWithCallback = new TaskModel();
 taskWithCallback.setTaskId(taskId);
 taskWithCallback.setWorkflowInstanceId(workflowId);
 taskWithCallback.setStatus(TaskModel.Status.IN_PROGRESS);
 taskWithCallback.setCallbackAfterSeconds(1000L);
 when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskWithCallback);
 when(executionDAOFacade.isInProgressLimitExceeded(task)).thenReturn(false);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(false);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 assertSame(TaskModel.Status.SCHEDULED, task.getStatus());
 }

 @Test public void testExecuteCompletedTaskWithSystemTaskCallback() {
 // Set workflow status to COMPLETED and task status to IN_PROGRESS
 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.Status.COMPLETED);
 TaskModel taskWithCallback = new TaskModel();
 taskWithCallback.setTaskId(taskId);
 taskWithCallback.setWorkflowInstanceId(workflowId);
 taskWithCallback.setStatus(TaskModel.Status.IN_PROGRESS);
 when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskWithCallback);
 when(systemTask.isAsyncComplete(task)).thenReturn(true);
 when(executionDAOFacade.isInProgressLimitExceeded(task)).thenReturn(false);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(false);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 assertFalse(queueDAO.isPresentInQueue(queueName, taskId));
 }

 @Test public void testExecuteCompletedTaskWithSystemTaskCallbackAndTaskNotFound() {
 // Set workflow status to COMPLETED and task status to IN_PROGRESS
 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.Status.COMPLETED);
 TaskModel taskWithCallback = new TaskModel();
 taskWithCallback.setTaskId(taskId);
 taskWithCallback.setWorkflowInstanceId(workflowId);
 taskWithCallback.setStatus(TaskModel.Status.IN_PROGRESS);
 when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
 when(systemTask.isAsyncComplete(task)).thenReturn(true);
 when(executionDAOFacade.isInProgressLimitExceeded(task)).thenReturn(false);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(false);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 assertFalse(queueDAO.isPresentInQueue(queueName, taskId));
 }

 @Test public void testExecuteTaskWithSystemTaskCallbackAndQueueSizeLimitExceeded() {
 // Set workflow status to SCHEDULED and task status to IN_PROGRESS
 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.Status.SCHEDULED);
 TaskModel taskWithCallback = new TaskModel();
 taskWithCallback.setTaskId(taskId);
 taskWithCallback.setWorkflowInstanceId(workflowId);
 taskWithCallback.setStatus(TaskModel.Status.IN_PROGRESS);
 when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskWithCallback);
 when(queueDAO.getQueueSize(queueName)).thenReturn(10L);
 when(executionDAOFacade.isInProgressLimitExceeded(task)).thenReturn(true);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(false);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 assertSame(TaskModel.Status.SCHEDULED, task.getStatus());
 }

 @Test public void testExecuteTaskWithSystemTaskCallbackAndRateLimitPerFrequencyExceeded() {
 // Set workflow status to SCHEDULED and task status to IN_PROGRESS
 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.Status.SCHEDULED);
 TaskModel taskWithCallback = new TaskModel();
 taskWithCallback.setTaskId(taskId);
 taskWithCallback.setWorkflowInstanceId(workflowId);
 taskWithCallback.setStatus(TaskModel.Status.IN_PROGRESS);
 when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskWithCallback);
 when(queueDAO.getQueueSize(queueName)).thenReturn(0L);
 when(executionDAOFacade.isInProgressLimitExceeded(task)).thenReturn(false);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(true);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 assertSame(TaskModel.Status.SCHEDULED, task.getStatus());
 }

 @Test public void testExecuteTaskAndWorkflowDecided() {
 // Set workflow status to SCHEDULED and task status to IN_PROGRESS
 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.Status.SCHEDULED);
 TaskModel taskWithCallback = new TaskModel();
 taskWithCallback.setTaskId(taskId);
 taskWithCallback.setWorkflowInstanceId(workflowId);
 taskWithCallback.setStatus(TaskModel.Status.IN_PROGRESS);
 when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskWithCallback);
 when(queueDAO.getQueueSize(queueName)).thenReturn(0L);
 when(executionDAOFacade.isInProgressLimitExceeded(task)).thenReturn(false);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(false);
 when(workflowExecutor.decide(workflowId)).thenReturn(Optional.of(WorkflowModel.Status.FAILED));
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 assertSame(TaskModel.Status.FAILED, task.getStatus());
 }

 @Test public void testExecuteTaskAndWorkflowDecidedWithEmptyWorkflow() {
 // Set workflow status to SCHEDULED and task status to IN_PROGRESS
 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.Status.SCHEDULED);
 TaskModel taskWithCallback = new TaskModel();
 taskWithCallback.setTaskId(taskId);
 taskWithCallback.setWorkflowInstanceId(workflowId);
 taskWithCallback.setStatus(TaskModel.Status.IN_PROGRESS);
 when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskWithCallback);
 when(queueDAO.getQueueSize(queueName)).thenReturn(0L);
 when(executionDAOFacade.isInProgressLimitExceeded(task)).thenReturn(false);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(false);
 when(workflowExecutor.decide(workflowId)).thenReturn(Optional.empty());
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 assertSame(TaskModel.Status.FAILED, task.getStatus());
 }

 @Test public void testPostponeTask() {
 // Test postpone task method
 asyncSystemTaskExecutor.postponeQuietly(queueName, task);
 Optional<TaskModel> taskFromQueue = queueDAO.getTaskById(queueName, taskId);
 assertSame(queueTaskMessagePostponeSecs, taskFromQueue.get().getCallbackAfterSeconds());
 }
}