
package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel.Status;

import java.time.Duration;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
    private AsyncSystemTaskExecutor taskExecutor;

    @Before
    public void setUp() {
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(5));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(5));
    }

    @Test
    public void testExecuteWhenTaskModelIsNull() {
        String taskId = "testTaskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        taskExecutor.execute(systemTask, taskId);

        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(executionDAOFacade, never()).updateTask(any());
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testExecuteWhenTaskModelExistsButIsTerminal() {
        String taskId = "testTaskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getStatus()).thenReturn(Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        taskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), anyString());
        verify(executionDAOFacade).updateTask(any());
        verify(workflowExecutor, never()).decide(anyString());
    }

    // More test cases here for feasability study..
}
