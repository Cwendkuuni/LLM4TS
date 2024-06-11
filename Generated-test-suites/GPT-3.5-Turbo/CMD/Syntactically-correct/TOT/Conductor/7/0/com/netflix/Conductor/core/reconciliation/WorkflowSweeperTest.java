import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
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
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);
        when(workflowSweeper.sweepAsync(workflowId)).thenReturn(future);
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        assertEquals(future, result);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        
        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(anyString(), eq(workflowId));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        long timeout = 60; // example workflowOffsetTimeout
        ReflectionTestUtils.invokeMethod(workflowSweeper, "unack", workflowModel, timeout);

        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), eq(61 * 1000));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long timeout = 45;
        long result = ReflectionTestUtils.invokeMethod(workflowSweeper, "workflowOffsetWithJitter", timeout);

        assertEquals(true, result >= 30 && result <= 60);
    }
}