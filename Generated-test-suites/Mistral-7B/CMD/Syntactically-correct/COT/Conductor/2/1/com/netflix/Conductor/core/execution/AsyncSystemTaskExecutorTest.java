package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;

@SuppressWarnings("unchecked")
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

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(task.getTaskDefName()).thenReturn("testTaskDefName");
        when(task.getWorkflowInstanceId()).thenReturn("testWorkflowId");
        when(workflow.getStatus()).thenReturn(Status.RUNNING);
    }

    @Test
    public void testExecute_taskIsNull() {
        when(executionDAOFacade.getTaskModel("nonExistentTaskId")).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, "nonExistentTaskId");
        verify(queueDAO, times(1)).remove("queueName", "nonExistentTaskId");
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        when(executionDAOFacade.getTaskModel("completedTaskId")).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, "completedTaskId");
        verify(queueDAO, times(1)).remove("queueName", "completedTaskId");
    }

    @Test
    public void testExecute_taskQueued() {
        when(executionDAOFacade.getTaskModel("queuedTaskId")).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.QUEUED);
        asyncSystemTaskExecutor.execute(systemTask, "queuedTaskId");
        verify(queueDAO, times(1)).postpone("queueName", "queuedTaskId", 1, 0L);
    }

    @Test(expected = Exception.class)
    public void testExecute_taskInProgress_executionException() {
        when(executionDAOFacade.getTaskModel("inProgressTaskId")).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        doThrow(new RuntimeException("Test Exception")).when(systemTask).execute(workflow, task, workflowExecutor);
        asyncSystemTaskExecutor.execute(systemTask, "inProgressTaskId");
        verify(queueDAO, times(1)).remove("queueName", "inProgressTaskId");
    }

    @Test
    public void testExecute_taskInProgress_noException() {
        when(executionDAOFacade.getTaskModel("inProgressTaskId")).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(workflowExecutor.decide("testWorkflowId")).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, "inProgressTaskId");
        verify(queueDAO, times(1)).remove("queueName", "inProgressTaskId");
        verify(workflowExecutor, times(1)).decide("testWorkflowId");
    }

    @Test
    public void testExecute_taskTerminated() {
        when(executionDAOFacade.getTaskModel("terminatedTaskId")).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINATED);
        asyncSystemTaskExecutor.execute(systemTask, "terminatedTaskId");
        verify(queueDAO, times(1)).remove("queueName", "terminatedTaskId");
    }

    @Test
    public void testExecute_workflowCompleted() {
        when(executionDAOFacade.getTaskModel("workflowCompletedTaskId")).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(workflow.getStatus()).thenReturn(Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, "workflowCompletedTaskId");
        verify(queueDAO, times(1)).remove("queueName", "workflowCompletedTaskId");
        verify(workflowExecutor, times(1)).decide("testWorkflowId");
    }

    @Test
    public void testExecute_taskExceedsInProgressLimit() {
        when(executionDAOFacade.getTaskModel("taskWithLimitExceededId")).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, "taskWithLimitExceededId");
        verify(queueDAO, times(1)).postpone("queueName", "taskWithLimitExceededId", 1, 0L);
    }

    @Test
    public void testExecute_taskExceedsRateLimitPerFrequency() {
        when(executionDAOFacade.getTaskModel("taskWithRateLimitExceededId")).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any(MetadataDAO.class)))
                .thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, "taskWithRateLimitExceededId");
        verify(queueDAO, times(1)).postpone("queueName", "taskWithRateLimitExceededId", 1, 0L);
    }

    @Test(expected = Exception.class)
    public void testExecute_taskWithEmptyWorkflow() {
        when(executionDAOFacade.getTaskModel("taskWithEmptyWorkflowId")).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getWorkflowModel("testWorkflowId", true)).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, "taskWithEmptyWorkflowId");
        verify(queueDAO, times(1)).remove("queueName", "taskWithEmptyWorkflowId");
    }

    @Test
    public void testExecute_taskWithRunningWorkflow() {
        when(executionDAOFacade.getTaskModel("taskWithRunningWorkflowId")).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getWorkflowModel("testWorkflowId", true)).thenReturn(workflow);
        asyncSystemTaskExecutor.execute(systemTask, "taskWithRunningWorkflowId");
        verify(queueDAO, times(1)).remove("queueName", "taskWithRunningWorkflowId");
        verify(workflowExecutor, times(1)).decide("testWorkflowId");
    }
}