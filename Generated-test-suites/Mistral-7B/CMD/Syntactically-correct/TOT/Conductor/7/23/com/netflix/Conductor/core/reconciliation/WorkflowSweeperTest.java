package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.WorkflowModelRepository;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metrics.Monitors;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper.WorkflowOffsetWithJitterFunction;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;

public class WorkflowSweeperTest {

    private QueueDAO queueDAO;
    private WorkflowModelRepository workflowModelRepository;
    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutorMock;
    private WorkflowRepairService workflowRepairServiceMock;
    private Logger loggerMock;
    private ConductorProperties conductorPropertiesMock;

    @Before
    public void setup() {
        queueDAO = Mockito.mock(QueueDAO.class);
        workflowModelRepository = Mockito.mock(WorkflowModelRepository.class);
        workflowExecutorMock = Mockito.mock(WorkflowExecutor.class);
        workflowRepairServiceMock = Mockito.mock(WorkflowRepairService.class);
        loggerMock = Mockito.mock(Logger.class);
        conductorPropertiesMock = Mockito.mock(ConductorProperties.class);

        when(queueDAO.remove(Utils.DECIDER_QUEUE, anyString())).thenReturn(true);
        when(workflowModelRepository.findById(anyString())).thenAnswer(
                invocation -> Optional.ofNullable(new WorkflowModel()));

        WorkflowSweeper.WorkflowOffsetWithJitterFunction workflowOffsetWithJitterFunction =
                new WorkflowSweeper.WorkflowOffsetWithJitterFunction();
        when(conductorPropertiesMock.getWorkflowOffsetTimeout()).thenReturn(5L);

        workflowSweeper = new WorkflowSweeper(
                workflowExecutorMock, workflowRepairServiceMock, conductorPropertiesMock, queueDAO);
        workflowSweeper.setWorkflowModelRepository(workflowModelRepository);
    }

    @Test
    public void testSweep_workflowFoundAndTerminalStatus_queueRemoved() {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.TERMINALED);

        when(workflowModelRepository.findById(workflowId)).thenReturn(Optional.of(workflowModel));

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verify(queueDAO).remove(Utils.DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep_workflowNotFound_queueRemoved() {
        // Given
        String workflowId = "testWorkflowId";

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verify(queueDAO).remove(Utils.DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep_workflowNotFound_errorLogged() {
        // Given
        String workflowId = "testWorkflowId";

        // When
        doThrow(new RuntimeException("Unexpected error"))
                .when(workflowModelRepository)
                .findById(workflowId);

        workflowSweeper.sweep(workflowId);

        // Then
        verify(loggerMock).error("Error running sweep for " + workflowId, any(RuntimeException.class));
    }

    @Test
    public void testSweep_workflowWithTasks_unackTimeoutSet() {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.STARTED);
        workflowModel.setTasks(Arrays.asList(
                new TaskModel(),
                new TaskModel(),
                new TaskModel()));

        when(workflowModelRepository.findById(workflowId)).thenReturn(Optional.of(workflowModel));
        when(workflowExecutorMock.decide(workflowId)).thenReturn(workflowModel);

        // When
        workflowSweeper.sweep(workflowId);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(queueNameCaptor.capture(), eq(workflowId), timeoutCaptor.capture());

        long timeout = timeoutCaptor.getValue();
        long expectedTimeout = 6L; // (3 * workflowOffsetTimeout) + 1
        assertEquals(expectedTimeout, timeout);
    }

    @Test
    public void testUnack_workflowWithNoTasks_unackTimeoutSet() {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.STARTED);

        when(workflowModelRepository.findById(workflowId)).thenReturn(Optional.of(workflowModel));

        // When
        workflowSweeper.unack(workflowModel, 5L);

        // Then
        verify(queueDAO).setUnackTimeout(
                Utils.DECIDER_QUEUE, workflowModel.getWorkflowId(), eq(5L * 1000));
    }

    @Test
    public void testUnack_workflowWithTasks_unackTimeoutSetForLongestTask() {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.STARTED);
        workflowModel.setTasks(Arrays.asList(
                new TaskModel(),
                new TaskModel(),
                new TaskModel() {{
                    setResponseTimeoutSeconds(100L);
                }}));

        when(workflowModelRepository.findById(workflowId)).thenReturn(Optional.of(workflowModel));
        when(workflowExecutorMock.decide(workflowId)).thenReturn(workflowModel);

        // When
        workflowSweeper.unack(workflowModel, 5L);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(queueNameCaptor.capture(), eq(workflowId), timeoutCaptor.capture());

        long timeout = timeoutCaptor.getValue();
        long expectedTimeout = 101L;
        assertEquals(expectedTimeout, timeout);
    }

    @Test
    public void testUnack_workflowWithTasks_unackTimeoutSetForShortestTask() {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.STARTED);
        workflowModel.setTasks(Arrays.asList(
                new TaskModel() {{
                    setResponseTimeoutSeconds(10L);
                }},
                new TaskModel(),
                new TaskModel()));

        when(workflowModelRepository.findById(workflowId)).thenReturn(Optional.of(workflowModel));
        when(workflowExecutorMock.decide(workflowId)).thenReturn(workflowModel);

        // When
        workflowSweeper.unack(workflowModel, 5L);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(queueNameCaptor.capture(), eq(workflowId), timeoutCaptor.capture());

        long timeout = timeoutCaptor.getValue();
        long expectedTimeout = 11L;
        assertEquals(expectedTimeout, timeout);
    }

    @Test
    public void testUnack_workflowWithNoTaskDefinitions_unackTimeoutSetForWorkflowDefinition() {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.STARTED);

        when(workflowModelRepository.findById(workflowId)).thenReturn(Optional.of(workflowModel));

        // When
        workflowSweeper.unack(workflowModel, 5L);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(queueNameCaptor.capture(), eq(workflowId), timeoutCaptor.capture());

        long timeout = timeoutCaptor.getValue();
        long expectedTimeout = 6L * 1000 + 1;
        assertEquals(expectedTimeout, timeout);
    }

    @Test
    public void testWorkflowOffsetWithJitter_smallJitter() {
        // Given
        long workflowOffsetTimeout = 5L;

        // When
        long actual = workflowOffsetWithJitterFunction.apply(workflowOffsetTimeout);

        // Then
        long expected = 4L; // (3 * workflowOffsetTimeout) + 1
        assertEquals(expected, actual);
    }

    @Test
    public void testWorkflowOffsetWithJitter_mediumJitter() {
        // Given
        long workflowOffsetTimeout = 10L;

        // When
        long actual = workflowOffsetWithJitterFunction.apply(workflowOffsetTimeout);

        // Then
        long expected = 8L; // (3 * workflowOffsetTimeout) + 1
        assertEquals(expected, actual);
    }

    @Test
    public void testWorkflowOffsetWithJitter_largeJitter() {
        // Given
        long workflowOffsetTimeout = 20L;

        // When
        long actual = workflowOffsetWithJitterFunction.apply(workflowOffsetTimeout);

        // Then
        long expected = 16L; // (3 * workflowOffsetTimeout) + 1
        assertEquals(expected, actual);
    }

    @Test
    public void testMonitors_recordUnackTime_timeRecorded() throws InterruptedException {
        // Given
        String workflowName = "testWorkflowName";
        long startTime = Instant.now().toEpochMilli();

        // When
        workflowSweeper.unack(new WorkflowModel(), 5L);
        TimeUnit.SECONDS.sleep(1);
        long endTime = Instant.now().toEpochMilli();

        // Then
        verify(Monitors.class).recordUnackTime(workflowName, endTime - startTime);
    }
}