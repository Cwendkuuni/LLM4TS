package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.utils.Metrics;
import com.netflix.conductor.utils.PropertyPlaceholder;
import com.netflix.conductor.utils.ThreadUtils;
import com.netflix.conductor.workflow.WorkflowExecution;
import com.netflix.conductor.workflow.WorkflowExecutionImpl;

public class AsyncSystemTaskExecutorTest {

 private AsyncSystemTaskExecutor systemTaskExecutor;
 private ExecutionDAOFacade executionDAOFacadeMock;
 private QueueDAO queueDAOMock;
 private MetadataDAO metadataDAOMock;
 private WorkflowSystemTask systemTaskMock;
 private ConductorProperties conductorPropertiesMock;
 private String queueName;
 private String taskId;
 private String workflowId;
 private Logger log;

 @Before
 public void setUp() {
 executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
 queueDAOMock = mock(QueueDAO.class);
 metadataDAOMock = mock(MetadataDAO.class);
 systemTaskMock = mock(WorkflowSystemTask.class);
 conductorPropertiesMock = mock(ConductorProperties.class);
 queueName = "testQueueName";
 taskId = "testTaskId";
 workflowId = "testWorkflowId";
 log = mock(Logger.class);

 systemTaskExecutor =
         new AsyncSystemTaskExecutor(
                 executionDAOFacadeMock,
                 queueDAOMock,
                 metadataDAOMock,
                 conductorPropertiesMock,
                 mock(WorkflowExecutor.class));
 }

 @Test
 public void testExecute_TaskIsNotExistInExecutionDAO_RemoveTaskFromQueue() {
 // Given
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(null);
 when(queueDAOMock.remove(queueName, taskId)).thenReturn(true);

 // When
 systemTaskExecutor.execute(systemTaskMock, taskId);

 // Then
 verify(queueDAOMock).remove(queueName, taskId);
 }

 @Test
 public void testExecute_TaskIsExistInExecutionDAO_UpdateTaskStatusAndRemoveFromQueue() {
 // Given
 TaskModel taskModel = new TaskModel();
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModel);
 when(queueDAOMock.remove(queueName, taskId)).thenReturn(true);

 ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
 when(executionDAOFacadeMock.updateTask(taskCaptor.capture())).thenReturn(taskModel);

 // When
 systemTaskExecutor.execute(systemTaskMock, taskId);

