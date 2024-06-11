package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.DummyWorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.execution.WorkflowInstance;
import com.netflix.conductor.core.model.task.TaskModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.utils.TestUtils;

public class AsyncSystemTaskExecutorTest {

 private static final long QUEUE_WAIT_TIME = 10L;
 private static final String WORKFLOW_ID = "workflow-id";
 private static final String TASK_ID = "task-id";
 private static final String QUEUE_NAME = "queueName";
 private static final String TASK_DEF_NAME = "taskDefName";
 private static final String TASK_TYPE = "taskType";
 private static final String WORKFLOW_PRIORITY = "workflowPriority";

 @Autowired
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 @MockBean
 private ExecutionDAOFacade executionDAOFacade;

 @MockBean
 private QueueDAO queueDAO;

 @MockBean
 private MetadataDAO metadataDAO;

 @MockBean
 private ConductorProperties conductorProperties;

 @MockBean
 private Monitors monitors;

 @MockBean
 private WorkflowExecutor workflowExecutor;

 @Before
 public void setup() {
 TaskModel task = new TaskModel();
 task.setTaskId(TASK_ID);
 task.setTaskDefName(TASK_DEF_NAME);
 task.setTaskType(TASK_TYPE);
 task.setWorkflowInstanceId(WORKFLOW_ID);

 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);
 when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(new WorkflowInstance());

 when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(true);

 DummyWorkflowSystemTask systemTask = new DummyWorkflowSystemTask();
 when(systemTask.isAsyncComplete(task)).thenReturn(false);
 when(systemTask.isTaskRetrievalRequired()).thenReturn(false);
 when(systemTask.start(any(), any(), any())).thenReturn(null);
 when(systemTask.execute(any(), any(), any())).thenReturn(null);

