package com.netflix.conductor.core.execution;
import org.junit.Before;
import org.junit.Test;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.MetadataDAO;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowSystemTask systemTask;
    private WorkflowExecutor workflowExecutor;
    private ConductorProperties conductorProperties;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() throws Exception {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        systemTask = mock(WorkflowSystemTask.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        conductorProperties = mock(ConductorProperties.class);
        asyncSystemTaskExecutor = 
                new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    // Test the method execute with the scenario when taskModel is null
    @Test
    public void testExecuteWhenTaskIsNull() throws Exception {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, "task_id");
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(log, times(1)).error(anyString(), any(), any());
    }

    //Capture a scenario where asyncSystemTask throws an exception
    @Test(expected = Exception.class)
    public void testExecuteWhenSystemTaskThrowsException() throws Exception {
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new Exception());
        asyncSystemTaskExecutor.execute(systemTask, "task_id");
        verify(log, times(1)).error(anyString(), any(), any());
    }

    // Test the  method loadTaskQuietly when getTaskModel throws an exception
    @Test
    public void testLoadTaskQuietlyWhenGetTaskModelThrowsException() throws Exception {
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new Exception());
        assertNull(asyncSystemTaskExecutor.loadTaskQuietly("task_id"));
    }

    //Note: Similar method can be created for rest of the methods to test typical use cases, edge cases and error scenarios

}