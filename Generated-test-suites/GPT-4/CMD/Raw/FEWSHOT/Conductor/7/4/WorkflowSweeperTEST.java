
package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;

public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Test
    public void testSweepAsync() throws Exception {
        String workflowId = "workflow1";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);

        verify(workflowSweeper).sweepAsync(workflowId);
        assertEquals(CompletableFuture.completedFuture(null), future);
    }

    @Test
    public void testSweep() throws Exception {
        String workflowId = "workflow1";
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getStatus()).thenReturn(Status.COMPLETED);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        verify(workflowExecutor).decide(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 100;
        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertEquals(workflowOffsetTimeout + jitter, workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskModel taskModel = mock(TaskModel.class);
        long workflowOffsetTimeout = 100;

        when(taskModel.getStatus()).thenReturn(Status.SCHEDULED);
        when(taskModel.getResponseTimeoutSeconds()).thenReturn(workflowOffsetTimeout);

        ReflectionTestUtils.invokeMethod(workflowSweeper, "unack", workflowModel, workflowOffsetTimeout);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), (workflowOffsetTimeout + 1) * 1000);
    }
}
