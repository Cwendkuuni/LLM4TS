package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;

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
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO.QueueConfig;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.QueueUtils;
import com.netflix.conductor.utils.TestUtils;

@SuppressWarnings("unchecked")
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

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 TestUtils.initMocks(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
 }

 @Test
 public void testExecute_taskNotFound() {
 // Given
 WorkflowSystemTask systemTask = new WorkflowSystemTask("testTask", "testTaskId");
 String taskId = "testTaskId";

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Then
 assertEquals("TaskId: testTaskId could not be found while executing {}",
 AsyncSystemTaskExecutor.LOGGER.ERROR, asyncSystemTaskExecutor.getLogger().getErrorMessages());
 assertNull(asyncSystemTaskExecutor.getTaskModel(taskId));
 }

 @Test
 public void testExecute_taskInProgress() {
 // Given
 WorkflowModel workflowModel = TestUtils.createWorkflowModel();
 TaskModel taskModel = TestUtils.createTaskModel(workflowModel);
 WorkflowSystemTask systemTask = new WorkflowSystemTask("testTask", "testTaskId");

 given(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
 .willReturn(workflowModel);

 given(executionDAOFacade.getTaskModel(taskId)).willReturn(taskModel);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Then
 assertEquals(
 "Workflow testWorkflowName has been completed for testSystemTask,testTaskId",
 AsyncSystemTaskExecutor.LOGGER.INFO,
 asyncSystemTaskExecutor.getLogger().getInfoMessages());
 assertNull(taskModel);

 // Cleanup
 queueDAO.remove("testQueue", "testTaskId");
 }

 @Test(expected = Exception.class)
 public void testExecute_concurrencyLimitExceeded() {
 // Given
 WorkflowModel workflowModel = TestUtils.createWorkflowModel();
 TaskModel taskModel1 = TestUtils.createTaskModel(workflowModel);
 TaskModel taskModel2 = TestUtils.createTaskModel(workflowModel);
 WorkflowSystemTask systemTask = new WorkflowSystemTask("testTask", "testTaskId1");

 given(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
 .willReturn(workflowModel);

 given(executionDAOFacade.getTaskModel(taskModel1.getId()))
 .willReturn(taskModel1);

 given(executionDAOFacade.getTaskModel(taskModel2.getId()))
 .willReturn(taskModel2);

 given(executionDAOFacade.exceedsInProgressLimit(taskModel1))
 .willReturn(true);

 // When, Then
 asyncSystemTaskExecutor.execute(systemTask, taskModel1.getId());
 assertFalse(executionDAOFacade.getTaskModel(taskModel2.getId()).isCompleted());

 asyncSystemTaskExecutor.execute(systemTask, taskModel2.getId());
 assertTrue(executionDAOFacade.getTaskModel(taskModel2.getId()).isCompleted());
 }

 @Test
 public void testExecute_rateLimitPerFrequencyExceeded() {
 // Given
 WorkflowModel workflowModel = TestUtils.createWorkflowModel();
 TaskModel taskModel = TestUtils.createTaskModel(workflowModel);
 WorkflowSystemTask systemTask = new WorkflowSystemTask("testTask", "testTaskId");
 QueueConfig queueConfig = new QueueConfig();
 queueConfig.setLimit(1);
 given(metadataDAO.getTaskDef("testTaskDefName"))
 .willReturn(TestUtils.createTaskDef());
 given(conductorProperties.getTaskExecutionPostponeDuration())
 .willReturn(TestUtils.createDuration(10000));
 given(queueDAO.getQueueConfig(anyString()))
 .willReturn(queueConfig);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskModel.getId());
 assertFalse(taskModel.isCompleted());

 asyncSystemTaskExecutor.execute(systemTask, taskModel.getId());
 assertFalse(taskModel.isCompleted());
 }

 @Test
 public void testExecute_workflowCompleted() {
 // Given
 WorkflowModel workflowModel = TestUtils.createWorkflowModel();
 workflowModel.setStatus(WorkflowModel.Status.COMPLETED);
 TaskModel taskModel = TestUtils.createTaskModel(workflowModel);
 WorkflowSystemTask systemTask = new WorkflowSystemTask("testTask", "testTaskId");

 given(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
 .willReturn(workflowModel);

 given(executionDAOFacade.getTaskModel(taskModel.getId()))
 .willReturn(taskModel);

 // When
 asyncSystemTaskExecutor.execute(systemTask, taskModel.getId());

 // Then
 assertEquals(
 "Workflow testWorkflowName has been completed for testSystemTask,testTaskId",
 AsyncSystemTaskExecutor.LOGGER.INFO,
 asyncSystemTaskExecutor.getLogger().getInfoMessages());
 assertFalse(taskModel.isCompleted());
 }

 @Test
 public void testPostponeQuietly() {
 // Given
 String queueName = "testQueue";
 TaskModel taskModel = TestUtils.createTaskModel();

 // When
 asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

 // Then
 assertEquals(queueName, queueDAO.getQueueName(taskModel));
 }

 @Test
 public void testLoadTaskQuietly_nullTaskModel() {
 // Given
 String taskId = "testTaskId";

 // When
 TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

 // Then
 assertNull(taskModel);
 }

 @Test
 public void testLoadTaskQuietly_taskModel() {
 // Given
 String taskId = "testTaskId";
 TaskModel taskModel = TestUtils.createTaskModel();
 given(executionDAOFacade.getTaskModel(taskId))
 .willReturn(taskModel);

 // When
 TaskModel loadedTaskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

 // Then
 assertEquals(taskModel, loadedTaskModel);
 }
}