package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.testutils.TestUtils;

@SpringJUnitConfig(classes = {WorkflowSweeper.class})
@TestPropertySource(properties = {"spring.main.allow-bean-definition-overriding=true"})
public class WorkflowSweeperTest {

    private final Random random = new Random();
    private WorkflowSweeper workflowSweeper;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private QueueDAO queueDAO;

    @Before
    public void setup() {
        conductorProperties = Mockito.mock(ConductorProperties.class);
        workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        queueDAO = Mockito.mock(QueueDAO.class);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, null, conductorProperties, queueDAO);
        when(conductorProperties.getAppId()).thenReturn("testAppId");
    }

    @Test
    public void testSweepAsyncMethod() throws ExecutionException, InterruptedException {
        String workflowId = "workflowId";
        when(workflowExecutor.decide(workflowId)).thenReturn(TestUtils.createWorkflowModel());

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        future.get();

        verify(workflowExecutor).decide(workflowId);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong());
    }

    @Test
    public void testSweepMethod_WorkflowNotFound() {
        String workflowId = "workflowId";

        when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenThrow(new NotFoundException("Workflow not found"));

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(eq(DECIDER_QUEUE), eq(workflowId));
        verify(logger).info("Workflow NOT found for id:" + workflowId + " Removed it from decider queue");
    }

    @Test
    public void testSweepMethod_WorkflowFound_TerminalStatus() {
        String workflowId = "workflowId";
        WorkflowModel workflow = TestUtils.createWorkflowModel();
        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(eq(DECIDER_QUEUE), workflowId);
    }

    @Test
    public void testSweepMethod_WorkflowFound_VerifyAndRepairTasks() {
        String workflowId = "workflowId";
        WorkflowModel workflow = TestUtils.createWorkflowModel();
        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);
        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowId)).thenCallRealMethod();

        workflowSweeper.sweep(workflowId);

        verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
    }

    @Test
    public void testUnackMethod_InProgressTask() {
        WorkflowModel workflowModel = TestUtils.createWorkflowModel();
        TaskModel inProgressTask = TestUtils.createTaskModel(Status.IN_PROGRESS);
        workflowModel.setTasks(Arrays.asList(inProgressTask));

        long workflowOffsetTimeout = 45L;
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        long postponeDurationSeconds = inProgressTask.getWaitTimeout() + 1;
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowModel.getWorkflowId()),
                eq(postponeDurationSeconds * 1000));
    }

    @Test
    public void testUnackMethod_ScheduledTask() {
        WorkflowModel workflowModel = TestUtils.createWorkflowModel();
        TaskModel scheduledTask = TestUtils.createTaskModel(Status.SCHEDULED);
        workflowModel.setTasks(Collections.singletonList(scheduledTask));

        long workflowOffsetTimeout = 45L;
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        Long pollTimeoutSeconds = scheduledTask.getTaskDefinition().getPollTimeoutSeconds();
        long postponeDurationSeconds = (pollTimeoutSeconds != null) ? pollTimeoutSeconds + 1 : workflowOffsetTimeout;
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowModel.getWorkflowId()),
                eq(postponeDurationSeconds * 1000));
    }

    @Test
    public void testUnackMethod_TaskDefinitionNotFound() {
        WorkflowModel workflowModel = TestUtils.createWorkflowModel();
        TaskModel scheduledTask = TestUtils.createTaskModel(Status.SCHEDULED);
        workflowModel.setTasks(Collections.singletonList(scheduledTask));

        long workflowOffsetTimeout = 45L;
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        long postponeDurationSeconds =
                workflowModel.getWorkflowDefinition().getTimeoutSeconds() + 1;
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowModel.getWorkflowId()),
                eq(postponeDurationSeconds * 1000));
    }

    @Test
    public void testWorkflowOffsetWithJitterMethod() {
        long workflowOffsetTimeout = 45L;

        long jitter = random.nextInt(2 * workflowOffsetTimeout / 3 + 1) - workflowOffsetTimeout / 3;

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertEquals(workflowOffsetTimeout + jitter, result);
    }
}