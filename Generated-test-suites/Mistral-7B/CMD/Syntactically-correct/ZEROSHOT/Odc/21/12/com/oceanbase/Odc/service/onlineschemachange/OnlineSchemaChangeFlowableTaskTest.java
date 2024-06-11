package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
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
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskResult;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskResult;

@Slf4j
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
    private OrganizationService organizationService;
    @Mock
    private TaskService taskService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getId()).thenReturn(1L);
        when(execution.getVariable("onlineSchemaChangeParameters")).thenReturn(parameter);
        when(execution.getVariable("connectionConfig")).thenReturn(connectionConfig);
        when(execution.getVariable("schemaName")).thenReturn(schema);

        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());
        when(scheduleTaskService.listTask(any(Pageable.class), eq(1L))).thenReturn(getPageOfTasks(TaskStatus.RUNNING));
        when(taskService.list(any(Pageable.class))).thenReturn(getPageOfTasks());
        when(taskService.detail(eq(1L))).thenReturn(new TaskEntity());

        // When
        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        // Then
        verify(taskService, times(1)).start(eq(1L));
    }

    @Test
    public void testIsSuccessful() {
        // Given
        when(taskService.detail(eq(1L))).thenReturn(new TaskEntity());

        // When
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsFailure() {
        // Given
        when(taskService.detail(eq(1L))).thenReturn(new TaskEntity());
        when(taskService.detail(eq(1L)).getStatus()).thenReturn(TaskStatus.FAILED);

        // When
        boolean result = onlineSchemaChangeFlowableTask.isFailure();

        // Then
        assertTrue(result);
    }

    @Test
    public void testOnFailure() throws Exception {
        // Given
        when(taskService.detail(eq(1L))).thenReturn(new TaskEntity());

        // When
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

        // Then
        verify(log, times(1)).warn("Online schema change task failed, taskId={}", 1);
    }

    @Test
    public void testOnSuccessful() throws Exception {
        // Given
        when(taskService.detail(eq(1L))).thenReturn(new TaskEntity());

        // When
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

        // Then
        verify(taskService, times(1)).update(any(TaskEntity.class));
        verify(quartzJobService, times(1)).scheduleJob(any(String.class), any(TriggerConfig.class), anyLong());
        verify(scheduleService, times(1)).update(any(ScheduleEntity.class), any(ScheduleStatus.class));
    }

    @Test
    public void testOnTimeout() throws Exception {
        // Given
        when(taskService.detail(eq(1L))).thenReturn(new TaskEntity());

        // When
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);

        // Then
        verify(log, times(1)).warn("Online schema change task timeout, taskId={}", 1);
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        // Given
        when(taskService.detail(eq(1L))).thenReturn(new TaskEntity());
        when(taskService.detail(eq(1L)).getStatus()).thenReturn(TaskStatus.RUNNING);

        Page<ScheduleTaskEntity> tasks = getPageOfTasks();
        when(scheduleTaskService.listTask(any(Pageable.class), eq(1L))).thenReturn(tasks);
        when(taskService.detail(eq(1L))).thenReturn(new TaskEntity());

        // When
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        // Then
        verify(log, times(1)).info("Successfully start schedule task with id={}", tasks.get(0).getId());
    }

    @Test
    public void testCancel() throws Exception {
        // Given
        when(taskService.cancel(eq(1L))).thenReturn(true);

        // When
        boolean result = onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        // Then
        assertTrue(result);
        verify(taskService, times(1)).cancel(eq(1L));
    }

    @Test
    public void testIsCancelled() {
        // Given
        when(taskService.detail(eq(1L))).thenReturn(new TaskEntity());
        when(taskService.detail(eq(1L)).getStatus()).thenReturn(TaskStatus.CANCELED);

        // When
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        // Then
        assertTrue(result);
    }

    private Page<ScheduleTaskEntity> getPageOfTasks() {
        Pageable pageable = Pageable.unpaged();
        return new Page<ScheduleTaskEntity>() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 1;
            }

            @Override
            public List<ScheduleTaskEntity> getContent() {
                return new ArrayList<>();
            }

            @Override
            public Pageable nextPageable(Pageable pageable) {
                return pageable;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Optional<ScheduleTaskEntity> getFirst() {
                return Optional.of(new ScheduleTaskEntity());
            }

            @Override
            public boolean isFirst() {
                return true;
            }

            @Override
            public Optional<ScheduleTaskEntity> getLast() {
                return Optional.empty();
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable previousPageable(Pageable pageable) {
                return pageable;
            }

            @Override
            public long getTotalElements() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };
    }

    private Page<ScheduleTaskEntity> getPageOfTasks(TaskStatus status) {
        Pageable pageable = Pageable.unpaged();
        return new Page<ScheduleTaskEntity>() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 1;
            }

            @Override
            public List<ScheduleTaskEntity> getContent() {
                ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
                scheduleTaskEntity.setStatus(status);
                return new ArrayList<ScheduleTaskEntity>() {{
                    add(scheduleTaskEntity);
                }};
            }

            @Override
            public Pageable nextPageable(Pageable pageable) {
                return pageable;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Optional<ScheduleTaskEntity> getFirst() {
                return Optional.of(new ScheduleTaskEntity());
            }

            @Override
            public boolean isFirst() {
                return true;
            }

            @Override
            public Optional<ScheduleTaskEntity> getLast() {
                return Optional.empty();
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable previousPageable(Pageable pageable) {
                return pageable;
            }

            @Override
            public long getTotalElements() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };
    }
}