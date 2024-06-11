package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.oceanbase.odc.service.schedule.model.TriggerStrategyEnum;
import com.oceanbase.odc.service.task.TaskService;

public class OnlineSchemaChangeFlowableTaskTest {

    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @Autowired
    private QuartzJobService quartzJobService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private TaskEntity taskEntity;
    private ExecutionEntity executionEntity;
    private ScheduleEntity scheduleEntity;
    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);

        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setContinueOnError(true);

        taskEntity = new TaskEntity();
        taskEntity.setId(1L);

        executionEntity = new ExecutionEntity();
        executionEntity.setId(1L);
        executionEntity.setTaskId(taskEntity.getId());
        executionEntity.setExecutionStatus(TaskStatus.APPROVED.name());

        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setConnectionId(connectionConfig.getId());
        scheduleEntity.setDatabaseName("test_database");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setStatus(ScheduleStatus.ENABLED);
        scheduleEntity.setAllowConcurrent(false);
        scheduleEntity.setCreatorId(1L);
        scheduleEntity.setOrganizationId(1L);
        scheduleEntity.setProjectId(1L);
        scheduleEntity.setDatabaseId(1L);
        scheduleEntity.setModifierId(scheduleEntity.getCreatorId());
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setTriggerStrategy(TriggerStrategyEnum.CRON);
        triggerConfig.setCronExpression("0/10 * * * * ?");
        scheduleEntity.setTriggerConfigJson(JsonUtils.toJson(triggerConfig));
        scheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(onlineSchemaChangeParameters));

        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        scheduleTaskEntity.setProgressPercentage(0.0);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskParameters()));
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
        scheduleTaskEntity.setFireTime(new Date());

        when(taskService.detail(Mockito.anyLong())).thenReturn(taskEntity);
        when(taskService.start(Mockito.anyLong())).thenReturn(null);
        when(scheduleService.create(Mockito.any(ScheduleEntity.class))).thenReturn(scheduleEntity);
        when(scheduleTaskService.saveAndFlush(Mockito.any(ScheduleTaskEntity.class))).thenReturn(scheduleTaskEntity);
        when(quartzJobService.createJob(Mockito.any(ScheduleEntity.class), Mockito.any(ScheduleTaskEntity.class)))
                .thenReturn(null);
        when(organizationService.getUser(Mockito.anyLong())).thenReturn(new User());
        when(oscTaskCompleteHandler.start(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);
    }

    @Test
    public void testStart() throws Exception {
        DelegateExecution delegateExecution = mock(DelegateExecution.class);
        when(delegateExecution.getId()).thenReturn(executionEntity.getId());
        when(delegateExecution.getVariables()).thenReturn(new Object[0]);

        onlineSchemaChangeFlowableTask.start(taskEntity.getId(), taskService, delegateExecution);

        ArgumentCaptor<Void> voidCaptor = ArgumentCaptor.forClass(Void.class);
        verify(taskService).start(taskEntity.getId());
        verify(quartzJobService).createJob(scheduleEntity, scheduleTaskEntity);
    }

    @Test
    public void testIsSuccessful() {
        TaskStatus taskStatus = TaskStatus.DONE;
        taskEntity.setStatus(taskStatus.name());
        assertEquals(true, onlineSchemaChangeFlowableTask.isSuccessful());

        taskStatus = TaskStatus.FAILED;
        taskEntity.setStatus(taskStatus.name());
        assertEquals(false, onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        TaskStatus taskStatus = TaskStatus.DONE;
        taskEntity.setStatus(taskStatus.name());
        assertEquals(false, onlineSchemaChangeFlowableTask.isFailure());

        taskStatus = TaskStatus.FAILED;
        taskEntity.setStatus(taskStatus.name());
        assertEquals(true, onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        taskEntity.setStatus(TaskStatus.FAILED.name());
        OnlineSchemaChangeFlowableTask testObject = new OnlineSchemaChangeFlowableTask();
        testObject.onFailure(1L, taskService);

        verify(taskService).cancel(1L);
    }

    @Test
    public void testOnSuccessful() {
        taskEntity.setStatus(TaskStatus.DONE.name());
        OnlineSchemaChangeFlowableTask testObject = new OnlineSchemaChangeFlowableTask();
        testObject.onSuccessful(1L, taskService);

        ArgumentCaptor<OnlineSchemaChangeTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeTaskResult.class);
        verify(taskService).update(taskEntity);
        verify(taskEntity).setResultJson(resultCaptor.capture());
    }

    @Test
    public void testOnTimeout() {
        OnlineSchemaChangeFlowableTask testObject = new OnlineSchemaChangeFlowableTask();
        testObject.onTimeout(1L, taskService);

        verify(taskService, never()).cancel(1L);
    }

    @Test
    public void testOnProgressUpdate() {
        Page<ScheduleTaskEntity> tasks = createTasks();
        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);
        assertEquals(TaskStatus.DONE, onlineSchemaChangeFlowableTask.getStatus());

        tasks = createFailedTasks();
        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);
        assertEquals(TaskStatus.FAILED, onlineSchemaChangeFlowableTask.getStatus());
    }

    private Page<ScheduleTaskEntity> createTasks() {
        return scheduleTaskService.listTask(Pageable.unpaged(), scheduleEntity.getId())
                .map(task -> new ScheduleTaskEntity().setStatus(TaskStatus.DONE));
    }

    private Page<ScheduleTaskEntity> createFailedTasks() {
        return scheduleTaskService.listTask(Pageable.unpaged(), scheduleEntity.getId())
                .map(task -> new ScheduleTaskEntity().setStatus(TaskStatus.FAILED));
    }

    @Test
    public void testCancel() {
        OnlineSchemaChangeFlowableTask testObject = new OnlineSchemaChangeFlowableTask();
        boolean result = testObject.cancel(true, 1L, taskService);
        verify(taskService).cancel(1L);
        assertEquals(true, result);
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeFlowableTask testObject = new OnlineSchemaChangeFlowableTask();
        testObject.setStatus(TaskStatus.CANCELED);
        assertEquals(true, testObject.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        OnlineSchemaChangeFlowableTask testObject = new OnlineSchemaChangeFlowableTask();
        ScheduleEntity scheduleEntity = testObject.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters,
                "test_database");
        assertEquals("test_database", scheduleEntity.getDatabaseName());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeFlowableTask testObject = new OnlineSchemaChangeFlowableTask();
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTaskEntity = testObject.createScheduleTaskEntity(scheduleEntity.getId(), param);
        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
    }
}