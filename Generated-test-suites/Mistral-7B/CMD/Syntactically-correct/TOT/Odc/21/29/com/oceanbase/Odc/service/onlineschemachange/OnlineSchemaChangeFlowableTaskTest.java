package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeTaskHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@Import(OnlineSchemaChangeFlowableTask.class)
public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private TaskService taskService;
    private OrganizationService organizationService;
    private ScheduleService scheduleService;
    private ScheduleTaskService scheduleTaskService;
    private QuartzJobService quartzJobService;

    @Autowired
    public void setOnlineSchemaChangeFlowableTask(OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask) {
        this.onlineSchemaChangeFlowableTask = onlineSchemaChangeFlowableTask;
    }

    @Bean
    public TaskService taskService() {
        return mock(TaskService.class);
    }

    @Bean
    public OrganizationService organizationService() {
        return mock(OrganizationService.class);
    }

    @Bean
    public ScheduleService scheduleService() {
        return mock(ScheduleService.class);
    }

    @Bean
    public ScheduleTaskService scheduleTaskService() {
        return mock(ScheduleTaskService.class);
    }

    @Bean
    public QuartzJobService quartzJobService() {
        return mock(QuartzJobService.class);
    }

    @Before
    public void setUp() {
        taskService = mock(TaskService.class);
        organizationService = mock(OrganizationService.class);
        scheduleService = mock(ScheduleService.class);
        scheduleTaskService = mock(ScheduleTaskService.class);
        quartzJobService = mock(QuartzJobService.class);

        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);

        when(taskService.detail(Mockito.anyLong())).thenReturn(new TaskEntity().setStatus(TaskStatus.APPROVED));
        when(organizationService.getCurrentUser()).thenReturn(new User().setId(1L).setOrganizationId(1L));
        when(scheduleService.create(Mockito.any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());
        when(scheduleTaskService.listTask(Pageable.unpaged(), Mockito.anyLong())).thenReturn(Mockito.mock(Page.class));
        when(quartzJobService.scheduleJob(Mockito.anyString(), Mockito.any(TriggerConfig.class), Mockito.anyLong()))
                .thenReturn(new Date());

        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setTaskService(taskService);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
    }

    @After
    public void tearDown() {
        onlineSchemaChangeFlowableTask = null;
    }

    @Test
    public void testStart() {
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);

        TaskEntity taskEntity = new TaskEntity().setId(1L);
        when(taskService.detail(Mockito.anyLong())).thenReturn(taskEntity);

        User creator = new User().setId(1L).setOrganizationId(1L);
        when(organizationService.getCurrentUser()).thenReturn(creator);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleService.create(Mockito.any(ScheduleEntity.class))).thenReturn(scheduleEntity);

        ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        when(quartzJobService.scheduleJob(Mockito.anyString(), Mockito.any(TriggerConfig.class), Mockito.anyLong()))
                .thenReturn(new Date());

        OnlineSchemaChangeTaskHandler taskHandler = Mockito.mock(OnlineSchemaChangeTaskHandler.class);
        when(taskHandler.start(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);
        onlineSchemaChangeFlowableTask.setTaskHandler(taskHandler);

        OnlineSchemaChangeContextHolder.trace(0L, 1L, 1L);

        OnlineSchemaChangeParameters testParameter = new OnlineSchemaChangeParameters();
        ConnectionConfig testConnectionConfig = new ConnectionConfig();
        testConnectionConfig.setId(2L);

        List<ScheduleTaskEntity> tests = testParameter.generateSubTaskParameters(testConnectionConfig, "test_schema")
                .stream()
                .map(param -> {
                    param.setUid("test_uid");
                    return createScheduleTaskEntity(scheduleEntity.getId(), param);
                }).collect(Collectors.toList());

        taskHandler.start(scheduleEntity.getId(), tests.get(0).getId());

        Assert.assertNotNull(onlineSchemaChangeFlowableTask.start(1L, taskService, Mockito.mock(DelegateExecution.class)));
    }

    private ScheduleTaskEntity createScheduleTaskEntity(Long scheduleId, OnlineSchemaChangeScheduleTaskParameters scheduleTaskParameters) {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
        scheduleTaskEntity.setJobName(scheduleId.toString());
        scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        scheduleTaskEntity.setProgressPercentage(0.0);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(scheduleTaskParameters));
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(scheduleTaskParameters)));
        scheduleTaskEntity.setFireTime(new Date());
        return scheduleTaskEntity;
    }

}