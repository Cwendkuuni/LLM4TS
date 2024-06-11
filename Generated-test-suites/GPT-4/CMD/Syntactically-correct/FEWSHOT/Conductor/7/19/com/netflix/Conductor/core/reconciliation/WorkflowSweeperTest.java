
package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;
import static com.netflix.conductor.core.utils.Utils.DECIDER_QUEUE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private WorkflowRepairService workflowRepairService;
    @Mock
    private ConductorProperties properties;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private WorkflowModel workflowModel;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync("workflowId");
        verify(workflowRepairService).verifyAndRepairWorkflowTasks(anyString());
        verify(workflowExecutor).decide(anyString());
        verify(queueDAO).remove(DECIDER_QUEUE, "workflowId");
    }

    @Test
    public void testSweep() {
        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);
        workflowSweeper.sweep("workflowId");
        verify(workflowRepairService).verifyAndRepairWorkflowTasks(anyString());
        verify(workflowExecutor).decide(anyString());
        verify(queueDAO).remove(DECIDER_QUEUE, "workflowId");
    }

    @Test
    public void testUnack() {
        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(taskModel.getTaskDefinition()).thenReturn(Optional.of(new TaskDef()));
        workflowSweeper.unack(workflowModel, 60);
        verify(queueDAO).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitterValue = workflowSweeper.workflowOffsetWithJitter(60);
        // Check if jitter value lies between value - 20 and value + 20
        assert(jitterValue >= 40 && jitterValue <= 80);
    }
}
