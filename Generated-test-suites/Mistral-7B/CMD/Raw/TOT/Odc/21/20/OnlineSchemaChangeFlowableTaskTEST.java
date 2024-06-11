package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

/**
 * Test class for OnlineSchemaChangeFlowableTask
 */
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
    private ConnectionConfig connectionConfig;

    @Mock
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Mock
    private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private User user;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(organizationService.findByUserId(anyLong())).thenReturn(user);
    }

    @Test
    public void testStartWithSuccess() throws Exception {
        // Given
        when(taskService.start(anyLong())).thenReturn(true);
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());

        // When
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";
        when(FlowTaskUtil.getConnectionConfig(any())).thenReturn(connectionConfig);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(any())).thenReturn(params);
        when(FlowTaskUtil.getSchemaName(any())).thenReturn(schema);
        when(FlowTaskUtil.getCloudMainAccountId(any())).thenReturn("cloud_id");

        // Then
        OnlineSchemaChangeTaskResult result = onlineSchemaChangeFlowableTask.start(1L, taskService, delegateExecution);
        assertTrue(result.isSuccess());
        verify(taskService, times(1)).start(1L);
        verify(organizationService, times(1)).findByUserId(Long.parseLong("creatorId"));
        verifyNoMoreInteractions(scheduleTaskRepository, scheduleService, scheduleTaskService, quartzJobService);
    }

    @Test
    public void testStartWithFailure() throws Exception {
        // Given
        when(taskService.start(anyLong())).thenReturn(false);

        // When
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";
        when(FlowTaskUtil.getConnectionConfig(any())).thenReturn(connectionConfig);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(any())).thenReturn(params);
        when(FlowTaskUtil.getSchemaName(any())).thenReturn(schema);
        when(FlowTaskUtil.getCloudMainAccountId(any())).thenReturn("cloud_id");

        // Then
        OnlineSchemaChangeTaskResult result = onlineSchemaChangeFlowableTask.start(1L, taskService, delegateExecution);
        assertFalse(result.isSuccess());
        verify(taskService, times(1)).start(1L);
        verifyNoMoreInteractions(scheduleTaskRepository, scheduleService, scheduleTaskService, quartzJobService);
    }

    @Test
    public void testListTaskWithSuccess() throws Exception {
        // Given
        List<ScheduleTaskEntity> scheduleTasks = Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity());
        Page<ScheduleTaskEntity> page = new PageImpl<>(scheduleTasks);
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(page);

        // When
        List<ScheduleTaskEntity> result = onlineSchemaChangeFlowableTask.listTask(taskService, 1L);

        // Then
        assertEquals(2, result.size());
        verify(taskService, times(1)).listTask(any(Pageable.class), eq(1L));
        verifyNoMoreInteractions(scheduleTaskRepository, scheduleService, scheduleTaskService, quartzJobService);
    }

    @Test
    public void testListTaskWithEmptyResult() throws Exception {
        // Given
        List<ScheduleTaskEntity> scheduleTasks = Collections.emptyList();
        Page<ScheduleTaskEntity> page = new PageImpl<>(scheduleTasks);
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(page);

        // When
        List<ScheduleTaskEntity> result = onlineSchemaChangeFlowableTask.listTask(taskService, 1L);

        // Then
        assertEquals(0, result.size());
        verify(taskService, times(1)).listTask(any(Pageable.class), eq(1L));
        verifyNoMoreInteractions(scheduleTaskRepository, scheduleService, scheduleTaskService, quartzJobService);
    }

    @Test
    public void testListTaskWithNullResult() throws Exception {
        // Given
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(null);

        // When & Then
        assertNull(onlineSchemaChangeFlowableTask.listTask(taskService, 1L));
        verify(taskService, times(1)).listTask(any(Pageable.class), eq(1L));
        verifyNoMoreInteractions(scheduleTaskRepository, scheduleService, scheduleTaskService, quartzJobService);
    }

    @Test
    public void testCancelWithSuccess() throws Exception {
        // Given
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(new PageImpl<>(new ScheduleTaskEntity()));
        when(taskHandler.terminate(anyLong(), anyLong())).thenReturn(true);

        // When
        boolean result = onlineSchemaChangeFlowableTask.cancel(taskService, 1L, true);

        // Then
        assertTrue(result);
        verify(taskService, times(1)).cancel(1L);
        verify(taskHandler, times(1)).terminate(1L, 1L);
        verifyNoMoreInteractions(scheduleTaskRepository, scheduleService, scheduleTaskService, quartzJobService);
    }

    @Test
    public void testCancelWithException() throws Exception {
        // Given
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(new PageImpl<>(new ScheduleTaskEntity()));
        when(taskHandler.terminate(anyLong(), anyLong())).thenThrow(new RuntimeException("Error"));

        // When & Then
        assertFalse(onlineSchemaChangeFlowableTask.cancel(taskService, 1L, true));
        verify(taskService, times(1)).cancel(1L);
        verify(taskHandler, times(1)).terminate(1L, 1L);
        verifyNoMoreInteractions(scheduleTaskRepository, scheduleService, scheduleTaskService, quartzJobService);
    }

    @Test
    public void testIsCancelledWithSuccess() throws Exception {
        // Given
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.status = TaskStatus.CANCELED;

        // When
        boolean result = task.isCancelled();

        // Then
        assertTrue(result);
        verifyNoMoreInteractions(scheduleTaskRepository, scheduleService, scheduleTaskService, quartzJobService);
    }

    @Test
    public void testIsCancelledWithFailure() throws Exception {
        // Given
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.status = TaskStatus.FAILED;

        // When
        boolean result = task.isCancelled();

        // Then
        assertFalse(result);
        verifyNoMoreInteractions(scheduleTaskRepository, scheduleService, scheduleTaskService, quartzJobService);
    }
}