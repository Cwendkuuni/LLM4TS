package com.netflix.conductor.core.reconciliation;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Async;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dao.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metrics.Monitors;
import com.netflix.conductor.core.model.TaskModel.Status;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO.TimeoutUnit;

@SuppressWarnings("unchecked")
public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() throws Exception {
        when(properties.getAppId()).thenReturn("test-app-id");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(workflowTimeout());

        WorkflowModel workflow = createWorkflowModel();
        String workflowId = "test-workflow-id";
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.of(workflow));

        doCallRealMethod().when(workflowSweeper).sweepAsync(workflowId);

        workflowSweeper.sweepAsync(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepWithNotFoundException() throws Exception {
        when(properties.getAppId()).thenReturn("test-app-id");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(workflowTimeout());

        doThrow(new NotFoundException("Workflow not found for id: test-workflow-id"))
                .when(queueDAO).get(DECIDER_QUEUE, "test-workflow-id");

        workflowSweeper.sweep("test-workflow-id");

        verify(queueDAO).remove(DECIDER_QUEUE, "test-workflow-id");
        verify(monitors).error(WorkflowSweeper.class.getSimpleName(), "sweep");
    }

    @Test
    public void testSweepWithException() throws Exception {
        when(properties.getAppId()).thenReturn("test-app-id");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(workflowTimeout());

        WorkflowModel workflow = createWorkflowModel();
        String workflowId = "test-workflow-id";
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.of(workflow));

        doThrow(new RuntimeException("Test exception"))
                .when(workflowExecutor)
                .decide(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(monitors).error(WorkflowSweeper.class.getSimpleName(), "sweep");
    }

    @Test
    public void testUnack() throws Exception {
        WorkflowModel workflow = createWorkflowModel();
        TaskModel taskModel = createTaskModel(Status.IN_PROGRESS);
        workflow.setTasks(Arrays.asList(taskModel));

        when(properties.getAppId()).thenReturn("test-app-id");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(workflowTimeout());

        long workflowOffsetTimeout = 45L;
        workflowSweeper.unack(workflow, workflowOffsetTimeout);

        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflow.getWorkflowId(), TimeoutUnit.SECONDS.toMillis(workflowOffsetTimeout * 2));
    }

    @Test
    public void testUnackWithWaitTask() throws Exception {
        WorkflowModel workflow = createWorkflowModel();
        TaskModel taskModel = createTaskModel(TaskType.TASK_TYPE_WAIT);
        workflow.setTasks(Arrays.asList(taskModel));

        when(properties.getAppId()).thenReturn("test-app-id");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(workflowTimeout());

        long workflowOffsetTimeout = 45L;
        workflowSweeper.unack(workflow, workflowOffsetTimeout);

        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflow.getWorkflowId(), TimeoutUnit.SECONDS.toMillis(workflowOffsetTimeout + 1));
    }

    @Test
    public void testUnackWithScheduledTask() throws Exception {
        WorkflowModel workflow = createWorkflowModel();
        TaskModel taskModel = createTaskModel(Status.SCHEDULED);
        workflow.setTasks(Arrays.asList(taskModel));

        when(properties.getAppId()).thenReturn("test-app-id");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(workflowTimeout());

        long workflowOffsetTimeout = 45L;
        workflowSweeper.unack(workflow, workflowOffsetTimeout);

        TaskDef taskDef = mock(TaskDef.class);
        when(taskModel.getTaskDefinition()).thenReturn(Optional.of(taskDef));
        when(taskDef.getPollTimeoutSeconds()).thenReturn(1L);

        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflow.getWorkflowId(), TimeoutUnit.SECONDS.toMillis(workflowOffsetTimeout + 1));
    }

    @Test
    public void testUnackWithNoTaskDef() throws Exception {
        WorkflowModel workflow = createWorkflowModel();
        TaskModel taskModel = createTaskModel(Status.SCHEDULED);
        workflow.setTasks(Arrays.asList(taskModel));

        when(properties.getAppId()).thenReturn("test-app-id");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(workflowTimeout());

        long workflowOffsetTimeout = 45L;
        workflowSweeper.unack(workflow, workflowOffsetTimeout);

        TaskDef taskDef = mock(TaskDef.class);
        when(taskModel.getTaskDefinition()).thenReturn(Optional.empty());

        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflow.getWorkflowId(), TimeoutUnit.SECONDS.toMillis(workflowOffsetTimeout * 2));
    }

    @VisibleForTesting
    private WorkflowModel createWorkflowModel() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId("test-workflow-id");
        workflow.setWorkflowName("test-workflow-name");
        workflow.setStatus(Status.RUNNING);
        workflow.setCreatedAt(Instant.now());
        workflow.setUpdatedAt(Instant.now());
        return workflow;
    }

    @VisibleForTesting
    private TaskModel createTaskModel(Status status) {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("test-task-id");
        taskModel.setTaskName("test-task-name");
        taskModel.setStatus(status);
        return taskModel;
    }

    @VisibleForTesting
    private long workflowTimeout() {
        return 45L;
    }

    @VisibleForTesting
    @Async
    private void doCallRealMethod() throws Exception {
        workflowSweeper.sweep("test-workflow-id");
    }
}