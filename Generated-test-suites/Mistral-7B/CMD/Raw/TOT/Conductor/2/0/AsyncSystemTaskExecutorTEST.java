package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@org.junit.runner.RunWith(MockitoJUnitRunner.class)
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

 @Rule
 public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 given(executionDAOFacade.getTaskModel("taskId")).willReturn(new TaskModel());
 given(queueDAO.getQueueName("taskId")).willReturn("queueName");
 }

 @Test
 public void testExecute_taskFoundAndCompleted() {
 // given
 WorkflowSystemTask systemTask = new WorkflowSystemTask() {
 @Override
 public void start(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {}

 @Override
 public void execute(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {}

 @Override
 public boolean isAsyncComplete(TaskModel task) {
 return true;
 }
 };
 String taskId = "taskId";
 String queueName = "queueName";
 TaskModel task = new TaskModel();
 WorkflowModel workflow = new WorkflowModel();

 given(executionDAOFacade.getWorkflowModel("workflowId", false)).willReturn(workflow);
 given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);
 given(queueDAO.remove(queueName, taskId)).willReturn(true);
 given(workflowExecutor.decide("workflowId")).willReturn(true);

 // when
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // then
 verify(executionDAOFacade).getTaskModel(taskId);
 verify(queueDAO).remove(queueName, taskId);
 verify(workflowExecutor).decide("workflowId");
 }

 @Test
 public void testExecute_taskNotFound() {
 // given
 given(executionDAOFacade.getTaskModel("taskId")).willReturn(null);

 // when
 asyncSystemTaskExecutor.execute(new WorkflowSystemTask(), "taskId");

 // then
 verify(queueDAO, never()).remove("queueName", "taskId");
 }

 @Test
 public void testExecute_taskInProgress() {
 // given
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.IN_PROGRESS);
 given(executionDAOFacade.getTaskModel("taskId")).willReturn(task);
 given(queueDAO.remove(queueName, taskId)).willReturn(false);

 // when
 asyncSystemTaskExecutor.execute(new WorkflowSystemTask(), "taskId");

 // then
 verify(queueDAO).postpone(queueName, taskId, 0, queueTaskMessagePostponeSecs);
 }

 @Test
 public void testExecute_taskScheduled() {
 // given
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.SCHEDULED);
 given(executionDAOFacade.getTaskModel("taskId")).willReturn(task);
 given(queueDAO.postpone(queueName, taskId, 0, queueTaskMessagePostponeSecs)).willReturn(true);
 given(workflowExecutor.decide("workflowId")).willReturn(false);

 // when
 asyncSystemTaskExecutor.execute(new WorkflowSystemTask(), "taskId");

 // then
 verify(queueDAO).postpone(queueName, taskId, 0, queueTaskMessagePostponeSecs);
 verify(workflowExecutor, never()).decide("workflowId");
 }

 @Test
 public void testExecute_taskError() {
 // given
 given(executionDAOFacade.getTaskModel("taskId")).willThrow(new RuntimeException());

 // when // assertThat(asyncSystemTaskExecutor.execute(systemTask, "taskId")).isNotNull(); // Uncomment this line if you want to test the returned value

 // then
 verify(executionDAOFacade).getTaskModel("taskId");
 verify(queueDAO, never()).remove("queueName", "taskId");
 verify(workflowExecutor, never()).decide("workflowId");
 }
}