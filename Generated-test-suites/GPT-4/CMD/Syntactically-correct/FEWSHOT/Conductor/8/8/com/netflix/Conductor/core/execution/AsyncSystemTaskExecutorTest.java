package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.model.TaskModel;
import org.junit.Before;
import org.junit.Test;

public class AsyncSystemTaskExecutorTest {

    private ExecutionDAOFacade executionDAOFacade;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowSystemTask workflowSystemTask;
    private AsyncSystemTaskExecutor systemTaskExecutor;

    @Before
    public void setUp() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowSystemTask = mock(WorkflowSystemTask.class);
        systemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, metadataDAO, conductorProperties, workflowSystemTask);
    }

    @Test
    public void testExecute() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskType("test");
        taskModel.setTaskId("task_id");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(false);
        systemTaskExecutor.execute(workflowSystemTask, "task_id");
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
    }

    @Test
    public void testPostponeQuietly() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("task_id");
        systemTaskExecutor.postponeQuietly("test_queue", taskModel);
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("task_id");
        when(executionDAOFacade.getTaskModel("task_id")).thenReturn(taskModel);
        TaskModel response = systemTaskExecutor.loadTaskQuietly("task_id");
        assertEquals(taskModel, response);
    }
}