package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.context.FlowableEngineContext;
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
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
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
import com.oceanbase.odc.service.task.model.TaskInstance;

@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

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
    private OrganizationService organizationService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FlowableEngineContext context = (FlowableEngineContext) mock(FlowableEngineContext.class);
        when(context.getEngineServices()).thenReturn(mock(TaskService.class));
        when(context.getEngineServices()).thenReturn(mock(TaskService.class));
        when(context.getEngineServices()).thenReturn(mock(QuartzJobService.class));
        when(context.getEngineServices()).thenReturn(mock(ScheduleService.class));
        when(context.getEngineServices()).thenReturn(mock(ScheduleTaskService.class));
        onlineSchemaChangeFlowableTask.setFlowableEngineContext(context);
    }

    @Test
    public void testStart() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId("1");
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskParameters scheduleParameters = new OnlineSchemaChangeScheduleTaskParameters();
        scheduleParameters.setSchemaName("testSchema");
        parameter.setGenerateSubTaskParameters(List.of(scheduleParameters));

        when(taskHandler.start(anyLong(), anyLong())).thenReturn(true);

        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());

        onlineSchemaChangeFlowableTask.start(1L, taskHandler, execution);

        verify(taskHandler).start(anyLong(), anyLong());
        verify(scheduleService).create(any(ScheduleEntity.class));
        verify(scheduleTaskRepository).saveAndFlush(any(ScheduleTaskEntity.class));
    }

    @Test
    public void testIsSuccessful() {
        TaskStatus status = TaskStatus.DONE;
        assertEquals(true, onlineSchemaChangeFlowableTask.isSuccessful(null, null));
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        assertEquals(false, onlineSchemaChangeFlowableTask.isSuccessful(null, null));
    }

    @Test
    public void testIsFailure() {
        TaskStatus status = TaskStatus.FAILED;
        assertEquals(true, onlineSchemaChangeFlowableTask.isFailure(null, null));
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        assertEquals(false, onlineSchemaChangeFlowableTask.isFailure(null, null));
    }

    @Test
    public void testOnFailure() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        when(taskService.cancel(taskId)).thenReturn(true);
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);
        verify(taskService).cancel(taskId);
    }

    @Test
    public void testOnSuccessful() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        TaskInstance taskInstance = new TaskInstance();
        taskInstance.setStatus(FlowStatus.EXECUTION_SUCCEEDED.getName());
        when(taskService.detail(taskId)).thenReturn(taskInstance);
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult(
                List.of(new ScheduleTaskEntity()));
        when(taskInstance.getResultJson()).thenReturn(JsonUtils.toJson(onlineSchemaChangeTaskResult));
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);
        verify(taskService).update(taskInstance);
    }

    @Test
    public void testOnTimeout() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);
        verify(taskService, never()).update(any(TaskInstance.class));
    }

    @Test
    public void testOnProgressUpdate() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        List<ScheduleTaskEntity> tasks = List.of(new ScheduleTaskEntity(), new ScheduleTaskEntity());
        Page<ScheduleTaskEntity> page = new PageImpl<>(tasks.stream().collect(Collectors.toList()));
        when(scheduleTaskService.listTask(any(Pageable.class), eq(1L))).thenReturn(page);
        onlineSchemaChangeFlowableTask.setPercentage(50.0);
        onlineSchemaChangeFlowableTask.percentageStatusUpdate(tasks);
        verify(taskService).update(any(TaskInstance.class));
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId("1");
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskParameters scheduleParameters = new OnlineSchemaChangeScheduleTaskParameters();
        scheduleParameters.setSchemaName("testSchema");
        parameter.setGenerateSubTaskParameters(List.of(scheduleParameters));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setConnectionId(connectionConfig.getId());
        scheduleEntity.setDatabaseName("testSchema");
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

        ScheduleEntity result = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter,
                "testSchema");
        assertEquals(scheduleEntity, result);
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters scheduleParameters = new OnlineSchemaChangeScheduleTaskParameters();

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
        scheduleTaskEntity.setJobName(scheduleId.toString());
        scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        scheduleTaskEntity.setProgressPercentage(0.0);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(scheduleParameters));
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(scheduleParameters)));
        scheduleTaskEntity.setFireTime(new Date());

        ScheduleTaskEntity result = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId,
                scheduleParameters);
        assertEquals(scheduleTaskEntity, result);
    }
}