package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

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
import com.oceanbase.odc.service.iam.model.User;
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
import com.oceanbase.odc.service.schedule.model.TriggerType;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskStatusUpdate;
import com.oceanbase.odc.service.test.BaseTest;

@SpringBootTest
@ActiveProfiles("test")
public class OnlineSchemaChangeFlowableTaskTest extends BaseTest {

    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
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

    private User creator;
    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters parameter;
    private ScheduleEntity scheduleEntity;
    private ScheduleTaskEntity scheduleTaskEntity;
    private TaskEntity taskEntity;

    @Captor
    private ArgumentCaptor<TaskStatusUpdate> taskStatusUpdateCaptor;

    @Before
    public void setUp() {
        creator = new User(1L, "test@test.com", "Test", "Test", 1L, 1L);
        connectionConfig = new ConnectionConfig(1L, "test", "test@test.com", "test", "test");
        parameter = new OnlineSchemaChangeParameters("test", true, 1L, 1L);
        scheduleEntity = new ScheduleEntity();
        scheduleTaskEntity = new ScheduleTaskEntity();
        taskEntity = new TaskEntity();
    }

    @Test
    public void testStart() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getId()).thenReturn(1L);
        when(execution.getVariable("flowInstanceId")).thenReturn(1L);
        when(execution.getVariable("creator")).thenReturn(creator);

        when(organizationService.getUserById(creator.getId())).thenReturn(creator);
        when(organizationService.getUserById(1L)).thenReturn(new User(1L, "test2@test.com", "Test2", "Test2", 1L, 1L));

        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(scheduleTaskEntity);
        when(taskService.detail(1L)).thenReturn(taskEntity);
        when(taskService.update(taskEntity)).thenReturn(taskEntity);
        when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);
        when(quartzJobService.scheduleJob(any(String.class), any(TriggerConfig.class), any(Runnable.class)))
                .thenReturn(1L);
        when(oscTaskCompleteHandler.start(scheduleEntity.getId(), scheduleTaskEntity.getId())).thenReturn(null);

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        verify(taskService, times(1)).start(1L);
        verify(taskService, times(1)).update(taskEntity);
        verify(quartzJobService, times(1)).scheduleJob(any(String.class), any(TriggerConfig.class), any(Runnable.class));
        verify(oscTaskCompleteHandler, times(1)).start(scheduleEntity.getId(), scheduleTaskEntity.getId());

        assertEquals(TaskStatus.RUNNING, taskEntity.getStatus());
        assertEquals(1L, scheduleEntity.getId());
        assertEquals(creator.getId(), scheduleEntity.getCreatorId());
        assertEquals("test", scheduleEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(TriggerType.CRON, scheduleEntity.getTriggerType());
        assertEquals(checkTaskCronExpression, scheduleEntity.getTriggerConfigJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, scheduleEntity.getMisfireStrategy());
        assertEquals(1L, scheduleTaskEntity.getId());
        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals(JsonUtils.toJson(parameter), scheduleTaskEntity.getParametersJson());
        assertNull(scheduleTaskEntity.getResultJson());
        assertEquals(TaskStatus.DONE, scheduleEntity.getStatus());
    }

    @Test(expected = Exception.class)
    public void testStartWithException() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getId()).thenReturn(1L);
        when(execution.getVariable("flowInstanceId")).thenReturn(1L);
        when(execution.getVariable("creator")).thenReturn(creator);

        when(organizationService.getUserById(creator.getId())).thenReturn(creator);
        when(organizationService.getUserById(1L)).thenReturn(new User(1L, "test2@test.com", "Test2", "Test2", 1L, 1L));

        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenThrow(new RuntimeException());
        when(taskService.detail(1L)).thenReturn(taskEntity);

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        verify(taskService, times(0)).start(1L);
        verify(taskService, times(0)).update(taskEntity);
        verify(quartzJobService, times(0)).scheduleJob(any(String.class), any(TriggerConfig.class), any(Runnable.class));
        verify(oscTaskCompleteHandler, times(0)).start(scheduleEntity.getId(), scheduleTaskEntity.getId());

        throw new RuntimeException("Test Exception");
    }

    @Test
    public void testIsSuccessful() {
        taskEntity.setStatus(TaskStatus.DONE);
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful(1L, taskService));
    }

    @Test
    public void testIsFailure() {
        taskEntity.setStatus(TaskStatus.FAILED);
        assertTrue(onlineSchemaChangeFlowableTask.isFailure(1L, taskService));
    }

    @Test
    public void testOnFailure() throws Exception {
        when(taskService.detail(1L)).thenReturn(taskEntity);

        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

        verify(log, times(1)).warn("Online schema change task failed, taskId={}", 1L);
    }

    @Test
    public void testOnSuccessful() throws Exception {
        when(taskService.detail(1L)).thenReturn(taskEntity);

        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

        verify(taskService, times(1)).update(taskEntity);
        verify(log, times(1)).info("Online schema change task succeed, taskId={}", 1L);
    }

    @Test
    public void testOnTimeout() throws Exception {
        when(taskService.detail(1L)).thenReturn(taskEntity);

        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);

        verify(log, times(1)).warn("Online schema change task timeout, taskId={}", 1L);
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        Page<ScheduleTaskEntity> tasks = Page.empty();
        Pageable pageable = Pageable.unpaged();

        when(scheduleTaskService.listTask(pageable, scheduleEntity.getId())).thenReturn(tasks);

        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        verify(log, times(0)).info("List schedule task size is 0 by scheduleId {}", scheduleEntity.getId());
        verify(log, times(0)).warn("onProgressUpdate occur exception,");
    }

    @Test
    public void testOnProgressUpdateWithTasks() throws Exception {
        Page<ScheduleTaskEntity> tasks = Page.of(scheduleTaskEntity, scheduleTaskEntity, scheduleTaskEntity);
        Pageable pageable = Pageable.unpaged();

        when(scheduleTaskService.listTask(pageable, scheduleEntity.getId())).thenReturn(tasks);

        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        verify(log, times(0)).info("List schedule task size is 0 by scheduleId {}", scheduleEntity.getId());
        verify(log, times(0)).warn("onProgressUpdate occur exception,");
        verify(taskService, times(1)).update(taskEntity);

        ArgumentCaptor<TaskStatusUpdate> taskStatusUpdateCaptor = ArgumentCaptor.forClass(TaskStatusUpdate.class);
        verify(taskService, times(1)).update(taskEntity);
        verify(taskService, times(1)).update(taskEntity);
        verify(taskEntity, times(1)).setStatus(taskStatusUpdateCaptor.capture().getNewStatus());
        TaskStatusUpdate taskStatusUpdate = taskStatusUpdateCaptor.getValue();
        assertEquals(TaskStatus.DONE, taskStatusUpdate.getNewStatus());
    }

    @Test
    public void testCancel() throws Exception {
        when(scheduleTaskRepository.findByStatusAndScheduleId(TaskStatus.RUNNING, scheduleEntity.getId()))
                .thenReturn(Optional.ofNullable(scheduleTaskEntity));

        onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        verify(quartzJobService, times(1)).interruptJob(scheduleEntity.getId());
        verify(taskService, times(1)).cancel(1L);
        verify(taskService, times(1)).update(taskEntity);
        verify(taskService, times(1)).deleteById(1L);
        verify(log, times(1)).info("Successfully cancel task with id={}", 1L);

        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testIsCancelled() {
        taskEntity.setStatus(TaskStatus.CANCELED);
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters("test", true, 1L, 1L);
        ConnectionConfig connectionConfig = new ConnectionConfig(1L, "test", "test@test.com", "test", "test");

        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter,
                "test");

        assertEquals(connectionConfig.getId(), scheduleEntity.getConnectionId());
        assertEquals("test", scheduleEntity.getDatabaseName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(false, scheduleEntity.isAllowConcurrent());
        assertEquals(creator.getId(), scheduleEntity.getCreatorId());
        assertEquals(creator.getOrganizationId(), scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals(creator.getId(), scheduleEntity.getModifierId());
        assertEquals("test", scheduleEntity.getJobName());
        assertNull(scheduleEntity.getTriggerConfigJson());
        assertNull(scheduleEntity.getMisfireStrategy());
        assertEquals(JsonUtils.toJson(parameter), scheduleEntity.getJobParametersJson());

    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters("test", 1L, 1L);
        ScheduleEntity scheduleEntity = new ScheduleEntity();

        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleEntity.getId(),
                param);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("test", scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage(), 0.0);
        assertEquals(JsonUtils.toJson(param), scheduleTaskEntity.getParametersJson());
        assertNull(scheduleTaskEntity.getResultJson());
        assertEquals(new Date(), scheduleTaskEntity.getFireTime());
    }

}