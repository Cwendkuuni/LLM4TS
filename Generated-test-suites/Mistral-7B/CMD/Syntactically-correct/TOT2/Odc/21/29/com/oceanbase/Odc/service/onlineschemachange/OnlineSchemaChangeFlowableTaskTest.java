import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.impl.persistence.entity.TaskEntityImpl;
import org.flowable.engine.impl.test.FlowableTestAbstract;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTaskTests;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.MisfireStrategy;
import com.oceanbase.odc.service.task.model.QuartzJobData;
import com.oceanbase.odc.service.task.model.TriggerConfigImpl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

class OnlineSchemaChangeFlowableTaskTests extends FlowableTestAbstract {

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private ScheduleTaskService scheduleTaskService;

    @MockBean
    private QuartzJobService quartzJobService;

    @MockBean
    private OrganizationService organizationService;

    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Autowired
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @Test
    void testStart() {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId("connectionId");
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setContinueOnError(true);
        String schema = "schemaName";

        ExecutionEntity execution = Mockito.mock(ExecutionEntity.class);
        TaskEntityImpl taskEntity = new TaskEntityImpl();
        taskEntity.setId(1L);
        Mockito.when(runtimeService().createExecutionQuery().executionQuery().singleResult()).thenReturn(execution);
        Mockito.when(taskService().start(1L)).thenReturn(null);
        Mockito.when(taskService().detail(1L)).thenReturn(taskEntity);

        // When
        onlineSchemaChangeFlowableTask.start(1L, taskService(), execution);

        // Then
        Assertions.assertNull(onlineSchemaChangeFlowableTask.getStatus());
        Assertions.assertEquals(1L, onlineSchemaChangeFlowableTask.getScheduleId());
        Assertions.assertEquals(1L, onlineSchemaChangeFlowableTask.getCreatorId());
        Assertions.assertEquals(1L, onlineSchemaChangeFlowableTask.getOrganizationId());
        Assertions.assertTrue(onlineSchemaChangeFlowableTask.isContinueOnError());

        // Check that the schedule task was created
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setConnectionId(connectionConfig.getId());
        scheduleEntity.setDatabaseName(schema);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setStatus(ScheduleStatus.ENABLED);
        scheduleEntity.setAllowConcurrent(false);
        scheduleEntity.setCreatorId(1L);
        scheduleEntity.setOrganizationId(1L);
        scheduleEntity.setProjectId(1L);
        scheduleEntity.setDatabaseId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(onlineSchemaChangeParameters));
        TriggerConfig triggerConfig = new TriggerConfigImpl();
        triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
        triggerConfig.setCronExpression("0/10 * * * * ?");
        scheduleEntity.setTriggerConfigJson(JsonUtils.toJson(triggerConfig));
        scheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        scheduleTaskEntity.setProgressPercentage(0.0);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskParameters(onlineSchemaChangeParameters)));
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(onlineSchemaChangeParameters)));
        scheduleTaskEntity.setFireTime(new Date());
        Page<ScheduleTaskEntity> tasks = new PageImpl<>(ListUtils.singletonList(scheduleTaskEntity));
        Mockito.when(scheduleTaskService().listTask(Pageable.unpaged(), scheduleEntity.getId())).thenReturn(tasks);
        Mockito.verify(quartzJobService, Mockito.times(1)).createJob(Mockito.anyString(), Mockito.any(OnlineSchemaChangeFlowableTask.class));
    }

    @Test
    void testIsSuccessful() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);

        // Then
        Assertions.assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    void testIsFailure() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);

        // Then
        Assertions.assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    void testOnFailure() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = Mockito.spy(new OnlineSchemaChangeFlowableTask());

        // When
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService());

        // Then
        Mockito.verify(taskService(), Mockito.times(1)).cancel(1L);
    }

    @Test
    void testOnSuccessful() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = Mockito.spy(new OnlineSchemaChangeFlowableTask());
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult(List.of());
        TaskEntityImpl taskEntity = new TaskEntityImpl();
        taskEntity.setId(1L);
        taskEntity.setStatus(TaskStatus.DONE);
        taskEntity.setResultJson(JsonUtils.toJson(onlineSchemaChangeTaskResult));
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId("connectionId");
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        onlineSchemaChangeScheduleTaskParameters.setConnectionConfig(connectionConfig);
        onlineSchemaChangeScheduleTaskParameters.setSchemaName("schemaName");
        Mockito.when(taskService().detail(1L)).thenReturn(taskEntity);

        // When
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService());

        // Then
        Mockito.verify(taskService(), Mockito.times(1)).update(taskEntity);
    }

    @Test
    void testOnTimeout() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = Mockito.spy(new OnlineSchemaChangeFlowableTask());

        // When
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService());

        // Then
        Mockito.verify(taskService(), Mockito.times(1)).cancel(1L);
    }

    @Test
    void testOnProgressUpdate() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = Mockito.spy(new OnlineSchemaChangeFlowableTask());
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId("connectionId");
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        onlineSchemaChangeScheduleTaskParameters.setConnectionConfig(connectionConfig);
        onlineSchemaChangeScheduleTaskParameters.setSchemaName("schemaName");
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setConnectionId(connectionConfig.getId());
        scheduleEntity.setDatabaseName("databaseName");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setStatus(ScheduleStatus.ENABLED);
        scheduleEntity.setAllowConcurrent(false);
        scheduleEntity.setCreatorId(1L);
        scheduleEntity.setOrganizationId(1L);
        scheduleEntity.setProjectId(1L);
        scheduleEntity.setDatabaseId(1L);
        TriggerConfig triggerConfig = new TriggerConfigImpl();
        triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
        triggerConfig.setCronExpression("0/10 * * * * ?");
        scheduleEntity.setTriggerConfigJson(JsonUtils.toJson(triggerConfig));
        scheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
        ScheduleTaskEntity scheduleTaskEntity1 = new ScheduleTaskEntity();
        scheduleTaskEntity1.setStatus(TaskStatus.RUNNING);
        scheduleTaskEntity1.setJobName("1");
        scheduleTaskEntity1.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        scheduleTaskEntity1.setProgressPercentage(10.0);
        ScheduleTaskEntity scheduleTaskEntity2 = new ScheduleTaskEntity();
        scheduleTaskEntity2.setStatus(TaskStatus.RUNNING);
        scheduleTaskEntity2.setJobName("2");
        scheduleTaskEntity2.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        scheduleTaskEntity2.setProgressPercentage(20.0);
        Page<ScheduleTaskEntity> tasks = new PageImpl<>(List.of(scheduleTaskEntity1, scheduleTaskEntity2));
        Pageable pageable = Pageable.unpaged();
        Mockito.when(runtimeService().createExecutionQuery().executionQuery().singleResult()).thenReturn(new ExecutionEntityImpl());
        Mockito.when(taskService().detail(1L)).thenReturn(new TaskEntityImpl());
        Mockito.when(scheduleTaskService().listTask(pageable, scheduleEntity.getId())).thenReturn(tasks);
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult1 = new OnlineSchemaChangeTaskResult(List.of());
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult2 = new OnlineSchemaChangeTaskResult(List.of());
        TaskEntityImpl taskEntity1 = new TaskEntityImpl();
        taskEntity1.setId(1L);
        taskEntity1.setStatus(TaskStatus.RUNNING);
        taskEntity1.setResultJson(JsonUtils.toJson(onlineSchemaChangeTaskResult1));
        TaskEntityImpl taskEntity2 = new TaskEntityImpl();
        taskEntity2.setId(2L);
        taskEntity2.setStatus(TaskStatus.RUNNING);
        taskEntity2.setResultJson(JsonUtils.toJson(onlineSchemaChangeTaskResult2));
        Mockito.when(taskService().update(taskEntity1)).thenReturn(taskEntity1);
        Mockito.when(taskService().update(taskEntity2)).thenReturn(taskEntity2);
        Mockito.when(oscTaskCompleteHandler.start(scheduleTaskEntity1.getId())).thenReturn(Boolean.TRUE);
        Mockito.when(oscTaskCompleteHandler.start(scheduleTaskEntity2.getId())).thenReturn(Boolean.TRUE);

        // When
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService());

        // Then
        Mockito.verify(taskService(), Mockito.times(2)).update(taskEntity1, taskEntity2);
        Mockito.verify(oscTaskCompleteHandler, Mockito.times(2)).start(scheduleTaskEntity1.getId());
    }
}