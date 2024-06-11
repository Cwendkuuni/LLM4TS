package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runner.TestExecutionListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.JobType;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskExecutionResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(PowerMockRunner.class)
@PrepareForTest({OnlineSchemaChangeFlowableTask.class, FlowTaskUtil.class})
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
    private OrganizationService organizationService;
    @Mock
    private TaskService taskService;
    @Mock
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(FlowTaskUtil.class);
        PowerMockito.when(FlowTaskUtil.getTaskCreator(any(DelegateExecution.class)))
                .thenReturn(createUser());
    }

    @Test
    public void testStart() throws Exception {
        // given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        when(organizationService.getOrganizationId(anyLong()))
                .thenReturn(1L);
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class)))
                .thenReturn(createScheduleTaskEntity(1L, parameter));
        when(scheduleService.create(any(ScheduleEntity.class)))
                .thenReturn(createScheduleEntity(connectionConfig, parameter, schema));

        // when
        DelegateExecution execution = mock(DelegateExecution.class);
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setScheduleTaskService(scheduleTaskService);
        task.setTaskService(taskService);
        task.setQuartzJobService(quartzJobService);
        task.setOrganizationService(organizationService);
        task.setScheduleTaskRepository(scheduleTaskRepository);
        task.setScheduleService(scheduleService);
        task.setConnectionConfig(connectionConfig);
        task.setOscTaskCompleteHandler(oscTaskCompleteHandler);
        task.start(1L, taskService, execution);

        // then
        verify(taskService, times(1)).start(1L);
        ArgumentCaptor<TaskExecutionResult> taskExecutionResultArgumentCaptor = ArgumentCaptor.forClass(TaskExecutionResult.class);
        verify(taskService, times(1)).update(taskExecutionResultArgumentCaptor.capture());
        TaskExecutionResult taskExecutionResult = taskExecutionResultArgumentCaptor.getValue();
        assertEquals(TaskStatus.RUNNING, taskExecutionResult.getStatus());
    }

    @Test(expected = Exception.class)
    public void testStartWithException() throws Exception {
        // given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        when(organizationService.getOrganizationId(anyLong()))
                .thenReturn(1L);
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class)))
                .thenThrow(new Exception("Test exception"));

        // when
        DelegateExecution execution = mock(DelegateExecution.class);
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setScheduleTaskService(scheduleTaskService);
        task.setTaskService(taskService);
        task.setQuartzJobService(quartzJobService);
        task.setOrganizationService(organizationService);
        task.setScheduleTaskRepository(scheduleTaskRepository);
        task.setScheduleService(scheduleService);
        task.setConnectionConfig(connectionConfig);
        task.setOscTaskCompleteHandler(oscTaskCompleteHandler);
        task.start(1L, taskService, execution);

        // then
        verify(taskService, never()).update(any(TaskExecutionResult.class));
        throw new Exception("Test exception");
    }

    @Test
    public void testIsSuccessful() {
        // given
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.DONE);

        // when
        boolean isSuccessful = task.isSuccessful();

        // then
        assertEquals(true, isSuccessful);
    }

    @Test
    public void testIsFailure() {
        // given
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.FAILED);

        // when
        boolean isFailure = task.isFailure();

        // then
        assertEquals(true, isFailure);
    }

    @Test
    public void testOnFailure() {
        // given
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        TaskService taskService = mock(TaskService.class);

        // when
        task.onFailure(1L, taskService);

        // then
        verify(taskService, times(1)).cancel(true, 1L, taskService);
    }

    @Test
    public void testOnSuccessful() {
        // given
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        TaskService taskService = mock(TaskService.class);

        // when
        task.onSuccessful(1L, taskService);

        // then
        verify(taskService, times(1)).update(any(TaskExecutionResult.class));
        verify(taskService, times(1)).update(any(TaskExecutionResult.class));
    }

    @Test
    public void testOnTimeout() {
        // given
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        TaskService taskService = mock(TaskService.class);

        // when
        task.onTimeout(1L, taskService);

        // then
        verify(taskService, times(1)).cancel(true, 1L, taskService);
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        // given
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        ScheduleTaskService scheduleTaskService = mock(ScheduleTaskService.class);
        TaskService taskService = mock(TaskService.class);
        List<ScheduleTaskEntity> tasks = ListUtils.emptyIfNull(createScheduleTasks(5));
        when(scheduleTaskService.listTask(any(Pageable.class), eq(1L)))
                .thenReturn(new PageImpl<>(tasks.stream().collect(Collectors.toList())));

        // when
        task.setScheduleTaskService(scheduleTaskService);
        task.setTaskService(taskService);
        task.onProgressUpdate(1L, taskService);

        // then
        for (ScheduleTaskEntity scheduleTaskEntity : tasks) {
            verify(taskService, times(1)).update(any(TaskExecutionResult.class));
        }
    }

    @Test
    public void testCancel() {
        // given
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        TaskService taskService = mock(TaskService.class);
        ScheduleTaskService scheduleTaskService = mock(ScheduleTaskService.class);
        doReturn(true).when(scheduleTaskService).terminate(eq(1L), eq(1L));

        // when
        task.setScheduleTaskService(scheduleTaskService);
        task.setTaskService(taskService);
        task.cancel(true, 1L, taskService);

        // then
        verify(taskService, times(1)).cancel(true, 1L, taskService);
    }

    @Test
    public void testIsCancelled() {
        // given
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.CANCELED);

        // when
        boolean isCancelled = task.isCancelled();

        // then
        assertEquals(true, isCancelled);
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setOrganizationId(1L);
        return user;
    }

    private ScheduleEntity createScheduleEntity(ConnectionConfig connectionConfig,
            OnlineSchemaChangeParameters parameter, String schema) {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setConnectionId(connectionConfig.id());
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
        return scheduleEntity;
    }

    private ScheduleTaskEntity createScheduleTaskEntity(Long scheduleId,
            OnlineSchemaChangeScheduleTaskParameters param) {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
        scheduleTaskEntity.setJobName(scheduleId.toString());
        scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        scheduleTaskEntity.setProgressPercentage(0.0);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(param));
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(param)));
        scheduleTaskEntity.setFireTime(new Date());
        return scheduleTaskEntity;
    }

    private List<ScheduleTaskEntity> createScheduleTasks(int size) {
        return ListUtils.emptyIfNull(IntStream.rangeClosed(1, size)
                .mapToObj(i -> createScheduleTaskEntity(1L, new OnlineSchemaChangeScheduleTaskParameters()))
                .collect(Collectors.toList()));
    }
}