package com.netflix.conductor.core.reconciliation;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.mock.MockitoTestExecutionListener;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.SchedulerConfiguration;
import com.netflix.conductor.core.WorkflowExecutor;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.testutils.SpringTest;
import com.netflix.conductor.testutils.TestUtils;

@TestPropertySource(locations = "classpath:test-application.properties")
@Component
@SpringTest
public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeper.class);

    @Autowired
    private WorkflowSweeper workflowSweeper;

    @Autowired
    private ConductorProperties properties;

    @Autowired
    private QueueDAO queueDAO;

    @Autowired
    private Monitors monitors;

    @Autowired
    private WorkflowExecutor workflowExecutor;

    @Autowired
    private Optional<WorkflowRepairService> workflowRepairService;

    private static final String WORKFLOW_ID = "testWorkflowId";
    private static final String WORKFLOW_NAME = "testWorkflowName";

    @Before
    public void setUp() {
        LOGGER.info("Test setup");
    }

    @After
    public void tearDown() {
        LOGGER.info("Test teardown");
    }

    @Test
    public void testSweepAsync() {
        when(properties.getWorkflowOffsetTimeout()).thenReturn(30L);
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(0L);

        workflowSweeper.sweepAsync(WORKFLOW_ID);

        ArgumentCaptor<Long> workflowOffsetTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, workflowOffsetTimeoutCaptor.capture(1L) * 1000);
    }

    @Test
    public void testSweep() throws Exception {
        when(properties.getWorkflowOffsetTimeout()).thenReturn(30L);
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(null);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(null);
        when(workflowRepairService.verifyAndRepairWorkflowTasks(WORKFLOW_ID)).thenReturn();

        workflowSweeper.sweep(WORKFLOW_ID);

        ArgumentCaptor<Long> workflowOffsetTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, workflowOffsetTimeoutCaptor.capture(1L) * 1000);
        verify(monitors).recordUnackTime(WORKFLOW_NAME, eq(workflowOffsetTimeoutCaptor.getValue()));
    }

    @Test(expected = NotFoundException.class)
    public void testNotFound() throws Exception {
        when(properties.getWorkflowOffsetTimeout()).thenReturn(30L);
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenThrow(new NotFoundException(""));

        workflowSweeper.sweep(WORKFLOW_ID);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDefinition());
        workflowModel.setWorkflowId(WORKFLOW_ID);

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(new TaskType("wait"));
        workflowModel.getTasks().add(taskModel);

        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.unack(workflowModel, 30L);

        ArgumentCaptor<Long> unackTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, unackTimeoutCaptor.capture(31L) * 1000);
    }

    @Test
    public void testUnackWithTaskTypeHuman() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDefinition());
        workflowModel.setWorkflowId(WORKFLOW_ID);

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(new TaskType("human"));
        workflowModel.getTasks().add(taskModel);

        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.unack(workflowModel, 30L);

        ArgumentCaptor<Long> unackTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, unackTimeoutCaptor.capture(31L) * 1000);
    }

    @Test
    public void testUnackWithTaskTypeTaskTypeWait() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDefinition());
        workflowModel.setWorkflowId(WORKFLOW_ID);

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(new TaskType("wait"));
        taskModel.setWaitTimeout(10L);
        workflowModel.getTasks().add(taskModel);

        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.unack(workflowModel, 30L);

        ArgumentCaptor<Long> unackTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, unackTimeoutCaptor.capture(41L) * 1000);
    }

    @Test
    public void testUnackWithTaskTypeTaskTypeNone() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDefinition());
        workflowModel.setWorkflowId(WORKFLOW_ID);

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        workflowModel.getTasks().add(taskModel);

        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.unack(workflowModel, 30L);

        ArgumentCaptor<Long> unackTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, unackTimeoutCaptor.capture(31L) * 1000);
    }

    @Test
    public void testUnackWithWorkflowDefinitionTimeout() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDefinition());
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.getWorkflowDefinition().setTimeoutSeconds(10L);

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        workflowModel.getTasks().add(taskModel);

        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.unack(workflowModel, 30L);

        ArgumentCaptor<Long> unackTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, unackTimeoutCaptor.capture(41L) * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        assertEquals(36, workflowSweeper.workflowOffsetWithJitter(30L));
    }

    @Test
    public void testWorkflowOffsetWithJitterBelowZero() {
        assertEquals(27, workflowSweeper.workflowOffsetWithJitter(-15L));
    }

    @Test
    public void testWorkflowOffsetWithJitterAboveZero() {
        assertEquals(45, workflowSweeper.workflowOffsetWithJitter(45L));
    }
}