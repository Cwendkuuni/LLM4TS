package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.test.FlowableTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskResult;
import com.oceanbase.odc.service.task.TaskService;

/**
 * Unit tests for {@link OnlineSchemaChangeFlowableTask}.
 */
public class OnlineSchemaChangeFlowableTaskTest {

    @Rule
    public FlowableTestRule flowableTestRule = new FlowableTestRule();

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

    @InjectMocks
    private OnlineSchemaChangeFlowableTask testee;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart_success() throws Exception {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "testSchema";
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        DelegateExecution execution = mock(DelegateExecution.class);

        when(taskService.start(taskId)).thenReturn(null);
        when(taskService.detail(taskId)).thenReturn(new TaskEntity());
        when(flowableTestRule.getExecution()).thenReturn(execution);
        when(execution.getVariable("onlineSchemaChangeParameters")).thenReturn(parameter);
        when(execution.getVariable("connectionConfig")).thenReturn(connectionConfig);

        // When
        testee.start(taskId, taskService, execution);

        // Then
        verify(taskService, times(1)).start(taskId);
        ArgumentCaptor<Void> voidCaptor = ArgumentCaptor.forClass(Void.class);
        verify(taskService, times(1)).update(voidCaptor.capture(updatedTask));
        assertEquals(TaskStatus.RUNNING, updatedTask.getStatus());
        verifyNoMoreInteractions(taskService);
    }

    @Test
    public void testIsSuccessful_success() {
        // Given
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.DONE);

        // When
        boolean result = testee.isSuccessful(taskEntity.getId(), mock(TaskService.class));

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsFailure_failure() {
        // Given
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.FAILED);

        // When
        boolean result = testee.isFailure(taskEntity.getId(), mock(TaskService.class));

        // Then
        assertTrue(result);
    }

    @Test
    public void testCancel_success() throws Exception {
        // Given
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        boolean mayInterruptIfRunning = true;

        // When
        boolean result = testee.cancel(mayInterruptIfRunning, taskId, taskService);

        // Then
        verify(taskService, times(1)).cancel(taskId);
        assertTrue(result);
    }

    @Test
    public void testOnProgressUpdate_success() throws Exception {
        // Given
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        Page<ScheduleTaskEntity> tasks = ListUtils.emptyIfNull(ListUtils.emptyIfNull(List.of(
                new ScheduleTaskEntity(),
                new ScheduleTaskEntity()))).map(s -> {
            s.setId(1L);
            return s;
        });
        when(scheduleTaskService.listTask(any(Pageable.class), eq(scheduleId))).thenReturn(tasks);

        // When
        testee.onProgressUpdate(taskId, taskService);

        // Then
        verify(taskService, times(1)).update(argThat(task -> {
            assertEquals(new OnlineSchemaChangeTaskResult(List.of(tasks.get(0), tasks.get(1))).toJson(),
                    task.getResultJson());
            assertEquals(TaskStatus.DONE, task.getStatus());
            return true;
        }));
    }

    @Test
    public void testCreateScheduleEntity_success() {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "testSchema";

        // When
        ScheduleEntity scheduleEntity = testee.createScheduleEntity(connectionConfig, parameter, schema);

        // Then
        verify(scheduleService, times(1)).create(scheduleEntity);
    }

    @Test
    public void testCreateScheduleTaskEntity_success() {
        // Given
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

        // When
        ScheduleTaskEntity scheduleTaskEntity = testee.createScheduleTaskEntity(scheduleId, param);

        // Then
        verify(scheduleTaskRepository, times(1)).saveAndFlush(scheduleTaskEntity);
    }

    private TaskEntity updatedTask;
}