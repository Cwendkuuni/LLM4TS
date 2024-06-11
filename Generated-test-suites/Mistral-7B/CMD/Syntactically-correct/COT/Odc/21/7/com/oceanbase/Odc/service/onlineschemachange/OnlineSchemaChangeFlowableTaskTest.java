package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
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
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;

/**
 * Test class for OnlineSchemaChangeFlowableTask
 *
 * @author yaobin
 */
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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        parameter.setId(1L);
        String schema = "test_schema";
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        DelegateExecution execution = mock(DelegateExecution.class);
        User user = new User();
        user.setId(1L);
        user.setOrganizationId(1L);
        FlowTaskUtil.whenGetTaskCreator(execution).thenReturn(user);
        FlowTaskUtil.whenGetOnlineSchemaChangeParameter(execution).thenReturn(parameter);
        FlowTaskUtil.whenGetConnectionConfig(execution).thenReturn(connectionConfig);
        FlowTaskUtil.whenGetSchemaName(execution).thenReturn(schema);

        // When
        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

        // Then
        TaskEntity flowTask = new TaskEntity();
        flowTask.setStatus(TaskStatus.RUNNING);
        flowTask.setId(taskId);
        taskService.start(taskId);
        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
        assertEquals(taskId, onlineSchemaChangeFlowableTask.getTaskId());
        assertEquals(1L, onlineSchemaChangeFlowableTask.getCreatorId());
        assertEquals(1L, onlineSchemaChangeFlowableTask.getOrganizationId());
        assertNull(onlineSchemaChangeFlowableTask.getScheduleId());
        OnlineSchemaChangeContextHolder.assertTraceEquals(1L, taskId, 1L);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setStatus(ScheduleStatus.ENABLED);
        scheduleEntity.setAllowConcurrent(false);
        scheduleEntity.setCreatorId(1L);
        scheduleEntity.setOrganizationId(1L);
        scheduleEntity.setProjectId(1L);
        scheduleEntity.setDatabaseId(1L);
        scheduleEntity.setDatabaseName(schema);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(parameter));
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
        triggerConfig.setCronExpression("0/10 * * * * ?");
        scheduleEntity.setTriggerConfigJson(JsonUtils.toJson(triggerConfig));
        scheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
        ScheduleEntity savedScheduleEntity = scheduleService.create(scheduleEntity);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(parameter)));
        scheduleTaskEntity.setJobName(scheduleEntity.getId().toString());
        scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        scheduleTaskEntity.setFireTime(new Date());
        List<ScheduleTaskEntity> scheduleTasks = Collections.singletonList(scheduleTaskEntity);

        Page<ScheduleTaskEntity> scheduleTaskPage = new PageImpl<>(scheduleTasks);

        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(scheduleTaskPage);
        when(taskHandler.start(anyLong(), anyLong())).thenReturn(true);

        // Callback after method execution
        verify(taskService).start(taskId);
        verify(taskHandler).start(1L, scheduleTaskEntity.getId());

        // Verify OnlineSchemaChangeContextHolder is cleared
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testIsSuccessful() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);

        // Then
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);

        // Then
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        // When
        onlineSchemaChangeFlowableTask.onFailure(1L, mock(TaskService.class));

        // Then
        verify(log).warn("Online schema change task failed, taskId={}", 1L);
    }

    @Test
    public void testOnSuccessful() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        // When
        onlineSchemaChangeFlowableTask.onSuccessful(1L, mock(TaskService.class));

        // Then
        verify(log).info("Online schema change task succeed, taskId={}", 1L);
        verify(onlineSchemaChangeFlowableTask).updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
    }

    @Test
    public void testOnTimeout() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        // When
        onlineSchemaChangeFlowableTask.onTimeout(1L, mock(TaskService.class));

        // Then
        verify(log).warn("Online schema change task timeout, taskId={}", 1L);
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.DONE);
        ScheduleTaskEntity doneTask = new ScheduleTaskEntity();
        doneTask.setStatus(TaskStatus.DONE);
        ScheduleTaskEntity failedTask = new ScheduleTaskEntity();
        failedTask.setStatus(TaskStatus.FAILED);
        OnlineSchemaChangeFlowableTask.OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        Page<ScheduleTaskEntity> scheduleTaskPage = new PageImpl<>(List.of(scheduleTaskEntity, failedTask));
        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(scheduleTaskPage);

        // When
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, mock(TaskService.class));

        // Then
        TaskEntity flowTask = new TaskEntity();
        flowTask.setStatus(TaskStatus.DONE);
        flowTask.setId(1L);
        flowTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeTaskResult(List.of(doneTask, failedTask))));
        verify(taskService).update(flowTask);
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testCancel() throws Exception {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        Page<ScheduleTaskEntity> scheduleTaskPage = new PageImpl<>(List.of(new ScheduleTaskEntity()));
        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(scheduleTaskPage);
        OnlineSchemaChangeFlowableTask.OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);

        // When
        boolean cancelled = onlineSchemaChangeFlowableTask.cancel(true, 1L, mock(TaskService.class));

        // Then
        assertTrue(cancelled);
        verify(taskHandler).terminate(1L, 1L);
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testIsCancelled() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);

        // Then
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        parameter.setId(1L);
        String schema = "test_schema";

        // When
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);

        // Then
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(1L, scheduleEntity.getCreatorId());
        assertEquals(1L, scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals(schema, scheduleEntity.getDatabaseName());
        assertEquals(JsonUtils.toJson(parameter), scheduleEntity.getJobParametersJson());
        assertNotNull(scheduleEntity.getTriggerConfigJson());
        assertNotNull(scheduleEntity.getMisfireStrategy());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        // Given
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

        // When
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

        // Then
        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals(scheduleId.toString(), scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage(), 0.0);
        assertEquals(JsonUtils.toJson(param), scheduleTaskEntity.getParametersJson());
        assertNotNull(scheduleTaskEntity.getResultJson());
        assertNotNull(scheduleTaskEntity.getFireTime());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        // When
        onlineSchemaChangeFlowableTask.updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);

        // Then
        verify(log).info("Update flow instance status to {} success", FlowStatus.EXECUTION_SUCCEEDED);
    }
}