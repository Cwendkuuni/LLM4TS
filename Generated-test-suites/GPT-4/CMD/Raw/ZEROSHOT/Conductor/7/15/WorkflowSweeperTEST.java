import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mockito;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.model.WorkflowModel;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest extends TestCase {

    @Mock
    WorkflowExecutor workflowExecutor;

    @Mock
    Optional<WorkflowRepairService> workflowRepairService;

    @Mock
    ConductorProperties properties;

    @Mock
    QueueDAO queueDAO;

    @InjectMocks
    WorkflowSweeper workflowSweeper;

    @Test
    public void testSweepAsync() {
        String workflowId = "test-id";
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        assertNotNull(result);
    }

    @Test
    public void testSweep() {
		WorkflowModel workflow = new WorkflowModel();
		workflow.setWorkflowId("test-id");
		Mockito.when(workflowRepairService.orElse(null).verifyAndRepairWorkflowTasks(Mockito.anyString())).thenReturn(null);
        Mockito.when(workflowExecutor.decide(Mockito.anyString())).thenReturn(workflow);
        workflowSweeper.sweep(workflow.getWorkflowId());
        Mockito.verify(workflowRepairService.orElse(null), Mockito.times(1)).verifyAndRepairWorkflowTasks(Mockito.anyString());
        Mockito.verify(workflowExecutor, Mockito.times(1)).decide(Mockito.anyString());
    }

    @Test
    public void testUnack() {
		WorkflowModel workflow = new WorkflowModel();
		long timeout = 3000L;
		workflowSweeper.unack(workflow, timeout);
		Mockito.verify(queueDAO, Mockito.times(1)).setUnackTimeout(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
		long timeout = 3000L;
		long jitteredTimeout = workflowSweeper.workflowOffsetWithJitter(timeout);
        assertTrue(jitteredTimeout >= timeout - (timeout / 3));
        assertTrue(jitteredTimeout <= timeout + (timeout / 3));
    }
}