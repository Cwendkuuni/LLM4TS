package com.netflix.conductor.core.execution;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.Tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private ConductorProperties conductorProperties;

    @InjectMocks
    private AsyncSystemTaskExecutor systemUnderTest;

    @Mock
    private WorkflowSystemTask mockSystemTask;

    @Mock
    private TaskModel mockTask;

    @Mock
    private WorkflowModel mockWorkflow;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(
                Instant.of(0, 1000L * 60L * 5L)); // 5 minutes
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(
                Instant.of(0, 1000L * 60L * 1L)); // 1 minute
    }

    @Test
    public void testExecute_taskIsNotFound_queueMessageIsRemoved() {
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        systemUnderTest.execute(mockSystemTask, taskId);

        verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_taskIsTerminal_queueMessageIsRemoved() {
        when(executionDAOFacade.getTaskModel(mockTask.getTaskId())).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.TERMINAL);

        systemUnderTest.execute(mockSystemTask, mockTask.getTaskId());

        verify(queueDAO).remove(anyString(), mockTask.getTaskId());
    }

    @Test
    public void testExecute_taskIsScheduled_executionLimitIsReached_queueMessageIsPostponed() {
        when(executionDAOFacade.getTaskModel(mockTask.getTaskId())).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.exceedsInProgressLimit(mockTask))
                .thenReturn(true);

        systemUnderTest.execute(mockSystemTask, mockTask.getTaskId());

        verify(queueDAO).postpone(anyString(), mockTask.getTaskId(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskIsScheduled_rateLimitIsReached_queueMessageIsPostponed() {
        when(executionDAOFacade.getTaskModel(mockTask.getTaskId())).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(mockTask, any()))
                .thenReturn(true);

        systemUnderTest.execute(mockSystemTask, mockTask.getTaskId());

        verify(queueDAO).postpone(anyString(), mockTask.getTaskId(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_workflowIsTerminal_taskIsCancelled() {
        when(executionDAOFacade.getTaskModel(mockTask.getTaskId())).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getWorkflowModel(mockWorkflow.getWorkflowId(), true))
                .thenReturn(mockWorkflow);
        when(mockWorkflow.getStatus()).thenReturn(WorkflowModel.Status.TERMINAL);

        systemUnderTest.execute(mockSystemTask, mockTask.getTaskId());

        verify(mockTask).setStatus(TaskModel.Status.CANCELED);
    }

    @Test
    public void testExecute_taskIsInProgress_workflowIsTerminal_taskIsEnded() {
        when(executionDAOFacade.getTaskModel(mockTask.getTaskId())).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getWorkflowModel(mockWorkflow.getWorkflowId(), true))
                .thenReturn(mockWorkflow);
        when(mockWorkflow.getStatus()).thenReturn(WorkflowModel.Status.TERMINAL);

        systemUnderTest.execute(mockSystemTask, mockTask.getTaskId());

        verify(mockTask).setEndTime(anyLong());
    }

    @Test
    public void testExecute_taskIsInProgress_workflowIsNotTerminal_callbackIsSet() {
        when(executionDAOFacade.getTaskModel(mockTask.getTaskId())).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getWorkflowModel(mockWorkflow.getWorkflowId(), true))
                .thenReturn(mockWorkflow);
        when(mockWorkflow.getStatus()).thenReturn(WorkflowModel.Status.IN_PROGRESS);

        systemUnderTest.execute(mockSystemTask, mockTask.getTaskId());

        verify(mockTask).setCallbackAfterSeconds(anyLong());
    }

    @Test
    public void testExecute_taskIsScheduled_workflowIsTerminal_taskIsEnded() {
        when(executionDAOFacade.getTaskModel(mockTask.getTaskId())).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getWorkflowModel(mockWorkflow.getWorkflowId(), false))
                .thenReturn(mockWorkflow);
        when(mockWorkflow.getStatus()).thenReturn(WorkflowModel.Status.TERMINAL);

        systemUnderTest.execute(mockSystemTask, mockTask.getTaskId());

        verify(mockTask).setEndTime(anyLong());
    }

    @Test
    public void testExecute_taskIsInProgress_workflowIsNotTerminal_callbackIsNotSet() {
        when(executionDAOFacade.getTaskModel(mockTask.getTaskId())).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getWorkflowModel(mockWorkflow.getWorkflowId(), false))
                .thenReturn(mockWorkflow);
        when(mockWorkflow.getStatus()).thenReturn(WorkflowModel.Status.IN_PROGRESS);

        systemUnderTest.execute(mockSystemTask, mockTask.getTaskId());

        verify(mockTask, never()).setCallbackAfterSeconds(anyLong());
    }

    @Test
    public void testExecute_taskIsTerminal_workflowIsNotTerminal_queueMessageIsRemoved() {
        when(executionDAOFacade.getTaskModel(mockTask.getTaskId())).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.TERMINAL);

        systemUnderTest.execute(mockSystemTask, mockTask.getTaskId());

        verify(queueDAO).remove(anyString(), mockTask.getTaskId());
    }

    @Test
    public void testExecute_taskIsInProgress_workflowIsTerminal_queueMessageIsNotRemoved() {
        when(executionDAOFacade.getTaskModel(mockTask.getTaskId())).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getWorkflowModel(mockWorkflow.getWorkflowId(), false))
                .thenReturn(mockWorkflow);
        when(mockWorkflow.getStatus()).thenReturn(WorkflowModel.Status.TERMINAL);

        systemUnderTest.execute(mockSystemTask, mockTask.getTaskId());

        verify(queueDAO, never()).remove(anyString(), mockTask.getTaskId());
    }

    @Test
    public void testExecute_taskIsScheduled_workflowIsTerminal_queueMessageIsNotRemoved() {
        when(executionDAOFacade.getTaskModel(mockTask.getTaskId())).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getWorkflowModel(mockWorkflow.getWorkflowId(), false))
                .thenReturn(mockWorkflow);
        when(mockWorkflow.getStatus()).thenReturn(WorkflowModel.Status.TERMINAL);

        systemUnderTest.execute(mockSystemTask, mockTask.getTaskId());

        verify(queueDAO, never()).remove(anyString(), mockTask.getTaskId());
    }

    @Test
    public void testExecute_taskIsTerminal_workflowIsTerminal_queueMessageIsNotRemoved() {
        when(executionDAOFacade.getTaskModel(mockTask.getTaskId())).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.TERMINAL);

        when(executionDAOFacade.getWorkflowModel(mockWorkflow.getWorkflowId(), false))
                .thenReturn(mockWorkflow);
        when(mockWorkflow.getStatus()).thenReturn(WorkflowModel.Status.TERMINAL);

        systemUnderTest.execute(mockSystemTask, mockTask.getTaskId());

        verify(queueDAO, never()).remove(anyString(), mockTask.getTaskId());
    }

    @Test
    public void testExecute_taskIsTerminal_workflowIsNotTerminal_queueMessageIsNotRemoved() {
        when(executionDAOFacade.getTaskModel(mockTask.getTaskId())).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.TERMINAL);

        when(executionDAOFacade.getWorkflowModel(mockWorkflow.getWorkflowId(), false))
                .thenReturn(mockWorkflow);
        when(mockWorkflow.getStatus()).thenReturn(WorkflowModel.Status.IN_PROGRESS);

        systemUnderTest.execute(mockSystemTask, mockTask.getTaskId());

        verify(queueDAO, never()).remove(anyString(), mockTask.getTaskId());
    }

    @Test
    public void testExecute_taskIsInProgress_workflowIsTerminal_queueMessageIsNotRemoved() {
        when(executionDAOFacade.getTaskModel(mockTask.getTaskId())).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getWorkflowModel(mockWorkflow.getWorkflowId(), false))
                .thenReturn(mockWorkflow);
        when(mockWorkflow.getStatus()).thenReturn(WorkflowModel.Status.TERMINAL);

        systemUnderTest.execute(mockSystemTask, mockTask.getTaskId());

        verify(queueDAO, never()).remove(anyString(), mockTask.getTaskId());
    }

    @Test
    public void testExecute_taskIsInProgress_workflowIsNotTerminal_queueMessageIsNotPostponed() {
        when(executionDAOFacade.getTaskModel(mockTask.getTaskId())).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getWorkflowModel(mockWorkflow.getWorkflowId(), false))
                .thenReturn(mockWorkflow);
        when(mockWorkflow.getStatus()).thenReturn(WorkflowModel.Status.IN_PROGRESS);

        systemUnderTest.execute(mockSystemTask, mockTask.getTaskId());

        verify(queueDAO, never()).postpone(anyString(), mockTask.getTaskId(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskIsScheduled_queueIsFull_queueMessageIsPostponed() {
        when(executionDAOFacade.getTaskModel(mockTask.getTaskId())).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        when(queueDAO.getSize(anyString()))
                .thenReturn(Optional.of(Integer.MAX_VALUE));

        systemUnderTest.execute(mockSystemTask, mockTask.getTaskId());

        verify(queueDAO).postpone(anyString(), mockTask.getTaskId(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskIsScheduled_queueIsNotFull_queueMessageIsNotPostponed() {
        when(executionDAOFacade.getTaskModel(mockTask.getTaskId())).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        when(queueDAO.getSize(anyString()))
                .thenReturn(Optional.of(0));

        systemUnderTest.execute(mockSystemTask, mockTask.getTaskId());

        verify(queueDAO, never()).postpone(anyString(), mockTask.getTaskId(), anyInt(), anyLong());
    }
}