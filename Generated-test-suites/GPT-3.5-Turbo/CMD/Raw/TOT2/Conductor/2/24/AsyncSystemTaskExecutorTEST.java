import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import org.junit.Before;
import org.junit.Test;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowSystemTask workflowSystemTask;

    @Before
    public void setUp() {
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        workflowSystemTask = mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                mock(ExecutionDAOFacade.class),
                queueDAO,
                metadataDAO,
                mock(ConductorProperties.class),
                mock(WorkflowExecutor.class));
    }

    @Test
    public void testExecute() {
        TaskModel task = new TaskModel();
        task.setTaskId("testTaskId");
        task.setStatus(TaskModel.Status.SCHEDULED);

        String queueName = QueueUtils.getQueueName(task);

        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

        // Verify that the task is removed from the queue
        verify(queueDAO).remove(queueName, task.getTaskId());
    }

    // Additional test cases for edge cases and error scenarios can be added here

}