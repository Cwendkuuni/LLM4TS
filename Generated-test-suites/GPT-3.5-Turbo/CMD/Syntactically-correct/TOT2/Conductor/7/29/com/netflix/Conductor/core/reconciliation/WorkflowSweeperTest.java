import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutorMock;
    private WorkflowRepairService workflowRepairService;
    private QueueDAO queueDAOMock;

    @Before
    public void setUp() {
        workflowExecutorMock = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        queueDAOMock = mock(QueueDAO.class);
        workflowSweeper = new WorkflowSweeper(workflowExecutorMock, Optional.of(workflowRepairService), mock(ConductorProperties.class), queueDAOMock);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "12345";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        future.join();
        verify(workflowExecutorMock).decide(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "12345";
        workflowSweeper.sweep(workflowId);
        verify(workflowExecutorMock).decide(workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModelMock = mock(WorkflowModel.class);
        when(workflowModelMock.getTasks()).thenReturn(new ArrayList<>());
        workflowSweeper.unack(workflowModelMock, 60L);
        // Add assertions here
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long offsetTimeout = 45L;
        long result = workflowSweeper.workflowOffsetWithJitter(offsetTimeout);
        // Add assertions here
    }
}