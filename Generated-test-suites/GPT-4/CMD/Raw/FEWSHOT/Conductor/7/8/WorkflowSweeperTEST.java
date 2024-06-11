package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), conductorProperties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        doNothing().when(workflowSweeper).sweep(any());
        workflowSweeper.sweepAsync("testWorkflowId");
        verify(workflowSweeper).sweep(any());
    }

    @Test
    public void testSweep() {
        when(workflowExecutor.decide(any())).thenReturn(new WorkflowModel());
        doNothing().when(queueDAO).remove(any(), any());
        workflowSweeper.sweep("testWorkflowId");
        verify(queueDAO).remove(any(), any());
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setTaskType(TaskType.TASK_TYPE_WAIT);
        workflowModel.getTasks().add(task);
        doNothing().when(queueDAO).setUnackTimeout(any(), any(), anyLong());
        workflowSweeper.unack(workflowModel, 10L);
        verify(queueDAO).setUnackTimeout(any(), any(), anyLong());
    }
    
    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(45L);
        assert(result>=30 && result<=60);
    }

}