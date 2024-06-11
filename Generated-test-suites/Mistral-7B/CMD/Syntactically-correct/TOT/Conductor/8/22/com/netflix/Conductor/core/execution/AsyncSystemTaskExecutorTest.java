import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.netflix.conductor.metrics.Monitors;
import org.netflix.conductor.core.config.ConductorProperties;
import org.netflix.conductor.core.dal.ExecutionDAOFacade;
import org.netflix.conductor.core.dal.QueueDAO;
import org.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import org.netflix.conductor.core.model.ExecutionStatus;
import org.netflix.conductor.core.model.QueueDefinition;
import org.netflix.conductor.core.model.TaskModel;
import org.netflix.conductor.core.model.WorkflowModel;
import org.netflix.conductor.core.utils.QueueUtils;
import org.netflix.conductor.dao.MetadataDAO;
import org.netflix.conductor.dao.QueueDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;

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

    @Mock
    private Monitors monitors;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Captor
    private ArgumentCaptor<TaskModel> taskModelArgumentCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_TaskNotFound() {
        String taskId = UUID.randomUUID().toString();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAO, never()).update(anyString(), any(TaskModel.class));
        verify(executionDAOFacade, never()).getWorkflowModel(anyString(), anyBoolean());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutor, never()).decide(anyString());
        verify(LOGGER, times(1)).error("TaskId: {} could not be found while executing {}", taskId, systemTask);
    }

    @Test
    public void testExecute_TaskInProgress() {
        String taskId = UUID.randomUUID().toString();
        String queueName = QueueUtils.getQueueName("workflow");
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setId("workflow-id");

        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new QueueDefinition());

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAO, never()).update(anyString(), any(TaskModel.class));
        verify(executionDAOFacade, never()).getWorkflowModel(anyString(), anyBoolean());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutor, never()).decide(anyString());
        verify(LOGGER, never()).info("Task {}/{} was already completed.", taskId, task.getTaskType());
        verify(LOGGER, times(1)).debug("Executing {}/{} in {} state", taskId, task.getTaskType(), task.getStatus());
    }

    @Test
    public void testExecute_TaskCompleted() {
        String taskId = UUID.randomUUID().toString();
        String queueName = QueueUtils.getQueueName("workflow");
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setId("workflow-id");

        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new QueueDefinition());

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(queueName, taskId);
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAO, never()).update(anyString(), any(TaskModel.class));
        verify(executionDAOFacade, never()).getWorkflowModel(anyString(), anyBoolean());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(1)).decide(workflow.getId());
        verify(LOGGER, never()).info("Task {}/{} was already completed.", taskId, task.getTaskType());
        verify(LOGGER, never()).debug("Executing {}/{} in {} state", taskId, task.getTaskType(), task.getStatus());
        verify(LOGGER, times(1)).info("Workflow {} has been completed for {}/{}", workflow.toShortString(), systemTask, task.getTaskId());
    }

    @Test
    public void testExecute_TaskFailed() {
        String taskId = UUID.randomUUID().toString();
        String queueName = QueueUtils.getQueueName("workflow");
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.FAILED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setId("workflow-id");
        workflow.setStatus(ExecutionStatus.FAILED);

        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new QueueDefinition());

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(queueName, taskId);
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAO, never()).update(anyString(), any(TaskModel.class));
        verify(executionDAOFacade, never()).getWorkflowModel(anyString(), anyBoolean());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(1)).decide(workflow.getId());
        verify(LOGGER, never()).info("Task {}/{} was already completed.", taskId, task.getTaskType());
        verify(LOGGER, never()).debug("Executing {}/{} in {} state", taskId, task.getTaskType(), task.getStatus());
        verify(LOGGER, never()).info("Workflow {} has been completed for {}/{}", workflow.toShortString(), systemTask, task.getTaskId());
        verify(LOGGER, times(1)).info("Workflow {} has been failed for {}/{}", workflow.toShortString(), systemTask, task.getTaskId());
    }

    @Test
    public void testExecute_TaskTerminal() {
        String taskId = UUID.randomUUID().toString();
        String queueName = QueueUtils.getQueueName("workflow");
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.TERMINAL);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setId("workflow-id");

        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new QueueDefinition());

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(queueName, taskId);
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAO, never()).update(anyString(), any(TaskModel.class));
        verify(executionDAOFacade, never()).getWorkflowModel(anyString(), anyBoolean());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutor, never()).decide(workflow.getId());
        verify(LOGGER, times(1)).info("Task {}/{} was already completed.", taskId, task.getTaskType());
        verify(LOGGER, never()).debug("Executing {}/{} in {} state", taskId, task.getTaskType(), task.getStatus());
    }

    @Test
    public void testExecute_TaskPostponed() {
        String taskId = UUID.randomUUID().toString();
        String queueName = QueueUtils.getQueueName("workflow");
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setCallbackAfterSeconds(100L);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(queueDAO.get(queueName, taskId)).thenReturn(task);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setId("workflow-id");

        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new QueueDefinition());

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, never()).remove(queueName, taskId);
        verify(queueDAO, times(1)).postpone(queueName, taskId, task.getWorkflowPriority(), task.getCallbackAfterSeconds());
        verify(queueDAO, never()).update(anyString(), any(TaskModel.class));
        verify(executionDAOFacade, never()).getWorkflowModel(anyString(), anyBoolean());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutor, never()).decide(workflow.getId());
        verify(LOGGER, never()).info("Task {}/{} was already completed.", taskId, task.getTaskType());
        verify(LOGGER, never()).debug("Executing {}/{} in {} state", taskId, task.getTaskType(), task.getStatus());
    }
}