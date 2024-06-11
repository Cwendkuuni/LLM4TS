package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.*;
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
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private ConnectionConfig connectionConfig;

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

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setConnectionConfig(connectionConfig);
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
        onlineSchemaChangeFlowableTask.setTaskHandler(taskHandler);
    }

    @Test
    public void testStart() throws Exception {
        // Given
        DelegateExecution delegateExecution = mock(DelegateExecution.class);
        when(delegateExecution.getId()).thenReturn(1L);
        when(delegateExecution.getVariables()).thenReturn(new ExecutionEntity().getVariables());
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";
        when(connectionConfig.getId()).thenReturn(1L);
        when(connectionConfig.getUid()).thenReturn("test_uid");
        when(organizationService.getOrganizationId(anyLong())).thenReturn(1L);
        when(connectionConfig.getSchemaName()).thenReturn(schema);
        OnlineSchemaChangeContextHolder.clear();

        // When
        onlineSchemaChangeFlowableTask.start(1L, null, delegateExecution);

        // Then
        verify(connectionConfig, times(1)).getId();
        verify(connectionConfig, times(1)).getSchemaName();
        verify(organizationService, times(1)).getOrganizationId(anyLong());
        verify(onlineSchemaChangeFlowableTask, times(1)).createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schema);
        verifyNoMoreInteractions(connectionConfig);
        verifyNoMoreInteractions(delegateExecution);
        verifyNoMoreInteractions(organizationService);
    }

    @Test
    public void testIsSuccessful() {
        // Given
        TaskStatus expectedStatus = TaskStatus.DONE;

        // When
        TaskStatus actualStatus = onlineSchemaChangeFlowableTask.isSuccessful();

        // Then
        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    public void testIsFailure() {
        // Given
        TaskStatus expectedStatus = TaskStatus.FAILED;

        // When
        TaskStatus actualStatus = onlineSchemaChangeFlowableTask.isFailure();

        // Then
        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    public void testOnFailure() {
        // Given
        long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.onFailure(taskId, null);

        // Then
        // Verify no interactions as this method is just logging
    }

    @Test
    public void testOnSuccessful() {
        // Given
        long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, null);

        // Then
        // Verify no interactions as this method is just updating flow instance status
    }

    @Test
    public void testOnTimeout() {
        // Given
        long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.onTimeout(taskId, null);

        // Then
        // Verify no interactions as this method is just logging
    }

    @Test
    public void testOnProgressUpdate() {
        // Given
        long taskId = 1L;
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        Page<ScheduleTaskEntity> page = Page.empty();
        page.setContent(tasks);

        // When
        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, null);

        // Then
        // Verify no interactions as this method is just updating flow task status
    }

    @Test
    public void testCreateScheduleEntity() throws Exception {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        JobType jobType = JobType.ONLINE_SCHEMA_CHANGE_COMPLETE;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        TriggerConfig triggerConfig = new TriggerConfig();

        when(connectionConfig.getId()).thenReturn(1L);
        when(connectionConfig.getUid()).thenReturn("test_uid");
        when(organizationService.getOrganizationId(anyLong())).thenReturn(1L);
        when(connectionConfig.getSchemaName()).thenReturn(schema);
        when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);
        when(scheduleTaskRepository.saveAndFlush(scheduleTaskEntity)).thenReturn(scheduleTaskEntity);
        when(quartzJobService.scheduleJob(scheduleEntity)).thenReturn(true);

        // When
        ScheduleEntity result = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schema);

        // Then
        verify(connectionConfig, times(1)).getId();
        verify(connectionConfig, times(1)).getSchemaName();
        verify(organizationService, times(1)).getOrganizationId(anyLong());
        verify(scheduleEntity, times(1)).setConnectionId(connectionConfig.getId());
        verify(scheduleEntity, times(1)).setDatabaseName(schema);
        verify(scheduleEntity, times(1)).setJobType(jobType);
        verify(scheduleEntity, times(1)).setStatus(ScheduleStatus.ENABLED);
        verify(scheduleEntity, times(1)).setAllowConcurrent(false);
        verify(scheduleEntity, times(1)).setCreatorId(1L);
        verify(scheduleEntity, times(1)).setOrganizationId(1L);
        verify(scheduleEntity, times(1)).setProjectId(1L);
        verify(scheduleEntity, times(1)).setDatabaseId(1L);
        verify(scheduleEntity, times(1)).setModifierId(1L);
        verify(triggerConfig, times(1)).setTriggerStrategy(TriggerStrategy.CRON);
        verify(triggerConfig, times(1)).setCronExpression("0/10 * * * * ?");
        verify(scheduleEntity, times(1)).setTriggerConfigJson(JsonUtils.toJson(triggerConfig));
        verify(scheduleEntity, times(1)).setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
        verify(scheduleEntity, times(1)).setJobParametersJson(JsonUtils.toJson(onlineSchemaChangeParameters));
        verify(scheduleService, times(1)).create(scheduleEntity);
        verify(quartzJobService, times(1)).scheduleJob(scheduleEntity);
        assertEquals(scheduleEntity, result);
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        // Given
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();

        // When
        ScheduleTaskEntity result = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, onlineSchemaChangeScheduleTaskParameters);

        // Then
        assertEquals(TaskStatus.PREPARING, result.getStatus());
        assertEquals(onlineSchemaChangeScheduleTaskParameters, JsonUtils.parse(result.getParametersJson(), OnlineSchemaChangeScheduleTaskParameters.class));
    }
}