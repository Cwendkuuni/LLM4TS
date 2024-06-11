package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
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
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

/**
 * Test for OnlineSchemaChangeFlowableTask class
 *
 * @author yaobin
 */
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
    private TaskService taskService;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Mock
    private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;

    @Mock
    private User user;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private TaskEntity taskEntity;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateScheduleEntity() {
        when(connectionConfig.id()).thenReturn(1L);
        when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);

        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "schema_name";
        OnlineSchemaChangeContextHolder.set(new Object[]{creatorId, 1L, 1L});

        OnlineSchemaChangeContextHolder.clear();
        ScheduleEntity result = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);

        verify(scheduleEntity).setConnectionId(1L);
        verify(scheduleEntity).setDatabaseName(schema);
        verify(scheduleEntity).setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        verify(scheduleEntity).setStatus(ScheduleStatus.ENABLED);
        verify(scheduleEntity).setAllowConcurrent(false);
        verify(scheduleEntity).setCreatorId(creatorId);
        verify(scheduleEntity).setOrganizationId(1L);
        verify(scheduleEntity).setProjectId(1L);
        verify(scheduleEntity).setDatabaseId(1L);
        verify(scheduleEntity).setModifierId(creatorId);
        verify(scheduleEntity).setTriggerConfigJson(containsString(JsonUtils.toJson(new TriggerConfig())));
        verify(scheduleEntity).setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
        verify(scheduleEntity).setJobParametersJson(containsString(JsonUtils.toJson(parameter)));
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        when(scheduleTaskRepository.saveAndFlush(scheduleTaskEntity)).thenReturn(scheduleTaskEntity);

        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

        ScheduleTaskEntity result = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, param);

        verify(scheduleTaskEntity).setStatus(TaskStatus.PREPARING);
        verify(scheduleTaskEntity).setJobName("1");
        verify(scheduleTaskEntity).setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        verify(scheduleTaskEntity).setProgressPercentage(0.0);
        verify(scheduleTaskEntity).setParametersJson(containsString(JsonUtils.toJson(param)));
        verify(scheduleTaskEntity).setResultJson(containsString(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(param))));
        verify(scheduleTaskEntity).setFireTime(any(Date.class));
    }

    @Test
    public void testStart() throws Exception {
        when(taskService.start(anyLong())).thenReturn(true);
        when(FlowTaskUtil.getTaskCreator(delegateExecution)).thenReturn(user);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(delegateExecution)).thenReturn(onlineSchemaChangeParameters);
        when(FlowTaskUtil.getConnectionConfig(delegateExecution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(delegateExecution)).thenReturn("schema_name");
        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(Page.empty());

        onlineSchemaChangeFlowableTask.start(1L, taskService, delegateExecution);

        verify(taskService).start(1L);
    }

    @Test
    public void testIsSuccessful() {
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testCancel() throws Exception {
        when(taskService.cancel(anyLong())).thenReturn(true);
        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(Page.of(scheduleTaskEntity));

        onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        verify(taskService).cancel(1L);
        verify(taskHandler).terminate(1L, scheduleTaskEntity.getId());
    }

    @Test
    public void testListScheduleTasks() throws Exception {
        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(Page.of(scheduleTaskEntity));

        List<ScheduleTaskEntity> result = onlineSchemaChangeFlowableTask.listScheduleTasks(1L);

        verify(scheduleTaskService).listTask(Pageable.unpaged(), 1L);

        assertEquals(1, result.size());
        assertEquals(scheduleTaskEntity, result.get(0));
    }

    @Test
    public void testUpdateProgress() throws Exception {
        when(taskService.detail(anyLong())).thenReturn(taskEntity);
        when(taskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(Page.of(scheduleTaskEntity));

        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        verify(taskEntity).setResultJson(anyString());
        verify(taskEntity).setStatus(TaskStatus.DONE);
        verify(taskEntity).setProgressPercentage(100);
    }
}