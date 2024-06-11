package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.WorkflowModel;
import com.netflix.conductor.core.config.SchedulerConfiguration;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;

@Component
public class WorkflowSweeperTest {

    private final ConductorProperties properties;
    private final WorkflowExecutor workflowExecutor;
    private final WorkflowRepairService workflowRepairService;
    private final QueueDAO queueDAO;
    private final Random random;

    @Autowired
    public WorkflowSweeperTest(
            ConductorProperties properties,
            WorkflowExecutor workflowExecutor,
            Optional<WorkflowRepairService> workflowRepairService,
            QueueDAO queueDAO) {
        this.properties = properties;
        this.queueDAO = queueDAO;
        this.workflowExecutor = workflowExecutor;
        this.workflowRepairService = workflowRepairService.orElse(null);
        this.random = new Random();
    }

    @Before
    public void setup() {
        Monitors mockMonitors = mock(Monitors.class);
        when(mockMonitors.error(Mockito.eq(WorkflowSweeper.class.getSimpleName()), Mockito.anyString())).thenReturn(null);
        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        when(workflowExecutor.decide(Mockito.eq("workflowId"))).thenReturn(new WorkflowModel());
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");
        when(queueDAO.setUnackTimeout(Mockito.eq(SchedulerConfiguration.DECIDER_QUEUE), Mockito.eq("workflowId"),
                Mockito.anyLong())).thenReturn(null);
        when(queueDAO.remove(Mockito.eq(SchedulerConfiguration.DECIDER_QUEUE), Mockito.eq("workflowId"))).thenReturn(null);
        WorkflowSweeper workflowSweeper = new WorkflowSweeper();
        workflowSweeper.setProperties(properties);
        workflowSweeper.setQueueDAO(queueDAO);
        workflowSweeper.setWorkflowExecutor(workflowExecutor);
        workflowSweeper.setWorkflowRepairService(workflowRepairService.orElse(null));
        WorkflowSweeper.setMonitors(mockMonitors);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync("workflowId");
        future.join();
    }

    @Test
    public void testSweep_WorkflowNotFound() {
        when(workflowExecutor.decide(Mockito.eq("workflowId"))).thenThrow(new NotFoundException());
        workflowSweeper.sweep("workflowId");
        Mockito.verify(queueDAO).remove(Mockito.eq(SchedulerConfiguration.DECIDER_QUEUE), Mockito.eq("workflowId"));
    }

    @Test
    public void testSweep_WorkflowTerminal() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.FAILED);
        when(workflowExecutor.decide(Mockito.eq("workflowId"))).thenReturn(workflowModel);
        workflowSweeper.sweep("workflowId");
        Mockito.verify(queueDAO).remove(Mockito.eq(SchedulerConfiguration.DECIDER_QUEUE),
                Mockito.eq("workflowId"));
    }

    @Test
    public void testSweep_VerifyAndRepairTasks() {
        WorkflowModel workflowModel = new WorkflowModel();
        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(new TaskModel());
        workflowModel.setTasks(tasks);
        workflowRepairService = mock(WorkflowRepairService.class);
        when(workflowRepairService.verifyAndRepairWorkflowTasks(Mockito.eq("workflowId"))).thenReturn(null);
        workflowSweeper = new WorkflowSweeper();
        workflowSweeper.setProperties(properties);
        workflowSweeper.setQueueDAO(queueDAO);
        workflowSweeper.setWorkflowExecutor(workflowExecutor);
        workflowSweeper.setWorkflowRepairService(workflowRepairService);
        workflowSweeper.sweep("workflowId");
        Mockito.verify(workflowRepairService).verifyAndRepairWorkflowTasks(Mockito.eq("workflowId"));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(taskModel);
        workflowModel.setTasks(tasks);
        when(queueDAO.setUnackTimeout(Mockito.eq(SchedulerConfiguration.DECIDER_QUEUE),
                Mockito.eq(workflowModel.getWorkflowId()), Mockito.anyLong())).thenReturn(null);
        workflowSweeper = new WorkflowSweeper();
        workflowSweeper.setProperties(properties);
        workflowSweeper.setQueueDAO(queueDAO);
        workflowSweeper.setWorkflowExecutor(workflowExecutor);
        workflowSweeper.unack(workflowModel, 1000);
        Mockito.verify(queueDAO).setUnackTimeout(Mockito.eq(SchedulerConfiguration.DECIDER_QUEUE),
                Mockito.eq(workflowModel.getWorkflowId()), Mockito.eq(1001L * 1000L));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long expectedJitter = new Random().nextInt((int) (2 * (workflowOffsetTimeout / 3) + 1)) - (workflowOffsetTimeout / 3);
        long actualJitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assert(expectedJitter == actualJitter);
    }
}