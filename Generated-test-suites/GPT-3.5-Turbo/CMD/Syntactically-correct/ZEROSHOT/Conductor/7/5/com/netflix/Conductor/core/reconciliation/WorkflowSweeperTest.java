import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private ConductorProperties properties;
    private QueueDAO queueDAO;

    @Before
    public void setUp() {
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        properties = new ConductorProperties();
        queueDAO = mock(QueueDAO.class);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        assertNotNull(future);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowContext context = new WorkflowContext("testAppId");
        WorkflowContext.set(context);

        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result >= 30 && result <= 60);
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
    public void testSweepWorkflowNotFound() {
        String workflowId = "testWorkflowId";

        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException());
        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(anyString(), eq(workflowId));
    }

    @Test
    public void testSweepException() {
        String workflowId = "testWorkflowId";

        when(workflowExecutor.decide(workflowId)).thenThrow(new RuntimeException("Test Exception"));
        workflowSweeper.sweep(workflowId);

        verify(queueDAO, never()).remove(anyString(), anyString());
    }
}