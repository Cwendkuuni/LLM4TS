package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.QueueStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAOImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;

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

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private TaskModel taskModel;

    @Mock
    private WorkflowSystemTask workflowSystemTask;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "123";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_exceedsInProgressLimit() {
        String queueName = "queueName";
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(queueDAO).postpone(queueName, taskCaptor.capture(), anyLong(), anyLong());
        TaskModel capturedTask = taskCaptor.getValue();
        assertEquals(taskId, capturedTask.getTaskId());
    }

    @Test
    public void testExecute_exceedsRateLimitPerFrequency() {
        String queueName = "queueName";
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new WorkflowModel());
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(queueDAO).postpone(queueName, taskCaptor.capture(), anyLong(), anyLong());
        TaskModel capturedTask = taskCaptor.getValue();
        assertEquals(taskId, capturedTask.getTaskId());
    }

    @Test
    public void testExecute_workflowCompleted() {
        String workflowId = "workflowId";
        String queueName = "queueName";
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(ExecutionStatus.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(ExecutionStatus.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove(queueName, taskId);
        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testExecute_taskInProgress() {
        String queueName = "queueName";
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(queueDAO).postpone(queueName, taskCaptor.capture(), anyLong(), anyLong());
        TaskModel capturedTask = taskCaptor.getValue();
        assertEquals(taskId, capturedTask.getTaskId());
    }

    @Test
    public void testExecute_taskScheduled() {
        String queueName = "queueName";
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(queueDAO).postpone(queueName, taskCaptor.capture(), anyLong(), anyLong());
        TaskModel capturedTask = taskCaptor.getValue();
        assertEquals(taskId, capturedTask.getTaskId());
    }

    @Test
    public void testExecute_taskAsyncComplete() {
        String queueName = "queueName";
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);

        when(workflowSystemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, never()).postpone(anyString(), any(TaskModel.class), anyLong(), anyLong());
        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testExecute_taskTerminal() {
        String queueName = "queueName";
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);
        task.setStatus(ExecutionStatus.FAILED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        String taskId = "123";

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAO).postpone(queueName, taskId, anyLong(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, loadedTask);
    }
}