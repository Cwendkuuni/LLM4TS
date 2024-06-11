package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskStub;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.execution.ExecutionDAOFacadeImpl;
import com.netflix.conductor.dao.metadata.MetadataDAOImpl;
import com.netflix.conductor.dao.queue.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.metrics.MonitorsImpl;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import com.netflix.conductor.model.config.WorkflowDefinition;
import com.netflix.conductor.utils.Clock;

@Configuration
@Import({ QueueDAOImpl.class, ExecutionDAOFacadeImpl.class, MetadataDAOImpl.class })
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private WorkflowSystemTask workflowSystemTask;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private WorkflowInstance workflowInstance;

    @Mock
    private WorkflowDefinition workflowDefinition;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration())
                .thenReturn(Instant.ofEpochSeconds(5000L));
        when(conductorProperties.getTaskExecutionPostponeDuration())
                .thenReturn(Instant.ofEpochSeconds(1000L));
        when(workflowExecutor.decide(anyString())).thenReturn(true);
    }

    @After
    public void teardown() {
        verifyNoMoreInteractions(executionDAOFacade, metadataDAO, queueDAO, conductorProperties,
                workflowExecutor, monitors, workflowSystemTask, task, workflowModel, workflowInstance,
                workflowDefinition);
    }

    @Test
    public void execute_taskIsAlreadyCompleted_shouldRemoveTaskFromQueue() {
        // Given
        givenWorkflowModelWithCompletedWorkflow();
        givenTaskWithCompletedWorkflow();

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        // Then
        verify(queueDAO).remove(eq("queueName"), eq("taskId"));
    }

    @Test
    public void execute_taskIsTerminal_shouldRemoveTaskFromQueue() {
        // Given
        givenWorkflowModelWithCompletedWorkflow();
        givenTaskWithTerminalStatus();

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        // Then
        verify(queueDAO).remove(eq("queueName"), eq("taskId"));
    }

    @Test
    public void execute_taskIsScheduled_shouldNotRemoveTaskFromQueue() {
        // Given
        givenTaskWithScheduledStatus();

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        // Then
        verify(queueDAO, never()).remove(eq("queueName"), eq("taskId"));
    }

    @Test(expected = NullPointerException.class)
    public void execute_taskIsNull_shouldThrowNPE() {
        // Given
        givenNullTask();

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, null);

        // Then
        // This test should throw an NPE as workflowSystemTask is null
    }

    @Test(expected = NullPointerException.class)
    public void execute_taskTypeIsNull_shouldThrowNPE() {
        // Given
        givenNullWorkflowSystemTask();

        // When
        asyncSystemTaskExecutor.execute(null, "taskId");

        // Then
        // This test should throw an NPE as task is null
    }

    @Test
    public void execute_taskIsScheduled_shouldUpdateTaskInDB() {
        // Given
        givenTaskWithScheduledStatus();

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        // Then
        verify(executionDAOFacade).updateTask(task);
    }

    @Test
    public void execute_taskIsInProgress_shouldUpdateTaskCallbackTimeAndPostponeTaskInQueue() {
        // Given
        givenTaskWithInProgressStatus();
        givenSystemTaskWithAsyncCompleteFalse();

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        // Then
        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).postpone(
                eq("queueName"), eq("taskId"), anyLong(), eq(queueTaskMessagePostponeSecs.getSeconds()));
    }

    @Test
    public void execute_taskIsAsyncCompleteAndNotScheduled_shouldRemoveTaskFromQueue() {
        // Given
        givenTaskWithAsyncCompleteTrueAndStatusNotScheduled();

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        // Then
        verify(queueDAO).remove(eq("queueName"), eq("taskId"));
    }

    @Test
    public void execute_taskIsAsyncCompleteAndHasWorkflowInstance_shouldCompleteWorkflow() {
        // Given
        givenTaskWithAsyncCompleteTrueAndHasWorkflowInstance();

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        // Then
        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void execute_taskIsAsyncCompleteAndNoWorkflowInstance_shouldNotCompleteWorkflow() {
        // Given
        givenTaskWithAsyncCompleteTrueAndNoWorkflowInstance();

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        // Then
        verify(workflowExecutor, never()).decide(workflowId);
    }

    @Test
    public void execute_taskIsInProgress_shouldUpdateTaskStartTime() {
        // Given
        givenTaskWithInProgressStatus();

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        // Then
        verify(task).setStartTime(eq(System.currentTimeMillis()));
    }

    @Test
    public void execute_taskIsTerminal_shouldUpdateTaskEndTime() {
        // Given
        givenTaskWithTerminalStatus();

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        // Then
        verify(task).setEndTime(eq(System.currentTimeMillis()));
    }

    private void givenWorkflowModelWithCompletedWorkflow() {
        givenWorkflowModelWithWorkflowStatus(WorkflowStatus.COMPLETED);
    }

    private void givenTaskWithCompletedWorkflow() {
        givenTaskWithWorkflowInstanceAndStatus(WorkflowStatus.COMPLETED);
    }

    private void givenTaskWithTerminalStatus() {
        givenTaskWithWorkflowInstanceAndStatus(TaskModel.Status.TERMINAL);
    }

    private void givenTaskWithScheduledStatus() {
        givenTaskWithStatus(TaskModel.Status.SCHEDULED);
    }

    private void givenNullTask() {
        task = null;
    }

    private void givenNullWorkflowSystemTask() {
        workflowSystemTask = null;
    }

    private void givenTaskWithInProgressStatus() {
        givenTaskWithStatus(TaskModel.Status.IN_PROGRESS);
    }

    private void givenTaskWithAsyncCompleteFalse() {
        givenTaskWithAsyncCompleteFalseAndHasWorkflowInstance();
    }

    private void givenTaskWithAsyncCompleteTrueAndNoWorkflowInstance() {
        givenTaskWithAsyncCompleteTrueAndHasNoWorkflowInstance();
    }

    private void givenTaskWithAsyncCompleteTrueAndHasWorkflowInstance() {
        givenTaskWithAsyncCompleteTrueAndHasWorkflowInstance();
        when(workflowModel.getId()).thenReturn(workflowId);
    }

    private void givenTaskWithAsyncCompleteTrueAndStatusNotScheduled() {
        givenTaskWithAsyncCompleteTrueAndHasNoWorkflowInstance();
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
    }

    private void givenSystemTaskWithAsyncCompleteFalse() {
        when(workflowSystemTask.isAsyncComplete(task)).thenReturn(false);
    }

    private void givenSystemTaskWithAsyncCompleteTrue() {
        when(workflowSystemTask.isAsyncComplete(task)).thenReturn(true);
    }

    private void givenWorkflowModelWithWorkflowStatus(WorkflowStatus workflowStatus) {
        givenWorkflowModel();
        when(workflowModel.getStatus()).thenReturn(workflowStatus);
    }

    private void givenTaskWithStatus(TaskModel.Status status) {
        givenTask();
        when(task.getStatus()).thenReturn(status);
    }

    private void givenTaskWithWorkflowInstanceAndStatus(TaskModel.Status status) {
        givenTaskWithStatus(status);
        givenTaskWithWorkflowInstance();
    }

    private void givenTask() {
        task = new TaskModel();
    }

    private void givenTaskWithWorkflowInstance() {
        task.setWorkflowInstanceId(workflowInstanceId);
        task.setTaskDefName("testTaskDefName");
        task.setTaskId("testTaskId");
        task.setWorkflowPriority(0);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setReasonForIncompletion(null);
    }

    private void givenWorkflowModel() {
        workflowModel = new WorkflowModel();
        workflowModel.setId(workflowId);
        workflowModel.setName("testName");
        workflowModel.setDefinition(workflowDefinition);
        workflowModel.setStatus(WorkflowStatus.RUNNING);
        workflowModel.setStartTime(System.currentTimeMillis());
    }

    private void givenWorkflowDefinition() {
        workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setName("testName");
    }

    private String workflowId = "testWorkflowId";
    private String workflowInstanceId = "testWorkflowInstanceId";
    private Instant queueTaskMessagePostponeSecs = Instant.ofEpochSeconds(1000L);

}