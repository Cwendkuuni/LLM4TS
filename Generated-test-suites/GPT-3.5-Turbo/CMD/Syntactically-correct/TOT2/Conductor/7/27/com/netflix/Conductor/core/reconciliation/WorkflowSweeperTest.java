import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        workflowSweeper = new WorkflowSweeper();
    }

    @Test
    public void testSweepAsync() {
        // Test for basic functionality of sweepAsync method
        // Implement test case here
    }

    @Test
    public void testSweep() {
        // Test for basic functionality of sweep method
        // Implement test case here
    }

    @Test
    public void testUnack() {
        // Test for basic functionality of unack method
        // Implement test case here
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        // Test for basic functionality of workflowOffsetWithJitter method
        // Implement test case here
    }

    @Test
    public void testWorkflowOffsetWithJitter_edgeCases() {
        // Test for edge cases in workflowOffsetWithJitter method
        // Implement test case here
    }

    @Test
    public void testWorkflowOffsetWithJitter_exceptions() {
        // Test for exception handling in workflowOffsetWithJitter method
        // Implement test case here
    }

    // Add more test cases as needed for other methods in WorkflowSweeper class

}