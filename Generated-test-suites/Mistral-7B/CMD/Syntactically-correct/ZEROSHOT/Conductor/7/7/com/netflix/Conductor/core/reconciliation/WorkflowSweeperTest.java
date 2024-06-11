package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.testutils.ConductorTestUtils;

/**
 * Test class for WorkflowSweeper
 */
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

    @Autowired
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("test-app-id");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);

        when(workflowExecutor.decide(ArgumentMatchers.anyString()))
                .thenReturn(new WorkflowModel());
        when(workflowExecutor.decide(ArgumentMatchers.anyString()))
                .thenThrow(new NotFoundException("Workflow not found"));

        when(queueDAO.remove(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(true);
        when(queueDAO.setUnackTimeout(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong()))
                .thenReturn(true);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "test-workflow-id";
        workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper).sweep(workflowId);
    }

    @Test
    public void testSweep_WorkflowFound() {
        String workflowId = "test-workflow-id";
        TaskModel taskModel = ConductorTestUtils.createTaskModel();
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(Arrays.asList(taskModel));

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        when(workflowExecutor.decide(workflowId))
                .thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep_WorkflowNotFound() {
        String workflowId = "test-workflow-id";

        workflowSweeper.sweep(workflowId);

        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = ConductorTestUtils.createTaskModel();
        workflowModel.setTasks(Collections.singletonList(taskModel));

        long workflowOffsetTimeout = 5L;

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(),
                ArgumentMatchers.eq(workflowOffsetTimeout * 1000));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 5L;
        long jitterRange = workflowOffsetTimeout / 3;

        long offsetWithJitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(offsetWithJitter >= workflowOffsetTimeout - jitterRange);
        assertTrue(offsetWithJitter <= workflowOffsetTimeout + jitterRange);
    }
}