package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dao.QueueDAO;
import com.netflix.conductor.core.WorkflowExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor.Decision;
import com.netflix.conductor.core.metrics.Monitors;
import com.netflix.conductor.core.model.TaskModel.Status;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.Utils;

@Configuration
public class TestConfig {

    @Bean
    public WorkflowExecutor workflowExecutor() {
        return mock(WorkflowExecutor.class);
    }

    @Bean
    public QueueDAO queueDAO() {
        return mock(QueueDAO.class);
    }

    @Bean
    public Monitors monitors() {
        return mock(Monitors.class);
    }

    @Bean
    public ConductorProperties conductorProperties() {
        return mock(ConductorProperties.class);
    }

    @Bean
    public WorkflowRepairService workflowRepairService() {
        return mock(WorkflowRepairService.class);
    }
}

@InjectMocks
public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeper.class);

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync_workflowExists() throws Exception {
        // Given
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.IN_PROGRESS);
        when(queueDAO.findById(DECIDER_QUEUE, workflowId)).thenReturn(Optional.ofNullable(workflowModel));
        when(properties.getAppId()).thenReturn("test-app-id");
        WorkflowContext workflowContext = new WorkflowContext("test-app-id");
        when(workflowExecutor.decide(workflowId)).thenReturn(Decision.APPROVE);

        // When
        WorkflowSweeper sweeper = new WorkflowSweeper(
                workflowExecutor, Optional.ofNullable(workflowRepairService), properties, queueDAO);
        CompletableFuture<Void> result = sweeper.sweepAsync(workflowId);

        // Then
        result.join();
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verifyNoMoreInteractions(queueDAO);
    }

    @Test
    public void testSweepAsync_workflowNotFound() throws Exception {
        // Given
        String workflowId = "test-workflow-id";
        when(queueDAO.findById(DECIDER_QUEUE, workflowId)).thenReturn(Optional.empty());

        // When
        WorkflowSweeper sweeper = new WorkflowSweeper(
                workflowExecutor, Optional.ofNullable(workflowRepairService), properties, queueDAO);
        CompletableFuture<Void> result = sweeper.sweepAsync(workflowId);

        // Then
        result.join();
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verifyZeroInteractions(monitors);
        verify(queueDAO).logInfo(
                "Workflow NOT found for id:{}. Removed it from decider queue", workflowId);
    }

    @Test
    public void testSweep_workflowInProgress() throws Exception {
        // Given
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.IN_PROGRESS);
        when(queueDAO.findById(DECIDER_QUEUE, workflowId)).thenReturn(Optional.ofNullable(workflowModel));
        when(properties.getAppId()).thenReturn("test-app-id");
        WorkflowContext workflowContext = new WorkflowContext("test-app-id");
        when(workflowExecutor.decide(workflowId)).thenReturn(Decision.APPROVE);

        // When
        WorkflowSweeper sweeper = new WorkflowSweeper(
                workflowExecutor, Optional.ofNullable(workflowRepairService), properties, queueDAO);
        sweeper.sweep(workflowId);

        // Then
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verifyZeroInteractions(monitors);
    }

    @Test
    public void testSweep_workflowTerminal() throws Exception {
        // Given
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.COMPLETED);
        when(queueDAO.findById(DECIDER_QUEUE, workflowId)).thenReturn(Optional.ofNullable(workflowModel));
        when(properties.getAppId()).thenReturn("test-app-id");
        WorkflowContext workflowContext = new WorkflowContext("test-app-id");

        // When
        WorkflowSweeper sweeper = new WorkflowSweeper(
                workflowExecutor, Optional.ofNullable(workflowRepairService), properties, queueDAO);
        sweeper.sweep(workflowId);

        // Then
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verifyZeroInteractions(monitors);
    }

    @Test
    public void testSweep_workflowNotFoundAndUnack() throws Exception {
        // Given
        String workflowId = "test-workflow-id";
        when(queueDAO.findById(DECIDER_QUEUE, workflowId)).thenReturn(Optional.empty());

        // When
        WorkflowSweeper sweeper = new WorkflowSweeper(
                workflowExecutor, Optional.ofNullable(workflowRepairService), properties, queueDAO);
        sweeper.sweep(workflowId);

        // Then
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowId, workflowOffsetWithJitter(15));
        verifyZeroInteractions(monitors);
    }

    @Test
    public void testSweep_workflowNotInProgress() throws Exception {
        // Given
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.FAILED);
        when(queueDAO.findById(DECIDER_QUEUE, workflowId)).thenReturn(Optional.ofNullable(workflowModel));
        when(properties.getAppId()).thenReturn("test-app-id");
        WorkflowContext workflowContext = new WorkflowContext("test-app-id");

        // When
        WorkflowSweeper sweeper = new WorkflowSweeper(
                workflowExecutor, Optional.ofNullable(workflowRepairService), properties, queueDAO);
        sweeper.sweep(workflowId);

        // Then
        verifyZeroInteractions(queueDAO);
        verifyZeroInteractions(monitors);
    }

    @Test
    public void testUnack_inProgressWaitTask() throws Exception {
        // Given
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("test-workflow-id");
        TaskModel taskModel = new TaskModel();
        taskModel.setWorkflowId(workflowModel.getWorkflowId());
        taskModel.setTaskDefinition(new TaskDef());
        taskModel.setTaskDefinition().setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setStatus(Status.IN_PROGRESS);
        List<TaskModel> tasks = Arrays.asList(taskModel);
        workflowModel.setTasks(tasks);

        // When
        WorkflowSweeper sweeper = new WorkflowSweeper();
        long timeout = sweeper.workflowOffsetWithJitter(15);
        sweeper.unack(workflowModel, timeout);

        // Then
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), timeout * 1000);
        verifyZeroInteractions(monitors);
    }

    @Test
    public void testUnack_inProgressTask() throws Exception {
        // Given
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("test-workflow-id");
        TaskModel taskModel = new TaskModel();
        taskModel.setWorkflowId(workflowModel.getWorkflowId());
        taskModel.setStatus(Status.IN_PROGRESS);
        List<TaskModel> tasks = Arrays.asList(taskModel);
        workflowModel.setTasks(tasks);

        // When
        WorkflowSweeper sweeper = new WorkflowSweeper();
        long timeout = sweeper.workflowOffsetWithJitter(15);
        sweeper.unack(workflowModel, timeout);

        // Then
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), timeout * 1000);
        verifyZeroInteractions(monitors);
    }

    @Test
    public void testUnack_scheduledTask() throws Exception {
        // Given
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("test-workflow-id");
        TaskModel taskModel = new TaskModel();
        taskModel.setWorkflowId(workflowModel.getWorkflowId());
        taskModel.setStatus(Status.SCHEDULED);
        when(taskModel.getTaskDefinition()).thenReturn(new TaskDef());
        List<TaskModel> tasks = Arrays.asList(taskModel);
        workflowModel.setTasks(tasks);
        when(workflowModel.getWorkflowDefinition()).thenReturn(new WorkflowModel());

        // When
        WorkflowSweeper sweeper = new WorkflowSweeper();
        long timeout = sweeper.workflowOffsetWithJitter(15);
        sweeper.unack(workflowModel, timeout);

        // Then
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), timeout * 1000);
        verifyZeroInteractions(monitors);
    }

    @Test
    public void testUnack_completedWorkflow() throws Exception {
        // Given
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("test-workflow-id");
        when(workflowModel.getStatus()).thenReturn(Status.COMPLETED);

        // When
        WorkflowSweeper sweeper = new WorkflowSweeper();
        long timeout = sweeper.workflowOffsetWithJitter(15);
        sweeper.unack(workflowModel, timeout);

        // Then
        verifyZeroInteractions(queueDAO);
        verifyZeroInteractions(monitors);
    }

    @Test
    public void testUnack_failedWorkflow() throws Exception {
        // Given
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("test-workflow-id");
        when(workflowModel.getStatus()).thenReturn(Status.FAILED);

        // When
        WorkflowSweeper sweeper = new WorkflowSweeper();
        long timeout = sweeper.workflowOffsetWithJitter(15);
        sweeper.unack(workflowModel, timeout);

        // Then
        verifyZeroInteractions(queueDAO);
        verifyZeroInteractions(monitors);
    }

    @VisibleForTesting
    long workflowOffsetWithJitter(long workflowOffsetTimeout) {
        long range = workflowOffsetTimeout / 3;
        long jitter = new Random().nextInt((int) (2 * range + 1)) - range;
        return workflowOffsetTimeout + jitter;
    }
}