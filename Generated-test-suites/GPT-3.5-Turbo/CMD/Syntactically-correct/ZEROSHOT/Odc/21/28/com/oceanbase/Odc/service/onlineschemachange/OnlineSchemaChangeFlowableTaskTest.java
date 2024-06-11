import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        onlineSchemaChangeFlowableTask = mock(OnlineSchemaChangeFlowableTask.class);
        when(onlineSchemaChangeFlowableTask.start(anyLong(), any(TaskService.class), any(DelegateExecution.class)))
                .thenReturn(null);
    }

    @Test
    public void testStart() throws Exception {
        onlineSchemaChangeFlowableTask.start(1L, new TaskService(), new DelegateExecution());
    }

    @Test
    public void testIsSuccessful() {
        when(onlineSchemaChangeFlowableTask.isSuccessful()).thenReturn(true);
    }

    @Test
    public void testIsFailure() {
        when(onlineSchemaChangeFlowableTask.isFailure()).thenReturn(false);
    }

    @Test
    public void testOnFailure() {
        onlineSchemaChangeFlowableTask.onFailure(1L, new TaskService());
    }

    @Test
    public void testOnSuccessful() {
        onlineSchemaChangeFlowableTask.onSuccessful(1L, new TaskService());
    }

    @Test
    public void testOnTimeout() {
        onlineSchemaChangeFlowableTask.onTimeout(1L, new TaskService());
    }

    @Test
    public void testOnProgressUpdate() {
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, new TaskService());
    }

    @Test
    public void testCancel() {
        when(onlineSchemaChangeFlowableTask.cancel(true, 1L, new TaskService())).thenReturn(true);
    }

    @Test
    public void testIsCancelled() {
        when(onlineSchemaChangeFlowableTask.isCancelled()).thenReturn(false);
    }

    @Test
    public void testCreateScheduleEntity() {
        onlineSchemaChangeFlowableTask.createScheduleEntity(new ConnectionConfig(), new OnlineSchemaChangeParameters(), "schema");
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, new OnlineSchemaChangeScheduleTaskParameters());
    }
}