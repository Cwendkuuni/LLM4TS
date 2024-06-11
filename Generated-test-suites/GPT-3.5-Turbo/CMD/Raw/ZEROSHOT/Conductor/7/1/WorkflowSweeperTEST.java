import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.stereotype.Component;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.model.WorkflowModel;

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

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    private WorkflowSweeper workflowSweeperSpy;

    @Before
    public void setUp() {
        workflowSweeperSpy = spy(new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO));
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync("testWorkflowId");

        assertNotNull(future);
        assertTrue(future.isDone());
    }

    @Test
    public void testSweep() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");

        doNothing().when(workflowSweeperSpy).unack(any(), anyLong());

        workflowSweeperSpy.sweep("testWorkflowId");

        verify(workflowExecutor, times(1)).decide("testWorkflowId");
        verify(queueDAO, times(1)).remove(anyString(), eq("testWorkflowId"));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");

        // Add test case when task status is IN_PROGRESS

        workflowSweeperSpy.unack(workflowModel, 60);

        verify(queueDAO, times(1)).setUnackTimeout(anyString(), eq("testWorkflowId"), eq(61 * 1000));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeperSpy.workflowOffsetWithJitter(45);

        assertTrue(result >= 30 && result <= 60);
    }
}