
package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private Optional<WorkflowRepairService> workflowRepairServiceOptional;
    @Mock
    private ConductorProperties properties;
    @Mock
    private QueueDAO queueDAO;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Test
    public void testSweepAsync() {
        final String workflowId = "testWorkflowId";
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        assertNotNull(result);
    }

    @Test
    public void testSweep() throws NotFoundException {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
        workflowRepairServiceOptional = Optional.of(workflowRepairService);

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowContext.set(workflowContext);

        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);
      
        workflowSweeper.sweep("testWorkflowId");
        
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getStatus()).thenReturn(Status.RUNNING);
        workflowSweeper.unack(workflowModel, 30L);
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitter = workflowSweeper.workflowOffsetWithJitter(45L);
        assertNotNull(jitter);
        long minRange = 30L;
        long maxRange = 60L;
        assertTrue(minRange <= jitter && jitter <= maxRange);
    }
}
