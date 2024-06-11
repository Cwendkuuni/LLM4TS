package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.model.TaskModel.Status;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.Mock;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        WorkflowModel mockWorkflow = Mockito.mock(WorkflowModel.class);
        Mockito.when(workflowExecutor.decide(workflowId)).thenReturn(mockWorkflow);
        Mockito.when(mockWorkflow.getStatus().isTerminal()).thenReturn(true);
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).remove(anyString(), workflowId);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        Mockito.doNothing().when(workflowSweeper).sweep(workflowId);
        workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel mockWorkflow = Mockito.mock(WorkflowModel.class);
        TaskModel mockTask = Mockito.mock(TaskModel.class);
        Mockito.when(mockWorkflow.getTasks()).thenReturn(Arrays.asList(mockTask));
        Mockito.when(mockTask.getStatus()).thenReturn(Status.IN_PROGRESS);
        Mockito.when(mockTask.getResponseTimeoutSeconds()).thenReturn(1);
        workflowSweeper.unack(mockWorkflow, 1);
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result >= 30 && result <= 60);
    }
}