package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
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
        conductorProperties = new ConductorProperties();
        workflowExecutor = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute_TaskNotFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId1234");

        verify(LOGGER).error("TaskId: taskId1234 could not be found while executing {}", any(WorkflowSystemTask.class));
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId1234");

        verify(LOGGER).info("Task {}/{} was already completed.", anyString(), anyString());
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_PostponeTask() {
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId1234");

        verify(LOGGER).warn("Concurrent Execution limited for {}:{}", anyString(), anyString());
        verify(queueDAO).postpone(anyString(), anyString(), anyLong());
    }

    // Add more test cases for the execute method based on different scenarios

    @Test
    public void testPostponeQuietly() {
        TaskModel task = new TaskModel();
        String queueName = "testQueue";
        when(queueDAO.postpone(anyString(), anyString(), anyLong(), anyLong())).thenReturn(true);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(anyString(), anyString(), anyLong(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly_NotFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly("taskId1234");

        assertNull(task);
    }

    @Test
    public void testLoadTaskQuietly_Found() {
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly("taskId1234");

        assertNotNull(loadedTask);
        assertEquals(task, loadedTask);
    }
}