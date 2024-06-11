package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
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
import com.netflix.conductor.core.model.Status;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
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
 public void setup() {
 ConductorProperties conductorProperties = new ConductorProperties();
 conductorPropertiesMock = Mockito.spy(conductorProperties);

 ExecutionDAOFacade executionDAOFacade = Mockito.mock(ExecutionDAOFacade.class);
 executionDAOFacadeMock = executionDAOFacade;

 QueueDAO queueDAO = new QueueDAOImpl();
 queueDAOMock = Mockito.mock(QueueDAO.class);
 when(queueDAO.getQueueName(any(TaskModel.class))).thenReturn("queueName");

 MetadataDAO metadataDAO = Mockito.mock(MetadataDAO.class);
 metadataDAOMock = metadataDAO;

 WorkflowExecutor workflowExecutor = Mockito.mock(WorkflowExecutor.class);
 workflowExecutorMock = workflowExecutor;

 AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
 executionDAOFacadeMock,
 queueDAOMock,
 metadataDAOMock,
 conductorPropertiesMock,
 workflowExecutorMock);

 this.asyncSystemTaskExecutor = asyncSystemTaskExecutor;
 }

 @Test
 public void testExecute_SCHEDULED_and_inProgress_task() throws Exception {
 // Given
 TaskModel task = TestUtils.createTaskModel("taskType", "taskId");
 WorkflowModel workflowModel = TestUtils.createWorkflowModel("workflowId");
 WorkflowSystemTask systemTask = new WorkflowSystemTask("taskType", "taskId") {
 @Override
 public Optional<Long> getEvaluationOffset(TaskModel task, long callbackAfterSeconds) {
 return Optional.empty();
 }
 };

 when(executionDAOFacadeMock.getTaskModel(eq("taskId"))).thenReturn(task);
 when(executionDAOFacadeMock.getWorkflowModel(eq("workflowId"), anyBoolean())).thenReturn(workflowModel);

 // When
 asyncSystemTaskExecutor.execute(systemTask, "taskId");

 // Then
 verify(queueDAOMock, times(1)).postpone(
 eq("queueName"),
 eq("taskId"),
 anyInt(),
 eq(queueTaskMessagePostponeSecs));
 }

 @Test
 public void testExecute_SCHEDULED_task_with_terminal_workflow() throws Exception {
 // Given
 TaskModel task = TestUtils.createTaskModel("taskType", "taskId");
 WorkflowModel workflowModel = TestUtils.createWorkflowModel("workflowId");
 workflowModel.setStatus(ExecutionStatus.COMPLETED);

 when(executionDAOFacadeMock.getTaskModel(eq("taskId"))).thenReturn(task);
 when(executionDAOFacadeMock.getWorkflowModel(eq("workflowId"), anyBoolean())).thenReturn(workflowModel);

 // When
 asyncSystemTaskExecutor.execute(new WorkflowSystemTask("taskType", "taskId"), "taskId");

 // Then
 verify(queueDAOMock, never()).postpone(anyString(), eq("taskId"), anyInt(), anyInt());
 }

 @Test(expected = Exception.class)
 public void testExecute_SCHEDULED_task_with_exceeded_inProgress_limit() throws Exception {
 // Given
 TaskModel task = TestUtils.createTaskModel("taskType", "taskId");
 when(executionDAOFacadeMock.exceedsInProgressLimit(task)).thenReturn(true);

 // When & Then
 asyncSystemTaskExecutor.execute(new WorkflowSystemTask("taskType", "taskId"), "taskId");

 // Verify that the task is postponed in the queue
 verify(queueDAOMock, times(1)).postpone(
 eq("queueName"),
 eq("taskId"),
 anyInt(),
 eq(queueTaskMessagePostponeSecs));
 }

 @Test(expected = Exception.class)
 public void testExecute_SCHEDULED_task_with_exceeded_rate_limit() throws Exception {
 // Given
 TaskModel task = TestUtils.createTaskModel("taskType", "taskId");
 when(metadataDAOMock.getTaskDef(eq("taskType"))).thenReturn(TestUtils.createTaskDef("taskDefName"));
 when(executionDAOFacadeMock.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

 // When & Then
 asyncSystemTaskExecutor.execute(new WorkflowSystemTask("taskType", "taskId"), "taskId");

 // Verify that the task is postponed in the queue
 verify(queueDAOMock, times(1)).postpone(
 eq("queueName"),
 eq("taskId"),
 anyInt(),
 eq(queueTaskMessagePostponeSecs));
 }

 @Test
 public void testExecute_IN_PROGRESS_task() throws Exception {
 // Given
 TaskModel task = TestUtils.createTaskModel("taskType", "taskId");
 task.setStatus(Status.IN_PROGRESS);
 WorkflowModel workflowModel = TestUtils.createWorkflowModel("workflowId");
 WorkflowSystemTask systemTask = new WorkflowSystemTask("taskType", "taskId") {
 @Override
 public void execute(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
 // empty implementation
 }
 };

 when(executionDAOFacadeMock.getTaskModel(eq("taskId"))).thenReturn(task);
 when(executionDAOFacadeMock.getWorkflowModel(eq("workflowId"), anyBoolean())).thenReturn(workflowModel);

 // When
 asyncSystemTaskExecutor.execute(systemTask, "taskId");

 // Then
 assertFalse(task.isCallbackRequired());
 }

 @Test
 public void testExecute_IN_PROGRESS_task_with_asyncComplete() throws Exception {
 // Given
 TaskModel task = TestUtils.createTaskModel("taskType", "taskId");
 task.setStatus(Status.IN_PROGRESS);
 WorkflowModel workflowModel = TestUtils.createWorkflowModel("workflowId");
 WorkflowSystemTask systemTask = new WorkflowSystemTask("taskType", "taskId") {
 @Override
 public boolean isAsyncComplete(TaskModel task) {
 return true;
 }
 };

 when(executionDAOFacadeMock.getTaskModel(eq("taskId"))).thenReturn(task);
 when(executionDAOFacadeMock.getWorkflowModel(eq("workflowId"), anyBoolean())).thenReturn(workflowModel);

 // When
 asyncSystemTaskExecutor.execute(systemTask, "taskId");

 // Then
 assertTrue(task.isCallbackRequired());
 }

 @Test
 public void testExecute_IN_PROGRESS_task_with_terminal_workflow() throws Exception {
 // Given
 TaskModel task = TestUtils.createTaskModel("taskType", "taskId");
 task.setStatus(Status.IN_PROGRESS);
 WorkflowModel workflowModel = TestUtils.createWorkflowModel("workflowId");
 workflowModel.setStatus(ExecutionStatus.COMPLETED);

 when(executionDAOFacadeMock.getTaskModel(eq("taskId"))).thenReturn(task);
 when(executionDAOFacadeMock.getWorkflowModel(eq("workflowId"), anyBoolean())).thenReturn(workflowModel);

 // When
 asyncSystemTaskExecutor.execute(new WorkflowSystemTask("taskType", "taskId"), "taskId");

 // Then
 verify(queueDAOMock, never()).postpone(anyString(), eq("taskId"), anyInt(), anyInt());
 }

 @Test
 public void testExecute_terminal_task() throws Exception {
 // Given
 TaskModel task = TestUtils.createTaskModel("taskType", "taskId");
 task.setStatus(Status.TERMINAL);
 WorkflowModel workflowModel = TestUtils.createWorkflowModel("workflowId");

 when(executionDAOFacadeMock.getTaskModel(eq("taskId"))).thenReturn(task);
 when(executionDAOFacadeMock.getWorkflowModel(eq("workflowId"), anyBoolean())).thenReturn(workflowModel);

 // When
 asyncSystemTaskExecutor.execute(new WorkflowSystemTask("taskType", "taskId"), "taskId");

 // Then
 assertEquals(Status.TERMINAL, task.getStatus());
 verify(queueDAOMock, never()).postpone(anyString(), eq("taskId"), anyInt(), anyInt());
 }

 @Test
 public void testExecute_terminal_workflow() throws Exception {
 // Given
 TaskModel task = TestUtils.createTaskModel("taskType", "taskId");
 WorkflowModel workflowModel = TestUtils.createWorkflowModel("workflowId");
 workflowModel.setStatus(ExecutionStatus.COMPLETED);

 when(executionDAOFacadeMock.getTaskModel(eq("taskId"))).thenReturn(task);
 when(executionDAOFacadeMock.getWorkflowModel(eq("workflowId"), anyBoolean())).thenReturn(workflowModel);

 // When
 asyncSystemTaskExecutor.execute(new WorkflowSystemTask("taskType", "taskId"), "taskId");

 // Then
 assertEquals(ExecutionStatus.COMPLETED, workflowModel.getStatus());
 }
}