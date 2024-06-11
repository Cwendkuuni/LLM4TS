package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.Queue;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private ConductorProperties conductorProperties;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private TaskModel task;
    private WorkflowModel workflow;
    private WorkflowSystemTask systemTask;
    private Queue queue;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskDefinition());
        when(queueDAO.get(anyString())).thenReturn(queue);
        when(workflowExecutor.decide(anyString())).thenReturn(true);

        task = new TaskModel();
        workflow = new WorkflowModel();
        systemTask = mock(WorkflowSystemTask.class);
        queue = mock(Queue.class);
    }

    /**
     * Tests if the execute method correctly handles a null taskId.
     */
    @Test
    public void testExecuteNullTaskId() {
        asyncSystemTaskExecutor.execute(systemTask, null);

        // No action should be taken if the taskId is null
        assertNull(queueDAO.getCallCount());
        assertNull(executionDAOFacade.getCallCount());
        assertNull(workflowExecutor.getCallCount());
        assertFalse(workflowExecutor.decideWasCalled());
    }

    /**
     * Tests if the execute method correctly handles a task that is already completed.
     */
    @Test
    public void testExecuteCompletedTask() {
        task.setStatus(ExecutionStatus.SUCCEEDED);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // The task should be removed from the queue if it is already completed
        assertTrue(queueDAO.removeWasCalled());
        assertEquals(1, queueDAO.getRemoveCallCount());
    }

    /**
     * Tests if the execute method correctly handles a task that is in the scheduled state.
     */
    @Test
    public void testExecuteScheduledTask() {
        // Set the task status to be scheduled
        task.setStatus(ExecutionStatus.SCHEDULED);

        // Set the workflow status to be running
        workflow.setStatus(ExecutionStatus.RUNNING);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // The task should be executed and the workflow should be evaluated after execution
        assertTrue(systemTask.isTaskRetrievalRequiredCalled());
        assertTrue(systemTask.startWasCalled());
        assertTrue(workflowExecutor.decideWasCalled());
    }

    /**
     * Tests if the execute method correctly handles a task that is in the in_progress state.
     */
    @Test
    public void testExecuteInProgressTask() {
        // Set the task status to be in_progress
        task.setStatus(ExecutionStatus.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // The task should be executed
        assertFalse(queueDAO.removeWasCalled());
        assertTrue(systemTask.executeWasCalled());
    }

    /**
     * Tests if the execute method correctly handles a task that has an exceeded rate limit.
     */
    @Test
    public void testExecuteRateLimitedTask() {
        // Set the task rate limit to be 1 per minute
        task.setRateLimitPerFrequency(Duration.ofMinutes(1));

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // The task should be postponed if the rate limit is exceeded
        assertTrue(queueDAO.postponeWasCalled());
    }

    /**
     * Tests if the execute method correctly handles a task that has an exceeded in_progress limit.
     */
    @Test
    public void testExecuteConcurrentLimitTask() {
        // Set the task in_progress limit to be 1
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // The task should be postponed if the in_progress limit is exceeded
        assertTrue(queueDAO.postponeWasCalled());
    }

    /**
     * Tests if the execute method correctly handles a queue message that is already removed.
     */
    @Test
    public void testExecuteRemovedQueueMessage() {
        queue.setTaskId(task.getTaskId());
        when(queueDAO.remove(anyString(), task.getTaskId())).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // The method should not throw an exception if the queue message is already removed
        assertFalse(queueDAO.removeWasCalledAfterThrowingException());
    }

    /**
     * Tests if the execute method correctly handles a null task.
     */
    @Test(expected = NullPointerException.class)
    public void testExecuteNullTask() {
        asyncSystemTaskExecutor.execute(systemTask, null);
    }

    /**
     * Tests if the execute method correctly handles a null systemTask.
     */
    @Test(expected = NullPointerException.class)
    public void testExecuteNullSystemTask() {
        asyncSystemTaskExecutor.execute(null, task.getTaskId());
    }

    private static class TaskDefinition {}
}