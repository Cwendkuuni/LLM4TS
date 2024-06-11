

package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;

import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
public class WorkflowSweeperTest {

    @InjectMocks
    WorkflowSweeper workflowSweeper;

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowContext workflowContext;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private TaskModel taskModel;

    private static final String WORKFLOW_ID = "workflow1";

    @Before
    public void setUp() throws Exception {
        when(properties.getAppId()).thenReturn("appId");
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        doNothing().when(workflowRepairService).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
        assertEquals(null, workflowSweeper.sweepAsync(WORKFLOW_ID).get());
    }

    @Test
    public void testSweep_success() {
        doNothing().when(workflowRepairService).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
        workflowSweeper.sweep(WORKFLOW_ID);
        verify(queueDAO, times(1)).remove("DECIDER_QUEUE", WORKFLOW_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSweep_fail() {
        doThrow(new IllegalArgumentException()).when(workflowRepairService).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
        workflowSweeper.sweep(WORKFLOW_ID);
    }

    @Test
    public void testUnack() {
        when(workflowModel.getWorkflowId()).thenReturn(WORKFLOW_ID);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(taskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_WAIT);
        workflowSweeper.unack(workflowModel, 10); 
        verify(queueDAO, times(1)).setUnackTimeout("DECIDER_QUEUE", "workflow1", 10000);
    }

    @Test
    public void testWorkflowOffsetWithJitter_positiveJitter() {
        long workflowOffsetTimeout = 30L;
        long jitteredTimeout = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertEquals(true, jitteredTimeout > workflowOffsetTimeout);
    }

    @Test
    public void testWorkflowOffsetWithJitter_negativeJitter() {
        long workflowOffsetTimeout = 60L;
        long jitteredTimeout = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertEquals(true, jitteredTimeout > 40);
    }
}
