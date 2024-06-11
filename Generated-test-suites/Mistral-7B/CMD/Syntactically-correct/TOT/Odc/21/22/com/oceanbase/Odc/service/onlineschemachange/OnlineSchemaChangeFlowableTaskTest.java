package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
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
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
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
import com.oceanbase.odc.service.schedule.model.ScheduleTaskResult;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskResult;

public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleService scheduleService;
    private ScheduleTaskService scheduleTaskService;
    private QuartzJobService quartzJobService;
    private OrganizationService organizationService;
    private OscTaskCompleteHandler oscTaskCompleteHandler;
    private User user;

    @Before
    public void setup() {
        user = new User();
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
    public void testStartTaskSuccessfully() throws Exception {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "testSchema";
        when(quartzJobService.scheduleJob(anyString(), any(TriggerConfig.class), anyLong(), any(List.class)))
                .thenReturn(true);
        when(quartzJobService.scheduleJob(anyString(), any(TriggerConfig.class), anyLong(), any(List.class)))
                .thenReturn(true);
        when(organizationService.getUserById(anyLong()))
                .thenReturn(createMockUser(1L, "admin@test.com", "admin", "admin", 1L));
        when(scheduleService.create(any(ScheduleEntity.class)))
                .thenReturn(createMockScheduleEntity(1L));

        // When
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        TaskService taskService = mock(TaskService.class);
        when(taskService.detail(anyLong()))
                .thenReturn(new ExecutionEntity());
        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        // Then
        verify(quartzJobService).scheduleJob(eq("OnlineSchemaChangeTask-1"), any(TriggerConfig.class), eq(1L),
                any(List.class));
        verify(quartzJobService).scheduleJob(eq("OnlineSchemaChangeSubTask-1"), any(TriggerConfig.class), eq(1L),
                any(List.class));
    }

    @Test
    public void testStartTaskFailed() throws Exception {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "testSchema";

        when(quartzJobService.scheduleJob(anyString(), any(TriggerConfig.class), anyLong(), any(List.class)))
                .thenThrow(new Exception("Quartz Job Scheduling Failed"));

        // When
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        TaskService taskService = mock(TaskService.class);
        when(taskService.detail(anyLong()))
                .thenReturn(new ExecutionEntity());

        // Then
        try {
            onlineSchemaChangeFlowableTask.start(1L, taskService, execution);
            fail("Expected exception");
        } catch (Exception e) {
            assertEquals("Quartz Job Scheduling Failed", e.getMessage());
        }

        verify(quartzJobService, never()).scheduleJob(anyString(), any(TriggerConfig.class), eq(1L),
                any(List.class));
    }

    @Test
    public void testProgressUpdateSuccessfully() {
        // Given
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
        scheduleTaskEntity.setProgressPercentage(50);
        when(scheduleTaskService.listTask(any(Pageable.class), eq(1L)))
                .thenReturn(createMockPage(List.of(scheduleTaskEntity)));

        // When
        TaskService taskService = mock(TaskService.class);
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        // Then
        verify(taskService).update(eq(createMockTaskEntity(1L, TaskStatus.RUNNING, 50.0)));
    }

    @Test
    public void testProgressUpdateFailed() {
        // Given
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
        scheduleTaskEntity.setProgressPercentage(50);
        when(scheduleTaskService.listTask(any(Pageable.class), eq(1L)))
                .thenReturn(createMockPage(List.of(scheduleTaskEntity)));

        // When
        TaskService taskService = mock(TaskService.class);
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);

        // Then
        try {
            onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
            fail("Expected exception");
        } catch (Exception e) {
            assertEquals("Not implemented", e.getMessage());
        }

        verify(taskService, never()).update(any(TaskEntity.class));
    }

    @Test
    public void testIsSuccessful() throws Exception {
        // Given
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.DONE);

        // When
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        // Then
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() throws Exception {
        // Given
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.FAILED);

        // When
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        // Then
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testIsCancelled() throws Exception {
        // Given
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.CANCELED);

        // When
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        // Then
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCancel() throws Exception {
        // Given
        TaskEntity taskEntity = new TaskEntity();
        when(taskService.detail(anyLong()))
                .thenReturn(new ExecutionEntity());

        // When
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.cancel(true, 1L, mock(TaskService.class));

        // Then
        verify(quartzJobService).terminateJob("OnlineSchemaChangeTask-1", eq(1L));
        verify(quartzJobService).terminateJob("OnlineSchemaChangeSubTask-1", eq(1L));
        verify(taskService).cancel(eq(1L));
    }

    private User createMockUser(long id, String username, String password, String name, long organizationId) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setOrganizationId(organizationId);
        return user;
    }

    private ScheduleEntity createMockScheduleEntity(long id) {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(id);
        return scheduleEntity;
    }

    private TaskEntity createMockTaskEntity(long id, TaskStatus status, double percentage) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(id);
        taskEntity.setStatus(status);
        taskEntity.setProgressPercentage(percentage);
        return taskEntity;
    }

    private Page<ScheduleTaskEntity> createMockPage(List<ScheduleTaskEntity> content) {
        return new PageImpl<>(content);
    }

}