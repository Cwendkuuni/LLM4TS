import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.*;

public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private DelegateExecution execution;

    @Mock
    private OnlineSchemaChangeScheduleTaskParameters parameters;

    @Mock
    private Pageable pageable;

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
    }

    @Test
    public void testStart() throws Exception {
        when(parameters.isContinueOnError()).thenReturn(true);

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        verify(taskService, times(1)).start(1L);
    }

    @Test
    public void testSuccessful() {
        onlineSchemaChangeFlowableTask.onSuccessful(1L,taskService);
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testFailure() {
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testTimeout() {
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
        verify(log, times(1)).warn(eq("Online schema change task timeout, taskId={}"), eq(1L));
    }

    @Test
    public void testProgressUpdate() {
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
        verify(scheduleTaskService, times(1)).listTask(pageable, scheduleId);
    }

    @Test
    public void testCancel() {
        onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);
        verify(scheduleTaskService, times(1)).listTask(pageable, scheduleId);
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

}