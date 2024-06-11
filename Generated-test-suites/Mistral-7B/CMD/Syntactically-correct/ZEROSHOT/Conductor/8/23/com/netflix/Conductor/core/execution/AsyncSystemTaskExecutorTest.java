package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;import org.mockito.Mock;import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueMessage;
import com.netflix.conductor.utils.QueueUtils;

@Component
public class AsyncSystemTaskExecutorTest {

 private static final String TASK_ID = "taskId";
 private static final String WORKFLOW_ID = "workflowId";
 private static final String QUEUE_NAME = "queueName";
 private static final long SYSTEM_TASK_CALLBACK_TIME = 100L;
 private static final long QUEUE_TASK_MESSAGE_POSTPONE_SECS = 5000L;
 private static final int WORKFLOW_PRIORITY = 1;

 @Mock
 private ExecutionDAOFacade executionDAOFacade;
 @Mock
 private QueueDAO queueDAO;
 @Mock
 private MetadataDAO metadataDAO;
 @Mock
 private WorkflowExecutor workflowExecutor;
 @InjectMocks
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecute_taskNotFound() {
 // given
 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(Optional.empty());

 // when
 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

 // then
 verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
 assertNull(executionDAOFacade.getTaskModel(TASK_ID).orElse(null));
 }

 @Test
 public void testExecute_taskAlreadyCompleted() {
 // given
 WorkflowModel workflowModel = mock(WorkflowModel.class);
 when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, true)).thenReturn(workflowModel);
 when(workflowModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

 TaskModel taskModel = mock(TaskModel.class);
 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(Optional.of(taskModel));

 // when
 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

 // then
 verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
 }

 @Test
 public void testExecute_taskInProgress() {
 // given
 WorkflowModel workflowModel = mock(WorkflowModel.class);
 when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, true)).thenReturn(workflowModel);
 when(workflowModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

 TaskModel taskModel = mock(TaskModel.class);
 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(Optional.of(taskModel));

 ArgumentCaptor<QueueMessage> queueMessageCaptor = ArgumentCaptor.forClass(QueueMessage.class);

 // when
 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

 // then
 verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, WORKFLOW_PRIORITY, SYSTEM_TASK_CALLBACK_TIME);
 }

 @Test
 public void testExecute_taskError() {
 // given
 WorkflowModel workflowModel = mock(WorkflowModel.class);
 when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, true)).thenReturn(workflowModel);
 when(workflowModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

 TaskModel taskModel = mock(TaskModel.class);
 when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(Optional.of(taskModel));

 // when & then
 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);
 Monitors.verifyNoErrors(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask");
 }

 @Test
 public void testPostponeQuietly() {
 // given
 String queueName = QueueUtils.getQueueName("workflowInstanceId");

 // when
 asyncSystemTaskExecutor.postponeQuietly(queueName, mock(TaskModel.class));

 // then
 verify(queueDAO).postpone(queueName, anyString(), WORKFLOW_PRIORITY, QUEUE_TASK_MESSAGE_POSTPONE_SECS);
 }

 @Test
 public void testLoadTaskQuietly_taskNotFound() {
 // given

 // when
 TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

 // then
 assertNull(taskModel);
 }
}