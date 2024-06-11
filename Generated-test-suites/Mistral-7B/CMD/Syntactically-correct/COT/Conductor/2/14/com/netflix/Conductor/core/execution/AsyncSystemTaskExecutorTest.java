package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.model.WorkflowModelImpl;
import com.netflix.conductor.utils.Json;

public class AsyncSystemTaskExecutorTest {

 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
 private ExecutionDAOFacade executionDAOFacadeMock;
 private QueueDAO queueDAOMock;
 private MetadataDAO metadataDAOMock;
 private ConductorProperties conductorPropertiesMock;
 private WorkflowExecutor workflowExecutorMock;
 private QueueDAOImpl queueDAO;
 private Monitors monitorsMock;

 @Before
 public void setUp() {
 executionDAOFacadeMock = Mockito.mock(ExecutionDAOFacade.class);
 queueDAOMock = Mockito.mock(QueueDAO.class);
 metadataDAOMock = Mockito.mock(MetadataDAO.class);
 conductorPropertiesMock = Mockito.mock(ConductorProperties.class);
 workflowExecutorMock = Mockito.mock(WorkflowExecutor.class);
 monitorsMock = Mockito.mock(Monitors.class);

 queueDAO = new QueueDAOImpl();

 asyncSystemTaskExecutor =
         new AsyncSystemTaskExecutor(
                 executionDAOFacadeMock,
                 queueDAOMock,
                 metadataDAOMock,
                 conductorPropertiesMock,
                 workflowExecutorMock);
 }

 @Test
 public void testExecute_taskNotFound() {
 String taskId = "123";
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

 Mockito.when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(null);
 Mockito.when(queueDAOMock.remove(ArgumentMatchers.anyString(), ArgumentMatchers.eq(taskId)))
         .thenReturn(true);

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Check if task was removed from queue
 Mockito.verify(queueDAOMock, Mockito.times(1)).remove(
         QueueUtils.getQueueName(ArgumentMatchers.any(TaskModel.class)), taskId);
 }

 @Test
 public void testExecute_terminalTask() {
 String workflowId = "123";
 String taskId = "456";
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.COMPLETED);
 task.setWorkflowInstanceId(workflowId);
 WorkflowModel workflowModel = new WorkflowModelImpl();
 workflowModel.setId(workflowId);
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

