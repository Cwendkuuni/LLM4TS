
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.TimeUnit;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private MetadataDAO metadataDAO;
    @Mock
    private WorkflowSystemTask workflowSystemTask;
    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private ConductorProperties conductorProperties;
    @Mock
    private TaskModel task;
    @Mock
    private WorkflowModel workflow;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        when(conductorProperties.getSystemTaskWorkerCallbackDuration())
                .thenReturn(new Duration(0L, TimeUnit.SECONDS));
        when(conductorProperties.getTaskExecutionPostponeDuration())
                .thenReturn(new Duration(0L, TimeUnit.SECONDS));
        when(workflowSystemTask.isTaskRetrievalRequired())
                .thenReturn(false);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflow);
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "testTaskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertNull(result);
    }
    
    @Test
    public void testPostponeQuietly() {
        String queueName = "testQueue";
        when(task.getTaskId()).thenReturn("testTaskId");
        when(task.getWorkflowPriority()).thenReturn(0);
        doThrow(new RuntimeException()).when(queueDAO)
                .postpone(queueName, "testTaskId",0,0);
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        verify(queueDAO).postpone(queueName, "testTaskId",0,0);
    }

    @Test
    public void testExecuteTaskNotFound() {
        when(executionDAOFacade.getTaskModel(any())).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.execute(workflowSystemTask, "someTaskId");
        verify(queueDAO, times(1)).remove(any(), any());
    }

    @Test
    public void testExecuteTaskExceedsInProgressLimit() {
        String taskId = "testTaskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        verify(queueDAO, atLeastOnce()).postpone(any(), any(), anyInt(), anyInt());
    }

    @Test
    public void testExecuteTaskExceedsRateLimit() {
        String taskId = "testTaskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, metadataDAO.getTaskDef(task.getTaskDefName()))).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        verify(queueDAO, atLeastOnce()).postpone(any(), any(), anyInt(), anyInt());
    }
}
