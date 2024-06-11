package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.SchedulerConfiguration;
import com.netflix.conductor.core.WorkflowExecutor;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;

public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeper.class);

    private final WorkflowSweeper sweeper;
    private final ConductorProperties properties;
    private final WorkflowExecutor mockWorkflowExecutor;
    private final WorkflowRepairService mockWorkflowRepairService;
    private final QueueDAO mockQueueDAO;
    private final Random mockRandom;

    @Before
    public void setup() {
        this.properties = mock(ConductorProperties.class);
        this.mockWorkflowExecutor = mock(WorkflowExecutor.class);
        this.mockWorkflowRepairService = mock(WorkflowRepairService.class);
        this.mockQueueDAO = mock(QueueDAO.class);
        this.mockRandom = mock(Random.class);

        when(properties.getAppId()).thenReturn("appId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);

        this.sweeper = new WorkflowSweeper(
                this.mockWorkflowExecutor,
                Optional.of(this.mockWorkflowRepairService),
                this.properties,
                this.mockQueueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "workflowId";
        this.sweeper.sweepAsync(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "workflowId";

        // Test case 1: Workflow is not found
        when(this.mockQueueDAO.find(DECIDER_QUEUE, workflowId)).thenReturn(null);
        this.sweeper.sweep(workflowId);

        // Test case 2: Workflow is found and is terminated
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.TERMINATED);
        when(this.mockQueueDAO.find(DECIDER_QUEUE, workflowId)).thenReturn(workflow);
        this.sweeper.sweep(workflowId);

        // Test case 3: Workflow is found and needs repairs
        WorkflowModel workflowToRepair = new WorkflowModel();
        workflowToRepair.setStatus(Status.CREATED);
        TaskModel task = new TaskModel();
        task.setStatus(Status.IN_PROGRESS);
        when(this.mockQueueDAO.find(DECIDER_QUEUE, workflowId)).thenReturn(workflowToRepair);
        this.mockWorkflowRepairService.verifyAndRepairWorkflowTasks(workflowId);
        this.sweeper.sweep(workflowId);

        // Test case 4: Unack task with a jitter value
        WorkflowModel workflowToUnack = new WorkflowModel();
        TaskModel taskToUnack = new TaskModel();
        taskToUnack.setStatus(Status.IN_PROGRESS);
        workflowToUnack.setTasks(Arrays.asList(taskToUnack));
        long workflowOffsetTimeout = this.properties.getWorkflowOffsetTimeout().getSeconds();
        long workflowOffsetWithJitter = this.sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        when(this.mockQueueDAO.find(DECIDER_QUEUE, workflowId)).thenReturn(workflowToUnack);
        this.sweeper.sweep(workflowId);

        // Test case 5: Unack workflow without a task
        WorkflowModel workflowToUnackWithoutTask = new WorkflowModel();
        when(this.mockQueueDAO.find(DECIDER_QUEUE, workflowId)).thenReturn(workflowToUnackWithoutTask);
        this.sweeper.sweep(workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        when(this.mockQueueDAO.find(DECIDER_QUEUE, workflowModel.getWorkflowId())).thenReturn(workflowModel);

        this.sweeper.unack(workflowModel, 5L);

        Mockito.verify(this.mockQueueDAO, Mockito.times(1)).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 5000L);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 5L;
        long expectedMin = (workflowOffsetTimeout / 3) - 1;
        long expectedMax = (workflowOffsetTimeout / 3) * 2 + 1;

        long actual = this.sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assert(actual >= expectedMin);
        assert(actual <= expectedMax);
    }
}