package com.netflix.conductor.core.execution;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;
    
    @Mock
    private QueueDAO queueDAO;
    
    @Mock
    private ConductorProperties conductorProperties;
    
    @Mock
    private WorkflowExecutor workflowExecutor;
    
    @Mock
    private WorkflowSystemTask workflowSystemTask;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade,queueDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void executeTest() {
        TaskModel.Task t = new TaskModel.Task();
        doReturn(t).when(executionDAOFacade).getTask(any());
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.Task.class));
    }

    @Test
    public void executeWithExceptionTest() {
        doThrow(RuntimeException.class).when(executionDAOFacade).getTask(any());
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(executionDAOFacade, times(0)).updateTask(any(TaskModel.Task.class));
    }
}