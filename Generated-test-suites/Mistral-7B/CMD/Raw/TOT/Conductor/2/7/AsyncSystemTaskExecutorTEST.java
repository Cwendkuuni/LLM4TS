package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueueMessage;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class AsyncSystemTaskExecutorTest {

 @Mock
 private ExecutionDAOFacade executionDAOFacade;

 @Mock
 private QueueDAO queueDAO;

 @Mock
 private MetadataDAO metadataDAO;

 @Mock
 private ConductorProperties conductorProperties;

 @Mock
 private WorkflowExecutor workflowExecutor;

 @InjectMocks
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 @Mock
 private WorkflowSystemTask systemTask;

 @Mock
 private TaskModel task;

 @Mock
 private WorkflowModel workflow;

 @Mock
 private QueueMessage queueMessage;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecute_taskNotFound() {
 // given
 String taskId = "taskId";
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

 // when
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // then
 verify(queueDAO, never()).remove(anyString(), anyString());
 }

 @Test
 public void testExecute_taskCompleted() {
 // given
 givenTaskWithWorkflowAndStatus(TaskModel.Status.COMPLETED);

 // when
 asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

 // then
 verify(queueDAO).remove(anyString(), task.getTaskId());
 }

 @Test
 public void testExecute_taskScheduled() {
 // given
 givenTaskWithWorkflowAndStatus(TaskModel.Status.SCHEDULED);

 // when
 asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

 // then
 verify(queueDAO).postpone(
 anyString(),
 eq(task.getTaskId()),
 anyInt(),
 eq(queueTaskMessagePostponeSecs));
 }

 @Test
 public void testExecute_taskInProgress() {
 // given
 givenTaskWithWorkflowAndStatus(TaskModel.Status.IN_PROGRESS);

 // when
 asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

 // then
 verify(systemTask).execute(workflow, task, workflowExecutor);
 }

 @Test
 public void testExecute_taskTerminal() {
 // given
 givenTaskWithWorkflowAndStatus(TaskModel.Status.FAILED);

 // when
 asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

 // then
 verify(queueDAO).remove(anyString(), task.getTaskId());
 }

 @Test
 public void testExecute_taskExceedsConcurrentLimit() {
 // given
 givenTaskWithWorkflowAndStatus(TaskModel.Status.SCHEDULED);
 when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

 // when
 asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

 // then
 verify(queueDAO).postpone(
 anyString(),
 eq(task.getTaskId()),
 anyInt(),
 eq(queueTaskMessagePostponeSecs));
 }

 @Test
 public void testExecute_taskExceedsRateLimitPerFrequency() {
 // given
 givenTaskWithWorkflowAndStatus(TaskModel.Status.SCHEDULED);
 givenWorkflowModelWithRateLimitPerFrequency(5);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

 // when
 asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

 // then
 verify(queueDAO).postpone(
 anyString(),
 eq(task.getTaskId()),
 anyInt(),
 eq(queueTaskMessagePostponeSecs));
 }

 private void givenTaskWithWorkflowAndStatus(TaskModel.Status status) {
 givenTaskWithWorkflow();
 task.setStatus(status);
 }

 private void givenTaskWithWorkflow() {
 givenWorkflowModel();
 task = new TaskModel();
 task.setTaskId(task.getTaskId());
 task.setTaskDefName("testTaskDefName");
 task.setWorkflowInstanceId("testWorkflowInstanceId");
 task.setTaskDefinition(new Object());
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setWorkflow(workflow);
 }

 private void givenWorkflowModel() {
 workflow = new WorkflowModel();
 workflow.setStatus(WorkflowInstance.Status.RUNNING);
 workflow.setId("testWorkflowInstanceId");
 }

 private void givenWorkflowModelWithRateLimitPerFrequency(int rateLimit) {
 workflow.setRateLimitPerFrequency(rateLimit);
 }
}