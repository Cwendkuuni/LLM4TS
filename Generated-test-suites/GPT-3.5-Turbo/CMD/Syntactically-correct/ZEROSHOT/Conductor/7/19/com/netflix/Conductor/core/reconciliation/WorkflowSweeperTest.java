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
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import static org.junit.Assert.assertEquals;

public class WorkflowSweeperTest {

    private WorkflowExecutor workflowExecutor;
    private Optional<WorkflowRepairService> workflowRepairService;
    private ConductorProperties properties;
    private QueueDAO queueDAO;
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        workflowRepairService = Optional.of(Mockito.mock(WorkflowRepairService.class));
        properties = Mockito.mock(ConductorProperties.class);
        queueDAO = Mockito.mock(QueueDAO.class);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, workflowRepairService, properties, queueDAO);
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> completableFuture = workflowSweeper.sweepAsync(workflowId);
        completableFuture.get();
        Mockito.verify(workflowSweeper, Mockito.times(1)).sweep(workflowId);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, workflowRepairService, properties, queueDAO);
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        // Assert that result is within [30, 60]
        assertEquals(true, result >= 30 && result <= 60);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");
        workflowModel.setStatus(Status.IN_PROGRESS);

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        workflowModel.getTasks().add(taskModel);

        workflowSweeper.unack(workflowModel, 45);

        Mockito.verify(queueDAO, Mockito.times(1)).setUnackTimeout(Mockito.eq("decider_queue"), Mockito.eq("testWorkflowId"), Mockito.anyLong());
    }

    @Test
    public void testSweepWorkflowFound() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        Mockito.when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.eq("decider_queue"), Mockito.eq("testWorkflowId"));
    }

    @Test
    public void testSweepWorkflowNotFound() {
        String workflowId = "testWorkflowId";
        Mockito.when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Workflow not found"));

        workflowSweeper.sweep(workflowId);

        Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.eq("decider_queue"), Mockito.eq("testWorkflowId"));
    }
}