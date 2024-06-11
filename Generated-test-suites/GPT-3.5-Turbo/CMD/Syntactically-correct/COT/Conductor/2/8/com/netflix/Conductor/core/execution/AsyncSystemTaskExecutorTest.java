
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";

        doNothing().when(asyncSystemTaskExecutor).execute(systemTask, taskId);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(asyncSystemTaskExecutor, times(1)).execute(systemTask, taskId);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "testQueue";
        TaskModel task = mock(TaskModel.class);

        doNothing().when(asyncSystemTaskExecutor).postponeQuietly(queueName, task);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(asyncSystemTaskExecutor, times(1)).postponeQuietly(queueName, task);
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "123";

        when(asyncSystemTaskExecutor.loadTaskQuietly(taskId)).thenReturn(null);

        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(asyncSystemTaskExecutor, times(1)).loadTaskQuietly(taskId);
    }
}
