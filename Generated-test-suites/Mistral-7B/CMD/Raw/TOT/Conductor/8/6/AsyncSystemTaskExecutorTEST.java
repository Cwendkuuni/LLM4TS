package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;

public class AsyncSystemTaskExecutorTest {

    @Autowired
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Autowired
    private ExecutionDAOFacade executionDAOFacadeMock;

    @Autowired
    private QueueDAO queueDAOMock;

    @Autowired
    private MetadataDAO metadataDAOMock;

    @Autowired
    private ConductorProperties conductorPropertiesMock;

    @Autowired
    private WorkflowExecutor workflowExecutorMock;

    private WorkflowSystemTask systemTaskMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        systemTaskMock = Mockito.mock(WorkflowSystemTask.class);

        Mockito.when(executionDAOFacadeMock.getTaskModel(Mockito.anyString()))
                .thenReturn(getTaskModel());

        Mockito.when(queueDAOMock.get(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(metadataDAOMock.getTaskDef(Mockito.anyString()))
                .thenReturn(getWorkflowModel());

        Mockito.when(workflowExecutorMock.decide(Mockito.anyString()))
                .thenReturn(true);

        Mockito.when(conductorPropertiesMock.getSystemTaskWorkerCallbackDuration())
                .thenReturn(Mockito.mock(ConductorProperties.Duration.class));

        Mockito.when(conductorPropertiesMock.getTaskExecutionPostponeDuration())
                .thenReturn(Mockito.mock(ConductorProperties.Duration.class));
    }

    @Test
    public void testExecute_TaskIsAlreadyCompleted_Returns() {
        // Given
        TaskModel task = getTaskModel();

        // When
        asyncSystemTaskExecutor.execute(systemTaskMock, task.getTaskId());

        // Then
        verify(queueDAOMock).remove(Mockito.eq("queueName"), Mockito.eq(task.getTaskId()));
    }

    @Test
    public void testExecute_TaskIsScheduledAndRateLimitIsExceeded_Postpones() {
        // Given
        TaskModel task = getTaskModel();

        Mockito.when(executionDAOFacadeMock.exceedsRateLimitPerFrequency(Mockito.any(), Mockito.any()))
                .thenReturn(true);

        // When
        asyncSystemTaskExecutor.execute(systemTaskMock, task.getTaskId());

        // Then
        ArgumentCaptor<Long> postponeArgCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAOMock).postpone(Mockito.eq("queueName"), Mockito.eq(task.getTaskId()),
                Mockito.eq(task.getWorkflowPriority()), postponeArgCaptor.capture());

        assertEquals(queueTaskMessagePostponeSecs, postponeArgCaptor.getValue());
    }

    @Test
    public void testExecute_TaskIsScheduledAndExecutionLimitIsExceeded_Postpones() {
        // Given
        TaskModel task = getTaskModel();

        Mockito.when(executionDAOFacadeMock.exceedsInProgressLimit(task))
                .thenReturn(true);

        // When
        asyncSystemTaskExecutor.execute(systemTaskMock, task.getTaskId());

        // Then
        ArgumentCaptor<Long> postponeArgCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAOMock).postpone(Mockito.eq("queueName"), Mockito.eq(task.getTaskId()),
                Mockito.eq(task.getWorkflowPriority()), postponeArgCaptor.capture());

        assertEquals(queueTaskMessagePostponeSecs, postponeArgCaptor.getValue());
    }

    @Test
    public void testExecute_TaskIsInProgress_Executes() {
        // Given
        TaskModel task = getTaskModel();
        task.setStatus(ExecutionStatus.IN_PROGRESS);

        // When
        asyncSystemTaskExecutor.execute(systemTaskMock, task.getTaskId());

        // Then
        verify(systemTaskMock).start(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testExecute_TaskIsNotScheduled_Postpones() {
        // Given
        TaskModel task = getTaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);

        // When
        asyncSystemTaskExecutor.execute(systemTaskMock, task.getTaskId());

        // Then
        ArgumentCaptor<Long> postponeArgCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAOMock).postpone(Mockito.eq("queueName"), Mockito.eq(task.getTaskId()),
                Mockito.eq(task.getWorkflowPriority()), postponeArgCaptor.capture());

        assertEquals(queueTaskMessagePostponeSecs, postponeArgCaptor.getValue());
    }

    @Test
    public void testExecute_TaskHasCompleted_RemovesFromQueue() {
        // Given
        TaskModel task = getTaskModel();
        task.setStatus(ExecutionStatus.COMPLETED);

        // When
        asyncSystemTaskExecutor.execute(systemTaskMock, task.getTaskId());

        // Then
        verify(queueDAOMock).remove(Mockito.eq("queueName"), Mockito.eq(task.getTaskId()));
    }

    @Test
    public void testExecute_TaskHasFailed_DoesNotRemoveFromQueue() {
        // Given
        TaskModel task = getTaskModel();
        task.setStatus(ExecutionStatus.FAILED);

        // When
        asyncSystemTaskExecutor.execute(systemTaskMock, task.getTaskId());

        // Then
        verify(queueDAOMock, Mockito.never()).remove(Mockito.eq("queueName"), Mockito.eq(task.getTaskId()));
    }

    @Test
    public void testExecute_TaskNotFound_DoesNothing() {
        // Given
        String taskId = "non-existing-task-id";

        // When
        asyncSystemTaskExecutor.execute(systemTaskMock, taskId);

        // Then
        verify(queueDAOMock, Mockito.never()).remove(Mockito.anyString(), Mockito.eq(taskId));
    }

    private TaskModel getTaskModel() {
        return new TaskModel() {{
            setTaskId("test-task-id");
            setTaskType("test-task-type");
            setWorkflowInstanceId("test-workflow-instance-id");
            setWorkflowPriority(1);
            setStatus(ExecutionStatus.SCHEDULED);
            setStartTime(System.currentTimeMillis());
            setEndTime(null);
            setTaskDefName("test-task-def-name");
            setQueueWaitTime(0);
            setCallbackAfterSeconds(systemTaskCallbackTime);
        }};
    }

    private WorkflowModel getWorkflowModel() {
        return new WorkflowModel() {{
            setId("test-workflow-id");
            setName("test-workflow-name");
            setStatus(ExecutionStatus.RUNNING);
        }};
    }

}