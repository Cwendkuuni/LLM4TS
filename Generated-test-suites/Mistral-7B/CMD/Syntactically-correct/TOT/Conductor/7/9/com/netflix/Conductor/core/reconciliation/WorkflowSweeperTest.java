package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.AsyncResult;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.SchedulerConfiguration;
import com.netflix.conductor.core.WorkflowExecutor;
import com.netflix.conductor.core.WorkflowModel;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutorMock;
import com.netflix.conductor.dao.QueueDAOMock;
import com.netflix.conductor.dao.WorkflowRepairServiceMock;
import com.netflix.conductor.model.WorkflowModelMock;
import com.netflix.conductor.metrics.MonitorsMock;

@SuppressWarnings("unchecked")
public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflowId";
    private static final String WORKFLOW_NAME = "workflowName";

    private WorkflowSweeper sweeperUnderTest;
    private WorkflowExecutorMock workflowExecutorMock;
    private QueueDAOMock queueDAOMock;
    private WorkflowRepairServiceMock workflowRepairServiceMock;
    private MonitorsMock monitorsMock;
    private ConductorProperties conductorPropertiesMock;
    private ApplicationContext context;

    @Before
    public void setup() {
        conductorPropertiesMock = Mockito.mock(ConductorProperties.class);
        queueDAOMock = new QueueDAOMock();
        workflowExecutorMock = new WorkflowExecutorMock();
        workflowRepairServiceMock = new WorkflowRepairServiceMock();
        monitorsMock = new MonitorsMock();
        context = Mockito.mock(ApplicationContext.class);

        when(context.getBean(WorkflowExecutor.class)).thenReturn(workflowExecutorMock);
        when(context.getBean(QueueDAO.class)).thenReturn(queueDAOMock);
        when(context.getBean(WorkflowRepairService.class)).thenReturn(workflowRepairServiceMock);
        when(context.getBean(Monitors.class)).thenReturn(monitorsMock);

        sweeperUnderTest = new WorkflowSweeper(
                workflowExecutorMock, workflowRepairServiceMock, conductorPropertiesMock, queueDAOMock);
    }

    @Test
    public void testSweep_WorkflowExistsAndIsTerminal() {
        // Given
        WorkflowModelMock workflowModel = new WorkflowModelMock();
        workflowModel.setStatus(Status.COMPLETED);

        givenWorkflowModelWhenDecidedReturns(workflowModel);

        // When
        sweeperUnderTest.sweep(WORKFLOW_ID);

        // Then
        verify(queueDAOMock).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verifyNoMoreInteractions(workflowExecutorMock);
    }

    @Test
    public void testSweep_WorkflowNotFound() {
        // Given

        // When
        sweeperUnderTest.sweep(WORKFLOW_ID);

        // Then
        verify(queueDAOMock).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(monitorsMock).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"));
        verifyNoMoreInteractions(workflowExecutorMock);
    }

    @Test
    public void testSweep_WorkflowNotTerminal_TaskIsWaiting() {
        // Given
        WorkflowModelMock workflowModel = new WorkflowModelMock();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskDefinition(new TaskDef());
        workflowModel.setTasks(Arrays.asList(taskModel));

        givenWorkflowModelWhenDecidedReturns(workflowModel);

        // When
        sweeperUnderTest.sweep(WORKFLOW_ID);

        // Then
        verify(queueDAOMock).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
        verifyNoMoreInteractions(workflowExecutorMock);
    }

    @Test
    public void testSweep_WorkflowNotTerminal_TaskIsScheduled() {
        // Given
        WorkflowModelMock workflowModel = new WorkflowModelMock();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.SCHEDULED);
        taskModel.setTaskDefinition(new TaskDef());
        workflowModel.setTasks(Arrays.asList(taskModel));

        givenWorkflowModelWhenDecidedReturns(workflowModel);

        // When
        sweeperUnderTest.sweep(WORKFLOW_ID);

        // Then
        verify(queueDAOMock).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
        verifyNoMoreInteractions(workflowExecutorMock);
    }

    @Test
    public void testSweep_WorkflowNotTerminal_UnhandledError() {
        // Given
        WorkflowModelMock workflowModel = new WorkflowModelMock();

        givenWorkflowModelWhenDecidedReturns(workflowModel);

        // When
        doThrow(new RuntimeException("Test error")).when(workflowExecutorMock).decide(WORKFLOW_ID);

        // Then
        sweeperUnderTest.sweep(WORKFLOW_ID);

        // Then
        verify(queueDAOMock).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
        verifyNoMoreInteractions(workflowExecutorMock);
    }

    @Test
    public void testUnack_WorkflowNotFound() {
        // Given

        // When
        sweeperUnderTest.unack(Collections.emptyList(), 1L);

        // Then
        verify(queueDAOMock).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
    }

    @Test
    public void testUnack_WorkflowFound() {
        // Given
        WorkflowModelMock workflowModel = new WorkflowModelMock();
        WorkflowModel workflowModelFromDB = new WorkflowModelMock();

        givenWorkflowModelFromDBWhenGetFromDBReturns(workflowModelFromDB);

        // When
        sweeperUnderTest.unack(Collections.singletonList(workflowModel), 1L);

        // Then
        verify(queueDAOMock).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
        verify(workflowExecutorMock).decide(WORKFLOW_ID);
    }

    @Test
    public void testWorkflowOffsetWithJitter_ReturnsExpectedOffset() {
        // Given
        long workflowOffsetTimeout = 45;

        // When
        long result = sweeperUnderTest.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Then
        long expected = workflowOffsetTimeout + new Random().nextInt(2 * (workflowOffsetTimeout / 3) + 1);
        assertEquals(expected, result);
    }

    @Test
    public void testSweepAsync_WorkflowExistsAndIsTerminal() throws Exception {
        // Given
        WorkflowModelMock workflowModel = new WorkflowModelMock();
        workflowModel.setStatus(Status.COMPLETED);

        givenWorkflowModelWhenDecidedReturns(workflowModel);

        // When
        sweeperUnderTest.sweepAsync(WORKFLOW_ID).get(10, TimeUnit.SECONDS);

        // Then
        verify(queueDAOMock).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verifyNoMoreInteractions(workflowExecutorMock);
    }

    @Test
    public void testSweepAsync_WorkflowNotFound() throws Exception {
        // Given

        // When
        CompletableFuture<Void> future = sweeperUnderTest.sweepAsync(WORKFLOW_ID);

        // Then
        future.get(10, TimeUnit.SECONDS);
        verify(monitorsMock).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"));
        verifyNoMoreInteractions(workflowExecutorMock);
    }

    private void givenWorkflowModelWhenDecidedReturns(WorkflowModelMock workflowModel) {
        when(workflowExecutorMock.decide(WORKFLOW_ID)).thenReturn(new AsyncResult<>(workflowModel));
    }

    private WorkflowModelMock givenWorkflowModelFromDBWhenGetFromDBReturns(WorkflowModelMock workflowModel) {
        when(queueDAOMock.getWorkflowModel(WORKFLOW_ID)).thenReturn(workflowModel);
        return workflowModel;
    }
}