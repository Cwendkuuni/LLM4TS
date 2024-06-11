import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.NoOpWorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.execution.ExecutionDAOFacadeImpl;
import com.netflix.conductor.dao.metadata.MetadataDAOImpl;
import com.netflix.conductor.dao.model.db.Execution;
import com.netflix.conductor.dao.model.db.QueueMessage;
import com.netflix.conductor.dao.model.db.Task;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.utils.json.JacksonMapper;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;

@SpringBootTest
public class AsyncSystemTaskExecutorTest {

 @Autowired
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 @MockBean
 private ExecutionDAOFacade executionDAOFacade;

 @MockBean
 private QueueDAO queueDAO;

 @MockBean
 private MetadataDAO metadataDAO;

 @MockBean
 private WorkflowExecutor workflowExecutor;

 @MockBean
 private ConductorProperties conductorProperties;

 @Before
 public void setup() {
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setId("workflow-1");
 workflowModel.setName("workflow-1");
 workflowModel.setDefinition(new byte[0]);
 workflowModel.setPriority(1);
 workflowModel.setStatus(WorkflowInstanceStatus.RUNNING);

 Mockito.when(executionDAOFacade.getWorkflowModel(Mockito.anyString(), Mockito.eq(true)))
 .thenReturn(workflowModel);

 Mockito.when(queueDAO.getQueueMessage(Mockito.anyString(), Mockito.anyString()))
 .thenReturn(Optional.empty());

 Mockito.when(queueDAO.postpone(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
 .thenReturn(true);

 Mockito.when(executionDAOFacade.getTaskModel(Mockito.anyString()))
 .thenReturn(new TaskModel());

 Mockito.when(metadataDAO.getTaskDef(Mockito.anyString()))
 .thenReturn(new TaskModel());

 Mockito.when(workflowExecutor.decide(Mockito.anyString()))
 .thenReturn(Collections.emptyList());
 }

 @Test
 public void testExecuteScheduledTask() {
 // Given
 TaskModel taskModel = new TaskModel();
 taskModel.setId("task-1");
 taskModel.setTaskDefName("some-task-def");
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 taskModel.setTaskType("some-task-type");
 taskModel.setWorkflowInstanceId("workflow-1");

 NoOpWorkflowSystemTask systemTask = new NoOpWorkflowSystemTask();
 systemTask.setTaskType("some-task-type");
 systemTask.setTaskRetrievalRequired(true);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskModel.getId());

 // Then
 verify(executionDAOFacade).getWorkflowModel(taskModel.getWorkflowInstanceId(), true);
 verify(queueDAO).postpone(QueueUtils.getQueueName(taskModel), taskModel.getId(), 1, queueTaskMessagePostponeSecs);
 }

 @Test
 public void testExecuteInProgressTask() {
 // Given
 TaskModel taskModel = new TaskModel();
 taskModel.setId("task-1");
 taskModel.setTaskDefName("some-task-def");
 taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
 taskModel.setTaskType("some-task-type");
 taskModel.setWorkflowInstanceId("workflow-1");

 NoOpWorkflowSystemTask systemTask = new NoOpWorkflowSystemTask();
 systemTask.setTaskType("some-task-type");
 systemTask.setTaskRetrievalRequired(true);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskModel.getId());

 // Then
 // No verification as we don't want to test the implementation of systemTask.execute
 }

 @Test
 public void testExecuteCompletedTask() {
 // Given
 TaskModel taskModel = new TaskModel();
 taskModel.setId("task-1");
 taskModel.setTaskDefName("some-task-def");
 taskModel.setStatus(TaskModel.Status.COMPLETED);
 taskModel.setTaskType("some-task-type");
 taskModel.setWorkflowInstanceId("workflow-1");

 NoOpWorkflowSystemTask systemTask = new NoOpWorkflowSystemTask();
 systemTask.setTaskType("some-task-type");
 systemTask.setTaskRetrievalRequired(true);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskModel.getId());

