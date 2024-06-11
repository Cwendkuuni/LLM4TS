
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;

import java.time.Duration;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;
	
    @Mock
    private WorkflowSystemTask systemTask;
	
	@Mock
	private WorkflowExecutor workflowExecutor;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private Logger logger;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<TaskModel.Status> statusArgumentCaptor;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private String taskId = "task1";

    private Long systemTaskCallbackTime = 10L;

    private String queueName = "QUEUE_NAME";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(30L));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(60L));
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO,
                conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute_withNullTask_shouldExit() {
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        // Verify that the queueDAO.remove() was never called since the task is null
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_withTerminalState_shouldRemoveFromQueue() {
        when(task.getStatus()).thenReturn(Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        // Call the method under test
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify that the queueDAO.remove() was called once with specified task Id
        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        // Verify that the queueDAO.postpone() method was called once with specific arguments
        verify(queueDAO, times(1)).postpone(queueName, taskId, 0, 0L);
    }

    @Test
    public void testLoadTaskQuietly() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Verify that the executionDAOFacade.getTaskModel() was called once
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }
}
