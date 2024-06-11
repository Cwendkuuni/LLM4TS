package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.context.Context;
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
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

/**
 * Test class for OnlineSchemaChangeFlowableTask
 */
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private QuartzJobService quartzJobService;

    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private Context context;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(context.getProcessEngineConfiguration()).thenReturn(mockProcessEngineConfiguration());
        when(scheduleService.create(any())).thenReturn(mockScheduleEntity());
    }

    @Test
    public void testStartSuccess() {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        when(organizationService.getOrganizationId(any(), anyLong())).thenReturn(1L);

        // When
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getId()).thenReturn(1L);
        when(execution.getVariable("online_schema_change_parameters")).thenReturn(onlineSchemaChangeParameters);
        when(execution.getVariable("connection_config")).thenReturn(connectionConfig);
        when(execution.getVariable("schema_name")).thenReturn(schema);
        OnlineSchemaChangeFlowableTask.checkTaskCronExpression = "0/1  *  *  *  * ?";
        when(quartzJobService.scheduleJob(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(1L);
        when(taskHandler.start(anyLong(), anyLong())).thenReturn(null);

        // Then
        when(taskHandler.start(anyLong(), anyLong())).thenAnswer(invocationOnMock -> null);
        assertNull(onlineSchemaChangeFlowableTask.start(1L, null, execution));

        verify(taskHandler, times(1)).start(eq(1L), eq(1L));
        verify(quartzJobService, times(1)).scheduleJob(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testStartFailed() {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        when(organizationService.getOrganizationId(any(), anyLong())).thenReturn(1L);

        // When & Then
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getId()).thenReturn(1L);
        when(execution.getVariable("online_schema_change_parameters")).thenReturn(onlineSchemaChangeParameters);
        when(execution.getVariable("connection_config")).thenReturn(connectionConfig);
        when(execution.getVariable("schema_name")).thenReturn(schema);
        OnlineSchemaChangeFlowableTask.checkTaskCronExpression = "0/1  *  *  *  * ?";
        when(quartzJobService.scheduleJob(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> onlineSchemaChangeFlowableTask.start(1L, null, execution));

        verify(quartzJobService, times(1)).scheduleJob(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testIsSuccessful() {
        // Given
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.DONE);

        // When
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());
        when(onlineSchemaChangeFlowableTask.getStatus()).thenReturn(taskEntity.getStatus());

        // Then
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        // Given
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.FAILED);

        // When
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());
        when(onlineSchemaChangeFlowableTask.getStatus()).thenReturn(taskEntity.getStatus());

        // Then
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());

        // When
        long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        when(taskService.detail(taskId)).thenReturn(new TaskEntity());
        when(taskService.cancel(taskId)).thenReturn(true);
        when(onlineSchemaChangeFlowableTask.getTaskService()).thenReturn(taskService);

        // Then
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        verify(taskService, times(1)).cancel(taskId);
    }

    @Test
    public void testOnSuccessful() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());
        ProcessInstance processInstance = mock(ProcessInstance.class);

        // When
        long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        when(taskService.detail(taskId)).thenReturn(processInstance);
        when(onlineSchemaChangeFlowableTask.getTaskService()).thenReturn(taskService);

        // Then
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        verify(processInstance, times(1)).setVariableLocal("online_schema_change_result",
                any(Map.class));
        verify(taskService, times(1)).update(processInstance);
    }

    @Test
    public void testOnTimeout() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());

        // When
        long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        when(taskService.detail(taskId)).thenReturn(null);
        when(onlineSchemaChangeFlowableTask.getTaskService()).thenReturn(taskService);

        // Then
        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        verify(taskService, never()).cancel(taskId);
    }

    @Test
    public void testOnProgressUpdate() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());

        // When
        TaskService taskService = mock(TaskService.class);
        when(taskService.getRunningTaskInstanceCountByProcessDefinitionIdAndStatus(anyLong(), eq(TaskStatus.RUNNING)))
                .thenReturn(1L);
        when(onlineSchemaChangeFlowableTask.getTaskService()).thenReturn(taskService);

        // Then
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testCancel() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());
        TaskService taskService = mock(TaskService.class);

        // When
        long taskId = 1L;
        when(taskService.cancel(taskId)).thenReturn(true);

        // Then
        assertTrue(onlineSchemaChangeFlowableTask.cancel(false, taskId, taskService));
    }

    @Test
    public void testIsCancelled() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        TaskStatus status = TaskStatus.CANCELED;

        // When
        onlineSchemaChangeFlowableTask.setStatus(status);

        // Then
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    private ProcessEngineConfiguration mockProcessEngineConfiguration() {
        ProcessEngineConfiguration processEngineConfiguration = mock(ProcessEngineConfiguration.class);
        when(processEngineConfiguration.getActivityBehaviorFactory()).thenReturn(mock(ActivityBehaviorFactory.class));
        return processEngineConfiguration;
    }

    private ScheduleEntity mockScheduleEntity() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        return scheduleEntity;
    }

}