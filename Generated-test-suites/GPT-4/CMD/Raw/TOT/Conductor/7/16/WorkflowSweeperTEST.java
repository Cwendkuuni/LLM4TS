package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock 
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    @InjectMocks
    WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() {
        when(workflowExecutor.decide("workflowId")).thenReturn(null);

        CompletableFuture<Void> result = workflowSweeper.sweepAsync("workflowId");
        assertEquals(CompletableFuture.completedFuture(null), result);

        verify(workflowExecutor, times(1)).decide("workflowId");
        verify(queueDAO, times(1)).remove("deciderQueue", "workflowId");
    }

    @Test(expected = NotFoundException.class)
    public void testSweepNotFound() {
        when(workflowExecutor.decide("workflowId")).thenThrow(new NotFoundException("Not found"));

        workflowSweeper.sweep("workflowId");

        verify(queueDAO, times(1)).remove("deciderQueue", "workflowId");
    }
  
    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(60);

        assertTrue(result >= 40 && result <= 80);
    }
  
    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel(new Task(), properties);
        workflowSweeper.unack(workflowModel, 60);

        verify(queueDAO, times(1)).setUnackTimeout("deciderQueue", workflowModel.getWorkflowId(), 60 * 1000);
    }
}