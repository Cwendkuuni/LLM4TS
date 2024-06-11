package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.util.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.utils.TestUtils;

public class AsyncSystemTaskExecutorTest {

    private static final long SYSTEM_TASK_WORKER_CALLBACK_DURATION = 100;
    private static final long TASK_EXECUTION_POSTPONE_DURATION = 10;
    private static final String QUEUE_NAME = "testQueue";
    private static final String TASK_ID = "testTaskId";
    private static final String WORKFLOW_ID = "testWorkflowId";
    private static final String TASK_TYPE = "testTaskType";
    private static final String WORKFLOW_DEF_NAME = "testWorkflowDefName";
    private static final String TASK_DEF_NAME = "testTaskDefName";
    private static final String TASK_INSTANCE_ID = "testTaskInstanceId";
    private static final TaskModel TASK =
            new TaskModel()
                    .withTaskId(TASK_ID)
                    .withTaskDefName(TASK_DEF_NAME)
                    .withTaskType(TASK_TYPE)
                    .withQueueName(QUEUE_NAME)
                    .withStatus(TaskModel.Status.SCHEDULED)
                    .withWorkflowInstanceId(WORKFLOW_ID)
                    .withWorkflowPriority(1);

    private AsyncSystemTaskExecutor executor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    @Captor private ArgumentCaptor<TaskModel> taskModelCaptor;

