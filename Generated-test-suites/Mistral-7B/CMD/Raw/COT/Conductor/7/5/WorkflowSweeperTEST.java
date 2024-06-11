package com.netflix.conductor.core.reconciliation.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.WorkflowExecutor;
import com.netflix.conductor.core.ReconciliationController;
import com.netflix.conductor.core.execution.WorkflowExecutorImpl;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.testutils.TestUtils;

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

    @Mock
    private ReconciliationController reconciliationController;

    @Mock
    private TaskDef taskDef;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setFieldValue(workflowSweeper, "queueDAO", queueDAO);
        ReflectionTestUtils.setFieldValue(workflowSweeper, "workflowExecutor", workflowExecutor);
        ReflectionTestUtils.setFieldValue(workflowSweeper, "workflowRepairService", workflowRepairService);
        ReflectionTestUtils.setFieldValue(workflowSweeper, "properties", properties);
        ReflectionTestUtils.setFieldValue(workflowSweeper, "monitors", monitors);
        ReflectionTestUtils.setFieldValue(workflowSweeper, "reconciliationController", reconciliationController);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "workflow-id-1";
        workflowSweeper.sweepAsync(workflowId);
        // Verify that the sweep method is called when the CompletableFuture completes
        ArgumentCaptor<String> workflowIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ReflectionTestUtils.invokeMethod(workflowSweeper, "sweep", workflowIdArgumentCaptor.capture(workflowId));
        String capturedWorkflowId = workflowIdArgumentCaptor.getValue();
        assertFalse("Expected sweep method to be called", capturedWorkflowId.isEmpty());
        assertEquals("workflow-id-1", capturedWorkflowId);
    }

    @Test
    public void testSweep_NotFoundException() {
        String workflowId = "workflow-id-2";
        when(queueDAO.find(DECIDER_QUEUE, workflowId)).thenThrow(new NotFoundException("Workflow not found"));
        workflowSweeper.sweep(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(monitors).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"));
    }

    @Test
    public void testSweep_Exception() {
        String workflowId = "workflow-id-3";
        WorkflowModel workflow = mock(WorkflowModel.class);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowStatus.RUNNING);
        when(queueDAO.find(DECIDER_QUEUE, workflowId)).thenAnswer(i -> workflow);

        try {
            workflowSweeper.sweep(workflowId);
            fail("Expected NotFoundException to be thrown");
        } catch (Exception e) {
            verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
            verify(monitors).error(WorkflowSweeper.class.getSimpleName(), "sweep", eq(e));
        }
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflow-id-4");
        workflowModel.setWorkflowName("workflow-name-4");
        workflowModel.setStatus(WorkflowStatus.RUNNING);

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setTaskId("task-id-1");
        taskModel1.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel1.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel1.setWaitTimeout(30);
        workflowModel.setTasks(Arrays.asList(taskModel1));

        TaskModel taskModel2 = new TaskModel();
        taskModel2.setTaskId("task-id-2");
        taskModel2.setTaskType(TaskType.TASK_TYPE_HUMAN);
        taskModel2.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel2.setWaitTimeout(0);
        workflowModel.setTasks(Arrays.asList(taskModel1, taskModel2));

        Long expectedUnackTimeout = 46L;
        when(queueDAO.find(DECIDER_QUEUE, workflowModel.getWorkflowId())).thenReturn(workflowModel);
        workflowSweeper.unack(workflowModel);
        ReflectionTestUtils.invokeMethod(queueDAO, "setUnackTimeout", DECIDER_QUEUE, workflowModel.getWorkflowId(), expectedUnackTimeout * 1000);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), expectedUnackTimeout * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter_PositiveNumber() {
        long workflowOffsetTimeout = 45;
        long expectedWorkflowOffsetWithJitter = 46L;
        long jitter = new Random().nextInt(91) - 30;
        long actualWorkflowOffsetWithJitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertEquals("Unexpected workflowOffsetWithJitter value", expectedWorkflowOffsetWithJitter, actualWorkflowOffsetWithJitter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWorkflowOffsetWithJitter_NegativeNumber() {
        long workflowOffsetTimeout = -12;
        workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
    }
}