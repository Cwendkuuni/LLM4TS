package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskDetail;
import com.oceanbase.odc.service.task.model.TaskExecution;
import com.oceanbase.odc.service.task.model.TaskResult;

/**
 * Test class for OnlineSchemaChangeFlowableTask
 *
 * @author yaobin
 * @date 2023-05-23
 * @since 4.2.0
 */
public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleService scheduleService;
    private ScheduleTaskService scheduleTaskService;
    private QuartzJobService quartzJobService;
    private OrganizationService organizationService;
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @Before
    public void setup() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleService = mock(ScheduleService.class);
        scheduleTaskService = mock(ScheduleTaskService.class);
        quartzJobService = mock(QuartzJobService.class);
        organizationService = mock(OrganizationService.class);
        oscTaskCompleteHandler = mock(OscTaskCompleteHandler.class);

        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
        onlineSchemaChangeFlowableTask.setOscTaskCompleteHandler(oscTaskCompleteHandler);
    }

    @Test
    public void testConstructor() {
        assertNotNull(onlineSchemaChangeFlowableTask);
    }

    @Test
    public void testStart() throws Exception {
        // Given
        DelegateExecution execution = mock(DelegateExecution.class);
        TaskService taskService = mock(TaskService.class);
        Long taskId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        User creator = mock(User.class);

        when(execution.getVariables()).thenReturn(Collections.singletonMap("onlineSchemaChangeParameter", parameter));
        when(execution.getId()).thenReturn(taskId);
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(creator);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schema);

        // When
        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

        // Then
        ArgumentCaptor<Void> voidCaptor = ArgumentCaptor.forClass(Void.class);
        verify(taskService).start(taskId, eq(voidCaptor.capture()));
        TaskExecution taskExecution = voidCaptor.getValue();
        assertEquals(TaskStatus.RUNNING, taskExecution.getStatus());
    }

    @Test
    public void testIsSuccessful() {
        // Given
        TaskStatus status = TaskStatus.DONE;

        // When
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsFailure() {
        // Given
        TaskStatus status = TaskStatus.FAILED;

        // When
        boolean result = onlineSchemaChangeFlowableTask.isFailure();

        // Then
        assertTrue(result);
    }

    @Test
    public void testOnFailure() {
        // Given
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);

        // When
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        // Then
        TaskExecution taskExecution = new TaskExecution();
        taskExecution.setId(taskId);
        taskExecution.setStatus(TaskStatus.FAILED);
        verify(taskService).update(eq(taskExecution));
    }

    @Test
    public void testOnSuccessful() {
        // Given
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);

        // When
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        // Then
        TaskExecution taskExecution = new TaskExecution();
        taskExecution.setId(taskId);
        taskExecution.setStatus(TaskStatus.DONE);
        verify(taskService).update(eq(taskExecution));
    }

    @Test
    public void testOnTimeout() {
        // Given
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);

        // When
        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        // Then
        TaskExecution taskExecution = new TaskExecution();
        taskExecution.setId(taskId);
        taskExecution.setStatus(TaskStatus.TIMEOUT);
        verify(taskService).update(eq(taskExecution));
    }

    @Test
    public void testOnProgressUpdate() {
        // Given
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        Page<ScheduleTaskEntity> tasks = new PageImpl<>(Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity()));
        Pageable pageable = Pageable.unpaged();

        when(scheduleTaskService.listTask(pageable, Mockito.anyLong())).thenReturn(tasks);

        // When
        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

        // Then
        ArgumentCaptor<TaskDetail> taskDetailCaptor = ArgumentCaptor.forClass(TaskDetail.class);
        verify(taskService).update(taskDetailCaptor.capture());
        TaskDetail taskDetail = taskDetailCaptor.getValue();
        assertEquals(TaskStatus.DONE, taskDetail.getStatus());
        assertEquals(100, taskDetail.getProgressPercentage());
    }

    @Test
    public void testCancel() {
        // Given
        boolean mayInterruptIfRunning = false;
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);

        // When
        boolean result = onlineSchemaChangeFlowableTask.cancel(mayInterruptIfRunning, taskId, taskService);

        // Then
        assertTrue(result);
        TaskExecution taskExecution = new TaskExecution();
        taskExecution.setId(taskId);
        taskExecution.setStatus(TaskStatus.CANCELED);
        verify(taskService).cancel(taskId);
        verify(taskService).update(eq(taskExecution));
    }

    @Test
    public void testIsCancelled() {
        // Given
        TaskStatus status = TaskStatus.CANCELED;

        // When
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        // Then
        assertTrue(result);
    }

    @Test
    public void testCreateScheduleEntity() {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "test_schema";

        // When
        ScheduleEntity result = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);

        // Then
        assertNotNull(result);
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, result.getJobType());
        assertEquals(ScheduleStatus.ENABLED, result.getStatus());
        assertEquals(LocalDateTime.now(), result.getCreatedAt());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        // Given
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

        // When
        ScheduleTaskEntity result = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

        // Then
        assertNotNull(result);
        assertEquals(TaskStatus.PREPARING, result.getStatus());
        assertEquals(0.0, result.getProgressPercentage());
    }
}