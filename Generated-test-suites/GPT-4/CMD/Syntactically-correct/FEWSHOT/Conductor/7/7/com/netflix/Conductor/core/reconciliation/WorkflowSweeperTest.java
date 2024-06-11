package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;
import static com.netflix.conductor.core.utils.Utils.DECIDER_QUEUE;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.junit.Rule;
import org.junit.Test;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {
    
    @Rule 
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    @Test
    public void testSweepAsync() {
        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        sweeper.sweepAsync("test_workflow_id");
        verify(workflowExecutor, times(1)).decide("test_workflow_id");
    }

    @Test
    public void testSweep() {
        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        sweeper.sweep("test_workflow_id");
        verify(workflowExecutor, times(1)).decide("test_workflow_id");
    }

    @Test
    @VisibleForTesting
    public void testUnack() { 
        // Test will be written manually
    }

    @Test
    @VisibleForTesting
    public void testWorkflowOffsetWithJitter() {
        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        long jitteredValue = sweeper.workflowOffsetWithJitter(5);
        assertTrue(jitteredValue >= 3 && jitteredValue <= 7);
    }
}