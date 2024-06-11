package com.netflix.conductor.core.reconciliation.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;

@RunWith(MockitoJUnitRunner.class)
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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(2L);
        when(properties.getWorkflowOffsetTimeout().getSeconds()).thenReturn(45);
        when(queueDAO.remove(eq("decider_queue"), eq("workflowId"))).thenReturn(true);
        when(workflowExecutor.decide(eq("workflowId"))).thenReturn(new WorkflowModel());
    }

    @Test
    public void testSweepAsync() {
        sweeper.sweepAsync("workflowId");
    }

    @Test
    public void testSweep() {
        WorkflowModel workflow = new WorkflowModel();
        when(workflowExecutor.decide(eq("workflowId"))).thenReturn(workflow);

        sweeper.sweep("workflowId");

        verifySweeperMethods(workflow);
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundException() {
        when(workflowExecutor.decide(eq("workflowId"))).thenThrow(new NotFoundException("Workflow not found"));

        sweeper.sweep("workflowId");
    }

    @Test
    public void testExceptionDuringSweep() {
        WorkflowModel workflow = new WorkflowModel();
        when(workflowExecutor.decide(eq("workflowId"))).thenReturn(workflow);

        when(monitors::error).thenReturn(mock(Runnable.class));

        sweeper.sweep("workflowId");

        verifySweeperMethods(workflow);
        verify(monitors).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflow = new WorkflowModel();
        List<TaskModel> tasks = Arrays.asList(new TaskModel(), new TaskModel());
        workflow.setTasks(tasks);

        sweeper.unack(workflow, 3000L);

        for (TaskModel task : tasks) {
            if (task.getStatus() == Status.IN_PROGRESS || task.getStatus() == Status.SCHEDULED) {
                verify(queueDAO).setUnackTimeout(eq("decider_queue"), eq(workflow.getWorkflowId()), eq(3001L));
                break;
            }
        }
    }

    @Test
    public void testUnackWithNoTaskToUnack() {
        WorkflowModel workflow = new WorkflowModel();

        sweeper.unack(workflow, 3000L);

        verify(queueDAO).setUnackTimeout(eq("decider_queue"), eq(workflow.getWorkflowId()), eq(3000L));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;

        long jitter = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        long expectedStart = workflowOffsetTimeout - (workflowOffsetTimeout / 3);
        long expectedEnd = workflowOffsetTimeout + (workflowOffsetTimeout / 3);

        assertFalse(jitter < expectedStart);
        assertFalse(jitter > expectedEnd);
    }

    private void verifySweeperMethods(WorkflowModel workflow) {
        verify(workflowContext).decide(eq("workflowId"));
        if (workflow != null && workflow.getStatus() != null && workflow.getStatus().isTerminal()) {
            verify(queueDAO).remove(eq("decider_queue"), eq(workflow.getWorkflowId()));
        }
        if (workflowRepairService != null) {
            verify(workflowRepairService).verifyAndRepairWorkflowTasks(eq("workflowId"));
        }

        if (workflow != null) {
            long workflowOffsetTimeout = sweeper.properties.getWorkflowOffsetTimeout().getSeconds();
            long startTime = Instant.now().toEpochMilli();
            long expectedEndTime = startTime + ChronoUnit.SECONDS.toMillis(workflowOffsetTimeout);

            verify(monitors).recordUnackTime(eq(workflow.getWorkflowName()), eq(expectedEndTime - startTime));
        } else {
            verify(queueDAO).setUnackTimeout(eq("decider_queue"), eq(workflow.getWorkflowId()), anyLong());
        }
    }
}