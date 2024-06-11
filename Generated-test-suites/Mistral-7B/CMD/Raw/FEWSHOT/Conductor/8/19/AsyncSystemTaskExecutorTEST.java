package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueNameGenerator;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @Mock
    private QueueNameGenerator queueNameGenerator;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(
                new Duration(1000L, TimeUnit.MILLISECONDS));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(
                new Duration(1000L, TimeUnit.SECONDS));
        when(queueDAO.getWorkflowPriority(anyString(), anyString())).thenReturn(1L);
        when(queueNameGenerator.getQueueName(any(TaskModel.class))).thenReturn("queueName");
    }

    @Test
    public void testExecuteWithValidTask() {
        final TaskModel task = mock(TaskModel.class);
        final String taskId = "taskId";
        final WorkflowModel workflow = mock(WorkflowModel.class);
        final WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify the task status
        verify(task).setStatus(TaskModel.Status.IN_PROGRESS);

        // Verify the task is executed
        verify(systemTask).execute(workflow, task, workflowExecutor);

        // Verify the task is removed from the queue
        verify(queueDAO).remove("queueName", taskId);
    }

    @Test
    public void testExecuteWithTerminalTask() {
        final TaskModel task = mock(TaskModel.class);
        final String taskId = "taskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(null, taskId);

        // Verify the task is removed from the queue
        verify(queueDAO).remove("queueName", taskId);

        // Verify the workflow is evaluated
        verify(workflowExecutor).decide("workflowId");
    }

    @Test
    public void testExecuteWithPostponedTask() {
        final TaskModel task = mock(TaskModel.class);
        final String taskId = "taskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(null, taskId);

        // Verify the task is postponed in the queue
        verify(queueDAO).postpone("queueName", taskId, 1L, 1000L);
    }

    @Test
    public void testExecuteWithTerminalWorkflow() {
        final TaskModel task = mock(TaskModel.class);
        final String taskId = "taskId";
        final String workflowId = "workflowId";
        final WorkflowModel workflow = mock(WorkflowModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);

        when(workflow.getStatus()).thenReturn(Status.COMPLETED);

        asyncSystemTaskExecutor.execute(null, taskId);

        // Verify the task is removed from the queue
        verify(queueDAO).remove("queueName", taskId);

        // Verify the workflow is evaluated
        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testExecuteWithTerminalWorkflowAndCancelledTask() {
        final TaskModel task = mock(TaskModel.class);
        final String taskId = "taskId";
        final String workflowId = "workflowId";
        final WorkflowModel workflow = mock(WorkflowModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);

        when(workflow.getStatus()).thenReturn(Status.COMPLETED);

        task.setStatus(TaskModel.Status.CANCELED);

        asyncSystemTaskExecutor.execute(null, taskId);

        // Verify no action is taken on the queue
        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testExecuteWithExceededConcurrentLimit() {
        final TaskModel task = mock(TaskModel.class);
        final String taskId = "taskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(null, taskId);

        // Verify the task is postponed in the queue
        verify(queueDAO).postpone("queueName", taskId, 1L, 1000L);
    }

    @Test
    public void testExecuteWithExceededRateLimit() {
        final TaskModel task = mock(TaskModel.class);
        final String taskId = "taskId";
        final String taskDefName = "taskDefName";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(null, taskId);

        // Verify the task is postponed in the queue
        verify(queueDAO).postpone("queueName", taskId, 1L, 1000L);
    }

    @Test
    public void testExecuteWithNullTask() {
        final String taskId = "taskId";

        asyncSystemTaskExecutor.execute(null, taskId);

        // Verify no action is taken
        verifyNoInteractions(executionDAOFacade);
        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testExecuteWithNullTaskId() {
        final WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, null);

        // Verify no action is taken
        verifyNoInteractions(executionDAOFacade);
        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testExecuteWithNullSystemTask() {
        final String taskId = "taskId";

        asyncSystemTaskExecutor.execute(null, taskId);

        // Verify no action is taken
        verifyNoInteractions(executionDAOFacade);
        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testLoadTaskQuietlyWithNullId() {
        assertNull(asyncSystemTaskExecutor.loadTaskQuietly(null));
    }

    @Test
    public void testLoadTaskQuietlyWithValidId() {
        final TaskModel task = mock(TaskModel.class);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        assertSame(task, asyncSystemTaskExecutor.loadTaskQuietly("taskId"));
    }

    @Test
    public void testPostponeQuietlyWithNullQueueName() {
        final TaskModel task = mock(TaskModel.class);

        assertFalse(asyncSystemTaskExecutor.postponeQuietly(null, task));
    }

    @Test
    public void testPostponeQuietlyWithValidQueueName() {
        final TaskModel task = mock(TaskModel.class);

        asyncSystemTaskExecutor.postponeQuietly("queueName", task);

        // Verify the task is postponed in the queue
        verify(queueDAO).postpone("queueName", task.getTaskId(), 1L, 1000L);
    }
}