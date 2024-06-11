
package com.netflix.conductor.core.reconciliation;

import static org.mockito.Mockito.*;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.config.ConductorProperties;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    WorkflowRepairService workflowRepairService;

    @Mock
    QueueDAO queueDAO;

    @Mock
    ConductorProperties properties;

    @Mock
    WorkflowExecutor workflowExecutor;

    @InjectMocks
    WorkflowSweeper workflowSweeper;

    private final String workflowId = "testWorkflowId";

    @Before
    public void setUp() {
        when(properties.getAppId()).thenReturn("testAppId");
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        future.get();
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweep() {
        when(queueDAO.contains(any(), any())).thenReturn(true);
        workflowSweeper.sweep(workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test(expected = NotFoundException.class)
    public void testSweepNotFound() {
        doThrow(new NotFoundException("Workflow NOT found")).when(workflowExecutor).decide(workflowId);
        workflowSweeper.sweep(workflowId);
    }

    // The test cases for methods "unack" and "workflowOffsetWithJitter" were omitted because they are not public.
}
