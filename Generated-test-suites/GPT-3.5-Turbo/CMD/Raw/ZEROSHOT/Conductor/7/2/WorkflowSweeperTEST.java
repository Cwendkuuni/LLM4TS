import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;
import org.junit.Before;
import org.junit.Test;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private QueueDAO queueDAO;
    private ConductorProperties properties;

    @Before
    public void setup() {
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        queueDAO = mock(QueueDAO.class);
        properties = mock(ConductorProperties.class);
        
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync("testWorkflowId");
        assertNotNull(result);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitteredTimeout = workflowSweeper.workflowOffsetWithJitter(45);
        assertTrue(jitteredTimeout >= 30 && jitteredTimeout <= 60);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");

        workflowSweeper.unack(workflowModel, 60);

        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), eq(61 * 1000));
    }

    @Test
    public void testSweep() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");

        when(workflowExecutor.decide("testWorkflowId")).thenReturn(workflowModel);

        workflowSweeper.sweep("testWorkflowId");

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }
}