package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Duration;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private WorkflowSystemTask workflowSystemTask;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowSystemTask = mock(WorkflowSystemTask.class);

        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(10));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(10));

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecuteValidTask() {
        TaskModel task = mock(TaskModel.class);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(any())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(any(), anyBoolean())).thenReturn(mock(WorkflowModel.class));

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(workflowSystemTask, times(1)).start(any(), any(), any());
    }

    @Test
    public void testExecuteMissingTask() {
        when(executionDAOFacade.getTaskModel(any())).thenReturn(null);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");
        verify(workflowSystemTask, times(0)).start(any(), any(), any());
    }

    @Test
    public void testExecuteExceedsInProgressLimit() {
        TaskModel task = mock(TaskModel.class);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(any())).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(workflowSystemTask, times(0)).start(any(), any(), any());
    }

    @Test
    public void testExecuteExceedsRateLimit() {
        TaskModel task = mock(TaskModel.class);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(any())).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(task.getRateLimitPerFrequency()).thenReturn(1);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(workflowSystemTask, times(0)).start(any(), any(), any());
    }
}