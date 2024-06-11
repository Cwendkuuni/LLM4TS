package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeContextHolder;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.JobBuilder;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskResult;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.task.model.TaskResult;

@Slf4j
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
    private FlowTaskUtil flowTaskUtil;
    @Mock
    private OscTaskCompleteHandler oscTaskCompleteHandler;
    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private TaskEntity taskEntity;
    @Mock
    private DelegateExecution delegateExecution;
    @Mock
    private TaskService taskService;

    @Mock
    private ScheduleEntity scheduleEntity;
    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;
    @Mock
    private OnlineSchemaChangeParameters parameter;
    @Mock
    private OnlineSchemaChangeScheduleTaskParameters param;
    @Mock
    private TriggerConfig triggerConfig;
    @Mock
    private OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult;
    @Mock
    private OnlineSchemaChangeScheduleTaskResult onlineSchemaChangeScheduleTaskResult;
    @Mock
    private ScheduleTaskEntity runningEntity;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void init() {
        when(scheduleTaskRepository.saveAndFlush(scheduleTaskEntity)).thenReturn(scheduleTaskEntity);
        when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(Page.empty());
    }

    @Test
    public void testCreateScheduleEntity() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);
        String schema = "schemaName";
        when(connectionConfig.id()).thenReturn(1L);
        when(parameter.getDatabaseName()).thenReturn(schema);
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(1L, scheduleEntity.getCreatorId());
        assertEquals(1L, scheduleEntity.getOrganizationId());
        assertEquals("", scheduleEntity.getProjectId());
        assertEquals("", scheduleEntity.getDatabaseId());
        assertEquals(0L, scheduleEntity.getModifierId());
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
        triggerConfig.setCronExpression("0/10 * * * * ?");
        assertEquals(triggerConfig.toString(), scheduleEntity.getTriggerConfigJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING.name(), scheduleEntity.getMisfireStrategy());
        assertEquals(JsonUtils.toJson(parameter).toString(), scheduleEntity.getJobParametersJson());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);
        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("", scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage());
        assertEquals(JsonUtils.toJson(param).toString(), scheduleTaskEntity.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(param)).toString(), scheduleTaskEntity.getResultJson());
        assertEquals(new Date(), scheduleTaskEntity.getFireTime());
    }

    @Test
    public void testStart() throws Exception {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        TaskService taskService = mock(TaskService.class);
        DelegateExecution execution = mock(DelegateExecution.class);
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);
        String schema = "schemaName";
        when(flowTaskUtil.getTaskCreator(execution)).thenReturn(mock(User.class));
        when(flowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        when(flowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(flowTaskUtil.getSchemaName(execution)).thenReturn(schema);
        when(organizationService.findById(1L)).thenReturn(mock(User.class));
        when(taskService.detail(anyLong())).thenReturn(taskEntity);
        when(taskService.start(anyLong())).thenReturnVoid();

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();
        taskParam.setUid("uid");
        taskParameters.add(taskParam);
        when(parameter.generateSubTaskParameters(connectionConfig, schema)).thenReturn(taskParameters);

        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        assertEquals(TaskStatus.RUNNING, taskEntity.getStatus());
    }

    @Test
    public void testIsSuccessful() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        assertEquals(TaskStatus.DONE, onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        assertEquals(TaskStatus.FAILED, onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() throws Exception {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        TaskService taskService = mock(TaskService.class);
        Long taskId = 1L;
        when(taskService.cancel(taskId)).thenThrow(new RuntimeException());
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);
        assertTrue(log.isWarnEnabled());
    }

    @Test
    public void testOnSuccessful() throws Exception {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        TaskService taskService = mock(TaskService.class);
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult(new ArrayList<>());
        when(taskService.update(taskEntity)).thenReturn(taskEntity);
        when(taskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        when(taskEntity.getResultJson()).thenReturn("");
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        when(scheduleTaskEntity.getResultJson()).thenReturn(JsonUtils.toJson(onlineSchemaChangeTaskResult));
        when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(Page.of(scheduleTaskEntity));
        when(quartzJobService.buildJob(any(JobBuilder.class))).thenReturn(mock(org.quartz.JobDetail.class));
        when(quartzJobService.scheduleJob(any())).thenReturn(mock(org.quartz.Scheduler.Trigger.class));
        OnlineSchemaChangeFlowableTask.OnlineSchemaChangeTaskCompletionListener completionListener = mock(OnlineSchemaChangeFlowableTask.OnlineSchemaChangeTaskCompletionListener.class);
        onlineSchemaChangeFlowableTask.setOnlineSchemaChangeTaskCompletionListener(completionListener);

        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
        assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
        assertEquals(FlowStatus.EXECUTION_SUCCEEDED, taskEntity.getFlowInstance().getStatus());
        verify(completionListener).onCompletion(taskId);
    }

    @Test
    public void testOnTimeout() throws Exception {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        TaskService taskService = mock(TaskService.class);
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
        assertTrue(log.isWarnEnabled());
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        TaskService taskService = mock(TaskService.class);
        Page<ScheduleTaskEntity> tasks = Page.of(scheduleTaskEntity, runningEntity);
        when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(tasks);
        when(singleTaskPercentage(runningEntity)).thenReturn(0.5);

        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        assertEquals(1.0, onlineSchemaChangeFlowableTask.percentage);
    }

    @Test
    public void testCancel() throws Exception {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        TaskService taskService = mock(TaskService.class);
        OnlineSchemaChangeFlowableTask.OnlineSchemaChangeTaskCompletionListener completionListener = mock(OnlineSchemaChangeFlowableTask.OnlineSchemaChangeTaskCompletionListener.class);
        onlineSchemaChangeFlowableTask.setOnlineSchemaChangeTaskCompletionListener(completionListener);

        onlineSchemaChangeFlowableTask.cancel(false, 1L, taskService);

        assertTrue(log.isWarnEnabled());
        verify(completionListener).onCancel(1L);
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        assertEquals(false, onlineSchemaChangeFlowableTask.isCancelled());
    }
}