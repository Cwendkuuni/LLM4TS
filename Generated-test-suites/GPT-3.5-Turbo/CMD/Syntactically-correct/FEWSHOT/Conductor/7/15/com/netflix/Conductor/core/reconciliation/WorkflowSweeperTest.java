package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private ConductorProperties properties;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private QueueDAO queueDAO;

    @Before
    public void setup() {
        properties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        queueDAO = mock(QueueDAO.class);
        
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);

        Assert.notNull(future, "CompletableFuture should not be null");

        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";

        WorkflowModel workflowModel = mock(WorkflowModel.class);

        WorkflowContext workflowContext = mock(WorkflowContext.class);
        when(properties.getAppId()).thenReturn("testAppId");
        WorkflowContext.set(workflowContext);

        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowId)).thenReturn(true);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(Status.COMPLETED);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);

        // Additional verification points can be added here
    }


    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(mock(TaskModel.class)));
        when(workflowModel.getWorkflowId()).thenReturn("testWorkflowId");

        workflowSweeper.unack(workflowModel, 60);

        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq("testWorkflowId"), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        Assert.isTrue(result >= 30 && result <= 60, "Result should be between 30 and 60");
    }
}