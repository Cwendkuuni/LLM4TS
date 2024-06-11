package com.netflix.conductor.core.execution;

 import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

 import java.time.Duration;
import java.util.Optional;

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
 import com.netflix.conductor.dao.Queue;
 import com.netflix.conductor.metrics.Monitors;
 import com.netflix.conductor.testutils.TestUtils;
 import com.netflix.conductor.testutils.mocks.TestExecutionDAOFacade;
 import com.netflix.conductor.testutils.mocks.TestMetadataDAO;
 import com.netflix.conductor.testutils.mocks.TestQueueDAO;
 import com.netflix.conductor.testutils.mocks.TestWorkflowExecutor;
 import com.netflix.conductor.testutils.models.TestTaskModel;
 import com.netflix.conductor.testutils.models.TestWorkflowModel;

 @SuppressWarnings("unchecked")
 public class AsyncSystemTaskExecutorTest {

 @Mock private ExecutionDAOFacade executionDAOFacade;
 @Mock private QueueDAO queueDAO;
 @Mock private MetadataDAO metadataDAO;
 @Mock private WorkflowExecutor workflowExecutor;
 @Mock private Monitors monitors;
 @Mock private ConductorProperties conductorProperties;

 @InjectMocks private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 TestUtils.initMocks(this);

 when(executionDAOFacade.getTaskModel(eq("taskId"))).thenReturn(new TestTaskModel());
 when(metadataDAO.getTaskDef(eq("taskDefName"))).thenReturn(new TestWorkflowModel());
 when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
 when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong())).thenReturn(true);
 when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
 when(queueDAO.getQueue(anyString())).thenReturn(new TestQueue());
 when(workflowExecutor.decide(anyString())).thenReturn(true);
 when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(new TestWorkflowModel());
 when(executionDAOFacade.updateTask(any())).thenReturn(true);
 when(executionDAOFacade.exceedsInProgressLimit(any())).thenReturn(false);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any())).thenReturn(false);
 }

 @Test
 public void testExecute_taskNotFound_queueMessageCleanedUp() {
 // Given
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 String taskId = "testTaskId";

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Then
 verify(queueDAO, times(1)).remove(eq(QueueUtils.getQueueName(new TestTaskModel())), eq(taskId));
 }

 @Test
 public void testExecute_taskAlreadyCompleted_queueMessageRemoved() {
 // Given
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 String taskId = "testTaskId";
 TestTaskModel task = new TestTaskModel();
 task.setStatus(TaskModel.Status.COMPLETED);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Then
 verify(queueDAO, times(1)).remove(eq(QueueUtils.getQueueName(new TestTaskModel())), eq(taskId));
 }

 @Test(expected = IllegalStateException.class)
 public void testExecute_exceedsInProgressLimit() {
 // Given
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 String taskId = "testTaskId";
 TestTaskModel task = new TestTaskModel();
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

 // When & Then
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
 verify(queueDAO, never()).remove(anyString(), anyString());
 }

 @Test(expected = IllegalStateException.class)
 public void testExecute_exceedsRateLimitPerFrequency() {
 // Given
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 String taskId = "testTaskId";
 TestTaskModel task = new TestTaskModel();
 TestWorkflowModel workflow = new TestWorkflowModel();
 workflow.setRateLimitPerFrequency(1);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(workflow);

 // When & Then
 asyncSystemTaskExecutor.execute(systemTask, taskId);
 verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
 verify(queueDAO, never()).remove(anyString(), anyString());
 }

 @Test
 public void testExecute_taskInProgress_queueMessagePostponed() {
 // Given
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 String taskId = "testTaskId";
 TestTaskModel task = new TestTaskModel();
 task.setStatus(TaskModel.Status.SCHEDULED);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 when(workflowExecutor.decide(anyString())).thenReturn(false);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Then
 verify(queueDAO, times(1)).postpone(
 eq(QueueUtils.getQueueName(new TestTaskModel())),
 eq(taskId),
 anyInt(),
 eq(queueTaskMessagePostponeSecs));
 }

 @Test
 public void testExecute_taskCompletedOrTerminated_queueMessageRemoved() {
 // Given
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 String taskId = "testTaskId";
 TestTaskModel task = new TestTaskModel();
 task.setStatus(TaskModel.Status.COMPLETED);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Then
 verify(queueDAO, times(1)).remove(eq(QueueUtils.getQueueName(new TestTaskModel())), eq(taskId));
 }

 @Test
 public void testExecute_taskInProgress_workflowDecided() {
 // Given
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 String taskId = "testTaskId";
 TestTaskModel task = new TestTaskModel();
 task.setStatus(TaskModel.Status.SCHEDULED);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 when(workflowExecutor.decide(anyString())).thenReturn(true);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Then
 verify(workflowExecutor, times(1)).decide(anyString());
 }

 @Test
 public void testPostponeQuietly_queueMessagePostponed() {
 // Given
 String queueName = "testQueueName";
 String taskId = "testTaskId";
 TestQueue queue = mock(Queue.class);
 when(queueDAO.getQueue(queueName)).thenReturn(queue);

 // When
 asyncSystemTaskExecutor.postponeQuietly(queueName, new TestTaskModel());

 // Then
 verify(queue, times(1)).postpone(eq(taskId), anyInt(), eq(queueTaskMessagePostponeSecs));
 }

 @Test
 public void testLoadTaskQuietly_taskReturned() {
 // Given
 String taskId = "testTaskId";
 TestTaskModel task = new TestTaskModel();
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

 // When
 TaskModel returnedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

 // Then
 assertEquals(task, returnedTask);
 }

 @Test
 public void testLoadTaskQuietly_taskNotFound_nullReturned() {
 // Given
 String taskId = "testTaskId";

 // When
 TaskModel returnedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

 // Then
 assertNull(returnedTask);
 }
 }