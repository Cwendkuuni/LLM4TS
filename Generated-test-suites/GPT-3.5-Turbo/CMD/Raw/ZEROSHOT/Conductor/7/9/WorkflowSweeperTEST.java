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

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.exception.NotFoundException;
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

    private WorkflowModel testWorkflow;

    @Before
    public void setup() {
        testWorkflow = new WorkflowModel();
        testWorkflow.setWorkflowId("testWorkflowId");

        WorkflowContext workflowContext = new WorkflowContext("testAppId");
        WorkflowContext.set(workflowContext);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync("testWorkflowId");
        assertNotNull(result);
    }

    @Test
    public void testSweep() {
        doNothing().when(workflowExecutor).decide(anyString());

        workflowSweeper.sweep("testWorkflowId");

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testSweepNotFound() {
        doThrow(NotFoundException.class).when(workflowExecutor).decide(anyString());

        workflowSweeper.sweep("testWorkflowId");

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testSweepException() {
        doThrow(new RuntimeException()).when(workflowExecutor).decide(anyString());

        workflowSweeper.sweep("testWorkflowId");

        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testUnack() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(10);

        testWorkflow.getTasks().add(taskModel);

        workflowSweeper.unack(testWorkflow, 30);

        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), eq(11L * 1000));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(45);
        assertTrue(result >= 30 && result <= 60);
    }
}