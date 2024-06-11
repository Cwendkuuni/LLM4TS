package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
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
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.task.model.TaskExecution;
import com.oceanbase.odc.service.task.model.TaskInstance;

@SuppressWarnings("unchecked")
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
    private OscTaskCompleteHandler taskHandler;
    @Mock
    private ConnectionConfig connectionConfig;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask task;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        TaskService taskService = mock(TaskService.class);
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());
        when(quartzJobService.scheduleJob(anyString(), anyString(), any(), any(TriggerConfig.class), any(MisfireStrategy.class)))
                .thenReturn(true);

        when(taskHandler.start(anyLong(), anyLong())).thenReturn(true);
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(new User());
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(new OnlineSchemaChangeParameters());
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn("test_schema");
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        task.start(1L, taskService, execution);

        verify(taskService, times(1)).start(1L);
        ArgumentCaptor<TaskExecution> taskExecutionArgumentCaptor = ArgumentCaptor.forClass(TaskExecution.class);
        verify(taskService, times(1)).start(eq(1L), taskExecutionArgumentCaptor.capture());
        TaskExecution taskExecution = taskExecutionArgumentCaptor.getValue();
        assertEquals(TaskStatus.RUNNING, taskExecution.getStatus());
        assertEquals(1, taskExecution.getTaskId());
        assertNull(taskExecution.getResult());

        verify(scheduleService, times(1)).create(any(ScheduleEntity.class));
        verify(quartzJobService, times(1)).scheduleJob("test_job_name-1", "test_job_group", any(Runnable.class),
                any(TriggerConfig.class), any(MisfireStrategy.class));
        verify(taskHandler, times(1)).start(1L, 1L);
    }

    @Test
    public void testIsSuccessful() {
        task.status = TaskStatus.DONE;
        assertTrue(task.isSuccessful());

        task.status = TaskStatus.FAILED;
        assertFalse(task.isSuccessful());

        task.status = TaskStatus.RUNNING;
        assertFalse(task.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        task.status = TaskStatus.DONE;
        assertFalse(task.isFailure());

        task.status = TaskStatus.FAILED;
        assertTrue(task.isFailure());

        task.status = TaskStatus.RUNNING;
        assertFalse(task.isFailure());
    }

    @Test
    public void testOnFailure() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        task.onFailure(taskId, taskService);
        verify(taskService, times(1)).cancel(taskId);
    }

    @Test
    public void testOnSuccessful() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        task.onSuccessful(taskId, taskService);
        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testOnTimeout() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        task.onTimeout(taskId, taskService);
        verify(taskService, times(1)).cancel(taskId);
    }

    @Test
    public void testOnProgressUpdate() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        Page<ScheduleTaskEntity> scheduleTasks = Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity()).stream()
                .collect(Collectors.toCollection(Page::new));

        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(scheduleTasks);

        task.onProgressUpdate(taskId, taskService);

        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);
        String schema = "test_schema";
        ScheduleEntity scheduleEntity = task.createScheduleEntity(connectionConfig, parameter, schema);

        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(false, scheduleEntity.isAllowConcurrent());
        assertEquals(creatorId, scheduleEntity.getCreatorId());
        assertEquals(organizationId, scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(triggerStrategy(TriggerStrategy.CRON), scheduleEntity.getTriggerConfigJson());
        assertEquals(checkTaskCronExpression, scheduleEntity.getTriggerConfigJson().getTriggerExpression());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, scheduleEntity.getMisfireStrategy());
        assertEquals(JsonUtils.toJson(parameter), scheduleEntity.getJobParametersJson());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeScheduleTaskParameters param = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        Long scheduleId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = task.createScheduleTaskEntity(scheduleId, param);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("test_job_name-1", scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage());
        assertEquals(JsonUtils.toJson(param), scheduleTaskEntity.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(param)), scheduleTaskEntity.getResultJson());
        assertEquals(new Date(), scheduleTaskEntity.getFireTime());
    }

    private TriggerConfig triggerStrategy(TriggerStrategy triggerStrategy) {
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setCronExpression(checkTaskCronExpression);
        triggerConfig.setTriggerStrategy(triggerStrategy);
        return triggerConfig;
    }
}