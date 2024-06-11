package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class AsyncSystemTaskExecutorTest {

    @Test
    public void testExecute() {
        // Setup
        ExecutionDAOFacade executionDAOFacade = mock(ExecutionDAOFacade.class);
        QueueDAO queueDAO = mock(QueueDAO.class);
        MetadataDAO metadataDAO = mock(MetadataDAO.class);
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);

        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                new ConductorProperties(),
                workflowExecutor);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId");

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        // Test
        executor.execute(systemTask, taskModel.getTaskId());

        // Verify
        verify(executionDAOFacade).getTaskModel(taskModel.getTaskId());
    }

    @Test
    public void testLoadTaskQuietly() {
        // Setup
        ExecutionDAOFacade executionDAOFacade = mock(ExecutionDAOFacade.class);
        QueueDAO queueDAO = mock(QueueDAO.class);
        MetadataDAO metadataDAO = mock(MetadataDAO.class);
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);

        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                new ConductorProperties(),
                workflowExecutor);

        String taskId = "taskId";

        // Test
        TaskModel result = executor.loadTaskQuietly(taskId);

        // Verify
        assertNull(result);
    }

    @Test
    public void testPostponeQuietly() {
        // Setup
        ExecutionDAOFacade executionDAOFacade = mock(ExecutionDAOFacade.class);
        QueueDAO queueDAO = mock(QueueDAO.class);
        MetadataDAO metadataDAO = mock(MetadataDAO.class);
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);

        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                new ConductorProperties(),
                workflowExecutor);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId");

        when(queueDAO.postpone("testQueue", taskModel.getTaskId(), taskModel.getWorkflowPriority(), 60)).thenReturn(true);

        // Test
        executor.postponeQuietly("testQueue", taskModel);

        // Verify
        verify(queueDAO).postpone("testQueue", taskModel.getTaskId(), taskModel.getWorkflowPriority(), 60);
    }

    // Add more test cases for edge cases and exception handling scenarios

}