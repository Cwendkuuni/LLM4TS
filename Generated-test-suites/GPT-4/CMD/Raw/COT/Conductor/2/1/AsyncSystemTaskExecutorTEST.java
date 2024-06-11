
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;

import com.netflix.conductor.common.metadata.tasks.Task.Status;
import com.netflix.conductor.core.config.impl.ConductorProperties;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.run.Workflow;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

    @Mock 
    private ExecutionDAOFacade mockedExecutionDAOFacade;
    
    @Mock 
    private QueueDAO mockedQueueDAO;
    
    @Mock 
    private MetadataDAO mockedMetadataDAO;
    
    @Mock
    private ConductorProperties mockedConductorProperties;
    
    @Mock 
    private WorkflowExecutor mockedWorkflowExecutor;
    
    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    
    private final long TEST_SYSTEM_TASK_WORKER_CALLBACK_DURATION = 5000L;
    private final long TEST_TASK_EXECUTION_POSTPONE_DURATION = 3000L;

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "a task id";

        TaskModel task = new TaskModel();
        task.setStartTime(1000);
        when(mockedExecutionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(mockedExecutionDAOFacade, times(1)).getTaskModel(anyString());
        assertEquals(1000, task.getStartTime());
    }

    @Test
    public void testExecuteWhenTaskIsNull() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "a task id";

        when(mockedExecutionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(mockedExecutionDAOFacade, times(1)).getTaskModel(anyString());
        verify(mockedQueueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteWhenTaskStatusIsTerminal() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "a task id";

        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        when(mockedExecutionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(mockedExecutionDAOFacade, times(1)).getTaskModel(anyString());
        assertEquals(TaskModel.Status.COMPLETED, task.getStatus());
        verify(mockedQueueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteWhenTaskStatusIsScheduled() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "a task id";

        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(mockedExecutionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(mockedExecutionDAOFacade, times(1)).getTaskModel(anyString());
        assertEquals(TaskModel.Status.SCHEDULED, task.getStatus());
        verify(mockedExecutionDAOFacade, times(1)).exceedsInProgressLimit(task);
        verify(mockedQueueDAO, times(1)).postpone(anyString(), anyString(), anyDouble(), anyLong());
    }
}
