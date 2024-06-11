
package com.netflix.conductor.core.reconciliation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.annotation.Async;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private QueueDAO queueDAO;
    private ConductorProperties properties;

    @BeforeEach
    public void setup() {
        workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        workflowRepairService = Mockito.mock(WorkflowRepairService.class);
        queueDAO = Mockito.mock(QueueDAO.class);
        properties = Mockito.mock(ConductorProperties.class);

        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void sweepAsync() {
        String workflowId = "workflowId";
        workflowSweeper.sweepAsync(workflowId);
        Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void sweep() {
        String workflowId = "workflowId";
        workflowSweeper.sweep(workflowId);
        Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void unack() {
        long workflowOffsetTimeout = 500;
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        Mockito.verify(queueDAO, Mockito.times(1)).setUnackTimeout(Mockito.anyString(), Mockito.anyString(), anyLong());
    }

    @Test
    public void workflowOffsetWithJitter() {
        long workflowOffsetTimeout = 500;
        long returnJitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(returnJitter <= 2 * workflowOffsetTimeout);
        assertTrue(returnJitter >= workflowOffsetTimeout / 2);
    }
}
