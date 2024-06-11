import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.netflix.conductor.common.utils.map.ConcurrentHashMapMap;
import org.netflix.conductor.core.config.ConductorProperties;
import org.netflix.conductor.core.dal.ExecutionDAOFacade;
import org.netflix.conductor.core.dal.QueueDAO;
import org.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import org.netflix.conductor.core.model.TaskModel;
import org.netflix.conductor.core.model.WorkflowModel;
import org.netflix.conductor.core.utils.QueueUtils;
import org.netflix.conductor.dao.MetadataDAO;
import org.netflix.conductor.dao.QueueDAO;
import org.netflix.conductor.metrics.Monitors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AsyncSystemTaskExecutorTest {

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private MetadataDAO metadataDAO;

    @MockBean
    private ConductorProperties conductorProperties;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        Map<String, Object> conductorPropertiesMap = new ConcurrentHashMap<>();
        conductorPropertiesMap.put("systemTaskWorkerCallbackDuration.seconds", 5L);
        conductorPropertiesMap.put("taskExecutionPostponeDuration.seconds", 3L);
        ConductorProperties conductorPropertiesMock = mock(ConductorProperties.class);
        Mockito.when(conductorPropertiesMock.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(5));
        Mockito.when(conduitorPropertiesMock.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(3));
        Mockito.when(conductorPropertiesMock.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(3));

        ArgumentCaptor<TaskModel> taskModelCaptor = ArgumentCaptor.forClass(TaskModel.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorPropertiesMock,
                workflowExecutor);
    }

    @Test
    public void testExecute_taskIsTerminal_queueMessageIsRemoved() {
        String taskId = "taskId123";
        String queueName = QueueUtils.getQueueName("taskQueueName");
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setTaskType("taskType123");
        task.setStatus(TaskModel.Status.COMPLETED);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testExecute_taskIsTerminal_workflowIsEvaluated() {
        String workflowId = "workflowId123";
        String taskId = "taskId123";
        String queueName = QueueUtils.getQueueName("taskQueueName");
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setTaskType("taskType123");
        task.setStatus(TaskModel.Status.COMPLETED);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testExecute_taskIsInProgress_taskIsPostponed() {
        String taskId = "taskId123";
        String queueName = QueueUtils.getQueueName("taskQueueName");
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setTaskType("taskType123");
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).postpone(queueName, taskId, 5, 3);
    }

    @Test
    public void testExecute_taskIsScheduled_taskIsExecuted() {
        String taskId = "taskId123";
        String queueName = QueueUtils.getQueueName("taskQueueName");
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setTaskType("taskType123");
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.RUNNING);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        Mockito.when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        Mockito.when(workflowExecutor.decide(workflowId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        verify(workflowExecutor).execute(any(WorkflowModel.class), eq(task), any(WorkflowSystemTask.class));
    }

    @Test
    public void testExecute_taskIsCanceled_taskIsRemovedFromQueue() {
        String taskId = "taskId123";
        String queueName = QueueUtils.getQueueName("taskQueueName");
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setTaskType("taskType123");
        task.setStatus(TaskModel.Status.CANCELED);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).remove(queueName, taskId);
    }
}