 ArgumentCaptor<TaskModel> taskArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);
 ArgumentCaptor<String> queueNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Long> callbackAfterSecondsArgumentCaptor = ArgumentCaptor.forClass(Long.class);
 ArgumentCaptor<Long> workflowPriorityArgumentCaptor = ArgumentCaptor.forClass(Long.class);
 ArgumentCaptor<String> taskIdArgumentCaptor = ArgumentCaptor.forClass(String.class);

 when(queueDAO.postpone(QUEUE_NAME, taskIdArgumentCaptor.capture(), workflowPriorityArgumentCaptor.capture(), callbackAfterSecondsArgumentCaptor.capture()))
         .thenReturn(null);
 when(queueDAO.remove(QUEUE_NAME, taskIdArgumentCaptor.capture())).thenReturn(true);
 when(queueDAO.getTaskCount(QUEUE_NAME)).thenReturn(0);
 when(metadataDAO.getTaskDef(TASK_DEF_NAME)).thenReturn(new WorkflowModel());
 when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(TestUtils.createDuration(1, TimeUnit.SECONDS));
 }

 @After
 public void tearDown() {
 // Clear the mocks
 executionDAOFacade = null;
 queueDAO = null;
 metadataDAO = null;
 conductorProperties = null;
 monitors = null;
 workflowExecutor = null;
 }

 @Test
 public void testExecuteWithTaskAlreadyCompleted() {
 TaskModel task = new TaskModel();
 task.setTaskId(TASK_ID);
 task.setTaskDefName(TASK_DEF_NAME);
 task.setTaskType(TASK_TYPE);
 task.setWorkflowInstanceId(WORKFLOW_ID);
 task.setStatus(TaskModel.Status.COMPLETED);
 TaskModel expectedTask = new TaskModel();
 expectedTask.setTaskId(TASK_ID);
 expectedTask.setTaskDefName(TASK_DEF_NAME);
 expectedTask.setTaskType(TASK_TYPE);
 expectedTask.setWorkflowInstanceId(WORKFLOW_ID);
 expectedTask.setStatus(TaskModel.Status.COMPLETED);

 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);

 asyncSystemTaskExecutor.execute(new DummyWorkflowSystemTask(), TASK_ID);

 assertEquals(expectedTask, executionDAOFacade.getTaskModel(TASK_ID));
 }

 @Test
 public void testExecuteWithWorkflowAlreadyCompleted() {
 TaskModel task = new TaskModel();
 task.setTaskId(TASK_ID);
 task.setTaskDefName(TASK_DEF_NAME);
 task.setTaskType(TASK_TYPE);
 task.setWorkflowInstanceId(WORKFLOW_ID);
 WorkflowInstance workflowInstance = new WorkflowInstance();
 workflowInstance.setStatus(Status.COMPLETED);

 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);
 when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflowInstance);

 asyncSystemTaskExecutor.execute(new DummyWorkflowSystemTask(), TASK_ID);

 assertEquals(workflowInstance.getStatus(), TaskModel.Status.COMPLETED);
 }

 @Test
 public void testExecuteWithInProgressLimitExceeded() {
 TaskModel task1 = new TaskModel();
 task1.setTaskId("task1");
 task1.setTaskDefName(TASK_DEF_NAME);
 task1.setTaskType(TASK_TYPE);
 task1.setWorkflowInstanceId(WORKFLOW_ID);
 task1.setStatus(TaskModel.Status.IN_PROGRESS);

 TaskModel task2 = new TaskModel();
 task2.setTaskId(TASK_ID);
 task2.setTaskDefName(TASK_DEF_NAME);
 task2.setTaskType(TASK_TYPE);
 task2.setWorkflowInstanceId(WORKFLOW_ID);
 task2.setStatus(TaskModel.Status.SCHEDULED);

 when(executionDAOFacade.getTaskModel("task1")).thenReturn(task1);
 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task2);

 asyncSystemTaskExecutor.execute(new DummyWorkflowSystemTask(), TASK_ID);

 assertEquals(task1.getStatus(), TaskModel.Status.IN_PROGRESS);
 // The new task should be postponed
 verify(queueDAO).postpone(QUEUE_NAME, eq(TASK_ID), anyLong(), anyLong());
 }

 @Test
 public void testExecuteWithRateLimitExceeded() {
 TaskModel task = new TaskModel();
 task.setTaskId(TASK_ID);
 task.setTaskDefName(TASK_DEF_NAME);
 task.setTaskType(TASK_TYPE);
 task.setWorkflowInstanceId(WORKFLOW_ID);
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setRateLimitPerFrequency(1);

 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);
 WorkflowModel taskDef = new WorkflowModel();
 taskDef.setRateLimitPerFrequency(2);
 when(metadataDAO.getTaskDef(TASK_DEF_NAME)).thenReturn(taskDef);

 asyncSystemTaskExecutor.execute(new DummyWorkflowSystemTask(), TASK_ID);

 verify(queueDAO).postpone(QUEUE_NAME, eq(TASK_ID), anyLong(), anyLong());
 }

 @Test
 public void testExecuteWithTaskExecutionCompleted() throws InterruptedException {
 TaskModel task = new TaskModel();
 task.setTaskId(TASK_ID);
 task.setTaskDefName(TASK_DEF_NAME);
 task.setTaskType(TASK_TYPE);
 task.setWorkflowInstanceId(WORKFLOW_ID);
 task.setStatus(TaskModel.Status.SCHEDULED);

 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);

 asyncSystemTaskExecutor.execute(new DummyWorkflowSystemTask(), TASK_ID);

 // Give the system task enough time to execute
 TimeUnit.SECONDS.sleep(1);

 // The task should have been removed from the queue
 verify(queueDAO).remove(QUEUE_NAME, eq(TASK_ID));
 }

 @Test
 public void testExecuteWithTaskExecutionFailed() {
 TaskModel task = new TaskModel();
 task.setTaskId(TASK_ID);
 task.setTaskDefName(TASK_DEF_NAME);
 task.setTaskType(TASK_TYPE);
 task.setWorkflowInstanceId(WORKFLOW_ID);
 task.setStatus(TaskModel.Status.SCHEDULED);

 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);

 asyncSystemTaskExecutor.execute(new DummyWorkflowSystemTaskWithException(), TASK_ID);

 // The task should still be in the queue
 verify(queueDAO, times(0)).remove(QUEUE_NAME, eq(TASK_ID));
 }
}