package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

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
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueueAction;
import com.netflix.conductor.metrics.Monitors;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private WorkflowModel workflowModel;
    private TaskModel taskModel;
    private String queueName;
    private String taskId;
    private WorkflowSystemTask systemTask;

    @Before
    public void setUp() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade,
                        queueDAO,
                        metadataDAO,
                        conductorProperties,
                        workflowExecutor);

        workflowModel = mock(WorkflowModel.class);
        taskModel = mock(TaskModel.class);
        systemTask = mock(WorkflowSystemTask.class);
        queueName = QueueUtils.getQueueName(taskModel);
        taskId = "testTaskId";

        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(metadataDAO.getTaskDef(taskModel.getTaskDefName())).thenReturn(mock(TaskDefinition.class));
        when(queueDAO.postpone(queueName, taskId, 0L, 0L)).thenReturn(true);
        when(queueDAO.remove(queueName, taskId)).thenReturn(true);
    }

    @Test
    public void testExecute_taskNotFound() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test
    public void testExecute_taskInProgress_noTaskRetrievalRequired() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, times(1)).execute(workflowModel, taskModel, workflowExecutor);
        verify(queueDAO, times(1)).postpone(queueName, taskId, 0L, systemTaskCallbackTime);
    }

    @Test
    public void testExecute_taskInProgress_taskRetrievalRequired() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, times(1)).execute(workflowModel, taskModel, workflowExecutor);
        verify(queueDAO, times(1)).postpone(queueName, taskId, 0L, systemTaskCallbackTime);
    }

    @Test
    public void testExecute_taskScheduled() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_taskTerminal() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_taskFailed() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.FAILED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_exceedsInProgressLimit() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postponeQuietly(queueName, taskModel);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_exceedsRateLimitPerFrequency() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(taskModel.getRateLimitPerFrequency()).thenReturn(1L);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, null)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postponeQuietly(queueName, taskModel);
    }

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAO, times(1)).postpone(queueName, taskId, 0L, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_null() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(result);
    }

    @Test
    public void testLoadTaskQuietly_valid() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(taskModel, result);
    }

    @Test
    public void testDecide_workflowTerminal() {
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);

        asyncSystemTaskExecutor.decide(workflowId);

        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testDecide_workflowInProgress() {
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.decide(workflowId);

        verify(workflowExecutor, never()).decide(workflowId);
    }
}