import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueueMessage;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.TestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
public class AsyncSystemTaskExecutorTest {

 @MockBean
 ExecutionDAOFacade executionDAOFacade;
 @MockBean
 QueueDAO queueDAO;
 @MockBean
 MetadataDAO metadataDAO;
 @MockBean
 Monitors monitors;
 @MockBean
 WorkflowExecutor workflowExecutor;
 @Autowired
 AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 private final WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
 private final TaskModel task = TestUtils.createTaskModel();
 private final String taskId = "taskId";
 private final String workflowId = "workflowId";
 private final String queueName = QueueUtils.getQueueName(task);
 private final QueueMessage queueMessage = new QueueMessage(taskId, queueName);

 @Before
 public void setup() {
 ConductorProperties conductorProperties = new ConductorProperties();
 conductorProperties.setSystemTaskWorkerCallbackDuration(
         Mockito.mock(Duration.class));
 conductorProperties.setTaskExecutionPostponeDuration(
         Mockito.mock(Duration.class));
 Mockito.when(executionDAOFacade.getWorkflowModel(
         ArgumentMatchers.eq(workflowId), ArgumentMatchers.eq(true)))
         .thenReturn(TestUtils.createWorkflowModel());
 Mockito.when(executionDAOFacade.getTaskModel(ArgumentMatchers.eq(taskId)))
         .thenReturn(task);
 Mockito.when(executionDAOFacade.exceedsInProgressLimit(task))
         .thenReturn(false);
 Mockito.when(executionDAOFacade.exceedsRateLimitPerFrequency(
         ArgumentMatchers.eq(task), ArgumentMatchers.any(WorkflowModel.class)))
         .thenReturn(false);
 Mockito.when(queueDAO.remove(queueName, taskId))
         .thenReturn(true);
 Mockito.when(queueDAO.postpone(queueName, taskId, ArgumentMatchers.anyInt(),
         ArgumentMatchers.anyInt()))
         .thenReturn(true);
 Mockito.when(queueDAO.remove(queueName, taskId))
         .thenReturn(true);
 Mockito.when(queueDAO.getNextMessage(queueName)).thenReturn(queueMessage);
 Mockito.when(queueDAO.getNextMessage(queueName)).thenReturn(null);
 Mockito.when(queueDAO.postpone(queueName, taskId, ArgumentMatchers.anyInt(),
         ArgumentMatchers.anyInt()))
         .thenThrow(new RuntimeException());
 Mockito.when(workflowExecutor.decide(ArgumentMatchers.eq(workflowId)))
         .thenReturn(ExecutionStatus.COMPLETED);
 Mockito.when(workflowExecutor.decide(ArgumentMatchers.eq(workflowId)))
         .thenThrow(new RuntimeException());
 Mockito.when(metadataDAO.getTaskDef(task.getTaskDefName()))
         .thenReturn(TestUtils.createTaskDefinition());
 Mockito.when(monitors.recordQueueWaitTime(ArgumentMatchers.eq(task.getTaskType()),
         ArgumentMatchers.anyLong()))
         .thenReturn(0L);
 }

 @Test
 public void testExecute_whenTaskIsNotFound_shouldReturn() {
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 verify(queueDAO).remove(queueName, taskId);
 assertNull(task);
 }

 @Test
 public void testExecute_whenTaskIsAlreadyCompleted_shouldReturn() {
 task.setStatus(TaskModel.Status.COMPLETED);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 verify(queueDAO).remove(queueName, taskId);
 assertSame(task, executionDAOFacade.getTaskModel(taskId));
 }

 @Test
 public void testExecute_whenTaskIsExecuting_shouldPostpone() {
 task.setStatus(TaskModel.Status.SCHEDULED);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 verify(queueDAO).postpone(queueName, taskId, ArgumentMatchers.anyInt(),
         ArgumentMatchers.anyInt());
 }

 @Test
 public void testExecute_whenTaskIsExecutedSuccessfully_shouldDecideWorkflow() {
 task.setStatus(TaskModel.Status.SCHEDULED);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 verify(workflowExecutor).decide(workflowId);
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_whenWorkflowDecisionFails_shouldThrowException() {
 task.setStatus(TaskModel.Status.SCHEDULED);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 verify(workflowExecutor).decide(workflowId);
 verify(workflowExecutor).decide(workflowId);
 }

 @Test
 public void testExecute_whenTaskIsExecutedAsync_shouldCallback() {
 task.setStatus(TaskModel.Status.SCHEDULED);
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 verify(queueDAO)
         .postpone(queueName, taskId, ArgumentMatchers.anyInt(),
                 ArgumentMatchers.anyInt());
 }

 @Test
 public void testExecute_whenTaskExecutionFails_shouldNotDecideWorkflow() {
 task.setStatus(TaskModel.Status.SCHEDULED);
 Mockito.when(workflowExecutor.decide(workflowId))
         .thenThrow(new RuntimeException());
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 verify(workflowExecutor, never())
         .decide(workflowId);
 }

 @Test
 public void testPostponeQuietly_whenQueueMessageExists_shouldPostpone() {
 asyncSystemTaskExecutor.postponeQuietly(queueName, task);
 verify(queueDAO).postpone(queueName, taskId, ArgumentMatchers.anyInt(),
         ArgumentMatchers.anyInt());
 }

 @Test(expected = RuntimeException.class)
 public void testPostponeQuietly_whenQueueMessageDoesNotExist_shouldThrowException() {
 asyncSystemTaskExecutor.postponeQuietly(queueName, task);
 verify(queueDAO, never())
         .postpone(queueName, taskId, ArgumentMatchers.anyInt(),
                 ArgumentMatchers.anyInt());
 }

 @Test
 public void testLoadTaskQuietly_whenTaskExists_shouldReturnTask() {
 TaskModel taskFromDB = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
 assertSame(task, taskFromDB);
 }

 @Test
 public void testLoadTaskQuietly_whenTaskDoesNotExist_shouldReturnNull() {
 TaskModel taskFromDB = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
 assertNull(taskFromDB);
 }
}