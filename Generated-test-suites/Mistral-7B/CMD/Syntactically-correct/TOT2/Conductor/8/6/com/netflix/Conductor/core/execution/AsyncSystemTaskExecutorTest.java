import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import static org.junit.Assert.*;

public class AsyncSystemTaskExecutorTest {

 @Autowired
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 @MockitoInstance(lenient = true)
 private static MockitoContext mockitoContext;

 @Mock
 private ExecutionDAOFacade executionDAOFacadeMock;

 @Mock
 private QueueDAO queueDAOMock;

 @Mock
 private MetadataDAO metadataDAOMock;

 @Mock
 private WorkflowExecutor workflowExecutorMock;

 @Value("${conductor.systemTaskWorkerCallbackDuration:5000}")
 private long systemTaskCallbackTime;

 @Value("${conductor.taskExecutionPostponeDuration:30000}")
 private long queueTaskMessagePostponeSecs;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 asyncSystemTaskExecutor =
 new AsyncSystemTaskExecutor(
 executionDAOFacadeMock,
 queueDAOMock,
 metadataDAOMock,
 Mockito.mock(ConductorProperties.class),
 workflowExecutorMock);
 }

 @Test
 public void testExecute_taskIsNotFound() {
 String taskId = "task-1";
 WorkflowSystemTask systemTaskMock = Mockito.mock(WorkflowSystemTask.class);

 AsyncSystemTaskExecutor.LOGGER.error("Error loading task: {}", taskId);

 asyncSystemTaskExecutor.execute(systemTaskMock, taskId);

 ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);
 Mockito.verify(queueDAOMock).remove(Mockito.eq(QueueUtils.getQueueName("task-1")), taskIdCaptor.capture());
 assertEquals(taskId, taskIdCaptor.getValue());
 }

 @Test
 public void testExecute_exceedsInProgressLimit() {
 String taskId = "task-1";
 TaskModel taskModelMock = Mockito.mock(TaskModel.class);
 WorkflowModel workflowModelMock = Mockito.mock(WorkflowModel.class);
 WorkflowSystemTask systemTaskMock = Mockito.mock(WorkflowSystemTask.class);

 Mockito.when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);
 Mockito.when(executionDAOFacadeMock.exceedsInProgressLimit(taskModelMock)).thenReturn(true);

 AsyncSystemTaskExecutor.LOGGER.warn("Concurrent Execution limited for {}", taskId);

 asyncSystemTaskExecutor.execute(systemTaskMock, taskId);

 Mockito.verify(queueDAOMock).postpone(
 Mockito.eq(QueueUtils.getQueueName("task-1")),
 taskIdMock(taskId),
 Mockito.anyLong(),
 queueTaskMessagePostponeSecs);
 }

 // Add more test cases for other methods and edge cases here

}