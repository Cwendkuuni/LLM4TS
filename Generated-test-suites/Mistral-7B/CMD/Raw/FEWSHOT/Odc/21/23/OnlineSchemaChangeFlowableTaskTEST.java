package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.model.ConnectionConfig;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.model.ConnectionConfigRepository;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity;
import com.oceanbase.odc.service.task.TaskEntity;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.TaskServiceMock;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private TaskServiceMock taskService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OrganizationService organizationService;
    private ConnectionConfigRepository connectionConfigRepository;
    private QuartzJobService quartzJobService;
    private ScheduleService scheduleService;
    private ScheduleTaskService scheduleTaskService;
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @org.junit.Before
    public void setUp() {
        taskService = new TaskServiceMock();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        organizationService = mock(OrganizationService.class);
        connectionConfigRepository = mock(ConnectionConfigRepository.class);
        quartzJobService = mock(QuartzJobService.class);
        scheduleService = mock(ScheduleService.class);
        scheduleTaskService = mock(ScheduleTaskService.class);
        oscTaskCompleteHandler = mock(OscTaskCompleteHandler.class);

        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setTaskService(taskService);
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
        onlineSchemaChangeFlowableTask.setConnectionConfigRepository(connectionConfigRepository);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setOscTaskCompleteHandler(oscTaskCompleteHandler);
    }

    @org.junit.Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);

        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "testSchema";

        ScheduleEntity expectedScheduleEntity = new ScheduleEntity();
        expectedScheduleEntity.setConnectionId(connectionConfig.getId());
        expectedScheduleEntity.setDatabaseName(schema);
        expectedScheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        expectedScheduleEntity.setStatus(ScheduleStatus.ENABLED);
        expectedScheduleEntity.setAllowConcurrent(false);
        expectedScheduleEntity.setCreatorId(1L);
        expectedScheduleEntity.setOrganizationId(1L);
        expectedScheduleEntity.setProjectId(1L);
        expectedScheduleEntity.setDatabaseId(1L);
        expectedScheduleEntity.setModifierId(1L);
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
        triggerConfig.setCronExpression("0/10 * * * * ?");
        expectedScheduleEntity.setTriggerConfigJson(JsonUtils.toJson(triggerConfig));
        expectedScheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
        expectedScheduleEntity.setJobParametersJson(JsonUtils.toJson(parameter));

        ScheduleEntity actualScheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);
        assertEquals(expectedScheduleEntity, actualScheduleEntity);
    }

    @org.junit.Test
    public void testCreateScheduleTaskEntity() {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

        ScheduleTaskEntity expectedScheduleTaskEntity = new ScheduleTaskEntity();
        expectedScheduleTaskEntity.setStatus(TaskStatus.PREPARING);
        expectedScheduleTaskEntity.setJobName(scheduleId.toString());
        expectedScheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        expectedScheduleTaskEntity.setProgressPercentage(0.0);
        expectedScheduleTaskEntity.setParametersJson(JsonUtils.toJson(param));
        expectedScheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(param)));
        expectedScheduleTaskEntity.setFireTime(new Date());

        ArgumentCaptor<ScheduleTaskEntity> scheduleTaskEntityArgumentCaptor = ArgumentCaptor.forClass(ScheduleTaskEntity.class);

        onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

        verify(scheduleTaskRepository).saveAndFlush(scheduleTaskEntityArgumentCaptor.capture());
        ScheduleTaskEntity actualScheduleTaskEntity = scheduleTaskEntityArgumentCaptor.getValue();
        assertEquals(expectedScheduleTaskEntity, actualScheduleTaskEntity);
    }

    @org.junit.Test
    public void testIsSuccessful() {
        TaskStatus expectedTaskStatus = TaskStatus.DONE;
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());
        when(onlineSchemaChangeFlowableTask.getStatus()).thenReturn(expectedTaskStatus);

        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @org.junit.Test
    public void testIsFailure() {
        TaskStatus expectedTaskStatus = TaskStatus.FAILED;
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());
        when(onlineSchemaChangeFlowableTask.getStatus()).thenReturn(expectedTaskStatus);

        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @org.junit.Test
    public void testOnFailure() {
        Long taskId = 1L;
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());

        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        verify(taskService).update(argThat(t -> {
            TaskEntity taskEntity = t;
            assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
            assertEquals(taskId, taskEntity.getId());
        }));
    }

    @org.junit.Test
    public void testOnSuccessful() {
        Long taskId = 1L;
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());

        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        verify(taskService).update(argThat(t -> {
            TaskEntity taskEntity = t;
            assertEquals(FlowStatus.EXECUTION_SUCCEEDED, taskEntity.getStatus());
            assertEquals(taskId, taskEntity.getId());
        }));
    }

    @org.junit.Test
    public void testOnTimeout() {
        Long taskId = 1L;
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());

        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        verifyNoMoreInteractions(taskService);
    }

    @org.junit.Test
    public void testOnProgressUpdate() {
        Long taskId = 1L;
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());

        Page<ScheduleTaskEntity> tasksPage = Mockito.mock(Page.class);
        when(scheduleTaskService.listTask(eq(Pageable.unpaged()), eq(1L))).thenReturn(tasksPage);

        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

        verify(taskService).update(argThat(t -> {
            TaskEntity taskEntity = t;
            assertEquals(TaskStatus.RUNNING, taskEntity.getStatus());
            assertEquals(taskId, taskEntity.getId());
        }));
    }

    @org.junit.Test
    public void testProgressStatusUpdate() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        Page<ScheduleTaskEntity> tasks = Mockito.mock(Page.class);

        when(tasks.getSize()).thenReturn(3);
        when(tasks.getContent()).thenReturn(Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity(), new ScheduleTaskEntity()));

        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);

        verify(onlineSchemaChangeFlowableTask).status = TaskStatus.DONE;
    }

    @org.junit.Test
    public void testSingleTaskPercentage() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);

        double expectedPercentage = 0.0;
        double actualPercentage = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity);

        assertEquals(expectedPercentage, actualPercentage, 0.0);

        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
        scheduleTaskEntity.setProgressPercentage(50);

        expectedPercentage = 0.5;
        actualPercentage = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity);

        assertEquals(expectedPercentage, actualPercentage, 0.0);
    }

    @org.junit.Test
    public void testCancel() {
        Long taskId = 1L;
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());

        onlineSchemaChangeFlowableTask.cancel(true, taskId, taskService);

        verify(taskService).cancel(taskId);
    }

    @org.junit.Test
    public void testIsCancelled() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;

        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }
}