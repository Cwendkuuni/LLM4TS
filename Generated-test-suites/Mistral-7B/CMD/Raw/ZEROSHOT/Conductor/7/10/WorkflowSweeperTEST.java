import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metrics.Monitors;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;

public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private WorkflowSweeper sweeper;

    private static final String WORKFLOW_ID = "workflowId";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(properties.getAppId()).thenReturn("appId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(workflowTimeout());
        when(queueDAO.remove(Utils.DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);

        WorkflowContext workflowContext = new WorkflowContext("appId");
        WorkflowContext.set(workflowContext);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(mockWorkflowModel());
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException());

        when(workflowRepairService.verifyAndRepairWorkflowTasks(WORKFLOW_ID)).thenReturn(null);
    }

    @Test
    public void testSweepAsync() {
        sweeper.sweepAsync(WORKFLOW_ID);
        assertFalse(sweeper.isSweeping());
    }

    @Test
    public void testSweepWithRepairService() {
        sweeper.sweep(WORKFLOW_ID);

        ArgumentCaptor<WorkflowModel> workflowModelArgumentCaptor = ArgumentCaptor.forClass(WorkflowModel.class);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModelArgumentCaptor.capture());
        WorkflowModel capturedWorkflowModel = workflowModelArgumentCaptor.getValue();

        assertNotNull(capturedWorkflowModel);
        assertEquals(Status.RUNNING, capturedWorkflowModel.getStatus());

        when(workflowRepairService.verifyAndRepairWorkflowTasks(WORKFLOW_ID)).thenReturn(Arrays.asList(taskModel()));
        sweeper.sweep(WORKFLOW_ID);
        verify(workflowExecutor, times(2)).decide(WORKFLOW_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testSweepWithNotFoundException() {
        sweeper.sweep(WORKFLOW_ID);
    }

    @Test
    public void testSweepWithNullWorkflowModel() {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(null);
        sweeper.sweep(WORKFLOW_ID);
        verify(queueDAO, times(1)).setUnackTimeout(Utils.DECIDER_QUEUE, WORKFLOW_ID, workflowOffsetTimeout() * 1000);
    }

    @Test
    public void testSweepWithTerminalWorkflowModel() {
        WorkflowModel terminalWorkflowModel = new WorkflowModel();
        terminalWorkflowModel.setStatus(Status.TERMINAL);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(terminalWorkflowModel);

        sweeper.sweep(WORKFLOW_ID);
        verify(queueDAO, times(1)).remove(Utils.DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mockWorkflowModel();
        sweeper.unack(workflowModel, workflowOffsetTimeout());
        verify(queueDAO, times(1)).setUnackTimeout(Utils.DECIDER_QUEUE, workflowModel.getWorkflowId(), workflowOffsetTimeout() * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        int workflowOffsetTimeoutSeconds = 45;
        long workflowOffsetWithJitter = sweeper.workflowOffsetWithJitter(workflowOffsetTimeoutSeconds);
        assertTrue(workflowOffsetWithJitter > (workflowOffsetTimeoutSeconds - 10));
        assertTrue(workflowOffsetWithJitter < (workflowOffsetTimeoutSeconds + 10));
    }

    private WorkflowModel mockWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setStatus(Status.RUNNING);
        return workflowModel;
    }

    private long workflowOffsetTimeout() {
        return 45 * 1000;
    }

    private TaskModel taskModel() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        return taskModel;
    }
}