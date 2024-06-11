import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework. beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.queue.SimpleQueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.test.TestWorkflowExecutor;

@ContextConfiguration(classes = AsyncSystemTaskExecutor.class)
public class AsyncSystemTaskExecutorTest {

 @MockBean
 ExecutionDAOFacade executionDAOFacade;

 @MockBean
 QueueDAO queueDAO;

 @MockBean
 MetadataDAO metadataDAO;

 @MockBean
 ConductorProperties conductorProperties;

 @MockBean
 WorkflowExecutor workflowExecutor;

 @Autowired
 AsyncSystemTaskExecutor systemUnderTest;

 private WorkflowSystemTask mockSystemTask;
 private TaskModel mockTask;
 private String mockTaskId;
 private SimpleQueueDAO mockQueueDAO;
 private Monitors mockMonitors;

 @Before
 public void setup() {
 mockSystemTask = Mockito.mock(WorkflowSystemTask.class);
 mockTask = Mockito.mock(TaskModel.class);
 mockTaskId = "mockTaskId";
 mockQueueDAO = Mockito.mock(SimpleQueueDAO.class);
 mockMonitors = Mockito.mock(Monitors.class);

 Mockito.when(executionDAOFacade.getTaskModel(mockTaskId)).thenReturn(mockTask);
 Mockito.when(executionDAOFacade.getWorkflowModel(Mockito.anyString(), Mockito.anyBoolean()))
 .thenReturn(createMockWorkflowModel());

 Mockito.when(queueDAO.remove(Mockito.anyString(), Mockito.eq(mockTaskId)))
 .thenAnswer(invocation -> invocation.getArgument(0).remove(mockTaskId));
 Mockito.when(queueDAO.postpone(Mockito.anyString(), Mockito.eq(mockTaskId), Mockito.anyInt(), Mockito.anyLong()))
 .then(invocation -> invocation.getArgument(0).postpone(mockTaskId, invocation.getArgument(2), invocation.getArgument(3), invocation.getArgument(4)));
 Mockito.when(queueDAO.get(Mockito.anyString()))
 .thenReturn(mockQueueDAO);
 Mockito.when(metadataDAO.getTaskDef(Mockito.anyString())).thenReturn(createMockTaskDef());
 Mockito.when(workflowExecutor.decide(Mockito.anyString())).thenReturn(true);
 Mockito.when(workflowExecutor.getWorkflowExecutor()).thenReturn(new TestWorkflowExecutor());
 Mockito.when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(createMockDuration(1000));
 Mockito.when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(createMockDuration(1000));
 Mockito.when(metadataDAO.getTaskDef(Mockito.anyString())).thenReturn(createMockTaskDef());
 Mockito.when(queueDAO.remove(Mockito.anyString(), Mockito.eq(mockTaskId))).thenReturn(true);
 Mockito.when(queueDAO.postpone(Mockito.anyString(), Mockito.eq(mockTaskId), Mockito.anyInt(), Mockito.anyLong()))
 .thenReturn(true);
 Mockito.when(workflowExecutor.getWorkflowExecutor()).thenReturn(new TestWorkflowExecutor());
 Mockito.when(workflowExecutor.decide(Mockito.anyString())).thenReturn(true);
 Mockito.when(queueDAO.get(Mockito.anyString())).thenReturn(mockQueueDAO);
 Mockito.when(metadataDAO.getWorkflowModel(Mockito.anyString())).thenReturn(createMockWorkflowModel());
 Mockito.when(executionDAOFacade.exceedsRateLimitPerFrequency(Mockito.any(TaskModel.class), Mockito.any())).thenReturn(false);
 Mockito.when(executionDAOFacade.exceedsInProgressLimit(Mockito.any(TaskModel.class))).thenReturn(false);
 Mockito.when(executionDAOFacade.getWorkflowModel(Mockito.anyString(), Mockito.anyBoolean()))
 .thenAnswer(invocation -> createMockWorkflowModel());
 Mockito.when(executionDAOFacade.updateTask(Mockito.any(TaskModel.class))).thenReturn(mockTask);
 Mockito.when(queueDAO.remove(Mockito.anyString(), Mockito.eq(mockTaskId))).thenReturn(true);
 Mockito.when(queueDAO.get(Mockito.anyString())).thenReturn(mockQueueDAO);
 Mockito.when(monitors.recordQueueWaitTime(Mockito.anyString(), Mockito.anyLong())).then(invocation -> {});

 systemUnderTest = new AsyncSystemTaskExecutor(
 executionDAOFacade,
 queueDAO,
 metadataDAO,
 conductorProperties,
 workflowExecutor);
 }

 @Test
 public void testExecute_workflowIsTerminalState() {
 // Given
 WorkflowModel workflow = createMockWorkflowModel();
 workflow.setStatus(WorkflowModel.Status.COMPLETED);

 // When
 systemUnderTest.execute(mockSystemTask, mockTaskId);

 // Then
 Mockito.verify(queueDAO).remove(QueueUtils.getQueueName(mockTask), mockTaskId);
 }

