
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import java.util.Optional;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.tasks.TaskModel.Status;
import com.netflix.conductor.common.run.Workflow;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;

public class AsyncSystemTaskExecutorTest {

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

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecuteTerminalTask_returnsWithoutExecuting() throws Exception {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "test-task";
        TaskModel task = mock(TaskModel.class);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(systemTask, never()).start(any(), any(), any());
        verify(queueDAO).remove(systemTask.getTaskType(), taskId);
    }

    @Test
    public void testExecuteFailureToRemoveTaskFromQueue_doesNotRethrowException() throws Exception {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "test-task";
        doThrow(new RuntimeException("cannot remove")).when(queueDAO).remove(anyString(), anyString());
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test
    public void testExecuteScheduledTask() throws Exception {
        String taskId = "test-task";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskModel task = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), systemTask.isTaskRetrievalRequired())).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(systemTask).start(workflow, task, workflowExecutor);
    }

    // Mutable state test
    @Test
    public void testConcurrentExecutionLimitExceeded(){
        String taskId = "testTaskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = mock(TaskModel.class);

        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).postpone(anyString(), eq(task.getTaskId()), anyInt(), anyLong());
    }
}
