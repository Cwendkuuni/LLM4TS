
import org.junit.Test;
import org.junit.Assert;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class WorkflowSweeperTest {
    @Test
    public void testSweepAsync() {
        WorkflowExecutor mockExecutor = mock(WorkflowExecutor.class);
        ConductorProperties mockProperties = mock(ConductorProperties.class);
        QueueDAO mockQueueDAO = mock(QueueDAO.class);
        Optional<WorkflowRepairService> mockWorkflowRepairService = Optional.of(mock(WorkflowRepairService.class));
        WorkflowSweeper sweeper = spy(new WorkflowSweeper(mockExecutor, mockWorkflowRepairService, mockProperties, mockQueueDAO));
        
        String workflowId = "testWorkflow";
        when(mockExecutor.decide(workflowId)).thenReturn(null);

        sweeper.sweepAsync(workflowId);

        verify(mockExecutor).decide(workflowId);
        verify(mockQueueDAO).setUnackTimeout(any(), any(), any());
    }

    @Test(expected = NullPointerException.class)
    public void testSweepWithNullWorkflowId() {
        WorkflowExecutor mockExecutor = mock(WorkflowExecutor.class);
        ConductorProperties mockProperties = mock(ConductorProperties.class);
        QueueDAO mockQueueDAO = mock(QueueDAO.class);
        Optional<WorkflowRepairService> mockWorkflowRepairService = Optional.of(mock(WorkflowRepairService.class));

        WorkflowSweeper sweeper = new WorkflowSweeper(mockExecutor, mockWorkflowRepairService, mockProperties, mockQueueDAO);
        sweeper.sweep(null);
    }

    @Test
    public void testUnack() {
        WorkflowExecutor mockExecutor = mock(WorkflowExecutor.class);
        ConductorProperties mockProperties = mock(ConductorProperties.class);
        QueueDAO mockQueueDAO = mock(QueueDAO.class);
        Optional<WorkflowRepairService> mockWorkflowRepairService = Optional.of(mock(WorkflowRepairService.class));
        WorkflowSweeper sweeper = new WorkflowSweeper(mockExecutor, mockWorkflowRepairService, mockProperties, mockQueueDAO);
        
        WorkflowModel mockWorkflow = mock(WorkflowModel.class);
        when(mockWorkflow.getWorkflowId()).thenReturn("mockId");

        sweeper.unack(mockWorkflow, 20);

        verify(mockQueueDAO).setUnackTimeout(any(), any(), any());
    }

    @Test(expected = NullPointerException.class)
    public void testUnackWithNullWorkflow() {
        WorkflowExecutor mockExecutor = mock(WorkflowExecutor.class);
        ConductorProperties mockProperties = mock(ConductorProperties.class);
        QueueDAO mockQueueDAO = mock(QueueDAO.class);
        Optional<WorkflowRepairService> mockWorkflowRepairService = Optional.of(mock(WorkflowRepairService.class));
        WorkflowSweeper sweeper = new WorkflowSweeper(mockExecutor, mockWorkflowRepairService, mockProperties, mockQueueDAO);

        sweeper.unack(null, 20);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowExecutor mockExecutor = mock(WorkflowExecutor.class);
        ConductorProperties mockProperties = mock(ConductorProperties.class);
        QueueDAO mockQueueDAO = mock(QueueDAO.class);
        Optional<WorkflowRepairService> mockWorkflowRepairService = Optional.of(mock(WorkflowRepairService.class));
        WorkflowSweeper sweeper = new WorkflowSweeper(mockExecutor, mockWorkflowRepairService, mockProperties, mockQueueDAO);
        long jitteredTime = sweeper.workflowOffsetWithJitter(90);
        Assert.assertTrue(jitteredTime >= 60 && jitteredTime <= 120);
    }

    @Test(expected = ArithmeticException.class)
    public void testWorkflowOffsetWithJitterZero() {
        WorkflowExecutor mockExecutor = mock(WorkflowExecutor.class);
        ConductorProperties mockProperties = mock(ConductorProperties.class);
        QueueDAO mockQueueDAO = mock(QueueDAO.class);
        Optional<WorkflowRepairService> mockWorkflowRepairService = Optional.of(mock(WorkflowRepairService.class));
        WorkflowSweeper sweeper = new WorkflowSweeper(mockExecutor, mockWorkflowRepairService, mockProperties, mockQueueDAO);
        sweeper.workflowOffsetWithJitter(0);
    }
}
