package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dao.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;

public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeperTest.class);

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor mockWorkflowExecutor;
    private QueueDAO mockQueueDAO;
    private ConductorProperties mockProperties;
    private WorkflowRepairService mockWorkflowRepairService;

    @Before
    public void setUp() {
        mockWorkflowExecutor = mock(WorkflowExecutor.class);
        mockQueueDAO = mock(QueueDAO.class);
        mockProperties = mock(ConductorProperties.class);
        mockWorkflowRepairService = mock(WorkflowRepairService.class);

        workflowSweeper = new WorkflowSweeper(mockWorkflowExecutor, Optional.of(mockWorkflowRepairService), mockProperties, mockQueueDAO);
    }

    @Test
    public void testSweepAsync() throws Exception {
        String workflowId = "workflow-1";
        workflowSweeper.sweepAsync(workflowId).get();
        verify(mockQueueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong());
        verify(mockWorkflowExecutor).decide(eq(workflowId));
    }

    @Test
    public void testSweep() throws Exception {
        String workflowId = "workflow-1";

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.RUNNING);
        workflowModel.setWorkflowName("workflow-name");

        when(mockWorkflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        when(mockQueueDAO.remove(eq(DECIDER_QUEUE), eq(workflowId))).thenReturn(true);

        workflowSweeper.sweep(workflowId);

        verify(mockQueueDAO).remove(eq(DECIDER_QUEUE), eq(workflowId));
        verify(mockWorkflowExecutor).decide(eq(workflowId));
    }

    @Test
    public void testSweepNotFound() throws Exception {
        String workflowId = "workflow-1";

        when(mockQueueDAO.remove(eq(DECIDER_QUEUE), eq(workflowId))).thenReturn(false);
        when(mockWorkflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Workflow not found"));

        workflowSweeper.sweep(workflowId);

        verify(mockQueueDAO, times(0)).setUnackTimeout(anyString(), anyString(), anyLong());
        verify(mockWorkflowExecutor, times(0)).decide(anyString());
        verify(mockLogger).info("Workflow NOT found for id:{}. Removed it from decider queue", workflowId);
    }

    @Test
    public void testSweepException() throws Exception {
        String workflowId = "workflow-1";

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.RUNNING);
        workflowModel.setWorkflowName("workflow-name");

        when(mockWorkflowExecutor.decide(workflowId)).thenThrow(new Exception("Error running sweep for " + workflowId));

        workflowSweeper.sweep(workflowId);

        verify(mockQueueDAO, times(0)).setUnackTimeout(anyString(), anyString(), anyLong());
        verify(mockWorkflowExecutor, times(1)).decide(eq(workflowId));
        verify(mockMonitors).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"), any(Throwable.class));
    }

    @Test
    public void testUnack() throws Exception {
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.RUNNING);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("task-1");
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskDefinition(Mockito.mock(TaskDef.class));

        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setTimeoutSeconds(45);

        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(taskModel);
        workflowModel.setTasks(tasks);

        when(mockQueueDAO.setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong())).thenReturn(true);

        workflowSweeper.unack(workflowModel, 45000L);

        verify(mockQueueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), eq(45001L));
    }

    @Test
    public void testUnackWaitTask() throws Exception {
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.RUNNING);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("task-1");
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskDefinition(Mockito.mock(TaskDef.class));
        taskModel.setWaitTimeout(120);

        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setTimeoutSeconds(45);

        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(taskModel);
        workflowModel.setTasks(tasks);

        when(mockQueueDAO.setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong())).thenReturn(true);

        workflowSweeper.unack(workflowModel, 45000L);

        verify(mockQueueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), eq(121001L));
    }

    @Test
    public void testUnackHumanTask() throws Exception {
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.RUNNING);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("task-1");
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskDefinition(Mockito.mock(TaskDef.class));
        taskModel.setTaskType(TaskType.TASK_TYPE_HUMAN);

        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setTimeoutSeconds(45);

        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(taskModel);
        workflowModel.setTasks(tasks);

        when(mockQueueDAO.setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong())).thenReturn(true);

        workflowSweeper.unack(workflowModel, 45000L);

        verify(mockQueueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), eq(45001L));
    }

    @Test
    public void testUnackScheduledTask() throws Exception {
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.RUNNING);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("task-1");
        taskModel.setStatus(Status.SCHEDULED);
        taskModel.setTaskDefinition(Mockito.mock(TaskDef.class));

        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setTimeoutSeconds(45);

        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(taskModel);
        workflowModel.setTasks(tasks);

        when(mockQueueDAO.setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong())).thenReturn(true);

        workflowSweeper.unack(workflowModel, 45000L);

        verify(mockQueueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), eq(45001L));
    }

    @Test
    public void testUnackWorkflowDefinitionTimeout() throws Exception {
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.RUNNING);

        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setTimeoutSeconds(45);

        List<TaskModel> tasks = new ArrayList<>();
        workflowModel.setTasks(tasks);
        workflowModel.setWorkflowDefinition(workflowDefinition);

        when(mockQueueDAO.setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong())).thenReturn(true);

        workflowSweeper.unack(workflowModel, 45000L);

        verify(mockQueueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), eq(46001L));
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        long workflowOffsetTimeout = 45;

        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(jitter >= workflowOffsetTimeout - 15 && jitter <= workflowOffsetTimeout + 15);
    }
}