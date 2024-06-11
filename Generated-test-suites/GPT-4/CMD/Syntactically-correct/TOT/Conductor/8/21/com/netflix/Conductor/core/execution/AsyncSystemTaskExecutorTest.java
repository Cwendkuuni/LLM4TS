package com.netflix.conductor.core.execution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
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

    @Mock
    private WorkflowSystemTask systemTask;
    
    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(), workflowExecutor);
    }

    // Test scenario for typical execution of execute method
    @Test
    public void testExecute() {
        executeTest("TaskID", "QueueName");
    }

    @Test(expected = Exception.class)
    public void testPostExecuteFail() {
        when(queueDAO.postpone(any(), any(), any(), anyLong())).thenThrow(new RuntimeException());
    }

    @Test
    public void testLoadTaskQuietlyWithException() {
        when(executionDAOFacade.getTaskModel(any())).thenThrow(new RuntimeException());
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("");
        assertNull(result);
    }

    private void executeTest(String taskId, String queueName) {
        doReturn(task).when(executionDAOFacade).getTaskModel(taskId);

        when(queueDAO.remove(any(), any())).thenReturn(true);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, atLeastOnce()).postpone(any(), any(), any(), anyLong());
        verify(executionDAOFacade, atLeastOnce()).getTaskModel(taskId);
        verify(queueDAO, atLeastOnce()).remove(any(), any());
        verify(systemTask, atLeastOnce()).isAsyncComplete(task);
    }
}