
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.netflix.conductor.core.WorkflowContext;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Before
    public void setup() {
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweepAsync(workflowId);
        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweep(workflowId);
        verify(queueDAO).remove(eq("DECIDER_QUEUE"), eq(workflowId));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");
        long workflowOffsetTimeout = 5000L;
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        verify(queueDAO).setUnackTimeout(eq("DECIDER_QUEUE"), eq("testWorkflowId"), eq(workflowOffsetTimeout * 1000));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result >= 30L && result <= 60L);
    }
}
