package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metrics.Monitors;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

@SuppressWarnings({"unchecked", "deprecation"})
public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeperTest.class);

    private WorkflowSweeper sweeper;
    private ConductorProperties properties;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private QueueDAO queueDAO;
    private Monitors monitors;

    @Before
    public void setUp() {
        properties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        queueDAO = mock(QueueDAO.class);
        monitors = mock(Monitors.class);

        sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO) {
            @Override
            protected Monitors getMonitors() {
                return monitors;
            }
        };
    }

    @Test
    public void testSweepAsync() throws Exception {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> result = sweeper.sweepAsync(workflowId);
        result.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testSweep_WorkflowFound_TerminalStatus() throws Exception {
        String workflowId = "testWorkflowIdTerminal";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.TERMINALED);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);

        sweeper.sweep(workflowId);

        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).remove(eq(DECIDER_QUEUE), eq(workflowId));
    }

    @Test
    public void testSweep_WorkflowNotFound() throws Exception {
        String workflowId = "testWorkflowIdNotFound";

        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException());

        sweeper.sweep(workflowId);

        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).remove(eq(DECIDER_QUEUE), eq(workflowId));
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong());
    }

    @Test
    public void testSweep_Exception() throws Exception {
        String workflowId = "testWorkflowIdException";
        WorkflowModel workflow = new WorkflowModel();

        when(workflowExecutor.decide(workflowId)).thenThrow(new Exception("Test Exception"));

        sweeper.sweep(workflowId);

        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong());
        Monitors.error(eq(WorkflowSweeper.class), eq("sweep"));
    }

    @Test
    public void testUnack() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        when(workflowModel.getTasks()).thenReturn(new LinkedList<TaskModel>() {{
            add(taskModel);
        }});

        sweeper.unack(workflowModel, 1000L);

        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), timeoutCaptor.capture());

        long unackTimeout = timeoutCaptor.getValue();
        assertEquals(1001L, unackTimeout);
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        long workflowOffsetTimeout = 45L;

        long jitter = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(jitter >= (workflowOffsetTimeout - 15L) && jitter <= (workflowOffsetTimeout + 15L));
    }
}