package com.netflix.conductor.core.execution;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.Queue;
import com.netflix.conductor.dao.QueueDAO.QueueAction;

@RunWith(MockitoJUnitRunner.class)
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

    @Mock
    private WorkflowSystemTask systemTask;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    private ArgumentCaptor<QueueAction> queueActionArgumentCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(Optional.of(task));
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(workflow);
        when(workflowExecutor.decide(anyString())).thenReturn(true);
        queueActionArgumentCaptor = ArgumentCaptor.forClass(QueueAction.class);
    }

    @Test
    public void execute_taskIsAlreadyCompleted_queueIsRemoved() {
        String queueName = QueueUtils.getQueueName(task);
        String taskId = task.getTaskId();

        when(task.getStatus()).thenReturn(ExecutionStatus.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test
    public void execute_taskHasTerminalStatus_queueIsRemoved() {
        String queueName = QueueUtils.getQueueName(task);
        String taskId = task.getTaskId();

        when(task.getStatus()).thenReturn(ExecutionStatus.FAILED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test
    public void execute_taskIsScheduled_queueIsPostponed() {
        String queueName = QueueUtils.getQueueName(task);
        String taskId = task.getTaskId();

        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(queueName, taskId, task.getWorkflowPriority(), anyLong());
    }

    @Test
    public void execute_taskIsInProgress_executionIsStarted() {
        String queueName = QueueUtils.getQueueName(task);
        String taskId = task.getTaskId();

        when(task.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask).start(workflow, task, workflowExecutor);
    }

    @Test(expected = RuntimeException.class)
    public void execute_taskIsTerminalButNotCompleted_queueIsNotRemoved() {
        String queueName = QueueUtils.getQueueName(task);
        String taskId = task.getTaskId();

        when(task.getStatus()).thenReturn(ExecutionStatus.TERMINATED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, never()).remove(queueName, taskId);
    }

    @Test
    public void execute_taskExecutionExceedsInProgressLimit_queueIsPostponed() {
        String queueName = QueueUtils.getQueueName(task);
        String taskId = task.getTaskId();
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(queueName, taskId, task.getWorkflowPriority(), anyLong());
    }

    @Test
    public void execute_taskExecutionExceedsRateLimitPerFrequency_queueIsPostponed() {
        String queueName = QueueUtils.getQueueName(task);
        String taskId = task.getTaskId();
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, workflow)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(queueName, taskId, task.getWorkflowPriority(), anyLong());
    }

    @Test
    public void execute_taskRetrievalFailed_queueIsNotTouched() {
        String queueName = QueueUtils.getQueueName(task);
        String taskId = task.getTaskId();

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, never()).remove(queueName, taskId);
        verify(queueDAO, never()).postpone(queueName, taskId, anyInt(), anyLong());
    }

    @Test
    public void execute_workflowHasTerminalStatus_queueIsRemoved() {
        String queueName = QueueUtils.getQueueName(task);
        String taskId = task.getTaskId();

        when(workflow.getStatus()).thenReturn(ExecutionStatus.TERMINATED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(queueName, taskId);
    }
}