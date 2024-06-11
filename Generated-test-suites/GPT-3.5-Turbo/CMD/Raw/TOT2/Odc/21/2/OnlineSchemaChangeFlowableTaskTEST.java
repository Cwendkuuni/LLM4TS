
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Test
    public void testStart() throws Exception {
        Long taskId = 1L;
        DelegateExecution execution = mock(DelegateExecution.class);
        TaskService taskService = mock(TaskService.class);

        when(taskService.detail(anyLong())).thenReturn(new TaskEntity());

        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

        verify(taskService, times(1)).start(taskId);
        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testProgressUpdate() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setStatus(TaskStatus.RUNNING);
        taskEntity.setProgressPercentage(50.0);

        Page<ScheduleTaskEntity> tasks = mock(Page.class);
        when(tasks.getSize()).thenReturn(1);
        when(tasks.getContent()).thenReturn(Collections.singletonList(taskEntity));

        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);

        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
        assertEquals(50.0, onlineSchemaChangeFlowableTask.getPercentage(), 0.0);
    }

    @Test
    public void testCancel() {
        Long taskId = 1L;

        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(Collections.singletonList(new ScheduleTaskEntity()));
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong(), anyLong(), anySet())).thenReturn(Collections.singletonList(new ScheduleTaskEntity()));

        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, taskId, taskService));
        assertEquals(TaskStatus.CANCELED, onlineSchemaChangeFlowableTask.getStatus());
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);

        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }
}
