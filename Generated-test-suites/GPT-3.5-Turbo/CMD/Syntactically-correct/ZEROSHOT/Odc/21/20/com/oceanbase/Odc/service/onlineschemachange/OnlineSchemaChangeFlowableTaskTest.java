import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskEntity;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private TaskService taskService;

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setUp() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setTaskService(taskService);
    }

    @Test
    public void testStart() throws Exception {
        when(scheduleTaskService.listTask(any(), anyLong())).thenReturn(any(Page.class));
        when(taskService.detail(anyLong())).thenReturn(new TaskEntity());

        onlineSchemaChangeFlowableTask.start(1L, taskService, any());
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.isSuccessful();
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.isFailure();
    }

    @Test
    public void testOnFailure() {
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
    }

    @Test
    public void testOnSuccessful() {
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
    }

    @Test
    public void testOnTimeout() {
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
    }

    @Test
    public void testOnProgressUpdate() {
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
    }

    @Test
    public void testCancel() {
        onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.isCancelled();
    }
}