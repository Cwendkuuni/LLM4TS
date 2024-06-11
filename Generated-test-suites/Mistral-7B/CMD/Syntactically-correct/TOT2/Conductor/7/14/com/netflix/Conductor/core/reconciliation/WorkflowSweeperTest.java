package com.netflix.conductor.core.reconciliation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.metrics.Monitors;

@org.junit.runner.RunWith(Org.junit.jupiter.engine.JUnitJupiter.class)
public class WorkflowSweeperTest {

 private static final String WORKFLOW_ID = "testWorkflowId";
 private static final String WORKFLOW_NAME = "testWorkflowName";
 private static final String TASK_DEF_ID = "testTaskDefId";
 private static final String TASK_DEF_NAME = "testTaskDefName";
 private static final String TASK_TYPE = TaskType.TASK_TYPE_EXEC;
 private static final String WAIT_TYPE = TaskType.TASK_TYPE_WAIT;
 private static final String HUMAN_TYPE = TaskType.TASK_TYPE_HUMAN;

 private ConductorProperties properties;
 private QueueDAO queueDAO;
 private WorkflowExecutor workflowExecutor;
 private WorkflowRepairService workflowRepairService;
 private Monitors monitors;
 private WorkflowSweeper sweeper;

 @Before
 public void setup() {
 properties = mock(ConductorProperties.class);
 queueDAO = mock(QueueDAO.class);
 workflowExecutor = mock(WorkflowExecutor.class);
 workflowRepairService = mock(WorkflowRepairService.class);
 monitors = mock(Monitors.class);
 sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
 }

 @Test
 public void testSweepAsync() {
 CompletableFuture<Void> result = sweeper.sweepAsync(WORKFLOW_ID);
 result.join();
 }

 @Test
 public void testSweepNormalWorkflow() throws Exception {
 // Given
 WorkflowModel workflow = new WorkflowModel();
 workflow.setWorkflowId(WORKFLOW_ID);
 workflow.setWorkflowName(WORKFLOW_NAME);
 workflow.setStatus(Status.STARTED);
 workflow.setTasks(Arrays.asList(new TaskModel()));

 when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);
 when(queueDAO.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 0L)).thenReturn(true);

 // When
 sweeper.sweep(WORKFLOW_ID);

 // Then
 assertThat(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID), is(true));
 }

 @Test
 public void testSweepNotFoundException() throws Exception {
 // Given
 when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenThrow(new NotFoundException());

 // When
 try {
 sweeper.sweep(WORKFLOW_ID);
 } catch (Exception e) {
 // Then
 assertThat(e, is(instanceOf(NotFoundException.class)));
 }
 }

 @Test
 public void testSweepException() throws Exception {
 // Given
 WorkflowModel workflow = new WorkflowModel();
 workflow.setWorkflowId(WORKFLOW_ID);
 workflow.setWorkflowName(WORKFLOW_NAME);
 workflow.setStatus(Status.STARTED);
 workflow.setTasks(Arrays.asList(new TaskModel()));
 when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);
 when(queueDAO.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 0L)).thenReturn(true);
 when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new Exception());

 // When and Then
 try {
 sweeper.sweep(WORKFLOW_ID);
 } catch (Exception e) {
 Monitors.error(WorkflowSweeper.class.getSimpleName(), "sweep");
 assertThat(e, is(instanceOf(Exception.class)));
 }
 }

 @Test
 public void testUnackWithNoWorkflow() throws Exception {
 // Given
 when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);
 when(queueDAO.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 0L)).thenReturn(true);

 // When
 sweeper.unack(null, 0L);

 // Then
 assertThat(queueDAO.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 0L), is(true));
 }

 @Test
 public void testUnackWithWorkflow() throws Exception {
 // Given
 WorkflowModel workflow = new WorkflowModel();
 workflow.setWorkflowId(WORKFLOW_ID);
 workflow.setWorkflowName(WORKFLOW_NAME);
 workflow.setStatus(Status.STARTED);
 workflow.setTasks(Arrays.asList(new TaskModel()));
 when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);
 when(queueDAO.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 0L)).thenReturn(true);
 when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);

 // When
 sweeper.unack(workflow, 0L);

 // Then
 assertThat(queueDAO.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 0L), is(true));
 }

 @Test
 public void testWorkflowOffsetWithJitter() throws Exception {
 // Given
 long workflowOffsetTimeout = 45;

 // When
 long workflowOffsetWithJitter = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

 // Then
 assertThat(workflowOffsetWithJitter, is(greaterThanOrEqualTo(30)));
 assertThat(workflowOffsetWithJitter, is(lessThanOrEqualTo(60)));
 }
}