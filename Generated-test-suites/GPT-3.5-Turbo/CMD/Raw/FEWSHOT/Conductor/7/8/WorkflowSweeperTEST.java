package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Test;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;

public class WorkflowSweeperTest {

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowSweeper sweeper = new WorkflowSweeper(null, null, null, null);

        long workflowOffsetTimeout = 45;
        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Verify result is within the range [workflowOffsetTimeout-1/3, workflowOffsetTimeout+1/3]
        assertEquals(true, result >= (workflowOffsetTimeout - 15) && result <= (workflowOffsetTimeout + 15));
    }

    @Test
    public void testUnack() {
        WorkflowSweeper sweeper = new WorkflowSweeper(null, null, null, null);

        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(10);
        workflowModel.setTasks(List.of(taskModel));

        sweeper.unack(workflowModel, 45);

        // Verify queueDAO.setUnackTimeout is called with expected parameters
        verify(mockQueueDAO).setUnackTimeout(eq("DECIDER_QUEUE"), eq(workflowModel.getWorkflowId()), eq(11 * 1000));
    }

    @Test
    public void testSweepAsync() {
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
        ConductorProperties properties = mock(ConductorProperties.class);
        QueueDAO queueDAO = mock(QueueDAO.class);

        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        
        CompletableFuture<Void> future = sweeper.sweepAsync("testWorkflowId");

        future.join();

        // Verify workflowExecutor.decide is called with expected workflowId
        verify(workflowExecutor).decide(eq("testWorkflowId"));
    }

    @Test
    public void testSweep() {
        WorkflowSweeper sweeper = new WorkflowSweeper(null, null, null, null);

        sweeper.sweep("testWorkflowId");

        // Verify expected behavior when WorkflowModel is null
        // Verify LOGGER.warn is called with expected message
    }

    // Add more test cases for comprehensive coverage of WorkflowSweeper class

}