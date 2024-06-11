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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;

@Component
public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeper.class);

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private QueueDAO queueDAO;
    private ConductorProperties properties;
    private WorkflowRepairService workflowRepairService;
    private Random random;

    @Before
    public void setup() {
        random = new Random();
        workflowExecutor = mock(WorkflowExecutor.class);
        queueDAO = mock(QueueDAO.class);
        properties = mock(ConductorProperties.class);
        workflowRepairService = mock(WorkflowRepairService.class);

        workflowSweeper = new WorkflowSweeper(
                workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "workflowId1";
        when(queueDAO.remove(Mockito.eq(DECIDER_QUEUE), Mockito.eq(workflowId))).thenReturn(null);
        when(workflowExecutor.decide(workflowId)).thenReturn(new WorkflowModel());

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        future.join();

        Mockito.verify(queueDAO).remove(Mockito.eq(DECIDER_QUEUE), Mockito.eq(workflowId));
        Mockito.verify(queueDAO, Mockito.times(0)).setUnackTimeout(
                Mockito.eq(DECIDER_QUEUE), Mockito.anyString(), Mockito.anyLong());
    }

    @Test
    public void testSweepWithNotFoundException() {
        String workflowId = "workflowId2";
        when(queueDAO.remove(Mockito.eq(DECIDER_QUEUE), Mockito.eq(workflowId))).thenThrow(new NotFoundException());

        workflowSweeper.sweep(workflowId);

        Mockito.verify(queueDAO).remove(Mockito.eq(DECIDER_QUEUE), Mockito.eq(workflowId));
        Mockito.verify(queueDAO, Mockito.times(1)).setUnackTimeout(
                Mockito.eq(DECIDER_QUEUE), Mockito.eq(workflowId), Mockito.anyLong());
    }

    @Test
    public void testSweepWithException() {
        String workflowId = "workflowId3";
        WorkflowModel workflowModel = new WorkflowModel();
        when(queueDAO.remove(Mockito.eq(DECIDER_QUEUE), Mockito.eq(workflowId))).thenReturn(null);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        try {
            workflowSweeper.sweep(workflowId);
        } catch (Exception e) {
            // Expected
        }

        Mockito.verify(queueDAO).remove(Mockito.eq(DECIDER_QUEUE), Mockito.eq(workflowId));
        Mockito.verify(queueDAO, Mockito.times(1)).setUnackTimeout(
                Mockito.eq(DECIDER_QUEUE), Mockito.eq(workflowId), Mockito.anyLong());
        Monitors.error(WorkflowSweeper.class.getSimpleName(), "sweep");
    }

    @Test
    public void testVerifyAndRepairWorkflowTasks() {
        String workflowId = "workflowId4";
        WorkflowModel workflowModel = new WorkflowModel();
        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(new TaskModel());
        workflowModel.setTasks(tasks);

        workflowSweeper.verifyAndRepairWorkflowTasks(workflowId);

        Mockito.verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
    }

    @Test
    public void testUnack() {
        String workflowId = "workflowId5";
        WorkflowModel workflowModel = new WorkflowModel();
        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(new TaskModel());
        workflowModel.setTasks(tasks);

        long workflowOffsetTimeout = 1000L;

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        Mockito.verify(queueDAO).setUnackTimeout(
                Mockito.eq(DECIDER_QUEUE), Mockito.eq(workflowId), Mockito.eq(workflowOffsetTimeout * 1000));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 1000L;

        long workflowOffsetWithJitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        long range = workflowOffsetTimeout / 3;
        long jitter = random.nextInt((int) (2 * range + 1)) - range;
        long expectedWorkflowOffsetWithJitter = workflowOffsetTimeout + jitter;

        assertEquals(expectedWorkflowOffsetWithJitter, workflowOffsetWithJitter);
    }
}