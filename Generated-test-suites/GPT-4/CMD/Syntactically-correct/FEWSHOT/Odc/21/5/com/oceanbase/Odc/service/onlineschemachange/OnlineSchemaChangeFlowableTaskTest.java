package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Arrays;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.quartz.QuartzJobService;
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
    private DelegateExecution execution;
    @Mock
    private TaskService taskService;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;


    @Test
    public void testStart() throws Exception {
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(taskService.detail(anyLong())).thenReturn(taskEntity);

        User user = new User();
        user.setId(1L);
        user.setOrganizationId(1L);

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setContinueOnError(true);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);

        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(user);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameters);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);

        assertNull(onlineSchemaChangeFlowableTask.start(1L, taskService, execution));
    }

    @Test
    public void testOnProgressUpdate() {
        PageImpl<ScheduleTaskEntity> tasks = new PageImpl<>(new ArrayList<>());
        when(scheduleTaskService.listTask(any(), anyLong())).thenReturn(tasks);

        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        verify(scheduleTaskService, times(1)).listTask(any(), anyLong());
    }
}