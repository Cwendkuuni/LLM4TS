import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskInstance;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OnlineSchemaChangeFlowableTaskTest {

    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
    @Autowired
    private QuartzJobService quartzJobService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setTaskService(taskService);
        onlineSchemaChangeFlowableTask.setOscTaskCompleteHandler(oscTaskCompleteHandler);
    }

    @After
    public void cleanup() {
        // Clean up mocked objects
        verifyNoMoreInteractions(scheduleTaskRepository, quartzJobService, scheduleService, scheduleTaskService,
                taskService, oscTaskCompleteHandler);
    }

    @Test
    public void givenOnlineSchemaChangeFlowableTaskWhenStartIsCalledThenFlowTaskIsStartedAndScheduledTaskIsCreated() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
        String schema = "schema_name";

        when(onlineSchemaChangeFlowableTask.generateOnlineSchemaChangeParameters(any()))
                .thenReturn(onlineSchemaChangeParameters);
        when(onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schema))
                .thenReturn(Mockito.mock(ScheduleEntity.class));
        when(onlineSchemaChangeFlowableTask.createScheduleTaskEntity(anyLong(), any()))
                .thenReturn(Mockito.mock(ScheduleTaskEntity.class));

        ProcessInstance processInstance = Mockito.mock(ProcessInstance.class);
        TaskInstance taskInstance = Mockito.mock(TaskInstance.class);
        when(taskService.detail(anyLong())).thenReturn(taskInstance);
        when(taskService.start(anyLong())).thenReturn(processInstance);

        onlineSchemaChangeFlowableTask.start(1L, taskService, processInstance);

        verify(taskService, times(1)).start(1L);
        verify(quartzJobService, times(1)).scheduleJob(any(), any(), any());
    }

    @Test
    public void givenOnlineSchemaChangeFlowableTaskWhenTaskIsCancelledThenAllScheduledTasksAreCancelled() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
        String schema = "schema_name";

        when(onlineSchemaChangeFlowableTask.generateOnlineSchemaChangeParameters(any()))
                .thenReturn(onlineSchemaChangeParameters);
        when(onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schema))
                .thenReturn(Mockito.mock(ScheduleEntity.class));
        when(onlineSchemaChangeFlowableTask.createScheduleTaskEntity(anyLong(), any()))
                .thenReturn(ListUtils.unmodifiableList(Mockito.mock(ScheduleTaskEntity.class),
                        Mockito.mock(ScheduleTaskEntity.class)));

        onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        verify(quartzJobService, times(1)).cancelJob(anyLong());
        verify(scheduleTaskService, times(2)).cancelAllByScheduleId(anyLong());
    }

    @Test(expected = IllegalStateException.class)
    public void givenOnlineSchemaChangeFlowableTaskWhenTaskIsFailedThenFlowTaskIsFailed() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
        String schema = "schema_name";

        when(onlineSchemaChangeFlowableTask.generateOnlineSchemaChangeParameters(any()))
                .thenReturn(onlineSchemaChangeParameters);
        when(onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schema))
                .thenReturn(Mockito.mock(ScheduleEntity.class));
        when(onlineSchemaChangeFlowableTask.createScheduleTaskEntity(anyLong(), any()))
                .thenThrow(new IllegalStateException("Test exception"));

        onlineSchemaChangeFlowableTask.start(1L, taskService, null);

        fail("Should throw exception when task failed");
    }

    @Test
    public void givenOnlineSchemaChangeFlowableTaskWhenTaskIsSuccessfulThenFlowTaskIsCompleted() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
        String schema = "schema_name";

        when(onlineSchemaChangeFlowableTask.generateOnlineSchemaChangeParameters(any()))
                .thenReturn(onlineSchemaChangeParameters);
        when(onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schema))
                .thenReturn(Mockito.mock(ScheduleEntity.class));
        when(onlineSchemaChangeFlowableTask.createScheduleTaskEntity(anyLong(), any()))
                .thenReturn(Mockito.mock(ScheduleTaskEntity.class));

        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

        verify(taskService, times(1)).update(any());
    }
}