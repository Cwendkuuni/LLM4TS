package com.netflix.conductor.core.execution;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.metadata.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowInstanceModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.Queue;
import com.netflix.conductor.dao.metadata.TaskDefinition;
import com.netflix.conductor.metrics.Monitors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = AsyncSystemTaskExecutor.class)
@ActiveProfiles("test")
public class AsyncSystemTaskExecutorTest {

 private static final String WORKFLOW_INSTANCE_ID = "workflowInstanceId";
 private static final String TASK_ID = "taskId";
 private static final String TASK_TYPE = "taskType";
 private static final String QUEUE_NAME = QueueUtils.getQueueName(new TaskModel().setId(TASK_ID));
 private static final String TASK_DEF_NAME = "taskDefName";
 private static final TaskDefinition TASK_DEF = mock(TaskDefinition.class);

 @Autowired
 private AsyncSystemTaskExecutor systemTaskExecutor;
 @Autowired
 private ExecutionDAOFacade executionDAOFacade;
 @Autowired
 private QueueDAO queueDAO;
 @Autowired
 private MetadataDAO metadataDAO;
 @Autowired
 private Monitors monitors;
 @Autowired
 private WorkflowExecutor workflowExecutor;

 @Before
 public void setup() {
 workflowExecutor = mock(WorkflowExecutor.class);
 when(workflowExecutor.decide(WORKFLOW_INSTANCE_ID)).thenReturn(null);
 when(metadataDAO.getTaskDef(TASK_DEF_NAME)).thenReturn(TASK_DEF);
 }

 @After
 public void tearDown() {
 // Clear any queued system tasks
 queueDAO.clear();
 }

 @Test
 public void testExecute_taskNotFound() {
 // Given
 String taskId = "nonExistentTask";
 WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

 // When
 systemTaskExecutor.execute(workflowSystemTask, taskId);

 // Then
 // No task found, no action taken
 Monitors.verifyNoRecordedErrors(monitors);
 }

 @Test
 public void testExecute_taskInCompletionState() {
 // Given
 TaskModel task = new TaskModel().setId(TASK_ID).setStatus(ExecutionStatus.COMPLETED);
 when(executionDAOFacade.getTaskModel(task.getId())).thenReturn(task);

 // When
 systemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

 // Then
 // Task is already completed, no action taken
 Monitors.verifyNoRecordedErrors(monitors);
 }

 @Test
 public void testExecute_taskScheduled() {
 // Given
 TaskModel task = new TaskModel().setId(TASK_ID).setStatus(ExecutionStatus.SCHEDULED);
 WorkflowModel workflow = new WorkflowInstanceModel().setId(WORKFLOW_INSTANCE_ID);
 WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
 when(executionDAOFacade.getTaskModel(task.getId())).thenReturn(task);
 when(executionDAOFacade.getWorkflowModel(WORKFLOW_INSTANCE_ID, true)).thenReturn(workflow);
 when(workflowExecutor.decide(WORKFLOW_INSTANCE_ID)).thenReturn(null);
 when(workflowSystemTask.isAsyncComplete(task)).thenReturn(false);

 // When
 systemTaskExecutor.execute(workflowSystemTask, TASK_ID);

 // Then
 // Task is still scheduled
 ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
 assertThat(queueDAO.getQueue(QUEUE_NAME), is(notNullValue()));
 assertThat(queueDAO.getQueue(QUEUE_NAME).getTasks(), is(not(nullValue())));
 assertThat(queueDAO.getQueue(QUEUE_NAME).getTasks().size(), is(1));
 assertThat(queueDAO.getQueue(QUEUE_NAME).getTasks().get(0), is(taskCaptor.capture()));
 assertThat(taskCaptor.getValue().getStatus(), is(ExecutionStatus.SCHEDULED));

 // No workflow evaluation since the task is not yet completed
 Monitors.verifyNoRecordedErrors(monitors);
 }

