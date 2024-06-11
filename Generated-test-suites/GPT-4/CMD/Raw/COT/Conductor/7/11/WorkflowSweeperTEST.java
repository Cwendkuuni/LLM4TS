import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.WorkflowExecutor;
import com.netflix.conductor.core.config.ConductorProperties;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Test
    public void testSweepAsync() {
        String workflowId = "test1";
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        assert(result.isDone());
        verify(workflowSweeper, times(1)).sweepAsync(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "test1";
        workflowSweeper.sweep(workflowId);
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModelMock = mock(WorkflowModel.class);
        when(workflowModelMock.getTasks()).thenReturn(new ArrayList<>());
        workflowSweeper.unack(workflowModelMock, 300);
        verify(workflowSweeper, times(1)).unack(workflowModelMock, 300);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitter = workflowSweeper.workflowOffsetWithJitter(45);
        assertTrue(jitter >= 30 && jitter <= 60);
    }
}