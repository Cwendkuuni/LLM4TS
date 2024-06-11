package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

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
import com.netflix.conductor.dao.Queue;
import com.netflix.conductor.utils.QueueUtils;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade mockExecutionDAOFacade;
    private QueueDAO mockQueueDAO;
    private MetadataDAO mockMetadataDAO;
    private ConductorProperties mockConductorProperties;
    private WorkflowExecutor mockWorkflowExecutor;

    @Before
    public void setup() {
        mockExecutionDAOFacade = mock(ExecutionDAOFacade.class);
        mockQueueDAO = mock(QueueDAO.class);
        mockMetadataDAO = mock(MetadataDAO.class);
        mockConductorProperties = mock(ConductorProperties.class);
        mockWorkflowExecutor = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                mockExecutionDAOFacade,
                mockQueueDAO,
                mockMetadataDAO,
                mockConductorProperties,
                mockWorkflowExecutor);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = UUID.randomUUID().toString();
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(mockQueueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        String taskId = UUID.randomUUID().toString();
        String workflowId = UUID.randomUUID().toString();
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(mockQueueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_taskScheduled() {
        String taskId = UUID.randomUUID().toString();
        String workflowId = UUID.randomUUID().toString();
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(task.getWorkflowInstanceId()).thenReturn(workflowId);
        when(workflow.getStatus()).thenReturn(WorkflowModel.State.RUNNING);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(mockQueueDAO).postpone(
                QueueUtils.getQueueName(task),
                taskId,
                task.getWorkflowPriority(),
                asyncSystemTaskExecutor.getSystemTaskCallbackTime());
    }

    @Test
    public void testExecute_taskInProgress() {
        String taskId = UUID.randomUUID().toString();
        String workflowId = UUID.randomUUID().toString();
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(task.getWorkflowInstanceId()).thenReturn(workflowId);
        when(workflow.getStatus()).thenReturn(WorkflowModel.State.RUNNING);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask).execute(workflow, task, mockWorkflowExecutor);
    }

    @Test
    public void testExecute_taskTerminal() {
        String taskId = UUID.randomUUID().toString();
        String workflowId = UUID.randomUUID().toString();
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINAL);
        when(task.getWorkflowInstanceId()).thenReturn(workflowId);
        when(workflow.getStatus()).thenReturn(WorkflowModel.State.TERMINATED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(mockQueueDAO).remove(QueueUtils.getQueueName(task), taskId);
    }

    @Test
    public void testExecute_taskFailed() {
        String taskId = UUID.randomUUID().toString();
        String workflowId = UUID.randomUUID().toString();
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(task.getWorkflowInstanceId()).thenReturn(workflowId);
        when(workflow.getStatus()).thenReturn(WorkflowModel.State.RUNNING);

        doThrow(new RuntimeException("Test Exception")).when(systemTask).execute(workflow, task, mockWorkflowExecutor);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(mockQueueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testPostponeQuietly_queueDAOException() {
        String queueName = "testQueue";
        TaskModel task = Mockito.mock(TaskModel.class);

        when(mockQueueDAO.postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs))
                .thenThrow(new Exception("Test Queue DAO Exception"));

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(mockLogger, times(1)).error("Error postponing task: {} in queue: {}", task.getTaskId(), queueName);
    }

    @Test
    public void testLoadTaskQuietly_executionDAOException() {
        String taskId = UUID.randomUUID().toString();

        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenThrow(new Exception("Test Execution DAO Exception"));

        asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // No verification as mockExecutionDAOFacade.getTaskModel() is not verified in the test
    }
}