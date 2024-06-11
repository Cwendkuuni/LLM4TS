package com.oceanbase.odc.service.onlineschemachange;

import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
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
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity;
import com.oceanbase.odc.service.task.TaskService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for OnlineSchemaChangeFlowableTask
 */
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Autowired
    private ConnectionConfig connectionConfig;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private QuartzJobService quartzJobService;

    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleTaskService scheduleTaskService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStartTask() throws Exception {
        // given
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        parameter.setContinueOnError(true);
        String schema = "test_schema";

        TaskEntity taskEntity = Mockito.mock(TaskEntity.class);
        when(taskEntity.getId()).thenReturn(1L);
        when(taskEntity.getStatus()).thenReturn(TaskStatus.APPROVED);

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        when(execution.getVariable("onlineSchemaChangeParameters")).thenReturn(parameter);
        when(execution.getVariable("connectionConfig")).thenReturn(connectionConfig);
        when(execution.getVariable("schemaName")).thenReturn(schema);

        when(organizationService.getCurrentOrganizationId()).thenReturn(1L);
        when(organizationService.getCurrentUserId()).thenReturn(1L);

        ProcessInstance processInstance = mock(ProcessInstance.class);
        when(taskService.createProcessInstance(FlowTaskUtil.getOnlineSchemaChangeFlowDefinitionKey(), execution))
                .thenReturn(processInstance);

        when(quartzJobService.scheduleJob(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(JobType.class),
                Mockito.any(ScheduleEntity.class), Mockito.any(TriggerConfig.class), Mockito.any(TriggerStrategy.class)))
                .thenReturn(1L);

        // when
        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        // then
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setConnectionId(connectionConfig.id());
        scheduleEntity.setDatabaseName(schema);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setStatus(ScheduleStatus.ENABLED);
        scheduleEntity.setCreatorId(1L);
        scheduleEntity.setOrganizationId(1L);
        scheduleEntity.setProjectId(1L);
        scheduleEntity.setDatabaseId(1L);
        scheduleEntity.setModifierId(1L);
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
        triggerConfig.setCronExpression("0/10 * * * * ?");
        scheduleEntity.setTriggerConfigJson(JsonUtils.toJson(triggerConfig));
        scheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NULL);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(parameter));
        assertNotNull(scheduleService.create(scheduleEntity));

        List<OnlineSchemaChangeScheduleTaskParameters> subTasks = parameter.generateSubTaskParameters(connectionConfig, schema);
        ScheduleTaskEntity firstTask = createScheduleTaskEntity(1L, subTasks.get(0));
        assertEquals(TaskStatus.RUNNING, firstTask.getStatus());
    }

    private ScheduleTaskEntity createScheduleTaskEntity(long scheduleId, OnlineSchemaChangeScheduleTaskParameters param) {
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