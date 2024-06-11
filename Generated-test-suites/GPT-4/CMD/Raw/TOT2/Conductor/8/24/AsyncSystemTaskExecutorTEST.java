
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;
    @Mock
    private WorkflowSystemTask systemTask;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private MetadataDAO metadataDAO;
    @Mock
    private TaskModel taskModel;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private String taskId = "testTaskId";
    private String queueName = "testQueueName";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ConductorProperties conductorProperties = new ConductorProperties();
        conductorProperties.setSystemTaskWorkerCallbackDuration(TimeUnit.SECONDS, new Random().nextInt(5000));
        conductorProperties.setTaskExecutionPostponeDuration(TimeUnit.SECONDS, new Random().nextInt(5000));
        
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
            executionDAOFacade,
            queueDAO,
            metadataDAO,
            conductorProperties,
            new WorkflowExecutor(executionDAOFacade, queueDAO, metadataDAO)
        );
    }

    @Test
    public void testExecute() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(executionDAOFacade, times(1)).getTaskModel(captor.capture());
        assertNotNull(captor.getValue());
    }

    @Test
    public void testExecuteWithException() {
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(executionDAOFacade, times(1)).getTaskModel(captor.capture());
        assertNull(captor.getValue());
    }
}
