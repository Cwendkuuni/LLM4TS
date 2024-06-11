import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModel;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import com.netflix.conductor.utils.QueueUtils;
import com.netflix.conductor.utils.TestUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@PowerMockRunner(delegate = PowerMockRunnerDelegate.TYPE_SUPPRESS_FAILURES)
public class AsyncSystemTaskExecutorTest {

 private AsyncSystemTaskExecutor executor;
 private ExecutionDAOFacade executionDAOFacadeMock;
 private QueueDAO queueDAOMock;
 private MetadataDAO metadataDAOMock;
 private ConductorProperties conductorPropertiesMock;
 private WorkflowExecutor workflowExecutorMock;
 private QueueDAO queueDAO;
 private ExecutionDAOFacade executionDAO;
 private MetadataDAO metadataDAO;
 private String queueName;
 private String taskId;
 private String workflowId;
 private TaskModel task;
 private WorkflowModel workflow;

 @Before
 public void setup() {
 executionDAOFacadeMock = Mockito.mock(ExecutionDAOFacade.class);
 queueDAOMock = new QueueDAOImpl();
 metadataDAOMock = Mockito.mock(MetadataDAO.class);
 conductorPropertiesMock = Mockito.mock(ConductorProperties.class);
 workflowExecutorMock = Mockito.mock(WorkflowExecutor.class);

 queueDAO = PowerMockito.mock(QueueDAO.class);
 executionDAO = PowerMockito.mock(ExecutionDAOFacade.class);
 metadataDAO = PowerMockito.mock(MetadataDAO.class);

 PowerMockito.whenNew(ExecutionDAOFacade.class).withArguments(executionDAO, metadataDAO).thenReturn(executionDAOFacadeMock);
 PowerMockito.whenNew(QueueDAO.class).withArguments(queueDAO).thenReturn(queueDAOMock);
 PowerMockito.whenNew(MetadataDAO.class).withArguments(metadataDAO).thenReturn(metadataDAOMock);

 executor = new AsyncSystemTaskExecutor(
         executionDAOFacadeMock,
         queueDAO,
         metadataDAO,
         conductorPropertiesMock,
         workflowExecutorMock);

 task = TestUtils.createTaskModel();
 workflow = TestUtils.createWorkflowModel();
 workflowId = UUID.randomUUID().toString();
 taskId = UUID.randomUUID().toString();
 queueName = QueueUtils.getQueueName(task);
 }

 @Test
 public void testExecute_TaskNotFound() {
 // given
 PowerMockito.when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(null);

 // when
 executor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

 // then
 PowerMockito.verifyStatic();
 queueDAOMock.remove(queueName, taskId);
 }

 @Test
 public void testExecute_WorkflowNotFound() {
 // given
 PowerMockito.when(executionDAOFacadeMock.getWorkflowModel(workflowId, false)).thenReturn(null);
 PowerMockito.when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);

 // when
 executor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

 // then
 PowerMockito.verifyStatic();
 queueDAOMock.remove(queueName, taskId);
 }

 @Test
 public void testExecute_TerminalWorkflow() {
 // given
 PowerMockito.when(executionDAOFacadeMock.getWorkflowModel(workflowId, false)).thenReturn(workflow);
 PowerMockito.when(workflow.getStatus()).thenReturn(WorkflowInstanceStatus.COMPLETED);
 PowerMockito.when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);

 // when
 executor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

 // then
 PowerMockito.verifyStatic();
 queueDAOMock.remove(queueName, taskId);
 }

 @Test
 public void testExecute_ExceedsExecutionLimit() {
 // given
 PowerMockito.when(executionDAOFacadeMock.getWorkflowModel(workflowId, false)).thenReturn(workflow);
 PowerMockito.when(workflow.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
 PowerMockito.when(executionDAOFacadeMock.exceedsInProgressLimit(task)).thenReturn(true);
 PowerMockito.when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);

 // when
 executor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

 // then
 PowerMockito.verifyStatic();
 queueDAOMock.postpone(queueName, taskId, task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
 }

 @Test
 public void testExecute_ExceedsRateLimitPerFrequency() {
 // given
 PowerMockito.when(executionDAOFacadeMock.getWorkflowModel(workflowId, false)).thenReturn(workflow);
 PowerMockito.when(workflow.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
 PowerMockito.when(executionDAOFacadeMock.exceedsRateLimitPerFrequency(task, null)).thenReturn(true);
 PowerMockito.when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);

 // when
 executor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

 // then
 PowerMockito.verifyStatic();
 queueDAOMock.postpone(queueName, taskId, task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
 }

 @Test
 public void testExecute_TaskInProgress() {
 // given
 PowerMockito.when(executionDAOFacadeMock.getWorkflowModel(workflowId, false)).thenReturn(workflow);
 PowerMockito.when(workflow.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
 PowerMockito.when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);

 // when
 executor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

 // then
 PowerMockito.verifyStatic();
 queueDAOMock.postpone(queueName, taskId, task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
 }

 @Test
 public void testExecute_TaskComplete() {
 // given
 PowerMockito.when(executionDAOFacadeMock.getWorkflowModel(workflowId, false)).thenReturn(workflow);
 PowerMockito.when(workflow.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
 PowerMockito.when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 PowerMockito.when(systemTaskCallbackTime).thenReturn(Duration.ofSeconds(10).getSeconds());
 PowerMockito.when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
 PowerMockito.when(task.getEndTime()).thenReturn(Instant.now().minus(Duration.ofMinutes(30)).toInstant());
 PowerMockito.when(workflowExecutorMock.decide(workflowId)).thenReturn(Optional.empty());
 PowerMockito.when(queueDAO.get(queueName, taskId)).thenReturn(Optional.of(task));
 PowerMockito.when(queueDAO.postpone(queueName, taskId, task.getWorkflowPriority(), queueTaskMessagePostponeSecs))
         .thenReturn(true);
 PowerMockito.when(queueDAO.remove(queueName, taskId)).thenReturn(true);
 PowerMockito.when(systemTaskCallbackTime).thenReturn(Duration.ofSeconds(10).getSeconds());

 // when
 executor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

 // then
 PowerMockito.verifyStatic();
 task.setStatus(TaskModel.Status.COMPLETED);
 PowerMockito.verifyStatic();
 queueDAOMock.remove(queueName, taskId);
 PowerMockito.verifyStatic();
 workflowExecutorMock.decide(workflowId);
 }

 @Test
 public void testPostponeQuietly() {
 // given
 PowerMockito.when(queueDAO.postpone(queueName, taskId, task.getWorkflowPriority(), queueTaskMessagePostponeSecs))
         .thenReturn(true);

 // when
 executor.postponeQuietly(queueName, task);

 // then
 PowerMockito.verifyStatic();
 queueDAOMock.postpone(queueName, taskId, task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
 }

 @Test
 public void testLoadTaskQuietly_TaskNotFound() {
 // given
 PowerMockito.when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(null);

 // when
 TaskModel task = executor.loadTaskQuietly(taskId);

 // then
 Assert.assertNull(task);
 }
}