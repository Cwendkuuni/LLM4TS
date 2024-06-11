import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.task.TaskService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask task;
    private TaskService mockTaskService;
    private DelegateExecution mockDelegateExecution;

    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setUp() {
        task = new OnlineSchemaChangeFlowableTask();
        mockTaskService = mock(TaskService.class);
        mockDelegateExecution = mock(DelegateExecution.class);

        task.setTask(mockTaskService);
    }

    @Test
    public void testStart() throws Exception {
        when(mockTaskService.start(anyLong())).thenReturn(null);
        task.start(1L, mockTaskService, mockDelegateExecution);
        verify(mockTaskService).start(anyLong());
    }

    @Test
    public void testIsSuccessful() {
        boolean result = task.isSuccessful();
        assertEquals(false, result);
    }

    @Test
    public void testIsFailure() {
        boolean result = task.isFailure();
        assertEquals(false, result);
    }

    @Test
    public void testOnFailure() {
        task.onFailure(1L, mockTaskService);
        verify(mockTaskService).onFailure(anyLong());
    }

    @Test
    public void testOnSuccessful() {
        task.onSuccessful(1L, mockTaskService);
        verify(mockTaskService).onSuccessful(anyLong());
    }

    @Test
    public void testOnTimeout() {
        task.onTimeout(1L, mockTaskService);
        verifyNoMoreInteractions(mockTaskService);
    }

    @Test
    public void testOnProgressUpdate() {
        task.onProgressUpdate(1L, mockTaskService);
        verify(mockTaskService).detail(anyLong());
    }

    @Test
    public void testCancel() {
        boolean result = task.cancel(true, 1L, mockTaskService);
        assertEquals(true, result);
    }

    @Test
    public void testIsCancelled() {
        boolean result = task.isCancelled();
        assertEquals(false, result);
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        ScheduleEntity scheduleEntity = task.createScheduleEntity(connectionConfig, parameter, schema);
        assertNotNull(scheduleEntity);
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTaskEntity = task.createScheduleTaskEntity(scheduleId, param);
        assertNotNull(scheduleTaskEntity);
    }
}