package com.netflix.conductor.core.execution;

import static com.netflix.conductor.core.execution.AsyncSystemTaskExecutor.LOGGER;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAOImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AsyncSystemTaskExecutorTest {

    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private AsyncSystemTaskExecutor systemUnderTest;
    private WorkflowSystemTask systemTask;
    private TaskModel task;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = new QueueDAOImpl();
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);

        systemUnderTest = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);

        systemTask = mock(WorkflowSystemTask.class);
        task = mock(TaskModel.class);
    }

    @Test
    public void testExecute_taskNotFound() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(null);

        systemUnderTest.execute(systemTask, task.getTaskId());

        verify(queueDAO).remove(eq("queueName"), eq(task.getTaskId()));
        verifyNoInteractions(executionDAOFacade, systemTask, workflowExecutor);
        verify(LOGGER, times(1)).error("TaskId: {} could not be found while executing {}", task.getTaskId(), systemTask);
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        systemUnderTest.execute(systemTask, task.getTaskId());

        verify(queueDAO, times(1)).remove(eq("queueName"), eq(task.getTaskId()));
        verifyNoInteractions(executionDAOFacade, systemTask, workflowExecutor);
    }

    @Test
    public void testExecute_taskTerminalStatus() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.FAILED);

        systemUnderTest.execute(systemTask, task.getTaskId());

        verify(queueDAO, times(1)).remove(eq("queueName"), eq(task.getTaskId()));
        verifyNoInteractions(executionDAOFacade, systemTask, workflowExecutor);
    }

    @Test
    public void testExecute_exceedsInProgressLimit() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        systemUnderTest.execute(systemTask, task.getTaskId());

        verify(queueDAO).postpone(eq("queueName"), eq(task.getTaskId()), anyInt(), anyInt());
        verifyNoInteractions(executionDAOFacade, systemTask, workflowExecutor);
    }

    @Test
    public void testExecute_exceedsRateLimitPerFrequency() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(task.getRateLimitPerFrequency()).thenReturn(1);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new WorkflowModel());
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, eq(new WorkflowModel()))).thenReturn(true);

        systemUnderTest.execute(systemTask, task.getTaskId());

        verify(queueDAO).postpone(eq("queueName"), eq(task.getTaskId()), anyInt(), anyInt());
        verifyNoInteractions(executionDAOFacade, systemTask, workflowExecutor);
    }

    @Test
    public void testExecute_taskInProgress() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        systemUnderTest.execute(systemTask, task.getTaskId());

        verify(systemTask).execute(any(), any(), any());
        verify(workflowExecutor).decide(eq(task.getWorkflowInstanceId()));
    }
}