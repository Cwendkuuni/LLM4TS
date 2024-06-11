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

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.WorkflowModel;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.test.TestRandom;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

@SuppressWarnings("unchecked")
public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-123";
    private static final String WORKFLOW_NAME = "workflow-name";
    private static final Instant WORKFLOW_START_TIME = Instant.now();
    private static final long WORKFLOW_OFFSET_TIMEOUT = 45;

    private WorkflowSweeper sweeper;
    private WorkflowExecutor workflowExecutorMock;
    private WorkflowRepairService workflowRepairServiceMock;
    private QueueDAO queueDAOMock;
    private ConductorProperties conductorPropertiesMock;
    private Monitors monitorsMock;

    private TestRandom testRandom;

    @Before
    public void setUp() {
        testRandom = new TestRandom();

        workflowExecutorMock = mock(WorkflowExecutor.class);
        workflowRepairServiceMock = mock(WorkflowRepairService.class);
        queueDAOMock = mock(QueueDAO.class);
        conductorPropertiesMock = mock(ConductorProperties.class);
        monitorsMock = mock(Monitors.class);

        sweeper = new WorkflowSweeper(
                workflowExecutorMock,
                Optional.of(workflowRepairServiceMock),
                conductorPropertiesMock,
                queueDAOMock);
    }

    @Test
    public void testSweepAsyncWithValidWorkflowId() {
        WorkflowModel workflowModel = getWorkflowModel();
        when(queueDAOMock.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.ofNullable(workflowModel));
        when(workflowExecutorMock.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        sweeper.sweepAsync(WORKFLOW_ID);

        verify(workflowExecutorMock).decide(WORKFLOW_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testSweepAsyncWithInvalidWorkflowId() {
        when(queueDAOMock.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.empty());

        sweeper.sweepAsync(WORKFLOW_ID);

        verify(queueDAOMock, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepWithWorkflowNotFound() {
        WorkflowModel workflowModel = getWorkflowModel();
        when(queueDAOMock.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.ofNullable(workflowModel));
        when(workflowExecutorMock.decide(WORKFLOW_ID)).thenThrow(new NotFoundException("Workflow not found"));

        sweeper.sweep(WORKFLOW_ID);

        verify(queueDAOMock, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = getWorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.SCHEDULED);
        workflowModel.setTasks(Collections.singletonList(taskModel));

        sweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);

        ArgumentCaptor<Long> unackTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAOMock).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), unackTimeoutCaptor.capture());
        long actualUnackTimeout = unackTimeoutCaptor.getValue();
        assertEquals(WORKFLOW_OFFSET_TIMEOUT * 1000, actualUnackTimeout);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;

        long actualWorkflowOffsetWithJitter = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        long expectedMinJitter = workflowOffsetTimeout / 3;
        long expectedMaxJitter = workflowOffsetTimeout * 2 / 3;
        long actualMinJitter = actualWorkflowOffsetWithJitter - workflowOffsetTimeout;
        long actualMaxJitter = actualWorkflowOffsetWithJitter + workflowOffsetTimeout;

        assertTrue("The minimum jitter should be less than the workflow offset timeout",
                actualMinJitter < workflowOffsetTimeout);
        assertTrue("The maximum jitter should be greater than the workflow offset timeout",
                actualMaxJitter > workflowOffsetTimeout);
        assertTrue("The minimum jitter should be within the expected range",
                actualMinJitter >= expectedMinJitter);
        assertTrue("The maximum jitter should be within the expected range",
                actualMaxJitter <= expectedMaxJitter);
    }

    private WorkflowModel getWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setStatus(Status.RUNNING);
        workflowModel.setStartTime(WORKFLOW_START_TIME);
        return workflowModel;
    }
}