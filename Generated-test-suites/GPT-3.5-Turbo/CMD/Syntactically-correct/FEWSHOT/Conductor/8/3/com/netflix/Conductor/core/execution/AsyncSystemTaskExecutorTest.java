/*
 * Test cases for AsyncSystemTaskExecutor class methods.
 */
package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;

public class AsyncSystemTaskExecutorTest {

    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowExecutor workflowExecutor;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                new ConductorProperties(),
                workflowExecutor
        );
    }

    @Test
    public void testExecute_withValidTask() {
        TaskModel mockTask = mock(TaskModel.class);
        WorkflowSystemTask mockSystemTask = mock(WorkflowSystemTask.class);
        String taskId = "testTaskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(mockTask);

        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(mockSystemTask).start(mockWorkflow, mockTask, workflowExecutor);
        verify(executionDAOFacade).updateTask(mockTask);
        verify(workflowExecutor).decide(mockTask.getWorkflowInstanceId());
    }

    @Test
    public void testExecute_withNullTask() {
        String taskId = "nonExistentTaskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        assertNull(executionDAOFacade.getTaskModel(taskId));
    }

    @Test
    public void testLoadTaskQuietly_withExistingTask() {
        String taskId = "existingTaskId";
        TaskModel mockTask = mock(TaskModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(mockTask);
        
        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        assertNotNull(loadedTask);
        assertEquals(mockTask, loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_withNonExistingTask() {
        String taskId = "nonExistentTaskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        
        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        assertNull(loadedTask);
    }
}