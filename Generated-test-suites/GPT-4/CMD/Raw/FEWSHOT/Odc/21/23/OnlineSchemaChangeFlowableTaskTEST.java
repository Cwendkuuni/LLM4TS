
package com.oceanbase.odc.service.onlineschemachange;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.iam.model.User;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
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
    private QuartzJobService quartzJobService;
    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private DelegateExecution execution;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setUp() {
        User user = new User();
        user.setId(1L);
        user.setOrganizationId(1L);

        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(user);
    }

    @Test
    public void startSuccessTest() throws Exception {
        TaskService taskService = mock(TaskService.class);
        long taskId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        String schemaName = "testSchema";
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);

        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(new OnlineSchemaChangeParameters());
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schemaName);
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(scheduleEntity);
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(taskEntity);

        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);
        verify(taskService).start(taskId);
    }

    @Test
    public void onProgressUpdateTest() throws Exception {
        TaskService taskService = mock(TaskService.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setStatus(TaskStatus.RUNNING);
        taskEntity.setProgressPercentage(50.0);

        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(new PageImpl<>(Arrays.asList(taskEntity)));

        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        verify(taskService).update(any(com.oceanbase.odc.metadb.task.TaskEntity.class));
    }
}
