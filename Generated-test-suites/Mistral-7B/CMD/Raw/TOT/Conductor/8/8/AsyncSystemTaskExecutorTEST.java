import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskWithResult;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.Queue;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.WorkflowInstanceDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.tasklist.TaskList;
import com.netflix.conductor.utils.RandomUtils;
import com.netflix.conductor.workers.WorkerContext;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private MetadataDAO metadataDAO;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private WorkflowInstanceDAO workflowInstanceDAO;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @Autowired
    private ConductorProperties conductorProperties;

    @Autowired
    private AsyncSystemTaskExecutor systemTaskExecutor;

    @Before
    public void setup() {
        ReflectionTestUtils.setFieldValue(conductorProperties, "systemTaskWorkerCallbackDuration", 1000);
        ReflectionTestUtils.setFieldValue(conductorProperties, "taskExecutionPostponeDuration", 1000);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "task-1";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        WorkflowSystemTask task = Mockito.mock(WorkflowSystemTask.class);

        systemTaskExecutor.execute(task, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_workflowCompleted() {
        String taskId = "task-1";
        String workflowId = "workflow-1";

        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(ExecutionStatus.COMPLETED);

        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        systemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(executionDAOFacade).getWorkflowModel(workflowId, false);
        verify(queueDAO).remove(anyString(), taskId);
        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        String taskId = "task-1";
        String workflowId = "workflow-1";

        TaskModel task = Mockito.mock(TaskModel.class);
        task.setStatus(ExecutionStatus.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);

        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        systemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(executionDAOFacade).getWorkflowModel(workflowId, false);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_taskNotInQueue() {
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName(taskId);

        TaskModel task = Mockito.mock(TaskModel.class);
        task.setStatus(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        systemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<Queue> queueArgumentCaptor = ArgumentCaptor.forClass(Queue.class);
        verify(queueDAO).remove(queueName, taskId);
        verify(executionDAOFacade).updateTask(task);
        verify(workflowExecutor).decide(task.getWorkflowInstanceId());
    }

    @Test
    public void testExecute_taskInQueue() {
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName(taskId);

        TaskModel task = Mockito.mock(TaskModel.class);
        task.setStatus(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false)).thenReturn(workflow);

        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        systemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<Queue> queueArgumentCaptor = ArgumentCaptor.forClass(Queue.class);
        verify(queueDAO).postpone(queueName, taskId, task.getWorkflowPriority(), 1000);
        verify(executionDAOFacade).updateTask(task);
        verifyNoMoreInteractions(queueDAO);
    }

    @Test
    public void testExecute_workflowError() {
        String taskId = "task-1";
        String workflowId = "workflow-1";

        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenThrow(new RuntimeException());

        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        systemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(executionDAOFacade).getWorkflowModel(workflowId, false);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_taskRateLimit() {
        String workflowInstanceId = "workflow-1";
        TaskModel task = Mockito.mock(TaskModel.class);
        task.setWorkflowInstanceId(workflowInstanceId);
        task.setRateLimitPerFrequency(1);
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);

        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        when(executionDAOFacade.getWorkflowModel(workflowInstanceId, false)).thenReturn(workflow);

        WorkflowSystemTaskWithResult systemTask = Mockito.mock(WorkflowSystemTaskWithResult.class);

        systemTaskExecutor.execute(systemTask, task.getTaskId());

        verify(executionDAOFacade).getTaskModel(task.getTaskId());
        verify(executionDAOFacade).getWorkflowModel(workflowInstanceId, false);
        verify(queueDAO).remove(anyString(), task.getTaskId());
        verify(workflowExecutor).decide(workflowInstanceId);

        ArgumentCaptor<Exception> exceptionArgumentCaptor = ArgumentCaptor.forClass(Exception.class);
        verify(Monitors).error(exceptionArgumentCaptor.capture(), anyString());
        assertEquals("RateLimit Execution limited for workflow-1, limit:1", exceptionArgumentCaptor.getValue().getMessage());
    }

    @Test
    public void testExecute_taskConcurrency() {
        String workflowInstanceId = "workflow-1";
        String taskId = "task-1";
        TaskModel task = Mockito.mock(TaskModel.class);
        task.setWorkflowInstanceId(workflowInstanceId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        when(executionDAOFacade.getWorkflowModel(workflowInstanceId, false)).thenReturn(workflow);

        WorkflowSystemTaskWithResult systemTask = Mockito.mock(WorkflowSystemTaskWithResult.class);

        systemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(executionDAOFacade).getWorkflowModel(workflowInstanceId, false);
        verify(queueDAO).postpone(anyString(), taskId, task.getWorkflowPriority(), 1000);
        verify(executionDAOFacade).updateTask(task);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_taskConcurrency_taskList() {
        String workflowInstanceId = "workflow-1";
        String taskId = "task-1";
        String taskListName = "tasklist-1";

        TaskModel task = Mockito.mock(TaskModel.class);
        task.setWorkflowInstanceId(workflowInstanceId);
        task.setTaskListName(taskListName);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getTaskList(taskListName)).thenReturn(Mockito.mock(TaskList.class));

        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        when(executionDAOFacade.getWorkflowModel(workflowInstanceId, false)).thenReturn(workflow);

        WorkflowSystemTaskWithResult systemTask = Mockito.mock(WorkflowSystemTaskWithResult.class);

        systemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(executionDAOFacade).getWorkflowModel(workflowInstanceId, false);
        verify(queueDAO).postpone(anyString(), taskId, task.getWorkflowPriority(), 1000);
        verify(executionDAOFacade).updateTask(task);
        verifyNoMoreInteractions(workflowExecutor);
    }
}