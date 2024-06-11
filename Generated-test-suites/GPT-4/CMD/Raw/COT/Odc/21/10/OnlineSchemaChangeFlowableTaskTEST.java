import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.service.task.TaskService;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private TaskService mockTaskService;
    private DelegateExecution mockExecution;

    @Before
    public void setup() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        mockTaskService = mock(TaskService.class);
        mockExecution = mock(DelegateExecution.class);
    }

    @Test
    public void testStart() throws Exception {
        assertNull(onlineSchemaChangeFlowableTask.start(1L, mockTaskService, mockExecution));
    }

    @Test
    public void testIsSuccessful() {
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        onlineSchemaChangeFlowableTask.onFailure(1L, mockTaskService);
    }

    @Test
    public void testOnSuccessful() {
        onlineSchemaChangeFlowableTask.onSuccessful(1L, mockTaskService);
    }

    @Test
    public void testOnTimeout() {
        onlineSchemaChangeFlowableTask.onTimeout(1L, mockTaskService);
    }

    @Test
    public void testOnProgressUpdate() {
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, mockTaskService);
    }

    @Test
    public void testCancel() {
        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, mockTaskService));
    }

    @Test
    public void testIsCancelled() {
        assertFalse(onlineSchemaChangeFlowableTask.isCancelled());
    }
}