package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.task.Task;
import org.junit.After;
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
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeTaskResult;
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
import com.oceanbase.odc.service.task.model.TaskResult;

public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleService scheduleService;
    private ScheduleTaskService scheduleTaskService;
    private QuartzJobService quartzJobService;
    private OrganizationService organizationService;
    private TaskService taskService;
    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private String schema;

    @Before
    public void setUp() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleService = mock(ScheduleService.class);
        scheduleTaskService = mock(ScheduleTaskService.class);
        quartzJobService = mock(QuartzJobService.class);
        organizationService = mock(OrganizationService.class);
        taskService = mock(TaskService.class);
        connectionConfig = new ConnectionConfig();
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        schema = "test_schema";
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testStart() throws Exception {
        // Given
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Task task = Mockito.mock(Task.class);
        when(execution.getId()).thenReturn(UUID.randomUUID().toString());
        when(execution.getVariable("onlineschemachangeParameters")).thenReturn(onlineSchemaChangeParameters);
        when(execution.getVariable("connectionConfig")).thenReturn(connectionConfig);
        when(execution.getVariable("schema")).thenReturn(schema);
        when(taskService.start(anyLong())).thenReturn(true);
        User creator = new User();
        creator.setId(1L);
        creator.setOrganizationId(2L);
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(creator);
        OnlineSchemaChangeContextHolder.set(1L, 1L, 2L);

        // When
        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        // Then
        verify(taskService, times(1)).start(anyLong());
        verify(scheduleService, times(1)).create(any(ScheduleEntity.class));
        verify(scheduleTaskService, times(1)).listTask(any(Pageable.class), eq(scheduleId)).thenReturn(PageMock.getPageWithScheduleTasks());
        verify(taskHandler, times(1)).start(scheduleId, tasks.get(0).getId());
    }

    @Test
    public void testIsSuccessful() {
        // Given
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;

        // When
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsFailure() {
        // Given
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;

        // When
        boolean result = onlineSchemaChangeFlowableTask.isFailure();

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsCancelled() {
        // Given
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;

        // When
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        // Then
        assertTrue(result);
    }

    @Test
    public void testCancel() throws Exception {
        // Given
        when(scheduleTaskService.listTask(any(Pageable.class), eq(scheduleId)))
                .thenReturn(PageMock.getPageWithRunningTask());

        // When
        boolean result = onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        // Then
        verify(taskService, times(1)).cancel(anyLong());
        verify(taskHandler, times(1)).terminate(scheduleId, scheduleTask.getId());
    }

    @Test
    public void testProgressUpdate() {
        // Given
        Page<ScheduleTaskEntity> tasks = PageMock.getPageWithScheduleTasks();
        when(scheduleTaskService.listTask(any(Pageable.class), eq(scheduleId)))
                .thenReturn(tasks);

        // When
        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);

        // Then
        verify(onlineSchemaChangeFlowableTask, times(1)).percentage = (50.0 + 50.0) / tasks.getSize();
        verify(flowTask, times(1)).setStatus(TaskStatus.DONE);
    }

    @Test
    public void testSingleTaskPercentage() {
        // Given
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setStatus(TaskStatus.RUNNING);
        scheduleTask.setProgressPercentage(50);

        // When
        double result = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTask);

        // Then
        assertEquals(0.5, result, 0.001);
    }

    @Test
    public void testCreateScheduleEntity() {
        // Given

        // When
        ScheduleEntity result = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schema);

        // Then
        assertEquals("ONLINE_SCHEMA_CHANGE_COMPLETE", result.getJobType().name());
        assertEquals(1, result.getProjectId().intValue());
        assertEquals(1, result.getDatabaseId().intValue());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

        // When
        ScheduleTaskEntity result = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

        // Then
        assertEquals(TaskStatus.PREPARING, result.getStatus());
        assertEquals("OnlineSchemaChangeFlowableTask", result.getJobName());
        assertEquals("ONLINE_SCHEMA_CHANGE_COMPLETE", result.getJobGroup());
        assertEquals(0.0, result.getProgressPercentage(), 0.0);
    }
}

class PageMock {
    public static <T> Page<T> getPageWithScheduleTasks() {
        Pageable pageable = PageRequestMock.getPageRequest();
        List<ScheduleTaskEntity> tasks = Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity());
        return new PageImpl<>(tasks, pageable, tasks.size());
    }

    public static <T> Page<T> getPageWithRunningTask() {
        Pageable pageable = PageRequestMock.getPageRequest();
        List<ScheduleTaskEntity> tasks = Arrays.asList(new ScheduleTaskEntity());
        return new PageImpl<>(tasks, pageable, tasks.size());
    }
}

class TaskMock {
    public static Task getTask() {
        return mock(Task.class);
    }
}

class ConnectionConfigMock {
    public static ConnectionConfig getConnectionConfig() {
        return new ConnectionConfig();
    }
}

class OnlineSchemaChangeContextHolderMock {
    public static void set(long creatorId, long flowTaskId, long organizationId) {
        OnlineSchemaChangeContextHolder.set(creatorId, flowTaskId, organizationId);
    }

    public static void clear() {
        OnlineSchemaChangeContextHolder.clear();
    }
}

class OnlineSchemaChangeTaskResultMock {
    public static OnlineSchemaChangeTaskResult newOnlineSchemaChangeTaskResult(List<ScheduleTaskEntity> tasks) {
        return new OnlineSchemaChangeTaskResult(tasks);
    }
}

class OnlineSchemaChangeScheduleTaskResultMock {
    public static OnlineSchemaChangeScheduleTaskResult newOnlineSchemaChangeScheduleTaskResult(OnlineSchemaChangeScheduleTaskParameters param) {
        return new OnlineSchemaChangeScheduleTaskResult(param);
    }
}

class ScheduleEntityMock {
    public static ScheduleEntity newScheduleEntity() {
        return new ScheduleEntity();
    }
}

class ScheduleTaskEntityMock {
    public static ScheduleTaskEntity newScheduleTaskEntity() {
        return new ScheduleTaskEntity();
    }
}

class ScheduleTaskRepositoryMock {
    public static ScheduleTaskRepository mockScheduleTaskRepository() {
        return mock(ScheduleTaskRepository.class);
    }
}

class ScheduleServiceMock {
    public static ScheduleService mockScheduleService() {
        return mock(ScheduleService.class);
    }
}

class ScheduleTaskServiceMock {
    public static ScheduleTaskService mockScheduleTaskService() {
        return mock(ScheduleTaskService.class);
    }
}

class QuartzJobServiceMock {
    public static QuartzJobService mockQuartzJobService() {
        return mock(QuartzJobService.class);
    }
}

class OrganizationServiceMock {
    public static OrganizationService mockOrganizationService() {
        return mock(OrganizationService.class);
    }
}

class TaskServiceMock {
    public static TaskService mockTaskService() {
        return mock(TaskService.class);
    }
}

class TaskResultMock {
    public static TaskResult newTaskResult() {
        return Mockito.mock(TaskResult.class);
    }
}

class PageRequestMock {
    public static Pageable getPageRequest() {
        return PageRequest.of(0, 10);
    }
}