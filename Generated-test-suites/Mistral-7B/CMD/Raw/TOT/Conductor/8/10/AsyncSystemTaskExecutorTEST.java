package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.stereotype.Component;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskImpl;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.WorkflowModelImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.QueueUtils;

@Component
public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor systemUnderTest;
    private ExecutionDAOFacade executionDAOFacadeMock;
    private QueueDAO queueDAOMock;
    private MetadataDAO metadataDAOMock;
    private WorkflowExecutor workflowExecutorMock;
    private WorkflowSystemTask workflowSystemTaskMock;

    @Before
    public void setup() {
        executionDAOFacadeMock = Mockito.mock(ExecutionDAOFacade.class);
        queueDAOMock = Mockito.mock(QueueDAO.class);
        metadataDAOMock = Mockito.mock(MetadataDAO.class);
        workflowExecutorMock = Mockito.mock(WorkflowExecutor.class);
        workflowSystemTaskMock = Mockito.mock(WorkflowSystemTaskImpl.class);

        ConductorProperties conductorProperties = Mockito.mock(ConductorProperties.class);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Mockito.mock(Duration.class));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Mockito.mock(Duration.class));

        systemUnderTest = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock,
                queueDAOMock,
                metadataDAOMock,
                Mockito.mock(ConductorProperties.class),
                workflowExecutorMock);
    }

    @Test
    public void execute_taskIsExistingAndNotTerminal_taskIsAddedToQueue() {
        String taskId = "task-123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("taskType");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setWorkflowInstanceId("workflow-123");
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setId("workflow-123");
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(taskModel));
        when(executionDAOFacadeMock.getWorkflowModel(
                workflowModel.getId(), workflowSystemTaskMock.isTaskRetrievalRequired())).thenReturn(workflowModel);

        systemUnderTest.execute(workflowSystemTaskMock, taskId);

        verify(queueDAOMock).postpone(
                QueueUtils.getQueueName(taskModel),
                taskId,
                taskModel.getWorkflowPriority(),
                systemUnderTest.getSystemTaskCallbackTime().getSeconds());
    }

    @Test
    public void execute_taskIsExistingAndTerminal_taskIsRemovedFromQueue() {
        String taskId = "task-123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("taskType");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setWorkflowInstanceId("workflow-123");
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setId("workflow-123");
        workflowModel.setStatus(WorkflowModel.Status.FAILED);

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(taskModel));
        when(executionDAOFacadeMock.getWorkflowModel(
                workflowModel.getId(), workflowSystemTaskMock.isTaskRetrievalRequired())).thenReturn(workflowModel);

        systemUnderTest.execute(workflowSystemTaskMock, taskId);

        verify(queueDAOMock).remove(QueueUtils.getQueueName(taskModel), taskId);
    }

    @Test
    public void execute_taskIsNotExisting_nothingHappens() {
        String taskId = "task-123";

        systemUnderTest.execute(workflowSystemTaskMock, taskId);

        verifyNoInteractions(executionDAOFacadeMock, queueDAOMock, metadataDAOMock, workflowExecutorMock);
    }

    @Test
    public void execute_exceedsConcurrentLimit_taskIsPostponed() {
        String taskId = "task-123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("taskType");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setWorkflowInstanceId("workflow-123");

        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setId("workflow-123");
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(taskModel));
        when(executionDAOFacadeMock.getWorkflowModel(
                workflowModel.getId(), workflowSystemTaskMock.isTaskRetrievalRequired())).thenReturn(workflowModel);
        when(executionDAOFacadeMock.exceedsInProgressLimit(taskModel)).thenReturn(true);

        systemUnderTest.execute(workflowSystemTaskMock, taskId);

        verify(queueDAOMock).postpone(
                QueueUtils.getQueueName(taskModel),
                taskId,
                taskModel.getWorkflowPriority(),
                systemUnderTest.getQueueTaskMessagePostponeSecs().getSeconds());
    }

    @Test
    public void execute_exceedsRateLimitPerFrequency_taskIsPostponed() {
        String taskId = "task-123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("taskType");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setWorkflowInstanceId("workflow-123");
        taskModel.setRateLimitPerFrequency(5);

        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setId("workflow-123");
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(taskModel));
        when(executionDAOFacadeMock.getWorkflowModel(
                workflowModel.getId(), workflowSystemTaskMock.isTaskRetrievalRequired())).thenReturn(workflowModel);
        when(metadataDAOMock.getTaskDef(taskModel.getTaskDefName())).thenReturn(Mockito.mock(TaskDef.class));
        when(executionDAOFacadeMock.exceedsRateLimitPerFrequency(
                taskModel, Mockito.any(MetadataDAO.class))).thenReturn(true);

        systemUnderTest.execute(workflowSystemTaskMock, taskId);

        verify(queueDAOMock).postpone(
                QueueUtils.getQueueName(taskModel),
                taskId,
                taskModel.getWorkflowPriority(),
                systemUnderTest.getQueueTaskMessagePostponeSecs().getSeconds());
    }

    @Test
    public void execute_taskIsExecutedAndCompleted_taskIsRemovedFromQueue() {
        String taskId = "task-123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("taskType");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setWorkflowInstanceId("workflow-123");
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setId("workflow-123");
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(taskModel));
        when(executionDAOFacadeMock.getWorkflowModel(
                workflowModel.getId(), workflowSystemTaskMock.isTaskRetrievalRequired())).thenReturn(workflowModel);
        when(workflowSystemTaskMock.isAsyncComplete(taskModel)).thenReturn(true);

        systemUnderTest.execute(workflowSystemTaskMock, taskId);

        verify(queueDAOMock).remove(QueueUtils.getQueueName(taskModel), taskId);
    }
}