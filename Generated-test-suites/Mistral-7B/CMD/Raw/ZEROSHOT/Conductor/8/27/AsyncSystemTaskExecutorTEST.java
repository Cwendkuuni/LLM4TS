import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.workflow.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueNameGenerator;
import com.netflix.conductor.utils.QueueUtils;
import com.netflix.conductor.metrics.Monitors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

public class AsyncSystemTaskExecutorTest {

 private AsyncSystemTaskExecutor systemUnderTest;
 private ExecutionDAOFacade executionDAOFacade;
 private QueueDAO queueDAO;
 private MetadataDAO metadataDAO;
 private WorkflowExecutor workflowExecutor;
 private ConductorProperties conductorProperties;
 private Logger logger;
 private String taskId;
 private String workflowId;
 private TaskModel task;
 private WorkflowModel workflow;
 private WorkflowSystemTask systemTask;
 private QueueNameGenerator queueNameGenerator;
 private Monitors monitors;

 @Before
 public void setUp() throws Exception {
 executionDAOFacade = mock(ExecutionDAOFacade.class);
 queueDAO = mock(QueueDAO.class);
 metadataDAO = mock(MetadataDAO.class);
 workflowExecutor = mock(WorkflowExecutor.class);
 conductorProperties = mock(ConductorProperties.class);
 logger = Mockito.mock(Logger.class);
 queueNameGenerator = mock(QueueNameGenerator.class);
 monitors = mock(Monitors.class);

 systemUnderTest =
         new AsyncSystemTaskExecutor(
                 executionDAOFacade,
                 queueDAO,
                 metadataDAO,
                 conductorProperties,
                 workflowExecutor);

 task = createTaskModel("TaskType", "TaskId", workflowId);
 workflow = createWorkflowModel("WorkflowId");
 systemTask = createMockWorkflowSystemTask();

 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
 when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
 when(queueDAO.postpone(
            QueueUtils.getQueueName(task),
            task.getTaskId(),
            task.getWorkflowPriority(),
            Long.parseLong("100"))).thenReturn(true);
 when(queueDAO.remove(QueueUtils.getQueueName(task), task.getTaskId())).thenReturn(true);
 when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new TaskDefinition());
 when(workflowExecutor.decide(workflowId)).thenReturn(true);
 when(logger.isDebugEnabled()).thenReturn(true);
 when(logger.isInfoEnabled()).thenReturn(true);
 when(logger.isErrorEnabled()).thenReturn(true);
 when(logger.isWarnEnabled()).thenReturn(true);
 }

 @Test
 public void execute_TaskIsAlreadyCompleted_RemovesTaskFromQueueAndDecidesWorkflow()
         throws InterruptedException {
 systemUnderTest.execute(systemTask, taskId);

 verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
 verify(workflowExecutor).decide(workflowId);
 }

 @Test
 public void execute_TaskIsTerminal_RemovesTaskFromQueue() throws Exception {
 task.setStatus(TaskModel.Status.COMPLETED);
 systemUnderTest.execute(systemTask, taskId);

 verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
 }

 @Test(expected = IllegalStateException.class)
 public void execute_TaskStatusIsTerminal_ThrowsException()
         throws InterruptedException, Exception {
 task.setStatus(TaskModel.Status.COMPLETED);
 systemUnderTest.execute(systemTask, taskId);
 }

 @Test
 public void execute_TaskIsScheduledAndConcurrentLimitIsExceeded_PostponesTask()
         throws InterruptedException {
 when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
 systemUnderTest.execute(systemTask, taskId);

 verify(queueDAO).postpone(
             QueueUtils.getQueueName(task),
             task.getTaskId(),
             task.getWorkflowPriority(),
             queueTaskMessagePostponeSecs);
 }

 @Test
 public void execute_TaskIsScheduledAndRateLimitPerFrequencyIsExceeded_PostponesTask()
         throws InterruptedException {
 when(executionDAOFacade.exceedsRateLimitPerFrequency(
             task, new TaskDefinition()))
         .thenReturn(true);
 systemUnderTest.execute(systemTask, taskId);

 verify(queueDAO).postpone(
             QueueUtils.getQueueName(task),
             task.getTaskId(),
             task.getWorkflowPriority(),
             queueTaskMessagePostponeSecs);
 }

 @Test
 public void execute_TaskIsScheduledAndTaskIsAsyncComplete_RemovesTaskFromQueue()
         throws InterruptedException {
 systemUnderTest.execute(systemTask, taskId);

 verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
 }

 @Test
 public void execute_TaskIsInProgress_ExecutesTask() throws Exception {
 systemUnderTest.execute(systemTask, taskId);

 verify(systemTask).execute(workflow, task, workflowExecutor);
 }

 @Test
 public void execute_TaskIsInProgress_UpdatesTask() throws Exception {
 systemUnderTest.execute(systemTask, taskId);

 verify(executionDAOFacade).updateTask(task);
 }

 @Test
 public void execute_TaskIsInProgress_RecordsQueueWaitTime() throws Exception {
 long queueWaitTime = 10L;
 when(monitors.recordQueueWaitTime(
         task.getTaskType(), queueWaitTime)).then(invocationOnMock -> queueWaitTime);
 systemUnderTest.execute(systemTask, taskId);

 assertEquals(queueWaitTime, task.getQueueWaitTime());
 }

 @Test
 public void execute_TaskIsInProgress_DecidesWorkflow() throws Exception {
 systemUnderTest.execute(systemTask, taskId);

 verify(workflowExecutor).decide(workflowId);
 }

 @Test
 public void execute_TaskIsInProgress_LogsDebugMessage() throws Exception {
 when(logger.isDebugEnabled()).thenReturn(true);
 systemUnderTest.execute(systemTask, taskId);

 verify(logger).debug("Executing {}/{} in {} state", task.getTaskType(), task.getTaskId(), task.getStatus());
 }

 @Test
 public void execute_TaskIsInProgress_LogsInfoMessage() throws Exception {
 when(logger.isInfoEnabled()).thenReturn(true);
 systemUnderTest.execute(systemTask, taskId);

 verify(logger).info(
             "Workflow {} has been completed for {}/{}",
             workflow.toShortString(),
             systemTask,
             task.getTaskId());
 }

 @Test
 public void execute_TaskIsInProgress_LogsErrorMessage() throws Exception {
 when(logger.isErrorEnabled()).thenReturn(true);
 throw new Exception();
 systemUnderTest.execute(systemTask, taskId);

 verify(logger).error("Error executing system task - {}, with id: {}", systemTask, taskId);
 }

 @Test
 public void execute_TaskIsInProgress_LogsWarnMessage() throws Exception {
 when(logger.isWarnEnabled()).thenReturn(true);
 systemUnderTest.execute(systemTask, taskId);

 verify(logger).warn(
             "Concurrent Execution limited for {}:{}",
             taskId,
             task.getTaskDefName());
 }

 @Test
 public void postponeQuietly_PostponesTask() throws Exception {
 systemUnderTest.postponeQuietly(queueNameGenerator.queueName("queueName"), task);

 verify(queueDAO).postpone(
             queueNameGenerator.queueName("queueName"),
             task.getTaskId(),
             task.getWorkflowPriority(),
             queueTaskMessagePostponeSecs);
 }

 @Test
 public void loadTaskQuietly_ReturnsNull() throws Exception {
 systemUnderTest.loadTaskQuietly(null);

 verify(executionDAOFacade).getTaskModel(null);
 }

 @Test
 public void loadTaskQuietly_ReturnsTask() throws Exception {
 systemUnderTest.loadTaskQuietly("taskId");

 verify(executionDAOFacade).getTaskModel("taskId");
 }
}

class TaskDefinition {
}

class TestConfiguration {
 static final long queueTaskMessagePostponeSecs = 100L;
}