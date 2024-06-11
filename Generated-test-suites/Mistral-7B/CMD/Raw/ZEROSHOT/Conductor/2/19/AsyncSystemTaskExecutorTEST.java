import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

import java.util.Optional;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueNameGenerator;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.workflow.WorkflowInstance;

public class AsyncSystemTaskExecutorTest {

 private static final String TASK_TYPE = "TaskType";
 private static final String TASK_ID = "TaskId";
 private static final String WORKFLOW_ID = "WorkflowId";
 private static final String QUEUE_NAME = "queueName";

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

 @Mock
 private WorkflowSystemTask systemTask;

 @Mock
 private TaskModel task;

 @Mock
 private WorkflowModel workflow;

 @InjectMocks
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "queueDAO", queueDAO);
 ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "metadataDAO", metadataDAO);
 ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "executionDAOFacade", executionDAOFacade);
 ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "conductorProperties", conductorProperties);
 ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "workflowExecutor", workflowExecutor);
 doReturn(Optional.of(task)).when(executionDAOFacade).getTaskModel(TASK_ID);
 doReturn(Optional.of(workflow)).when(executionDAOFacade).getWorkflowModel(WORKFLOW_ID, false);
 doReturn(QueueNameGenerator.getQueueName(task)).when(queueDAO).getQueueName(TASK_TYPE);
 doReturn(QUEUE_NAME).when(queueDAO).getQueueName(anyString());
 doReturn(new WorkflowInstance()).when(workflow).getWorkflowInstance();
 }

 @After
 public void tearDown() {
 // Add any tear down code here
 }

 @Test
 public void testExecute_TaskNotFound() {
 doReturn(Optional.empty()).when(executionDAOFacade).getTaskModel(TASK_ID);
 asyncSystemTaskExecutor.execute(systemTask, TASK_ID);
 verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
 }

 @Test
 public void testExecute_TaskInProgress_NoPostpone() {
 givenTaskIsInProgress();
 doCallRealMethod().when(asyncSystemTaskExecutor).execute(systemTask, TASK_ID);
 verify(queueDAO, never()).postpone(QUEUE_NAME, TASK_ID, anyInt(), anyInt());
 }

 @Test
 public void testExecute_TaskInProgress_Postpone() {
 givenTaskIsInProgress();
 doCallRealMethod().when(asyncSystemTaskExecutor).execute(systemTask, TASK_ID);
 verify(queueDAO, times(1)).postpone(QUEUE_NAME, TASK_ID, anyInt(), anyInt());
 }

 @Test
 public void testExecute_TaskCompleted_RemoveFromQueue() {
 givenTaskIsCompleted();
 doCallRealMethod().when(asyncSystemTaskExecutor).execute(systemTask, TASK_ID);
 verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
 }

 @Test
 public void testExecute_TaskTerminal_RemoveFromQueue() {
 givenTaskIsTerminal();
 doCallRealMethod().when(asyncSystemTaskExecutor).execute(systemTask, TASK_ID);
 verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
 }

 @Test
 public void testExecute_TaskInProgress_RateLimitExceeded() {
 givenTaskIsInProgress();
 doThrow(new RuntimeException("Rate limit exceeded")) // Add your rate limit exception here
 .when(executionDAOFacade).exceedsRateLimitPerFrequency(task, any());
 doCallRealMethod().when(asyncSystemTaskExecutor).execute(systemTask, TASK_ID);
 verify(queueDAO, times(1)).postpone(QUEUE_NAME, TASK_ID, anyInt(), anyInt());
 }

 @Test
 public void testExecute_TaskInProgress_ConcurrentLimitExceeded() {
 givenTaskIsInProgress();
 doThrow(new RuntimeException("Concurrent limit exceeded")) // Add your concurrent limit exception here
 .when(executionDAOFacade).exceedsInProgressLimit(task);
 doCallRealMethod().when(asyncSystemTaskExecutor).execute(systemTask, TASK_ID);
 verify(queueDAO, times(1)).postpone(QUEUE_NAME, TASK_ID, anyInt(), anyInt());
 }

 private void givenTaskIsInProgress() {
 task.setStatus(TaskModel.Status.IN_PROGRESS);
 }

 private void givenTaskIsCompleted() {
 task.setStatus(TaskModel.Status.COMPLETED);
 }

 private void givenTaskIsTerminal() {
 task.setStatus(TaskModel.Status.TERMINAL);
 }

 private void doCallRealMethod() {
 asyncSystemTaskExecutor.execute(systemTask, TASK_ID);
 }
}