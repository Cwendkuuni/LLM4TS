import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.concurrent.CompletableFuture;

@RunWith(MockitoJUnitRunner.class)
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
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "123";
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        assertEquals(CompletableFuture.completedFuture(null).join(), result.join());
    }

    @Test
    public void testSweep() {
        String workflowId = "456";
        when(workflowExecutor.decide(anyString())).thenReturn(null);
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertEquals(true, result >= 30 && result <= 60);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getWorkflowId()).thenReturn("789");
        when(workflowModel.getTasks()).thenReturn(new ArrayList<>());
        workflowSweeper.unack(workflowModel, 60);
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }
}