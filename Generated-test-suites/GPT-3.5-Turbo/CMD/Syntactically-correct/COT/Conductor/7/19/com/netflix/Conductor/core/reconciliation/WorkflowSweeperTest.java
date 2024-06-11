
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;
import java.util.Optional;

import org.junit.Test;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class WorkflowSweeperTest {

    private WorkflowExecutor mockWorkflowExecutor = mock(WorkflowExecutor.class);
    private WorkflowRepairService mockWorkflowRepairService = mock(WorkflowRepairService.class);
    private ConductorProperties mockConductorProperties = mock(ConductorProperties.class);
    private QueueDAO mockQueueDAO = mock(QueueDAO.class);

    private WorkflowSweeper workflowSweeper = new WorkflowSweeper(mockWorkflowExecutor, Optional.of(mockWorkflowRepairService), mockConductorProperties, mockQueueDAO);

    @Test
    public void testWorkflowSweeperSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        assertNotNull(future);
    }

    @Test
    public void testWorkflowSweeperSweep() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        when(mockWorkflowExecutor.decide(anyString())).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        verify(mockQueueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testWorkflowSweeperSweepNotFoundException() {
        String workflowId = "testWorkflowId";
        when(mockWorkflowExecutor.decide(anyString())).thenThrow(new NotFoundException("Test not found"));
        
        workflowSweeper.sweep(workflowId);

        verify(mockQueueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testWorkflowSweeperSweepException() {
        String workflowId = "testWorkflowId";
        when(mockWorkflowExecutor.decide(anyString())).thenThrow(new RuntimeException("Test exception"));

        workflowSweeper.sweep(workflowId);

        verify(mockQueueDAO, never()).remove(anyString(), anyString());
        verify(mockQueueDAO, never()).setUnackTimeout(anyString(), anyString(), anyLong());
        verify(mockQueueDAO, never()).remove(anyString(), anyString());
    }

}
