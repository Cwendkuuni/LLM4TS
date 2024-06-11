import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;

public class WorkflowSweeperTest {
     
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    
    @InjectMocks
    WorkflowSweeper workflowSweeper;

    @Mock
    WorkflowExecutor workflowExecutor;
        
    @Mock
    WorkflowRepairService workflowRepairService;
    
    @Mock
    ConductorProperties properties;
    
    @Mock
    QueueDAO queueDAO;
   
    @Test
    public void testSweep() throws Exception {
        String workflowId = "workflowId";

        when(workflowExecutor.decide(workflowId)).thenReturn(new WorkflowModel());
        doNothing().when(queueDAO).remove(anyString(), anyString());
        
        workflowSweeper.sweep(workflowId);

        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }
    
}