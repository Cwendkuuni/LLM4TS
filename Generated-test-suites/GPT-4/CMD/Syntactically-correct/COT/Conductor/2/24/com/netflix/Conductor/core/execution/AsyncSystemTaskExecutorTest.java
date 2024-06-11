package com.netflix.conductor.core.execution;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private WorkflowSystemTask systemTask;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NullPointerException.class) 
    public void executeMethodNullWorkflowSystemTaskTest() {
        String taskId = "task_abc123";
        asyncSystemTaskExecutor.execute(null, taskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeMethodEmptyTaskIdTest() {
        asyncSystemTaskExecutor.execute(systemTask, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeMethodNullTaskIdTest() {
        asyncSystemTaskExecutor.execute(systemTask, null);
    }

    @Test
    public void executeMethodExceptionTest() {
        String taskId = "task_abc123";
        doThrow(RuntimeException.class).when(asyncSystemTaskExecutor).execute(systemTask, taskId);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test
    public void executeMethodWorkflowTaskStatusTerminalTest() {

        // Some code to simulate terminal status for workflow task.

    }
}