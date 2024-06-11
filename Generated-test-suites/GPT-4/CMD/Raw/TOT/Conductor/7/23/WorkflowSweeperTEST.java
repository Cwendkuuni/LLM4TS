import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class WorkflowSweeeperTest {
    @InjectMocks
    private WorkfloSweeper workflowSweeper;

    @Mock
    QueueDAO queueDAO;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync(){
        String workflowId = "someWorkflowId";

        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);

        // check if CompletableFuture is not null
        assertNotNull(result);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // result is supposed to be in interval [30, 60]
        assertTrue(result >= 30 && result <= 60);
    }
}