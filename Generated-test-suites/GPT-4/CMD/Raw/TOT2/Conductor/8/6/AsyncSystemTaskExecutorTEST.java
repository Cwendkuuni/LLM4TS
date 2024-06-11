package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AsyncSystemTaskExecutorTest {
    
    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;
    
    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void execute_returnWhenTaskIsNull() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    // Do similar testing for each of the scenarios, i.e task is terminal, exceedsInProgressLimit, exceedsRateLimitPerFrequency, etc.
    // The goal is to simulate different state of task (null, terminal, scheduled, in_progress) and check if appropriate actions are taken (log, remove task from queue, task execution postponed) and if correct methods are called on mocks.
    // You will also want to test whether correct methods are called in case of exceptions occurring during "execute" method execution.
}