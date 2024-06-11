package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.SchedulerConfiguration;
import com.netflix.conductor.core.dao.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metrics.Monitors;
import com.netflix.conductor.core.model.TaskModel.Status;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.scheduler.SweeperExecutor;
import com.netflix.conductor.core.test.ConductorTestHelper;
import com.netflix.conductor.core.test.WorkflowModelTestHelper;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAOTest;

public class WorkflowSweeperTest {

    @org.springframework.beans.factory.annotation.Autowired
    private QueueDAO queueDAO;

    @org.springframework.beans.factory.annotation.Autowired
    private WorkflowExecutor workflowExecutor;

    @org.springframework.beans.factory.annotation.Autowired
    private Monitors monitors;

    @Captor
    private ArgumentCaptor<TaskModel> taskModelCaptor;

    @VisibleForTesting
    private static final String WORKFLOW_ID = "testWorkflowId";

    @Test
    public void testSweeper_validWorkflow_returns() {
        // Given
        ConductorProperties properties = new ConductorProperties();
        properties.setAppId("testAppId");
        properties.setWorkflowOffsetTimeout(SweeperExecutor.DEFAULT_WORKFLOW_OFFSET_TIMEOUT);

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowModel workflowModel = WorkflowModelTestHelper.buildWorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);

        WorkflowSweeper sweeper = new WorkflowSweeper(
                workflowExecutor,
                null,
                properties,
                queueDAO);

        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.ofNullable(workflowModel));

        // When
        sweeper.sweep(WORKFLOW_ID);

        // Then
        verifySweeperCall(workflowModel, 0);
    }

    @Test
    public void testSweeper_nonExistentWorkflow_removes() {
        // Given
        ConductorProperties properties = new ConductorProperties();
        properties.setAppId("testAppId");
        properties.setWorkflowOffsetTimeout(SweeperExecutor.DEFAULT_WORKFLOW_OFFSET_TIMEOUT);

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowModel workflowModel = null;

        WorkflowSweeper sweeper = new WorkflowSweeper(
                workflowExecutor,
                null,
                properties,
                queueDAO);

        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.ofNullable(workflowModel));

        // When
        sweeper.sweep(WORKFLOW_ID);

        // Then
        verifyQueueRemove(WORKFLOW_ID);
    }

    @Test
    public void testSweeper_workflowWithTasks_unacks() {
        // Given
        ConductorProperties properties = new ConductorProperties();
        properties.setAppId("testAppId");
        properties.setWorkflowOffsetTimeout(SweeperExecutor.DEFAULT_WORKFLOW_OFFSET_TIMEOUT);

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowModel workflowModel = WorkflowModelTestHelper.buildWorkflowModelWithTasks();
        workflowModel.setWorkflowId(WORKFLOW_ID);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskDefinition(new TaskDef());
        taskModel.setTaskId("testTaskId");
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setTasks(Arrays.asList(taskModel));

        WorkflowSweeper sweeper = new WorkflowSweeper(
                workflowExecutor,
                null,
                properties,
                queueDAO);

        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.ofNullable(workflowModel));

        // When
        sweeper.sweeper(WORKFLOW_ID);

        // Then
        verifyUnack(WORKFLOW_ID, 1);
    }

    @Test
    public void testSweeper_workflowWithNoTasks_doesNotUnack() {
        // Given
        ConductorProperties properties = new ConductorProperties();
        properties.setAppId("testAppId");
        properties.setWorkflowOffsetTimeout(SweeperExecutor.DEFAULT_WORKFLOW_OFFSET_TIMEOUT);

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowModel workflowModel = WorkflowModelTestHelper.buildWorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);

        WorkflowSweeper sweeper = new WorkflowSweeper(
                workflowExecutor,
                null,
                properties,
                queueDAO);

        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.ofNullable(workflowModel));

        // When
        sweeper.sweeper(WORKFLOW_ID);

        // Then
        verifyNoUnack(WORKFLOW_ID);
    }

    @Test
    public void testSweeper_workflowWithTaskDefNotPresent_doesNotUnack() {
        // Given
        ConductorProperties properties = new ConductorProperties();
        properties.setAppId("testAppId");
        properties.setWorkflowOffsetTimeout(SweeperExecutor.DEFAULT_WORKFLOW_OFFSET_TIMEOUT);

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowModel workflowModel = WorkflowModelTestHelper.buildWorkflowModelWithTaskDefNotPresent();
        workflowModel.setWorkflowId(WORKFLOW_ID);

        WorkflowSweeper sweeper = new WorkflowSweeper(
                workflowExecutor,
                null,
                properties,
                queueDAO);

        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.ofNullable(workflowModel));

        // When
        sweeper.sweeper(WORKFLOW_ID);

        // Then
        verifyNoUnack(WORKFLOW_ID);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        // Given
        long workflowOffsetTimeout = 10000;

        // When
        long workflowOffsetWithJitter = new WorkflowSweeper().workflowOffsetWithJitter(workflowOffsetTimeout);

        // Then
        long expectedJitter = workflowOffsetTimeout / 3;
        long offset = workflowOffsetWithJitter - workflowOffsetTimeout;
        long expectedOffset = Math.min(workflowOffsetTimeout + expectedJitter, Long.MAX_VALUE) - expectedJitter;

        // Check
        assertThat(workflowOffsetWithJitter, greaterThan(expectedOffset));
        assertThat(workflowOffsetWithJitter, lessThan(expectedOffset + expectedJitter * 2));
    }

    private void verifySweeperCall(WorkflowModel workflowModel, int expectedUnackTimeout) {
        sweeper.sweeper(WORKFLOW_ID);

        verify(workflowExecutor)
                .decide(eq(WORKFLOW_ID), any(WorkflowContext.class));

        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(WORKFLOW_ID), eq(expectedUnackTimeout * 1000));
    }

    private void verifyQueueRemove(String workflowId) {
        sweeper.sweeper(WORKFLOW_ID);

        verify(queueDAO).remove(eq(DECIDER_QUEUE), eq(workflowId));
    }

    private void verifyUnack(String workflowId, int expectedNumberOfTasks) {
        sweeper.sweeper(WORKFLOW_ID);

        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong());
        Mockito.verify(queueDAO, atLeastOnce()).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong());

        verify(monitors, times(1)).recordUnackTime(anyString(), anyLong());

        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.of(workflowModel));

        ArgumentCaptor<TaskModel> taskModelCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(queueDAO, times(expectedNumberOfTasks)).setUnackTimeout(
                eq(DECIDER_QUEUE), eq(WORKFLOW_ID), eq(taskModelCaptor.capture(taskModel).get().getTaskId()), anyLong());

        List<TaskModel> tasks = workflowModel.getTasks();
        for (TaskModel taskModel : tasks) {
            String taskId = taskModel.getTaskId();
            Long taskUnackTimeout = taskModelCaptor.getValue().getUnackTimeout();
            assertEquals(workflowId, taskModel.getWorkflowId());
            assertNotNull(taskUnackTimeout);
            assertEquals(Long.MAX_VALUE, taskUnackTimeout);
        }
    }

    private void verifyNoUnack(String workflowId) {
        sweeper.sweeper(WORKFLOW_ID);

        verify(queueDAO, never()).setUnackTimeout(eq(DECIDER_QUEUE), eq(WORKFLOW_ID), anyLong());
    }

    private static void verify(WorkflowExecutor workflowExecutor, WorkflowModel workflowModel) {
        WorkflowContext workflowContext = ConductorTestHelper.buildWorkflowContext();
        workflowExecutor.decide(WORKFLOW_ID, workflowContext);

        verify(workflowContext).setDeciderResponse(eq(workflowModel));
    }

    private static void verify(QueueDAO queueDAO, WorkflowModel workflowModel) {
        Optional<WorkflowModel> workflowOptional = queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID);
        WorkflowModel expectedWorkflowModel = workflowModel;

        if (workflowOptional.isPresent()) {
            expectedWorkflowModel = workflowOptional.get();
        }

        ArgumentCaptor<WorkflowModel> workflowModelCaptor = ArgumentCaptor.forClass(WorkflowModel.class);
        verify(queueDAO, atLeastOnce()).get(eq(DECIDER_QUEUE), eq(WORKFLOW_ID));
        verify(queueDAO, atLeastOnce()).set(eq(DECIDER_QUEUE), eq(expectedWorkflowModel));
    }

    private static void verify(QueueDAO queueDAO, long workflowId, long timeout) {
        verify(queueDAO, atLeastOnce()).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), eq(timeout * 1000));
    }

    private static void verify(QueueDAO queueDAO, long workflowId, TaskModel taskModel) {
        verify(queueDAO, atLeastOnce())
                .setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), eq(taskModel.getTaskId()), anyLong());
    }

    private static void verify(Monitors monitors, long workflowName, long unackTime) {
        Monitors monitorsMock = mock(Monitors.class);
        Monitors monitorsCaptor = ArgumentCaptor.forClass(Monitors.class);
        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, null, properties, queueDAO);
        sweeper.monitors = monitorsMock;

        sweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(monitorsCaptor.capture(monitors), atLeastOnce())
                .recordUnackTime(eq(workflowName), eq(unackTime));
    }

    @org.springframework.beans.factory.annotation.Autowired
    private SchedulerConfiguration schedulerConfiguration;

    private static final String DECIDER_QUEUE = "decider-queue";
    private static final String CLASS_NAME = WorkflowSweeper.class.getSimpleName();

    @org.springframework.beans.factory.annotation.Autowired
    private WorkflowSweeper sweeper;

    @org.springframework.beans.factory.annotation.Autowired
    private QueueDAO queueDAO;

    @org.springframework.beans.factory.annotation.Autowired
    private WorkflowExecutor workflowExecutor;

    @org.springframework.beans.factory.annotation.Autowired
    private Monitors monitors;

    private long workflowOffsetTimeout;

    @Before
    public void setUp() {
        workflowOffsetTimeout = schedulerConfiguration.getWorkflowOffsetTimeout().getSeconds();
    }

    @After
    public void tearDown() {
        ConductorTestHelper.clearContext();
    }
}