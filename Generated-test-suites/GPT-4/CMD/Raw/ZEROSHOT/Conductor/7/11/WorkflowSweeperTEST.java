
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.junit.Before;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;

import static org.mockito.Mockito.*;

import java.util.Optional;

public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private Optional<WorkflowRepairService> workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepWorkflowNotFound() {
        String workflowId = "test-wf-id";
        when(workflowExecutor.decide(workflowId)).thenThrow(NotFoundException.class);
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).remove(anyString(), eq(workflowId));
    }

    @Test
    public void testSweepWorkflowExceptionOccured() {
        String workflowId = "test-wf-id";
        when(workflowExecutor.decide(workflowId)).thenThrow(RuntimeException.class);
        workflowSweeper.sweep(workflowId);
    }

    @Test
    public void testSweepWorkflowNormalExecution() {
        String workflowId = "test-wf-id";
        WorkflowModel wfModelMock = Mockito.mock(WorkflowModel.class);
        when(workflowExecutor.decide(workflowId)).thenReturn(wfModelMock);
        when(wfModelMock.getStatus()).thenReturn(Status.COMPLETED);
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).remove(anyString(), eq(workflowId));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
      long workflowOffsetTimeout = 100;
      long jitteredTimeout = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
      assertTrue(jitteredTimeout >= (2.0/3.0)*workflowOffsetTimeout  && jitteredTimeout <= (4.0/3.0)*workflowOffsetTimeout);
    }

    @Test
    public void testUnack() {
        WorkflowModel wfModelMock = Mockito.mock(WorkflowModel.class);
        TaskModel taskModelMock = Mockito.mock(TaskModel.class);
        TaskDef taskDefMock = Mockito.mock(TaskDef.class);

        when(wfModelMock.getTasks()).thenReturn(Arrays.asList(taskModelMock));
        when(taskModelMock.getStatus()).thenReturn(Status.SCHEDULED);
        when(taskModelMock.getTaskDefinition()).thenReturn(Optional.of(taskDefMock));
        when(workflowSweeper.workflowOffsetWithJitter(anyLong())).thenReturn(50l);

        workflowSweeper.unack(wfModelMock, 100l);

        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());  
    }
}
