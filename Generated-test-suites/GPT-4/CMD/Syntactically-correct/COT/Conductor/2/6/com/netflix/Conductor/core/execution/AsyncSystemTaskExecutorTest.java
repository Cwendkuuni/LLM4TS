package com.netflix.conductor.core.execution;

import org.junit.Test;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class AsyncSystemTaskExecutorTest {

    @Test
    public void testConstructor() {
        ExecutionDAOFacade executionDAOFacade = mock(ExecutionDAOFacade.class);
        QueueDAO queueDAO = mock(QueueDAO.class);
        MetadataDAO metadataDAO = mock(MetadataDAO.class);
        ConductorProperties conductorProperties = mock(ConductorProperties.class);
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);

        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, 
           queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        assertNotNull(asyncSystemTaskExecutor);
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "random-task-id";
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = mock(AsyncSystemTaskExecutor.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertEquals("Expect task execution", taskId, asyncSystemTaskExecutor.execute(systemTask, taskId));
    }

    @Test(expected = NullPointerException.class)
    public void testExecuteWithNullSystemTask() {
        String taskId = "random-task-id";
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = mock(AsyncSystemTaskExecutor.class);

        asyncSystemTaskExecutor.execute(null, taskId);
        assertEquals("Expect exception", taskId, asyncSystemTaskExecutor.execute(null, taskId));
    }   

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteWithEmptyTaskId() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = mock(AsyncSystemTaskExecutor.class);

        asyncSystemTaskExecutor.execute(systemTask, "");
        assertEquals("Expect exception", "", asyncSystemTaskExecutor.execute(systemTask, ""));
    }
}
