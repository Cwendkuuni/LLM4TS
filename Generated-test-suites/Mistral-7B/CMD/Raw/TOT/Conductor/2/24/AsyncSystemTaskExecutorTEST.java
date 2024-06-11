package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.joda.time.Duration;
import org.joda.time.Instants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.config.WorkflowDefinition;

public class AsyncSystemTaskExecutorTest {

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private MetadataDAO metadataDAO;

    @MockBean
    private ConductorProperties conductorProperties;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @Autowired
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask mockSystemTask;

    private TaskModel mockTask;

    private String mockTaskId = "mockTaskId";

    @Before
    public void setup() {
        mockSystemTask = Mockito.mock(WorkflowSystemTask.class);
        mockTask = Mockito.mock(TaskModel.class);

        Mockito.when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(
                Duration.standardSeconds(5));
        Mockito.when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(
                Duration.standardSeconds(10));

        when(executionDAOFacade.getTaskModel(mockTaskId)).thenReturn(mockTask);
        when(executionDAOFacade.getWorkflowModel(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean()))
                .thenReturn(createMockWorkflowModel());
        when(metadataDAO.getTaskDef(ArgumentMatchers.anyString())).thenReturn(createMockWorkflowDefinition());
        when(workflowExecutor.decide(ArgumentMatchers.anyString())).thenReturn(true);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute_TaskWithNoError_ShouldUpdateTaskStatusAndRemoveFromQueue() {
        // Given
        String workflowId = "workflowId";
        Status workflowStatus = Status.IN_PROGRESS;
        WorkflowModel mockWorkflowModel = createMockWorkflowModel();
        mockWorkflowModel.setStatus(workflowStatus);

        when(executionDAOFacade.exceedsInProgressLimit(mockTask)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(mockTask, ArgumentMatchers.any()))
                .thenReturn(false);

        // When
        asyncSystemTaskExecutor.execute(mockSystemTask, mockTaskId);

        // Then
        verify(mockTask, times(1)).setStatus(ArgumentMatchions.notNullValue());
        verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(mockTask), mockTaskId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testExecute_TaskWithExceededInProgressLimit_ShouldPostponeTask() {
        // Given
        when(executionDAOFacade.exceedsInProgressLimit(mockTask)).thenReturn(true);

        // When
        asyncSystemTaskExecutor.execute(mockSystemTask, mockTaskId);

        // Then
        verify(queueDAO, times(1)).postpone(
                QueueUtils.getQueueName(mockTask),
                mockTaskId,
                mockTask.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_TaskWithExceededRateLimitPerFrequency_ShouldPostponeTask() {
        // Given
        when(executionDAOFacade.exceedsRateLimitPerFrequency(mockTask, ArgumentMatchers.any()))
                .thenReturn(true);

        // When
        asyncSystemTaskExecutor.execute(mockSystemTask, mockTaskId);

        // Then
        verify(queueDAO, times(1)).postpone(
                QueueUtils.getQueueName(mockTask),
                mockTaskId,
                mockTask.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_TaskWithTerminalWorkflow_ShouldRemoveTaskFromQueueAndUpdateStatus() {
        // Given
        Status workflowStatus = Status.FAILED;
        WorkflowModel mockWorkflowModel = createMockWorkflowModel();
        mockWorkflowModel.setStatus(workflowStatus);

        // When
        asyncSystemTaskExecutor.execute(mockSystemTask, mockTaskId);

        // Then
        verify(mockTask, times(1)).setStatus(ArgumentMatchions.notNullValue());
        verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(mockTask), mockTaskId);
    }

    @Test
    public void testExecute_TaskWithError_ShouldLogErrorAndUpdateTaskStatus() {
        // Given
        when(executionDAOFacade.getTaskModel(mockTaskId)).thenThrow(new RuntimeException());

        // When
        asyncSystemTaskExecutor.execute(mockSystemTask, mockTaskId);

        // Then
        verify(mockTask, times(1)).setStatus(ArgumentMatchions.notNullValue());
        verify(mockTask, times(1)).setReasonForIncompletion(ArgumentMatchers.anyString());
    }

    private WorkflowModel createMockWorkflowModel() {
        return Mockito.mock(WorkflowModel.class);
    }

    private WorkflowDefinition createMockWorkflowDefinition() {
        return Mockito.mock(WorkflowDefinition.class);
    }
}