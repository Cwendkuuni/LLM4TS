package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.MisfireStrategy;
import com.oceanbase.odc.service.task.TaskService;

@SuppressWarnings("unchecked")
public class OnlineSchemaChangeFlowableTaskTest {
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleService scheduleService;
    private ScheduleTaskService scheduleTaskService;
    private QuartzJobService quartzJobService;
    private OrganizationService organizationService;
    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private DelegateExecution delegateExecution;

    @Before
    public void setUp() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        scheduleService = Mockito.mock(ScheduleService.class);
        scheduleTaskService = Mockito.mock(ScheduleTaskService.class);
        quartzJobService = Mockito.mock(QuartzJobService.class);
        organizationService = Mockito.mock(OrganizationService.class);
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        onlineSchemaChangeParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
        delegateExecution = Mockito.mock(DelegateExecution.class);
        when(connectionConfig.id()).thenReturn(1L);
        when(onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schemaName"))
                .thenReturn(List.of(new OnlineSchemaChangeScheduleTaskParameters("param1"), new OnlineSchemaChangeScheduleTaskParameters("param2")));
    }

    @Test
    public void testStart() throws Exception {
        when(delegateExecution.getId()).thenReturn(1L);
        when(delegateExecution.getVariables()).thenReturn(Map.of());
        when(organizationService.getUserById(eq(creatorId))).thenReturn(Mockito.mock(User.class));
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(quartzJobService.scheduleJob("jobName", "jobGroup", any(TriggerConfig.class), any(MisfireStrategy.class), anyLong()))
                .thenReturn(1L);
        when(taskHandler.start(eq(scheduleId.intValue()), anyLong())).thenReturn(null);

        onlineSchemaChangeFlowableTask.start(1L, Mockito.mock(TaskService.class), delegateExecution);

        verify(delegateExecution, times(1)).setVariable("status", TaskStatus.RUNNING);
        verify(taskService, times(1)).start(1L);
        verify(organizationService, times(1)).getUserById(eq(creatorId));
        verify(scheduleService, times(1)).create(any(ScheduleEntity.class));
        verify(quartzJobService, times(1)).scheduleJob("jobName", "jobGroup", any(TriggerConfig.class), any(MisfireStrategy.class), anyLong());
        verify(taskHandler, times(1)).start(eq(scheduleId.intValue()), anyLong());
        verifyNoMoreInteractions(delegateExecution);
        verifyNoMoreInteractions(taskService);
        verifyNoMoreInteractions(organizationService);
        verifyNoMoreInteractions(scheduleService);
        verifyNoMoreInteractions(quartzJobService);
    }

    @Test
    public void testIsSuccessful() {
        when(delegateExecution.getVariables()).thenReturn(Map.of("status", TaskStatus.DONE));
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        when(delegateExecution.getVariables()).thenReturn(Map.of("status", TaskStatus.FAILED));
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() throws Exception {
        when(delegateExecution.getId()).thenReturn(1L);
        when(delegateExecution.getVariables()).thenReturn(Map.of());
        when(taskService.detail(1L)).thenReturn(new ExecutionEntity());
        onlineSchemaChangeFlowableTask.onFailure(1L, Mockito.mock(TaskService.class));
        verify(taskService, times(1)).cancel(1L);
        verify(logger, times(1)).warn("Online schema change task failed, taskId={}", 1L);
    }

    @Test
    public void testOnSuccessful() throws Exception {
        when(delegateExecution.getId()).thenReturn(1L);
        when(delegateExecution.getVariables()).thenReturn(Map.of());
        when(taskService.detail(1L)).thenReturn(new ExecutionEntity());
        onlineSchemaChangeFlowableTask.onSuccessful(1L, Mockito.mock(TaskService.class));
        verify(taskService, times(1)).update(any(TaskEntity.class));
        verify(logger, times(1)).info("Online schema change task succeed, taskId={}", 1L);
    }

    @Test
    public void testOnTimeout() throws Exception {
        when(delegateExecution.getId()).thenReturn(1L);
        when(delegateExecution.getVariables()).thenReturn(Map.of());
        onlineSchemaChangeFlowableTask.onTimeout(1L, Mockito.mock(TaskService.class));
        verify(logger, times(1)).warn("Online schema change task timeout, taskId={}", 1L);
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        when(delegateExecution.getId()).thenReturn(1L);
        when(scheduleTaskService.listTask(any(Pageable.class), eq(scheduleId))).thenReturn(Page.empty());

        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, Mockito.mock(TaskService.class));
        verifyNoInteractions(logger);

        when(scheduleTaskService.listTask(any(Pageable.class), eq(scheduleId)))
                .thenReturn(Page.of(Collections.singletonList(new ScheduleTaskEntity()), Pageable.unpaged()));

        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, Mockito.mock(TaskService.class));
        verify(taskService, times(1)).update(any(TaskEntity.class));
        verify(logger, times(1)).info("Online schema change task progress updated, taskId={}", 1L);
    }

    @Test
    public void testCancel() throws Exception {
        when(delegateExecution.getId()).thenReturn(1L);
        when(scheduleTaskService.listTask(any(Pageable.class), eq(scheduleId)))
                .thenReturn(Page.of(Collections.singletonList(new ScheduleTaskEntity()), Pageable.unpaged()));

        onlineSchemaChangeFlowableTask.cancel(true, 1L, Mockito.mock(TaskService.class));
        verify(taskHandler, times(1)).terminate(eq(scheduleId.intValue()), anyLong());
        verify(delegateExecution, times(1)).setVariable("status", TaskStatus.CANCELED);
        verify(taskService, times(1)).cancel(1L);
        verify(logger, times(1)).warn("Online schema change task canceled, taskId={}", 1L);
    }

    @Test
    public void testIsCancelled() {
        when(delegateExecution.getVariables()).thenReturn(Map.of("status", TaskStatus.CANCELED));
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() throws Exception {
        when(organizationService.getUserById(eq(creatorId))).thenReturn(Mockito.mock(User.class));

        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, "schemaName");

        verify(scheduleEntity, times(1)).setConnectionId(1L);
        verify(scheduleEntity, times(1)).setDatabaseName("schemaName");
        verify(scheduleEntity, times(1)).setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        verify(scheduleEntity, times(1)).setStatus(ScheduleStatus.ENABLED);
        verify(scheduleEntity, times(1)).setAllowConcurrent(false);
        verify(scheduleEntity, times(1)).setCreatorId(creatorId);
        verify(scheduleEntity, times(1)).setOrganizationId(organizationId);
        verify(scheduleEntity, times(1)).setProjectId(1L);
        verify(scheduleEntity, times(1)).setDatabaseId(1L);
        verify(scheduleEntity, times(1)).setModifierId(creatorId);
        verify(scheduleEntity, times(1)).setJobParametersJson(JsonUtils.toJson(onlineSchemaChangeParameters));
    }

    @Test
    public void testCreateScheduleTaskEntity() throws Exception {
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, new OnlineSchemaChangeScheduleTaskParameters("param"));

        verify(scheduleTaskEntity, times(1)).setStatus(TaskStatus.PREPARING);
        verify(scheduleTaskEntity, times(1)).setJobName(Long.toString(scheduleId));
        verify(scheduleTaskEntity, times(1)).setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        verify(scheduleTaskEntity, times(1)).setProgressPercentage(0.0);
        verify(scheduleTaskEntity, times(1)).setParametersJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskParameters("param")));
        verify(scheduleTaskEntity, times(1)).setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(new OnlineSchemaChangeScheduleTaskParameters("param"))));
        verify(scheduleTaskEntity, times(1)).setFireTime(new Date());
    }
}