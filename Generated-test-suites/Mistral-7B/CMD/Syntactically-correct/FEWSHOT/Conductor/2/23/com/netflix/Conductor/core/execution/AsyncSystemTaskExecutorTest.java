package com.netflix.conductor.core.execution;

import static com.netflix.conductor.core.execution.AsyncSystemTaskExecutor.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueMessage;
import com.netflix.conductor.dao.QueueMessageStatus;
import com.netflix.conductor.dao.QueueMessageWithTask;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;

public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor testee;

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

    private WorkflowSystemTask systemTask;

    private TaskModel task;

    private WorkflowModel workflow;

    private String queueName;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(10));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(5));
        systemTask = mock(WorkflowSystemTask.class);
        task = mock(TaskModel.class);
        workflow = mock(WorkflowModel.class);
        queueName = "testQueue";
    }

    @Test
    public void testExecute_taskNull_shouldReturn() {
        // Given
        String taskId = "taskId";

        // When
        testee.execute(systemTask, taskId);

        // Then
        verify(queueDAO, never()).remove(queueName, taskId);
        verifyZeroInteractions(executionDAOFacade, metadataDAO, workflowExecutor);
    }

    @Test
    public void testExecute_executionDAOFacadeThrowsException_shouldReturn() {
        // Given
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

        // When
        testee.execute(systemTask, taskId);

        // Then
        verify(queueDAO, never()).remove(queueName, taskId);
        verifyZeroInteractions(executionDAOFacade, metadataDAO, workflowExecutor);
        verify(executionDAOFacade).updateTask(task);
    }

    @Test
    public void testExecute_workflowModelNull_shouldReturn() {
        // Given
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(null);

        // When
        testee.execute(systemTask, taskId);

        // Then
        verify(queueDAO, never()).remove(queueName, taskId);
        verifyZeroInteractions(executionDAOFacade, metadataDAO, workflowExecutor);
        verify(executionDAOFacade).updateTask(task);
    }

    @Test
    public void testExecute_workflowModelTerminal_shouldReturn() {
        // Given
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.TERMINATED);

        // When
        testee.execute(systemTask, taskId);

        // Then
        verify(queueDAO, times(1)).remove(queueName, taskId);
        verifyZeroInteractions(executionDAOFacade, metadataDAO, workflowExecutor);
    }

    @Test
    public void testExecute_taskInProgress_shouldPostpone() {
        // Given
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(queueDAO.get(queueName, taskId)).thenReturn(QueueMessage.builder().build());
        when(workflowExecutor.decide(anyString())).thenReturn(true);

        // When
        testee.execute(systemTask, taskId);

        // Then
        verify(queueDAO, times(1)).postpone(
                queueName,
                taskId,
                anyInt(),
                anyLong());
        verifyZeroInteractions(executionDAOFacade, metadataDAO);
    }

    @Test
    public void testExecute_taskCompleted_shouldRemoveFromQueue() {
        // Given
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(queueDAO.get(queueName, taskId)).thenReturn(
                QueueMessage.builder().status(QueueMessageStatus.PENDING).build());

        // When
        testee.execute(systemTask, taskId);

        // Then
        verify(queueDAO, times(1)).remove(queueName, taskId);
        verifyZeroInteractions(executionDAOFacade, metadataDAO);
    }

    @Test
    public void testExecute_asyncComplete_shouldRemoveFromQueue() {
        // Given
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(queueDAO.get(queueName, taskId)).thenReturn(
                QueueMessageWithTask.builder().queueMessage(
                        QueueMessage.builder()
                                .status(QueueMessageStatus.PENDING)
                                .taskId(taskId)
                                .build())
                        .task(task)
                        .build());
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        // When
        testee.execute(systemTask, taskId);

        // Then
        verify(queueDAO, times(1)).remove(queueName, taskId);
        verifyZeroInteractions(executionDAOFacade, metadataDAO);
    }

    @Test
    public void testExecute_taskStatusTerminal_shouldRemoveFromQueue() {
        // Given
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(queueDAO.get(queueName, taskId)).thenReturn(
                QueueMessageWithTask.builder().queueMessage(
                        QueueMessage.builder()
                                .status(QueueMessageStatus.PENDING)
                                .taskId(taskId)
                                .build())
                        .task(task)
                        .build());
        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINAL);

        // When
        testee.execute(systemTask, taskId);

        // Then
        verify(queueDAO, times(1)).remove(queueName, taskId);
        verifyZeroInteractions(executionDAOFacade, metadataDAO);
    }

    @Test
    public void testExecute_taskStatusScheduled_shouldUpdateCallback() {
        // Given
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(queueDAO.get(queueName, taskId)).thenReturn(QueueMessage.builder().build());
        when(workflowExecutor.decide(anyString())).thenReturn(true);

        // When
        testee.execute(systemTask, taskId);

        // Then
        verify(task).setCallbackAfterSeconds(anyLong());
        verifyZeroInteractions(executionDAOFacade, metadataDAO);
    }

    @Test
    public void testExecute_taskStatusInProgress_shouldUpdateCallback() {
        // Given
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(queueDAO.get(queueName, taskId)).thenReturn(QueueMessage.builder().build());

        // When
        testee.execute(systemTask, taskId);

        // Then
        verify(task).setCallbackAfterSeconds(anyLong());
        verifyZeroInteractions(executionDAOFacade, metadataDAO);
    }

    @Test
    public void testExecute_taskStatusTerminal_shouldUpdateCallback() {
        // Given
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(queueDAO.get(queueName, taskId)).thenReturn(QueueMessage.builder().build());
        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINAL);

        // When
        testee.execute(systemTask, taskId);

        // Then
        verify(task).setCallbackAfterSeconds(anyLong());
        verifyZeroInteractions(executionDAOFacade, metadataDAO);
    }
}