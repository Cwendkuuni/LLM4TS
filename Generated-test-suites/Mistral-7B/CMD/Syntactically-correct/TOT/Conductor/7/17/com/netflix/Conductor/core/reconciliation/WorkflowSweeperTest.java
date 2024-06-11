package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dao.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.metrics.Monitors;
import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

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
    private WorkflowSweeper sweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweeperWithValidWorkflowId() throws Exception {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);
        when(queueDAO.getWorkflowModel(workflowId)).thenReturn(workflowModel);

        // When
        CompletableFuture<Void> result = sweeper.sweepAsync(workflowId);

        // Then
        verify(workflowExecutor).decide(workflowId);
        ArgumentCaptor<WorkflowContext> workflowContextCaptor = ArgumentCaptor.forClass(WorkflowContext.class);
        verify(workflowExecutor).decide(workflowId, workflowContextCaptor.capture());
        WorkflowContext workflowContext = workflowContextCaptor.getValue();
        assertNotNull(workflowContext);

        ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowIdCaptor.capture());
        assertEquals(workflowId, workflowIdCaptor.getValue());

        verify(monitors).recordUnackTime(anyString(), eq(0L));

        result.join();
    }

    @Test
    public void testSweeperWithNotFoundWorkflowId() throws Exception {
        // Given
        String workflowId = "testWorkflowId";
        when(queueDAO.getWorkflowModel(workflowId)).thenThrow(new NotFoundException("Not found"));

        // When
        CompletableFuture<Void> result = sweeper.sweepAsync(workflowId);

        // Then
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(monitors).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"), any(Throwable.class));
        verifyNoMoreInteractions(workflowExecutor);

        result.join();
    }

    @Test
    public void testSweeperWithUnackedWorkflow() throws Exception {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);
        when(queueDAO.getWorkflowModel(workflowId)).thenReturn(workflowModel);

        // When
        CompletableFuture<Void> result = sweeper.sweepAsync(workflowId);

        // Then
        verify(workflowExecutor).decide(workflowId);
        ArgumentCaptor<WorkflowContext> workflowContextCaptor = ArgumentCaptor.forClass(WorkflowContext.class);
        verify(workflowExecutor).decide(workflowId, workflowContextCaptor.capture());
        WorkflowContext workflowContext = workflowContextCaptor.getValue();
        assertNotNull(workflowContext);

        ArgumentCaptor<WorkflowModel> workflowModelCaptor = ArgumentCaptor.forClass(WorkflowModel.class);
        ArgumentCaptor<Long> jitterCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowId, jitterCaptor.capture());
        Long jitter = jitterCaptor.getValue();
        assertEquals(ChronoUnit.SECONDS.toMillis(46L), jitter);

        verify(monitors).recordUnackTime(anyString(), eq(0L));

        result.join();
    }

    @Test
    public void testSweeperWithRepairedWorkflow() throws Exception {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);
        when(queueDAO.getWorkflowModel(workflowId)).thenReturn(workflowModel);
        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowId)).thenReturn(true);

        // When
        CompletableFuture<Void> result = sweeper.sweepAsync(workflowId);

        // Then
        verify(workflowExecutor).decide(workflowId);
        ArgumentCaptor<WorkflowContext> workflowContextCaptor = ArgumentCaptor.forClass(WorkflowContext.class);
        verify(workflowExecutor).decide(workflowId, workflowContextCaptor.capture());
        WorkflowContext workflowContext = workflowContextCaptor.getValue();
        assertNotNull(workflowContext);

        verifyNoInteractions(queueDAO);

        result.join();
    }

    @Test
    public void testUnackWithNoTasks() throws Exception {
        // Given
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        // When
        sweeper.unack(workflowModel, 1000L);

        // Then
        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testUnackWithOneTask() throws Exception {
        // Given
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("testTaskId");
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        workflowModel.setTasks(Arrays.asList(taskModel));

        // When
        sweeper.unack(workflowModel, 1000L);

        // Then
        ArgumentCaptor<WorkflowModel> workflowModelCaptor = ArgumentCaptor.forClass(WorkflowModel.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowModelCaptor.capture(), timeoutCaptor.capture());
        assertEquals(ChronoUnit.SECONDS.toMillis(1L), timeoutCaptor.getValue());
    }

    @Test
    public void testUnackWithTaskTypeWait() throws Exception {
        // Given
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("testTaskId");
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        workflowModel.setTasks(Arrays.asList(taskModel));

        // When
        sweeper.unack(workflowModel, 1000L);

        // Then
        ArgumentCaptor<WorkflowModel> workflowModelCaptor = ArgumentCaptor.forClass(WorkflowModel.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowModelCaptor.capture(), timeoutCaptor.capture());
        assertEquals(ChronoUnit.SECONDS.toMillis(51L), timeoutCaptor.getValue());
    }

    @Test
    public void testUnackWithTaskTypeTask() throws Exception {
        // Given
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("testTaskId");
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_TASK);
        workflowModel.setTasks(Arrays.asList(taskModel));

        // When
        sweeper.unack(workflowModel, 1000L);

        // Then
        ArgumentCaptor<WorkflowModel> workflowModelCaptor = ArgumentCaptor.forClass(WorkflowModel.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowModelCaptor.capture(), timeoutCaptor.capture());
        assertEquals(ChronoUnit.SECONDS.toMillis(51L), timeoutCaptor.getValue());
    }

    @Test
    public void testUnackWithWorkflowDefinition() throws Exception {
        // Given
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);
        when(workflowModel.getWorkflowDefinition()).thenReturn(new TaskDef());

        // When
        sweeper.unack(workflowModel, 1000L);

        // Then
        ArgumentCaptor<WorkflowModel> workflowModelCaptor = ArgumentCaptor.forClass(WorkflowModel.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowModelCaptor.capture(), timeoutCaptor.capture());
        assertEquals(ChronoUnit.SECONDS.toMillis(51L), timeoutCaptor.getValue());
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        // Given
        long workflowOffsetTimeout = 45L;

        // When
        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Then
        long expectedMin = ChronoUnit.SECONDS.toMillis(30L);
        long expectedMax = ChronoUnit.SECONDS.toMillis(60L);
        assertTrue(result >= expectedMin);
        assertTrue(result <= expectedMax);
    }
}