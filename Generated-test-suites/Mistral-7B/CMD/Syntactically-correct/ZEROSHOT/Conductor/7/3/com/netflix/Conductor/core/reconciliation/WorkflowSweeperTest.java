package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import com.netflix.conductor.metrics.Monitors;

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
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(properties, workflowExecutor, workflowRepairService, queueDAO, monitors);
    }

    @Test
    public void testSweepAsync() throws Exception {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper).sweep(workflowId);
    }

    @Test
    public void testSweep() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setId(workflowId);
        workflow.setStatus(WorkflowStatus.RUNNING);
        workflow.setWorkflowName("testWorkflowName");

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.of(workflow));
        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(monitors).recordUnackTime("testWorkflowName", anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void testSweepNotFound() throws Exception {
        String workflowId = "testWorkflowId";
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.empty());

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setId("testWorkflowId");
        workflowModel.setStatus(WorkflowStatus.RUNNING);
        workflowModel.setWorkflowName("testWorkflowName");

        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(new TaskModel());
        tasks.add(new TaskModel());

        workflowModel.setTasks(tasks);

        when(queueDAO.get(DECIDER_QUEUE, workflowModel.getId())).thenReturn(Optional.of(workflowModel));

        workflowSweeper.unack(workflowModel, 5L);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getId(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 5L;

        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(jitter >= (workflowOffsetTimeout - 2));
        assertTrue(jitter <= (workflowOffsetTimeout + 2));
    }
}