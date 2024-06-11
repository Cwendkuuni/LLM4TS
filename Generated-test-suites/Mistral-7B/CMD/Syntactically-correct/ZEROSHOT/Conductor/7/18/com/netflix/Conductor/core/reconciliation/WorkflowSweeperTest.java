package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.QueueDAO;
import com.netflix.conductor.core.metrics.Monitors;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAOImpl;

public class WorkflowSweeperTest {

 @Mock
 private ConductorProperties properties;

 @Mock
 private WorkflowExecutor workflowExecutor;

 @Mock
 private QueueDAO queueDAO;

 @Mock
 private Monitors monitors;

 @Mock
 private WorkflowRepairService workflowRepairService;

 @InjectMocks
 private WorkflowSweeper workflowSweeper;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testSweepAsync() {
 // Given
 when(properties.getAppId()).thenReturn("testAppId");
 when(properties.getWorkflowOffsetTimeout()).thenReturn(Duration.ofSeconds(45));
 WorkflowModel workflowModel = getWorkflowModel();
 when(queueDAO.get(DECIDER_QUEUE, anyString())).thenReturn(Optional.ofNullable(workflowModel));
 when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);

 // When
 CompletableFuture<Void> future = workflowSweeper.sweepAsync("testWorkflowId");

 // Then
 assertEquals(new AsyncResult<>(null), future.get());
 }

 @Test
 public void testSweep() {
 // Given
 when(properties.getAppId()).thenReturn("testAppId");
 when(properties.getWorkflowOffsetTimeout()).thenReturn(Duration.ofSeconds(45));
 WorkflowModel workflowModel = getWorkflowModel();
 when(queueDAO.get(DECIDER_QUEUE, anyString())).thenReturn(Optional.ofNullable(workflowModel));
 when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);
 when(workflowRepairService.verifyAndRepairWorkflowTasks(anyString())).thenReturn(true);

 // When
 workflowSweeper.sweep("testWorkflowId");

 // Then
 // Verify and repair tasks in the workflow was called
 verify(workflowRepairService).verifyAndRepairWorkflowTasks("testWorkflowId");
 }

 @Test(expected = NotFoundException.class)
 public void testSweepNotFound() {
 // Given
 when(properties.getAppId()).thenReturn("testAppId");
 when(properties.getWorkflowOffsetTimeout()).thenReturn(Duration.ofSeconds(45));
 when(queueDAO.get(DECIDER_QUEUE, anyString())).thenReturn(Optional.empty());

 // When
 // Then NotFoundException is thrown
 workflowSweeper.sweep("testWorkflowId");
 }

 @Test
 public void testUnack() {
 // Given
 WorkflowModel workflowModel = getWorkflowModel();
 when(queueDAO.get(DECIDER_QUEUE, anyString())).thenReturn(Optional.ofNullable(workflowModel));

 // When
 workflowSweeper.unack(workflowModel, 123L);

 // Then
 // queueDAO.setUnackTimeout was called with the correct timeout
 verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, anyString(), 123000L);
 }

 @Test
 public void testWorkflowOffsetWithJitter() {
 // Given
 long workflowOffsetTimeout = 45;

 // When
 long workflowOffsetWithJitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

 // Then
 // workflowOffsetWithJitter is between [30-60] seconds
 assertTrue(workflowOffsetWithJitter >= (workflowOffsetTimeout - 15));
 assertTrue(workflowOffsetWithJitter <= (workflowOffsetTimeout + 15));
 }

 private WorkflowModel getWorkflowModel() {
 TaskModel taskModel = new TaskModel();
 taskModel.setWorkflowId("testWorkflowId");
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setTasks(Collections.singletonList(taskModel));
 return workflowModel;
 }
}