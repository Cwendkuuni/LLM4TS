
package com.netflix.conductor.core.execution;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;

import java.time.Duration;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

    @Mock
    ExecutionDAOFacade executionDAOFacade;
    @Mock
    QueueDAO queueDAO;
    @Mock
    MetadataDAO metadataDAO;
    @Mock
    ConductorProperties conductorProperties;
    @Mock
    WorkflowExecutor workflowExecutor;

    @InjectMocks
    AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
       when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(1));
       when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(1));
    }

    @Test
    public void testExecuteWhenTaskNotFound() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException());
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    //Other test cases based on the code logic inside the execute method;
    //such as when task status is terminal, is scheduled and if it exceeds in progress limit, 
    //exceeds rate limit, any exceptions thrown during execution, task updating, 
    //removing task from queue and workflow execution decision
    //should be written similarly as shown above for comprehensive test coverage.
}
