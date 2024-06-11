package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

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
    private ConductorProperties conductorProperties;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }


    @Test
    public void testExecuteWithNullTask() {
        String taskId = "sampleTaskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
    }

    @Test
    public void testExecuteWithValidTask() {
        String taskId = "sampleTaskId";
        TaskModel taskModel = new TaskModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).remove(anyString(), eq(taskId));
    }
    
    @Test
    public void testPostponeQuietly() {
        String queueName = "sampleQueueName";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("sampleTaskId");

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
    }
    
    @Test
    public void testLoadTaskQuietlyWithException() {
        String taskId = "sampleTaskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        assertNull(result);
    }
    
    @Test
    public void testLoadTaskQuietlyWithValidTask() {
        String taskId = "sampleTaskId";
        TaskModel expectedTask = new TaskModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(expectedTask);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        assertEquals(expectedTask, result);
    }
}
