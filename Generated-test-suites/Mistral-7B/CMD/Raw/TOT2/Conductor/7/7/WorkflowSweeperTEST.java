package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.WorkflowExecutor;
import com.netflix.conductor.core.config.SchedulerConfiguration;
import com.netflix.conductor.core.execution.WorkflowExecutor.ExecutionResult;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.Queue;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;
import static com.netflix.conductor.core.utils.Utils.DECIDER_QUEUE;
import static com.netflix.conductor.core.utils.Utils.WORKFLOW_OFFSET_TIMEOUT_PROPERTY_NAME;

@Component
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
    private WorkflowSweeper sweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        when(properties.getWorkflowOffsetTimeout().getSeconds()).thenReturn(5000);
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, anyString(), anyLong()))
                .thenReturn(true);
        when(monitors.recordUnackTime(anyString(), anyLong())).thenReturn(1L);
        when(queueDAO.remove(DECIDER_QUEUE, anyString())).thenReturn(true);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        sweeper.sweepAsync(workflowId).join();
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";

        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowName("testWorkflowName");
        workflow.setWorkflowId(workflowId);
        workflow.setStatus(WorkflowStatus.RUNNING);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);

        sweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test(expected = NotFoundException.class)
    public void testSweepNotFoundException() {
        String workflowId = "testWorkflowId";
        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException());

        sweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testUnack() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowName("testWorkflowName");
        workflow.setWorkflowId(workflowId);
        workflow.setStatus(WorkflowStatus.RUNNING);

        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(new TaskModel());
        workflow.setTasks(tasks);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);

        sweeper.unack(workflow, 10L * 1000);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, 10L * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 5000;
        long jitter = new Random().nextInt(3 * workflowOffsetTimeout / 2) - (workflowOffsetTimeout / 4);
        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(result >= workflowOffsetTimeout - jitter);
        assertTrue(result <= workflowOffsetTimeout + jitter);
    }
}