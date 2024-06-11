package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
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
import com.oceanbase.odc.service.task.model.TaskResult;
import com.oceanbase.odc.service.task.model.TaskStatusUpdateResult;

/**
 * Test for OnlineSchemaChangeFlowableTask
 */
public class OnlineSchemaChangeFlowableTaskTest {
    private static final String SCHEMA = "testSchema";
    private static final String UID = "testUid";
    private static final int TASK_COUNT = 3;

    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @Autowired
    private QuartzJobService quartzJobService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setId("connectionId");
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSchemaName(SCHEMA);
        onlineSchemaChangeParameters.setContinueOnError(true);
        onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        onlineSchemaChangeScheduleTaskParameters.setSchemaName(SCHEMA);
        onlineSchemaChangeScheduleTaskParameters.setUid(UID);
    }

    @Test
    public void testStart() throws Exception {
        // given
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getId()).thenReturn(1L);
        when(execution.getVariables()).thenReturn(new Object[]{"connectionConfig", connectionConfig});

        when(organizationService.getCurrentUser()).thenReturn(new User());
        when(taskService.detail(1L)).thenReturn(new TaskEntity());
        when(quartzJobService.createJob(argThat(contains(OnlineSchemaChangeFlowableTask.class)), eq(1L)))
                .thenReturn(1L);
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());

        // when
        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        // then
        ArgumentCaptor<List<OnlineSchemaChangeScheduleTaskParameters>> taskParametersCaptor = ArgumentCaptor
                .forClass(java.util.List.class);
        verify(scheduleTaskService).createTasks(eq(1L), taskParametersCaptor.capture());
        List<OnlineSchemaChangeScheduleTaskParameters> tasks = taskParametersCaptor.getValue();
        assertEquals(TASK_COUNT, tasks.size());

        verify(onlineSchemaChangeFlowableTask).createScheduleEntity(connectionConfig, onlineSchemaChangeParameters,
                SCHEMA);
        verify(onlineSchemaChangeFlowableTask).createScheduleTaskEntity(1L, onlineSchemaChangeScheduleTaskParameters);
        verify(quartzJobService).createJob(eq(OnlineSchemaChangeFlowableTask.class), eq(1L));
        verify(taskService).start(1L);
    }

    @Test
    public void testIsSuccessful() {
        // given
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);

        // when
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

        // then
        assertEquals(true, result);
    }

    @Test
    public void testIsFailure() {
        // given
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);

        // when
        boolean result = onlineSchemaChangeFlowableTask.isFailure();

        // then
        assertEquals(true, result);
    }

    @Test
    public void testOnFailure() throws Exception {
        // given
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getId()).thenReturn(1L);

        when(taskService.detail(1L)).thenReturn(new TaskEntity());

        // when
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

        // then
        verify(log).warn("Online schema change task failed, taskId={}", 1L);
    }

    @Test
    public void testOnSuccessful() throws Exception {
        // given
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getId()).thenReturn(1L);
        when(execution.getVariables()).thenReturn(new Object[]{"result", new OnlineSchemaChangeTaskResult(Arrays.asList(
                new ScheduleTaskEntity()))});

        when(taskService.detail(1L)).thenReturn(new TaskEntity());

        // when
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

        // then
        verify(taskService).update(any(TaskEntity.class));
        verify(log).info("Online schema change task succeed, taskId={}", 1L);
    }

    @Test
    public void testOnTimeout() {
        // given

        // when
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);

        // then
        verify(log).warn("Online schema change task timeout, taskId={}", 1L);
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        // given
        List<ScheduleTaskEntity> tasks = Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity(),
                new ScheduleTaskEntity());
        Page<ScheduleTaskEntity> page = new PageImpl<>(tasks);

        when(taskService.listTask(any(Pageable.class), eq(1L))).thenReturn(page);

        // when
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        // then
        verify(taskService).update(any(TaskEntity.class));

        ArgumentCaptor<TaskStatusUpdateResult> taskStatusUpdateResultCaptor = ArgumentCaptor
                .forClass(com.oceanbase.odc.service.task.model.TaskStatusUpdateResult.class);
        verify(taskService).update(taskStatusUpdateResultCaptor.capture());
        TaskStatusUpdateResult taskStatusUpdateResult = taskStatusUpdateResultCaptor.getValue();
        assertEquals(new OnlineSchemaChangeTaskResult(tasks), JsonUtils.fromJson(taskStatusUpdateResult.getResultJson(),
                OnlineSchemaChangeTaskResult.class));
    }

    @Test
    public void testCancel() throws Exception {
        // given
        when(taskService.cancel(1L)).thenReturn(true);

        // when
        boolean result = onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        // then
        verify(taskService).cancel(1L);
        verify(log).info("Online schema change task canceled, taskId={}", 1L);
        assertEquals(true, result);
    }

    @Test
    public void testIsCancelled() {
        // given
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);

        // when
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        // then
        assertEquals(true, result);
    }

    @Test
    public void testCreateScheduleEntity() throws Exception {
        // given

        // when
        ScheduleEntity result = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig,
                onlineSchemaChangeParameters, SCHEMA);

        // then
        assertEquals(1L, result.getId());
        assertEquals(connectionConfig.getId(), result.getConnectionId());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, result.getJobType());
        assertEquals(ScheduleStatus.ENABLED, result.getStatus());
        assertEquals(false, result.isAllowConcurrent());
        assertEquals(creatorId, result.getCreatorId());
        assertEquals(organizationId, result.getOrganizationId());
        assertEquals(1L, result.getProjectId());
        assertEquals(1L, result.getDatabaseId());
        assertEquals(JsonUtils.toJson(new TriggerConfig()), result.getTriggerConfigJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, result.getMisfireStrategy());
        assertEquals(JsonUtils.toJson(onlineSchemaChangeParameters), result.getJobParametersJson());
    }

    @Test
    public void testCreateScheduleTaskEntity() throws Exception {
        // given

        // when
        ScheduleTaskEntity result = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L,
                onlineSchemaChangeScheduleTaskParameters);

        // then
        assertEquals(1L, result.getId());
        assertEquals(TaskStatus.PREPARING, result.getStatus());
        assertEquals("onlineSchemaChangeFlowableTask", result.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), result.getJobGroup());
        assertEquals(0.0, result.getProgressPercentage());
        assertEquals(JsonUtils.toJson(onlineSchemaChangeScheduleTaskParameters), result.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(onlineSchemaChangeScheduleTaskParameters)),
                result.getResultJson());
        assertEquals(new Date(), result.getFireTime());
    }

    @Test
    public void testListScheduleTasks() throws Exception {
        // given
        List<ScheduleTaskEntity> tasks = Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity(),
                new ScheduleTaskEntity());
        Page<ScheduleTaskEntity> page = new PageImpl<>(tasks);

        when(scheduleTaskRepository.findAll(any(Pageable.class))).thenReturn(page);

        // when
        List<ScheduleTaskEntity> result = onlineSchemaChangeFlowableTask.listScheduleTasks(Pageable.unpaged());

        // then
        assertEquals(tasks, result);
    }
}