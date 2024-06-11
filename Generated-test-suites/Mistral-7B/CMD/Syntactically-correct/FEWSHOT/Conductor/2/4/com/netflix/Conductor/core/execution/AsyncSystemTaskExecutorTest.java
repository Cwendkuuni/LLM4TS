package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.execution.ExecutionDAOFacadeImpl;
import com.netflix.conductor.dao.metadata.MetadataDAOImpl;
import com.netflix.conductor.dao.queue.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModelImpl;
import com.netflix.conductor.model.execution.WorkflowSystemTaskImpl;
import com.netflix.conductor.worker.workflow.WorkflowExecutor;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    private static final Instant NOW = Instant.now();

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

    @Mock
    private WorkflowModelImpl workflowModel;

    @Mock
    private TaskModel taskModel;

    @Mock
    private WorkflowSystemTaskImpl systemTask;

    @Mock
    private QueueDAOImpl queueDAOInstance;

    @Mock
    private ExecutionDAOFacadeImpl executionDAOFacadeInstance;

    @Mock
    private MetadataDAOImpl metadataDAOInstance;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(1));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(2));
        when(workflowExecutor.decide(eq(UUID.randomUUID().toString()))).thenReturn(true);
        when(queueDAOInstance.remove(eq("queueName"), eq("taskId"))).thenAnswer(invocation -> true);
        when(executionDAOFacadeInstance.getTaskModel(eq("taskId"))).thenReturn(taskModel);
        when(executionDAOFacadeInstance.getWorkflowModel(eq("workflowId"), eq(false))).thenReturn(workflowModel);
        when(metadataDAOInstance.getTaskDef(eq("taskDefName"))).thenReturn(mock(Object.class));
        when(queueDAOInstance.postpone(
                eq("queueName"),
                eq("taskId"),
                eq(1),
                eq(Duration.ofSeconds(3).getSeconds()))).thenAnswer(invocation -> true);
        when(queueDAOInstance.getNextMessage(eq("queueName"))).thenAnswer(invocation -> Optional.of(taskModel));
        when(queueDAOInstance.remove(eq("queueName"), eq("taskId"))).thenAnswer(invocation -> true);
        when(queueDAOInstance.size(eq("queueName"))).thenAnswer(invocation -> 1);
        when(queueDAOInstance.getQueueName(eq(taskModel))).thenReturn("queueName");
        when(queueDAOInstance.remove(eq("queueName"), anyString())).thenAnswer(invocation -> true);
        when(executionDAOFacadeInstance.updateTask(eq(taskModel))).thenAnswer(invocation -> taskModel);
        when(executionDAOFacadeInstance.exceedsInProgressLimit(eq(taskModel))).thenAnswer(invocation -> false);
        when(executionDAOFacadeInstance.exceedsRateLimitPerFrequency(
                eq(taskModel), any(Object.class))).thenAnswer(invocation -> false);
        when(executionDAOFacadeInstance.getWorkflowModel(
                eq("workflowId"), eq(true))).thenAnswer(invocation -> workflowModel);
        when(workflowModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(workflowModel.getWorkflowInstanceId()).thenReturn("workflowId");
        when(systemTask.isAsyncComplete(eq(taskModel))).thenAnswer(invocation -> true);
        when(systemTask.start(eq(workflowModel), eq(taskModel), eq(workflowExecutor))).thenAnswer(invocation -> {});
        when(systemTask.execute(eq(workflowModel), eq(taskModel), eq(workflowExecutor))).thenAnswer(invocation -> {});
        when(systemTask.getEvaluationOffset(eq(taskModel), eq(Duration.ofSeconds(1)))).thenAnswer(invocation -> Optional.of(Duration.ofSeconds(1).toMillis()));
        when(monitors.recordQueueWaitTime(eq("queueName"), eq(Duration.ofSeconds(1)))).thenAnswer(invocation -> {});

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacadeInstance,
                queueDAOInstance,
                metadataDAOInstance,
                conductorProperties,
                workflowExecutor);
    }

    @After
    public void tearDown() {
        verify(monitors).recordError(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask");
    }

    @Test
    public void testExecute_whenTaskIsNotInQueue_thenDoNothing() {
        givenTaskIsNotInQueue();

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAOInstance, never()).remove(eq("queueName"), eq("taskId"));
        verify(queueDAOInstance, never()).postpone(eq("queueName"), eq("taskId"), anyInt(), anyInt());
        verify(executionDAOFacadeInstance, never()).updateTask(eq(taskModel));
    }

    @Test
    public void testExecute_whenTaskIsInQueueButNotTerminal_thenPostpone() {
        givenTaskIsInQueueButNotTerminal();

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAOInstance, times(1)).postpone(eq("queueName"), eq("taskId"), anyInt(), anyInt());
        verify(queueDAOInstance, times(1)).getNextMessage(eq("queueName"));
        verify(executionDAOFacadeInstance, never()).updateTask(eq(taskModel));
    }

    @Test
    public void testExecute_whenTaskIsInQueueAndTerminal_thenRemoveFromQueue() {
        givenTaskIsInQueueAndTerminal();

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAOInstance, times(1)).remove(eq("queueName"), eq("taskId"));
        verify(queueDAOInstance, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacadeInstance, never()).updateTask(eq(taskModel));
    }

    @Test
    public void testExecute_whenTaskIsScheduled_thenExecute() {
        givenTaskIsScheduled();

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(systemTask, times(1)).start(eq(workflowModel), eq(taskModel), eq(workflowExecutor));
        verify(queueDAOInstance, never()).remove(eq("queueName"), eq("taskId"));
        verify(queueDAOInstance, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacadeInstance, never()).updateTask(eq(taskModel));
    }

    @Test
    public void testExecute_whenTaskIsInProgress_thenExecute() {
        givenTaskIsInProgress();

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(systemTask, times(1)).execute(eq(workflowModel), eq(taskModel), eq(workflowExecutor));
        verify(queueDAOInstance, never()).remove(eq("queueName"), eq("taskId"));
        verify(queueDAOInstance, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacadeInstance, never()).updateTask(eq(taskModel));
    }

    @Test
    public void testExecute_whenTaskIsTerminal_thenRemoveFromQueue() {
        givenTaskIsTerminal();

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAOInstance, times(1)).remove(eq("queueName"), eq("taskId"));
        verify(queueDAOInstance, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacadeInstance, never()).updateTask(eq(taskModel));
    }

    @Test
    public void testExecute_whenTaskIsAsyncCompleteButNotScheduled_thenRemoveFromQueue() {
        givenTaskIsAsyncCompleteButNotScheduled();

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAOInstance, times(1)).remove(eq("queueName"), eq("taskId"));
        verify(queueDAOInstance, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacadeInstance, never()).updateTask(eq(taskModel));
    }

    @Test
    public void testExecute_whenTaskIsAsyncCompleteAndScheduled_thenDoNothing() {
        givenTaskIsAsyncCompleteAndScheduled();

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAOInstance, never()).remove(eq("queueName"), eq("taskId"));
        verify(queueDAOInstance, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacadeInstance, never()).updateTask(eq(taskModel));
    }

    @Test
    public void testPostponeQuietly_whenTaskExists_thenPostpone() {
        givenTaskExists();

        asyncSystemTaskExecutor.postponeQuietly("queueName", taskModel);

        verify(queueDAOInstance, times(1)).postpone(eq("queueName"), eq("taskId"), anyInt(), anyInt());
    }

    @Test
    public void testPostponeQuietly_whenTaskDoesNotExist_thenDoNothing() {
        givenTaskDoesNotExist();

        asyncSystemTaskExecutor.postponeQuietly("queueName", taskModel);

        verify(queueDAOInstance, never()).postpone(eq("queueName"), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testLoadTaskQuietly_whenTaskExists_thenReturnTaskModel() {
        givenTaskExists();

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("taskId");

        verify(executionDAOFacadeInstance, times(1)).getTaskModel(eq("taskId"));
        assertEquals(taskModel, result);
    }

    @Test
    public void testLoadTaskQuietly_whenTaskDoesNotExist_thenReturnNull() {
        givenTaskDoesNotExist();

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("taskId");

        verify(executionDAOFacadeInstance, never()).getTaskModel(eq("taskId"));
        assertNull(result);
    }

    private void givenTaskIsNotInQueue() {
        when(queueDAOInstance.getNextMessage(eq("queueName"))).thenReturn(Optional.empty());
    }

    private void givenTaskIsInQueueButNotTerminal() {
        when(queueDAOInstance.getNextMessage(eq("queueName"))).thenReturn(Optional.of(taskModel));
        when(workflowModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
    }

    private void givenTaskIsInQueueAndTerminal() {
        when(queueDAOInstance.getNextMessage(eq("queueName"))).thenReturn(Optional.of(taskModel));
        when(workflowModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
    }

    private void givenTaskIsScheduled() {
        when(queueDAOInstance.getNextMessage(eq("queueName"))).thenReturn(Optional.of(taskModel));
        when(workflowModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
    }

    private void givenTaskIsInProgress() {
        when(queueDAOInstance.getNextMessage(eq("queueName"))).thenReturn(Optional.of(taskModel));
        when(workflowModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
    }

    private void givenTaskIsTerminal() {
        when(queueDAOInstance.getNextMessage(eq("queueName"))).thenReturn(Optional.of(taskModel));
        when(workflowModel.getStatus()).thenReturn(TaskModel.Status.TERMINATED_NORMAL);
    }

    private void givenTaskIsAsyncCompleteButNotScheduled() {
        when(queueDAOInstance.getNextMessage(eq("queueName"))).thenReturn(Optional.of(taskModel));
        when(workflowModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(taskModel.isAsyncComplete()).thenReturn(true);
    }

    private void givenTaskIsAsyncCompleteAndScheduled() {
        when(queueDAOInstance.getNextMessage(eq("queueName"))).thenReturn(Optional.of(taskModel));
        when(workflowModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(taskModel.isAsyncComplete()).thenReturn(true);
    }

    private void givenTaskExists() {
        when(queueDAOInstance.getNextMessage(eq("queueName"))).thenReturn(Optional.of(taskModel));
    }

    private void givenTaskDoesNotExist() {
        when(queueDAOInstance.getNextMessage(eq("queueName"))).thenReturn(Optional.empty());
    }
}