package com.netflix.conductor.core.reconciliation.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;

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
    private Logger logger;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(new Random(1L));
        when(properties.getWorkflowOffsetTimeout().getSeconds()).thenReturn(45);
        when(logger.isInfoEnabled()).thenReturn(true);
        when(logger.isDebugEnabled()).thenReturn(true);
        when(logger.isWarnEnabled()).thenReturn(true);
        when(logger.isErrorEnabled()).thenReturn(true);
        when(queueDAO.remove(eq(DECIDER_QUEUE), eq(anyString()))).thenReturn(null);
        when(queueDAO.setUnackTimeout(eq(DECIDER_QUEUE), eq(anyString()), anyLong()))
                .thenReturn(null);
        when(workflowExecutor.decide(anyString())).thenReturn(new WorkflowModel());
    }

    @Test
    public void testSweepAsync() {
        workflowSweeper.sweepAsync("testWorkflowId");
        // Verify that sweepAsync method calls sweep method
        verify(workflowSweeper).sweep("testWorkflowId");
    }

    @Test
    public void testSweep() {
        // Test normal case
        workflowSweeper.sweep("testWorkflowId");
        // Test workflow not found
        when(workflowExecutor.decide(eq("testWorkflowId"))).thenThrow(new NotFoundException());
        workflowSweeper.sweep("testWorkflowId");
        // Test exception
        when(workflowExecutor.decide(eq("testWorkflowId"))).thenThrow(new RuntimeException());
        workflowSweeper.sweep("testWorkflowId");
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        taskModelGenerator(workflowModel, Status.IN_PROGRESS, 30, TaskType.TASK_TYPE_WAIT);
        workflowSweeper.unack(workflowModel, 45);
        assertSame(46L * 1000, workflowModel.getUnackTimeout());

        workflowModel = new WorkflowModel();
        taskModelGenerator(workflowModel, Status.IN_PROGRESS, 0, TaskType.TASK_TYPE_HUMAN);
        workflowSweeper.unack(workflowModel, 45);
        assertSame(46L * 1000, workflowModel.getUnackTimeout());

        workflowModel = new WorkflowModel();
        taskModelGenerator(workflowModel, Status.SCHEDULED, 60, null);
        workflowSweeper.unack(workflowModel, 45);
        assertSame(61L * 1000, workflowModel.getUnackTimeout());

        workflowModel = new WorkflowModel();
        taskModelGenerator(workflowModel, Status.SCHEDULED, 0, WorkflowSweeper.class);
        workflowSweeper.unack(workflowModel, 45);
        assertSame(46L * 1000, workflowModel.getUnackTimeout());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        Random random = new Random(1L);
        long workflowOffsetTimeout = 45;
        long jitter = random.nextInt((int) (2 * workflowOffsetTimeout / 3 + 1)) - (workflowOffsetTimeout / 3);
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertSame(workflowOffsetTimeout + jitter, result);
    }

    private void taskModelGenerator(WorkflowModel workflowModel, Status status, long timeout, TaskType taskType) {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(status);
        taskModel.setTaskType(taskType);
        taskModel.setResponseTimeoutSeconds(timeout);
        workflowModel.getTasks().add(taskModel);
    }
}