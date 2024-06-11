package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.util.Optional;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private WorkflowRepairService workflowRepairService;
    @Mock
    private ConductorProperties properties;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private WorkflowModel workflowModel;
    @Mock
    private TaskModel taskModel;
    @Mock
    private TaskDef taskDef;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        final String workflowId = "workflowId";
        CompletableFuture future = workflowSweeper.sweepAsync(workflowId);
        
        future.join();
        
        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testSweep() {
        final String workflowId = "workflowId";
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        workflowSweeper.sweep(workflowId);
        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testUnack() {
        final String workflowId = "workflowId";
        final long workflowOffsetTimeout = 7000;
        
        when(workflowModel.getTasks()).thenReturn(Arrays.asList(taskModel));
        when(workflowModel.getWorkflowId()).thenReturn(workflowId);
        
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(taskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_WAIT);

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        
        verify(queueDAO).setUnackTimeout(any(), any(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        final long workflowOffsetTimeout = 10;
        when(properties.getWorkflowOffsetTimeout()).thenReturn(Duration.ofSeconds(workflowOffsetTimeout));

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        //can't assert specific value due to randomness, but we can check that the result is within expected range
        assertTrue(result >= (2 * workflowOffsetTimeout) / 3 && result <= (4 * workflowOffsetTimeout) / 3);
    }
}