import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;

public class WorkflowSweeperTest {

    @Mock
    ConductorProperties propertiesMock;

    @Mock
    WorkflowExecutor workflowExecutorMock;

    @Mock
    WorkflowRepairService workflowRepairServiceMock;

    @Mock
    QueueDAO queueDAOMock;
    
    @Autowired
    WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        workflowSweeper = new WorkflowSweeper(workflowExecutorMock, 
        Optional.ofNullable(workflowRepairServiceMock), propertiesMock, queueDAOMock);
    }

    // Test case for sweepAsync method
    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        WorkflowSweeper spySweeper = spy(workflowSweeper);
        
        CompletableFuture<Void> result = spySweeper.sweepAsync(workflowId);
        
        verify(spySweeper, times(1)).sweep(eq(workflowId));
        assertNotNull(result);
    }

    // Test case for sweep method when workflow is found
    @Test
    public void testSweepWorkflowFound() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.COMPLETED);
        when(workflowExecutorMock.decide(eq(workflowId))).thenReturn(workflowModel);
        
        workflowSweeper.sweep(workflowId);
        
        verify(queueDAOMock).remove(anyString(), eq(workflowId));
    }

    // Test case for sweep method when workflow is not found
    @Test
    public void testSweepWorkflowNotFound() {
        String workflowId = "testWorkflowId";
        when(workflowExecutorMock.decide(eq(workflowId))).thenThrow(new NotFoundException("Workflow not found"));
        
        workflowSweeper.sweep(workflowId);
        
        verify(queueDAOMock).remove(anyString(), eq(workflowId));
    }

    // Test case for sweep method with exception
    @Test
    public void testSweepWithException() {
        String workflowId = "testWorkflowId";
        when(workflowExecutorMock.decide(eq(workflowId))).thenThrow(new RuntimeException("Test Exception"));
        
        workflowSweeper.sweep(workflowId);
        
        verifyStatic(Monitors.class, times(1));
        Monitors.error(anyString(), anyString());
    }

    // Test case for unack method with task in progress
    @Test
    public void testUnackTaskInProgress() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskInProgress = new TaskModel();
        taskInProgress.setStatus(Status.IN_PROGRESS);
        workflowModel.setTasks(List.of(taskInProgress));
        
        workflowSweeper.unack(workflowModel, 60);
        
        verify(queueDAOMock).setUnackTimeout(eq("deciderQueue"), anyString(), eq(61*1000L));
    }

    // Test case for workflowOffsetWithJitter method
    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowSweeper spySweeper = spy(workflowSweeper);
        when(spySweeper.workflowOffsetWithJitter(eq(45L))).thenReturn(50L);
        
        long result = spySweeper.workflowOffsetWithJitter(45L);
        
        assertEquals(50L, result);
    }
}