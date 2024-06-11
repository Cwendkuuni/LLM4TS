import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private QueueDAO queueDAO;
    private ConductorProperties properties;

    @Before
    public void setUp() {
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        queueDAO = mock(QueueDAO.class);
        properties = mock(ConductorProperties.class);
        
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), 
                                              properties, queueDAO);
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        String workflowId = "testWorkflowId";
        
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        future.get();
        
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        
        workflowSweeper.sweep(workflowId);
        
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");
        long workflowOffsetTimeout = 60;
        
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        
        // Ensure the result is within the expected range
        assertTrue(result >= 30 && result <= 60);
    }
}