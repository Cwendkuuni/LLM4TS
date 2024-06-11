package com.oceanbase.odc.service.onlineschemachange.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.Collectors;
import org.apache.commons.collections4.utils.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.test.FlowableTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
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
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.iam.OrganizationService;

/**
 * Test class for OnlineSchemaChangeFlowableTask
 *
 * @author yaobin
 * @date 2023-05-23
 * @since 4.2.0
 */
@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

    @Rule
    public FlowableTestRule flowableTestRule = new FlowableTestRule();

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private TaskService taskService;
    private ScheduleService scheduleService;
    private ScheduleTaskService scheduleTaskService;
    private QuartzJobService quartzJobService;
    private OrganizationService organizationService;

    @Before
    public void setup() {
        taskService = mock(TaskService.class);
        scheduleService = mock(ScheduleService.class);
        scheduleTaskService = mock(ScheduleTaskService.class);
        quartzJobService = mock(QuartzJobService.class);
        organizationService = mock(OrganizationService.class);
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask() {
            {
                this.taskService = taskService;
                this.scheduleService = scheduleService;
                this.scheduleTaskService = scheduleTaskService;
                this.quartzJobService = quartzJobService;
                this.organizationService = organizationService;
            }
        };
    }

    @After
    public void tearDown() {
        reset(taskService, scheduleService, scheduleTaskService, quartzJobService, organizationService);
    }

    @Test
    public void testStart() throws Exception {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "testSchema";
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Long taskId = 1L;

        // When
        when(taskService.start(taskId)).thenReturn(true);
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(new User());
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schema);
        when(organizationService.getUserById(anyLong())).thenReturn(new User());

        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

        // Then
        verify(taskService, times(1)).start(taskId);
    }

    @Test
    public void testIsSuccessful() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        TaskStatus expectedStatus = TaskStatus.DONE;

        // When
        boolean actualResult = onlineSchemaChangeFlowableTask.isSuccessful();

        // Then
        assertThat(actualResult).isEqualTo(expectedStatus == TaskStatus.DONE);
    }

    @Test
    public void testIsFailure() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        TaskStatus expectedStatus = TaskStatus.FAILED;

        // When
        boolean actualResult = onlineSchemaChangeFlowableTask.isFailure();

        // Then
        assertThat(actualResult).isEqualTo(expectedStatus == TaskStatus.FAILED);
    }

    @Test
    public void testOnFailure() throws Exception {
        // Given
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);

        // When
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        // Then
        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testOnSuccessful() throws Exception {
        // Given
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(List.of());

        // When
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        // Then
        TaskEntity flowTask = new TaskEntity();
        flowTask.setId(taskId);
        flowTask.setStatus(TaskStatus.DONE);
        flowTask.setResultJson(JsonUtils.toJson(result));
        verify(taskService, times(1)).update(flowTask);
    }

    @Test
    public void testOnTimeout() throws Exception {
        // Given
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);

        // When
        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        // Then
        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testOnProgressUpdate() {
        // Given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        ScheduleTaskService scheduleTaskService = mock(ScheduleTaskService.class);
        Page<ScheduleTaskEntity> tasks = Mockito.mock(Page.class);

        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(tasks);
        when(tasks.getSize()).thenReturn(2);
        when(tasks.getContent()).thenReturn(List.of(new ScheduleTaskEntity(), new ScheduleTaskEntity()));

        // When
        onlineSchemaChangeFlowableTask.onProgressUpdate(taskService);

        // Then
        verify(scheduleTaskService, times(1)).listTask(any(Pageable.class), anyLong());
        TaskStatus status = TaskStatus.DONE;
        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);
        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testCreateScheduleEntity() {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "testSchema";

        // When
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);

        // Then
        verify(scheduleService, times(1)).create(scheduleEntity);
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        // Given
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

        // When
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

        // Then
        verify(scheduleTaskRepository, times(1)).saveAndFlush(scheduleTaskEntity);
    }
}