import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowModel;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

 @Mock
 private QueueDAO queueDAO;

 @Mock
 private WorkflowExecutor workflowExecutor;

 @Mock
 private ConductorProperties properties;

 @Mock
 private WorkflowRepairService workflowRepairService;

 @InjectMocks
 private WorkflowSweeper sweeperUnderTest;

 @Autowired
 private Random random;

 @Test
 public void testSweepAsync() {
 WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
 CompletableFuture<Void> future = sweeperUnderTest.sweepAsync("workflowId");
 Mockito.verify(queueDAO).remove(DECIDER_QUEUE, "workflowId");
 Mockito.verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "workflowId", 0L);
 assertEquals(CompletableFuture.completedFuture(null), future);
 }

 @Test
 public void testSweep() {
 WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
 String workflowId = "workflowId";
 try {
 sweeperUnderTest.sweep(workflowId);
 } catch (NotFoundException e) {
 // Expected
 }

 Mockito.verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
 assertNull(workflowModel);
 }

 @Test
 public void testSweepWithWorkflow() {
 WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
 workflowModel.setWorkflowId("workflowId");
 workflowModel.setStatus(Status.RUNNING);
 workflowModel.setWorkflowName("workflowName");

 TaskModel taskModel = Mockito.mock(TaskModel.class);
 taskModel.setTaskDefinition(Mockito.mock(TaskDef.class));
 taskModel.setStatus(Status.IN_PROGRESS);
 workflowModel.addTaskModel(taskModel);

 when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(workflowModel);

 sweeperUnderTest.sweep(workflowId);

 Mockito.verify(queueDAO, Mockito.times(2)).setUnackTimeout(
 DECIDER_QUEUE, workflowId, anyLong());
 }

 @Test
 public void testSweepWithNotFoundException() {
 WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
 when(queueDAO.get(DECIDER_QUEUE, "workflowId")).thenReturn(null);

 sweeperUnderTest.sweep("workflowId");

 Mockito.verify(queueDAO).remove(DECIDER_QUEUE, "workflowId");
 Mockito.verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "workflowId", 0L);
 }

 @Test
 public void testUnack() {
 WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
 workflowModel.setWorkflowId("workflowId");
 workflowModel.setWorkflowName("workflowName");

 TaskModel taskModel = Mockito.mock(TaskModel.class);
 taskModel.setTaskDefinition(Mockito.mock(TaskDef.class));
 taskModel.setStatus(Status.IN_PROGRESS);
 workflowModel.addTaskModel(taskModel);

 when(queueDAO.get(DECIDER_QUEUE, "workflowId")).thenReturn(workflowModel);

 sweeperUnderTest.unack(workflowModel, 1L);

 Mockito.verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "workflowId", 2001L);
 }

 @Test
 public void testUnackWithNullWorkflowModel() {
 sweeperUnderTest.unack(null, 1L);

 Mockito.verify(queueDAO, Mockito.never()).setUnackTimeout(
 DECIDER_QUEUE, anyString(), anyLong());
 }

 @Test
 public void testUnackWithNonInProgressTask() {
 WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
 TaskModel taskModel = Mockito.mock(TaskModel.class);
 taskModel.setStatus(Status.FAILED);
 workflowModel.addTaskModel(taskModel);

 when(queueDAO.get(DECIDER_QUEUE, "workflowId")).thenReturn(workflowModel);

 sweeperUnderTest.unack(workflowModel, 1L);

 Mockito.verify(queueDAO, Mockito.never()).setUnackTimeout(
 DECIDER_QUEUE, "workflowId", anyLong());
 }

 @Test
 public void testWorkflowOffsetWithJitter() {
 long workflowOffsetTimeout = 45L;
 long workflowOffsetTimeoutWithJitter = sweeperUnderTest.workflowOffsetWithJitter(workflowOffsetTimeout);

 long expectedMin = workflowOffsetTimeout - (workflowOffsetTimeout / 3);
 long expectedMax = workflowOffsetTimeout + (workflowOffsetTimeout / 3);

 assertEquals(expectedMin, workflowOffsetTimeoutWithJitter);
 assertEquals(expectedMax, workflowOffsetTimeoutWithJitter);
 }

 @Test
 public void testWorkflowOffsetWithJitterWithNegativeValue() {
 long workflowOffsetTimeout = -45L;
 long workflowOffsetTimeoutWithJitter = sweeperUnderTest.workflowOffsetWithJitter(workflowOffsetTimeout);

 assertEquals(workflowOffsetTimeout, workflowOffsetTimeoutWithJitter);
 }
}