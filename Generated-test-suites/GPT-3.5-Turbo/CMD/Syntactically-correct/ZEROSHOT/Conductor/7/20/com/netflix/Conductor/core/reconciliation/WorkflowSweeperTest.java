import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
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

    @Before
    public void setUp() {
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        assertNotNull(result);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result >= 30 && result <= 60);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");
        workflowModel.setStatus("testStatus");
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus("IN_PROGRESS");
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        workflowModel.getTasks().add(taskModel);

        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong())).thenReturn(true);

        workflowSweeper.unack(workflowModel, 30);

        verify(queueDAO, times(1)).setUnackTimeout(eq("deciderQueue"), eq("testWorkflowId"), eq(30L * 1000));
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus("testStatus");
        workflowModel.setWorkflowId(workflowId);

        WorkflowContext workflowContext = new WorkflowContext("testAppId");
        WorkflowContext.set(workflowContext);

        when(workflowExecutor.decide(eq(workflowId))).thenReturn(workflowModel);
        when(workflowRepairService.verifyAndRepairWorkflowTasks(eq(workflowId))).thenReturn(true);

        workflowSweeper.sweep(workflowId);

        Mockito.verify(queueDAO, times(1)).remove(eq("deciderQueue"), eq(workflowId));
    }

}