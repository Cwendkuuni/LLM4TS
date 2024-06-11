import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import java.util.concurrent.ExecutionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;

public class WorkflowSweeperTest {
    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private Optional<WorkflowRepairService> workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        workflowSweeper = new WorkflowSweeper(
                workflowExecutor, workflowRepairService, properties, queueDAO
        );
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        String workflowId = "testWorkflow";
        when(workflowExecutor.decide(workflowId))
                .thenReturn(new WorkflowModel());
        Mockito.doNothing().when(queueDAO).remove(anyString(), anyString());
        
        workflowSweeper.sweepAsync(workflowId);

        Mockito.verify(workflowExecutor, timeout(100).times(1)).decide(workflowId);
        Mockito.verify(queueDAO, timeout(100).times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflow";
        when(workflowExecutor.decide(workflowId)).thenReturn(new WorkflowModel());
        Mockito.doNothing().when(queueDAO).remove(anyString(), anyString());

        workflowSweeper.sweep(workflowId);

        Mockito.verify(workflowExecutor, Mockito.times(1)).decide(workflowId);
        Mockito.verify(queueDAO, Mockito.times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 10;
        long workflowOffsetWithJitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        Assert.assertTrue(workflowOffsetWithJitter >= 0 && workflowOffsetWithJitter <= workflowOffsetTimeout + (workflowOffsetTimeout / 3));
    }
}