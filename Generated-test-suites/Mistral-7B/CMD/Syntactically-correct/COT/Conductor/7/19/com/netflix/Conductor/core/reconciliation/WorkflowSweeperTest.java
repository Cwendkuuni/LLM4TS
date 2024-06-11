package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowOffsetCalculator;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.scheduler.SchedulerConfiguration;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowState;
import com.netflix.conductor.metrics.Monitors;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-1";
    private static final String WORKFLOW_NAME = "workflow-name";

    private WorkflowSweeper sweeper;
    private ConductorProperties properties;
    private WorkflowExecutor workflowExecutor;
    private QueueDAO queueDAO;
    private Monitors monitors;
    private WorkflowRepairService workflowRepairService;
    private WorkflowOffsetCalculator workflowOffsetCalculator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        new AuthenticatingTestExecutionListener().register(this.getClass().getSimpleName());
        properties = mock(ConductorProperties.class);
        when(properties.getAppId()).thenReturn("app-id");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);

        workflowExecutor = mock(WorkflowExecutor.class);
        queueDAO = mock(QueueDAO.class);
        monitors = mock(Monitors.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        workflowOffsetCalculator = mock(WorkflowOffsetCalculator.class);

        sweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.of(workflowRepairService),
                properties,
                queueDAO,
                monitors,
                workflowOffsetCalculator);
        sweeper.setProperties(properties);
    }

    @Test
    public void testSweepAsync() throws Exception {
        CompletableFuture<Void> result = sweeper.sweepAsync(WORKFLOW_ID);
        result.get(5, TimeUnit.SECONDS);
        verify(workflowExecutor, times(1)).decide(ArgumentMatchers.eq(WORKFLOW_ID));
    }

    @Test
    public void testSweepWithoutWorkflow() throws Exception {
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(null);
        sweeper.sweep(WORKFLOW_ID);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepWithNotFoundException() throws Exception {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException("Not Found"));
        sweeper.sweep(WORKFLOW_ID);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepWithError() throws Exception {
        WorkflowModel workflowModel = createWorkflowModel();
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        doThrow(new Exception("Error running sweep"))
                .when(sweeper)
                .sweep(WORKFLOW_ID);

        sweeper.sweep(WORKFLOW_ID);
        verify(monitors, times(1)).error(ArgumentMatchers.eq(WorkflowSweeper.class.getSimpleName()),
                ArgumentMatchers.eq("sweep"));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = createWorkflowModel();
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        sweeper.unack(workflowModel);

        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), ArgumentMatchers.anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 5L;
        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result > workflowOffsetTimeout - 1 && result < workflowOffsetTimeout + 2);
    }

    @Test
    public void testSweep_RepairWorkflowTasks() throws Exception {
        WorkflowModel workflowModel = createWorkflowModel();
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
        when(workflowRepairService.verifyAndRepairWorkflowTasks(WORKFLOW_ID)).thenReturn(true);

        sweeper.sweep(WORKFLOW_ID);

        verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
    }

    @Test
    public void testSweep_Unack() throws Exception {
        WorkflowModel workflowModel = createWorkflowModel();
        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        sweeper.sweep(WORKFLOW_ID);

        WorkflowModel actualWorkflowModel = verifyWorkflowInQueue(workflowModel.getWorkflowId(), DECIDER_QUEUE);
        assertEquals(WorkflowState.UNACKNOWLEDGED, actualWorkflowModel.getState());
        assertEquals(Status.IN_PROGRESS, actualWorkflowModel.getTasks().get(0).getStatus());
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setState(WorkflowState.ENQUEUED);
        workflowModel.setStatus(Status.READY);

        List<TaskModel> tasks = Arrays.asList(
                new TaskModel(),
                new TaskModel()
        );
        workflowModel.setTasks(tasks);

        return workflowModel;
    }

    private WorkflowModel verifyWorkflowInQueue(String workflowId, String queueName) {
        return queueDAO.get(queueName, workflowId);
    }
}