package com.netflix.conductor.core.reconciliation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncResult;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.QueueDAO;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper.WorkflowOffsetWithJitterFunction;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;
import com.netflix.conductor.testutils.TestWorkflowExecutor;
import com.netflix.conductor.testutils.TestWorkflowModel;
import com.netflix.conductor.testutils.TestWorkflowModel.TestTaskDefinition;
import com.netflix.conductor.testutils.TestWorkflowModel.TestWorkflowDefinition;

@Configuration
public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "testWorkflowId";

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private QueueDAO queueDAO;
    private WorkflowModel workflowModel;
    private ConductorProperties properties;
    private Monitors monitors;
    private TestWorkflowExecutor testWorkflowExecutor;
    private Random random;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        random = new Random();
        properties = mock(ConductorProperties.class);
        monitors = mock(Monitors.class);
        testWorkflowExecutor = new TestWorkflowExecutor();
        workflowExecutor = testWorkflowExecutor;
        queueDAO = mock(QueueDAO.class);
        WorkflowModel testWorkflowModel = new TestWorkflowModel();
        testWorkflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel = testWorkflowModel;
        WorkflowDefinition testWorkflowDefinition = new TestWorkflowDefinition();
        testWorkflowDefinition.setName("testWorkflowName");
        testWorkflowModel.setWorkflowDefinition(testWorkflowDefinition);
        properties.setAppId("testAppId");
        when(queueDAO.getWorkflow(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.of(workflowModel));
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, anyString(), anyLong()))
                .then(invocation -> new AsyncResult<>(null));
        when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).then(invocation -> null);

        WorkflowSweeperConfig config = new WorkflowSweeperConfig();
        config.setQueueDAO(queueDAO);
        config.setConductorProperties(properties);
        config.setMonitors(monitors);
        config.setWorkflowExecutor(workflowExecutor);
        workflowSweeper = new WorkflowSweeper();
        when(workflowSweeper.new WorkflowOffsetWithJitterFunction()).then(this::workflowOffsetWithJitter);
        workflowSweeper = new WorkflowSweeper();
        workflowSweeper.setProperties(properties);
        workflowSweeper.setQueueDAO(queueDAO);
        workflowSweeper.setMonitors(monitors);
        workflowSweeper.setWorkflowExecutor(workflowExecutor);
    }

    @Test
    public void testSweepAsync_whenWorkflowExists_shouldExecuteSweeperAndRemoveFromQueue() {
        // Given

        // When
        workflowSweeper.sweepAsync(WORKFLOW_ID).join();

        // Then
        assertThat(workflowModel.getStatus(), is(WorkflowStatus.TERMINAL));
        assertThat(queueDAO.getWorkflow(DECIDER_QUEUE, WORKFLOW_ID), nullValue());
    }

    @Test
    public void testSweepAsync_whenWorkflowNotFound_shouldRemoveFromQueue() {
        // Given
        when(queueDAO.getWorkflow(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.empty());

        // When
        workflowSweeper.sweepAsync(WORKFLOW_ID).join();

        // Then
        assertThat(queueDAO.getWorkflow(DECIDER_QUEUE, WORKFLOW_ID), nullValue());
    }

    @Test
    public void testSweepAsync_whenWorkflowNotFoundAndError_shouldRemoveFromQueueAndLogError() {
        // Given
        when(queueDAO.getWorkflow(DECIDER_QUEUE, WORKFLOW_ID)).thenThrow(new RuntimeException());

        // When
        workflowSweeper.sweepAsync(WORKFLOW_ID).join();

        // Then
        assertThat(queueDAO.getWorkflow(DECIDER_QUEUE, WORKFLOW_ID), nullValue());
        Monitors.error(WorkflowSweeper.class.getSimpleName(), "sweep");
    }

    @Test
    public void testSweep_whenWorkflowExists_shouldExecuteSweeperAndRemoveFromQueue() {
        // Given

        // When
        workflowSweeper.sweep(WORKFLOW_ID);

        // Then
        assertThat(workflowModel.getStatus(), is(WorkflowStatus.TERMINAL));
        assertThat(queueDAO.getWorkflow(DECIDER_QUEUE, WORKFLOW_ID), nullValue());
    }

    @Test
    public void testSweep_whenWorkflowNotFound_shouldReturn() {
        // Given
        when(queueDAO.getWorkflow(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.empty());

        // When
        workflowSweeper.sweep(WORKFLOW_ID);

        // Then
        // No-op
    }

    @Test
    public void testSweep_whenWorkflowNotFoundAndError_shouldReturnAndLogError() {
        // Given
        when(queueDAO.getWorkflow(DECIDER_QUEUE, WORKFLOW_ID)).thenThrow(new RuntimeException());

        // When
        workflowSweeper.sweep(WORKFLOW_ID);

        // Then
        Monitors.error(WorkflowSweeper.class.getSimpleName(), "sweep");
    }

    @Test
    public void testUnack_whenTasksInProgress_shouldUpdateUnackTimeout() {
        // Given
        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskDefinition(new TestTaskDefinition("testTaskType"));
        workflowModel.setTasks(Arrays.asList(taskModel));

        // When
        workflowSweeper.unack(workflowModel, 1000L);

        // Then
        long expectedUnackTimeout =
                taskModel.getTaskType().equals(TaskType.TASK_TYPE_WAIT)
                        || taskModel.getTaskType().equals(TaskType.TASK_TYPE_HUMAN)
                                ? 1001L
                                : 1001L;
        assertThat(queueDAO.getWorkflow(DECIDER_QUEUE, WORKFLOW_ID).map(workflow -> workflow.getUnackTimeout()),
                is(expectedUnackTimeout));
    }

    @Test
    public void testUnack_whenTasksScheduled_shouldUpdateUnackTimeout() {
        // Given
        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.SCHEDULED);
        taskModel.setTaskDefinition(new TestTaskDefinition("testTaskType"));
        WorkflowDefinition workflowDefinition = new TestWorkflowDefinition();
        workflowDefinition.setTimeoutSeconds(45);
        workflowModel.setTasks(Collections.singletonList(taskModel));
        workflowModel.setWorkflowDefinition(workflowDefinition);

        // When
        workflowSweeper.unack(workflowModel, 1000L);

        // Then
        long expectedUnackTimeout =
                1001L + workflowDefinition.getTimeoutSeconds() + 1;
        assertThat(queueDAO.getWorkflow(DECIDER_QUEUE, WORKFLOW_ID).map(workflow -> workflow.getUnackTimeout()),
                is(expectedUnackTimeout));
    }

    @Test
    public void testUnack_whenNoTasks_shouldUpdateUnackTimeout() {
        // Given
        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowDefinition workflowDefinition = new TestWorkflowDefinition();
        workflowDefinition.setTimeoutSeconds(45);
        workflowModel.setWorkflowDefinition(workflowDefinition);

        // When
        workflowSweeper.unack(workflowModel, 1000L);

        // Then
        long expectedUnackTimeout = 1001L + workflowDefinition.getTimeoutSeconds() + 1;
        assertThat(queueDAO.getWorkflow(DECIDER_QUEUE, WORKFLOW_ID).map(workflow -> workflow.getUnackTimeout()),
                is(expectedUnackTimeout));
    }

    @Test
    public void testWorkflowOffsetWithJitter_whenWorkflowOffsetTimeoutIsZero_shouldReturnZero() {
        // Given

        // When
        WorkflowOffsetWithJitterFunction workflowOffsetWithJitterFunction = workflowSweeper.new WorkflowOffsetWithJitterFunction();

        // Then
        long result = workflowOffsetWithJitterFunction.apply(0L);

        // Then
        assertThat(result, is(0L));
    }

    @Test
    public void testWorkflowOffsetWithJitter_whenWorkflowOffsetTimeoutIsPositive_shouldReturnValidValue() {
        // Given

        // When
        WorkflowOffsetWithJitterFunction workflowOffsetWithJitterFunction = workflowSweeper.new WorkflowOffsetWithJitterFunction();

        // Then
        long workflowOffsetTimeout = 45;
        long result = workflowOffsetWithJitterFunction.apply(workflowOffsetTimeout);

        // Then
        long expectedRange = workflowOffsetTimeout / 3;
        long expectedJitter = new Random().nextInt((int) (2 * expectedRange + 1)) - expectedRange;
        long expectedResult = workflowOffsetTimeout + expectedJitter;
        assertThat(result, is(expectedResult));
    }

    private long workflowOffsetWithJitter(long workflowOffsetTimeout) {
        long range = workflowOffsetTimeout / 3;
        long jitter = new Random().nextInt((int) (2 * range + 1)) - range;
        return workflowOffsetTimeout + jitter;
    }
}