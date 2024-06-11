package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor systemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacadeMock;
    private QueueDAO queueDAOMock;
    private MetadataDAO metadataDAOMock;
    private WorkflowSystemTask systemTaskMock;
    private TaskModel taskMock;
    private WorkflowModel workflowModelMock;
    private long queueTaskMessagePostponeSecs;
    private long systemTaskCallbackTime;

    @Before
    public void setup() {
        executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
        queueDAOMock = mock(QueueDAO.class);
        metadataDAOMock = mock(MetadataDAO.class);
        systemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock,
                queueDAOMock,
                metadataDAOMock,
                null, // ConductorProperties is not mocked in this test
                null // WorkflowExecutor is not mocked in this test
        );
        queueTaskMessagePostponeSecs = 1;
        systemTaskCallbackTime = 1;
        systemTaskMock = mock(WorkflowSystemTask.class);
        taskMock = Mockito.mock(TaskModel.class);
        workflowModelMock = Mockito.mock(WorkflowModel.class);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "taskId123";
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.empty());

        systemTaskExecutor.execute(systemTaskMock, taskId);

        verify(LOGGER, times(1)).error(
                "TaskId: {} could not be found while executing {}", taskId, systemTaskMock);
        verifyNoMoreInteractions(executionDAOFacadeMock);
        verifyNoMoreInteractions(queueDAOMock);
        verifyNoMoreInteractions(metadataDAOMock);
    }

    @Test
    public void testExecute_taskTerminal() {
        String taskId = "taskId123";
        TaskModel task = TaskModel.builder().id(taskId).build();
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(task));

        systemTaskExecutor.execute(systemTaskMock, taskId);

        verify(LOGGER, times(1)).info(
                "Task {}/{} was already completed.", task.getTaskType(), task.getTaskId());
        verify(queueDAOMock, times(1)).remove(QueueUtils.getQueueName(task), taskId);
        verifyNoMoreInteractions(executionDAOFacadeMock);
    }

    @Test
    public void testExecute_taskScheduled() {
        String taskId = "taskId123";
        TaskModel task = TaskModel.builder().id(taskId).status(TaskModel.Status.SCHEDULED).build();
        WorkflowModel workflow = WorkflowModel.builder().id("workflowId123").build();

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(task));
        when(executionDAOFacadeMock.getWorkflowModel(workflow.getId(), false)).thenReturn(workflow);
        when(queueDAOMock.exceedsInProgressLimit(task)).thenReturn(true);

        systemTaskExecutor.execute(systemTaskMock, taskId);

        verify(LOGGER, times(1)).warn(
                "Concurrent Execution limited for {}:{}, limit:{}",
                taskId,
                task.getTaskDefName(),
                task.getConcurrentExecutionLimit());
        verify(queueDAOMock, times(1)).postpone(
                QueueUtils.getQueueName(task),
                taskId,
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
        verifyNoMoreInteractions(executionDAOFacadeMock);
    }

    @Test
    public void testExecute_taskRateLimitExceeded() {
        String taskId = "taskId123";
        TaskModel task = TaskModel.builder().id(taskId).status(TaskModel.Status.SCHEDULED).build();
        WorkflowModel workflow = WorkflowModel.builder().id("workflowId123").build();

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(task));
        when(executionDAOFacadeMock.getWorkflowModel(workflow.getId(), false)).thenReturn(workflow);
        when(metadataDAOMock.getTaskDef(task.getTaskDefName())).thenReturn(workflow);
        when(queueDAOMock.exceedsInProgressLimit(task)).thenReturn(false);
        when(queueDAOMock.exceedsRateLimitPerFrequency(task, workflow)).thenReturn(true);

        systemTaskExecutor.execute(systemTaskMock, taskId);

        verify(LOGGER, times(1)).warn(
                "RateLimit Execution limited for {}:{}, limit:{}",
                taskId,
                task.getTaskDefName(),
                task.getRateLimitPerFrequency());
        verify(queueDAOMock, times(1)).postpone(
                QueueUtils.getQueueName(task),
                taskId,
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
        verifyNoMoreInteractions(executionDAOFacadeMock);
    }

    @Test(expected = Exception.class)
    public void testExecute_workflowTerminal() {
        String taskId = "taskId123";
        String workflowId = "workflowId123";
        TaskModel task = TaskModel.builder().id(taskId).build();
        WorkflowModel workflow = WorkflowModel.builder().id(workflowId).status(WorkflowModel.Status.TERMINATED).build();

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(task));
        when(executionDAOFacadeMock.getWorkflowModel(workflowId, false)).thenReturn(workflow);

        systemTaskExecutor.execute(systemTaskMock, taskId);

        verify(LOGGER, times(1)).info(
                "Workflow {} has been completed for {}/{}, id: {}",
                workflow.toShortString(),
                systemTaskMock,
                taskId,
                workflowId);
        verify(queueDAOMock, times(1)).remove(QueueUtils.getQueueName(task), taskId);
        verifyNoMoreInteractions(executionDAOFacadeMock);
    }

    // Add more test cases for other methods in the class here...
}