 @Test
 public void testExecute_taskInProgress() {
 // Given
 TaskModel task = new TaskModel().setId(TASK_ID).setStatus(ExecutionStatus.IN_PROGRESS);
 WorkflowModel workflow = new WorkflowInstanceModel().setId(WORKFLOW_INSTANCE_ID);
 WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
 when(executionDAOFacade.getTaskModel(task.getId())).thenReturn(task);
 when(executionDAOFacade.getWorkflowModel(WORKFLOW_INSTANCE_ID, true)).thenReturn(workflow);
 when(workflowExecutor.decide(WORKFLOW_INSTANCE_ID)).thenReturn(null);
 when(workflowSystemTask.isAsyncComplete(task)).thenReturn(true);

 // When
 systemTaskExecutor.execute(workflowSystemTask, TASK_ID);

 // Then
 // The task is completed and removed from the queue
 ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
 assertThat(queueDAO.getQueue(QUEUE_NAME), is(notNullValue()));
 assertThat(queueDAO.getQueue(QUEUE_NAME).getTasks(), is(not(nullValue())));
 assertThat(queueDAO.getQueue(QUEUE_NAME).getTasks().size(), is(0));
 Monitors.verifyNoRecordedErrors(monitors);
 }

 @Test
 public void testExecute_taskTerminalState() {
 // Given
 TaskModel task = new TaskModel().setId(TASK_ID).setStatus(ExecutionStatus.FAILED);
 when(executionDAOFacade.getTaskModel(task.getId())).thenReturn(task);

 // When
 systemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

 // Then
 // No action taken since the task is already completed
 Monitors.verifyNoRecordedErrors(monitors);
 // The task is removed from the queue
 ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
 assertThat(queueDAO.getQueue(QUEUE_NAME), nullValue());
 }

 @Test
 public void testExecute_taskWithExceededConcurrentLimit() {
 // Given
 TaskModel task = new TaskModel().setId(TASK_ID).setStatus(ExecutionStatus.SCHEDULED);
 when(executionDAOFacade.getTaskModel(task.getId())).thenReturn(task);
 when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

 // When
 systemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

 // Then
 // The task is postponed due to exceeded concurrent limit
 Monitors.verifyNoRecordedErrors(monitors);
 // The task is still in the queue
 ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
 assertThat(queueDAO.getQueue(QUEUE_NAME), is(notNullValue()));
 assertThat(queueDAO.getQueue(QUEUE_NAME).getTasks(), is(not(nullValue())));
 assertThat(queueDAO.getQueue(QUEUE_NAME).getTasks().size(), is(1));
 assertThat(queueDAO.getQueue(QUEUE_NAME).getTasks().get(0), is(taskCaptor.capture()));
 assertThat(taskCaptor.getValue().getStatus(), is(ExecutionStatus.SCHEDULED));
 }

 @Test
 public void testExecute_taskWithExceededRateLimit() {
 // Given
 TaskModel task = new TaskModel().setId(TASK_ID).setTaskDefName(TASK_DEF_NAME).setStatus(ExecutionStatus.SCHEDULED);
 when(executionDAOFacade.getTaskModel(task.getId())).thenReturn(task);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(task, TASK_DEF)).thenReturn(true);

 // When
 systemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

 // Then
 // The task is postponed due to exceeded rate limit
 Monitors.verifyNoRecordedErrors(monitors);
 // The task is still in the queue
 ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
 assertThat(queueDAO.getQueue(QUEUE_NAME), is(notNullValue()));
 assertThat(queueDAO.getQueue(QUEUE_NAME).getTasks(), is(not(nullValue())));
 assertThat(queueDAO.getQueue(QUEUE_NAME).getTasks().size(), is(1));
 assertThat(queueDAO.getQueue(QUEUE_NAME).getTasks().get(0), is(taskCaptor.capture()));
 assertThat(taskCaptor.getValue().getStatus(), is(ExecutionStatus.SCHEDULED));
 }
}