package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
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
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeTaskHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.task.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.util.FlowTaskUtil;

/**
 * Test class for OnlineSchemaChangeFlowableTask.
 */
public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private QuartzJobService quartzJobServiceMock;
    private ScheduleTaskService scheduleTaskServiceMock;
    private TaskService taskServiceMock;
    private OrganizationService organizationServiceMock;
    private OnlineSchemaChangeTaskHandler taskHandlerMock;
    private OscTaskCompleteHandler completeHandlerMock;
    private BaseODCFlowTaskDelegate baseODCFlowTaskDelegateMock;
    private ScheduleEntity scheduleEntityMock;
    private ConnectionConfig connectionConfigMock;

    @Before
    public void setUp() {
        quartzJobServiceMock = mock(QuartzJobService.class);
        scheduleTaskServiceMock = mock(ScheduleTaskService.class);
        taskServiceMock = mock(TaskService.class);
        organizationServiceMock = mock(OrganizationService.class);
        taskHandlerMock = mock(OnlineSchemaChangeTaskHandler.class);
        completeHandlerMock = mock(OscTaskCompleteHandler.class);
        baseODCFlowTaskDelegateMock = mock(BaseODCFlowTaskDelegate.class);

        when(quartzJobServiceMock.terminate(anyLong(), anyLong())).thenReturn(true);

        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask() {
            {
                this.quartzJobService = quartzJobServiceMock;
                this.scheduleTaskService = scheduleTaskServiceMock;
                this.taskService = taskServiceMock;
                this.organizationService = organizationServiceMock;
                this.taskHandler = taskHandlerMock;
                this.completeHandler = completeHandlerMock;
                this.baseODCFlowTaskDelegate = baseODCFlowTaskDelegateMock;
            }
        };

        connectionConfigMock = Mockito.spy(new ConnectionConfig());
        when(connectionConfigMock.getId()).thenReturn(1L);

        OnlineSchemaChangeParameters onlineSchemaChangeParameters = Mockito.spy(new OnlineSchemaChangeParameters());
        scheduleEntityMock = Mockito.spy(new ScheduleEntity());
    }

    @Test
    public void testStart() throws Exception {
        // Given
        String schema = "test_schema";
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        Pageable pageable = Pageable.unpaged();

        when(taskServiceMock.start(anyLong())).thenAnswer(invocation -> {
            TaskEntity taskEntity = invocation.getArgument(0);
            taskEntity.setStatus(TaskStatus.RUNNING);
            return "taskId";
        });

        when(organizationServiceMock.getCreator(anyLong())).thenReturn(Mockito.mock(User.class));
        when(organizationServiceMock.getCloudMainAccountId(any(DelegateExecution.class))).thenReturn("test_account_id");
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(any(DelegateExecution.class))).thenReturn(onlineSchemaChangeParameters);
        when(FlowTaskUtil.getConnectionConfig(any(DelegateExecution.class))).thenReturn(connectionConfigMock);
        when(FlowTaskUtil.getSchemaName(any(DelegateExecution.class))).thenReturn(schema);
        when(scheduleServiceMock.create(any(ScheduleEntity.class))).thenReturn(scheduleEntityMock);
        when(onlineSchemaChangeTaskHandlerMock.start(anyLong(), anyLong())).thenReturn(true);

        // When
        onlineSchemaChangeFlowableTask.start(1L, taskServiceMock, Mockito.mock(DelegateExecution.class));

        // Then
        verify(quartzJobServiceMock, times(1)).terminate(scheduleEntityMock.getId(), 0L);
        verify(taskServiceMock, times(1)).start("taskId");
        verify(taskHandlerMock, times(1)).start(scheduleEntityMock.getId(), 0L);
    }

    @Test
    public void testIsSuccessful() {
        // Given
        when(onlineSchemaChangeFlowableTask.status).thenReturn(TaskStatus.DONE);

        // When
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

        // Then
        assertEquals(true, result);
    }

    @Test
    public void testIsFailure() {
        // Given
        when(onlineSchemaChangeFlowableTask.status).thenReturn(TaskStatus.FAILED);

        // When
        boolean result = onlineSchemaChangeFlowableTask.isFailure();

        // Then
        assertEquals(true, result);
    }

    @Test
    public void testOnFailure() {
        // Given
        long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskServiceMock);

        // Then
        verify(log, times(1)).warn("Online schema change task failed, taskId={}", taskId);
        verify(taskServiceMock, times(1)).cancel(taskId);
    }

    @Test
    public void testOnSuccessful() {
        // Given
        long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskServiceMock);

        // Then
        verify(log, times(1)).info("Online schema change task succeed, taskId={}", taskId);
        verify(taskServiceMock, times(1)).update(any(TaskEntity.class));
        verify(onlineSchemaChangeFlowableTask, times(1)).updateFlowInstanceStatus(eq(FlowStatus.EXECUTION_SUCCEEDED));
    }

    @Test
    public void testOnTimeout() {
        // Given
        long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskServiceMock);

        // Then
        verify(log, times(1)).warn("Online schema change task timeout, taskId={}", taskId);
    }

    @Test
    public void testOnProgressUpdate() {
        // Given
        Page<ScheduleTaskEntity> tasks = Mockito.mock(Page.class);
        when(scheduleTaskServiceMock.listTask(any(Pageable.class), eq(scheduleEntityMock.getId()))).thenReturn(tasks);

        // When
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskServiceMock);

        // Then
        verify(taskServiceMock, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testCreateScheduleEntity() {
        // Given
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
        String schema = "test_schema";

        // When
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig,
                onlineSchemaChangeParameters, schema);

        // Then
        verify(scheduleEntity).setConnectionId(connectionConfig.getId());
        verify(scheduleEntity).setDatabaseName(schema);
        verify(scheduleEntity).setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        verify(scheduleEntity).setStatus(ScheduleStatus.ENABLED);
        verify(scheduleEntity).setAllowConcurrent(false);
        verify(scheduleEntity).setCreatorId(anyLong());
        verify(scheduleEntity).setOrganizationId(anyLong());
        verify(scheduleEntity).setProjectId(1L);
        verify(scheduleEntity).setDatabaseId(1L);
        verify(scheduleEntity).setModifierId(scheduleEntity.getCreatorId());
        verify(scheduleEntity).setJobParametersJson(JsonUtils.toJson(onlineSchemaChangeParameters));
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        // Given
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();

        // When
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId,
                onlineSchemaChangeScheduleTaskParameters);

        // Then
        verify(scheduleTaskEntity).setStatus(TaskStatus.PREPARING);
        verify(scheduleTaskEntity).setJobName(scheduleId.toString());
        verify(scheduleTaskEntity).setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        verify(scheduleTaskEntity).setProgressPercentage(0.0);
        verify(scheduleTaskEntity).setParametersJson(JsonUtils.toJson(onlineSchemaChangeScheduleTaskParameters));
        verify(scheduleTaskEntity).setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(onlineSchemaChangeScheduleTaskParameters)));
        verify(scheduleTaskEntity).setFireTime(new Date());
    }

    @Test
    public void testCancel() {
        // Given
        boolean mayInterruptIfRunning = true;
        long taskId = 1L;

        // When
        boolean result = onlineSchemaChangeFlowableTask.cancel(mayInterruptIfRunning, taskId, taskServiceMock);

        // Then
        verify(quartzJobServiceMock, times(1)).terminate(scheduleEntityMock.getId(), 0L);
        verify(taskServiceMock, times(1)).cancel(taskId);
        verify(onlineSchemaChangeFlowableTask, times(1)).status = TaskStatus.CANCELED;
        assertEquals(true, result);
    }

    @Test
    public void testIsCancelled() {
        // Given
        when(onlineSchemaChangeFlowableTask.status).thenReturn(TaskStatus.CANCELED);

        // When
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        // Then
        assertEquals(true, result);
    }
}