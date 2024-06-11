import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;

public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private TaskService taskServiceMock;
    private ScheduleTaskRepository scheduleTaskRepositoryMock;
    private ScheduleService scheduleServiceMock;
    private ScheduleTaskService scheduleTaskServiceMock;
    private ConnectionConfig connectionConfigMock;
    private OnlineSchemaChangeTaskHandler taskHandlerMock;
    private OscTaskCompleteHandler completeHandlerMock;

    @Before
    public void setUp() {
        taskServiceMock = mock(TaskService.class);
        scheduleTaskRepositoryMock = mock(ScheduleTaskRepository.class);
        scheduleServiceMock = mock(ScheduleService.class);
        scheduleTaskServiceMock = mock(ScheduleTaskService.class);
        connectionConfigMock = mock(ConnectionConfig.class);
        taskHandlerMock = mock(OnlineSchemaChangeTaskHandler.class);
        completeHandlerMock = mock(OscTaskCompleteHandler.class);

        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepositoryMock);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleServiceMock);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskServiceMock);
        onlineSchemaChangeFlowableTask.setTaskHandler(taskHandlerMock);
        onlineSchemaChangeFlowableTask.setCompleteHandler(completeHandlerMock);
    }

    @Test
    public void testStart() throws Exception {
        Long taskId = 1L;
        DelegateExecution executionMock = mock(DelegateExecution.class);
        when(taskServiceMock.detail(taskId)).thenReturn(new TaskEntity());
        when(taskServiceMock.start(taskId)).thenReturn(true);

        onlineSchemaChangeFlowableTask.start(taskId, taskServiceMock, executionMock);

        verify(taskServiceMock, times(1)).start(taskId);
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        Long taskId = 1L;
        when(taskServiceMock.detail(taskId)).thenReturn(new TaskEntity());
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskServiceMock);
        verify(taskServiceMock, times(1)).detail(taskId);
    }

    @Test
    public void testOnSuccessful() {
        Long taskId = 1L;
        when(taskServiceMock.detail(taskId)).thenReturn(new TaskEntity());
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskServiceMock);
        verify(taskServiceMock, times(1)).detail(taskId);
    }

    @Test
    public void testOnTimeout() {
        Long taskId = 1L;
        when(taskServiceMock.detail(taskId)).thenReturn(new TaskEntity());
        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskServiceMock);
        verify(taskServiceMock, times(1)).detail(taskId);
    }

    @Test
    public void testOnProgressUpdate() {
        Long taskId = 1L;
        when(taskServiceMock.detail(taskId)).thenReturn(new TaskEntity());
        when(scheduleTaskServiceMock.listTask(any(Pageable.class), anyLong())).thenReturn(new Page<>());
        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskServiceMock);
        verify(taskServiceMock, times(1)).detail(taskId);
        verify(scheduleTaskServiceMock, times(1)).listTask(any(Pageable.class), anyLong());
    }

    @Test
    public void testCancel() {
        Long taskId = 1L;
        when(scheduleTaskServiceMock.listTask(any(Pageable.class), anyLong())).thenReturn(new Page<>());
        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, taskId, taskServiceMock));
        verify(taskServiceMock, times(1)).cancel(taskId);
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }
}