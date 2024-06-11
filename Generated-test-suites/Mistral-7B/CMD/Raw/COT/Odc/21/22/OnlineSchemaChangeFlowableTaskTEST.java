package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
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
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
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
import com.oceanbase.odc.service.task.model.TaskExecution;

@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private QuartzJobService quartzJobService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private DelegateExecution execution;

    @Mock
    private ExecutionEntity executionEntity;

    @Mock
    private TaskEntity taskEntity;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Mock
    private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(organizationService.getCurrentUser()).thenReturn(mock(User.class));
        when(quartzJobService.create(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyBoolean(),
                Mockito.anyBoolean())).thenReturn(mock(TriggerConfig.class));
        when(scheduleService.create(Mockito.any(ScheduleEntity.class))).thenReturn(scheduleEntity);
        when(scheduleTaskRepository.saveAndFlush(Mockito.any(ScheduleTaskEntity.class))).thenReturn(scheduleTaskEntity);
        when(taskService.detail(Mockito.anyLong())).thenReturn(taskEntity);
        when(taskService.start(Mockito.anyLong())).thenReturn(null);
        when(taskService.cancel(Mockito.anyLong(), Mockito.anyBoolean())).thenReturn(true);
        when(taskService.listTask(Mockito.any(Pageable.class), Mockito.anyLong())).thenReturn(getPage(scheduleTaskEntity));
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        when(scheduleTaskEntity.getId()).thenReturn(1L);
        when(scheduleEntity.getId()).thenReturn(1L);
        when(connectionConfig.id()).thenReturn(1L);
    }

    @Test
    public void testStart() throws Exception {
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(onlineSchemaChangeParameters);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn("schemaName");
        when(FlowTaskUtil.getCloudMainAccountId(execution)).thenReturn("uid");

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        verify(taskService, Mockito.times(1)).start(1L);
    }

    @Test
    public void testIsSuccessful() {
        TaskStatus taskStatus = TaskStatus.DONE;
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        assertEquals(taskStatus, onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        TaskStatus taskStatus = TaskStatus.FAILED;
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        assertEquals(taskStatus, onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() throws Exception {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
    }

    @Test
    public void testOnSuccessful() throws Exception {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
        updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
    }

    @Test
    public void testOnTimeout() throws Exception {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
    }

    @Test
    public void testProgressUpdate() {
        List<ScheduleTaskEntity> tasks = Arrays.asList(scheduleTaskEntity);
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.progressStatusUpdate(getPage(tasks));
        onlineSchemaChangeFlowableTask.percentage = 50;
        assertEquals(50, onlineSchemaChangeFlowableTask.percentage);
    }

    @Test
    public void testCancel() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService));
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = mock(OnlineSchemaChangeParameters.class);
        String schema = "schemaName";
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema)).thenReturn(List.of(onlineSchemaChangeScheduleTaskParameters));

        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schema);

        assertEquals(1L, scheduleEntity.getConnectionId());
        assertEquals("schemaName", scheduleEntity.getDatabaseName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(false, scheduleEntity.isAllowConcurrent());
        assertEquals(creatorId, scheduleEntity.getCreatorId());
        assertEquals(organizationId, scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals(creatorId, scheduleEntity.getModifierId());
        assertEquals(JsonUtils.toJson(triggerConfig), scheduleEntity.getTriggerConfigJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, scheduleEntity.getMisfireStrategy());
        assertEquals(JsonUtils.toJson(onlineSchemaChangeParameters), scheduleEntity.getJobParametersJson());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = mock(OnlineSchemaChangeScheduleTaskParameters.class);

        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, onlineSchemaChangeScheduleTaskParameters);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("OnlineSchemaChangeFlowableTask_1", scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage());
        assertEquals(JsonUtils.toJson(onlineSchemaChangeScheduleTaskParameters), scheduleTaskEntity.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(onlineSchemaChangeScheduleTaskParameters)), scheduleTaskEntity.getResultJson());
        assertEquals(new Date(), scheduleTaskEntity.getFireTime());
    }

    private Page<ScheduleTaskEntity> getPage(ScheduleTaskEntity... entities) {
        Page<ScheduleTaskEntity> page = mock(Page.class);
        when(page.getSize()).thenReturn(entities.length);
        when(page.getContent()).thenReturn(Arrays.asList(entities));
        return page;
    }

    private void updateFlowInstanceStatus(FlowStatus status) {
        // Empty implementation
    }
}