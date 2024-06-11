import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
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
import org.junit.Before;
import org.junit.Test;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
        properties = mock(ConductorProperties.class);
        queueDAO = mock(QueueDAO.class);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        try {
            future.get();
            verify(workflowExecutor).decide(workflowId);
        } catch (InterruptedException | ExecutionException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSweepExceptionHandling() {
        String workflowId = "testWorkflowId";
        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Workflow not found"));
        workflowSweeper.sweep(workflowId);
        verify(queueDAO).remove(anyString(), eq(workflowId));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowSweeper ws = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        long workflowOffsetTimeout = 45;
        long result = ws.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result >= 30 && result <= 60);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.IN_PROGRESS);
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(10); // Edge case
        workflowModel.getTasks().add(taskModel);
        
        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);
        workflowSweeper.unack(workflowModel, 30);
        verify(queueDAO).setUnackTimeout(anyString(), anyString(), eq(11 * 1000L)); // Edge case

        // Test another case
        WorkflowModel workflowModel2 = new WorkflowModel();
        workflowModel2.setStatus(Status.SCHEDULED);
        TaskModel taskModel2 = new TaskModel();
        taskModel2.setStatus(Status.SCHEDULED);
        Optional<TaskDef> optionalTaskDef = Optional.of(new TaskDef());
        optionalTaskDef.get().setPollTimeoutSeconds(20); // Edge case
        workflowModel2.getTasks().add(taskModel2);

        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel2);
        workflowSweeper.unack(workflowModel2, 30);
        verify(queueDAO).setUnackTimeout(anyString(), anyString(), eq(21 * 1000L)); // Edge case
    }
}