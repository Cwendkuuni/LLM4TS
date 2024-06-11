
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

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

    @Test
    public void testSweepAsync() throws InterruptedException {
        workflowSweeper.sweepAsync("testWorkflow1");
        verify(workflowSweeper, times(1)).sweep("testWorkflow1");
    }

    @Test
    public void testSweep_WorkflowNotFound() {
        String workflowId = "testWorkflow2";
        doThrow(NotFoundException.class)
                .when(workflowExecutor)
                .decide(workflowId);
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep_WorkflowFound() {
        String workflowId = "testWorkflow3";
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep_WorkflowException() {
        String workflowId = "testWorkflow4";
        when(workflowExecutor.decide(workflowId)).thenThrow(new RuntimeException());
        workflowSweeper.sweep(workflowId);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 30L;
        long jitteredOffsetTimeout = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(jitteredOffsetTimeout >= 20L && jitteredOffsetTimeout <= 40L);
    }
}