 // Then
 // No verification as we don't want to test the implementation of systemTask.execute
 }

 @Test(expected = NullPointerException.class)
 public void testExecuteNonExistentTask() {
 // Given
 String taskId = UUID.randomUUID().toString();
 NoOpWorkflowSystemTask systemTask = new NoOpWorkflowSystemTask();
 systemTask.setTaskType("some-task-type");
 systemTask.setTaskRetrievalRequired(true);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Then
 // No verification as we don't want to test the implementation of systemTask.execute
 // Expected to throw a NullPointerException as taskModel is null
 }

 @Test
 public void testExecuteTaskWithExceededInProgressLimit() {
 // Given
 TaskModel taskModel = new TaskModel();
 taskModel.setId("task-1");
 taskModel.setTaskDefName("some-task-def");
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 taskModel.setTaskType("some-task-type");
 taskModel.setWorkflowInstanceId("workflow-1");
 taskModel.setInProgressLimit(1);

 NoOpWorkflowSystemTask systemTask = new NoOpWorkflowSystemTask();
 systemTask.setTaskType("some-task-type");
 systemTask.setTaskRetrievalRequired(true);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskModel.getId());

 // Then
 // Expected to postpone the task as inProgressLimit is exceeded
 verify(queueDAO).postpone(QueueUtils.getQueueName(taskModel), taskModel.getId(), 1, queueTaskMessagePostponeSecs);
 }

 @Test
 public void testExecuteTaskWithExceededRateLimitPerFrequency() {
 // Given
 TaskModel taskModel = new TaskModel();
 taskModel.setId("task-1");
 taskModel.setTaskDefName("some-task-def");
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 taskModel.setTaskType("some-task-type");
 taskModel.setWorkflowInstanceId("workflow-1");
 taskModel.setRateLimitPerFrequency(1);

 NoOpWorkflowSystemTask systemTask = new NoOpWorkflowSystemTask();
 systemTask.setTaskType("some-task-type");
 systemTask.setTaskRetrievalRequired(true);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskModel.getId());

 // Then
 // Expected to postpone the task as rateLimitPerFrequency is exceeded
 verify(queueDAO).postpone(QueueUtils.getQueueName(taskModel), taskModel.getId(), 1, queueTaskMessagePostponeSecs);
 }

 @Test
 public void testExecuteTaskWithTerminalWorkflow() {
 // Given
 TaskModel taskModel = new TaskModel();
 taskModel.setId("task-1");
 taskModel.setTaskDefName("some-task-def");
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 taskModel.setTaskType("some-task-type");
 taskModel.setWorkflowInstanceId("workflow-1");

 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setId("workflow-1");
 workflowModel.setName("workflow-1");
 workflowModel.setDefinition(new byte[0]);
 workflowModel.setPriority(1);
 workflowModel.setStatus(WorkflowInstanceStatus.COMPLETED);

 Mockito.when(executionDAOFacade.getWorkflowModel(workflowModel.getId(), true))
 .thenReturn(workflowModel);

 NoOpWorkflowSystemTask systemTask = new NoOpWorkflowSystemTask();
 systemTask.setTaskType("some-task-type");
 systemTask.setTaskRetrievalRequired(true);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskModel.getId());

 // Then
 // Expected to remove the task from queue as the workflow is completed
 verify(queueDAO).remove(QueueUtils.getQueueName(taskModel), taskModel.getId());
 }

 @Test
 public void testExecuteTaskWithTerminalTask() {
 // Given
 TaskModel taskModel = new TaskModel();
 taskModel.setId("task-1");
 taskModel.setTaskDefName("some-task-def");
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 taskModel.setTaskType("some-task-type");
 taskModel.setWorkflowInstanceId("workflow-1");

 Task task = new Task();
 task.setId(taskModel.getId());
 task.setTaskDefName(taskModel.getTaskDefName());
 task.setTaskType(taskModel.getTaskType());
 task.setStatus(ExecutionStatus.COMPLETED);
 task.setEndTime(System.currentTimeMillis());
 task.setWorkflowInstanceId(taskModel.getWorkflowInstanceId());

 Execution execution = new Execution();
 execution.setId(UUID.randomUUID().toString());
 execution.setTask(task);
 execution.setExecutionTime(System.currentTimeMillis());
 execution.setWorkflowInstanceId(taskModel.getWorkflowInstanceId());
 execution.setStatus(ExecutionStatus.COMPLETED);
 execution.setEndTime(System.currentTimeMillis());

 Mockito.when(executionDAOFacade.getTaskModel(taskModel.getId()))
 .thenReturn(taskModel);

 Mockito.when(executionDAOFacade.getTask(taskModel.getId()))
 .thenReturn(task);

 Mockito.when(executionDAOFacade.getExecution(execution.getId()))
 .thenReturn(execution);

 NoOpWorkflowSystemTask systemTask = new NoOpWorkflowSystemTask();
 systemTask.setTaskType("some-task-type");
 systemTask.setTaskRetrievalRequired(true);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskModel.getId());

 // Then
 // Expected to remove the task from queue as the task is completed
 verify(queueDAO).remove(QueueUtils.getQueueName(taskModel), taskModel.getId());

 // Expected to evaluate the workflow as the task is completed
 verify(workflowExecutor).decide(taskModel.getWorkflowInstanceId());
 }

 @Test
 public void testExecuteTaskWithException() {
 // Given
 TaskModel taskModel = new TaskModel();
 taskModel.setId("task-1");
 taskModel.setTaskDefName("some-task-def");
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 taskModel.setTaskType("some-task-type");
 taskModel.setWorkflowInstanceId("workflow-1");

 NoOpWorkflowSystemTask systemTask = new NoOpWorkflowSystemTask();
 systemTask.setTaskType("some-task-type");
 systemTask.setTaskRetrievalRequired(true);

 // When
 // Expected to throw an exception as systemTask.execute will throw an exception
 try {
 asyncSystemTaskExecutor.execute(systemTask, taskModel.getId());
 } catch (Exception e) {
 // Then
 // Expected to remove the task from queue as an exception was thrown
 verify(queueDAO).remove(QueueUtils.getQueueName(taskModel), taskModel.getId());
 }
 }
}