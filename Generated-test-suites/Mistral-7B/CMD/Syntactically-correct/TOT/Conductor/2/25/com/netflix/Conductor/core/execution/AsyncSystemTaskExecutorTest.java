package com.netflix.conductor.core.execution;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.Queue;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.WorkflowModelImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

    @Captor private ArgumentCaptor<TaskModel> taskCaptor;
    @Captor private ArgumentCaptor<Queue> queueCaptor;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowExecutor workflowExecutor;
    private ConductorProperties conductorProperties;

    @Before
    public void initializeMocks() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        conductorProperties = mock(ConductorProperties.class);

        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade,
                        queueDAO,
                        metadataDAO,
                        conductorProperties,
                        workflowExecutor);

        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(new Duration(1000));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(new Duration(1000));
    }

    @Test
    public void testExecute_taskIsCompleted_workflowIsCompleted() {
        TaskModel task = new TaskModel();
        task.setTaskId("task1");
        task.setTaskType("TaskType1");
        task.setWorkflowInstanceId("workflow1");
        task.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setId("workflow1");
        workflow.setStatus(WorkflowModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(
                mock(WorkflowSystemTask.class), task.getTaskId());

        // Verify that the task status is updated to COMPLETED and removed from the queue
        verifyTaskIsUpdatedToCompleted(task);
        verifyTaskIsRemovedFromQueue(task);

        // Verify that the workflow status is updated to COMPLETED
        verifyWorkflowStatusIsUpdatedToCompleted(workflow);
    }

    @Test
    public void testExecute_taskIsPostponed_queueSizeLimitIsReached() {
        TaskModel task = new TaskModel();
        task.setTaskId("task1");
        task.setTaskType("TaskType1");
        task.setWorkflowInstanceId("workflow1");
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setInProgressLimit(1);

        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setId("workflow1");
        workflow.setStatus(WorkflowModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false)).thenReturn(workflow);
        when(queueDAO.getQueueSize(QueueUtils.getQueueName(task))).thenReturn(1);

        asyncSystemTaskExecutor.execute(
                mock(WorkflowSystemTask.class), task.getTaskId());

        // Verify that the task is postponed
        verifyTaskIsPostponed(task);
    }

    @Test
    public void testExecute_taskIsPostponed_rateLimitIsReached() {
        TaskModel task = new TaskModel();
        task.setTaskId("task1");
        task.setTaskType("TaskType1");
        task.setWorkflowInstanceId("workflow1");
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setRateLimitPerFrequency(1);

        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setId("workflow1");
        workflow.setStatus(WorkflowModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(
                mock(WorkflowSystemTask.class), task.getTaskId());

        // Verify that the task is postponed
        verifyTaskIsPostponed(task);
    }

    @Test
    public void testExecute_taskIsFailed_workflowIsNotCompleted() {
        TaskModel task = new TaskModel();
        task.setTaskId("task1");
        task.setTaskType("TaskType1");
        task.setWorkflowInstanceId("workflow1");
        task.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setId("workflow1");
        workflow.setStatus(WorkflowModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false)).thenReturn(workflow);
        when(workflowExecutor.decide(workflow.getId())).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.execute(
                mock(WorkflowSystemTask.class), task.getTaskId());

        // Verify that the task status is not updated and remains in the queue
        verifyTaskStatusIsNotUpdated(task);
        verifyTaskIsNotRemovedFromQueue(task);

        // Verify that the workflow status is not updated
        verifyWorkflowStatusIsNotUpdated(workflow);
    }

    private void verifyTaskIsUpdatedToCompleted(TaskModel task) {
        verify(executionDAOFacade).updateTask(taskCaptor.capture());
        TaskModel capturedTask = taskCaptor.getValue();
        assertThat(capturedTask.getStatus(), is(TaskModel.Status.COMPLETED));
    }

    private void verifyTaskIsRemovedFromQueue(TaskModel task) {
        verify(queueDAO).remove(queueCaptor.capture());
        Queue capturedQueue = queueCaptor.getValue();
        assertThat(capturedQueue.getName(), is(QueueUtils.getQueueName(task)));
        assertThat(capturedQueue.getTaskId(), is(task.getTaskId()));
    }

    private void verifyWorkflowStatusIsUpdatedToCompleted(WorkflowModel workflow) {
        verify(executionDAOFacade).updateWorkflowModel(workflowCaptor.capture());
        WorkflowModel capturedWorkflow = workflowCaptor.getValue();
        assertThat(capturedWorkflow.getStatus(), is(WorkflowModel.Status.COMPLETED));
    }

    private void verifyTaskIsPostponed(TaskModel task) {
        verify(queueDAO).postpone(queueCaptor.capture(), taskCaptor.capture(), eq(task.getWorkflowPriority()), eq(queueTaskMessagePostponeSecs));
        Queue capturedQueue = queueCaptor.getValue();
        TaskModel capturedTask = taskCaptor.getValue();
        assertThat(capturedQueue.getName(), is(QueueUtils.getQueueName(capturedTask)));
        assertThat(capturedTask.getTaskId(), is(task.getTaskId()));
    }

    private void verifyTaskStatusIsNotUpdated(TaskModel task) {
        verify(executionDAOFacade).updateTask(taskCaptor.capture());
        TaskModel capturedTask = taskCaptor.getValue();
        assertThat(capturedTask.getStatus(), is(TaskModel.Status.SCHEDULED));
    }

    private void verifyTaskIsNotRemovedFromQueue(TaskModel task) {
        verify(queueDAO, never()).remove(queueCaptor.capture(), taskCaptor.capture());
    }

    private void verifyWorkflowStatusIsNotUpdated(WorkflowModel workflow) {
        verify(executionDAOFacade, never()).updateWorkflowModel(workflowCaptor.capture());
    }

    private ArgumentCaptor<WorkflowModel> workflowCaptor = argumentCaptor(WorkflowModel.class);
}

class Duration {
    private final long seconds;

    public Duration(long seconds) {
        this.seconds = seconds;
    }
}