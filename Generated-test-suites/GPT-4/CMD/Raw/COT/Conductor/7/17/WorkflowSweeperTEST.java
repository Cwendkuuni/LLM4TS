
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

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

    @After
    public void tearDown() {
        verifyNoMoreInteractions(workflowExecutor, workflowRepairService, properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync("testWorkflowId");
        assertTrue(result.isDone());
    }

    @Test(expected = NotFoundException.class)
    public void testSweepWithWorkflowIdNotFound() {
        doThrow(NotFoundException.class).when(workflowExecutor).decide(anyString());
        workflowSweeper.sweep("testWorkflowId");
    }

    @Test
    public void testUnackWithNullWorkflow() {
        workflowSweeper.unack(null, 30);
        verify(queueDAO).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitterValue = workflowSweeper.workflowOffsetWithJitter(0L);
        assertEquals(0L, jitterValue);
        jitterValue = workflowSweeper.workflowOffsetWithJitter(Long.MAX_VALUE);
        assertTrue(jitterValue >= 0);
    }

    /** More test methods for sweep and unack depends on TaskModel and WorkflowModel data **/
}
