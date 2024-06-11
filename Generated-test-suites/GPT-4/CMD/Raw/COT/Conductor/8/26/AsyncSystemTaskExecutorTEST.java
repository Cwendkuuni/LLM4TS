
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AsyncSystemTaskExecutorTest {
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

    private String taskId = "testTaskId";
    private String workflowId = "testWorkflowId";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(10));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(1000));
    }

    @Test
    public void testExecute() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getWorkflowInstanceId()).thenReturn(workflowId);
        when(executionDAOFacade.getWorkflowModel(workflowId, workflowSystemTask.isTaskRetrievalRequired())).thenReturn(workflowModel);
        doNothing().when(executionDAOFacade).updateTask(taskModel);
        doNothing().when(workflowExecutor).decide(workflowId);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask).start(workflowModel, taskModel, workflowExecutor);
    }

    @Test(expected = NullPointerException.class)
    public void testExecuteWhenTaskIsNull() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
    }

    @Test(expected = NullPointerException.class)
    public void testExecuteWhenWorkflowIsNull() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getWorkflowInstanceId()).thenReturn(workflowId);
        when(executionDAOFacade.getWorkflowModel(workflowId, workflowSystemTask.isTaskRetrievalRequired())).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
    }
}
