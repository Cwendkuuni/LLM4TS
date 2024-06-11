import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.RandomUtils;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.TaskEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
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
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    private ScheduleService scheduleService;
    private TaskService taskService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private QuartzJobService quartzJobService;
    private OrganizationService organizationService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        scheduleService = mock(ScheduleService.class);
        taskService = mock(TaskService.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        quartzJobService = mock(QuartzJobService.class);
        organizationService = mock(OrganizationService.class);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
        onlineSchemaChangeFlowableTask.setTaskService(taskService);
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
    }

    @Test
    public void testOnlineSchemaChangeSuccess() throws Exception {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setId(1L);
        String schema = "test_schema";
        OnlineSchemaChangeScheduleTaskParameters scheduleTaskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        scheduleTaskParameter.setId(1L);
        scheduleTaskParameter.setUid("test_uid");
        scheduleTaskParameter.setSchema(schema);

        ScheduleEntity scheduleEntity = createScheduleEntity(connectionConfig, parameters, schema);
        ScheduleTaskEntity scheduleTaskEntity = createScheduleTaskEntity(scheduleEntity, scheduleTaskParameter);

        stub(scheduleService.create(any(ScheduleEntity.class)))
                .returns(scheduleEntity);
        stub(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class)))
                .returns(scheduleTaskEntity);
        stub(taskHandler.start(scheduleEntity.getId(), scheduleTaskEntity.getId()))
                .returns(null);

        // When
        onlineSchemaChangeFlowableTask.start(1L, taskService, new ExecutionEntity());

        // Then
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setStatus(TaskStatus.DONE);
        taskEntity.setProgressPercentage(100);
        taskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeTaskResult(ListUtils.emptyList())));

        stub(taskService.detail(1L))
                .returns(taskEntity);

        Page<ScheduleTaskEntity> tasks = new PageImpl<>(ListUtils.emptyList());
        stub(scheduleTaskService.listTask(any(Pageable.class), eq(scheduleEntity.getId())))
                .returns(tasks);

        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        assertEquals(100, onlineSchemaChangeFlowableTask.percentage);
    }

    private ScheduleEntity createScheduleEntity(ConnectionConfig connectionConfig, OnlineSchemaChangeParameters parameters,
            String schema) {
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
        scheduleEntity.setModifierId(scheduleEntity.getCreatorId());
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
        triggerConfig.setCronExpression("0/10 * * * * ?");
        scheduleEntity.setTriggerConfigJson(JsonUtils.toJson(triggerConfig));
        scheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(parameters));
        return scheduleEntity;
    }

    private ScheduleTaskEntity createScheduleTaskEntity(ScheduleEntity scheduleEntity,
            OnlineSchemaChangeScheduleTaskParameters scheduleTaskParameter) {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
        scheduleTaskEntity.setJobName(scheduleEntity.getId().toString());
        scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        scheduleTaskEntity.setProgressPercentage(0.0);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(scheduleTaskParameter));
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(scheduleTaskParameter)));
        scheduleTaskEntity.setFireTime(new Date());
        return scheduleTaskEntity;
    }
}