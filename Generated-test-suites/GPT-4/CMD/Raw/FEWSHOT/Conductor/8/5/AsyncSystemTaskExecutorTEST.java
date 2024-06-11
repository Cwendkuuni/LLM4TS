package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowExecutor workflowExecutor;
    private ConductorProperties conductorProperties;

    @Before
    public void setUp() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        conductorProperties = mock(ConductorProperties.class);
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(10));
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(10));
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "test-id";
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(any(), anyBoolean())).thenReturn(workflowModel);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "test-id";
        TaskModel taskModel = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        TaskModel returnedTaskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertEquals(taskModel, returnedTaskModel);
    }

    @Test
    public void testLoadTaskQuietlyWithException() {
        String taskId = "test-id";
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());
        TaskModel returnedTaskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertNull(returnedTaskModel);
    }
}