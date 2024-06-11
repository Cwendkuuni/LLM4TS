package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito. Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.TestUtils;

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
 conductorPropertiesMock = Mockito.mock(ConductorProperties.class);
 workflowExecutorMock = mock(WorkflowExecutor.class);

 asyncSystemTaskExecutor =
         new AsyncSystemTaskExecutor(
                 executionDAOFacadeMock,
                 queueDAOMock,
                 metadataDAOMock,
                 conductorPropertiesMock,
                 workflowExecutorMock);
 }

 @After
 public void tearDown() {
 TestUtils.resetAllMocks();
 }

 @Test
 public void testExecute_TaskNotFound_RemoveDeadTaskFromQueue() {
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
 String taskId = "taskId";
 String taskType = "taskType";

 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(null);

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(queueDAOMock).remove(taskType, taskId);
 }

 @Test
 public void testExecute_TaskFoundAndNotTerminal_UpdateTaskAndPostpone() {
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
 TaskModel task = Mockito.mock(TaskModel.class);
 String taskId = "taskId";
 String taskType = "taskType";
 String queueName = "queueName";
 String workflowId = "workflowId";

 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 when(metadataDAOMock.getTaskDef(task.getTaskDefName())).thenReturn(new WorkflowModel());
 when(queueDAOMock.getSize(queueName)).thenReturn(10);

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(workflowExecutorMock).decide(workflowId);
 verify(queueDAOMock).postpone(queueName, taskId, 1, 1000L);
 }

 @Test(expected = IllegalStateException.class)
 public void testExecute_TaskAlreadyCompleted_RuntimeException() {
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
 TaskModel task = Mockito.mock(TaskModel.class);
 String taskId = "taskId";
 String taskType = "taskType";
 String queueName = "queueName";
 String workflowId = "workflowId";

 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 when(task.getStatus()).thenReturn(ExecutionStatus.COMPLETED);

 asyncSystemTaskExecutor.execute(systemTask, taskId);
 }

 @Test(expected = IllegalStateException.class)
 public void testExecute_TaskInErrorState_RuntimeException() {
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
 TaskModel task = Mockito.mock(TaskModel.class);
 String taskId = "taskId";
 String taskType = "taskType";
 String queueName = "queueName";
 String workflowId = "workflowId";

 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 when(task.getStatus()).thenReturn(ExecutionStatus.ERROR);

 asyncSystemTaskExecutor.execute(systemTask, taskId);
 }

 @Test
 public void testExecute_TaskInProgress_UpdateTaskAndExecuteSystemTask() {
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
 TaskModel task = Mockito.mock(TaskModel.class);
 String taskId = "taskId";
 String taskType = "taskType";
 String queueName = "queueName";
 String workflowId = "workflowId";

 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 when(metadataDAOMock.getTaskDef(task.getTaskDefName())).thenReturn(new WorkflowModel());
 when(queueDAOMock.getSize(queueName)).thenReturn(10);

 doAnswer(invocation -> {
 throw new IllegalStateException("Simulated exception");
 }).when(systemTask).execute(any(), any(), any());

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(systemTask).execute(any(), any(), any());
 verify(queueDAOMock).postpone(queueName, taskId, 1, 1000L);
 }

 @Test
 public void testPostponeQuietly_TaskFound_PostponeTaskInQueue() {
 String queueName = "queueName";
 String taskId = "taskId";

 asyncSystemTaskExecutor.postponeQuietly(queueName, Mockito.mock(TaskModel.class));

 verify(queueDAOMock).postpone(queueName, taskId, 1, 1000L);
 }

 @Test
 public void testLoadTaskQuietly_TaskFound_ReturnTaskObject() {
 String taskId = "taskId";
 TaskModel task = Mockito.mock(TaskModel.class);

 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);

 assertEquals(task, asyncSystemTaskExecutor.loadTaskQuietly(taskId));
 }

 @Test
 public void testLoadTaskQuietly_TaskNotFound_ReturnNull() {
 String taskId = "taskId";

 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(null);

 assertNull(asyncSystemTaskExecutor.loadTaskQuietly(taskId));
 }
}