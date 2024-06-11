import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowSystemTask systemTask;

    @Mock
    private WorkflowModel workflow;

    private final String taskId = "someTaskId";

    @Before
    public void setUp() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
    }

    @After
    public void tearDown() {
        reset(executionDAOFacade, queueDAO, metadataDAO,
              conductorProperties, workflowExecutor, task, systemTask, workflow);
    }

    @Test
    public void testExecuteTaskNotFound() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    // other comprehensive test cases would follow: testing the method in normal conditions, when task is already
    // terminal, when task is scheduled, when an exception occurs, etc.
}