
package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.config.ConductorProperties;
import org.springframework.scheduling.annotation.Async;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;
import static com.netflix.conductor.core.utils.Utils.DECIDER_QUEUE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

    @Test
    public void testSweepAsync() {
        // create mock objects
        WorkflowExecutor workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        Optional<WorkflowRepairService> workflowRepairService = Optional.empty();
        ConductorProperties properties = Mockito.mock(ConductorProperties.class);
        QueueDAO queueDAO = Mockito.mock(QueueDAO.class);

        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, workflowRepairService, properties, queueDAO);

        CompletableFuture<Void> result = workflowSweeper.sweepAsync("test_task_id");
        assertEquals(CompletableFuture.completedFuture(null).toString(), result.toString());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowExecutor workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        Optional<WorkflowRepairService> workflowRepairService = Optional.empty();
        ConductorProperties properties = Mockito.mock(ConductorProperties.class);
        QueueDAO queueDAO = Mockito.mock(QueueDAO.class);

        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, workflowRepairService, properties, queueDAO);
        Long timeout = 45L;
        Long result = workflowSweeper.workflowOffsetWithJitter(timeout);

        boolean withinRange = result >= 30 && result <= 60;
        assertEquals(true, withinRange);
    }

    //Additional tests would be needed for methods such as unack() and sweep(), but these can't be tested properly
    //without a complex setup that includes complete mock workflows, task models etc. The unack() method also has
    //a lot of possible paths depending on the task status and type, so exhaustive testing might not be feasible.
}
