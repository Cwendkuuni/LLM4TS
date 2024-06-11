package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleEntity;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity;
import com.oceanbase.odc.service.task.TaskService;

public class OnlineSchemaChangeFlowableTaskTest {

    private BaseODCFlowTaskDelegate<Void> onlineSchemaChangeFlowableTask;
    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters parameter;
    private OrganizationService organizationService;
    private ScheduleService scheduleService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private QuartzJobService quartzJobService;
    private OscTaskCompleteHandler oscTaskCompleteHandler;
    private TaskService taskService;

    @Before
    public void setUp() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        parameter = new OnlineSchemaChangeParameters();
        organizationService = Mockito.mock(OrganizationService.class);
        scheduleService = Mockito.mock(ScheduleService.class);
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        quartzJobService = Mockito.mock(QuartzJobService.class);
        oscTaskCompleteHandler = Mockito.mock(OscTaskCompleteHandler.class);
        taskService = Mockito.mock(TaskService.class);
        when(connectionConfig.id()).thenReturn(1L);
        when(organizationService.getOrganizationId(anyLong())).thenReturn(1L);
        when(organizationService.getCreatorId(anyLong())).thenReturn(1L);
    }

    @Test
    public void testStartWithSuccess() throws Exception {
        // given
        when(taskService.detail(anyLong())).thenReturn(new ExecutionEntity());

        OnlineSchemaChangeContextHolder.set(new OnlineSchemaChangeContextHolder.Context(1L, 1L, 1L));

        // when
        onlineSchemaChangeFlowableTask.start(1L, taskService, Mockito.mock(DelegateExecution.class));

        // then
        verify(taskService, times(1)).start(1L);
        verifyNoMoreInteractions(quartzJobService);
    }

    @Test
    public void testStartWithQuartzJobFailure() throws Exception {
        // given
        when(taskService.detail(anyLong())).thenReturn(new ExecutionEntity());

        OnlineSchemaChangeContextHolder.set(new OnlineSchemaChangeContextHolder.Context(1L, 1L, 1L));

        when(quartzJobService.scheduleJob(anyString(), anyString(), any(), any(), any(), any())).thenThrow(new Exception());

        // when
        onlineSchemaChangeFlowableTask.start(1L, taskService, Mockito.mock(DelegateExecution.class));

        // then
        verify(taskService, times(1)).start(1L);
        verify(quartzJobService, times(1)).scheduleJob(anyString(), anyString(), eq(1L), any(), any(), any());
        verify(taskService, times(1)).cancel(1L);
    }

    @Test
    public void testIsSuccessfulWithSuccess() {
        // given

        // when
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;

        // then
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsSuccessfulWithFailure() {
        // given

        // when
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;

        // then
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailureWithSuccess() {
        // given

        // when
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;

        // then
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testIsFailureWithFailure() {
        // given

        // when
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;

        // then
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailureWithTaskNotFound() {
        // given

        // when
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

        // then
        verify(taskService, times(1)).cancel(1L);
    }

    @Test
    public void testOnFailureWithTaskFound() {
        // given
        ExecutionEntity execution = new ExecutionEntity();
        execution.setStatus(FlowStatus.EXECUTION_FAILED.name());

        // when
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

        // then
        verify(taskService, times(1)).cancel(1L);
        verify(taskService, times(1)).update(execution);
    }

    @Test
    public void testOnProgressUpdateWithSuccess() {
        // given
        int total = 10;
        int done = 5;
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            ScheduleTaskEntity task = new ScheduleTaskEntity();
            task.setStatus(TaskStatus.DONE);
            tasks.add(task);
        }
        Page<ScheduleTaskEntity> page = new PageImpl<>(tasks);

        // when
        onlineSchemaChangeFlowableTask.progressStatusUpdate(page);
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        // then
        assertEquals(50.0, onlineSchemaChangeFlowableTask.percentage, 0.01);
        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testCancelWithSuccess() {
        // given

        // when
        onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        // then
        verify(taskService, times(1)).cancel(1L);
    }

    @Test
    public void testCancelWithRunningTask() {
        // given

        // when
        onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setStatus(TaskStatus.RUNNING);
        doReturn(scheduleTask).when(scheduleTaskRepository).saveAndFlush(any(ScheduleTaskEntity.class));

        // then
        verify(scheduleTaskRepository, times(1)).saveAndFlush(any(ScheduleTaskEntity.class));
    }

    @Test
    public void testIsCancelledWithSuccess() {
        // given

        // when
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;

        // then
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testIsCancelledWithRunningTask() {
        // given

        // when
        onlineSchemaChangeFlowableTask.status = TaskStatus.RUNNING;

        // then
        assertFalse(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCreateScheduleEntityWithSuccess() {
        // given
        String json = "{}";

        // when
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter,
                "test_schema");

        // then
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals("test_schema", scheduleEntity.getDatabaseName());
        assertEquals(1L, scheduleEntity.getConnectionId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getCreatorId());
        assertEquals(JsonUtils.toJson(parameter), scheduleEntity.getJobParametersJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, scheduleEntity.getMisfireStrategy());
        assertEquals(TriggerStrategy.CRON, scheduleEntity.getTriggerStrategy());
        assertEquals(TriggerStrategy.CRON, scheduleEntity.getTriggerStrategy());
        assertEquals(checkTaskCronExpression, scheduleEntity.getTriggerConfigJson());
    }

    @Test
    public void testCreateScheduleTaskEntityWithSuccess() {
        // given
        String json = "{}";

        // when
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L,
                new OnlineSchemaChangeScheduleTaskParameters());

        // then
        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("OnlineSchemaChangeFlowableTask", scheduleTaskEntity.getJobName());
        assertEquals("ScheduleTask", scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage(), 0.0);
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(new OnlineSchemaChangeScheduleTaskParameters())),
                scheduleTaskEntity.getResultJson());
    }
}