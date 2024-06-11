import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private MetadataDAO metadataDAO;

    @MockBean
    private ConductorProperties conductorProperties;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @Autowired
    private WorkflowSystemTask workflowSystemTask;

    private TaskModel task;
    private String taskId = "taskId";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        task = mock(TaskModel.class);
    }

    @Test
    public void testExecute_TaskIsAlreadyCompleted_ShouldRemoveTaskFromQueue() {
        // Given
        WorkflowModel workflow = mock(WorkflowModel.class);
        when(executionDAOFacade.getWorkflowModel(eq(task.getWorkflowInstanceId()), eq(false)))
                .thenReturn(workflow);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Then
        verify(queueDAO).remove(eq("queueName"), eq(taskId));
    }

    // Add other test cases for typical use cases, edge cases, and error scenarios here

    // 
    // Test cases for all methods go here
    // 
}