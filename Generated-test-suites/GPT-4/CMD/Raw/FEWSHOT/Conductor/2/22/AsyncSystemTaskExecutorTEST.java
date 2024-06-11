
package com.netflix.conductor.core.execution;

import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
    private WorkflowExecutor workflowExecutor;
    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(10));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(10));
    }

    @Test
    public void testExecute_whenTaskIsNull_shouldNotThrowError() {
        when(executionDAOFacade.getTaskModel("123")).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("SystemTask"), "123");
        verify(queueDAO, times(1)).remove(eq("SystemTask"), eq("123"));
    }

    @Test
    public void testExecute_whenTaskStatusIsTerminal_thenReturn() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        task.setTaskId("123");
        task.setTaskType("SystemTask");
        when(executionDAOFacade.getTaskModel("123")).thenReturn(task);
        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("SystemTask"), "123");
        verify(queueDAO, times(1)).remove(eq("SystemTask"), eq("123"));
    }

    // Add more test methods here. Be sure to test all conceivable scenarios to ensure the code performs as expected under all circumstances.
}
