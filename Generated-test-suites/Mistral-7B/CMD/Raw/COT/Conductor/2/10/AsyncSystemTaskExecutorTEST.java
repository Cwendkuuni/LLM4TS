package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceStatus;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacadeMock;
    private QueueDAO queueDAOMock;
    private MetadataDAO metadataDAOMock;
    private WorkflowExecutor workflowExecutorMock;
    private ConductorProperties conductorPropertiesMock;

    @Before
    public void setUp() {
        executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
        queueDAOMock = mock(QueueDAO.class);
        metadataDAOMock = mock(MetadataDAO.class);
        workflowExecutorMock = mock(WorkflowExecutor.class);
        conductorPropertiesMock = mock(ConductorProperties.class);

        when(conductorPropertiesMock.getSystemTaskWorkerCallbackDuration())
                .thenReturn(Mockito.duration(1, TimeUnit.SECONDS));
        when(conductorPropertiesMock.getTaskExecutionPostponeDuration())
                .thenReturn(Mockito.duration(1, TimeUnit.SECONDS));

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock,
                queueDAOMock,
                metadataDAOMock,
                Mockito.spy(conductorPropertiesMock),
                workflowExecutorMock);
    }

    @Test
    public void testExecute_taskNotFound_queueIsCleared() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = null;

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAOMock).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_taskInProgress_taskStatusIsUpdated() {
        String taskId = "taskId";
        String queueName = "queueName";
        TaskModel task = Mockito.spy(new TaskModel());
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowInstanceStatus.RUNNING);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacadeMock.getWorkflowModel(task.getWorkflowInstanceId(), false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(task).setStatus(TaskModel.Status.IN_PROGRESS);
        verify(workflowExecutorMock).decide(task.getWorkflowInstanceId());
    }

    @Test(expected = Exception.class)
    public void testExecute_queueLimitExceeded_taskIsPostponed() {
        String taskId = "taskId";
        String queueName = "queueName";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowInstanceStatus.RUNNING);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacadeMock.getWorkflowModel(task.getWorkflowInstanceId(), false))
                .thenReturn(workflowModel);
        when(queueDAOMock.exceedsInProgressLimit(task))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAOMock).postpone(queueName, taskId, task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test(expected = Exception.class)
    public void testExecute_rateLimitExceeded_taskIsPostponed() {
        String taskId = "taskId";
        String queueName = "queueName";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowInstanceStatus.RUNNING);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacadeMock.getWorkflowModel(task.getWorkflowInstanceId(), false))
                .thenReturn(workflowModel);
        when(metadataDAOMock.getTaskDef(task.getTaskDefName()))
                .thenReturn(new TaskDefinitionModel());
        when(queueDAOMock.exceedsRateLimitPerFrequency(task, any()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAOMock).postpone(queueName, taskId, task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_taskCompleted_workflowIsEvaluated() {
        String taskId = "taskId";
        String queueName = "queueName";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowInstanceStatus.RUNNING);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacadeMock.getWorkflowModel(task.getWorkflowInstanceId(), false))
                .thenReturn(workflowModel);
        when(workflowExecutorMock.decide(anyString())).thenReturn(Optional.empty());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(workflowExecutorMock).decide(task.getWorkflowInstanceId());
    }

    @Test
    public void testPostponeQuietly_taskIsPostponed() {
        String queueName = "queueName";
        TaskModel task = new TaskModel();
        task.setTaskId("taskId");

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAOMock).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_taskIsReturned() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, loadedTask);
    }
}