
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class WorkflowSweeperTest {
    private WorkflowSweeper workflowSweeper;
    
    @Test
    public void testSweepAsync() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper();
        CompletableFuture<Void> result = workflowSweeper.sweepAsync("workflowId");
        assertNotNull(result);
    }
    
    @Test
    public void testSweep() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper();
        workflowSweeper.sweep("workflowId");
        // Test for workflow not found scenario handled by NotFoundException
    }
    
    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper();
        long result = workflowSweeper.workflowOffsetWithJitter(45);
        // Test the range of jitter from [30-60] seconds
        assertTrue(result >= 30 && result <= 60);
    }
    
    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getTasks()).thenReturn(new ArrayList<>());
        
        WorkflowSweeper workflowSweeper = new WorkflowSweeper();
        workflowSweeper.unack(workflowModel, 60);
        // Test the unack method
    }
}
