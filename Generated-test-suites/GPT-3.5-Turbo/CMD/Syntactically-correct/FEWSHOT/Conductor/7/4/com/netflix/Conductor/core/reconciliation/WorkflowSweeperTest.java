//Import Statements
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

//Imports from the source file
import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentMatchers;

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

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Test
    public void testWorkflowOffsetWithJitter() {
        // Test workflowOffsetWithJitter with example values
        Mockito.when(properties.getWorkflowOffsetTimeout()).thenReturn(45L);
        long result = workflowSweeper.workflowOffsetWithJitter(45);
        assertEquals(30, result); // Minimum value
        assertEquals(60, result); // Maximum value
    }

// Add more test cases for other methods in WorkflowSweeper class as needed

    @Test
    public void testSweepAsync() {
        // Implement test for sweepAsync method
    }

    @Test
    public void testSweep() {
        // Implement test for sweep method
    }

    @Test
    public void testUnack() {
        // Implement test for unack method
    }
}