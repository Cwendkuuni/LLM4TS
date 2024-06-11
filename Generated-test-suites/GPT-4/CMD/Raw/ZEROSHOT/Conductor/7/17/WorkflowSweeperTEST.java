
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static com.netflix.conductor.core.utils.Utils.DECIDER_QUEUE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private QueueDAO queueDAO;
    private ConductorProperties properties;
    private Optional<WorkflowRepairService> workflowRepairServiceOptional;

    @Before
    public void setup() {
        workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        workflowRepairService = Mockito.mock(WorkflowRepairService.class);
        queueDAO = Mockito.mock(QueueDAO.class);
        properties = Mockito.mock(ConductorProperties.class);
        workflowRepairServiceOptional = Optional.of(workflowRepairService);

        workflowSweeper = new WorkflowSweeper(workflowExecutor, workflowRepairServiceOptional, properties, queueDAO);
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweepAsync(workflowId).get();
        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        given(workflowExecutor.decide(workflowId)).willReturn(workflowModel);

        workflowSweeper.sweep(workflowId);
        verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowId, 0);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_HUMAN);
        taskModel.setWaitTimeout(10);
        workflowModel.setTasks(new ArrayList<>());
        workflowModel.getTasks().add(taskModel);

        workflowSweeper.unack(workflowModel, 10L);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 11000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(30L);
        assert result >= 20 && result <= 40;
    }
}
