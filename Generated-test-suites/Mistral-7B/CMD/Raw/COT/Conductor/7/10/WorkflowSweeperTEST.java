package com.netflix.conductor.core.reconciliation.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.WorkflowExecutor;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutorTestUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;
import static com.netflix.conductor.core.utils.Utils.DECIDER_QUEUE;

@ComponentScan(basePackages = "com.netflix.conductor.core.reconciliation")
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
    private WorkflowContext workflowContext;

    @Mock
    private Logger logger;

    @InjectMocks
    private WorkflowSweeper sweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(new Random(1L));
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, anyString())).thenReturn(0L);
        when(queueDAO.remove(DECIDER_QUEUE, anyString())).thenReturn(true);
        when(workflowExecutor.decide(anyString())).thenReturn(new WorkflowModel());
        when(workflowExecutor.decide(anyString())).thenThrow(new NotFoundException(""));
        when(workflowRepairService.verifyAndRepairWorkflowTasks(anyString())).thenReturn(Collections.emptyList());
        when(logger.isInfoEnabled()).thenReturn(true);
    }

    @After
    public void teardown() {
        reset(properties, workflowExecutor, workflowRepairService, queueDAO, workflowContext, logger);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        sweeper.sweepAsync(workflowId);
        verify(workflowExecutor, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweepNoSuchWorkflow() {
        String workflowId = "testWorkflowId";
        sweeper.sweep(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepSuccess() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = new WorkflowModel();
        WorkflowModel expectedWorkflow = new WorkflowModel();
        expectedWorkflow.setWorkflowId(workflowId);
        expectedWorkflow.setStatus(Status.RUNNING);
        expectedWorkflow.setWorkflowName("testWorkflowName");
        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);

        sweeper.sweep(workflowId);

        ArgumentCaptor<WorkflowModel> workflowArgumentCaptor = ArgumentCaptor.forClass(WorkflowModel.class);
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void testSweepWorkflowNotFound() {
        String workflowId = "testWorkflowId";
        sweeper.sweep(workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        List<TaskModel> tasks = Arrays.asList(new TaskModel(), new TaskModel());
        workflowModel.setTasks(tasks);

        sweeper.unack(workflowModel, 1000L);

        ArgumentCaptor<WorkflowModel> workflowArgumentCaptor = ArgumentCaptor.forClass(WorkflowModel.class);
        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 1000L;
        long expectedJitter = 333L;
        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result >= (workflowOffsetTimeout - expectedJitter) &&
                result <= (workflowOffsetTimeout + expectedJitter));
    }
}