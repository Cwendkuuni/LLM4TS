package com.netflix.conductor.core.reconciliation;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutorMock;
    private WorkflowRepairService workflowRepairServiceMock;
    private ConductorProperties conductorPropertiesMock;
    private QueueDAO queueDAOMock;
    private static final String workflowId = "testWorkflowId";

    @Before
    public void setUp() {
        workflowExecutorMock = mock(WorkflowExecutor.class);
        workflowRepairServiceMock = mock(WorkflowRepairService.class);
        queueDAOMock = mock(QueueDAO.class);
        conductorPropertiesMock = mock(ConductorProperties.class);
        workflowSweeper = new WorkflowSweeper(workflowExecutorMock, Optional.of(workflowRepairServiceMock),
                conductorPropertiesMock, queueDAOMock);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);

        verify(workflowExecutorMock).decide(workflowId);
        assert(result.isDone());
    }

    @Test
    public void testSweep() {
        WorkflowModel workflowMock = mock(WorkflowModel.class);
        when(workflowExecutorMock.decide(workflowId)).thenReturn(workflowMock);

        workflowSweeper.sweep(workflowId);

        verify(workflowExecutorMock).decide(workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowMock = mock(WorkflowModel.class);
        long timeout = 30000;

        when(workflowMock.getTimeoutSeconds()).thenReturn(timeout);
        workflowSweeper.unack(workflowMock, timeout);

        verify(queueDAOMock).setUnackTimeout(any(String.class), eq(workflowMock.getWorkflowId()), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long timeout = 30000;
        long result = workflowSweeper.workflowOffsetWithJitter(timeout);
        assert(result >= timeout - timeout/3 && result <= timeout + timeout/3);
    }
}