 // Then
 TaskModel actualTaskModel = taskCaptor.getValue();
 assertEquals(TaskModel.Status.CANCELED, actualTaskModel.getStatus());
 verify(queueDAOMock).remove(queueName, taskId);
 }

 @Test
 public void testExecute_TaskIsExistInExecutionDAO_UpdateTaskStatusAndLeaveInQueue() {
 // Given
 TaskModel taskModel = new TaskModel();
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModel);
 when(queueDAOMock.remove(queueName, taskId)).thenReturn(false);

 ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
 when(executionDAOFacadeMock.updateTask(taskCaptor.capture())).thenReturn(taskModel);

 // When
 systemTaskExecutor.execute(systemTaskMock, taskId);

 // Then
 TaskModel actualTaskModel = taskCaptor.getValue();
 assertEquals(TaskModel.Status.SCHEDULED, actualTaskModel.getStatus());
 verify(queueDAOMock, never()).remove(queueName, taskId);
 }

 @Test
 public void testExecute_TaskIsExistInExecutionDAO_UpdateTaskStatusAndPostponeInQueue() {
 // Given
 WorkflowModel workflowModel = new WorkflowModel();
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(new TaskModel());
 when(executionDAOFacadeMock.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);
 when(queueDAOMock.remove(queueName, taskId)).thenReturn(false);
 when(queueDAOMock.postpone(queueName, taskId, 0, Long.MAX_VALUE)).thenReturn(true);

 ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
 when(executionDAOFacadeMock.updateTask(taskCaptor.capture())).thenReturn(new TaskModel());

 // When
 systemTaskExecutor.execute(systemTaskMock, taskId);

 // Then
 TaskModel actualTaskModel = taskCaptor.getValue();
 assertEquals(Optional.of(Long.MAX_VALUE), actualTaskModel.getCallbackAfterSeconds());
 verify(queueDAOMock).postpone(queueName, taskId, 0, Long.MAX_VALUE);
 }

 @Test
 public void testExecute_WorkflowIsCompleted_RemoveTaskFromQueue() {
 // Given
 WorkflowModel workflowModel = new WorkflowExecutionImpl();
 workflowModel.setStatus(WorkflowExecution.Status.COMPLETED);
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(new TaskModel());
 when(executionDAOFacadeMock.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);
 when(queueDAOMock.remove(queueName, taskId)).thenReturn(true);
 ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
 when(workflowExecutor.decide(workflowIdCaptor.capture())).thenReturn(true);

 // When
 systemTaskExecutor.execute(systemTaskMock, taskId);

 // Then
 verify(queueDAOMock).remove(queueName, taskId);
 verify(workflowExecutor).decide(workflowId);
 }

 @Test
 public void testExecute_TaskIsInProgress_UpdateTaskStatusAndCallback() {
 // Given
 WorkflowModel workflowModel = new WorkflowExecutionImpl();
 workflowModel.setStatus(WorkflowExecution.Status.STARTED);
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(new TaskModel());
 when(executionDAOFacadeMock.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);
 when(queueDAOMock.remove(queueName, taskId)).thenReturn(false);
 when(queueDAOMock.postpone(queueName, taskId, 0, Long.MAX_VALUE)).thenReturn(true);
 when(systemTaskMock.isAsyncComplete(new TaskModel())).thenReturn(false);
 when(systemTaskMock.getEvaluationOffset(new TaskModel(), Long.MAX_VALUE))
         .thenReturn(Optional.of(System.currentTimeMillis() - 1000L));

 ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
 when(executionDAOFacadeMock.updateTask(taskCaptor.capture())).thenReturn(new TaskModel());

 // When
 systemTaskExecutor.execute(systemTaskMock, taskId);

 // Then
 TaskModel actualTaskModel = taskCaptor.getValue();
 assertEquals(Optional.of(Long.MAX_VALUE), actualTaskModel.getCallbackAfterSeconds());
 verify(queueDAOMock).postpone(queueName, taskId, 0, Long.MAX_VALUE);
 }

 @Test
 public void testExecute_TaskIsAsyncComplete_RemoveTaskFromQueueAndDecideWorkflow() {
 // Given
 WorkflowModel workflowModel = new WorkflowExecutionImpl();
 workflowModel.setStatus(WorkflowExecution.Status.STARTED);
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(new TaskModel());
 when(executionDAOFacadeMock.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);
 when(queueDAOMock.remove(queueName, taskId)).thenReturn(true);
 ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
 when(workflowExecutor.decide(workflowIdCaptor.capture())).thenReturn(true);
 when(systemTaskMock.isAsyncComplete(new TaskModel())).thenReturn(true);

 // When
 systemTaskExecutor.execute(systemTaskMock, taskId);

 // Then
 verify(queueDAOMock).remove(queueName, taskId);
 verify(workflowExecutor).decide(workflowId);
 }

 @Test
 public void testExecute_ThrowException_LogErrorAndLeaveTaskInQueue() {
 // Given
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenThrow(new RuntimeException());
 when(queueDAOMock.remove(queueName, taskId)).thenReturn(false);
 when(queueDAOMock.postpone(queueName, taskId, 0, Long.MAX_VALUE)).thenReturn(true);

 // When & Then
 Monitors.recordExceptionThrown(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask");
 systemTaskExecutor.execute(systemTaskMock, taskId);
 verify(queueDAOMock).postpone(queueName, taskId, 0, Long.MAX_VALUE);
 }
}