package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.Queue;
import com.netflix.conductor.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

@SuppressWarnings("unchecked")
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
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private static final String WORKFLOW_ID = "workflowId";
    private static final String TASK_ID = "taskId";
    private static final String QUEUE_NAME = "queueName";
    private static final String TASK_TYPE = "taskType";

    private static final WorkflowModel WORKFLOW_MODEL = new WorkflowModel();
    private static final TaskModel TASK_MODEL = new TaskModel();
    private static final WorkflowSystemTask SYSTEM_TASK = mock(WorkflowSystemTask.class);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(WORKFLOW_MODEL);
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(TASK_MODEL);
        when(queueDAO.remove(QUEUE_NAME, TASK_ID)).thenReturn(true);
        when(queueDAO.getQueue(QUEUE_NAME)).thenReturn(new Queue());
        when(queueDAO.postpone(QUEUE_NAME, TASK_ID, 0, 0)).thenReturn(true);
        when(metadataDAO.getTaskDef(TASK_TYPE)).thenReturn(new TaskDef());
        doNothing().when(workflowExecutor).decide(WORKFLOW_ID);
    }

    @Test
    public void testExecute_taskIsTerminal_removeFromQueue() {
        TASK_MODEL.setStatus(TaskModel.Status.TERMINAL);
        when(executionDAOFacade.exceedsInProgressLimit(TASK_MODEL)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                TASK_MODEL, new TaskDef()))
                .thenReturn(false);
        when(TASK_MODEL.getWorkflowInstanceId()).thenReturn(WORKFLOW_ID);
        when(WORKFLOW_MODEL.getStatus()).thenReturn(WorkflowModel.Status.TERMINAL);

        asyncSystemTaskExecutor.execute(SYSTEM_TASK, TASK_ID);

        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_taskIsTerminal_doNotRemoveFromQueueIfError() {
        TASK_MODEL.setStatus(TaskModel.Status.TERMINAL);
        when(executionDAOFacade.exceedsInProgressLimit(TASK_MODEL)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                TASK_MODEL, new TaskDef()))
                .thenReturn(false);
        when(TASK_MODEL.getWorkflowInstanceId()).thenReturn(WORKFLOW_ID);
        when(WORKFLOW_MODEL.getStatus()).thenReturn(WorkflowModel.Status.TERMINAL);
        doThrow(new RuntimeException("Error")).when(queueDAO).remove(QUEUE_NAME, TASK_ID);

        asyncSystemTaskExecutor.execute(SYSTEM_TASK, TASK_ID);

        verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_exceedsInProgressLimit_postponeTask() {
        TASK_MODEL.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(TASK_MODEL)).thenReturn(true);
        when(TASK_MODEL.getWorkflowInstanceId()).thenReturn(WORKFLOW_ID);

        asyncSystemTaskExecutor.execute(SYSTEM_TASK, TASK_ID);

        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 0, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_exceedsRateLimitPerFrequency_postponeTask() {
        TASK_MODEL.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                TASK_MODEL, new TaskDef())).thenReturn(true);
        when(TASK_MODEL.getWorkflowInstanceId()).thenReturn(WORKFLOW_ID);

        asyncSystemTaskExecutor.execute(SYSTEM_TASK, TASK_ID);

        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 0, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_taskIsInProgress_updateTaskStatusAndCallback() {
        TASK_MODEL.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(TASK_MODEL)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                TASK_MODEL, new TaskDef()))
                .thenReturn(false);
        when(TASK_MODEL.getWorkflowInstanceId()).thenReturn(WORKFLOW_ID);
        when(queueDAO.getQueue(QUEUE_NAME)).thenReturn(new Queue());
        when(queueDAO.postpone(QUEUE_NAME, TASK_ID, 0, 0)).thenReturn(false);
        when(queueDAO.remove(QUEUE_NAME, TASK_ID)).thenReturn(false);
        when(systemTask.isAsyncComplete(TASK_MODEL)).thenReturn(false);

        asyncSystemTaskExecutor.execute(SYSTEM_TASK, TASK_ID);

        verify(TASK_MODEL).setStatus(TaskModel.Status.IN_PROGRESS);
        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 0, systemTaskCallbackTime);
    }

    @Test
    public void testExecute_taskIsCompletedAsync_removeFromQueue() {
        TASK_MODEL.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(TASK_MODEL)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                TASK_MODEL, new TaskDef()))
                .thenReturn(false);
        when(TASK_MODEL.getWorkflowInstanceId()).thenReturn(WORKFLOW_ID);
        when(queueDAO.getQueue(QUEUE_NAME)).thenReturn(new Queue());
        when(queueDAO.postpone(QUEUE_NAME, TASK_ID, 0, 0)).thenReturn(false);
        when(queueDAO.remove(QUEUE_NAME, TASK_ID)).thenReturn(false);
        when(systemTask.isAsyncComplete(TASK_MODEL)).thenReturn(true);

        asyncSystemTaskExecutor.execute(SYSTEM_TASK, TASK_ID);

        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_taskHasEnded_removeFromQueue() {
        TASK_MODEL.setStatus(TaskModel.Status.SCHEDULED);
        TASK_MODEL.setEndTime(System.currentTimeMillis() - 2 * systemTaskCallbackTime);
        when(executionDAOFacade.exceedsInProgressLimit(TASK_MODEL)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                TASK_MODEL, new TaskDef()))
                .thenReturn(false);
        when(TASK_MODEL.getWorkflowInstanceId()).thenReturn(WORKFLOW_ID);

        asyncSystemTaskExecutor.execute(SYSTEM_TASK, TASK_ID);

        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_taskExecutionError_removeFromQueue() {
        TASK_MODEL.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(TASK_MODEL)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                TASK_MODEL, new TaskDef()))
                .thenReturn(false);
        when(TASK_MODEL.getWorkflowInstanceId()).thenReturn(WORKFLOW_ID);
        when(queueDAO.getQueue(QUEUE_NAME)).thenReturn(new Queue());
        when(queueDAO.postpone(QUEUE_NAME, TASK_ID, 0, 0)).thenReturn(false);
        when(queueDAO.remove(QUEUE_NAME, TASK_ID)).thenReturn(false);
        when(systemTaskCallbackTime).thenReturn(0);

        asyncSystemTaskExecutor.execute(SYSTEM_TASK, TASK_ID);

        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_taskIsCancelled_removeFromQueue() {
        TASK_MODEL.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(TASK_MODEL)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                TASK_MODEL, new TaskDef()))
                .thenReturn(false);
        when(TASK_MODEL.getWorkflowInstanceId()).thenReturn(WORKFLOW_ID);
        when(TASK_MODEL.setStatus(TaskModel.Status.CANCELED)).thenReturn(true);
        when(queueDAO.getQueue(QUEUE_NAME)).thenReturn(new Queue());
        when(queueDAO.postpone(QUEUE_NAME, TASK_ID, 0, 0)).thenReturn(false);
        when(queueDAO.remove(QUEUE_NAME, TASK_ID)).thenReturn(false);

        asyncSystemTaskExecutor.execute(SYSTEM_TASK, TASK_ID);

        verify(TASK_MODEL).setStatus(TaskModel.Status.CANCELED);
        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_workflowIsTerminal_decideWorkflow() {
        TASK_MODEL.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(TASK_MODEL)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                TASK_MODEL, new TaskDef()))
                .thenReturn(false);
        when(TASK_MODEL.getWorkflowInstanceId()).thenReturn(WORKFLOW_ID);
        when(WORKFLOW_MODEL.getStatus()).thenReturn(WorkflowModel.Status.TERMINAL);

        asyncSystemTaskExecutor.execute(SYSTEM_TASK, TASK_ID);

        verify(workflowExecutor).decide(WORKFLOW_ID);
    }

    @Test
    public void testExecute_taskIsInProgress_updateTaskStatusAndCallback() {
        TASK_MODEL.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(TASK_MODEL)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                TASK_MODEL, new TaskDef()))
                .thenReturn(false);
        when(TASK_MODEL.getWorkflowInstanceId()).thenReturn(WORKFLOW_ID);
        when(queueDAO.getQueue(QUEUE_NAME)).thenReturn(new Queue());
        when(queueDAO.postpone(QUEUE_NAME, TASK_ID, 0, 0)).thenReturn(false);
        when(queueDAO.remove(QUEUE_NAME, TASK_ID)).thenReturn(false);
        when(systemTask.isAsyncComplete(TASK_MODEL)).thenReturn(false);
        when(systemTask.getEvaluationOffset(TASK_MODEL, systemTaskCallbackTime)).thenReturn(Optional.of(1000L));

        asyncSystemTaskExecutor.execute(SYSTEM_TASK, TASK_ID);

        verify(TASK_MODEL).setStatus(TaskModel.Status.IN_PROGRESS);
        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 0, systemTaskCallbackTime + 1000);
    }

    private static class TaskDef {}
}