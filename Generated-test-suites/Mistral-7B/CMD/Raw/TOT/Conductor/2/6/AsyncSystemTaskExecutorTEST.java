package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;

@Component
public class AsyncSystemTaskExecutorTest {

 @Mock private ExecutionDAOFacade executionDAOFacade;
 @Mock private QueueDAO queueDAO;
 @Mock private MetadataDAO metadataDAO;
 @Mock private WorkflowExecutor workflowExecutor;
 @Mock private ConductorProperties conductorProperties;
 @InjectMocks private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(5));
 when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(10));
 }

 @Test
 public void testExecute_taskNotFoundInDB() {
 String taskId = "taskId";
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 doNothing().when(queueDAO).remove(anyString(), eq(taskId));
 doThrow(new RuntimeException("DB Exception")).when(executionDAOFacade).getTaskModel(taskId);

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
 }

 @Test
 public void testExecute_taskIsTerminal() {
 String workflowId = "workflowId";
 String taskId = "taskId";
 WorkflowModel workflow = new WorkflowModel();
 workflow.setId(workflowId);
 workflow.setStatus(WorkflowStatus.COMPLETED);
 TaskModel task = new TaskModel();
 task.setId(taskId);
 task.setWorkflowInstanceId(workflowId);
 task.setStatus(TaskModel.Status.SCHEDULED);
 doReturn(workflow).when(executionDAOFacade).getWorkflowModel(workflowId, false);
 doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
 doReturn(workflow).when(executionDAOFacade).getWorkflowModel(workflowId, true);
 doReturn(Optional.empty()).when(metadataDAO).getTaskDef(task.getTaskDefName());
 doNothing().when(queueDAO).remove(anyString(), eq(taskId));

 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

 verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
 }

 @Test
 public void testExecute_taskIsScheduledAndExceedsExecutionLimit() {
 String workflowId = "workflowId";
 String taskId = "taskId";
 WorkflowModel workflow = new WorkflowModel();
 workflow.setId(workflowId);
 workflow.setStatus(WorkflowStatus.RUNNING);
 TaskModel task = new TaskModel();
 task.setId(taskId);
 task.setWorkflowInstanceId(workflowId);
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setInProgressLimit(1);
 task.setRateLimitPerFrequency(2);
 doReturn(workflow).when(executionDAOFacade).getWorkflowModel(workflowId, false);
 doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
 doReturn(Optional.of(workflow)).when(metadataDAO).getTaskDef(task.getTaskDefName());
 doNothing().when(queueDAO).remove(anyString(), eq(taskId));
 doThrow(new RuntimeException("Exception")).when(executionDAOFacade).exceedsInProgressLimit(task);

 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

 verify(queueDAO, times(1)).postpone(
         QueueUtils.getQueueName(task), taskId, task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
 }

 @Test
 public void testExecute_taskIsScheduledAndExceedsRateLimit() {
 String workflowId = "workflowId";
 String taskId = "taskId";
 WorkflowModel workflow = new WorkflowModel();
 workflow.setId(workflowId);
 workflow.setStatus(WorkflowStatus.RUNNING);
 TaskModel task = new TaskModel();
 task.setId(taskId);
 task.setWorkflowInstanceId(workflowId);
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setInProgressLimit(1);
 task.setRateLimitPerFrequency(2);
 task.setRateLimitForFrequency(Instant.now().minus(Duration.ofMinutes(10)));
 doReturn(workflow).when(executionDAOFacade).getWorkflowModel(workflowId, false);
 doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
 doReturn(Optional.of(workflow)).when(metadataDAO).getTaskDef(task.getTaskDefName());
 doNothing().when(queueDAO).remove(anyString(), eq(taskId));
 doThrow(new RuntimeException("Exception")).when(executionDAOFacade).exceedsRateLimitPerFrequency(task, workflow);

 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

 verify(queueDAO, times(1)).postpone(
         QueueUtils.getQueueName(task), taskId, task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
 }

 @Test
 public void testExecute_taskIsInProgress() {
 String workflowId = "workflowId";
 String taskId = "taskId";
 WorkflowModel workflow = new WorkflowModel();
 workflow.setId(workflowId);
 workflow.setStatus(WorkflowStatus.RUNNING);
 TaskModel task = new TaskModel();
 task.setId(taskId);
 task.setWorkflowInstanceId(workflowId);
 task.setStatus(TaskModel.Status.IN_PROGRESS);
 doReturn(workflow).when(executionDAOFacade).getWorkflowModel(workflowId, false);
 doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
 doReturn(Optional.empty()).when(metadataDAO).getTaskDef(task.getTaskDefName());
 doReturn(mock(WorkflowSystemTask.class)).when(systemTaskCallbackTime).isAsyncComplete(task);
 doNothing().when(queueDAO).remove(anyString(), eq(taskId));

 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

 verify(queueDAO, never()).remove(anyString(), eq(taskId));
 }

 @Test
 public void testExecute_taskIsInProgressAndCompleted() {
 String workflowId = "workflowId";
 String taskId = "taskId";
 WorkflowModel workflow = new WorkflowModel();
 workflow.setId(workflowId);
 workflow.setStatus(WorkflowStatus.RUNNING);
 TaskModel task = new TaskModel();
 task.setId(taskId);
 task.setWorkflowInstanceId(workflowId);
 task.setStatus(TaskModel.Status.IN_PROGRESS);
 doReturn(workflow).when(executionDAOFacade).getWorkflowModel(workflowId, false);
 doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
 doReturn(Optional.of(workflow)).when(metadataDAO).getTaskDef(task.getTaskDefName());
 doReturn(true).when(systemTaskCallbackTime).isAsyncComplete(task);
 doNothing().when(queueDAO).remove(anyString(), eq(taskId));

 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

 verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
 }

 @Test
 public void testExecute_taskIsTerminalAndWorkflowIsRunning() {
 String workflowId = "workflowId";
 String taskId = "taskId";
 WorkflowModel workflow = new WorkflowModel();
 workflow.setId(workflowId);
 workflow.setStatus(WorkflowStatus.RUNNING);
 TaskModel task = new TaskModel();
 task.setId(taskId);
 task.setWorkflowInstanceId(workflowId);
 task.setStatus(TaskModel.Status.SCHEDULED);
 doReturn(workflow).when(executionDAOFacade).getWorkflowModel(workflowId, false);
 doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
 doReturn(Optional.empty()).when(metadataDAO).getTaskDef(task.getTaskDefName());
 doNothing().when(queueDAO).remove(anyString(), eq(taskId));

 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

 verify(workflowExecutor, times(1)).decide(workflowId);
 }

 @Test
 public void testExecute_taskIsTerminalAndWorkflowIsCompleted() {
 String workflowId = "workflowId";
 String taskId = "taskId";
 WorkflowModel workflow = new WorkflowModel();
 workflow.setId(workflowId);
 workflow.setStatus(WorkflowStatus.COMPLETED);
 TaskModel task = new TaskModel();
 task.setId(taskId);
 task.setWorkflowInstanceId(workflowId);
 task.setStatus(TaskModel.Status.SCHEDULED);
 doReturn(workflow).when(executionDAOFacade).getWorkflowModel(workflowId, false);
 doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
 doReturn(Optional.empty()).when(metadataDAO).getTaskDef(task.getTaskDefName());
 doNothing().when(queueDAO).remove(anyString(), eq(taskId));

 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

 verify(queueDAO, never()).remove(anyString(), eq(taskId));
 }

 @Test
 public void testPostponeQuietly() {
 String queueName = "queueName";
 String taskId = "taskId";
 doNothing().when(queueDAO).postpone(queueName, taskId, task.getWorkflowPriority(), queueTaskMessagePostponeSecs);

 asyncSystemTaskExecutor.postponeQuietly(queueName, task);

 verify(queueDAO, times(1)).postpone(queueName, taskId, task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
 }

 @Test
 public void testLoadTaskQuietly_taskNull() {
 String taskId = "taskId";
 doThrow(new RuntimeException("DB Exception")).when(executionDAOFacade).getTaskModel(taskId);

 TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

 assertNull(task);
 }

 @Test
 public void testLoadTaskQuietly_taskNotNull() {
 String taskId = "taskId";
 TaskModel task = new TaskModel();
 doReturn(task).when(executionDAOFacade).getTaskModel(taskId);

 TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

 assertSame(task, loadedTask);
 }
}