 @Test
 public void testExecute_workflowIsInProgressState() {
 // Given
 WorkflowModel workflow = createMockWorkflowModel();
 workflow.setStatus(WorkflowModel.Status.RUNNING);

 // When
 systemUnderTest.execute(mockSystemTask, mockTaskId);

 // Then
 Mockito.verify(queueDAO).postpone(QueueUtils.getQueueName(mockTask), mockTaskId, 0, 1000L);
 }

 @Test
 public void testExecute_workflowIsScheduledState_concurrentExecutionLimit() {
 // Given
 WorkflowModel workflow = createMockWorkflowModel();
 workflow.setStatus(WorkflowModel.Status.SCHEDULED);

 // When
 systemUnderTest.execute(mockSystemTask, mockTaskId);

 // Then
 Mockito.verify(queueDAO, Mockito.times(2)).postpone(QueueUtils.getQueueName(mockTask), mockTaskId, 0, 1000L);
 }

 @Test
 public void testExecute_workflowIsScheduledState_rateLimitPerFrequency() {
 // Given
 WorkflowModel workflow = createMockWorkflowModel();
 workflow.setStatus(WorkflowModel.Status.SCHEDULED);

 // When
 systemUnderTest.execute(mockSystemTask, mockTaskId);

 // Then
 Mockito.verify(queueDAO).postpone(QueueUtils.getQueueName(mockTask), mockTaskId, 0, 1000L);
 }

 @Test
 public void testExecute_workflowIsScheduledState_neitherConcurrentExecutionLimitNorRateLimitPerFrequency() {
 // Given
 WorkflowModel workflow = createMockWorkflowModel();
 workflow.setStatus(WorkflowModel.Status.SCHEDULED);

 // When
 systemUnderTest.execute(mockSystemTask, mockTaskId);

 // Then
 Mockito.verify(queueDAO, Mockito.never()).postpone(QueueUtils.getQueueName(mockTask), mockTaskId, 0, 1000L);
 }

 @Test
 public void testExecute_workflowIsRunningState() {
 // Given
 WorkflowModel workflow = createMockWorkflowModel();
 workflow.setStatus(WorkflowModel.Status.RUNNING);

 // When
 systemUnderTest.execute(mockSystemTask, mockTaskId);

 // Then
 Mockito.verify(queueDAO).postpone(QueueUtils.getQueueName(mockTask), mockTaskId, 0, 1000L);
 }

 @Test
 public void testExecute_workflowIsTerminalState_workflowHasBeenCompletedBeforeExecution() {
 // Given
 WorkflowModel workflow = createMockWorkflowModel();
 workflow.setStatus(WorkflowModel.Status.COMPLETED);

 // When
 systemUnderTest.execute(mockSystemTask, mockTaskId);

 // Then
 Mockito.verify(queueDAO).remove(QueueUtils.getQueueName(mockTask), mockTaskId);
 }

 @Test
 public void testExecute_taskIsTerminalState() {
 // Given
 TaskModel task = createMockTaskModel();
 task.setStatus(TaskModel.Status.COMPLETED);

 // When
 systemUnderTest.execute(mockSystemTask, mockTaskId);

 // Then
 Mockito.verify(queueDAO).remove(QueueUtils.getQueueName(mockTask), mockTaskId);
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_workflowIsInProgressState_taskIsNotTerminalState() {
 // Given
 WorkflowModel workflow = createMockWorkflowModel();
 workflow.setStatus(WorkflowModel.Status.RUNNING);

 // When
 systemUnderTest.execute(mockSystemTask, mockTaskId);

 // Then
 Mockito.verify(queueDAO).postpone(QueueUtils.getQueueName(mockTask), mockTaskId, 0, 1000L);

 // And Exception
 Assert.assertThrows(
 RuntimeException.class,
 () -> systemUnderTest.execute(mockSystemTask, mockTaskId));
 }

 @Test
 public void testPostponeQuietly_taskIsTerminalState() {
 // Given

 // When
 systemUnderTest.postponeQuietly("queueName", mockTask);

 // Then
 Mockito.verify(queueDAO, Mockito.never()).postpone(Mockito.eq("queueName"), Mockito.eq(mockTaskId), Mockito.anyInt(), Mockito.anyLong());
 }

 @Test
 public void testPostponeQuietly_taskIsNotTerminalState() {
 // Given

 // When
 systemUnderTest.postponeQuietly("queueName", mockTask);

 // Then
 Mockito.verify(queueDAO).postpone(Mockito.eq("queueName"), Mockito.eq(mockTaskId), Mockito.eq(0), Mockito.eq(1000L));
 }

 @Test
 public void testLoadTaskQuietly_taskIsNotPresent() {
 // Given

 // When
 TaskModel result = systemUnderTest.loadTaskQuietly(mockTaskId);

 // Then
 Assert.assertNull(result);
 }

 @Test
 public void testLoadTaskQuietly_taskIsPresent() {
 // Given

 // When
 TaskModel result = systemUnderTest.loadTaskQuietly(mockTaskId);

 // Then
 Assert.assertNotNull(result);
 }
}