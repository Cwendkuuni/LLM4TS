package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.Utils;

public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeper.class);

    private WorkflowSweeper workflowSweeper;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private QueueDAO queueDAO;
    private Monitors monitors;
    private WorkflowRepairService workflowRepairService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        queueDAO = mock(QueueDAO.class);
        monitors = mock(Monitors.class);
        workflowRepairService = mock(WorkflowRepairService.class);

        when(conductorProperties.getAppId()).thenReturn("appId");
        when(conductorProperties.getWorkflowOffsetTimeout()).thenReturn(new Random(1L));

        workflowSweeper = new WorkflowSweeper(
                workflowExecutor, Optional.of(workflowRepairService), conductorProperties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "workflowId";
        workflowSweeper.sweepAsync(workflowId);
        InOrder inOrder = Mockito.inOrder(workflowExecutor, queueDAO);
        inOrder.verify(workflowExecutor).decide(workflowId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testSweep() {
        String workflowId = "workflowId";

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setWorkflowName("workflowName");
        workflowModel.setStatus(Status.RUNNING);

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.of(workflowModel));
        when(workflowExecutor.decide(workflowId)).thenThrow(new RuntimeException());

        workflowSweeper.sweep(workflowId);

        InOrder inOrder = Mockito.inOrder(workflowExecutor, queueDAO, monitors);
        inOrder.verify(workflowExecutor).decide(workflowId);
        inOrder.verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        inOrder.verify(monitors).error(WorkflowSweeper.class.getSimpleName(), "sweep");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testSweepWorkflowNotFound() {
        String workflowId = "workflowId";

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.empty());

        workflowSweeper.sweep(workflowId);

        InOrder inOrder = Mockito.inOrder(queueDAO);
        inOrder.verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");
        workflowModel.setTasks(Arrays.asList(new TaskModel(), new TaskModel()));

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setStatus(Status.IN_PROGRESS);
        taskModel1.setTaskType(new TaskType("TaskType1"));

        TaskModel taskModel2 = new TaskModel();
        taskModel2.setStatus(Status.SCHEDULED);
        taskModel2.setTaskDefinition(new TaskDef());

        workflowModel.setTasks(Arrays.asList(taskModel1, taskModel2));

        when(queueDAO.get(DECIDER_QUEUE, workflowModel.getWorkflowId())).thenReturn(Optional.of(workflowModel));

        workflowSweeper.unack(workflowModel, 1000L);

        InOrder inOrder = Mockito.inOrder(queueDAO);
        inOrder.verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), 2001L);
        inOrder.verifyNoMoreInteractions();
    }
}