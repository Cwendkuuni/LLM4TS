
package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "123";
        CompletableFuture<Void> sweepResult = workflowSweeper.sweepAsync(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        assertEquals(CompletableFuture.completedFuture(null), sweepResult);
    }

    @Test
    public void testSweep() {
        String workflowId = "123";
        when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(true);
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        workflowSweeper.sweep(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long jitteredOffset = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        boolean withinRange = jitteredOffset >= (2/3)*workflowOffsetTimeout && jitteredOffset <= (4/3)*workflowOffsetTimeout;
        assertEquals(true, withinRange);
    }

    // Similar tests to be created for other methods
}
