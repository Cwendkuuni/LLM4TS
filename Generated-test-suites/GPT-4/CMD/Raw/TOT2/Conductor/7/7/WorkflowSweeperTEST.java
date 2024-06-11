import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertTrue;

public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long startTime = System.currentTimeMillis();
        long result = workflowSweeper.workflowOffsetWithJitter(45);
        long endTime = System.currentTimeMillis();
        // test for the result to be in the range [30-60]
        assertTrue(result >= 30 && result <= 60);

        // test should complete within 10ms
        assertTrue("Test run exceeded the time limit.", (endTime-startTime) <= 10);
    }

}