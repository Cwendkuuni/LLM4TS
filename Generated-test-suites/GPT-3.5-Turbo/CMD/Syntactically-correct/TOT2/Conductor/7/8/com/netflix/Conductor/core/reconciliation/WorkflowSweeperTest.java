import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;
    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private WorkflowRepairService workflowRepairService;
    @Mock
    private QueueDAO queueDAO;

    WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.ofNullable(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync("testWorkflowId");
        assertNotNull(result);
        assertTrue(result.isDone());
    }

    @Test
    public void testSweep() {
        workflowSweeper.sweep("testWorkflowId");
        // Write appropriate asserts based on the scenarios you have tested
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(new ArrayList<>());
        workflowModel.getTasks().add(new TaskModel());
        workflowSweeper.unack(workflowModel, 60);
        // Write appropriate asserts based on the scenarios you have tested
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(45);
        // Write appropriate asserts based on the scenarios you have tested
    }
}