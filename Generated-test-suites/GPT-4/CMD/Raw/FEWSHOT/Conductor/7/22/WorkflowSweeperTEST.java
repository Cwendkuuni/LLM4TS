
package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.anyString;

import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() throws Exception {

        CompletableFuture<Void> result = workflowSweeper.sweepAsync("123");
        assertNotNull(result);
    }

    @Test
    public void testSweep() throws Exception {
        WorkflowModel mockWorkflowModel = mock(WorkflowModel.class);

        when(workflowExecutor.decide(anyString())).thenReturn(mockWorkflowModel);
        when(mockWorkflowModel.getStatus().isTerminal()).thenReturn(false);

        workflowSweeper.sweep("123");

        verify(workflowExecutor).decide(anyString());
        verify(workflowRepairService).verifyAndRepairWorkflowTasks(anyString());
        verify(mockWorkflowModel).getStatus();

    }

    @Test
    public void testUnack() {

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("123");

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);

        workflowModel.getTasks().add(taskModel);

        workflowSweeper.unack(workflowModel, 1000);

        verify(queueDAO).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitteredTime = workflowSweeper.workflowOffsetWithJitter(30000);
        assertTrue(jitteredTime >= 20000); 
        assertTrue(jitteredTime <= 40000);
    }
}
