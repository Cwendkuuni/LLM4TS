
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Mock
    private WorkflowExecutor workflowExecutor;
    
    @Mock
    private WorkflowRepairService workflowRepairService;
    
    @Mock
    private ConductorProperties properties;
    
    @Mock
    private QueueDAO queueDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() throws Exception {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync("workflowId");
        //Assert statements as per your need
    }

    @Test
    public void testSweep() throws Exception {
        workflowSweeper.sweep("workflowId");
        //Assert statements as per your need
    }

    @Test
    public void testUnack() throws Exception {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setResponseTimeoutSeconds(0);

        TaskDef taskDef = new TaskDef();
        taskDef.setPollTimeoutSeconds(0);
        taskModel.setTaskDefinition(Optional.of(taskDef));

        workflowModel.getTasks().add(taskModel);

        workflowSweeper.unack(workflowModel, 1L);
        //verify() and assert statements as per your need
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        long result = workflowSweeper.workflowOffsetWithJitter(45L);
        //Assert statements as per your need
    }
}
