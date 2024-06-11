
package com.netflix.conductor.test.core.reconciliation;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private Optional<WorkflowRepairService> workflowRepairService;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private QueueDAO queueDAO;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Test
    public void testSweepAsync() {
        doNothing().when(workflowExecutor).decide(anyString());
        workflowSweeper.sweepAsync("testWorkflowId");
        verify(workflowExecutor, times(1)).decide("testWorkflowId");
    }

    @Test
    public void testSweep() {
        when(workflowExecutor.decide(anyString())).thenReturn(new WorkflowModel());
        workflowSweeper.sweep("testWorkflowId");
        verify(workflowExecutor, times(1)).decide("testWorkflowId");
    }

    @Test
    public void testUnack() throws Exception {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskModel taskModel = mock(TaskModel.class);
        workflowModel.setTasks(Arrays.asList(taskModel));
        workflowSweeper.unack(workflowModel, 123L);
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        long result = workflowSweeper.workflowOffsetWithJitter(123L);
        Assert.assertTrue(82L <= result && result <= 164L);
    }
}
