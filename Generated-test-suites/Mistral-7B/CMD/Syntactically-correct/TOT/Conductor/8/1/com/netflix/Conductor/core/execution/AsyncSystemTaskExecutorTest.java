package com.netflix.conductor.core.execution;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.Queue;

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

    @Mock
    private WorkflowSystemTask workflowSystemTask;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_taskAlreadyCompleted_queueRemoved() {
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(ExecutionStatus.COMPLETED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }

    @Test
    public void testExecute_taskInProgress_queuePostponed() {
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(Optional.of(new Queue()));
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO).postpone(QueueUtils.getQueueName(task), task.getTaskId(), 0, 1000L);
    }

    @Test
    public void testExecute_taskScheduled_queuePostponed() {
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(Optional.of(new Queue()));
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO).postpone(QueueUtils.getQueueName(task), task.getTaskId(), 0, 1000L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecute_workflowNotFound_exceptionThrown() {
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenThrow(new RuntimeException("Workflow not found"));
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
    }

    @Test
    public void testExecute_concurrencyLimitExceeded_queuePostponed() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(workflow.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO).postpone(QueueUtils.getQueueName(task), task.getTaskId(), 0, 1000L);
    }

    @Test
    public void testExecute_rateLimitExceeded_queuePostponed() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(workflow.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO).postpone(QueueUtils.getQueueName(task), task.getTaskId(), 0, 1000L);
    }

    @Test
    public void testExecute_taskTerminalState_queueRemoved() {
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(ExecutionStatus.FAILED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }
}