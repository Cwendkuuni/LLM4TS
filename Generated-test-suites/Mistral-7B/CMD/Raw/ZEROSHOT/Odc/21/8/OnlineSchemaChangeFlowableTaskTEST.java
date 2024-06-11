package com.oceanbase.odc.service.onlineschemachange.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange. OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.task.TaskService;

class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask testSubject;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private QuartzJobService quartzJobService;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @Mock
    private TaskService taskService;

    @Mock
    private BaseODCFlowTaskDelegate baseODCFlowTaskDelegate;

    @Mock
    private FlowTaskUtil flowTaskUtil;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(taskService.list(Long.class, Pageable.unpaged())).thenReturn(Page.empty());
        when(taskService.detail(Long.class, Long.class)).thenReturn(new TaskEntity());
        when(flowTaskUtil.getCloudMainAccountId(DelegateExecution.class)).thenReturn("uid");
        when(flowTaskUtil.getOnlineSchemaChangeParameter(DelegateExecution.class)).thenReturn(new OnlineSchemaChangeParameters());
        when(flowTaskUtil.getConnectionConfig(DelegateExecution.class)).thenReturn(new ConnectionConfig());
        when(flowTaskUtil.getSchemaName(DelegateExecution.class)).thenReturn("schema");
        when(organizationService.findById(Long.class)).thenReturn(new User());
        when(scheduleTaskRepository.saveAndFlush(ScheduleTaskEntity.class)).thenAnswer(i -> i.getArguments()[0]);
        when(quartzJobService.scheduleJob(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, 1L, "jobName", "jobGroup"))
                .thenReturn(new Object());
        when(scheduleService.create(ScheduleEntity.class)).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    public void testStart() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "schema";
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setStatus(TaskStatus.RUNNING);

        long flowTaskId = 1L;
        when(taskService.start(Long.class)).thenReturn(taskEntity);
        when(taskService.detail(Long.class, Long.class)).thenReturn(taskEntity);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setConnectionId(connectionConfig.getId());
        scheduleEntity.setDatabaseName(schema);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setStatus(ScheduleStatus.ENABLED);
        scheduleEntity.setAllowConcurrent(false);
        scheduleEntity.setCreatorId(1L);
        scheduleEntity.setOrganizationId(1L);
        scheduleEntity.setProjectId(1L);
        scheduleEntity.setDatabaseId(1L);
        scheduleEntity.setModifierId(scheduleEntity.getCreatorId());
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
        triggerConfig.setCronExpression("0/10 * * * * ?");
        scheduleEntity.setTriggerConfigJson(JsonUtils.toJson(triggerConfig));
        scheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(parameter));
        when(scheduleService.create(ScheduleEntity.class)).thenReturn(scheduleEntity);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParametersList = List.of(new OnlineSchemaChangeScheduleTaskParameters());
        List<ScheduleTaskEntity> scheduleTasks = subTaskParametersList.stream().map(param -> {
            ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
            scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
            scheduleTaskEntity.setJobName(scheduleEntity.getId().toString());
            scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
            scheduleTaskEntity.setProgressPercentage(0.0);
            scheduleTaskEntity.setParametersJson(JsonUtils.toJson(param));
            scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(param)));
            scheduleTaskEntity.setFireTime(new Date());
            return scheduleTaskEntity;
        }).collect(Collectors.toList());

        when(taskHandler.start(Long.class, Long.class)).thenReturn(true);

        testSubject.start(flowTaskId, taskService, new DelegateExecution());

        TaskStatus actualStatus = testSubject.getStatus();
        long actualScheduleId = testSubject.getScheduleId();
        long actualCreatorId = testSubject.getCreatorId();
        long actualOrganizationId = testSubject.getOrganizationId();
        boolean actualContinueOnError = testSubject.isContinueOnError();

        assertThat(actualStatus).isEqualTo(TaskStatus.RUNNING);
        assertThat(actualScheduleId).isEqualTo(scheduleEntity.getId());
        assertThat(actualCreatorId).isEqualTo(scheduleEntity.getCreatorId());
        assertThat(actualOrganizationId).isEqualTo(scheduleEntity.getOrganizationId());
        assertThat(actualContinueOnError).isEqualTo(parameter.isContinueOnError());
    }

    @Test
    public void testGetStatus() {
        TaskStatus expectedStatus = TaskStatus.DONE;
        when(taskService.detail(Long.class, Long.class)).thenReturn(new TaskEntity().setStatus(expectedStatus));
        TaskStatus actualStatus = testSubject.getStatus();
        verify(taskService).detail(Long.class, Long.class);
        assertThat(actualStatus).isEqualTo(expectedStatus);
    }

    @Test
    public void testIsFailure() {
        TaskStatus expectedStatus = TaskStatus.FAILED;
        when(taskService.detail(Long.class, Long.class)).thenReturn(new TaskEntity().setStatus(expectedStatus));
        boolean actualResult = testSubject.isFailure();
        verify(taskService).detail(Long.class, Long.class);
        assertThat(actualResult).isTrue();
    }

    @Test
    public void testIsSuccessful() {
        TaskStatus expectedStatus = TaskStatus.DONE;
        when(taskService.detail(Long.class, Long.class)).thenReturn(new TaskEntity().setStatus(expectedStatus));
        boolean actualResult = testSubject.isSuccessful();
        verify(taskService).detail(Long.class, Long.class);
        assertThat(actualResult).isTrue();
    }

    @Test
    public void testIsCancelled() {
        TaskStatus expectedStatus = TaskStatus.CANCELED;
        when(taskService.detail(Long.class, Long.class)).thenReturn(new TaskEntity().setStatus(expectedStatus));
        boolean actualResult = testSubject.isCancelled();
        verify(taskService).detail(Long.class, Long.class);
        assertThat(actualResult).isTrue();
    }

    @Test
    public void testCancel() {
        boolean expectedResult = true;
        boolean actualResult = testSubject.cancel(true, 1L, taskService);
        verify(taskService).cancel(1L);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void testProgressUpdate() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "schema";

        Page<ScheduleTaskEntity> scheduleTasks = Page.of(new ScheduleTaskEntity().setStatus(TaskStatus.RUNNING),
                new ScheduleTaskEntity().setStatus(TaskStatus.FAILED), new ScheduleTaskEntity().setStatus(TaskStatus.CANCELED),
                new ScheduleTaskEntity().setStatus(TaskStatus.PREPARING));

        when(taskService.detail(Long.class, Long.class)).thenReturn(new TaskEntity().setId(1L));
        when(taskService.list(Pageable.unpaged(), Long.class)).thenReturn(scheduleTasks);

        testSubject.progressUpdate(taskService);

        double actualPercentage = testSubject.getPercentage();
        TaskStatus actualStatus = testSubject.getStatus();

        assertThat(actualPercentage).isGreaterThanOrEqualTo(33.0);
        assertThat(actualStatus).isIn(TaskStatus.DONE, TaskStatus.FAILED);
    }

}