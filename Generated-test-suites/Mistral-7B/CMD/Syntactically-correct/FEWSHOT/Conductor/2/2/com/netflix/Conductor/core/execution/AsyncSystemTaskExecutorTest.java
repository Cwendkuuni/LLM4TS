package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOResponse;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.model.WorkflowModel.WorkflowInstanceStatuses;

public class AsyncSystemTaskExecutorTest {

    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = new ConductorProperties();
        workflowExecutor = mock(WorkflowExecutor.class);

        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(getTaskModel());
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(getWorkflowModel());
        when(metadataDAO.getTaskDef("taskDefName")).thenReturn(getTaskDef());

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute_TaskNotFound() {
        String taskId = "taskId";

        asyncSystemTaskExecutor.execute(getSystemTask(), taskId);

        verify(queueDAO, never()).remove("queueName", taskId);
        verify(executionDAOFacade, never()).updateTask(any());
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        String taskId = "taskId";
        TaskModel task = getTaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(getSystemTask(), taskId);

        verify(queueDAO, times(1)).remove("queueName", taskId);
        verify(executionDAOFacade, times(1)).updateTask(task);
    }

    @Test
    public void testExecute_TaskInProgress_AsyncComplete() {
        String taskId = "taskId";
        TaskModel task = getTaskModel();

        WorkflowSystemTask systemTask = getSystemTask();
        systemTask.setAsyncComplete(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove("queueName", taskId);
        verify(executionDAOFacade, times(1)).updateTask(task);
    }

    @Test
    public void testExecute_TaskInProgress_NotAsyncComplete() {
        String taskId = "taskId";
        TaskModel task = getTaskModel();

        WorkflowSystemTask systemTask = getSystemTask();
        systemTask.setAsyncComplete(false);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone("queueName", taskId, 0, systemTaskCallbackTime.toSeconds());
        verify(executionDAOFacade, times(1)).updateTask(task);
    }

    @Test
    public void testExecute_TaskScheduled_ExceedsInProgressLimit() {
        String taskId = "taskId";
        TaskModel task = getTaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(getSystemTask(), taskId);

        verify(queueDAO, times(1)).postpone("queueName", taskId, 0, queueTaskMessagePostponeSecs);
        verify(executionDAOFacade, never()).updateTask(task);
    }

    @Test
    public void testExecute_TaskScheduled_ExceedsRateLimitPerFrequency() {
        String taskId = "taskId";
        TaskModel task = getTaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(true);

        asyncSystemTaskExecutor.execute(getSystemTask(), taskId);

        verify(queueDAO, times(1)).postpone("queueName", taskId, 0, queueTaskMessagePostponeSecs);
        verify(executionDAOFacade, never()).updateTask(task);
    }

    @Test
    public void testExecute_WorkflowCompleted() {
        String taskId = "taskId";
        String workflowId = "workflowId";
        TaskModel task = getTaskModel();

        WorkflowModel workflow = getWorkflowModel();
        workflow.setStatus(WorkflowInstanceStatuses.COMPLETED);

        asyncSystemTaskExecutor.execute(getSystemTask(), taskId);

        verify(queueDAO, times(1)).remove("queueName", taskId);
        verify(executionDAOFacade, times(1)).updateTask(task);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        TaskModel task = getTaskModel();

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(queueName, task.getTaskId(), 0, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_TaskNotFound() {
        String taskId = "taskId";

        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(task);
    }

    private TaskModel getTaskModel() {
        TaskModel task = new TaskModel();
        task.setTaskDefName("taskDefName");
        task.setTaskId("taskId");
        task.setQueueName(QueueUtils.getQueueName("workflowInstanceId"));
        task.setWorkflowInstanceId("workflowInstanceId");
        task.setStatus(TaskModel.Status.SCHEDULED);
        return task;
    }

    private WorkflowModel getWorkflowModel() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId("workflowId");
        workflow.setStatus(WorkflowInstanceStatus.RUNNING);
        return workflow;
    }

    private WorkflowSystemTask getSystemTask() {
        return Mockito.spy(new WorkflowSystemTask("taskDefName", Mockito.mock(WorkflowModel.class)) {
            @Override
            public boolean isAsyncComplete(TaskModel task) {
                return false;
            }

            @Override
            public void start(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                // no-op
            }

            @Override
            public void execute(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                // no-op
            }
        });
    }

    private TaskDef getTaskDef() {
        return new TaskDef();
    }

    private ConductorProperties getConductorProperties() {
        ConductorProperties conductorProperties = new ConductorProperties();
        conductorProperties.setSystemTaskWorkerCallbackDuration(Mockito.mock(Duration.class));
        conductorProperties.setTaskExecutionPostponeDuration(Mockito.mock(Duration.class));
        return conductorProperties;
    }

    private Duration queueTaskMessagePostponeSecs = Duration.ofSeconds(1);
    private Duration systemTaskCallbackTime = Duration.ofSeconds(2);

}