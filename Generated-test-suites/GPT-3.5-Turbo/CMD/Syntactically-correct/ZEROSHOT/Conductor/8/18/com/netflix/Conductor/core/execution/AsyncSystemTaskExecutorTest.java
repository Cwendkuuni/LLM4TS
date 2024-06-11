import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(), workflowExecutor);
        ReflectionTestUtils.setField(asyncSystemTaskExecutor, "systemTaskCallbackTime", 60L);
        ReflectionTestUtils.setField(asyncSystemTaskExecutor, "queueTaskMessagePostponeSecs", 30L);
    }

    @Test
    public void testExecute() {
        String taskId = "12345";
        WorkflowSystemTask mockSystemTask = mock(WorkflowSystemTask.class);
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(false);
        when(metadataDAO.getTaskDef(any(String.class))).thenReturn(null);
        when(executionDAOFacade.getWorkflowModel(any(String.class), any(Boolean.class))).thenReturn(new WorkflowModel());

        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);

        verify(executionDAOFacade).updateTask(taskModel);
        verify(workflowExecutor).decide(taskModel.getWorkflowInstanceId());
    }
}