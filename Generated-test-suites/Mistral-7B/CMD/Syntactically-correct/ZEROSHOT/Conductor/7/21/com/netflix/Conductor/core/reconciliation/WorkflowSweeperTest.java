package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
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
    private Monitors monitors;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    private static final String WORKFLOW_ID = "testWorkflowId";
    private static final String WORKFLOW_NAME = "testWorkflowName";
    private static final long WORKFLOW_OFFSET_TIMEOUT = 45;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(WORKFLOW_OFFSET_TIMEOUT);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenAnswer(new Answer<WorkflowModel>() {
            @Override
            public WorkflowModel answer(InvocationOnMock invocation) throws Exception {
                return new WorkflowModel();
            }
        });
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException(""));
        when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);
        when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenThrow(new RuntimeException(""));
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong())).thenReturn(true);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        future.join();
        verify(workflowSweeper, times(1)).sweep(WORKFLOW_ID);
    }

    @Test
    public void testSweep_WorkflowFound() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.RUNNING);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testSweep_WorkflowNotFound() {
        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweep_VerifyAndRepairTasks() {
        when(workflowRepairService.isEnabled()).thenReturn(true);
        when(workflowRepairService.verifyAndRepairWorkflowTasks(WORKFLOW_ID))
                .thenReturn(new ArrayList<>());

        workflowSweeper.sweep(WORKFLOW_ID);

        // Verify and repair tasks were called
        verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
    }

    @Test
    public void testSweep_Unack() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.RUNNING);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);
        long workflowOffsetTimeout = workflowOffsetWithJitter(WORKFLOW_OFFSET_TIMEOUT);

        workflowSweeper.sweep(WORKFLOW_ID);

        long endTime = Instant.now().toEpochMilli();
        verify(monitors, times(1)).recordUnackTime(eq(WORKFLOW_NAME), anyLong());
        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(WORKFLOW_ID), anyLong());
        assertTrue(endTime > Instant.now().getEpochSecond() * 1000 - workflowOffsetTimeout);
    }

    @Test
    public void testUnack_WorkflowNotFound() {
        workflowSweeper.unack(null, WORKFLOW_OFFSET_TIMEOUT);

        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(WORKFLOW_ID),
                anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = workflowOffsetWithJitter(WORKFLOW_OFFSET_TIMEOUT);
        assertTrue(workflowOffsetTimeout >= WORKFLOW_OFFSET_TIMEOUT - 15);
        assertTrue(workflowOffsetTimeout <= WORKFLOW_OFFSET_TIMEOUT + 15);
    }

    private long workflowOffsetWithJitter(long workflowOffsetTimeout) {
        return workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
    }
}