package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;

@SuppressWarnings("deprecation")
public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(10L);
        when(properties.getSweeperExecutorName()).thenReturn("sweeperExecutorName");
        when(queueDAO.remove(DECIDER_QUEUE, anyString())).thenReturn(true);
        when(workflowExecutor.decide(anyString())).thenReturn(new WorkflowModel());
        when(workflowExecutor.decide(anyString())).thenThrow(new NotFoundException());
        when(workflowRepairService.verifyAndRepairWorkflowTasks(anyString())).thenReturn(true);
        when(monitors.error(anyString(), anyString())).thenReturn(null);
        when(logger.isInfoEnabled()).thenReturn(true);
        when(logger.isDebugEnabled()).thenReturn(true);
        when(logger.isWarnEnabled()).thenReturn(true);
        when(logger.info(anyString(), any(Object[].class))).thenAnswer(invocation -> {});
        when(logger.debug(anyString(), any(Object[].class))).thenAnswer(invocation -> {});
        when(logger.warn(anyString(), any(Object[].class))).thenAnswer(invocation -> {});
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(workflowExecutor, queueDAO, monitors, logger);
    }

    @Test
    public void testSweepAsync() throws Exception {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweepAsync(workflowId).get(5, TimeUnit.SECONDS);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verifyZeroInteractions(workflowExecutor, monitors, logger);
    }

    @Test
    public void testSweep() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        workflowSweeper.sweep(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verifyZeroInteractions(workflowExecutor, monitors, logger);

        workflowModel = null;
        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException());
        workflowSweeper.sweep(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verifyZeroInteractions(workflowExecutor, monitors, logger);
    }

    @Test
    public void testVerifyAndRepairWorkflowTasks() throws Exception {
        String workflowId = "testWorkflowId";
        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowId)).thenReturn(true);
        workflowSweeper.sweep(workflowId);
        verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
        verifyZeroInteractions(workflowExecutor, monitors, logger);

        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowId)).thenReturn(false);
        workflowSweeper.sweep(workflowId);
        verifyZeroInteractions(workflowRepairService);
        verifyZeroInteractions(workflowExecutor, monitors, logger);
    }

    @Test
    public void testUnack() throws Exception {
        WorkflowModel workflowModel = new WorkflowModel();
        List<TaskModel> tasks = Arrays.asList(new TaskModel(), new TaskModel());
        workflowModel.setTasks(tasks);
        when(workflowModel.getWorkflowId()).thenReturn("testWorkflowId");

        when(workflowModel.getTasks().get(0).getStatus()).thenReturn(Status.IN_PROGRESS);
        when(workflowModel.getTasks().get(0).getTaskType()).thenReturn(TaskType.TASK_TYPE_WAIT);
        workflowSweeper.unack(workflowModel, 10L);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "testWorkflowId", 11L * 1000);

        when(workflowModel.getTasks().get(0).getStatus()).thenReturn(Status.SCHEDULED);
        when(workflowModel.getTasks().get(0).getTaskType()).thenReturn(TaskType.TASK_TYPE_HUMAN);
        workflowSweeper.unack(workflowModel, 10L);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "testWorkflowId", 11L * 1000);

        when(workflowModel.getTasks().get(1).getStatus()).thenReturn(Status.IN_PROGRESS);
        when(workflowModel.getTasks().get(1).getTaskType()).thenReturn(TaskType.TASK_TYPE_TASK);
        workflowSweeper.unack(workflowModel, 10L);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "testWorkflowId", 11L * 1000);

        when(workflowModel.getTasks().get(1).getStatus()).thenReturn(Status.SCHEDULED);
        when(workflowModel.getTasks().get(1).getTaskType()).thenReturn(TaskType.TASK_TYPE_TASK);
        workflowSweeper.unack(workflowModel, 10L);
        verifyZeroInteractions(queueDAO);
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        long workflowOffsetTimeout = 10L;
        long workflowOffsetWithJitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertEquals(11L, workflowOffsetWithJitter);
    }
}