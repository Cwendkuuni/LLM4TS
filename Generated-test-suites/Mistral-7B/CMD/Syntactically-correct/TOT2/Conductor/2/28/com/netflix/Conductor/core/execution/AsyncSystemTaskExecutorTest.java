package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private WorkflowSystemTask systemTask;

    private TaskModel task;

    private static final String TASK_ID = "taskId";
    private static final String QUEUE_NAME = QueueUtils.getQueueName(task);
    private static final String WORKFLOW_ID = "workflowId";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        task = new TaskModel();
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(new WorkflowModel());
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(null);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(null);
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(null);
    }

    @After
    public void tearDown() {
        // TODO: add assertions to verify the mocks here
    }

    @Test
    public void testExecute_taskExists_executesTaskAndUpdatesStatus() {
        // Given
        WorkflowModel workflow = new WorkflowModel();
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflow);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);

        // When
        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        // Then
        assertEquals(TaskModel.Status.IN_PROGRESS, task.getStatus());
        assertEquals(task, systemTask.getTask());
    }

    @Test
    public void testExecute_taskDoesNotExist_removesDeadMessageFromQueue() {
        // Given
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(null);

        // When
        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        // Then
        assertNull(task);
    }

    @Test(expected = Exception.class)
    public void testExecute_taskIsTerminal_throwsException() {
        // Given
        task.setStatus(TaskModel.Status.COMPLETED);

        // When
        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        // Then
        // An exception is expected to be thrown here
    }

    @Test
    public void testExecute_taskIsScheduled_postponesTask() {
        // Given
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any()))
                .thenReturn(false);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);

        // When
        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        // Then
        assertTrue(queueDAO.contains(QUEUE_NAME, TASK_ID));
    }

    @Test
    public void testExecute_taskIsInProgress_executesTask() {
        // Given
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any()))
                .thenReturn(false);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);

        // When
        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        // Then
        assertSame(task, systemTask.getTask());
    }

    @Test
    public void testExecute_taskHasCompleted_removesTaskFromQueue() {
        // Given
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any()))
                .thenReturn(false);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        // When
        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        // Then
        assertFalse(queueDAO.contains(QUEUE_NAME, TASK_ID));
    }

    @Test(expected = Exception.class)
    public void testExecute_queueDAOThrowsException() {
        // Given
        doThrow(new RuntimeException("queueDAOException")).when(queueDAO)
                .postpone(QUEUE_NAME, TASK_ID, anyInt(), anyInt());

        // When
        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        // Then
        // An exception is expected to be thrown here
    }

    @Test
    public void testExecute_executionDAOFacadeThrowsException_properlyHandlesException() {
        // Given
        doThrow(new RuntimeException("executionDAOFacadeException"))
                .when(executionDAOFacade).getWorkflowModel(WORKFLOW_ID, false);

        // When
        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        // Then
        assertFalse(task.getStatus().isTerminal());
        assertEquals(TaskModel.Status.FAILED, task.getStatus());
    }

    @Test
    public void testExecute_workflowExecutorDecideThrowsException() {
        // Given
        doThrow(new RuntimeException("workflowExecutorDecideException"))
                .when(workflowExecutor).decide(WORKFLOW_ID);

        // When
        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        // Then
        assertFalse(task.getStatus().isTerminal());
        assertEquals(TaskModel.Status.FAILED, task.getStatus());
    }

    @Test
    public void testPostponeQuietly_taskExists_postponesTask() {
        // Given
        when(queueDAO.contains(QUEUE_NAME, TASK_ID)).thenReturn(true);

        // When
        asyncSystemTaskExecutor.postponeQuietly(QUEUE_NAME, task);

        // Then
        assertFalse(queueDAO.isEmpty());
    }

    @Test
    public void testLoadTaskQuietly_taskExists() {
        // Given
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);

        // When
        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

        // Then
        assertSame(task, loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_taskDoesNotExist() {
        // Given
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(null);

        // When
        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

        // Then
        assertNull(loadedTask);
    }
}