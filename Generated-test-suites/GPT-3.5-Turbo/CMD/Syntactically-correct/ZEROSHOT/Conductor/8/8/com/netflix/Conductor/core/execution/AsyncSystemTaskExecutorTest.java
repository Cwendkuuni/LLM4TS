import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.workflow.WorkflowExecutor;
import org.junit.Before;
import org.junit.Test;

public class AsyncSystemTaskExecutorTest {

    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        // Create test data
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "testTaskId";

        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskType("testTaskType");
        task.setWorkflowInstanceId("testWorkflowId");

        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId("testWorkflowId");
        workflow.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

        // Mock behaviors
        when(executionDAOFacade.getTaskModel(any())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(any(), anyBoolean())).thenReturn(workflow);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(systemTask.isAsyncComplete(any())).thenReturn(true);

        // Call method under test
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify interactions
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).getWorkflowModel("testWorkflowId", true);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
        verify(executionDAOFacade, times(1)).updateTask(task);
        verify(workflowExecutor, times(1)).decide("testWorkflowId");
    }

    @Test
    public void testPostponeQuietly() {
        // Create test data
        String queueName = "testQueue";
        TaskModel task = new TaskModel();
        task.setTaskId("testTaskId");
        task.setWorkflowPriority(1);

        // Mock behavior
        doNothing().when(queueDAO).postpone(eq(queueName), eq("testTaskId"), eq(1), anyLong());

        // Call method under test
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        // Verify interactions
        verify(queueDAO, times(1)).postpone(queueName, "testTaskId", 1, asyncSystemTaskExecutor.getQueueTaskMessagePostponeSecs());
    }

    @Test
    public void testLoadTaskQuietly() {
        // Mock behavior
        when(executionDAOFacade.getTaskModel("testTaskId")).thenReturn(new TaskModel());

        // Call method under test
        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly("testTaskId");

        // Verify interactions
        verify(executionDAOFacade, times(1)).getTaskModel("testTaskId");
    }
}