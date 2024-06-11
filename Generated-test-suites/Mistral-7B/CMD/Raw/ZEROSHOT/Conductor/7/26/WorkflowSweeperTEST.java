import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;

public class WorkflowSweeperTest {

 private static final String WORKFLOW_ID = UUID.randomUUID().toString();
 private static final String WORKFLOW_NAME = "Test Workflow";
 private static final TaskType WAIT_TASK_TYPE = TaskType.TASK_TYPE_WAIT;
 private static final TaskType HUMAN_TASK_TYPE = TaskType.TASK_TYPE_HUMAN;
 private static final TaskType OTHER_TASK_TYPE = TaskType.TASK_TYPE_OTHER;
 private static final long WORKFLOW_OFFSET_TIMEOUT = 45;
 private static final long JITTER = 10;
 private static final long WORKFLOW_OFFSET_WITH_JITTER = WORKFLOW_OFFSET_TIMEOUT + JITTER;

 @Mock
 private QueueDAO queueDAO;
 @Mock
 private WorkflowExecutor workflowExecutor;
 @Mock
 private WorkflowRepairService workflowRepairService;
 @Mock
 private Monitors monitors;
 @Mock
 private ConductorProperties properties;
 @Mock
 private TaskDef waitTaskDef;
 @Mock
 private TaskDef humanTaskDef;
 @Mock
 private TaskDef otherTaskDef;
 @Mock
 private WorkflowModel workflowModel;
 @Mock
 private TaskModel waitTaskModel;
 @Mock
 private TaskModel humanTaskModel;
 @Mock
 private TaskModel otherTaskModel;

 @InjectMocks
 private WorkflowSweeper workflowSweeper;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(properties.getAppId()).thenReturn("testAppId");
 when(properties.getWorkflowOffsetTimeout()).thenReturn(WORKFLOW_OFFSET_TIMEOUT);
 when(properties.getWorkflowOffsetTimeout().getSeconds()).thenReturn(WORKFLOW_OFFSET_TIMEOUT);
 when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);
 when(queueDAO.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong()))
         .thenReturn(true);
 when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
 when(workflowModel.getWorkflowName()).thenReturn(WORKFLOW_NAME);
 when(workflowModel.getId()).thenReturn(WORKFLOW_ID);
 when(workflowModel.getStatus()).thenReturn(Status.RUNNING);
 when(workflowModel.getTasks()).thenReturn(Arrays.asList(waitTaskModel, humanTaskModel, otherTaskModel));
 when(waitTaskModel.getId()).thenReturn("waitTaskId");
 when(waitTaskModel.getTaskType()).thenReturn(WAIT_TASK_TYPE);
 when(waitTaskModel.getStatus()).thenReturn(Status.IN_PROGRESS);
 when(waitTaskModel.getWaitTimeout()).thenReturn(10L);
 when(humanTaskModel.getId()).thenReturn("humanTaskId");
 when(humanTaskModel.getTaskType()).thenReturn(HUMAN_TASK_TYPE);
 when(humanTaskModel.getStatus()).thenReturn(Status.SCHEDULED);
 when(otherTaskModel.getId()).thenReturn("otherTaskId");
 when(otherTaskModel.getTaskType()).thenReturn(OTHER_TASK_TYPE);
 when(otherTaskModel.getStatus()).thenReturn(Status.FAILED);
 when(taskModel.getTaskDefinition()).thenReturn(taskDef);
 when(waitTaskDef.getPollTimeoutSeconds()).thenReturn(20L);
 when(humanTaskDef.getPollTimeoutSeconds()).thenReturn(null);
 when(workflowModel.getWorkflowDefinition()).thenReturn(null);
 when(workflowRepairService.verifyAndRepairWorkflowTasks(WORKFLOW_ID)).thenReturn(Collections.emptyList());
 when(monitors.recordUnackTime(WORKFLOW_NAME, anyLong())).thenReturn(true);
 }

 @Test
 public void testSweepAsync() {
 // Test the asynchronous method by verifying that the sweep method is called
 CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
 verify(workflowSweeper).sweep(WORKFLOW_ID);
 assertFalse(future.join());
 }

 @Test
 public void testSweep() {
 // Test the sweep method
 workflowSweeper.sweep(WORKFLOW_ID);
 verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);

 if (workflowRepairService != null) {
 // Test that verifyAndRepairWorkflowTasks is called if workflowRepairService is not null
 verify(workflowRepairService).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
 }

 // Test that workflow is removed from decider queue if it is not found
 doThrow(new NotFoundException("")).when(workflowExecutor).decide(WORKFLOW_ID);
 workflowSweeper.sweep(WORKFLOW_ID);
 verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);

 // Test that workflow is unacknowledged with the correct timeout
 verify(queueDAO).setUnackTimeout(
 DECIDER_QUEUE, WORKFLOW_ID, WORKFLOW_OFFSET_WITH_JITTER * 1000);

 // Test that workflow is unacknowledged with the correct postpone duration
 when(workflowModel.getTasks()).thenReturn(Arrays.asList(waitTaskModel));
 workflowSweeper.sweep(WORKFLOW_ID);
 long postponeDurationSeconds = 0;
 if (WAIT_TASK_TYPE.equals(TaskType.TASK_TYPE_WAIT)) {
 postponeDurationSeconds = WAIT_TASK_TYPE.getValue().getWaitTimeout() + 1;
 } else if (WAIT_TASK_TYPE.equals(TaskType.TASK_TYPE_HUMAN)) {
 postponeDurationSeconds = WORKFLOW_OFFSET_TIMEOUT + 1;
 } else {
 postponeDurationSeconds = WORKFLOW_OFFSET_TIMEOUT + 1;
 }
 verify(queueDAO).setUnackTimeout(
 DECIDER_QUEUE, WORKFLOW_ID, postponeDurationSeconds * 1000);
 }

 @Test
 public void testUnack() {
 // Test that unack method sets the unack timeout correctly for tasks with wait timeout
 workflowSweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);
 long waitTaskPostponeDurationSeconds = waitTaskModel.getWaitTimeout() + 1;
 verify(queueDAO)
         .setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, waitTaskPostponeDurationSeconds * 1000);

 // Test that unack method sets the unack timeout correctly for tasks with response timeout
 workflowSweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);
 when(workflowModel.getTasks()).thenReturn(Collections.singletonList(humanTaskModel));
 long humanTaskPostponeDurationSeconds =
                 humanTaskModel.getResponseTimeoutSeconds() + 1;
 verify(queueDAO)
         .setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, humanTaskPostponeDurationSeconds * 1000);

 // Test that unack method sets the unack timeout correctly for tasks with no timeout
 workflowSweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);
 when(workflowModel.getTasks()).thenReturn(Collections.singletonList(otherTaskModel));
 long otherTaskPostponeDurationSeconds = WORKFLOW_OFFSET_TIMEOUT + 1;
 verify(queueDAO)
         .setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, otherTaskPostponeDurationSeconds * 1000);
 }

 @Test
 public void testWorkflowOffsetWithJitter() {
 // Test that workflowOffsetWithJitter returns the correct value
 long actualWorkflowOffsetWithJitter =
         workflowSweeper.workflowOffsetWithJitter(WORKFLOW_OFFSET_TIMEOUT);
 long expectedWorkflowOffsetWithJitter = WORKFLOW_OFFSET_WITH_JITTER;
 assertEquals(expectedWorkflowOffsetWithJitter, actualWorkflowOffsetWithJitter);
 }
}