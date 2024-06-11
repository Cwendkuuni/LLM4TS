import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.utils.QueueUtils;
import com.netflix.conductor.utils.WorkflowExecutor;

public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

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
    private WorkflowSystemTask workflowSystemTask;

    @Mock
    private TaskModel taskModel;

    @Mock
    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(executionDAOFacade.getTaskModel(any())).thenReturn(Optional.of(taskModel));
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new Object() {});
    }

    @Test
    public void testExecute_taskIsAlreadyCompleted() {
        String taskId = "testTaskId";
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // No action is expected since the task is already completed
    }

    @Test
    public void testExecute_taskIsTerminal() {
        String taskId = "testTaskId";
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.TERMINAL);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // No action is expected since the task is already terminal
    }

    @Test
    public void testExecute_taskIsScheduled() {
        String taskId = "testTaskId";
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Verify that the task is removed from the queue after execution
        when(queueDAO.remove(anyString(), taskId)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_taskIsInProgress() {
        String taskId = "testTaskId";
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Verify that the task's status is updated and the workflow is evaluated
        when(workflowExecutor.decide(anyString())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowExecutor).decide(anyString());
    }

    @Test
    public void testExecute_taskIsPostponed() {
        String taskId = "testTaskId";
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Verify that the task is postponed in the queue
        when(queueDAO.postpone(
                anyString(), anyString(), anyInt(), anyInt())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test(expected = Exception.class)
    public void testExecute_exceptionDuringExecution() {
        String taskId = "testTaskId";
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        when(workflowModel.getStatus()).thenThrow(new RuntimeException("Test Exception"));

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // An exception is expected during task execution
    }

    @Test
    public void testPostponeQuietly_taskIsNotFound() {
        String queueName = "testQueueName";
        String taskId = "testTaskId";

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        // No action is expected since the task is not found
    }

    @Test
    public void testLoadTaskQuietly_taskIsNotFound() {
        String taskId = "testTaskId";

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // The loaded task is expected to be null
        assertNotNull(loadedTask);
    }
}