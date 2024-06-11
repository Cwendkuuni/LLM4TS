package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

@Component
public class WorkflowSweeper {

 // Existing code...

}

public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "testWorkflowId";
    private static final long WORKFLOW_OFFSET_TIMEOUT = 45;
    private static final long WORKFLOW_OFFSET_WITH_JITTER = 60;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConductorProperties properties = mock(ConductorProperties.class);
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
        QueueDAO queueDAO = mock(QueueDAO.class);

        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(WORKFLOW_OFFSET_TIMEOUT);

        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

        this.sweeper = sweeper;
        this.workflowExecutor = workflowExecutor;
        this.workflowRepairService = workflowRepairService;
        this.queueDAO = queueDAO;
    }

    @Test
    public void testSweepAsync() {
        assertFalse(sweeper.sweepAsync(WORKFLOW_ID).join());
    }

    @Test
    public void testSweep() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(WORKFLOW_ID);
        workflow.setStatus(Status.CREATED);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);

        sweeper.sweep(WORKFLOW_ID);

        verifySweeperUnack(WORKFLOW_ID, WORKFLOW_OFFSET_TIMEOUT);
    }

    @Test(expected = NotFoundException.class)
    public void testSweepNotFound() {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException());

        sweeper.sweep(WORKFLOW_ID);
    }

    @Test
    public void testSweepWithRepairService() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(WORKFLOW_ID);
        workflow.setStatus(Status.FAILED);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);

        sweeper.sweep(WORKFLOW_ID);

        verifySweeperRepairTasks(WORKFLOW_ID);
    }

    @Test(expected = Exception.class)
    public void testSweepWithError() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(WORKFLOW_ID);
        workflow.setStatus(Status.CREATED);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);

        when(queueDAO.remove(anyString(), WORKFLOW_ID)).thenThrow(new RuntimeException());

        sweeper.sweep(WORKFLOW_ID);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setStatus(Status.CREATED);

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.SCHEDULED);
        taskModel.setTaskDefinition(new TaskDef());
        taskModel.setWorkflowModel(workflowModel);

        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(taskModel);
        workflowModel.setTasks(tasks);

        when(workflowModel.getTasks()).thenReturn(tasks);

        sweeper.unack(workflowModel, WORKFLOW_OFFSET_WITH_JITTER);

        ArgumentCaptor<Long> unackTimeoutCaptor = ArgumentCaptor.forClass(Long.class);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, unackTimeoutCaptor.capture());

        long actualUnackTimeout = unackTimeoutCaptor.getValue();
        assert (actualUnackTimeout >= WORKFLOW_OFFSET_WITH_JITTER && actualUnackTimeout <= WORKFLOW_OFFSET_WITH_JITTER * 2);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long actualWorkflowOffsetWithJitter = sweeper.workflowOffsetWithJitter(WORKFLOW_OFFSET_TIMEOUT);

        assert (actualWorkflowOffsetWithJitter >= WORKFLOW_OFFSET_TIMEOUT * 2/3 && actualWorkflowOffsetWithJitter <= WORKFLOW_OFFSET_TIMEOUT * 4/3);
    }

    private void verifySweeperUnack(String workflowId, long workflowOffsetTimeout) {
        ArgumentCaptor<WorkflowModel> workflowModelCaptor = ArgumentCaptor.forClass(WorkflowModel.class);
        ArgumentCaptor<Long> unackTimeoutCaptor = ArgumentCaptor.forClass(Long.class);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowId, unackTimeoutCaptor.capture());
        verify(workflowExecutor).decide(workflowId);

        long actualUnackTimeout = unackTimeoutCaptor.getValue();
        assert (actualUnackTimeout >= workflowOffsetTimeout && actualUnackTimeout <= workflowOffsetTimeout * 2);
    }

    private void verifySweeperRepairTasks(String workflowId) {
        verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
    }

    @Captor
    private ArgumentCaptor<WorkflowModel> workflowModelCaptor;

    private WorkflowSweeper sweeper;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private QueueDAO queueDAO;
}