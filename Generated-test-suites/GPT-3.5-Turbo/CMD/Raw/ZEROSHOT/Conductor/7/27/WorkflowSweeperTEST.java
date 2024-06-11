import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.CompletableFuture;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;

    private WorkflowExecutor workflowExecutorMock;
    private WorkflowRepairService workflowRepairServiceMock;
    private ConductorProperties propertiesMock;
    private QueueDAO queueDAOMock;

    @Before
    public void setup() {
        workflowExecutorMock = mock(WorkflowExecutor.class);
        workflowRepairServiceMock = mock(WorkflowRepairService.class);
        propertiesMock = mock(ConductorProperties.class);
        queueDAOMock = mock(QueueDAO.class);

        workflowSweeper = new WorkflowSweeper(workflowExecutorMock, Optional.of(workflowRepairServiceMock), propertiesMock, queueDAOMock);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        assertNotNull(future);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweep(workflowId);
        // Add relevant assertions here
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowSweeper.unack(workflowModel, 60);
        // Add relevant assertions here
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(45);
        // Add relevant assertions here
    }
}