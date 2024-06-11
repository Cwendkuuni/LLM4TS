
import static org.mockito.Mockito.*;

import java.util.Random;
import java.util.Optional;

import java.util.concurrent.CompletableFuture;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;

import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.common.metadata.tasks.TaskDef;

@RunWith(SpringRunner.class)
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
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_sweepAsync() {
        String workflowId = "TestWorkflowId";
        workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void test_sweep() {
        String workflowId = "TestWorkflowId";
        WorkflowContext context = new WorkflowContext("appId");
        WorkflowModel model = mock(WorkflowModel.class);
        when(workflowExecutor.decide(workflowId)).thenReturn(model);
        when(properties.getWorkflowOffsetTimeout().getSeconds()).thenReturn(1L);

        workflowSweeper.sweep(workflowId);

        verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void test_unack() {
         long workflowOffsetTimeout = 123L;
         WorkflowModel workflowModel = mock(WorkflowModel.class);
         TaskModel taskModel = mock(TaskModel.class);
         when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskModel));
         when(taskModel.getStatus()).thenReturn(Status.IN_PROGRESS);
         when(taskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_WAIT);

         workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
         verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void test_workflowOffsetWithJitter() {
        long workflowOffsetTimeout = 60L;
        long workflowOffsetWithJitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(workflowOffsetWithJitter >= 30 && workflowOffsetWithJitter <= 90);
    }
}
