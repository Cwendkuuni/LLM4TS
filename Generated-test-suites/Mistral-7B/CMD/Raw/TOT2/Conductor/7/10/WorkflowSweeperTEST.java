package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.exception.NotFoundException;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metrics.Monitors;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.test.ConductorTestUtils;
import com.netflix.conductor.test.TaskTestUtils;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

@Component
public class WorkflowSweeper {

 // Existing code here

}

public class WorkflowSweeperTest {

 @Mock WorkflowExecutor workflowExecutor;
 @Mock QueueDAO queueDAO;
 @Mock ConductorProperties properties;
 @Mock Monitors monitors;
 @Mock Random random;

 @Autowired private WorkflowSweeper workflowSweeper;

 @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 when(workflowExecutor.decide(anyString())).thenReturn(new WorkflowModel());
 when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
 when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong())).thenReturn(true);
 }

 @After
 public void tearDown() {
 verifyNoMoreInteractions(workflowExecutor);
 verifyNoMoreInteractions(queueDAO);
 }

 @Test
 public void testSweepAsync_WorkflowFound() throws Exception {
 WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
 String workflowId = "test-workflow-id";
 CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
 assertTrue(result.get(1, TimeUnit.SECONDS).isCompletedNormal());
 }

 @Test(expected = NotFoundException.class)
 public void testSweepAsync_WorkflowNotFound() throws Exception {
 String workflowId = "test-non-existent-workflow-id";
 CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
 assertTrue(result.join().isCompletedExceptionally());
 verify(queueDAO).remove(eq(DECIDER_QUEUE), eq(workflowId));
 }

 @Test
 public void testSweep_WorkflowFound() throws Exception {
 String workflowId = "test-workflow-id";
 WorkflowModel workflow = new WorkflowModel();
 when(workflowExecutor.decide(workflowId)).thenReturn(workflow);
 workflowSweeper.sweep(workflowId);
 verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong());
 }

 @Test(expected = NotFoundException.class)
 public void testSweep_WorkflowNotFound() throws Exception {
 String workflowId = "test-non-existent-workflow-id";
 workflowSweeper.sweep(workflowId);
 verify(queueDAO).remove(eq(DECIDER_QUEUE), eq(workflowId));
 }

 @Test
 public void testUnack() throws Exception {
 WorkflowModel workflowModel = new WorkflowModel();
 List<TaskModel> tasks = List.of(TaskTestUtils.createTaskModel("task1"), TaskTestUtils.createTaskModel("task2"));
 workflowModel.setTasks(tasks);
 ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
 when(queueDAO.setUnackTimeout(anyString(), eq(workflowIdCaptor.capture()), anyLong())).thenReturn(true);
 workflowSweeper.unack(workflowModel, 1000L);
 assertEquals(workflowId, workflowIdCaptor.getValue());
 }

 @Test
 public void testWorkflowOffsetWithJitter_PositiveValue() {
 long workflowOffsetTimeout = 45L;
 long expectedMin = 30L;
 long expectedMax = 60L;
 long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
 assertTrue(result >= expectedMin);
 assertTrue(result <= expectedMax);
 }

 @Test
 public void testWorkflowOffsetWithJitter_ZeroValue() {
 long workflowOffsetTimeout = 0L;
 long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
 assertEquals(0L, result);
 }
}