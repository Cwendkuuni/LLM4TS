import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
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
import static org.mockito.ArgumentMatchers.*;
import java.util.Optional;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.Random;

public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        assertNull(future.join());
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        WorkflowModel mockWorkflowModel = new WorkflowModel();
        mockWorkflowModel.setWorkflowId(workflowId);

        when(workflowExecutor.decide(anyString())).thenReturn(mockWorkflowModel);
        doNothing().when(queueDAO).remove(anyString(), anyString());

        workflowSweeper.sweep(workflowId);

        mockWorkflowModel.setStatus(Status.COMPLETED);
        workflowSweeper.sweep(workflowId);

        NotFoundException notFoundException = new NotFoundException("Workflow not found");
        when(workflowExecutor.decide(anyString())).thenThrow(notFoundException);
        doNothing().when(queueDAO).remove(anyString(), anyString());
        workflowSweeper.sweep(workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel mockWorkflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(10);
        mockWorkflowModel.getTasks().add(taskModel);

        workflowSweeper.unack(mockWorkflowModel, 45);

        TaskModel taskModel2 = new TaskModel();
        taskModel2.setStatus(Status.SCHEDULED);
        TaskDef taskDef = new TaskDef();
        taskDef.setPollTimeoutSeconds(15);
        taskModel2.setTaskDefinition(Optional.of(taskDef));
        mockWorkflowModel.getTasks().add(taskModel2);

        workflowSweeper.unack(mockWorkflowModel, 45);

        TaskModel taskModel3 = new TaskModel();
        taskModel3.setStatus(Status.COMPLETED);
        mockWorkflowModel.getTasks().add(taskModel3);

        workflowSweeper.unack(mockWorkflowModel, 45);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitter = workflowSweeper.workflowOffsetWithJitter(45);
        assertTrue(jitter >= 30 && jitter <= 60);
    }
}