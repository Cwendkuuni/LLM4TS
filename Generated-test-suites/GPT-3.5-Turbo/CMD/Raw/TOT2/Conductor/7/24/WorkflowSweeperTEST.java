import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        properties = Mockito.mock(ConductorProperties.class);
        workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        workflowRepairService = Mockito.mock(WorkflowRepairService.class);
        queueDAO = Mockito.mock(QueueDAO.class);
        
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.ofNullable(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        assertNotNull(result);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        ReflectionTestUtils.setField(workflowSweeper, "properties", properties);
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result >= 30 && result <= 60);
    }

    // Additional test cases for other methods in WorkflowSweeper can be added here

}