    @Before
    public void setup() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);

        when(conductorProperties.getSystemTaskWorkerCallbackDuration())
                .thenReturn(Duration.ofSeconds(SYSTEM_TASK_WORKER_CALLBACK_DURATION));
        when(conductorProperties.getTaskExecutionPostponeDuration())
                .thenReturn(Duration.ofSeconds(TASK_EXECUTION_POSTPONE_DURATION));

        executor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade,
                        queueDAO,
                        metadataDAO,
                        conductorProperties,
                        workflowExecutor);
    }

    @After
    public void teardown() {
        TestUtils.deleteAllFromDatabase();
    }

    @Test
    public void testExecute_taskIsNotFound() {
        when(executionDAOFacade.getTaskModel(eq(TASK_ID))).thenReturn(null);

        executor.execute(mock(WorkflowSystemTask.class), TASK_ID);

        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
        assertNull(executionDAOFacade.getTaskModel(TASK_ID));
    }

    @Test
    public void testExecute_taskIsCompleted() {
        TaskModel task = new TaskModel().withTaskId(TASK_ID).withStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(eq(TASK_ID))).thenReturn(task);

        executor.execute(mock(WorkflowSystemTask.class), TASK_ID);

        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
        assertSame(TaskModel.Status.COMPLETED, task.getStatus());
    }

    @Test
    public void testExecute_taskIsTerminated() {
        TaskModel task = new TaskModel().withTaskId(TASK_ID).withStatus(TaskModel.Status.TERMINATED);
        when(executionDAOFacade.getTaskModel(eq(TASK_ID))).thenReturn(task);

        executor.execute(mock(WorkflowSystemTask.class), TASK_ID);

        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
        assertSame(TaskModel.Status.TERMINATED, task.getStatus());
    }

    @Test
    public void testExecute_taskIsScheduled() {
        when(executionDAOFacade.getTaskModel(eq(TASK_ID)))
                .thenReturn(new TaskModel().withTaskId(TASK_ID).withStatus(TaskModel.Status.SCHEDULED));

        when(executionDAOFacade.exceedsInProgressLimit(taskModelCaptor.capture()))
                .thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any()))
                .thenReturn(false);

        executor.execute(mock(WorkflowSystemTask.class), TASK_ID);

        TaskModel capturedTask = taskModelCaptor.getValue();
        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
        assertSame(TaskModel.Status.SCHEDULED, capturedTask.getStatus());
    }

    @Test
    public void testExecute_taskExecutionLimitExceeded() {
        when(executionDAOFacade.getTaskModel(eq(TASK_ID)))
                .thenReturn(new TaskModel().withTaskId(TASK_ID).withStatus(TaskModel.Status.SCHEDULED));

        when(executionDAOFacade.exceedsInProgressLimit(taskModelCaptor.capture()))
                .thenReturn(true);

        executor.execute(mock(WorkflowSystemTask.class), TASK_ID);

        TaskModel capturedTask = taskModelCaptor.getValue();
        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 1, TASK_EXECUTION_POSTPONE_DURATION);
        assertSame(TaskModel.Status.SCHEDULED, capturedTask.getStatus());
    }

    @Test
    public void testExecute_rateLimitPerFrequencyExceeded() {
        when(executionDAOFacade.getTaskModel(eq(TASK_ID)))
                .thenReturn(new TaskModel().withTaskId(TASK_ID).withStatus(TaskModel.Status.SCHEDULED));

        WorkflowModel workflow =
                new WorkflowModel()
                        .withId(WORKFLOW_ID)
                        .withDefinitionName(WORKFLOW_DEF_NAME)
                        .withTaskDefName(TASK_DEF_NAME)
                        .withRateLimitPerFrequency(1);
        when(executionDAOFacade.getWorkflowModel(eq(WORKFLOW_ID), eq(false)))
                .thenReturn(workflow);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModelCaptor.capture(), any()))
                .thenReturn(true);

        executor.execute(mock(WorkflowSystemTask.class), TASK_ID);

        TaskModel capturedTask = taskModelCaptor.getValue();
        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 1, TASK_EXECUTION_POSTPONE_DURATION);
        assertSame(TaskModel.Status.SCHEDULED, capturedTask.getStatus());
    }

    @Test
    public void testExecute_workflowCompleted() {
        when(executionDAOFacade.getTaskModel(eq(TASK_ID)))
                .thenReturn(new TaskModel().withTaskId(TASK_ID).withStatus(TaskModel.Status.SCHEDULED));

        WorkflowModel workflow =
                new WorkflowModel()
                        .withId(WORKFLOW_ID)
                        .withDefinitionName(WORKFLOW_DEF_NAME)
                        .withStatus(ExecutionStatus.COMPLETED.toString())
                        .withTaskDefName(TASK_DEF_NAME);
        when(executionDAOFacade.getWorkflowModel(eq(WORKFLOW_ID), eq(false)))
                .thenReturn(workflow);

        executor.execute(mock(WorkflowSystemTask.class), TASK_ID);

        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
    }

    @Test
    public void testExecute_taskExecutionCompleted() throws InterruptedException {
        WorkflowModel workflow =
                new WorkflowModel()
                        .withId(WORKFLOW_ID)
                        .withDefinitionName(WORKFLOW_DEF_NAME)
                        .withStatus(ExecutionStatus.RUNNING.toString())
                        .withTaskDefName(TASK_DEF_NAME)
                        .withInstances(new WorkflowModel.Instance()
                                .withInstanceId(TASK_INSTANCE_ID)
                                .withStatus(ExecutionStatus.RUNNING.toString())
                                .withTaskId(TASK_ID)
                                .withTaskType(TASK_TYPE));
        when(executionDAOFacade.getTaskModel(eq(TASK_ID)))
                .thenReturn(new TaskModel().withTaskId(TASK_ID).withStatus(TaskModel.Status.SCHEDULED));
        when(executionDAOFacade.getWorkflowModel(eq(WORKFLOW_ID), eq(false)))
                .thenReturn(workflow);

        when(workflowExecutor.decide(eq(WORKFLOW_ID)))
                .thenReturn(Optional.of(ExecutionStatus.COMPLETED));

        doReturn(true)
                .when(systemTask)
                .isAsyncComplete(taskModelCaptor.capture());

        executor.execute(mock(WorkflowSystemTask.class), TASK_ID);

        TaskModel capturedTask = taskModelCaptor.getValue();
        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
        assertFalse(capturedTask.isAsyncComplete());
    }

    @Test
    public void testExecute_taskExecutionFailed() throws InterruptedException {
        WorkflowModel workflow =
                new WorkflowModel()
                        .withId(WORKFLOW_ID)
                        .withDefinitionName(WORKFLOW_DEF_NAME)
                        .withStatus(ExecutionStatus.RUNNING.toString())
                        .withTaskDefName(TASK_DEF_NAME)
                        .withInstances(new WorkflowModel.Instance()
                                .withInstanceId(TASK_INSTANCE_ID)
                                .withStatus(ExecutionStatus.FAILED.toString())
                                .withTaskId(TASK_ID)
                                .withTaskType(TASK_TYPE));
        when(executionDAOFacade.getTaskModel(eq(TASK_ID)))
                .thenReturn(new TaskModel().withTaskId(TASK_ID).withStatus(TaskModel.Status.SCHEDULED));
        when(executionDAOFacade.getWorkflowModel(eq(WORKFLOW_ID), eq(false)))
                .thenReturn(workflow);

        when(workflowExecutor.decide(eq(WORKFLOW_ID)))
                .thenReturn(Optional.empty());

        executor.execute(mock(WorkflowSystemTask.class), TASK_ID);

        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
    }

    @Test
    public void testPostponeQuietly() {
        executor.postponeQuietly(QUEUE_NAME, TASK);

        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 1, TASK_EXECUTION_POSTPONE_DURATION);
    }

    @Test
    public void testLoadTaskQuietly_taskIsNotFound() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(null);

        TaskModel taskModel = executor.loadTaskQuietly(TASK_ID);

        assertNull(taskModel);
    }

    @Test
    public void testLoadTaskQuietly_taskIsLoaded() {
        TaskModel taskModel = new TaskModel().withTaskId(TASK_ID).withStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);

        TaskModel loadedTaskModel = executor.loadTaskQuietly(TASK_ID);

        assertSame(taskModel, loadedTaskModel);
    }
}