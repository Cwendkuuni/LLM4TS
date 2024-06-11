package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskImpl;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.metadata.InMemoryMetadataDAO;
import com.netflix.conductor.dao.queue.QueueEntry;
import com.netflix.conductor.dao.queue.QueueMessage;
import com.netflix.conductor.dao.queue.QueueMessageImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskDef;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.model.WorkflowModelImpl;
import com.netflix.conductor.utils.Json;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor systemUnderTest;

    private static final String TASK_TYPE = "testTaskType";
    private static final String TASK_ID = "testTaskId";
    private static final String WORKFLOW_ID = "testWorkflowId";
    private static final String WORKFLOW_INSTANCE_ID = "testWorkflowInstanceId";
    private static final String QUEUE_NAME = QueueUtils.getQueueName(new TaskModel(TASK_ID));
    private static final long SYSTEM_TASK_CALLBACK_TIME = 100L;
    private static final long QUEUE_TASK_MESSAGE_POSTPONE_SECS = 500L;

    private TaskModel task;
    private WorkflowModel workflow;
    private WorkflowSystemTask systemTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        task = new TaskModel(TASK_ID);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setWorkflowInstanceId(WORKFLOW_INSTANCE_ID);
        task.setWorkflowPriority(10);
        task.setTaskDefName("testTaskDefName");
        task.setCallbackAfterSeconds(QUEUE_TASK_MESSAGE_POSTPONE_SECS);

        workflow = new WorkflowModelImpl();
        workflow.setId(WORKFLOW_ID);
        workflow.setName("testWorkflowName");
        workflow.setStatus(ExecutionStatus.RUNNING);

        systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(any())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(final InvocationOnMock invocation) throws Throwable {
                return false;
            }
        });
        when(systemTask.getEvaluationOffset(any(), anyLong()))
                .thenReturn(Optional.of(Optional.of(QUEUE_TASK_MESSAGE_POSTPONE_SECS)));

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflow);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskDef());
        when(workflowExecutor.decide(anyString())).thenReturn(null);

        Monitors.registerMonitor(Monitors.MonitorType.QUEUE_WAIT_TIME);
    }

    @Test
    public void testExecute_TaskIsAlreadyCompleted() {
        // Given
        task.setStatus(TaskModel.Status.COMPLETED);

        // When
        systemUnderTest.execute(systemTask, TASK_ID);

        // Then
        verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
        verify(queueDAO, never()).postpone(QUEUE_NAME, TASK_ID, anyInt(), anyLong());
        verify(executionDAOFacade, never()).updateTask(task);
        verifyNoMoreInteractions(executionDAOFacade);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(metadataDAO);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_TaskIsNotInQueue() {
        // Given
        TaskModel nullTask = null;

        // When
        systemUnderTest.execute(systemTask, TASK_ID);

        // Then
        verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(executionDAOFacade, never()).updateTask(task);
        verify(executionDAOFacade).getTaskModel(TASK_ID);
        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
        verifyNoMoreInteractions(executionDAOFacade);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(metadataDAO);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_TaskIsTerminal() {
        // Given
       task.setStatus(TaskModel.Status.FAILED);

        // When
        systemUnderTest.execute(systemTask, TASK_ID);

        // Then
        verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
        verify(queueDAO, never()).postpone(QUEUE_NAME, TASK_ID, anyInt(), anyLong());
        verify(executionDAOFacade, never()).updateTask(task);
        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
        verifyNoMoreInteractions(executionDAOFacade);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(metadataDAO);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_TaskIsNotInProgress() {
        // Given
        when(systemTask.isAsyncComplete(any())).thenReturn(true);

        // When
        systemUnderTest.execute(systemTask, TASK_ID);

        // Then
        verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
        verify(queueDAO, never()).postpone(QUEUE_NAME, TASK_ID, anyInt(), anyLong());
        verify(executionDAOFacade).getTaskModel(TASK_ID);
        verify(executionDAOFacade).updateTask(task);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(metadataDAO);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_TaskIsInProgress() {
        // Given
        when(systemTask.isAsyncComplete(any())).thenReturn(false);

        // When
        systemUnderTest.execute(systemTask, TASK_ID);

        // Then
        verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
        verify(queueDAO, atLeastOnce()).postpone(QUEUE_NAME, TASK_ID, anyInt(), anyLong());
        verify(executionDAOFacade).getTaskModel(TASK_ID);
        verify(executionDAOFacade).updateTask(task);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(metadataDAO);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_TaskIsInProgressAndIsAsyncComplete() {
        // Given
        when(systemTask.isAsyncComplete(any())).thenReturn(true);

        // When
        systemUnderTest.execute(systemTask, TASK_ID);

        // Then
        verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
        verify(queueDAO, never()).postpone(QUEUE_NAME, TASK_ID, anyInt(), anyLong());
        verify(executionDAOFacade).getTaskModel(TASK_ID);
        verify(executionDAOFacade).updateTask(task);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(metadataDAO);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_WorkflowIsTerminal() {
        // Given
        workflow.setStatus(ExecutionStatus.COMPLETED);

        // When
        systemUnderTest.execute(systemTask, TASK_ID);

        // Then
        verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
        verify(queueDAO, never()).postpone(QUEUE_NAME, TASK_ID, anyInt(), anyLong());
        verify(executionDAOFacade).getTaskModel(TASK_ID);
        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
        verifyNoMoreInteractions(executionDAOFacade);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(metadataDAO);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_ConcurrentLimitIsExceeded() {
        // Given
        workflow.setStatus(ExecutionStatus.RUNNING);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        // When
        systemUnderTest.execute(systemTask, TASK_ID);

        // Then
        verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
        verify(queueDAO, never()).postpone(QUEUE_NAME, TASK_ID, anyInt(), anyLong());
        verify(executionDAOFacade).getTaskModel(TASK_ID);
        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, anyInt(), QUEUE_TASK_MESSAGE_POSTPONE_SECS);
        verifyNoMoreInteractions(executionDAOFacade);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(metadataDAO);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_RateLimitIsExceeded() {
        // Given
        workflow.setStatus(ExecutionStatus.RUNNING);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        // When
        systemUnderTest.execute(systemTask, TASK_ID);

        // Then
        verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
        verify(queueDAO, never()).postpone(QUEUE_NAME, TASK_ID, anyInt(), anyLong());
        verify(executionDAOFacade).getTaskModel(TASK_ID);
        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, anyInt(), QUEUE_TASK_MESSAGE_POSTPONE_SECS);
        verifyNoMoreInteractions(executionDAOFacade);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(metadataDAO);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_TaskIsPostponed() {
        // Given
        workflow.setStatus(ExecutionStatus.RUNNING);

        // When
        systemUnderTest.execute(systemTask, TASK_ID);

        // Then
        verify(queueDAO, atLeastOnce()).remove(QUEUE_NAME, TASK_ID);
        verify(queueDAO, atLeastOnce()).postpone(QUEUE_NAME, TASK_ID, anyInt(), anyLong());
        verify(executionDAOFacade).getTaskModel(TASK_ID);
        verify(executionDAOFacade).updateTask(task);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(metadataDAO);
        verifyNoMoreInteractions(workflowExecutor);

        // Verify that the task was posted to the queue
        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, anyInt(), QUEUE_TASK_MESSAGE_POSTPONE_SECS);
        ArgumentCaptor<QueueMessage> queueMessageArgumentCaptor = ArgumentCaptor.forClass(QueueMessage.class);
        QueueMessage queueMessage = queueMessageArgumentCaptor.getValue();
        assertEquals(QueueMessage.Type.TASK, queueMessage.getType());
        assertEquals(TASK_ID, queueMessage.getId());
        assertEquals(TASK_TYPE, queueMessage.getTaskType());
        assertEquals(WORKFLOW_INSTANCE_ID, queueMessage.getWorkflowInstanceId());
        assertEquals(WORKFLOW_ID, queueMessage.getWorkflowId());
        assertEquals(QUEUE_NAME, queueMessage.getQueueName());
        assertEquals(QUEUE_TASK_MESSAGE_POSTPONE_SECS, queueMessage.getPriority());
        assertEquals(QUEUE_TASK_MESSAGE_POSTPONE_SECS, queueMessage.getDelay());
        assertEquals(System.currentTimeMillis(), queueMessage.getTimestamp());
    }

    @Test
    public void testExecute_WorkflowIsEvaluated() {
        // Given
        workflow.setStatus(ExecutionStatus.RUNNING);

        // When
        systemUnderTest.execute(systemTask, TASK_ID);

        // Then
        verify(workflowExecutor).decide(WORKFLOW_ID);
    }
}