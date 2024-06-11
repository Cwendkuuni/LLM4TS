
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Future;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

@RunWith(MockitoJUnitRunner.class)
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

    @Test
    public void testSweep() throws Exception {
        String workflowId = "workflowId";
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.WorkflowStatus.RUNNING);

        workflowSweeper.sweep(workflowId);

        verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
        verify(workflowExecutor).decide(workflowId);
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testWorkflowNotFound() throws Exception {
        String workflowId = "workflowId";
        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Workflow Not Found"));

        workflowSweeper.sweep(workflowId);

        verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
        verify(queueDAO).remove(eq(DECIDER_QUEUE), eq(workflowId));
    }

    @Test
    public void testSweepAsync() throws Exception {
        String workflowId = "workflowId";
        Future<Void> result = workflowSweeper.sweepAsync(workflowId);

        assertEquals(result.get(), null);
    }

    @Test
    public void testUnack() throws Exception {
        long workflowOffsetTimeout = 100L;
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskModel taskModel = mock(TaskModel.class);
        when(workflowModel.getTasks()).thenReturn(Arrays.asList(taskModel));
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), workflowOffsetTimeout);
    }
}
