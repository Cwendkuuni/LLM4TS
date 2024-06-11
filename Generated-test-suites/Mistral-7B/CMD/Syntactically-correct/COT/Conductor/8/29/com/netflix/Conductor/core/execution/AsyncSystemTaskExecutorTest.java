package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
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
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskStub;
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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_taskNull() {
        // Given
        String taskId = "taskId";
        WorkflowSystemTask systemTask = new WorkflowSystemTaskStub();

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(executionDAOFacade, never()).getTaskModel(taskId);
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskExists_terminal() {
        // Given
        String taskId = "taskId";
        String workflowId = "workflowId";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(ExecutionStatus.COMPLETED);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setId(workflowId);
        workflow.setStatus(WorkflowStatus.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);

        WorkflowSystemTask systemTask = new WorkflowSystemTaskStub();

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, never()).remove(anyString(), taskId);
        verify(executionDAOFacade, never()).updateTask(task);
        verify(workflowExecutor, never()).decide(workflowId);
    }

    @Test
    public void testExecute_taskExists_scheduled() {
        // Given
        String taskId = "taskId";
        String workflowId = "workflowId";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(ExecutionStatus.SCHEDULED);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setId(workflowId);
        workflow.setStatus(WorkflowStatus.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(queueDAO.remove(anyString(), taskId)).thenReturn(true);

        WorkflowSystemTask systemTask = new WorkflowSystemTaskStub();

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, times(1)).remove(anyString(), taskId);
        verify(executionDAOFacade, times(1)).updateTask(task);
        verify(workflowExecutor, never()).decide(workflowId);
    }

    @Test
    public void testExecute_taskExists_inProgress() {
        // Given
        String taskId = "taskId";
        String workflowId = "workflowId";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(ExecutionStatus.IN_PROGRESS);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setId(workflowId);
        workflow.setStatus(WorkflowStatus.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        doThrow(new RuntimeException("Test Exception")).when(systemTask).execute(any(), any(), any());
        doNothing().when(queueDAO).remove(anyString(), taskId);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, never()).remove(anyString(), taskId);
        verify(executionDAOFacade, times(1)).updateTask(task);
        verify(workflowExecutor, never()).decide(workflowId);

        // Verify exception is caught and logged
        verify(LOGGER, times(1)).error("Error executing system task - Test Exception, with id:" + taskId);
        verify(Monitors, times(1)).error(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask");
    }

    @Test
    public void testExecute_taskExceedsInProgressLimit() {
        // Given
        String taskId = "taskId";
        String workflowId = "workflowId";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(ExecutionStatus.SCHEDULED);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setId(workflowId);
        workflow.setStatus(WorkflowStatus.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        WorkflowSystemTask systemTask = new WorkflowSystemTaskStub();

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, never()).remove(anyString(), taskId);
        verify(executionDAOFacade, never()).updateTask(task);
        verify(workflowExecutor, never()).decide(workflowId);
        verify(queueDAO, times(1)).postpone(anyString(), taskId, anyInt(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_rateLimitExceeded() {
        // Given
        String taskId = "taskId";
        String workflowId = "workflowId";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(ExecutionStatus.SCHEDULED);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setId(workflowId);
        workflow.setStatus(WorkflowStatus.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(Optional.of(new TaskDefinitionStub()));
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        WorkflowSystemTask systemTask = new WorkflowSystemTaskStub();

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, never()).remove(anyString(), taskId);
        verify(executionDAOFacade, never()).updateTask(task);
        verify(workflowExecutor, never()).decide(workflowId);
        verify(queueDAO, times(1)).postpone(anyString(), taskId, anyInt(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testPostponeQuietly_queueDAOException() {
        // Given
        String queueName = "queueName";
        TaskModel task = new TaskModel();

        // When
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        // Then
        verify(queueDAO, times(1)).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
        verify(LOGGER, times(1)).error("Error postponing task: {} in queue: {}", task.getTaskId(), queueName);
    }

    @Test
    public void testLoadTaskQuietly_executionDAOFacadeException() {
        // Given
        String taskId = "taskId";

        // When
        asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Then
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }
}

class TaskDefinitionStub {}

class WorkflowSystemTaskStub implements WorkflowSystemTask {
    private boolean isAsyncComplete = false;

    @Override
    public boolean isAsyncComplete(TaskModel task) {
        return isAsyncComplete;
    }

    @Override
    public void start(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
        isAsyncComplete = true;
    }

    @Override
    public void execute(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
        // No-op
    }
}