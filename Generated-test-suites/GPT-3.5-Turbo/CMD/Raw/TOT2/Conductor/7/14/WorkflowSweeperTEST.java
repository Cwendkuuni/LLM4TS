import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        assertNotNull(future);

        // Verify that sweep method is called
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        
        // Mock behavior for workflowExecutor.decide method
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        // Call the sweep method
        workflowSweeper.sweep(workflowId);

        // Verify that queueDAO.remove is called when workflow is terminal
        verify(queueDAO, times(1)).remove(anyString(), eq(workflowId));
    }

    @Test
    public void testSweepWithException() {
        String workflowId = "testWorkflowId";
        
        // Mock behavior to throw NotFoundException
        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Workflow not found"));

        // Call the sweep method
        workflowSweeper.sweep(workflowId);

        // Verify that queueDAO.remove is called when NotFoundException is thrown
        verify(queueDAO, times(1)).remove(anyString(), eq(workflowId));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(Collections.singletonList(new TaskModel()));

        workflowSweeper.unack(workflowModel, 60);

        // Add assertions here based on the logic in the unack method
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(45);

        // Add assertions here based on the logic in the workflowOffsetWithJitter method
    }
}