 Mockito.when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 Mockito.when(executionDAOFacadeMock.getWorkflowModel(workflowId, Argument.eq(false)))
         .thenReturn(workflowModel);
 Mockito.when(workflowExecutorMock.decide(workflowId)).thenReturn(null);

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Check if task was removed from queue
 Mockito.verify(queueDAOMock, Mockito.times(1)).remove(
         QueueUtils.getQueueName(ArgumentMatchers.any(TaskModel.class)), taskId);
 }

 @Test
 public void testExecute_nonTerminalTask() throws Exception {
 String workflowId = "123";
 String taskId = "456";
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setWorkflowInstanceId(workflowId);
 WorkflowModel workflowModel = new WorkflowModelImpl();
 workflowModel.setId(workflowId);
 WorkflowInstance workflowInstance = new WorkflowInstance();
 workflowInstance.setId(workflowId);
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
 Monitors monitors = Mockito.mock(Monitors.class);

 Mockito.when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 Mockito.when(executionDAOFacadeMock.getWorkflowModel(workflowId, Argument.eq(false)))
         .thenReturn(workflowModel);
 Mockito.when(workflowExecutorMock.decide(workflowId)).thenReturn(null);
 Mockito.when(systemTask.start(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
         .thenReturn(new Object());
 Mockito.when(systemTask.execute(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
         .thenReturn(new Object());
 Mockito.when(conductorPropertiesMock.getSystemTaskWorkerCallbackDuration())
         .thenReturn(Mockito.mock(Duration.class));
 Mockito.when(conductorPropertiesMock.getTaskExecutionPostponeDuration())
         .thenReturn(Mockito.mock(Duration.class));
 Mockito.when(monitorsMock.recordQueueWaitTime(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
         .thenReturn(null);

 asyncSystemTaskExecutor =
         new AsyncSystemTaskExecutor(
                 executionDAOFacadeMock,
                 queueDAOMock,
                 metadataDAOMock,
                 conductorPropertiesMock,
                 workflowExecutorMock,
                 monitorsMock);

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Check if task was postponed in queue
 Mockito.verify(queueDAOMock, Mockito.times(1)).postpone(
         QueueUtils.getQueueName(task),
         taskId,
         task.getWorkflowPriority(),
         ArgumentMatchers.anyLong());
 }

 @Test(expected = Exception.class)
 public void testExecute_concurrencyLimit() throws Exception {
 String workflowId = "123";
 String taskId = "456";
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setWorkflowInstanceId(workflowId);
 WorkflowModel workflowModel = new WorkflowModelImpl();
 workflowModel.setId(workflowId);
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
 Monitors monitors = Mockito.mock(Monitors.class);

 Mockito.when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 Mockito.when(executionDAOFacadeMock.getWorkflowModel(workflowId, Argument.eq(false)))
         .thenReturn(workflowModel);
 Mockito.when(workflowExecutorMock.decide(workflowId)).thenReturn(null);
 Mockito.when(systemTask.start(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
         .thenReturn(new Object());
 Mockito.when(systemTask.execute(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
         .thenReturn(new Object());
 Mockito.when(conductorPropertiesMock.getSystemTaskWorkerCallbackDuration())
         .thenReturn(Mockito.mock(Duration.class));
 Mockito.when(conductorPropertiesMock.getTaskExecutionPostponeDuration())
         .thenReturn(Mockito.mock(Duration.class));
 Mockito.when(monitorsMock.recordQueueWaitTime(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
         .thenReturn(null);

 ExecutionDAOFacade executionDAOFacadeSpy = Mockito.spy(executionDAOFacadeMock);
 Mockito.doThrow(new Exception("Concurrency limit exceeded"))
         .when(executionDAOFacadeSpy)
         .exceedsInProgressLimit(ArgumentMatchers.any(TaskModel.class));

 AsyncSystemTaskExecutor asyncSystemTaskExecutor =
         new AsyncSystemTaskExecutor(
                 executionDAOFacadeMock,
                 queueDAOMock,
                 metadataDAOMock,
                 conductorPropertiesMock,
                 workflowExecutorMock,
                 monitorsMock);

 asyncSystemTaskExecutor.execute(systemTask, taskId);
 }

 @Test(expected = Exception.class)
 public void testExecute_rateLimit() throws Exception {
 String workflowId = "123";
 String taskId = "456";
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setWorkflowInstanceId(workflowId);
 WorkflowModel workflowModel = new WorkflowModelImpl();
 workflowModel.setId(workflowId);
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
 Monitors monitors = Mockito.mock(Monitors.class);

 Mockito.when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 Mockito.when(executionDAOFacadeMock.getWorkflowModel(workflowId, Argument.eq(false)))
         .thenReturn(workflowModel);
 Mockito.when(workflowExecutorMock.decide(workflowId)).thenReturn(null);
 Mockito.when(systemTask.start(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
         .thenReturn(new Object());
 Mockito.when(systemTask.execute(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
         .thenReturn(new Object());
 Mockito.when(conductorPropertiesMock.getSystemTaskWorkerCallbackDuration())
         .thenReturn(Mockito.mock(Duration.class));
 Mockito.when(conductorPropertiesMock.getTaskExecutionPostponeDuration())
         .thenReturn(Mockito.mock(Duration.class));
 Mockito.when(monitorsMock.recordQueueWaitTime(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
         .thenReturn(null);
 Mockito.when(metadataDAOMock.getTaskDef(task.getTaskDefName()))
         .thenReturn(new TaskDefinition());

 AsyncSystemTaskExecutor asyncSystemTaskExecutor =
         new AsyncSystemTaskExecutor(
                 executionDAOFacadeMock,
                 queueDAOMock,
                 metadataDAOMock,
                 conductorPropertiesMock,
                 workflowExecutorMock,
                 monitorsMock);

 Mockito.doThrow(new Exception("Rate limit exceeded"))
         .when(executionDAOFacadeMock)
         .exceedsRateLimitPerFrequency(task, ArgumentMatchers.any());

 asyncSystemTaskExecutor.execute(systemTask, taskId);
 }
}

class TaskDefinition {
 // Empty class to mock MetadataDAO.getTaskDef method
}