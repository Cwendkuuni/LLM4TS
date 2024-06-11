package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueueName;
import com.netflix.conductor.dao.WorkflowDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowDAO workflowDAO;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(executionDAOFacade.getExecutionDAO()).thenReturn(mock(ExecutionDAOFacade.class));
        when(queueDAO.getDAO()).thenReturn(mock(QueueDAO.class));
        when(metadataDAO.getDAO()).thenReturn(mock(MetadataDAO.class));
        when(workflowExecutor.decide(any())).thenReturn(Optional.empty());
        when(workflowExecutor.getWorkflowExecutor()).thenReturn(mock(WorkflowExecutor.class));
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(
                Optional.of(1000L));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(
                Optional.of(1000L));
    }

    @Test
    public void testExecute_nullTaskId() {
        asyncSystemTaskExecutor.execute(systemTask, null);
        verify(queueDAO).remove(any(), anyString());
    }

    @Test
    public void testExecute_taskNotFound() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO).remove(any(), anyString());
    }

    @Test
    public void testExecute_terminalTask() {
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO).remove(any(), anyString());
        verify(workflowExecutor).decide(anyString());
    }

    @Test
    public void testExecute_exceedsInProgressLimit() {
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO, never()).remove(any(), anyString());
        verify(queueDAO).postpone(any(), eq(task.getTaskId()), anyInt(), anyInt());
    }

    @Test
    public void testExecute_exceedsRateLimitPerFrequency() {
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO, never()).remove(any(), anyString());
        verify(queueDAO).postpone(any(), eq(task.getTaskId()), anyInt(), anyInt());
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO).remove(any(), anyString());
        verify(workflowExecutor).decide(anyString());
    }

    @Test
    public void testExecute_taskInProgress() {
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(systemTask).execute(any(), any(), any());
    }

    @Test
    public void testExecute_taskScheduled() {
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(systemTask).start(any(), any(), any());
    }

    @Test
    public void testPostponeQuietly_taskExists() {
        when(queueDAO.getDAO()).thenReturn(mock(QueueDAO.class));
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(true);

        asyncSystemTaskExecutor.postponeQuietly("queueName", task);
        verify(queueDAO).postpone(anyString(), eq(task.getTaskId()), anyInt(), anyInt());
    }

    @Test
    public void testPostponeQuietly_taskDoesNotExist() {
        when(queueDAO.getDAO()).thenReturn(mock(QueueDAO.class));
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.postponeQuietly("queueName", task);
        // Do nothing as it is a quiet method.
    }

    @Test
    public void testLoadTaskQuietly_taskExists() {
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("taskId");
        assertSame(task, result);
    }

    @Test
    public void testLoadTaskQuietly_taskDoesNotExist() {
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(null);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("taskId");
        assertNull(result);
    }
}