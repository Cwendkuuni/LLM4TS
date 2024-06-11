package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModelImpl;
import com.netflix.conductor.utils.WorkflowExecutorMock;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.Optional;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(5));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(10));
        when(queueDAO.getQueueName(any(TaskModel.class))).thenReturn("task-queue");
    }

    @Test
    public void testExecute_TaskNotInExecutionDAO_RemoveTaskFromQueue() {
        String taskId = "task-id";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        TaskModel task = null;
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove("task-queue", taskId);
    }

    @Test
    public void testExecute_TaskAlreadyCompleted_RemoveTaskFromQueue() {
        String workflowId = "workflow-id";
        String taskId = "task-id";
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(ExecutionStatus.COMPLETED);

        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setStatus(ExecutionStatus.COMPLETED);

        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).remove("task-queue", taskId);
    }

    @Test
    public void testExecute_TaskRateLimitExceeded_PostponeTask() {
        String taskId = "task-id";
        TaskModel task = new TaskModel();
        task.setTaskDefName("task-def-name");
        task.setRateLimitPerFrequency(1);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);

        Monitors mockMonitors = mock(Monitors.class);
        when(conductorProperties.getMonitors()).thenReturn(mockMonitors);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone("task-queue", taskId, task.getWorkflowPriority(), 10);
    }

    @Test
    public void testExecute_TaskInExecutionDAO_ExecuteTaskAndUpdate() {
        String taskId = "task-id";
        String workflowId = "workflow-id";
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(ExecutionStatus.SCHEDULED);

        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setStatus(ExecutionStatus.RUNNING);

        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);

        Monitors mockMonitors = mock(Monitors.class);
        when(conductorProperties.getMonitors()).thenReturn(mockMonitors);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify that the task status is updated
        verify(executionDAOFacade).updateTask(task);

        // Verify that the task is removed from the queue if the execution is completed
        task.setStatus(ExecutionStatus.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove("task-queue", taskId);
    }

    @Test
    public void testExecute_TaskInProgress_ExecuteTaskAndUpdate() {
        String taskId = "task-id";
        String workflowId = "workflow-id";
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(ExecutionStatus.IN_PROGRESS);

        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setStatus(ExecutionStatus.RUNNING);

        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);

        Monitors mockMonitors = mock(Monitors.class);
        when(conductorProperties.getMonitors()).thenReturn(mockMonitors);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify that the task status is updated
        verify(executionDAOFacade).updateTask(task);

        // Verify that the task is removed from the queue if the execution is completed
        task.setStatus(ExecutionStatus.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove("task-queue", taskId);
    }

    @Test
    public void testExecute_TaskAsyncComplete_RemoveTaskFromQueue() {
        String taskId = "task-id";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.IN_PROGRESS);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove("task-queue", taskId);
    }

    @Test
    public void testExecute_TaskConcurrentLimitExceeded_PostponeTask() {
        String taskId = "task-id";
        String taskDefName = "task-def-name";
        TaskModel task = new TaskModel();
        task.setTaskDefName(taskDefName);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone("task-queue", taskId, task.getWorkflowPriority(), 10);
    }
}