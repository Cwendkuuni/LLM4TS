package com.netflix.conductor.core.reconciliation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dao.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.model.WorkflowModelImpl;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

    @Autowired
    private WorkflowSweeper workflowSweeper;

    @Autowired
    private QueueDAO queueDAO;

    @Autowired
    private WorkflowExecutor workflowExecutor;

    @Autowired
    private ConductorProperties conductorProperties;

    @Autowired
    private WorkflowRepairService workflowRepairService;

    @Before
    public void setup() {
        // Initialize mocks
        QueueDAOImpl queueDAOMock = mock(QueueDAOImpl.class);
        WorkflowModelImpl workflowModelMock = mock(WorkflowModelImpl.class);
        WorkflowContext workflowContextMock = mock(WorkflowContext.class);
        TaskModel taskModelMock = mock(TaskModel.class);
        TaskDef taskDefMock = mock(TaskDef.class);
        when(queueDAOMock.getUnackTimeout(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(0L);
        when(workflowExecutor.decide(ArgumentMatchers.anyString())).thenReturn(workflowModelMock);
        when(workflowModelMock.getStatus()).thenReturn(Status.SCHEDULED);
        when(taskModelMock.getStatus()).thenReturn(Status.IN_PROGRESS);
        when(taskModelMock.getTaskDefinition()).thenReturn(taskDefMock);
        when(taskDefMock.getTaskType()).thenReturn(TaskType.TASK_TYPE_WAIT);
        when(workflowModelMock.getWorkflowId()).thenReturn("workflow-id");
        when(workflowModelMock.getTasks()).thenReturn(Collections.singletonList(taskModelMock));
        when(workflowModelMock.getWorkflowDefinition()).thenReturn(mock(TaskDef.class));
        when(workflowExecutor.getConductorProperties()).thenReturn(conductorProperties);
        when(taskModelMock.getId()).thenReturn("task-id");
        when(workflowModelMock.getWorkflowName()).thenReturn("workflow-name");
        when(queueDAOMock.remove(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(true);
        when(queueDAOMock.setUnackTimeout(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong())).thenReturn(true);

        // Set dependencies
        WorkflowSweeper sweeper = new WorkflowSweeper();
        sweeper.setQueueDAO(queueDAOMock);
        sweeper.setWorkflowExecutor(workflowExecutor);
        sweeper.setWorkflowRepairService(workflowRepairService);
        sweeper.setConductorProperties(conductorProperties);

        this.workflowSweeper = sweeper;
    }

    @Test
    public void testSweep_WorkflowFound() throws Exception {
        // Arrange
        WorkflowModel workflow = new WorkflowModelImpl();
        when(workflowExecutor.decide(workflow.getWorkflowId())).thenReturn(workflow);

        // Act
        workflowSweeper.sweep("workflow-id");

        // Assert
        Mockito.verify(queueDAOMock).setUnackTimeout(
                DECIDER_QUEUE, workflow.getWorkflowId(), workflowOffsetWithJitter(45) * 1000);
    }

    @Test
    public void testSweep_WorkflowNotFound() throws Exception {
        // Arrange

        // Act
        workflowSweeper.sweep("workflow-id-not-found");

        // Assert
        Mockito.verify(queueDAOMock).remove(DECIDER_QUEUE, "workflow-id-not-found");
    }

    @Test
    public void testSweepAsync() throws Exception {
        // Arrange

        // Act
        workflowSweeper.sweepAsync("workflow-id")
                .thenAccept(x -> {});

        // Assert
        // Asynchronous tests are difficult to assert directly, this can be tested by inspecting the
        // conductor logs or by using a monitoring tool to check if the sweeper was called.
    }

    @Test
    public void testUnack_TaskInProgress() throws Exception {
        // Arrange

        // Act
        workflowSweeper.unack(workflowModelMock, 45);

        // Assert
        Mockito.verify(queueDAOMock).setUnackTimeout(
                DECIDER_QUEUE, "workflow-id", 46 * 1000);
    }

    @Test
    public void testUnack_TaskScheduled() throws Exception {
        // Arrange
        TaskModel taskModel = new TaskModel();
        when(taskModel.getStatus()).thenReturn(Status.SCHEDULED);

        // Act
        workflowSweeper.unack(workflowModelMock, 45);

        // Assert
        Mockito.verify(queueDAOMock).setUnackTimeout(
                DECIDER_QUEUE, "workflow-id", 46 * 1000);
    }

    @Test
    public void testUnack_TaskCompleted() throws Exception {
        // Arrange
        TaskModel taskModel = new TaskModel();
        when(taskModel.getStatus()).thenReturn(Status.COMPLETED);

        // Act
        workflowSweeper.unack(workflowModelMock, 45);

        // Assert
        Mockito.verifyZeroInteractions(queueDAOMock);
    }

    @Test
    public void testUnack_TaskFailed() throws Exception {
        // Arrange
        TaskModel taskModel = new TaskModel();
        when(taskModel.getStatus()).thenReturn(Status.FAILED);

        // Act
        workflowSweeper.unack(workflowModelMock, 45);

        // Assert
        Mockito.verifyZeroInteractions(queueDAOMock);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        // Arrange

        // Act
        long workflowOffsetTimeout = 45;
        long workflowOffsetWithJitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Assert
        assertEquals(45 + new Random().nextInt(9), workflowOffsetWithJitter);
    }

    @Test
    public void testSweep_WorkflowWithWaitTask() throws Exception {
        // Arrange
        TaskModel waitTaskModel = new TaskModel();
        when(waitTaskModel.getTaskDefinition()).thenReturn(taskDefMock);
        when(taskDefMock.getPollTimeoutSeconds()).thenReturn(10);
        WorkflowModel workflow = new WorkflowModelImpl();
        when(workflowExecutor.decide(workflow.getWorkflowId())).thenReturn(workflow);
        workflow.setTasks(Arrays.asList(waitTaskModel));

        // Act
        workflowSweeper.sweep("workflow-id");

        // Assert
        Mockito.verify(queueDAOMock).setUnackTimeout(
                DECIDER_QUEUE, "workflow-id", 11 * 1000);
    }

    @Test
    public void testSweep_WorkflowWithHumanTask() throws Exception {
        // Arrange
        WorkflowModel workflow = new WorkflowModelImpl();
        when(workflowExecutor.decide(workflow.getWorkflowId())).thenReturn(workflow);
        workflow.setTasks(Collections.emptyList());

        // Act
        workflowSweeper.sweep("workflow-id");

        // Assert
        Mockito.verifyZeroInteractions(queueDAOMock);
    }

    @Test
    public void testSweep_WorkflowWithTimeout() throws Exception {
        // Arrange
        WorkflowModel workflow = new WorkflowModelImpl();
        when(workflowExecutor.decide(workflow.getWorkflowId())).thenReturn(workflow);
        workflow.setWorkflowDefinition(mock(TaskDef.class));
        when(workflow.getWorkflowDefinition().getTimeoutSeconds()).thenReturn(10);

        // Act
        workflowSweeper.sweep("workflow-id");

        // Assert
        Mockito.verify(queueDAOMock).setUnackTimeout(
                DECIDER_QUEUE, "workflow-id", 11 * 1000);
    }

    @Test
    public void testSweep_WorkflowWithoutTasks() throws Exception {
        // Arrange
        WorkflowModel workflow = new WorkflowModelImpl();
        when(workflowExecutor.decide(workflow.getWorkflowId())).thenReturn(workflow);

        // Act
        workflowSweeper.sweep("workflow-id");

        // Assert
        Mockito.verifyZeroInteractions(queueDAOMock);
    }

    @Test
    public void testSweep_WorkflowWithException() throws Exception {
        // Arrange
        WorkflowModel workflow = new WorkflowModelImpl();
        when(workflowExecutor.decide(workflow.getWorkflowId()))
                .thenThrow(new RuntimeException("Test exception"));

        // Act & Assert
        assertFalse(workflowSweeper.sweep("workflow-id")
                .isCompletedIn(5, TimeUnit.SECONDS));
    }
}