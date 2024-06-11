package com.netflix.conductor.core.execution;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private WorkflowExecutor workflowExecutor;
    private MetadataDAO metadataDAO;
    private WorkflowSystemTask workflowSystemTask;
    private ConductorProperties conductorProperties;
    
    @Before
    public void setUp() {
      executionDAOFacade = Mockito.mock(ExecutionDAOFacade.class);
      queueDAO = Mockito.mock(QueueDAO.class);
      workflowExecutor = Mockito.mock(WorkflowExecutor.class);
      metadataDAO = Mockito.mock(MetadataDAO.class);
      workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
      conductorProperties = Mockito.mock(ConductorProperties.class);
      asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void executeTask() {
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        Mockito.when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(false);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO, times(1)).postpone(any(), any(), any(), any());
    }

    @Test
    public void executeTaskException() {
        String taskId = "123";
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO, times(1)).remove(any(), any());
    }

    @Test
    public void executeTaskWithNull() {
        String taskId = "123";
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO, times(1)).remove(any(), any());
    }

    @Test(expected = RuntimeException.class)
    public void executeTaskWithException() {
        String taskId = "123";
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
    }
}