import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweepAsync(workflowId);
        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweep(workflowId);
        // Add appropriate assertions here
    }

    @Test
    public void testUnack() {
        WorkflowModel mockWorkflowModel = new WorkflowModel();
        long workflowOffsetTimeout = 60; // Example value
        workflowSweeper.unack(mockWorkflowModel, workflowOffsetTimeout);
        // Add appropriate assertions here
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45; // Example value
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        // Add appropriate assertions here
    }
}