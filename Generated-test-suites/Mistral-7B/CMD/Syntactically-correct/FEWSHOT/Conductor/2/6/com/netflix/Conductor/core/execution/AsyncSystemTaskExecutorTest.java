package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueueMessage;

@SuppressWarnings("deprecation")
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

    @InjectMocks
    private AsyncSystemTaskExecutor systemUnderTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(
                Instant.ofEpochSecond(1000));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(
                Instant.ofEpochSecond(2000));
    }

    @Test
    public void execute_taskDoesNotExistInExecutionDAO_taskIsRemovedFromQueue() {
        String taskId = "task-id-1";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        TaskModel task = null;
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        systemUnderTest.execute(systemTask, taskId);

        ArgumentCaptor<QueueMessage> queueMessageArgumentCaptor = ArgumentCaptor.forClass(QueueMessage.class);
        queueDAO.remove(QueueUtils.getQueueName("task-id-1"), eq(taskId));
        verify(queueDAO).remove(queueMessageArgumentCaptor.capture());
        QueueMessage queueMessage = queueMessageArgumentCaptor.getValue();
        assertNull(queueMessage.getTask());
    }

    @Test
    public void execute_taskIsTerminal_taskIsRemovedFromQueue() {
        String taskId = "task-id-1";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-id-1");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        systemUnderTest.execute(systemTask, taskId);

        ArgumentCaptor<QueueMessage> queueMessageArgumentCaptor = ArgumentCaptor.forClass(QueueMessage.class);
        queueDAO.remove(eq(QueueUtils.getQueueName("task-id-1")), eq(taskId));
        verify(queueDAO).remove(queueMessageArgumentCaptor.capture());
        QueueMessage queueMessage = queueMessageArgumentCaptor.getValue();
        assertNull(queueMessage.getTask());
    }

    @Test
    public void execute_taskIsInProgress_taskIsUpdatedAndNotRemovedFromQueue() {
        String taskId = "task-id-1";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-id-1");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false)).thenReturn(workflow);

        systemUnderTest.execute(systemTask, taskId);

        verify(executionDAOFacade).updateTask(task);
        verifyNoInteractions(queueDAO);
    }

    @Test
    public void execute_taskIsScheduled_taskIsPostponedInQueue() {
        String taskId = "task-id-1";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-id-1");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false)).thenReturn(workflow);

        systemUnderTest.execute(systemTask, taskId);

        ArgumentCaptor<QueueMessage> queueMessageArgumentCaptor = ArgumentCaptor.forClass(QueueMessage.class);
        queueDAO.postpone(
                eq(QueueUtils.getQueueName("task-id-1")),
                eq(taskId),
                eq(task.getWorkflowPriority()),
                ge(task.getCallbackAfterSeconds()));
        verify(queueDAO).postpone(queueMessageArgumentCaptor.capture());
        QueueMessage queueMessage = queueMessageArgumentCaptor.getValue();
        assertSame(task, queueMessage.getTask());
    }

    @Test
    public void execute_taskIsInProgressAndAsyncComplete_taskIsRemovedFromQueue() {
        String taskId = "task-id-1";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-id-1");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false)).thenReturn(workflow);

        systemTask.setAsyncComplete(true);

        systemUnderTest.execute(systemTask, taskId);

        ArgumentCaptor<QueueMessage> queueMessageArgumentCaptor = ArgumentCaptor.forClass(QueueMessage.class);
        queueDAO.remove(eq(QueueUtils.getQueueName("task-id-1")), eq(taskId));
        verify(queueDAO).remove(queueMessageArgumentCaptor.capture());
        QueueMessage queueMessage = queueMessageArgumentCaptor.getValue();
        assertNull(queueMessage.getTask());
    }

    @Test
    public void execute_taskIsInProgressAndNotAsyncComplete_taskIsPostponedInQueue() {
        String taskId = "task-id-1";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-id-1");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false)).thenReturn(workflow);

        systemUnderTest.execute(systemTask, taskId);

        ArgumentCaptor<QueueMessage> queueMessageArgumentCaptor = ArgumentCaptor.forClass(QueueMessage.class);
        queueDAO.postpone(
                eq(QueueUtils.getQueueName("task-id-1")),
                eq(taskId),
                eq(task.getWorkflowPriority()),
                ge(task.getCallbackAfterSeconds()));
        verify(queueDAO).postpone(queueMessageArgumentCaptor.capture());
        QueueMessage queueMessage = queueMessageArgumentCaptor.getValue();
        assertSame(task, queueMessage.getTask());
    }

    @Test
    public void execute_taskIsTerminalAndHasNotBeenEvaluated_workflowIsEvaluated() {
        String taskId = "task-id-1";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-id-1");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        workflowExecutor.decide = mock(Runnable.class);

        systemUnderTest.execute(systemTask, taskId);

        verify(workflowExecutor).decide("workflow-id-1");
    }

    @Test
    public void execute_taskIsTerminalAndHasBeenEvaluated_workflowIsNotEvaluated() {
        String taskId = "task-id-1";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-id-1");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        when(workflowExecutor.getDecidedWorkflows()).thenReturn(new HashSet<String>());

        systemUnderTest.execute(systemTask, taskId);

        verifyZeroInteractions(workflowExecutor);
    }

    @Test
    public void execute_exceptionIsThrown_taskIsNotUpdatedInExecutionDAO() {
        String taskId = "task-id-1";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-id-1");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false)).thenThrow(new RuntimeException());

        systemUnderTest.execute(systemTask, taskId);

        verifyZeroInteractions(executionDAOFacade);
    }
}