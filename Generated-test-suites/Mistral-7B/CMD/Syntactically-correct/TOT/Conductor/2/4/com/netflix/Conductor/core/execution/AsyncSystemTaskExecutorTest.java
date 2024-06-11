package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.time.Duration;
import java.util.Optional;

/**
 * Test class for {@link AsyncSystemTaskExecutor}.
 */
public class AsyncSystemTaskExecutorTest {

    private static final String WORKFLOW_ID = "workflow-123";
    private static final String TASK_TYPE = "task-type";
    private static final String TASK_ID = "task-id-123";
    private static final String QUEUE_NAME = "queue-name";

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

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    @Before
    public void setUp() {
        when(executionDAOFacade.getWorkflowModel(
                WORKFLOW_ID, systemTask::isTaskRetrievalRequired))
            .thenReturn(workflow);
        when(workflow.getWorkflowDefinitionName()).thenReturn("workflow-definition");
        when(workflowExecutor.decide(WORKFLOW_ID)).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                return null;
            }
        });
    }

    @Test
    public void testExecute_taskNotFound() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        // The task was not found, it should have been removed from the queue
        verifyQueueRemove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        // The task was already completed, it should have been removed from the queue
        verifyQueueRemove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_taskInProgress() {
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        // The task was in progress, it should have been executed and the workflow decided
        verifyExecution(QUEUE_NAME, TASK_ID, WorkflowModel.Status.RUNNING);
    }

    @Test
    public void testExecute_taskScheduled() {
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        // The task was scheduled, it should have been executed and the workflow decided
        verifyExecution(QUEUE_NAME, TASK_ID, WorkflowModel.Status.RUNNING);
    }

    @Test
    public void testExecute_taskTerminal() {
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        // The workflow was completed, the task should have been removed from the queue
        verifyQueueRemove(QUEUE_NAME, TASK_ID);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_exceedsInProgressLimit() {
        when(executionDAOFacade.getTaskModelCountByStatus(TaskModel.Status.IN_PROGRESS, TASK_TYPE))
                .thenReturn(10);

        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        // The concurrent execution limit was exceeded, an exception should have been thrown
        verify(executionDAOFacade).exceedsInProgressLimit(task);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_exceedsRateLimitPerFrequency() {
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(mock(TaskDefinition.class));
        when(task.getRateLimitPerFrequency()).thenReturn(5);

        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        // The rate limit per frequency was exceeded, an exception should have been thrown
        verify(executionDAOFacade).exceedsRateLimitPerFrequency(task, mock(TaskDefinition.class));
    }

    private void verifyQueueRemove(String queueName, String taskId) {
        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        when(queueDAO.getTaskCountInQueue(queueName)).thenReturn(1);
        when(queueDAO.remove(queueName, taskId)).thenReturn(true);

        verify(queueDAO).remove(queueName, taskId);
        verify(queueDAO).getTaskCountInQueue(queueName);
    }

    private void verifyExecution(String queueName, String taskId, WorkflowModel.Status workflowStatus) {
        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenAnswer(invocation -> {
            workflow.setStatus(workflowStatus);
            return null;
        });

        verify(executionDAOFacade).updateTask(task);
        verify(workflowExecutor).decide(WORKFLOW_ID);
    }
}

class TaskDefinition {
    int rateLimitPerFrequency;
}