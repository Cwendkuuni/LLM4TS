
package com.netflix.conductor.core.execution.tests;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;

    @Before
    public void setUp() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
            executionDAOFacade, queueDAO, metadataDAO,
            conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute_AsyncSystemTask_InvalidTaskId() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";

        //Simulate a task that does not exist
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(any(String.class), eq(taskId));
    }

    @Test
    public void testExecute_AsyncSystemTask_ValidTaskId_TaskStatusTerminal() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        task.setTaskId(taskId);
        
        //Mock getTaskModel method
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(any(String.class), eq(taskId));
    }

    //Create several other tests covering different scenarios in the execute method.
}
