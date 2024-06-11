import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import java.util.concurrent.CompletableFuture;
import java.util.Optional;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.tasks.PollData;
import static org.mockito.Mockito.*;
import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;
import static com.netflix.conductor.core.utils.Utils.DECIDER_QUEUE;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private ConductorProperties properties;
    private QueueDAO queueDAO;
    
    @Test
    public void testSweepAsync() {
        String workflowId = "1234";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        Assert.assertNotNull(future);
    }
    
    @Test
    public void testSweep() {
        String workflowId = "1234";
        WorkflowModel mockWorkflow = Mockito.mock(WorkflowModel.class);
        when(workflowExecutor.decide(workflowId)).thenReturn(mockWorkflow);
        
        workflowSweeper.sweep(workflowId);
        
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }
    
    @Test
    public void testUnack() {
        WorkflowModel mockWorkflow = Mockito.mock(WorkflowModel.class);
        when(mockWorkflow.getTasks()).thenReturn(new ArrayList<TaskModel>());
        
        workflowSweeper.unack(mockWorkflow, 60);
        
        // Add assertions based on expected behavior
    }
    
    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(45);
        Assert.assertTrue(result >= 30 && result <= 60);
    }
}