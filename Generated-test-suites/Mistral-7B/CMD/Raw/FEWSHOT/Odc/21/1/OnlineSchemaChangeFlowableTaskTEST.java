package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.context.Context;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

/**
 * Unit tests for OnlineSchemaChangeFlowableTask
 *
 * @author yaobin
 */
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private TaskService taskService;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private QuartzJobService quartzJobService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private OscTaskCompleteHandler taskHandler;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Context.setProcessEngineConfiguration(mockProcessEngineConfiguration());
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);
        String schema = "test_schema";

        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);

        verify(scheduleService, times(1)).create(scheduleEntity);
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = mock(OnlineSchemaChangeScheduleTaskParameters.class);

        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

        verify(scheduleTaskRepository, times(1)).saveAndFlush(scheduleTaskEntity);
    }

    @Test
    public void testStart() throws Exception {
        Long taskId = 1L;
        TaskEntity flowTask = mock(TaskEntity.class);
        DelegateExecution execution = mock(DelegateExecution.class);
        User creator = mock(User.class);
        OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "test_schema";

        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(creator);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schema);

        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

        verify(taskService, times(1)).start(taskId);
        verify(organizationService, times(1)).get(creator.getId());
        verify(organizationService, times(1)).get(creator.getOrganizationId());
        verify(onlineSchemaChangeFlowableTask, times(1)).createScheduleEntity(connectionConfig, parameter, schema);
        verify(taskHandler, times(1)).start(scheduleId, tasks.get(0).getId());
        verifyNoMoreInteractions(taskService, organizationService, onlineSchemaChangeFlowableTask);
    }

    @Test
    public void testIsSuccessful() {
        TaskStatus status = TaskStatus.DONE;

        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        TaskStatus status = TaskStatus.FAILED;

        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);

        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        verify(taskService, times(1)).onFailure(taskId, taskService);
    }

    @Test
    public void testOnSuccessful() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);

        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        verify(taskService, times(1)).update(flowTask);
    }

    @Test
    public void testOnTimeout() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);

        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        verify(taskService, times(1)).onTimeout(taskId, taskService);
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        Page<ScheduleTaskEntity> tasks = mock(Page.class);

        when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(tasks);

        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

        verify(tasks, times(1)).stream();
        verify(onlineSchemaChangeFlowableTask, times(1)).progressStatusUpdate(tasks);
        verify(onlineSchemaChangeFlowableTask, times(1)).singleTaskPercentage(tasks.get(0));
        verify(onlineSchemaChangeFlowableTask, times(1)).percentage = currentPercentage;
        verify(flowTask, times(1)).setResultJson(JsonUtils.toJson(new OnlineSchemaChangeTaskResult(tasks.getContent())));
        verify(flowTask, times(1)).setStatus(status);
        verify(flowTask, times(1)).setProgressPercentage(percentage);
        verify(taskService, times(1)).update(flowTask);
    }

    @Test
    public void testCancel() throws Exception {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);

        when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(tasks);

        onlineSchemaChangeFlowableTask.cancel(true, taskId, taskService);

        verify(taskService, times(1)).cancel(taskId);
        verify(quartzJobService, times(1)).terminate(scheduleId, tasks.get(0).getId());
        verify(onlineSchemaChangeFlowableTask, times(1)).status = TaskStatus.CANCELED;
    }

    @Test
    public void testIsCancelled() {
        TaskStatus status = TaskStatus.CANCELED;

        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    private ProcessEngineConfiguration mockProcessEngineConfiguration() {
        ProcessEngineConfiguration config = mock(ProcessEngineConfiguration.class);
        when(config.getActivityExecutor()).thenReturn(null);
        return config;
    }

    private List<ScheduleTaskEntity> tasks() {
        return Arrays.asList(mock(ScheduleTaskEntity.class), mock(ScheduleTaskEntity.class));
    }
}