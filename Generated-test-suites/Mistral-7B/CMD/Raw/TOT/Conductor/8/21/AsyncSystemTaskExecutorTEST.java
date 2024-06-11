import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.utils.TestExtensions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AsyncSystemTaskExecutorTest {

 @Mock
 private ExecutionDAOFacade executionDAOFacade;

 @Mock
 private QueueDAO queueDAO;

 @Mock
 private MetadataDAO metadataDAO;

 @Mock
 private WorkflowExecutor workflowExecutor;

 @Mock
 private ConductorProperties conductorProperties;

 @InjectMocks
 private AsyncSystemTaskExecutor systemUnderTest;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(TestExtensions.Duration.ofSeconds(1));
 when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(TestExtensions.Duration.ofSeconds(1));
 }

 @Test
 public void testExecute_workflowNotTerminal_taskNotInQueue_success() {
 // Given
 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowInstanceStatus.RUNNING);
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setTaskDefName("someTaskDefName");
 String queueName = QueueUtils.getQueueName(task);
 when(executionDAOFacade.getWorkflowModel(any(), anyBoolean())).thenReturn(workflow);

 // When
 systemUnderTest.execute(new WorkflowSystemTask() {
 @Override
 public void start(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {}

 @Override
 public void execute(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {}
 }, task.getTaskId());

 // Then
 // no need to assert anything here as we are testing the side effects of the method
 // the method should have updated the task status, removed it from the queue, and decided the workflow

 verify(workflowExecutor).decide(workflow.getId());
 }

 @Test
 public void testExecute_workflowTerminal_taskNotInQueue_success() {
 // Given
 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowInstanceStatus.COMPLETED);
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setTaskDefName("someTaskDefName");
 String queueName = QueueUtils.getQueueName(task);
 when(executionDAOFacade.getWorkflowModel(any(), anyBoolean())).thenReturn(workflow);
 when(queueDAO.remove(queueName, task.getTaskId())).thenReturn(true);

 // When
 systemUnderTest.execute(new WorkflowSystemTask() {
 @Override
 public void start(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {}

 @Override
 public void execute(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {}
 }, task.getTaskId());

 // Then
 verify(workflowExecutor).decide(workflow.getId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecute_workflowTerminal_taskInQueue_exception() {
 // Given
 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowInstanceStatus.COMPLETED);
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setTaskDefName("someTaskDefName");
 String queueName = QueueUtils.getQueueName(task);
 when(executionDAOFacade.getWorkflowModel(any(), anyBoolean())).thenReturn(workflow);
 when(queueDAO.remove(queueName, task.getTaskId())).thenReturn(false);

 // When & Then
 assertThrows(IllegalArgumentException.class,
 () -> systemUnderTest.execute(new WorkflowSystemTask() {}, task.getTaskId()));
 }

 @Test
 public void testExecute_taskSucceeds_callback() {
 // Given
 WorkflowModel workflow = new WorkflowModel();
 WorkflowInstance workflowInstance = new WorkflowInstance();
 workflowInstance.setId("workflowId");
 workflow.setWorkflowDefinitionId("someWorkflowDefId");
 workflow.setWorkflowInstance(workflowInstance);
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setTaskDefName("someTaskDefName");
 String queueName = QueueUtils.getQueueName(task);
 when(executionDAOFacade.getWorkflowModel(any(), anyBoolean())).thenReturn(workflow);
 when(queueDAO.postpone(queueName, task.getTaskId(), 0, 1)).thenReturn(true);
 Monitors mockMonitors = mock(Monitors.class);
 when(workflowExecutor.getMonitors()).thenReturn(mockMonitors);

 // When
 systemUnderTest.execute(new WorkflowSystemTask() {
 @Override
 public void start(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {}

 @Override
 public void execute(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {}

 @Override
 public void asyncComplete(TaskModel task) {
 Monitors.recordSuccess(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask");
 }
 }, task.getTaskId());

 // Then
 verify(workflowExecutor, times(1)).decide(workflow.getId());
 verify(mockMonitors).recordSuccess(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask");
 }
}