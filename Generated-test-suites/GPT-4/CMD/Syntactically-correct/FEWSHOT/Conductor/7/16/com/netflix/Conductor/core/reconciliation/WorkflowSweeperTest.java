package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.scheduling.annotation.Async;

public class WorkflowSweeperTest {
    private final WorkflowExecutor workflowExecutor = Mockito.mock(WorkflowExecutor.class);
    private final WorkflowRepairService workflowRepairService = Mockito.mock(WorkflowRepairService.class);
    private final ConductorProperties properties = Mockito.mock(ConductorProperties.class);
    private final QueueDAO queueDAO = Mockito.mock(QueueDAO.class);

    private WorkflowSweeper sweeper = new WorkflowSweeper(
            workflowExecutor,
            Optional.of(workflowRepairService),
            properties,
            queueDAO);

    @Test
    public void sweepAsyncTest() {
        String workflowId = "test";
        sweeper.sweepAsync(workflowId);
        verify(workflowExecutor).decide(workflowId);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void sweepTest() {
        String workflowId = "test";
        sweeper.sweep(workflowId);
        verify(workflowExecutor).decide(workflowId);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void unackTest() {
        WorkflowModel workflowModel = new WorkflowModel();
        sweeper.unack(workflowModel, 0L);
        assertEquals(workflowModel.getWorkflowId(), sweeper.sweepAsync(workflowModel.getWorkflowId()));
    }

    @Test
    public void workflowOffsetWithJitterTest() {
        long offset = 0L;
        assertEquals(offset, sweeper.workflowOffsetWithJitter(offset));
    }
}