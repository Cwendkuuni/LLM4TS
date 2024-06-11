package com.netflix.conductor.core.execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;

public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor executor;

    @Mock
    private WorkflowSystemTask sysTask; 

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void executeWhenTaskIsNotTerminal() {
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        executor.execute(sysTask, "taskId");

        verify(sysTask).start(workflow, task, executor.workflowExecutor);
    }

    @Test
    public void executeWhenTaskIsTerminal() {
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        executor.execute(sysTask, "taskId");

        verifyNoMoreInteractions(sysTask);
    }

    // More edge case tests and exception scenarios tests would be needed here
}