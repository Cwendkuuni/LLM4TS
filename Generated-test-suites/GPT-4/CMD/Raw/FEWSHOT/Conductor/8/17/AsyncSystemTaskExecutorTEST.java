package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

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
    private WorkflowSystemTask systemTask;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    // Please adjust to "systemTask worker callback duration" from your settings.
    private final long systemTaskCallbackTime = 3000L;

    // Please adjust to "task execution postpone duration" from your settings.
    private final long queueTaskMessagePostponeSecs = 3600L;

    @Test
    public void testExecute_successfulExecution() {
        AsyncSystemTaskExecutor asyncExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                mock(ConductorProperties.class),
                workflowExecutor
        );

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        asyncExecutor.execute(systemTask, "task1");

        verify(executionDAOFacade, Mockito.times(1)).getTaskModel("task1");
        verify(task, never()).setStatus(TaskModel.Status.CANCELED);
        verify(executionDAOFacade, Mockito.times(1)).updateTask(task);
    }

    @Test
    public void testExecute_nullTask() {
        AsyncSystemTaskExecutor asyncExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                mock(ConductorProperties.class),
                workflowExecutor
        );

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
        asyncExecutor.execute(systemTask, "task1");

        verify(executionDAOFacade, Mockito.times(1)).getTaskModel("task1");
        verify(task, never()).setStatus(TaskModel.Status.CANCELED);
        verify(executionDAOFacade, never()).updateTask(task);
    }
}