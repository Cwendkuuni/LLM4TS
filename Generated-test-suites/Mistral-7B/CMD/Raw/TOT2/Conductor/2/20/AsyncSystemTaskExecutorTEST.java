package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;

public class AsyncSystemTaskExecutorTest {

 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
 private ExecutionDAOFacade executionDAOFacadeMock;
 private QueueDAO queueDAOMock;
 private MetadataDAO metadataDAOMock;
 private ConductorProperties conductorPropertiesMock;
 private WorkflowExecutor workflowExecutorMock;

 @Before
 public void setUp() {
 executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
 queueDAOMock = mock(QueueDAO.class);
 metadataDAOMock = mock(MetadataDAO.class);
 conductorPropertiesMock = mock(ConductorProperties.class);
 workflowExecutorMock = mock(WorkflowExecutor.class);

 asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
 executionDAOFacadeMock,
 queueDAOMock,
 metadataDAOMock,
 conductorPropertiesMock,
 workflowExecutorMock);
 }

 @Test
 public void testExecute_taskNotFound() {
 String taskId = "testTaskId";
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
 doReturn(null).when(executionDAOFacadeMock).getTaskModel(taskId);

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(queueDAOMock, times(1)).remove(Mockito.eq("queueName"), Mockito.eq(taskId));
 }

 @Test
 public void testExecute_taskAlreadyCompleted() {
 String workflowId = "testWorkflowId";
 String taskId = "testTaskId";
 TaskModel task = Mockito.mock(TaskModel.class);
 WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
 doReturn(workflow).when(executionDAOFacadeMock).getWorkflowModel(workflowId, false);
 doReturn(task).when(executionDAOFacadeMock).getTaskModel(taskId);
 doReturn(true).when(task.getStatus()).isTerminal();

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(queueDAOMock, times(1)).remove(Mockito.eq("queueName"), Mockito.eq(taskId));
 }

 @Test
 public void testExecute_exceedsConcurrentExecutionLimit() {
 String taskId = "testTaskId";
 String taskType = "testTaskType";
 String queueName = "testQueueName";
 TaskModel task = Mockito.mock(TaskModel.class);
 doReturn(task).when(executionDAOFacadeMock).getTaskModel(taskId);
 doReturn(true).when(task.getStatus()).isTerminal();
 doReturn(queueName).when(QueuesUtils::getQueueName);

 doThrow(new RuntimeException("Concurrent Execution limit exceeded"))
 .when(executionDAOFacadeMock)
 .exceedsInProgressLimit(task);

 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(queueDAOMock, never()).postpone(
 Mockito.eq(queueName),
 Mockito.eq(taskId),
 Mockito.anyInt(),
 Mockito.anyInt());
 }

 @Test
 public void testExecute_exceedsRateLimitPerFrequency() {
 String taskId = "testTaskId";
 String taskType = "testTaskType";
 String queueName = "testQueueName";
 String workflowId = "testWorkflowId";
 TaskModel task = Mockito.mock(TaskModel.class);
 WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
 doReturn(workflow).when(executionDAOFacadeMock).getWorkflowModel(workflowId, false);
 doReturn(task).when(executionDAOFacadeMock).getTaskModel(taskId);
 doReturn(true).when(task.getStatus()).isTerminal();
 doReturn(1).when(task.getRateLimitPerFrequency());
 doReturn(workflow).when(metadataDAOMock).getTaskDef(taskType);
 doReturn(Optional.of(1)).when(workflow.getLastTaskExecutionTime());

 doThrow(new RuntimeException("RateLimit Execution limit exceeded"))
 .when(executionDAOFacadeMock)
 .exceedsRateLimitPerFrequency(task, workflow);

 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(queueDAOMock, never()).postpone(
 Mockito.eq(queueName),
 Mockito.eq(taskId),
 Mockito.anyInt(),
 Mockito.anyInt());
 }

 @Test
 public void testExecute_success() {
 String taskId = "testTaskId";
 String taskType = "testTaskType";
 String queueName = "testQueueName";
 String workflowId = "testWorkflowId";
 TaskModel task = Mockito.mock(TaskModel.class);
 WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
 doReturn(workflow).when(executionDAOFacadeMock).getWorkflowModel(workflowId, false);
 doReturn(task).when(executionDAOFacadeMock).getTaskModel(taskId);
 doReturn(false).when(task.getStatus()).isTerminal();
 doReturn(false).when(task.getStatus()).equals(TaskModel.Status.SCHEDULED);
 doReturn(true).when(systemTask.isAsyncComplete(task));
 doReturn("testWorkflowPriority").when(task.getWorkflowPriority());
 doReturn(queueName).when(QueuesUtils::getQueueName);
 doReturn("testCallbackAfterSeconds").when(systemTaskCallbackTime());

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(queueDAOMock, times(1)).postpone(
 Mockito.eq(queueName),
 Mockito.eq(taskId),
 Mockito.eq("testWorkflowPriority"),
 Mockito.eq("testCallbackAfterSeconds"));
 }

 @Test
 public void testPostponeQuietly_queueExists() {
 String queueName = "testQueueName";
 String taskId = "testTaskId";

 asyncSystemTaskExecutor.postponeQuietly(queueName, Mockito.mock(TaskModel.class));

 verify(queueDAOMock, times(1)).postpone(
 Mockito.eq(queueName),
 Mockito.eq(taskId),
 Mockito.eq("testWorkflowPriority"),
 Mockito.eq(queueTaskMessagePostponeSecs));
 }

 @Test
 public void testPostponeQuietly_queueDoesNotExist() {
 String queueName = "testQueueName";
 String taskId = "testTaskId";

 doThrow(new RuntimeException("Queue does not exist")).when(queueDAOMock).postpone(
 Mockito.eq(queueName),
 Mockito.eq(taskId),
 Mockito.anyInt(),
 Mockito.anyInt());

 asyncSystemTaskExecutor.postponeQuietly(queueName, Mockito.mock(TaskModel.class));

 verify(queueDAOMock, never()).postpone(
 Mockito.eq(queueName),
 Mockito.eq(taskId),
 Mockito.anyInt(),
 Mockito.anyInt());
 }

 private long systemTaskCallbackTime() {
 return 5000L;
 }
}