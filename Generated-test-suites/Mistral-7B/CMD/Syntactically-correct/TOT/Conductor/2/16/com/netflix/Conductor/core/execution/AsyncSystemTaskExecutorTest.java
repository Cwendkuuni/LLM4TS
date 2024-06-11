package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.quality.Strictness;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowInstance;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueueMessage;
import com.netflix.conductor.dao.WorkflowInstanceDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.QueueUtilsTests;

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
 private WorkflowInstanceDAO workflowInstanceDAO;
 @Mock
 private WorkflowModel workflowModel;

 @InjectMocks
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
 @Mock(strictness = Strictness.LENIENT)
 private WorkflowSystemTask systemTask;
 @Rule
 public MockitoJUnit mockitoRule = new MockitoJUnit();

 @Before
 public void setup() {
 Monitors.setInstance(monitors);
 }

 @Test
 public void testExecute_taskIsNotFound_taskRemovedFromQueue() throws Exception {
 // Given
 String taskId = "taskId";
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
 QueueMessage queueMessage = new QueueMessage();
 queueMessage.setTaskId(taskId);
 queueMessage.setTaskType("taskType");
 queueMessage.setQueue("queueName");
 doReturn(queueMessage).when(queueDAO).get(queueMessage);
 doNothing().when(queueDAO).remove(queueMessage);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Then
 verify(queueDAO).remove(queueMessage);
 }

 @Test
 public void testExecute_taskIsAlreadyCompleted_taskIsRemovedFromQueue() throws Exception {
 // Given
 String taskId = "taskId";
 TaskModel task = new TaskModel();
 task.setTaskId(taskId);
 task.setStatus(ExecutionStatus.COMPLETED);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 QueueMessage queueMessage = new QueueMessage();
 queueMessage.setTaskId(taskId);
 queueMessage.setTaskType("taskType");
 queueMessage.setQueue("queueName");
 doReturn(queueMessage).when(queueDAO).get(queueMessage);
 doNothing().when(queueDAO).remove(queueMessage);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Then
 verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
 }

 @Test
 public void testExecute_taskIsInProgress_taskIsNotPostponed() throws Exception {
 // Given
 String taskId = "taskId";
 TaskModel task = new TaskModel();
 task.setTaskId(taskId);
 task.setStatus(ExecutionStatus.IN_PROGRESS);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 QueueMessage queueMessage = new QueueMessage();
 queueMessage.setTaskId(taskId);
 queueMessage.setTaskType("taskType");
 queueMessage.setQueue("queueName");
 doReturn(queueMessage).when(queueDAO).get(queueMessage);
 doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Then
 verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
 }

 @Test
 public void testExecute_taskIsScheduled_taskIsPostponed() throws Exception {
 // Given
 String taskId = "taskId";
 TaskModel task = new TaskModel();
 task.setTaskId(taskId);
 task.setStatus(ExecutionStatus.SCHEDULED);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 QueueMessage queueMessage = new QueueMessage();
 queueMessage.setTaskId(taskId);
 queueMessage.setTaskType("taskType");
 queueMessage.setQueue("queueName");
 doReturn(queueMessage).when(queueDAO).get(queueMessage);
 doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Then
 verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), anyInt());
 }

 @Test
 public void testExecute_taskIsRateLimited_taskIsPostponed() throws Exception {
 // Given
 String taskId = "taskId";
 TaskModel task = new TaskModel();
 task.setTaskId(taskId);
 task.setStatus(ExecutionStatus.SCHEDULED);
 task.setRateLimitPerFrequency(2);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 QueueMessage queueMessage = new QueueMessage();
 queueMessage.setTaskId(taskId);
 queueMessage.setTaskType("taskType");
 queueMessage.setQueue("queueName");
 doReturn(queueMessage).when(queueDAO).get(queueMessage);
 doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());
 doReturn(workflowModel).when(metadataDAO).getTaskDef(task.getTaskDefName());
 doReturn(Optional.of(5)).when(executionDAOFacade).getTasksInProgressWithinTimeWindow(
 task, QueueUtils.getQueueName(task), 10000);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Then
 verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), anyInt());
 }

 @Test
 public void testExecute_taskIsConcurrentlyExecutionedLimited_taskIsPostponed() throws Exception {
 // Given
 String taskId = "taskId";
 TaskModel task = new TaskModel();
 task.setTaskId(taskId);
 task.setStatus(ExecutionStatus.SCHEDULED);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 QueueMessage queueMessage = new QueueMessage();
 queueMessage.setTaskId(taskId);
 queueMessage.setTaskType("taskType");
 queueMessage.setQueue("queueName");
 doReturn(queueMessage).when(queueDAO).get(queueMessage);
 doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());
 doReturn(Optional.of(5)).when(executionDAOFacade).getTasksInProgressWithinTimeWindow(
 task, QueueUtils.getQueueName(task), 10000);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 // Then
 verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), anyInt());
 }

 @Test
 public void testExecute_taskIsCompleted_workflowIsDecided() throws Exception {
 // Given
 String taskId = "taskId";
 TaskModel task = new TaskModel();
 task.setTaskId(taskId);
 task.setStatus(ExecutionStatus.COMPLETED);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 QueueMessage queueMessage = new QueueMessage();
 queueMessage.setTaskId(taskId);
 queueMessage.setTaskType("taskType");
 queueMessage.setQueue("queueName");
 doReturn(queueMessage).when(queueDAO).get(queueMessage);
 doNothing().when(queueDAO).remove(queueMessage);
 doReturn(workflowInstanceDAO).when(workflowExecutor).getWorkflowInstance(workflowModel);
 doReturn(Optional.of(workflowInstanceDAO)).when(workflowExecutor).decide(workflowId);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Then
 verify(workflowExecutor).decide(workflowId);
 }

 @Test
 public void testPostponeQuietly_queueMessageIsPostponed() throws Exception {
 // Given
 String queueName = "queueName";
 TaskModel task = new TaskModel();
 task.setTaskId("taskId");
 QueueMessage queueMessage = new QueueMessage();
 queueMessage.setTaskId("taskId");
 queueMessage.setTaskType("taskType");
 queueMessage.setQueue(queueName);
 when(queueDAO.get(queueMessage)).thenReturn(queueMessage);
 doNothing().when(queueDAO).postpone(queueName, "taskId", anyInt(), anyInt());

 // When
 asyncSystemTaskExecutor.postponeQuietly(queueName, task);

 // Then
 verify(queueDAO).postpone(queueName, "taskId", anyInt(), anyInt());
 }

 @Test
 public void testLoadTaskQuietly_taskIsLoaded() throws Exception {
 // Given
 String taskId = "taskId";
 TaskModel task = new TaskModel();
 task.setTaskId(taskId);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

 // When
 TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

 // Then
 assertEquals(task, loadedTask);
 }
}