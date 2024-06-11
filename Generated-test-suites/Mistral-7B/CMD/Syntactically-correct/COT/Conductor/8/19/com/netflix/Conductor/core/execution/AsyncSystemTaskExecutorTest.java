package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.QueuePriority;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskDefinition;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacadeMock;
    private QueueDAO queueDAOMock;
    private MetadataDAO metadataDAOMock;
    private ConductorProperties conductorPropertiesMock;
    private WorkflowExecutor workflowExecutorMock;
    private Monitors monitorsMock;

    @Before
    public void setUp() {
        executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
        queueDAOMock = mock(QueueDAO.class);
        metadataDAOMock = mock(MetadataDAO.class);
        conductorPropertiesMock = mock(ConductorProperties.class);
        workflowExecutorMock = mock(WorkflowExecutor.class);
        monitorsMock = mock(Monitors.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock,
                queueDAOMock,
                metadataDAOMock,
                conductorPropertiesMock,
                workflowExecutorMock);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "some-task-id";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        doReturn(Optional.empty()).when(executionDAOFacadeMock).getTaskModel(taskId);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacadeMock, times(1)).getTaskModel(taskId);
        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAOMock, never()).remove(anyString(), anyString());
        verify(workflowExecutorMock, never()).decide(anyString());
    }

    @Test
    public void testExecute_taskInProgress_taskRetrievalNotRequired() {
        String taskId = "some-task-id";
        String workflowId = "some-workflow-id";
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        doReturn(Optional.of(task)).when(executionDAOFacadeMock).getTaskModel(taskId);
        doReturn(workflow).when(executionDAOFacadeMock).getWorkflowModel(workflowId, false);

        doReturn(false).when(systemTask).isTaskRetrievalRequired();

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacadeMock, times(1)).getTaskModel(taskId);
        verify(executionDAOFacadeMock, times(1)).getWorkflowModel(workflowId, false);
        verify(queueDAOMock, times(1)).postpone(
                anyString(),
                taskId,
                anyInt(),
                eq(systemTaskCallbackTime));
        verify(queueDAOMock, never()).remove(anyString(), anyString());
        verify(workflowExecutorMock, times(1)).decide(workflowId);
    }

    @Test
    public void testExecute_taskInProgress_taskRetrievalRequired() {
        String taskId = "some-task-id";
        String workflowId = "some-workflow-id";
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        doReturn(Optional.of(task)).when(executionDAOFacadeMock).getTaskModel(taskId);
        doReturn(workflow).when(executionDAOFacadeMock).getWorkflowModel(workflowId, true);

        doReturn(true).when(systemTask).isTaskRetrievalRequired();

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacadeMock, times(1)).getTaskModel(taskId);
        verify(executionDAOFacadeMock, times(1)).getWorkflowModel(workflowId, true);
        verify(queueDAOMock, times(1)).postpone(
                anyString(),
                taskId,
                anyInt(),
                eq(systemTaskCallbackTime));
        verify(queueDAOMock, never()).remove(anyString(), anyString());
        verify(workflowExecutorMock, times(1)).decide(workflowId);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_taskTerminal() {
        String taskId = "some-task-id";
        String queueName = QueueUtils.getQueueName("some-workflow-id");
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        doReturn(Optional.of(task)).when(executionDAOFacadeMock).getTaskModel(taskId);
        doReturn(ExecutionStatus.COMPLETED).when(task).getStatus();

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacadeMock, times(1)).getTaskModel(taskId);
        verify(queueDAOMock, times(1)).remove(queueName, taskId);
        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(workflowExecutorMock, never()).decide(anyString());
    }

    @Test
    public void testExecute_taskScheduled_exceedsInProgressLimit() {
        String taskId = "some-task-id";
        String queueName = QueueUtils.getQueueName("some-workflow-id");
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        doReturn(Optional.of(task)).when(executionDAOFacadeMock).getTaskModel(taskId);
        doReturn(workflow).when(executionDAOFacadeMock).getWorkflowModel(task.getWorkflowInstanceId(), anyBoolean());
        doReturn(true).when(executionDAOFacadeMock).exceedsInProgressLimit(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacadeMock, times(1)).getTaskModel(taskId);
        verify(queueDAOMock, times(1)).postponeQuietly(queueName, task);
        verify(queueDAOMock, never()).remove(anyString(), anyString());
        verify(workflowExecutorMock, never()).decide(anyString());
        verify(monitorsMock, times(1)).error(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask");
    }

    @Test
    public void testExecute_taskScheduled_exceedsRateLimitPerFrequency() {
        String taskId = "some-task-id";
        String queueName = QueueUtils.getQueueName("some-workflow-id");
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        doReturn(Optional.of(task)).when(executionDAOFacadeMock).getTaskModel(taskId);
        doReturn(workflow).when(executionDAOFacadeMock).getWorkflowModel(task.getWorkflowInstanceId(), anyBoolean());
        doReturn(true).when(task).getRateLimitPerFrequency();
        doReturn(task.getRateLimitPerFrequency()).when(metadataDAOMock).getTaskDef(task.getTaskDefName());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacadeMock, times(1)).getTaskModel(taskId);
        verify(queueDAOMock, times(1)).postponeQuietly(queueName, task);
        verify(queueDAOMock, never()).remove(anyString(), anyString());
        verify(workflowExecutorMock, never()).decide(anyString());
        verify(monitorsMock, times(1)).error(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask");
    }

    @Test
    public void testExecute_taskInProgress_taskAsyncComplete() {
        String taskId = "some-task-id";
        String queueName = QueueUtils.getQueueName("some-workflow-id");
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        doReturn(Optional.of(task)).when(executionDAOFacadeMock).getTaskModel(taskId);
        doReturn(workflow).when(executionDAOFacadeMock).getWorkflowModel(task.getWorkflowInstanceId(), anyBoolean());
        doReturn(true).when(systemTask).isAsyncComplete(task);
        doReturn(QueuePriority.HIGH).when(task).getWorkflowPriority();

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacadeMock, times(1)).getTaskModel(taskId);
        verify(queueDAOMock, times(1)).postpone(
                queueName,
                taskId,
                eq(QueuePriority.HIGH.getValue()),
                eq(0L));
        verify(queueDAOMock, never()).remove(anyString(), anyString());
        verify(workflowExecutorMock, never()).decide(anyString());
    }

    @Test
    public void testExecute_taskTerminal_workflowTerminal() {
        String taskId = "some-task-id";
        String queueName = QueueUtils.getQueueName("some-workflow-id");
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        doReturn(Optional.of(task)).when(executionDAOFacadeMock).getTaskModel(taskId);
        doReturn(workflow).when(executionDAOFacadeMock).getWorkflowModel(task.getWorkflowInstanceId(), anyBoolean());
        doReturn(ExecutionStatus.COMPLETED).when(workflow).getStatus();

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacadeMock, times(1)).getTaskModel(taskId);
        verify(queueDAOMock, times(1)).remove(queueName, taskId);
        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(workflowExecutorMock, times(1)).decide(workflowId);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "some-queue-name";
        TaskModel task = Mockito.mock(TaskModel.class);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAOMock, times(1)).postponeQuietly(queueName, task);
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadTaskQuietly_taskNotFound() {
        String taskId = "some-task-id";

        asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacadeMock, times(1)).getTaskModel(taskId);
        verify(executionDAOFacadeMock, times(1)).getTaskModel(taskId);
        throw new IllegalStateException("Task not found");
    }
}