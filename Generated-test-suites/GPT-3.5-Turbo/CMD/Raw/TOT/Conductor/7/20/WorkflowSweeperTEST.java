import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private QueueDAO queueDAO;

    @Before
    public void setup() {
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        queueDAO = mock(QueueDAO.class);

        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), new ConductorProperties(), queueDAO);
    }

    @Test
    public void testSweepAsync() {
        // typical case
        String workflowId = "workflow123";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);

        // verify that sweep method is called
        verify(workflowSweeper, times(1)).sweep(workflowId);
        assertTrue(future.isDone());
    }

    @Test
    public void testUnack() {
        // typical case
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflow123");
        long workflowOffsetTimeout = 60;
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        // verify that setUnackTimeout is called with proper arguments
        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq("workflow123"), eq(60000L));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        // typical case
        long workflowOffsetTimeout = 60;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // verify that the result is within expected range
        assertTrue(result >= 45 && result <= 75);
    }

    @Test
    public void testSweep() {
        // edge case: workflow not found
        String workflowId = "invalidWorkflow";
        WorkflowModel workflowModel = new WorkflowModel();
        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException());
        workflowSweeper.sweep(workflowId);

        // verify that remove method is called as workflow is not found
        verify(queueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq(workflowId));

        // typical case: workflow found
        workflowModel.setStatus(Status.RUNNING);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        workflowSweeper.sweep(workflowId);

        // verify that unack, Monitors.recordUnackTime and other methods are called
        verify(queueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq(workflowId));
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), eq(workflowId), anyLong());
        verify(workflowSweeper, times(1)).unack(eq(workflowModel), anyLong());
        // add more assertions and verifications for other scenarios
    }

    @Test
    public void testConstructor() {
        // typical case
        assertNotNull("Properties field should not be null", workflowSweeper.properties);
        assertNotNull("QueueDAO field should not be null", workflowSweeper.queueDAO);
        assertNotNull("workflowExecutor field should not be null", workflowSweeper.workflowExecutor);
        assertNotNull("workflowRepairService field should not be null", workflowSweeper.workflowRepairService);
    }

    // add more test cases for other methods in the WorkflowSweeper class

}