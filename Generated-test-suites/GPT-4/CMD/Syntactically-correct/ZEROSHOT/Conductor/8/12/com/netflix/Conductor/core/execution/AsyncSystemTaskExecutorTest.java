
package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import java.time.Duration;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    private final ExecutionDAOFacade executionDAOFacade = mock(ExecutionDAOFacade.class);
    private final QueueDAO queueDAO = mock(QueueDAO.class);
    private final MetadataDAO metadataDAO = mock(MetadataDAO.class);
    private final ConductorProperties conductorProperties = mock(ConductorProperties.class);
    private final WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(1));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(1));

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute_Success() {
        // Sets the task and workflow mocks
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);

        // Mocks the behavior of external calls in the execute method
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(task.getWorkflowInstanceId()).thenReturn(String.valueOf(1));
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.RUNNING);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        // Executes the test
        asyncSystemTaskExecutor.execute(systemTask, "1");

        // Verifies the correct interactions
        verify(task, times(1)).incrementPollCount();
        verify(task, times(2)).setStatus(TaskModel.Status.COMPLETED);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(task);
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    // More tests could be added for other cases such as when the task or workflow cannot be fetched, the task is already in terminal
    // state, state of task is in progress, queue task message is postponed.
}
