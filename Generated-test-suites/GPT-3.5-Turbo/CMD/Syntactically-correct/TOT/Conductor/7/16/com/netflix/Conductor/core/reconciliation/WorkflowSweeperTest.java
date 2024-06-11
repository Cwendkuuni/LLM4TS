import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
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

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        assertNotNull(future);
        assertTrue(future.isDone());
        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    public void testSweep_whenWorkflowIsNotFound() {
        String workflowId = "nonExistentWorkflowId";
        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Workflow not found"));
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).remove(anyString(), eq(workflowId));
    }
    
    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(10);
        workflowModel.getTasks().add(taskModel);
        
        workflowSweeper.unack(workflowModel, 30);
        
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), eq(workflowModel.getWorkflowId()), eq(11L * 1000));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long jitterTime = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(jitterTime >= 30 && jitterTime <= 60);
    }
}