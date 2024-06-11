package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
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
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskResult;

/**
 * Test class for OnlineSchemaChangeFlowableTask
 */
@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {
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
    private TaskService taskService;
    @Mock
    private BaseODCFlowTaskDelegate<Void> delegate;
    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private TaskEntity taskEntity;
    @Mock
    private ProcessInstance processInstance;

    @Mock
    private ScheduleEntity scheduleEntity;
    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;
    @Mock
    private OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult;
    @Mock
    private OnlineSchemaChangeScheduleTaskResult onlineSchemaChangeScheduleTaskResult;
    @Mock
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    @Mock
    private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(connectionConfig.getId()).thenReturn(1L);
        when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);
        when(scheduleTaskRepository.saveAndFlush(scheduleTaskEntity)).thenReturn(scheduleTaskEntity);
        when(taskService.detail(anyLong())).thenReturn(taskEntity);
        when(taskService.start(anyLong())).thenReturn(true);
        when(taskService.update(taskEntity)).thenReturn(taskEntity);
        when(quartzJobService.terminate(anyLong(), anyLong())).thenReturn(true);
        when(flowTaskUtil.getOnlineSchemaChangeParameter(any(DelegateExecution.class))).thenReturn(onlineSchemaChangeParameters);
        when(flowTaskUtil.getConnectionConfig(any(DelegateExecution.class))).thenReturn(connectionConfig);
        when(flowTaskUtil.getCloudMainAccountId(any(DelegateExecution.class))).thenReturn("uid");
        when(flowTaskUtil.getSchemaName(any(DelegateExecution.class))).thenReturn("schemaName");
        when(scheduleEntity.getId()).thenReturn(1L);
        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleEntity.getStatus()).thenReturn(ScheduleStatus.ENABLED);
        when(scheduleEntity.getCreatorId()).thenReturn(1L);
        when(scheduleEntity.getOrganizationId()).thenReturn(1L);
        when(scheduleEntity.getProjectId()).thenReturn(1L);
        when(scheduleEntity.getDatabaseId()).thenReturn(1L);
        when(scheduleEntity.getModifierId()).thenReturn(1L);
        when(triggerConfig.getTriggerStrategy()).thenReturn(TriggerStrategy.CRON);
        when(triggerConfig.getCronExpression()).thenReturn("0/10 * * * * ?");
        when(scheduleEntity.getMisfireStrategy()).thenReturn(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(onlineSchemaChangeScheduleTaskParameters));
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.PREPARING);
        when(scheduleTaskEntity.getJobName()).thenReturn("jobName");
        when(scheduleTaskEntity.getJobGroup()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        when(scheduleTaskEntity.getProgressPercentage()).thenReturn(0.0);
        when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(onlineSchemaChangeParameters));
        when(scheduleTaskEntity.getResultJson()).thenReturn(JsonUtils.toJson(onlineSchemaChangeTaskResult));
        when(scheduleTaskEntity.getFireTime()).thenReturn(new Date());
        when(onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schemaName")).thenReturn(ListUtils.emptyList());
        when(createScheduleTaskEntity(scheduleEntity, onlineSchemaChangeScheduleTaskParameters)).thenReturn(scheduleTaskEntity);
    }

    @Test
    public void testStart_success() throws Exception {
        // given
        doReturn(true).when(taskService).start(anyLong());
        doReturn(true).when(quartzJobService).terminate(anyLong(), anyLong());

        // when
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = mock(OnlineSchemaChangeParameters.class);
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schemaName = "schemaName";
        when(flowTaskUtil.getOnlineSchemaChangeParameter(any(DelegateExecution.class))).thenReturn(onlineSchemaChangeParameters);
        when(flowTaskUtil.getConnectionConfig(any(DelegateExecution.class))).thenReturn(connectionConfig);
        when(flowTaskUtil.getCloudMainAccountId(any(DelegateExecution.class))).thenReturn("uid");
        when(flowTaskUtil.getSchemaName(any(DelegateExecution.class))).thenReturn(schemaName);

        // when
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.start(1L, taskService, mock(DelegateExecution.class));

        // then
        verify(taskService, times(1)).start(1L);
        verify(quartzJobService, times(1)).terminate(1L, 1L);
        verifyNoMoreInteractions(taskService);
        verifyNoMoreInteractions(quartzJobService);
        verify(onlineSchemaChangeFlowableTask).createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schemaName);
        verify(onlineSchemaChangeFlowableTask).createScheduleTaskEntity(1L, onlineSchemaChangeScheduleTaskParameters);
        verify(onlineSchemaChangeFlowableTask).taskHandler.start(1L, 1L);
    }

    @Test
    public void testStart_exception() throws Exception {
        // given
        doThrow(new RuntimeException("exception")).when(taskService).start(anyLong());

        // when
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = mock(OnlineSchemaChangeParameters.class);
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schemaName = "schemaName";
        when(flowTaskUtil.getOnlineSchemaChangeParameter(any(DelegateExecution.class))).thenReturn(onlineSchemaChangeParameters);
        when(flowTaskUtil.getConnectionConfig(any(DelegateExecution.class))).thenReturn(connectionConfig);
        when(flowTaskUtil.getCloudMainAccountId(any(DelegateExecution.class))).thenReturn("uid");
        when(flowTaskUtil.getSchemaName(any(DelegateExecution.class))).thenReturn(schemaName);

        // when
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.start(1L, taskService, mock(DelegateExecution.class));

        // then
        verify(taskService, times(1)).start(1L);
        verifyThrowable(RuntimeException.class, () -> onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schemaName));
        verifyThrowable(RuntimeException.class, () -> onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, onlineSchemaChangeScheduleTaskParameters));
        verify(onlineSchemaChangeFlowableTask.taskHandler, never()).start(1L, 1L);
    }

    @Test
    public void testIsSuccessful() {
        // given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.status = TaskStatus.RUNNING;

        // when
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

        // then
        assertFalse(result);
    }

    @Test
    public void testIsFailure() {
        // given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;

        // when
        boolean result = onlineSchemaChangeFlowableTask.isFailure();

        // then
        assertTrue(result);
    }

    @Test
    public void testOnFailure() {
        // given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        long taskId = 1L;

        // when
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        // then
        verify(taskService, times(1)).cancel(true, taskId, taskService);
    }

    @Test
    public void testOnSuccessful() {
        // given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        long taskId = 1L;
        TaskResult taskResult = mock(TaskResult.class);
        when(taskService.detail(taskId)).thenReturn(taskEntity);
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(taskResult));
        when(taskService.update(taskEntity)).thenReturn(taskEntity);

        // when
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        // then
        verify(taskService, times(1)).update(taskEntity);
        verifyNoMoreInteractions(taskService);
        verify(onlineSchemaChangeFlowableTask).updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
    }

    @Test
    public void testOnTimeout() {
        // given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        // when
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);

        // then
        verifyNoInteractions(taskService);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    public void testOnProgressUpdate() {
        // given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        Page<ScheduleTaskEntity> scheduleTaskEntities = mock(Page.class);
        when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(scheduleTaskEntities);

        // when
        onlineSchemaChangeFlowableTask.progressStatusUpdate(scheduleTaskEntities);

        // then
        verify(onlineSchemaChangeFlowableTask, times(1)).status = TaskStatus.DONE;
    }

    @Test
    public void testIsCancelled() {
        // given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;

        // when
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        // then
        assertTrue(result);
    }

    @Test
    public void testCancel() {
        // given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        boolean result = onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        // then
        assertTrue(result);
    }

    private ScheduleTaskEntity createScheduleTaskEntity(ScheduleEntity scheduleEntity,
            OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters) {
        return onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleEntity.getId(), onlineSchemaChangeScheduleTaskParameters);
    }
}