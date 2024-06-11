package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.WorkflowExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor.ExecutionResult;
import com.netflix.conductor.core.reconciliation.QueueDAO;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;

public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeper.class);
    private static final String WORKFLOW_ID = "workflowId";
    private static final String WORKFLOW_NAME = "workflowName";
    private static final int WAIT_TIMEOUT = 10;
    private static final int RESPONSE_TIMEOUT = 20;
    private static final long WORKFLOW_OFFSET_TIMEOUT = 45;

    private WorkflowSweeper workflowSweeper;
    private QueueDAO queueDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private Monitors monitors;
    private WorkflowRepairService workflowRepairService;

    @Before
    public void setUp() {
        queueDAO = mock(QueueDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        monitors = mock(Monitors.class);
        workflowRepairService = mock(WorkflowRepairService.class);

        when(conductorProperties.getAppId()).thenReturn("appId");
        when(conductorProperties.getWorkflowOffsetTimeout()).thenReturn(WORKFLOW_OFFSET_TIMEOUT);

        WorkflowContext workflowContext = new WorkflowContext(conductorProperties.getAppId());
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(new ExecutionResult(new WorkflowModel()));

        workflowSweeper = new WorkflowSweeper(
                workflowExecutor, Optional.of(workflowRepairService), conductorProperties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        workflowSweeper.sweepAsync(WORKFLOW_ID);
        Mockito.verify(queueDAO).remove(Utils.DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweep() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setStatus(WorkflowStatus.RUNNING);

        when(queueDAO.getFromQueue(Utils.DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(new ExecutionResult(workflowModel));

        workflowSweeper.sweep(WORKFLOW_ID);

        Mockito.verify(queueDAO)
                .setUnackTimeout(Utils.DECIDER_QUEUE, WORKFLOW_ID, WORKFLOW_OFFSET_TIMEOUT * 1000);
    }

    @Test(expected = NotFoundException.class)
    public void testSweepNotFound() {
        when(queueDAO.getFromQueue(Utils.DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(null);

        workflowSweeper.sweep(WORKFLOW_ID);

        Mockito.verify(queueDAO).remove(Utils.DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setTasks(Arrays.asList(createTaskModel(TaskType.TASK_TYPE_WAIT, WAIT_TIMEOUT),
                createTaskModel(TaskType.TASK_TYPE_HUMAN), createTaskModel(TaskType.TASK_TYPE_EXECUTION)));

        when(queueDAO.getFromQueue(Utils.DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);

        Mockito.verify(queueDAO)
                .setUnackTimeout(Utils.DECIDER_QUEUE, WORKFLOW_ID, WAIT_TIMEOUT * 1000 + 1);
    }

    @Test
    public void testUnackTaskTypeExecution() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setTasks(Arrays.asList(createTaskModel(TaskType.TASK_TYPE_EXECUTION),
                createTaskModel(TaskType.TASK_TYPE_EXECUTION), createTaskModel(TaskType.TASK_TYPE_EXECUTION)));

        when(queueDAO.getFromQueue(Utils.DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);

        Mockito.verify(queueDAO)
                .setUnackTimeout(Utils.DECIDER_QUEUE, WORKFLOW_ID, WORKFLOW_OFFSET_TIMEOUT * 1000 + 1);
    }

    @Test
    public void testUnackWorkflowTimeout() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setWorkflowId(WORKFLOW_ID);

        when(queueDAO.getFromQueue(Utils.DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);

        Mockito.verify(queueDAO)
                .setUnackTimeout(Utils.DECIDER_QUEUE, WORKFLOW_ID, WORKFLOW_OFFSET_TIMEOUT * 1000 + 1);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;

        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertEquals(40, jitter);
    }

    private TaskModel createTaskModel(TaskType taskType, int timeout) {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskDefinition(new TaskDef());
        taskModel.setTaskType(taskType);
        taskModel.setStatus(Status.SCHEDULED);
        taskModel.setResponseTimeoutSeconds(timeout);
        return taskModel;
    }
}