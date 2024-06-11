
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor executor;

    @Mock
    private WorkflowRepairService repairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    private WorkflowSweeper sweeper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        sweeper = new WorkflowSweeper(executor, Optional.of(repairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() throws Exception {
        when(executor.decide(anyString())).thenReturn(null);
        CompletableFuture<Void> resultFuture = sweeper.sweepAsync("workflowId");
        Mockito.verify(executor, times(1)).decide(anyString());
    }

    @Test
    public void testSweepNotFound() {
        when(executor.decide(anyString())).thenThrow(new NotFoundException("Error"));
        sweeper.sweep("workflowId");
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testSweepException() {
        when(executor.decide(anyString())).thenThrow(new RuntimeException());
        sweeper.sweep("workflowId");
        verify(queueDAO, times(0)).remove(anyString(), anyString());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = sweeper.workflowOffsetWithJitter(60);
        assert(result >= 40 && result <= 80);
    }
}
