import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;

import java.util.concurrent.CompletableFuture;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {
    
    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;
    
    @Before
    public void setUp() {
        when(properties.getWorkflowOffsetTimeout()).thenReturn(Duration.ofSeconds(30));
    }

    @Test
    public void testSweepAsync() {
        when(workflowSweeper.sweepAsync(anyString())).thenReturn(CompletableFuture.completedFuture(null));
        String workflowId = "testWorkflow";
        workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper).sweepAsync(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflow";
        workflowSweeper.sweep(workflowId);
        verify(workflowSweeper).sweep(workflowId);
    }

    @Test(expected = NotFoundException.class)
    public void testSweepNotFound() {
        String workflowId = "nonExistantWorkflow";
        doThrow(NotFoundException.class).when(workflowExecutor).decide(workflowId);
        workflowSweeper.sweep(workflowId);
    }

    @Test(expected = Exception.class)
    public void testSweepException() {
        String workflowId = "testWorkflow";
        doThrow(Exception.class).when(workflowExecutor).decide(workflowId);
        workflowSweeper.sweep(workflowId);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        Long result = workflowSweeper.workflowOffsetWithJitter(30L);
        assertNotNull(result);
        assertTrue(result >= 20 && result <= 40);
    }
}