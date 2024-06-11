package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueNameGenerator;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

public class AsyncSystemTaskExecutorTest {
    private static final String WORKFLOW_ID = "123";
    private static final String TASK_ID = "taskId";
    private static final String TASK_QUEUE = "taskQueue";
    private static final String TASK_TYPE = "taskType";
    private static final Status WORKFLOW_STATUS = Status.IN_PROGRESS;
    private static final Status TASK_STATUS = Status.SCHEDULED;
    private static final long SYSTEM_TASK_CALLBACK_TIME = 1L;

    @Captor
    private ArgumentCaptor<TaskModel> taskModelArgumentCaptor;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ConductorProperties conductorProperties;
    private ExecutionDAOFacade executionDAOFacade;
    private MetadataDAO metadataDAO;
    private QueueDAO queueDAO;
    private QueueNameGenerator queueNameGenerator;
    private Monitors monitors;
    private WorkflowExecutor workflowExecutor;

    @Before
    public void setUp() {
        conductorProperties = mock(ConductorProperties.class);
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        metadataDAO = mock(MetadataDAO.class);
        queueDAO = mock(QueueDAO.class);
        queueNameGenerator = mock(QueueNameGenerator.class);
        monitors = mock(Monitors.class);
        workflowExecutor = mock(WorkflowExecutor.class);

        MockitoAnnotations.initMocks(this);

        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(
                new Duration(SYSTEM_TASK_CALLBACK_TIME, TimeUnit.SECONDS));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(
                new Duration(queueTaskMessagePostponeSecs, TimeUnit.SECONDS));

        when(queueNameGenerator.getQueueName(any(TaskModel.class))).thenReturn(TASK_QUEUE);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute_taskIsCompleted() {
        WorkflowModel workflowModel = createWorkflowModel();
        TaskModel task = createTaskModel(WORKFLOW_ID, TASK_STATUS);

        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(createSystemTask(), task.getTaskId());

        verifyTaskIsRemovedFromQueue(task);
        verifyTaskStatusIsUpdated(task, Status.CANCELED);
    }

    @Test
    public void testExecute_taskIsTerminal() {
        WorkflowModel workflowModel = createWorkflowModel();
        TaskModel task = createTaskModel(WORKFLOW_ID, new Status("COMPLETED"));

        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(createSystemTask(), task.getTaskId());

        verifyTaskIsRemovedFromQueue(task);
        verifyTaskStatusIsUpdated(task, Status.COMPLETED);
    }

    @Test
    public void testExecute_taskIsInProgress() {
        WorkflowModel workflowModel = createWorkflowModel();
        TaskModel task = createTaskModel(WORKFLOW_ID, TASK_STATUS);

        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflowModel);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(false);

        asyncSystemTaskExecutor.execute(createSystemTask(), task.getTaskId());

        verifyTaskIsPostponedInQueue(task);
    }

    @Test
    public void testExecute_taskIsScheduled() {
        TaskModel task = createTaskModel(WORKFLOW_ID, TASK_STATUS);

        asyncSystemTaskExecutor.execute(createSystemTask(), task.getTaskId());

        verifyTaskIsPostponedInQueue(task);
    }

    @Test
    public void testExecute_exceedsInProgressLimit() {
        TaskModel task = createTaskModel(WORKFLOW_ID, TASK_STATUS);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(createSystemTask(), task.getTaskId());

        verifyTaskIsPostponedInQueue(task);
    }

    @Test
    public void testExecute_exceedsRateLimitPerFrequency() {
        TaskModel task = createTaskModel(WORKFLOW_ID, TASK_STATUS);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(true);

        asyncSystemTaskExecutor.execute(createSystemTask(), task.getTaskId());

        verifyTaskIsPostponedInQueue(task);
    }

    @Test
    public void testExecute_couldNotFindTask() {
        asyncSystemTaskExecutor.execute(createSystemTask(), "non-existent_task_id");

        verifyTaskIsNotRemovedFromQueue("non-existent_task_id");
        verifyNoTaskStatusIsUpdated("non-existent_task_id");
    }

    private WorkflowModel createWorkflowModel() {
        return new WorkflowModel(WORKFLOW_ID, WORKFLOW_STATUS);
    }

    private TaskModel createTaskModel(String workflowId, Status status) {
        return new TaskModel(TASK_ID, workflowId, status, null, null, null, null, null);
    }

    private WorkflowSystemTask createSystemTask() {
        return new WorkflowSystemTask() {
            @Override
            public String getTaskType() {
                return TASK_TYPE;
            }

            @Override
            public boolean isTaskRetrievalRequired() {
                return false;
            }

            @Override
            public boolean isAsyncComplete(TaskModel task) {
                return false;
            }

            @Override
            public void start(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                // No-op
            }

            @Override
            public void execute(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                // No-op
            }
        };
    }

    private void verifyTaskIsRemovedFromQueue(TaskModel task) {
        verify(queueDAO).remove(TASK_QUEUE, task.getTaskId());
    }

    private void verifyTaskStatusIsUpdated(TaskModel task, Status expectedStatus) {
        verify(executionDAOFacade).updateTask(taskModelArgumentCaptor.capture());
        TaskModel updatedTask = taskModelArgumentCaptor.getValue();

        assertSame(task, updatedTask);
        assertEquals(expectedStatus, updatedTask.getStatus());
    }

    private void verifyTaskIsPostponedInQueue(TaskModel task) {
        verify(queueDAO).postpone(TASK_QUEUE, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    private void verifyTaskIsNotRemovedFromQueue(String taskId) {
        verify(queueDAO, never()).remove(TASK_QUEUE, taskId);
    }

    private void verifyNoTaskStatusIsUpdated(String taskId) {
        verify(executionDAOFacade, never()).updateTask(taskModelArgumentCaptor.capture());
    }
}