import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.QueueDAO;
import com.netflix.conductor.core.reconciliation.WorkflowModel;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.model.WorkflowModelImpl;
import com.netflix.conductor.metrics.Monitors;

import static com.netflix.conductor.core.utils.Utils.DECIDER_QUEUE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper sweeper;

    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private ConductorProperties properties;
    private QueueDAO queueDAO;
    private Monitors monitors;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        properties = mock(ConductorProperties.class);
        queueDAO = mock(QueueDAO.class);
        monitors = mock(Monitors.class);
        sweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.of(workflowRepairService),
                properties,
                queueDAO);
        when(properties.getAppId()).thenReturn("AppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(new ConductorProperties.Duration(60));
        when(properties.getWorkflowOffsetTimeout().getSeconds()).thenReturn(60);
        when(monitors.recordUnackTime(anyString(), anyLong())).thenReturn(0L);
    }

    @Test
    public void testSweepAsync() {
        sweeper.sweepAsync("workflowId");
        // To be implemented
    }

    @Test
    public void testSweep() {
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setWorkflowId("workflowId");
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        when(queueDAO.getWorkflowByWorkflowId(anyString())).thenReturn(workflowModel);

        sweeper.sweep("workflowId");

        verifySweeperFunctionality("workflowId");;
        verifyExceptionHandling("workflowId");
        verifyQueueDaoMethods();
    }

    private void verifySweeperFunctionality(String workflowId) {
        // Check if workflow is removed from decider queue
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);

        // Check if workflow is not found, it is removed from decider queue
        when(queueDAO.getWorkflowByWorkflowId(workflowId)).thenThrow(new NotFoundException());
        sweeper.sweep("workflowId");
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);

        // Check if unackTimeout is set correctly for workflow
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId)).thenReturn(0L);
        sweeper.sweep("workflowId");
        OngoingStubbing<Long> unackTimeout = mock(Long.class);
        unackTimeout.thenReturn(61L);
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong())).thenReturn(unackTimeout);
        sweeper.sweep("workflowId");
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowId, 61 * 1000);
    }

    private void verifyExceptionHandling(String workflowId) {
        // Check if exception is logged when an error occurs during sweep
        when(queueDAO.getWorkflowByWorkflowId(workflowId)).thenThrow(new Exception());
        sweeper.sweep("workflowId");
        verify(monitors).error(WorkflowSweeper.class.getSimpleName(), "sweep");
        verifyLogMessageContains("Error running sweep for " + workflowId);
    }

    private void verifyQueueDaoMethods() {
        // Check if getWorkflowByWorkflowId method is called with correct argument
        sweeper.sweep("workflowId");
        verify(queueDAO).getWorkflowByWorkflowId(workflowId);

        // Check if setUnackTimeout method is called with correct arguments
        sweeper.sweep("workflowId");
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());
    }

    private void verifyLogMessageContains(String message) {
        // To be implemented
    }
}