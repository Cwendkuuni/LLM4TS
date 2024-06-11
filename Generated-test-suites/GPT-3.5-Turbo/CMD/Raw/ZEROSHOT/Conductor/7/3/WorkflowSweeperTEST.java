import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.concurrent.CompletableFuture;
import java.util.Optional;

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
    @Spy
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> completableFuture = workflowSweeper.sweepAsync(workflowId);
        assertNotNull(completableFuture);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        
        when(workflowExecutor.decide(workflowId)).thenReturn(null);
        
        workflowSweeper.sweep(workflowId);
        
        verify(queueDAO).remove(anyString(), eq(workflowId));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");
        
        workflowSweeper.unack(workflowModel, 60);
        
        verify(queueDAO).setUnackTimeout(anyString(), eq("testWorkflowId"), eq(60 * 1000));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitter = workflowSweeper.workflowOffsetWithJitter(45);
        assertTrue(jitter >= 30 && jitter <= 60);
    }
}