package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
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
import com.oceanbase.odc.service.schedule.model.ScheduleTaskResult;
import com.oceanbase.odc.service.task.TaskService;

@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {
    @MockBean
    private QuartzJobService quartzJobService;
    @MockBean
    private ScheduleService scheduleService;
    @MockBean
    private ScheduleTaskService scheduleTaskService;
    @MockBean
    private OrganizationService organizationService;
    @MockBean
    private TaskService taskService;
    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    @Autowired
    private FlowTaskUtil flowTaskUtil;

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;

    @Before
    public void setup() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setContinueOnError(true);
        onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
    }

    @Test
    public void testCreateScheduleEntity() {
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, "test_schema");

        assertNotNull(scheduleEntity);
        assertEquals(connectionConfig.getId(), scheduleEntity.getConnectionId());
        assertEquals("test_schema", scheduleEntity.getDatabaseName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(false, scheduleEntity.isAllowConcurrent());
        assertEquals(creatorId, scheduleEntity.getCreatorId());
        assertEquals(organizationId, scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertNotNull(scheduleEntity.getModifierId());
        assertEquals(TriggerStrategy.CRON, scheduleEntity.getTriggerStrategy());
        assertEquals(checkTaskCronExpression, scheduleEntity.getTriggerConfigJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, scheduleEntity.getMisfireStrategy());
        assertNotNull(scheduleEntity.getJobParametersJson());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, onlineSchemaChangeScheduleTaskParameters);

        assertNotNull(scheduleTaskEntity);
        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals(Long.toString(scheduleId), scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage(), 0.0);
        assertNotNull(scheduleTaskEntity.getParametersJson());
        assertNotNull(scheduleTaskEntity.getResultJson());
        assertNotNull(scheduleTaskEntity.getFireTime());
    }

    @Test
    public void testStart() throws Exception {
        OnlineSchemaChangeContextHolder.clear();
        Long taskId = 1L;
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(taskId);
        taskEntity.setStatus(TaskStatus.WAITING);
        DelegateExecution execution = new DelegateExecution();
        execution.setId(taskId);
        execution.setVariables(Collections.singletonMap("flowInstanceId", "test_flow_instance_id"));

        when(taskService.detail(taskId)).thenReturn(taskEntity);
        when(flowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(onlineSchemaChangeParameters);
        when(flowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(flowTaskUtil.getSchemaName(execution)).thenReturn("test_schema");
        when(organizationService.getCreator(anyLong())).thenReturn(new User());

        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

        verify(taskService).start(taskId);
        assertEquals(TaskStatus.RUNNING, taskEntity.getStatus());
        assertEquals(creatorId, taskEntity.getCreatorId());
        assertEquals(organizationId, taskEntity.getOrganizationId());
        assertEquals("test_flow_instance_id", OnlineSchemaChangeContextHolder.getTraceInfo().getFlowInstanceId());
        assertEquals(1L, OnlineSchemaChangeContextHolder.getTraceInfo().getTaskId());
        assertEquals(organizationId, OnlineSchemaChangeContextHolder.getTraceInfo().getOrganizationId());
    }

    @Test
    public void testIsSuccessful() {
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful(null, null));
    }

    @Test
    public void testIsFailure() {
        assertFalse(onlineSchemaChangeFlowableTask.isFailure(null, null));
    }

    @Test
    public void testCancel() {
        when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(getPageOfScheduleTasks());

        onlineSchemaChangeFlowableTask.cancel(true, null, taskService);

        verify(quartzJobService).terminate(scheduleId, anyLong());
        assertEquals(TaskStatus.CANCELED, onlineSchemaChangeFlowableTask.getStatus());
        assertTrue(taskService.cancel(null));
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testProgressUpdate() {
        Page<ScheduleTaskEntity> tasks = getPageOfScheduleTasks();

        onlineSchemaChangeFlowableTask.progressUpdate(tasks);

        assertEquals(TaskStatus.DONE, onlineSchemaChangeFlowableTask.getStatus());
    }

    private Page<ScheduleTaskEntity> getPageOfScheduleTasks() {
        List<ScheduleTaskEntity> scheduleTaskEntities = Arrays.asList(
                new ScheduleTaskEntity(),
                new ScheduleTaskEntity()
        );
        return new PageImpl<>(scheduleTaskEntities);
    }
}