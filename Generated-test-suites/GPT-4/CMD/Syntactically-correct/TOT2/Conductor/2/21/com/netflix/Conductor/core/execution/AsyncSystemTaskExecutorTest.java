package com.netflix.conductor.core.execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    AsyncSystemTaskExecutor executor;

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

    @Mock
    WorkflowSystemTask systemTask;

    @Mock
    TaskModel task;

    private final String taskID = "task1";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldExecuteSuccessfully() {

        when(executor.loadTaskQuietly(taskID)).thenReturn(task);

        executor.execute(systemTask, taskID);

        verify(executionDAOFacade, atLeastOnce()).updateTask(task);

        verify(queueDAO, atLeastOnce()).remove(anyString(), eq(taskID));
    }

    @Test
    public void shouldHandleNullTask() {

        when(executor.loadTaskQuietly(taskID)).thenReturn(null);

        executor.execute(systemTask, taskID);

        verify(queueDAO, atLeastOnce()).remove(systemTask.getTaskType(), taskID);
    }
}