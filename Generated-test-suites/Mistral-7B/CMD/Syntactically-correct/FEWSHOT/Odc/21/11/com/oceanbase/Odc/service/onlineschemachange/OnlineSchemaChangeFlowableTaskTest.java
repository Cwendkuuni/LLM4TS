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
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;

@SuppressWarnings("unchecked")
public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleService scheduleService;
    private ScheduleTaskService scheduleTaskService;
    private QuartzJobService quartzJobService;
    private OrganizationService organizationService;
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @Before
    public void setup() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleService = mock(ScheduleService.class);
        scheduleTaskService = mock(ScheduleTaskService.class);
        quartzJobService = mock(QuartzJobService.class);
        organizationService = mock(OrganizationService.class);
        oscTaskCompleteHandler = mock(OscTaskCompleteHandler.class);
        when(onlineSchemaChangeFlowableTask.getScheduleTaskRepository()).thenReturn(scheduleTaskRepository);
        when(onlineSchemaChangeFlowableTask.getScheduleService()).thenReturn(scheduleService);
        when(onlineSchemaChangeFlowableTask.getScheduleTaskService()).thenReturn(scheduleTaskService);
        when(onlineSchemaChangeFlowableTask.getQuartzJobService()).thenReturn(quartzJobService);
        when(onlineSchemaChangeFlowableTask.getOrganizationService()).thenReturn(organizationService);
        when(onlineSchemaChangeFlowableTask.getOscTaskCompleteHandler()).thenReturn(oscTaskCompleteHandler);
    }

    @Test
    public void start_shouldCallStartOnTaskHandlerAndUpdateFlowInstanceStatus() throws Exception {
        // Given
        DelegateExecution execution = mock(DelegateExecution.class);
        ExecutionEntity executionEntity = new ExecutionEntity();
        when(execution.getId()).thenReturn(1L);
        when(execution.getVariables()).thenReturn(executionEntity);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "schemaName";
        continueOnError = false;
        OnlineSchemaChangeContextHolder.clear();
        ScheduleEntity schedule = createScheduleEntity(connectionConfig, parameter, schema);
        List<ScheduleTaskEntity> tasks = parameter.generateSubTaskParameters(connectionConfig, schema).stream()
                .map(param -> createScheduleTaskEntity(schedule.getId(), param)).collect(Collectors.toList());

        when(quartzJobService.scheduleJob(anyString(), anyString(), any(TriggerConfig.class), anyLong()))
                .thenReturn(true);
        when(oscTaskCompleteHandler.start(schedule.getId(), tasks.get(0).getId())).thenReturn(true);

        // When
        onlineSchemaChangeFlowableTask.start(1L, null, execution);

        // Then
        verify(quartzJobService).scheduleJob(anyString(), anyString(), any(TriggerConfig.class), eq(schedule.getId()));
        verify(oscTaskCompleteHandler).start(schedule.getId(), tasks.get(0).getId());
        verify(scheduleService).updateFlowInstanceStatus(FlowStatus.EXECUTION_STARTED, 1L);
    }

    // Add other test cases for other methods in the class.

    private ScheduleEntity createScheduleEntity(ConnectionConfig connectionConfig,
            OnlineSchemaChangeParameters parameter, String schema) {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setConnectionId(connectionConfig.id());
        scheduleEntity.setDatabaseName(schema);
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
        return scheduleService.create(scheduleEntity);
    }

    private ScheduleTaskEntity createScheduleTaskEntity(Long scheduleId,
            OnlineSchemaChangeScheduleTaskParameters param) {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
        scheduleTaskEntity.setJobName(scheduleId.toString());
        scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        scheduleTaskEntity.setProgressPercentage(0.0);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(param));
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(param)));
        scheduleTaskEntity.setFireTime(new Date());
        return scheduleTaskRepository.saveAndFlush(scheduleTaskEntity);
    }
}