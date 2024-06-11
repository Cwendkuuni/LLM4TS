package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {
    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private ConductorProperties properties;
    private QueueDAO queueDAO;

    @Before
    public void setup() {
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        properties = mock(ConductorProperties.class);
        queueDAO = mock(QueueDAO.class);
        
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService),
                        properties, queueDAO);
    }

    @Test
    public void testWorkflowSweepAsync() {
        String workflowId = "w1";
        workflowSweeper.sweepAsync(workflowId);
        
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testWorkflowSweep() {
        String workflowId = "w1";
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);
        verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(workflowId);
    }

    @Test(expected = NotFoundException.class)
    public void testSweepOnNotFoundException() {
        String workflowId = "w1";
        when(workflowExecutor.decide(workflowId)).thenThrow(NotFoundException.class);

        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    // Add more test methods for handling each specific method, edge cases and error scenarios
}
