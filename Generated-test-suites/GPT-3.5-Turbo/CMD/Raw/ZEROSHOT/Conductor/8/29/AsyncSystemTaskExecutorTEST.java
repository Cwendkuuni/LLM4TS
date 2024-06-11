import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
    public void setup() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        String taskId = "testTaskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("testTaskType");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(new WorkflowModel());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).getWorkflowModel(anyString(), anyBoolean());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "testTaskId";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(taskModel, result);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "testQueue";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("testTaskId");
        taskModel.setWorkflowPriority(1L);

        when(queueDAO.postpone(anyString(), anyString(), anyLong(), anyLong())).thenReturn(true);

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAO, times(1)).postpone(queueName, taskModel.getTaskId(), taskModel.getWorkflowPriority(), 60L);
    }

}