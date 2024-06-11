package com.oceanbase.odc.service.onlineschemachange.test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.test.mock.MockDelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedFieldInjector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskExecution;
import com.oceanbase.odc.service.task.model.TaskInstance;
import com.oceanbase.odc.service.task.model.TaskResult;
import com.oceanbase.odc.service.task.test.util.TaskServiceTestUtil;

/**
 * Test class for OnlineSchemaChangeFlowableTask
 *
 * @author yaobin
 * @date 2023-05-23
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
    private OrganizationService organizationService;

    @Mock
    private TaskService taskService;

    @Mock
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask testee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        new AutowiredAnnotatedFieldInjector().inject(testee);
        when(taskService.detail(anyLong())).thenReturn(new TaskInstance());
        when(taskService.listTask(any(Pageable.class), anyLong())).thenReturn(new PageImpl<>(Arrays.asList(new ScheduleTaskEntity())));
    }

    @Test
    public void testStart() {
        DelegateExecution execution = new MockDelegateExecution();
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);
        String schema = "testSchema";
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(new User());
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schema);
        doNothing().when(testee).createScheduleEntity(connectionConfig, parameter, schema);
        testee.start(1L, taskService, execution);
        verify(testee, times(1)).createScheduleEntity(connectionConfig, parameter, schema);
    }

    @Test
    public void testIsSuccessful() {
        TaskStatus status = TaskStatus.DONE;
        assertTrue(testee.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        TaskStatus status = TaskStatus.FAILED;
        assertFalse(testee.isSuccessful());
        assertFalse(testee.isFailure());
        testee.status = status;
        assertTrue(testee.isFailure());
    }

    @Test
    public void testOnFailure() {
        Long taskId = 1L;
        testee.onFailure(taskId, taskService);
        verify(taskService, times(1)).update(any(TaskInstance.class));
    }

    @Test
    public void testOnSuccessful() {
        Long taskId = 1L;
        testee.onSuccessful(taskId, taskService);
        verify(taskService, times(1)).update(any(TaskInstance.class));
    }

    @Test
    public void testOnTimeout() {
        Long taskId = 1L;
        testee.onTimeout(taskId, taskService);
        verifyNoInteractions(taskService);
    }

    @Test
    public void testOnProgressUpdate() {
        long taskId = 1L;
        Page<ScheduleTaskEntity> tasks = new PageImpl<>(Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity()));
        TaskInstance flowTask = new TaskInstance();
        TaskStatus dbStatus = TaskStatus.RUNNING;
        when(taskService.detail(taskId)).thenReturn(flowTask);
        when(scheduleTaskService.listTask(any(Pageable.class), eq(testee.scheduleId))).thenReturn(tasks);
        testee.onProgressUpdate(taskId, taskService);
        TaskStatus newStatus = testee.status;
        assertEquals(TaskStatus.DONE, newStatus);
        verify(flowTask, times(1)).setStatus(eq(newStatus));
        verify(flowTask, times(1)).setResultJson(anyString());
        verify(flowTask, times(1)).setProgressPercentage(eq(100));
        verify(taskService, times(1)).update(flowTask);
    }

    @Test
    public void testCancel() {
        boolean mayInterruptIfRunning = true;
        Long taskId = 1L;
        testee.cancel(mayInterruptIfRunning, taskId, taskService);
        verify(taskService, times(1)).cancel(taskId);
        verify(testee, times(1)).cancel(mayInterruptIfRunning, taskId, taskService);
    }

    @Test
    public void testIsCancelled() {
        TaskStatus status = TaskStatus.CANCELED;
        testee.status = status;
        assertTrue(testee.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);
        String schema = "testSchema";
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        User creator = new User();
        Long creatorId = 1L;
        Long organizationId = 1L;
        when(FlowTaskUtil.getTaskCreator(any())).thenReturn(creator);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(any())).thenReturn(parameter);
        when(FlowTaskUtil.getConnectionConfig(any())).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(any())).thenReturn(schema);
        ScheduleEntity schedule = testee.createScheduleEntity(connectionConfig, parameter, schema);
        ArgumentCaptor<ScheduleEntity> scheduleEntityArgumentCaptor = ArgumentCaptor.forClass(ScheduleEntity.class);
        verify(scheduleService, times(1)).create(scheduleEntityArgumentCaptor.capture());
        ScheduleEntity createdSchedule = scheduleEntityArgumentCaptor.getValue();
        assertEquals(connectionConfig.id(), createdSchedule.getConnectionId());
        assertEquals(schema, createdSchedule.getDatabaseName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, createdSchedule.getJobType());
        assertEquals(ScheduleStatus.ENABLED, createdSchedule.getStatus());
        assertEquals(false, createdSchedule.isAllowConcurrent());
        assertEquals(creatorId, createdSchedule.getCreatorId());
        assertEquals(organizationId, createdSchedule.getOrganizationId());
        assertEquals(1L, createdSchedule.getProjectId());
        assertEquals(1L, createdSchedule.getDatabaseId());
        assertEquals(creatorId, createdSchedule.getModifierId());
        assertEquals(TriggerStrategy.CRON, createdSchedule.getTriggerStrategy());
        assertEquals(checkTaskCronExpression, createdSchedule.getTriggerConfigJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, createdSchedule.getMisfireStrategy());
        assertEquals(JsonUtils.toJson(parameter), createdSchedule.getJobParametersJson());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
        scheduleTaskEntity.setJobName(scheduleId.toString());
        scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        scheduleTaskEntity.setProgressPercentage(0.0);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(param));
        ScheduleTaskEntity createdScheduleTaskEntity = testee.createScheduleTaskEntity(scheduleId, param);
        assertEquals(TaskStatus.PREPARING, createdScheduleTaskEntity.getStatus());
        assertEquals(scheduleId.toString(), createdScheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), createdScheduleTaskEntity.getJobGroup());
        assertEquals(0.0, createdScheduleTaskEntity.getProgressPercentage());
        assertEquals(JsonUtils.toJson(param), createdScheduleTaskEntity.getParametersJson());
    }
}