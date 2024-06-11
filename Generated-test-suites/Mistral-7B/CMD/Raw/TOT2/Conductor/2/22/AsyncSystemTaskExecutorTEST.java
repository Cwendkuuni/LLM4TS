package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.ZoneOffset;
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
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;

public class AsyncSystemTaskExecutorTest {

 private AsyncSystemTaskExecutor executor;
 private ExecutionDAOFacade executionDAOFacadeMock;
 private QueueDAO queueDAOMock;
 private MetadataDAO metadataDAOMock;
 private ConductorProperties conductorPropertiesMock;
 private WorkflowExecutor workflowExecutorMock;
 private WorkflowModel workflowModelMock;
 private TaskModel taskModelMock;
 private String queueName;
 private String taskId;
 private long queueTaskMessagePostponeSecs;
 private long systemTaskCallbackTime;

 @Before
 public void setUp() {
 executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
 queueDAOMock = mock(QueueDAO.class);
 metadataDAOMock = mock(MetadataDAO.class);
 conductorPropertiesMock = mock(ConductorProperties.class);
 workflowExecutorMock = mock(WorkflowExecutor.class);
 workflowModelMock = Mockito.spy(new WorkflowModel());
 taskModelMock = Mockito.spy(new TaskModel());
 queueName = QueueUtils.getQueueName(taskModelMock);
 taskId = taskModelMock.getTaskId();
 queueTaskMessagePostponeSecs = 5L;
 systemTaskCallbackTime = 30L;

 executor = new AsyncSystemTaskExecutor(
 executionDAOFacadeMock,
 queueDAOMock,
 metadataDAOMock,
 conductorPropertiesMock,
 workflowExecutorMock);
 }

 @Test
 public void testExecute_taskNotExists_taskIdNull() {
 WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);
 String nullTaskId = null;
 executor.execute(systemTaskMock, nullTaskId);

 verify(executionDAOFacadeMock, times(1)).getTaskModel(eq(nullTaskId));
 verifyZeroInteractions(queueDAOMock);
 }

 @Test
 public void testExecute_taskExists_taskIsTerminal() {
 WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);
 taskModelMock.setStatus(TaskModel.Status.COMPLETED);
 doReturn(taskModelMock).when(executionDAOFacadeMock).getTaskModel(eq(taskId));

 executor.execute(systemTaskMock, taskId);

 verify(queueDAOMock, times(1)).remove(eq(queueName), eq(taskId));
 verifyZeroInteractions(executionDAOFacadeMock);
 }

 @Test
 public void testExecute_taskExists_taskIsScheduled() {
 WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);
 TaskModel taskModel = new TaskModel();
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 doReturn(taskModel).when(executionDAOFacadeMock).getTaskModel(eq(taskId));
 doThrow(new RuntimeException("Simulated Exception")).when(workflowExecutorMock).execute(any(), any(), any());

 executor.execute(systemTaskMock, taskId);

 verify(queueDAOMock, times(1)).postpone(
 eq(queueName), eq(taskId), anyInt(), eq(queueTaskMessagePostponeSecs));
 verifyZeroInteractions(executionDAOFacadeMock);
 }

 @Test
 public void testExecute_taskExists_taskInProgress() {
 WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);
 taskModelMock.setStatus(TaskModel.Status.IN_PROGRESS);
 doReturn(taskModelMock).when(executionDAOFacadeMock).getTaskModel(eq(taskId));
 doCallRealMethod().when(systemTaskMock).execute(any(), any(), any());

 executor.execute(systemTaskMock, taskId);

 verifyZeroInteractions(queueDAOMock);
 verifyZeroInteractions(executionDAOFacadeMock);
 }

 @Test
 public void testExecute_taskExists_taskCompleted() {
 WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);
 taskModelMock.setStatus(TaskModel.Status.COMPLETED);
 doReturn(taskModelMock).when(executionDAOFacadeMock).getTaskModel(eq(taskId));
 doCallRealMethod().when(systemTaskMock).execute(any(), any(), any());

 executor.execute(systemTaskMock, taskId);

 verifyZeroInteractions(queueDAOMock);
 verifyZeroInteractions(executionDAOFacadeMock);
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_exceptionDuringExecution() {
 WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);
 taskModelMock.setStatus(TaskModel.Status.SCHEDULED);
 doReturn(taskModelMock).when(executionDAOFacadeMock).getTaskModel(eq(taskId));
 doThrow(new RuntimeException("Simulated Exception")).when(workflowExecutorMock).execute(any(), any(), any());

 executor.execute(systemTaskMock, taskId);

 verify(queueDAOMock, times(1)).postpone(
 eq(queueName), eq(taskId), anyInt(), eq(queueTaskMessagePostponeSecs));
 verifyZeroInteractions(executionDAOFacadeMock);
 }

 @Test
 public void testExecute_workflowCompleted() {
 WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);
 taskModelMock.setWorkflowInstanceId("WorkflowId");
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setStatus(Status.COMPLETED);
 doReturn(workflowModel).when(executionDAOFacadeMock).getWorkflowModel(eq("WorkflowId"), anyBoolean());
 doCallRealMethod().when(systemTaskMock).execute(any(), any(), any());

 executor.execute(systemTaskMock, taskId);

 verifyZeroInteractions(queueDAOMock);
 verify(workflowExecutorMock).decide("WorkflowId");
 }

 @Test
 public void testPostponeQuietly_queueDAOException() {
 WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);
 doThrow(new RuntimeException("Simulated Exception")).when(queueDAOMock).postpone(
 eq(queueName), eq(taskId), anyInt(), anyLong());

 executor.postponeQuietly(queueName, taskModelMock);

 verifyZeroInteractions(executionDAOFacadeMock);
 }

 @Test
 public void testLoadTaskQuietly_taskNotFound() {
 TaskModel expectedNullTask = null;
 doReturn(expectedNullTask).when(executionDAOFacadeMock).getTaskModel(taskId);

 TaskModel actualTask = executor.loadTaskQuietly(taskId);

 assertNull(actualTask);
 }

 @Test
 public void testLoadTaskQuietly_taskFound() {
 TaskModel expectedTask = taskModelMock;
 doReturn(expectedTask).when(executionDAOFacadeMock).getTaskModel(taskId);

 TaskModel actualTask = executor.loadTaskQuietly(taskId);

 assertEquals(expectedTask, actualTask);
 }
}