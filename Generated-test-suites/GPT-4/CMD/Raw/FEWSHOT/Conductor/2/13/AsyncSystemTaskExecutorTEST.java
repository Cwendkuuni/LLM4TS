package com.netflix.conductor.core.execution;

import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {
    
    @Mock
    private ExecutionDAOFacade executionDAOFacade;
    
    @Mock
    private QueueDAO queueDAO;
    
    @Mock
    private MetadataDAO metadataDAO;
    
    @Mock
    private WorkflowSystemTask workflowSystemTask;
    
    @Mock
    private WorkflowExecutor workflowExecutor;
    
    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private TaskModel task;

    private AsyncSystemTaskExecutor executor;

    @Before
    public void setup() {
        executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        
        when(executionDAOFacade.getTaskModel(any(String.class))).thenReturn(task);
        when(workflowSystemTask.getTaskType()).thenReturn("TaskType");
        when(task.getTaskType()).thenReturn("TaskType");
        when(task.getTaskId()).thenReturn("TaskId");
    }

    @Test
    public void testExecuteTaskNotFound() {
        when(executionDAOFacade.getTaskModel("TaskId")).thenReturn(null);

        executor.execute(workflowSystemTask, "TaskId");

        verify(queueDAO).remove("TaskType", "TaskId");
    }

    @Test
    public void testExecuteTaskAlreadyCompleted() {
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        executor.execute(workflowSystemTask, "TaskId");

        verify(queueDAO).remove(any(String.class), eq("TaskId"));
    }

    @Test
    public void testExecuteTaskInProgressLimitExceeded() {
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        
        executor.execute(workflowSystemTask, "TaskId");

        verify(queueDAO).postpone(any(String.class), any(String.class), anyInt(), anyLong());
    }

    // More tests can be added to cover different scenarios"

}