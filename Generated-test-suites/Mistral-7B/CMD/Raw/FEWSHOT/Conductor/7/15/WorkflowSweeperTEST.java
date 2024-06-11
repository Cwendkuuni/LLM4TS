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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;
import com.netflix.conductor.metrics.Monitors;

@Component
public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-1";
    private static final String WORKFLOW_NAME = "workflow-name";

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
    private Logger logger;

    @Autowired
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        when(properties.getAppId()).thenReturn("app-id");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(10L);
        when(properties.getWorkflowOffsetTimeout().getSeconds()).thenReturn(60L);

        when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong())).thenReturn(true);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(getWorkflowModel());
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException());

        when(workflowRepairService.verifyAndRepairWorkflowTasks(WORKFLOW_ID)).thenReturn(Arrays.asList(
                getTaskModel1(), getTaskModel2()));
    }

    @Test
    public void testSweepAsync() {
        workflowSweeper.sweepAsync(WORKFLOW_ID);
        verify(workflowSweeper).sweep(WORKFLOW_ID);
    }

    @Test
    public void testSweep() {
        WorkflowModel workflow = getWorkflowModel();
        workflowSweeper.sweep(WORKFLOW_ID);

        verify(workflowExecutor).decide(WORKFLOW_ID);

        if (workflow != null) {
            verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
        } else {
            verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        }
    }

    @Test
    public void testSweepWithRepairService() {
        WorkflowModel workflow = getWorkflowModel();
        workflowSweeper.sweep(WORKFLOW_ID);

        if (workflow != null) {
            verify(workflowRepairService).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
            verifyZeroInteractions(queueDAO);
        } else {
            verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        }
    }

    @Test
    public void testSweepWithError() {
        WorkflowModel workflow = getWorkflowModel();
        doThrow(new Exception("Test error")).when(workflowExecutor).decide(WORKFLOW_ID);

        workflowSweeper.sweep(WORKFLOW_ID);

        Monitors.error(eq(WorkflowSweeper.class.getSimpleName()), "sweep");
        verify(monitors, times(1)).error(eq(WorkflowSweeper.class.getSimpleName()), "sweep", eq("Test error"));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = getWorkflowModel();
        workflowSweeper.unack(workflowModel, 1L);

        TaskModel taskModel1 = getTaskModel1();
        TaskModel taskModel2 = getTaskModel2();

        for (TaskModel taskModel : workflowModel.getTasks()) {
            if (taskModel.getStatus() == Status.IN_PROGRESS) {
                if (taskModel.getTaskType().equals(TaskType.TASK_TYPE_WAIT)
                        || taskModel.getTaskType().equals(TaskType.TASK_TYPE_HUMAN)) {
                    verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 61L);
                    return;
                }
            }
        }

        fail("Unexpected task model found.");
    }

    @Test
    public void testUnackScheduledTask() {
        WorkflowModel workflowModel = getWorkflowModel();
        workflowSweeper.unack(workflowModel, 1L);

        TaskModel taskModel = getTaskModel1();

        if (taskModel.getStatus() == Status.SCHEDULED) {
            Optional<TaskDef> taskDefinition = taskModel.getTaskDefinition();
            assertEquals(workflowModel.getWorkflowDefinition().getTimeoutSeconds() + 1,
                    taskDefinition.map(TaskDef::getPollTimeoutSeconds).orElse(workflowModel.getWorkflowDefinition()
                            .getTimeoutSeconds() + 1));
            return;
        }

        fail("Unexpected task model found.");
    }

    @Test
    public void testUnackWorkflowTimeout() {
        WorkflowModel workflowModel = getWorkflowModel();
        workflowSweeper.unack(workflowModel, 1L);

        if (workflowModel.getWorkflowDefinition().getTimeoutSeconds() != 0) {
            assertEquals(workflowModel.getWorkflowDefinition().getTimeoutSeconds() + 1,
                    queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID) / 1000.0);
            return;
        }

        fail("Unexpected workflow definition found.");
    }

    @Test
    public void testUnackTaskTimeout() {
        WorkflowModel workflowModel = getWorkflowModel();
        workflowSweeper.unack(workflowModel, 1L);

        TaskModel taskModel = getTaskModel1();

        if (taskModel.getResponseTimeoutSeconds() != 0) {
            assertEquals(taskModel.getResponseTimeoutSeconds() + 1,
                    queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID) / 1000.0);
            return;
        }

        fail("Unexpected task model found.");
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 60L;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(result >= (workflowOffsetTimeout * 0.6666666666666666F));
        assertTrue(result <= (workflowOffsetTimeout * 1.3333333333333333F));
    }

    private WorkflowModel getWorkflowModel() {
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        WorkflowDefinition workflowDefinition = Mockito.mock(WorkflowDefinition.class);
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDefinition);
        when(workflowDefinition.getTimeoutSeconds()).thenReturn(30L);
        when(workflowDefinition.getName()).thenReturn(WORKFLOW_NAME);

        List<TaskModel> tasks = Arrays.asList(getTaskModel1(), getTaskModel2());
        when(workflowModel.getTasks()).thenReturn(tasks);

        return workflowModel;
    }

    private TaskModel getTaskModel1() {
        TaskModel taskModel = Mockito.mock(TaskModel.class);
        when(taskModel.getStatus()).thenReturn(Status.IN_PROGRESS);
        when(taskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_WAIT);
        when(taskModel.getName()).thenReturn("task1");

        return taskModel;
    }

    private TaskModel getTaskModel2() {
        TaskModel taskModel = Mockito.mock(TaskModel.class);
        when(taskModel.getStatus()).thenReturn(Status.SCHEDULED);
        when(taskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_HUMAN);
        when(taskModel.getName()).thenReturn("task2");

        return taskModel;
    }

    private void verifyZeroInteractions(QueueDAO queueDAO) {
        verifyNoMoreInteractions(queueDAO);
    }
}