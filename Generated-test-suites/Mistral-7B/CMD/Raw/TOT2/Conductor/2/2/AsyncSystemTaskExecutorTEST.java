package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

 @Mock
 private ExecutionDAOFacade executionDAOFacade;

 @Mock
 private QueueDAO queueDAO;

 @Mock
 private MetadataDAO metadataDAO;

 @Mock
 private WorkflowExecutor workflowExecutor;

 @Mock
 private ConductorProperties conductorProperties;

 @InjectMocks
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 @Mock
 private WorkflowSystemTask systemTask;

 @Mock
 private TaskModel task;

 @Mock
 private WorkflowModel workflow;

 @Mock
 private QueueUtils queueUtils;

 @Mock
 private Monitors monitors;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(queueDAO.getQueueName(task)).thenReturn("testQueue");
 when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
 when(executionDAOFacade.getWorkflowModel(workflowId, systemTask.isTaskRetrievalRequired()))
 .thenReturn(workflow);
 when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new Object());
 doReturn(monitors).when(asyncSystemTaskExecutor).getMonitors();
 }

 @Test
 public void testExecute_taskDoesNotExist() {
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 verify(queueDAO, times(1)).remove("testQueue", taskId);
 }

 @Test
 public void testExecute_workflowIsTerminal() {
 givenWorkflowIsTerminal();
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 verify(queueDAO, times(1)).remove("testQueue", taskId);
 }

 @Test
 public void testExecute_taskIsScheduled() {
 givenTaskIsScheduled();
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 verify(queueDAO, times(1)).postpone("testQueue", taskId, 1, queueTaskMessagePostponeSecs);
 }

 @Test
 public void testExecute_taskIsInProgress() {
 givenTaskIsInProgress();
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 verify(systemTask, times(1)).execute(workflow, task, workflowExecutor);
 }

 @Test(expected = Exception.class)
 public void testExecute_exceedsInProgressLimit() {
 givenExceedsInProgressLimit();
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 verify(queueDAO, times(1)).remove("testQueue", taskId);
 }

 @Test(expected = Exception.class)
 public void testExecute_exceedsRateLimitPerFrequency() {
 givenExceedsRateLimitPerFrequency();
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 verify(queueDAO, times(1)).remove("testQueue", taskId);
 }

 @Test
 public void testExecute_taskIsCompleted() {
 givenTaskIsCompleted();
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 verify(queueDAO, times(1)).remove("testQueue", taskId);
 verify(workflowExecutor, times(1)).decide(workflowId);
 }

 private void givenTaskIsScheduled() {
 givenTaskIsInitialState();
 when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
 }

 private void givenTaskIsInProgress() {
 givenTaskIsInitialState();
 when(task.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);
 }

 private void givenTaskIsCompleted() {
 givenTaskIsInitialState();
 when(task.getStatus()).thenReturn(ExecutionStatus.COMPLETED);
 }

 private void givenTaskIsInitialState() {
 givenTaskIsNotTerminal();
 }

 private void givenTaskIsNotTerminal() {
 givenTaskIsNotNull();
 }

 private void givenTaskIsNotNull() {
 task = new TaskModel();
 }

 private void givenWorkflowIsTerminal() {
 givenWorkflowIsInitialState();
 when(workflow.getStatus()).thenReturn(ExecutionStatus.TERMINAL);
 }

 private void givenWorkflowIsInitialState() {
 workflow = new WorkflowModel();
 }

 private void givenExceedsInProgressLimit() {
 givenTaskIsInitialState();
 when(executionDAOFacade.exceedsInProgressLimit(task))
 .thenReturn(true);
 }

 private void givenExceedsRateLimitPerFrequency() {
 givenTaskIsInitialState();
 when(executionDAOFacade.exceedsRateLimitPerFrequency(task, new Object()))
 .thenReturn(true);
 }
}