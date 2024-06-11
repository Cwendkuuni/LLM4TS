import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;
import static com.netflix.conductor.core.utils.Utils.DECIDER_QUEUE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    @Autowired
    WorkflowSweeper workflowSweeper;

    @Mock
    ConductorProperties properties;

    @Mock
    WorkflowExecutor workflowExecutor;

    @Mock
    WorkflowRepairService workflowRepairService;

    @Mock
    QueueDAO queueDAO;

    @Before
    public void setUp() {
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync("workflow1");
        assertNotNull(future);
        verify(workflowExecutor, times(1)).decide("workflow1");
    }

    @Test
    public void testSweep() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);
        when(workflowExecutor.decide("workflow1")).thenReturn(workflowModel);
        workflowSweeper.sweep("workflow1");
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "workflow1");
        verify(workflowSweeper, times(1)).unack(any(WorkflowModel.class), anyLong());
    }

    @Test
    public void testSweepWorkflowNotFound() {
        NotFoundException exception = mock(NotFoundException.class);
        when(workflowExecutor.decide("workflow1")).thenThrow(exception);
        workflowSweeper.sweep("workflow1");
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "workflow1");
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(10);
        workflowModel.getTasks().add(taskModel);
        workflowSweeper.unack(workflowModel, 30);
        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 31 * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        long offsetWithJitter = workflowSweeper.workflowOffsetWithJitter(45);
        assertTrue(offsetWithJitter >= 30 && offsetWithJitter <= 60);
    }
}