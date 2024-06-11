import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

 private AsyncSystemTaskExecutor systemUnderTest;
 private ExecutionDAOFacade executionDAOFacadeMock;
 private QueueDAO queueDAOMock;
 private MetadataDAO metadataDAOMock;
 private ConductorProperties conductorPropertiesMock;
 private WorkflowExecutor workflowExecutorMock;

 @Before
 public void setup() {
 executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
 queueDAOMock = mock(QueueDAO.class);
 metadataDAOMock = mock(MetadataDAO.class);
 conductorPropertiesMock = mock(ConductorProperties.class);
 workflowExecutorMock = mock(WorkflowExecutor.class);
 systemUnderTest = new AsyncSystemTaskExecutor(
 executionDAOFacadeMock,
 queueDAOMock,
 metadataDAOMock,
 conductorPropertiesMock,
 workflowExecutorMock);
 }

 @Test
 public void testExecute_taskDoesNotExistInExecutionDAO_taskIsRemovedFromQueue() {
 String taskId = "taskId123";
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(null);

 systemUnderTest.execute(systemTask, taskId);

 verify(queueDAOMock).remove(anyString(), taskId);
 }

 @Test
 public void testExecute_taskIsInProgress_taskStatusIsUpdated() {
 String taskId = "taskId123";
 TaskModel task = Mockito.mock(TaskModel.class);
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 when(executionDAOFacadeMock.getWorkflowModel(eq(task.getWorkflowInstanceId()), anyBoolean()))
 .thenReturn(Mockito.mock(WorkflowModel.class));

 systemUnderTest.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

 assertEquals(TaskModel.Status.IN_PROGRESS, ReflectionTestUtils.getFieldValue(task, "status"));
 }

 @Test(expected = NullPointerException.class)
 public void testExecute_executionDAOFacadeIsNull() {
 WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
 systemUnderTest.execute(systemTask, null);
 }

 @Test
 public void testExecute_taskIsTerminal_taskIsRemovedFromQueue() {
 String taskId = "taskId123";
 TaskModel task = Mockito.mock(TaskModel.class);
 task.setStatus(TaskModel.Status.COMPLETED);
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);

 systemUnderTest.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

 verify(queueDAOMock).remove(anyString(), taskId);
 }

 @Test
 public void testExecute_exceedsInProgressLimit_taskIsPostponed() {
 String taskId = "taskId123";
 String queueName = "QueueName";
 TaskModel task = Mockito.mock(TaskModel.class);
 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
 when(executionDAOFacadeMock.exceedsInProgressLimit(task)).thenReturn(true);

 ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Long> callbackAfterSecondsCaptor = ArgumentCaptor.forClass(Long.class);

 systemUnderTest.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

 verify(queueDAOMock).postpone(queueNameCaptor.capture(), eq(taskId), anyLong(), eq(queueTaskMessagePostponeSecs));
 Long callbackAfterSeconds = callbackAfterSecondsCaptor.getValue();
 assertEquals(queueTaskMessagePostponeSecs, callbackAfterSeconds);
 }

 // Add more test cases for other methods and edge cases here
}