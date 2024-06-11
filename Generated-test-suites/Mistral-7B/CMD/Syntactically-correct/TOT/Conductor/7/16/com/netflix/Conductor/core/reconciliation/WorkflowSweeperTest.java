package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dao.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModelImpl;
import com.netflix.conductor.model.task.WorkflowTask;
import com.netflix.conductor.model.task.WorkflowTaskImpl;
import com.netflix.conductor.model.taskdefinition.TaskDefinition;
import com.netflix.conductor.model.taskdefinition.TaskDefinitionImpl;
import com.netflix.conductor.metrics.Monitors;

public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
    }

    @Test
    public void testSweepNoWorkflowFound() {
        String workflowId = "workflowId1";
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.empty());

        workflowSweeper.sweep(workflowId);

        // Verify no workflow found, removed from decider queue
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testUnackTasksWithinResponseTimeout() {
        String workflowId = "workflowId1";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.RUNNING);
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.of(workflowModel));

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setTaskId("taskId1");
        taskModel1.setStatus(Status.IN_PROGRESS);
        taskModel1.setResponseTimeoutSeconds(1L);
        taskModel1.setTaskDefinition(new TaskDefinitionImpl());
        workflowModel.setTasks(Arrays.asList(taskModel1));

        WorkflowTask workflowTask = new WorkflowTaskImpl();
        workflowTask.setId("workflowTaskId1");
        workflowTask.setWorkflowId(workflowId);
        workflowTask.setTaskId(taskModel1.getTaskId());

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        when(queueDAO.get(DECIDER_QUEUE, workflowId, TaskModel.class)).thenReturn(Optional.of(taskModel1));
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong()))
                .thenAnswer(invocation -> invocation.getArguments()[2]);

        workflowSweeper.sweep(workflowId);

        long unackTimeout = workflowSweeper.workflowOffsetWithJitter(5L);
        long expectedUnackTimeout = 6L;

        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowId, eq(expectedUnackTimeout * 1000));
    }

    @Test
    public void testUnackTasksWithinWaitTimeout() {
        String workflowId = "workflowId1";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.RUNNING);
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.of(workflowModel));

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setTaskId("taskId1");
        taskModel1.setStatus(Status.SCHEDULED);
        taskModel1.setTaskDefinition(new TaskDefinitionImpl());
        taskModel1.setWaitTimeout(2L);
        workflowModel.setTasks(Arrays.asList(taskModel1));

        WorkflowTask workflowTask = new WorkflowTaskImpl();
        workflowTask.setId("workflowTaskId1");
        workflowTask.setWorkflowId(workflowId);
        workflowTask.setTaskId(taskModel1.getTaskId());

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        when(queueDAO.get(DECIDER_QUEUE, workflowId, TaskModel.class)).thenReturn(Optional.of(taskModel1));
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong()))
                .thenAnswer(invocation -> invocation.getArguments()[2]);

        workflowSweeper.sweep(workflowId);

        long unackTimeout = workflowSweeper.workflowOffsetWithJitter(5L);
        long expectedUnackTimeout = 5L;

        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowId, eq(expectedUnackTimeout * 1000));
    }

    @Test
    public void testUnackTaskWithinWorkflowTimeout() {
        String workflowId = "workflowId1";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.RUNNING);
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.of(workflowModel));

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setTaskId("taskId1");
        taskModel1.setStatus(Status.IN_PROGRESS);
        taskModel1.setResponseTimeoutSeconds(1L);
        taskModel1.setTaskDefinition(new TaskDefinitionImpl());
        workflowModel.setTasks(Arrays.asList(taskModel1));

        WorkflowTask workflowTask = new WorkflowTaskImpl();
        workflowTask.setId("workflowTaskId1");
        workflowTask.setWorkflowId(workflowId);
        workflowTask.setTaskId(taskModel1.getTaskId());

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        when(queueDAO.get(DECIDER_QUEUE, workflowId, TaskModel.class)).thenReturn(Optional.of(taskModel1));
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong()))
                .thenAnswer(invocation -> invocation.getArguments()[2]);

        workflowSweeper.sweep(workflowId);

        long unackTimeout = workflowSweeper.workflowOffsetWithJitter(5L);
        long expectedUnackTimeout = 5L;

        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowId, eq(expectedUnackTimeout * 1000));
    }

    @Test
    public void testUnackTasksWithNoDefinitions() {
        String workflowId = "workflowId1";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.RUNNING);
        workflowModel.setTasks(Collections.emptyList());
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.of(workflowModel));

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());
    }

    @Test
    public void testUnackTasksWithTimeoutSetToZero() {
        String workflowId = "workflowId1";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.RUNNING);
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.of(workflowModel));

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setTaskId("taskId1");
        taskModel1.setStatus(Status.IN_PROGRESS);
        taskModel1.setResponseTimeoutSeconds(1L);
        taskModel1.setTaskDefinition(new TaskDefinitionImpl());
        workflowModel.setTasks(Arrays.asList(taskModel1));

        WorkflowTask workflowTask = new WorkflowTaskImpl();
        workflowTask.setId("workflowTaskId1");
        workflowTask.setWorkflowId(workflowId);
        workflowTask.setTaskId(taskModel1.getTaskId());

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        when(queueDAO.get(DECIDER_QUEUE, workflowId, TaskModel.class)).thenReturn(Optional.of(taskModel1));
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong()))
                .thenAnswer(invocation -> invocation.getArguments()[2]);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowId, eq(TimeUnit.SECONDS.toMillis(1) + 1));
    }

    @Test
    public void testUnackTasksWithWorkflowNotFound() {
        String workflowId = "workflowId1";
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenThrow(NotFoundException.class);

        workflowSweeper.sweep(workflowId);

        // Verify workflow not found, attempt to unack using the id
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());
    }

    @Test
    public void testSweepWithWorkflowRepairService() {
        String workflowId = "workflowId1";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.RUNNING);
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.of(workflowModel));
        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowId)).thenReturn(true);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepNoWorkflowToRemove() {
        String workflowId = "workflowId1";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.RUNNING);
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.of(workflowModel));
        when(workflowModel.getStatus()).thenReturn(Status.TERMINATED);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, never()).remove(DECIDER_QUEUE, workflowId);
    }
}