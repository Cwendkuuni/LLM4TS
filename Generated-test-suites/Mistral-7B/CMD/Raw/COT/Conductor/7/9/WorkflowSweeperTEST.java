package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;

public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;
    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private WorkflowRepairService workflowRepairService;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private ConductorProperties properties;
    @Mock
    private Monitors monitors;

    private static final String WORKFLOW_ID = "workflowId";
    private static final String WORKFLOW_NAME = "workflowName";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("appId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(new Random(1L));
        when(queueDAO.remove(DECIDER_QUEUE, any())).thenReturn(true);
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, anyString(), anyLong())).thenReturn(true);
    }

    @After
    public void teardown() {
        verifyNoMoreInteractions(workflowExecutor, workflowRepairService, queueDAO, properties, monitors);
    }

    @Test
    public void testSweepAsync() {
        workflowSweeper.sweepAsync(WORKFLOW_ID);
        verify(workflowSweeper).sweep(WORKFLOW_ID);
    }

    @Test
    public void testSweep() {
        WorkflowModel workflowModel = createWorkflowModel();
        workflowSweeper.sweep(WORKFLOW_ID);

        verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
        verify(queueDAO, times(workflowModel.getTasks().size())).setUnackTimeout(
                DECIDER_QUEUE, eq(workflowModel.getWorkflowId()), anyLong());
    }

    @Test
    public void testSweep_NotFoundException() {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException("Not Found"));
        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweep_Exception() {
        WorkflowModel workflowModel = createWorkflowModel();
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new RuntimeException("Error"));
        workflowSweeper.sweep(WORKFLOW_ID);

        verify(monitors).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = createWorkflowModel();
        workflowSweeper.unack(workflowModel, 1000L);

        for (TaskModel taskModel : workflowModel.getTasks()) {
            if (taskModel.getStatus() != Status.IN_PROGRESS) {
                continue;
            }

            Optional<TaskDef> taskDefinition = taskModel.getTaskDefinition();
            if (taskDefinition.isPresent()) {
                TaskDef taskDef = taskDefinition.get();
                assertFalse("Unexpectedly not postponing task with poll timeout",
                        taskDef.getPollTimeoutSeconds() == 0);
                long expectedPostponeDuration = taskDef.getPollTimeoutSeconds() + 1;
                verify(queueDAO).setUnackTimeout(DECIDER_QUEUE,
                        eq(workflowModel.getWorkflowId()), eq(expectedPostponeDuration * 1000));
            } else {
                assertFalse("Unexpectedly not postponing workflow-level timeout",
                        workflowModel.getWorkflowDefinition().getTimeoutSeconds() == 0);
                long expectedPostponeDuration =
                        workflowModel.getWorkflowDefinition().getTimeoutSeconds() + 1;
                verify(queueDAO).setUnackTimeout(DECIDER_QUEUE,
                        eq(workflowModel.getWorkflowId()), eq(expectedPostponeDuration * 1000));
            }
        }
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45_000L;
        long jitter = new Random().nextInt((int) (2 * workflowOffsetTimeout / 3 + 1)) - workflowOffsetTimeout / 3;
        long expectedWorkflowOffset = workflowOffsetTimeout + jitter;

        long actualWorkflowOffset = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertSame("Unexpected workflow offset", expectedWorkflowOffset, actualWorkflowOffset);
    }

    private WorkflowModel createWorkflowModel() {
        TaskModel taskModel1 = new TaskModel();
        taskModel1.setStatus(Status.SCHEDULED);
        taskModel1.setTaskDefinition(Optional.of(new TaskDef()));
        taskModel1.setWaitTimeout(0);

        TaskModel taskModel2 = new TaskModel();
        taskModel2.setStatus(Status.IN_PROGRESS);
        taskModel2.setTaskDefinition(Optional.empty());
        taskModel2.setWaitTimeout(5_000L);

        List<TaskModel> tasks = Arrays.asList(taskModel1, taskModel2);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(tasks);
        return workflowModel;
    }
}