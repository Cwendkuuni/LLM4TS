package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.After;
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
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.JobType;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskStatus;

/**
 * Test class for OnlineSchemaChangeFlowableTask
 *
 * @author yaobin
 * @date 2023-05-23
 */
@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private QuartzJobService quartzJobService;

    @Mock
    private TaskService taskService;

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private ProcessInstance processInstance;

    @Mock
    private DelegateExecution execution;

    @Mock
    private User creator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(connectionConfig.id()).thenReturn(1L);
        when(organizationService.getCreatorById(anyLong())).thenReturn(creator);
    }

    @After
    public void tearDown() {
        reset(connectionConfig, scheduleRepository, scheduleService, scheduleTaskRepository,
                quartzJobService, taskService, organizationService, processInstance, execution);
    }

    @Test
    public void testStart() throws Exception {
        when(execution.getId()).thenReturn(1L);
        when(flowTaskUtil.getOnlineSchemaChangeParameter(any())).thenReturn(new OnlineSchemaChangeParameters());
        when(flowTaskUtil.getTaskCreator(any())).thenReturn(creator);
        when(flowTaskUtil.getConnectionConfig(any())).thenReturn(connectionConfig);
        when(flowTaskUtil.getSchemaName(any())).thenReturn("schemaName");

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        verify(taskService, times(1)).start(1L);
        verify(organizationService, times(1)).getCreatorById(anyLong());
        verify(onlineSchemaChangeFlowableTask, times(1)).createScheduleEntity(eq(connectionConfig), any(), eq("schemaName"));
        verify(onlineSchemaChangeFlowableTask, times(1)).createScheduleTaskEntity(eq(1L), any());
    }

    @Test(expected = Exception.class)
    public void testStartWithException() throws Exception {
        when(execution.getId()).thenReturn(1L);
        when(flowTaskUtil.getOnlineSchemaChangeParameter(any())).thenThrow(new Exception());

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        verify(taskService, never()).start(1L);
    }

    @Test(expected = Exception.class)
    public void testCreateScheduleEntityWithException() throws Exception {
        when(flowTaskUtil.getOnlineSchemaChangeParameter(any())).thenReturn(new OnlineSchemaChangeParameters());
        when(flowTaskUtil.getConnectionConfig(any())).thenReturn(connectionConfig);
        when(flowTaskUtil.getSchemaName(any())).thenReturn("schemaName");

        doThrow(new Exception()).when(onlineSchemaChangeFlowableTask).createScheduleEntity(eq(connectionConfig), any(), eq("schemaName"));

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        verify(taskService, never()).start(1L);
        verify(onlineSchemaChangeFlowableTask, times(1)).createScheduleEntity(eq(connectionConfig), any(), eq("schemaName"));
    }

    @Test
    public void testIsSuccessful() {
        TaskStatus status = TaskStatus.DONE;
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());

        status = TaskStatus.FAILED;
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());

        status = TaskStatus.RUNNING;
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        TaskStatus status = TaskStatus.DONE;
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());

        status = TaskStatus.FAILED;
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());

        status = TaskStatus.RUNNING;
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() throws Exception {
        long taskId = 1L;
        when(taskService.detail(taskId)).thenReturn(new TaskEntity());

        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        verify(log, times(1)).warn("Online schema change task failed, taskId={}", taskId);
        verify(taskService, times(1)).onFailure(taskId, taskService);
    }

    @Test
    public void testOnSuccessful() throws Exception {
        long taskId = 1L;

        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        verify(log, times(1)).info("Online schema change task succeed, taskId={}", taskId);
        verify(onlineSchemaChangeFlowableTask, times(1)).updateFlowInstanceStatus(eq(FlowStatus.EXECUTION_SUCCEEDED));
        verify(taskService, times(1)).onSuccessful(taskId, taskService);
    }

    @Test
    public void testOnTimeout() throws Exception {
        long taskId = 1L;

        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        verify(log, times(1)).warn("Online schema change task timeout, taskId={}", taskId);
        verify(taskService, times(1)).onTimeout(taskId, taskService);
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        long taskId = 1L;
        Page<ScheduleTaskEntity> tasks = Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity())
                .stream().collect(Collectors.toList());
        when(scheduleTaskService.listTask(Pageable.unpaged(), eq(1L))).thenReturn(tasks.stream().collect(Collectors.toPageable(Pageable.unpaged())));

        when(singleTaskPercentage(any())).thenReturn(0.0);
        when(tasks.getSize()).thenReturn(2);

        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

        verify(log, never()).info("List schedule task size is 0 by scheduleId {}", eq(1L));
        verify(taskService, times(1)).update(any());
    }

    @Test
    public void testCancel() throws Exception {
        boolean canceled = false;
        long taskId = 1L;

        onlineSchemaChangeFlowableTask.cancel(false, taskId, taskService);

        verify(taskService, times(1)).cancel(taskId);
        verify(onlineSchemaChangeFlowableTask, times(1)).terminate(eq(1L), anyLong());
        verify(onlineSchemaChangeFlowableTask, times(1)).setStatus(eq(TaskStatus.CANCELED));
    }

    @Test
    public void testIsCancelled() {
        TaskStatus status = TaskStatus.DONE;
        assertFalse(onlineSchemaChangeFlowableTask.isCancelled());

        status = TaskStatus.CANCELED;
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() throws Exception {
        OnlineSchemaChangeParameters param = new OnlineSchemaChangeParameters();
        String schema = "schemaName";

        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, param, schema);

        assertEquals(1L, scheduleEntity.getConnectionId());
        assertEquals("schemaName", scheduleEntity.getDatabaseName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(false, scheduleEntity.isAllowConcurrent());
        assertEquals(creator.getId(), scheduleEntity.getCreatorId());
        assertEquals(organizationId, scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals(creator.getId(), scheduleEntity.getModifierId());
        assertEquals("triggerConfigJson", scheduleEntity.getTriggerConfigJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, scheduleEntity.getMisfireStrategy());
        assertEquals(JsonUtils.toJson(param), scheduleEntity.getJobParametersJson());

        TriggerConfig triggerConfig = JsonUtils.parseJson(scheduleEntity.getTriggerConfigJson(), TriggerConfig.class);
        assertEquals(TriggerStrategy.CRON, triggerConfig.getTriggerStrategy());
        assertEquals(checkTaskCronExpression, triggerConfig.getCronExpression());
    }

    @Test
    public void testCreateScheduleTaskEntity() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);

        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleEntity.getId(), param);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("JobName_1", scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage());
        assertEquals(JsonUtils.toJson(param), scheduleTaskEntity.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(param)), scheduleTaskEntity.getResultJson());
        assertEquals(new Date(), scheduleTaskEntity.getFireTime());
    }
}