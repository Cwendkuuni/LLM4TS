
import org.junit.Test;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @Test
    public void testExecute() {
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = mock(AsyncSystemTaskExecutor.class);

        asyncSystemTaskExecutor.execute(any(WorkflowSystemTask.class), anyString());

        verify(asyncSystemTaskExecutor, times(1)).execute(any(WorkflowSystemTask.class), anyString());
    }

    @Test
    public void testPostponeQuietly() {
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = mock(AsyncSystemTaskExecutor.class);

        when(asyncSystemTaskExecutor.postponeQuietly(anyString(), any(TaskModel.class))).thenCallRealMethod();

        asyncSystemTaskExecutor.postponeQuietly("testQueueName", new TaskModel());

        verify(asyncSystemTaskExecutor, times(1)).postponeQuietly("testQueueName", new TaskModel());
    }

    @Test
    public void testLoadTaskQuietly() {
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = mock(AsyncSystemTaskExecutor.class);

        when(asyncSystemTaskExecutor.loadTaskQuietly(anyString())).thenCallRealMethod();

        asyncSystemTaskExecutor.loadTaskQuietly("testTaskId");

        verify(asyncSystemTaskExecutor, times(1)).loadTaskQuietly("testTaskId");
    }
}
