import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.utils.ExecutionUtils;

public class AsyncSystemTaskExecutorTest {

 private ExecutionDAOFacade executionDAOFacade;
 private QueueDAO queueDAO;
 private MetadataDAO metadataDAO;
 private AnnotationConfigApplicationContext context;
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
 private String queueName;
 private String workflowId;
 private TaskModel task;
 private WorkflowModel workflow;
 private WorkflowSystemTask workflowSystemTask;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 context = new AnnotationConfigApplicationContext();
 context.register(ConductorProperties.class);
 ConductorProperties conductorProperties = context.getBean(ConductorProperties.class);
 executionDAOFacade = Mockito.spy(context.getBean(ExecutionDAOFacade.class));
 queueDAO = Mockito.spy(new QueueDAOImpl());
 metadataDAO = Mockito.mock(MetadataDAO.class);
 asyncSystemTaskExecutor =
         new AsyncSystemTaskExecutor(
                 executionDAOFacade, queueDAO, metadataDAO, context.getBean(ConductorProperties.class),
                 Mockito.mock(WorkflowExecutor.class));
 queueName = QueueUtils.getQueueName("testQueue");
 workflowId = "testWorkflowId";
 workflow = new WorkflowModel();
 workflow.setId(workflowId);
 workflow.setName("Test Workflow");
 workflow.setStatus(ExecutionStatus.RUNNING);
 task = new TaskModel();
 task.setId("testTaskId");
 task.setTaskDefName("TestTaskDef");
 task.setStatus(ExecutionStatus.SCHEDULED);
 task.setWorkflowInstanceId(workflowId);
 workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
 }

 @Test
 public void testExecuteWithNullTask() {
 asyncSystemTaskExecutor.execute(workflowSystemTask, null);
 Mockito.verify(queueDAO, Mockito.times(0)).remove(queueName, ArgumentMatchers.anyString());
 }

 @Test
 public void testExecuteWithNullWorkflow() {
 task.setWorkflowInstanceId(null);
 asyncSystemTaskExecutor.execute(workflowSystemTask, task.getId());
 Mockito.verify(queueDAO, Mockito.times(0)).remove(queueName, ArgumentMatchers.anyString());
 }

 @Test
 public void testExecuteWithTerminalTask() {
 task.setStatus(ExecutionStatus.COMPLETED);
 asyncSystemTaskExecutor.execute(workflowSystemTask, task.getId());
 Mockito.verify(queueDAO, Mockito.times(1)).remove(queueName, task.getId());
 }

 @Test
 public void testExecuteWithExceededInProgressLimit() {
 workflow.setConcurrent(1);
 task.setStatus(ExecutionStatus.SCHEDULED);
 Mockito.when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
 asyncSystemTaskExecutor.execute(workflowSystemTask, task.getId());
 Mockito.verify(queueDAO, Mockito.times(1)).postpone(
         queueName, task.getId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
 }

 @Test
 public void testExecuteWithExceededRateLimitPerFrequency() {
 workflow.setConcurrent(1);
 workflow.setRateLimitPerFrequency(1);
 task.setStatus(ExecutionStatus.SCHEDULED);
 Mockito.when(executionDAOFacade.exceedsRateLimitPerFrequency(task, metadataDAO)).thenReturn(true);
 asyncSystemTaskExecutor.execute(workflowSystemTask, task.getId());
 Mockito.verify(queueDAO, Mockito.times(1)).postpone(
         queueName, task.getId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
 }

 @Test
 public void testExecuteWithCompletedWorkflow() {
 workflow.setStatus(ExecutionStatus.COMPLETED);
 asyncSystemTaskExecutor.execute(workflowSystemTask, task.getId());
 Mockito.verify(queueDAO, Mockito.times(1)).remove(queueName, task.getId());
 }

 @Test
 public void testExecuteWithWorkflowExecutionRequired() {
 workflow.setStatus(ExecutionStatus.RUNNING);
 Mockito.when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflow);
 asyncSystemTaskExecutor.execute(workflowSystemTask, task.getId());
 // Verify that workflowExecutor.decide() is called
 Mockito.verify(asyncSystemTaskExecutor.workflowExecutor).decide(workflowId);
 }

 @Test
 public void testExecuteWithTaskExecutionError() {
 task.setStatus(ExecutionStatus.SCHEDULED);
 String errorMessage = "Test Error Message";
 Mockito.when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenThrow(new RuntimeException(errorMessage));
 asyncSystemTaskExecutor.execute(workflowSystemTask, task.getId());
 // Verify that queueDAO.remove() is called
 Mockito.verify(queueDAO, Mockito.times(1)).remove(queueName, task.getId());
 // Verify that Monitors.error() is called
 Mockito.verify(asyncSystemTaskExecutor, Mockito.times(1)).monitorError(errorMessage);
 }

 @Test
 public void testExecuteWithSuccess() {
 task.setStatus(ExecutionStatus.SCHEDULED);
 workflow.setStatus(ExecutionStatus.RUNNING);
 Mockito.when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflow);
 Mockito.when(workflowSystemTask.start(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
         .thenReturn(ExecutionUtils.createExecutionResult("Test Result"));
 asyncSystemTaskExecutor.execute(workflowSystemTask, task.getId());
 // Verify that workflowExecutor.decide() is called
 Mockito.verify(asyncSystemTaskExecutor.workflowExecutor).decide(workflowId);
 // Verify that queueDAO.remove() is called
 Mockito.verify(queueDAO, Mockito.times(1)).remove(queueName, task.getId());
 }

 @Test
 public void testExecuteWithAsyncCompleteTask() {
 task.setStatus(ExecutionStatus.SCHEDULED);
 workflow.setStatus(ExecutionStatus.RUNNING);
 Mockito.when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflow);
 Mockito.when(workflowSystemTask.isAsyncComplete(task)).thenReturn(true);
 asyncSystemTaskExecutor.execute(workflowSystemTask, task.getId());
 // Verify that workflowExecutor.decide() is called
 Mockito.verify(asyncSystemTaskExecutor.workflowExecutor, Mockito.times(1)).decide(workflowId);
 // Verify that queueDAO.remove() is called
 Mockito.verify(queueDAO, Mockito.times(1)).remove(queueName, task.getId());
 }
}