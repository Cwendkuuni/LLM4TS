package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.metadata.MetadataDAOImpl;
import com.netflix.conductor.dao.ExecutionDAOFacadeImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModelImpl;
import com.netflix.conductor.utils.QueueUtils;
import com.netflix.conductor.workflow.WorkflowExecutor;

public class AsyncSystemTaskExecutorTest {

    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private Monitors monitors;
    private WorkflowExecutor workflowExecutor;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private String queueName;
    private TaskModel task;
    private WorkflowModel workflowModel;
    private WorkflowSystemTask workflowSystemTask;

    @Before
    public void setUp() {
        ConductorProperties conductorProperties = Mockito.mock(ConductorProperties.class);
        executionDAOFacade = new ExecutionDAOFacadeImpl();
        queueDAO = new QueueDAOImpl();
        metadataDAO = new MetadataDAOImpl();
        monitors = Mockito.mock(Monitors.class);
        workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
        queueName = "testQueue";
        task = new TaskModel();
        task.setTaskId("taskId");
        task.setTaskType("taskType");
        task.setStatus(TaskModel.Status.SCHEDULED);
        workflowModel = new WorkflowModelImpl();
        workflowModel.setId("workflowId");
        workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
    }

    @After
    public void tearDown() {
        reset(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor, monitors);
    }

    @Test
    public void testExecute_TaskNotFound() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(Optional.empty());

        asyncSystemTaskExecutor.execute(workflowSystemTask, task.getTaskId());

        verify(queueDAO, never()).remove(queueName, task.getTaskId());
        verify(executionDAOFacade, never()).updateTask(task);
        verify(workflowExecutor, never()).decide(workflowModel.getId());
        assertEquals(TaskModel.Status.SCHEDULED, task.getStatus());
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        task.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(Optional.of(task));

        asyncSystemTaskExecutor.execute(workflowSystemTask, task.getTaskId());

        verify(queueDAO, never()).remove(queueName, task.getTaskId());
        verify(executionDAOFacade, never()).updateTask(task);
        verify(workflowExecutor, never()).decide(workflowModel.getId());
        assertEquals(TaskModel.Status.COMPLETED, task.getStatus());
    }

    @Test
    public void testExecute_TaskInProgress() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(Optional.of(task));

        asyncSystemTaskExecutor.execute(workflowSystemTask, task.getTaskId());

        verify(queueDAO).remove(queueName, task.getTaskId());
        verify(executionDAOFacade).updateTask(task);
        verify(workflowExecutor).decide(workflowModel.getId());
        assertEquals(TaskModel.Status.IN_PROGRESS, task.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_ExceptionThrown() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenThrow(new RuntimeException("Test exception"));

        asyncSystemTaskExecutor.execute(workflowSystemTask, task.getTaskId());

        verify(queueDAO, never()).remove(queueName, task.getTaskId());
        verify(executionDAOFacade, never()).updateTask(task);
        verify(workflowExecutor, never()).decide(workflowModel.getId());
        assertEquals(TaskModel.Status.SCHEDULED, task.getStatus());
    }

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(
                queueName,
                task.getTaskId(),
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_TaskFound() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(Optional.of(task));

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(task.getTaskId());

        verify(executionDAOFacade).getTaskModel(task.getTaskId());
        assertEquals(task, loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_TaskNotFound() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(Optional.empty());

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(task.getTaskId());

        verify(executionDAOFacade).getTaskModel(task.getTaskId());
        assertNull(loadedTask);
    }
}