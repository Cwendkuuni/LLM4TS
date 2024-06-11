package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.OnlineSchemaChangeFlowableTask;
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
import com.oceanbase.odc.service.schedule.model.TriggerType;
import com.oceanbase.odc.service.task.TaskService;

public class OnlineSchemaChangeFlowableTaskTest {

    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @Autowired
    private QuartzJobService quartzJobService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private String schema;

    @Before
    public void setUp() {
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        onlineSchemaChangeParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
        schema = "testSchema";
    }

    @Test
    public void testStart() throws Exception {
        // Given
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        TaskService taskService = Mockito.mock(TaskService.class);
        when(taskService.start(anyLong())).thenReturn(true);
        User creator = Mockito.mock(User.class);
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(creator);
        when(creator.getId()).thenReturn(1L);
        when(creator.getOrganizationId()).thenReturn(1L);

        OnlineSchemaChangeContextHolder holder = Mockito.spy(OnlineSchemaChangeContextHolder.class);
        when(FlowTaskUtil.getCloudMainAccountId(execution)).thenReturn("testUid");
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(onlineSchemaChangeParameters);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schema);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);

        // When
        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        // Then
        verify(taskService, times(1)).start(anyLong());
        verify(onlineSchemaChangeFlowableTask, times(1)).createScheduleEntity(any(), any(), anyString());
        OnlineSchemaChangeContextHolder.assertTrace(1L, 1L, 1L);

    }

    @Test
    public void testIsSuccessful() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        // When
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;

        // Then
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        // When
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;

        // Then
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        TaskService taskService = Mockito.mock(TaskService.class);

        // When
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        // Then
        verify(taskService, times(1)).logEvent("Online schema change task failed, taskId={}", taskId);
    }

    @Test
    public void testOnSuccessful() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        TaskService taskService = Mockito.mock(TaskService.class);

        // When
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        // Then
        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testOnTimeout() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        TaskService taskService = Mockito.mock(TaskService.class);

        // When
        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        // Then
        verify(taskService, times(1)).logEvent("Online schema change task timeout, taskId={}", taskId);
    }

    @Test
    public void testOnProgressUpdate() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        TaskService taskService = Mockito.mock(TaskService.class);
        Page<ScheduleTaskEntity> tasks = Mockito.mock(Page.class);
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(tasks);

        // When
        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

        // Then
        verify(scheduleTaskService, times(1)).listTask(any(Pageable.class), anyLong());
        verify(onlineSchemaChangeFlowableTask, times(1)).progressStatusUpdate(tasks);
        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testCreateScheduleEntity() throws Exception {
        // Given
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
        String schema = "testSchema";
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);

        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(scheduleEntity);

        // When
        ScheduleEntity result = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schema);

        // Then
        verify(scheduleService, times(1)).create(any(ScheduleEntity.class));
        assertEquals(scheduleEntity, result);
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        // Given
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class);

        // When
        ScheduleTaskEntity result = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

        // Then
        verify(scheduleTaskRepository, times(1)).saveAndFlush(any(ScheduleTaskEntity.class));
        assertEquals(TaskStatus.PREPARING, result.getStatus());
    }

    @Test
    public void testIsCancelled() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;

        // Then
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }
}