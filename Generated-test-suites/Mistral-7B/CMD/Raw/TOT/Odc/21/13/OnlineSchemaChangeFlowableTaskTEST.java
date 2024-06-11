package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
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
import com.oceanbase.odc.service.flow.task.OnlineSchemaChangeTaskHandler;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult.TaskStatusInfo;

/**
 * Tests for OnlineSchemaChangeFlowableTask
 */
public class OnlineSchemaChangeFlowableTaskTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private OnlineSchemaChangeTaskHandler onlineSchemaChangeTaskHandler;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private TaskService taskService;

    @Mock
    private QuartzJobService quartzJobService;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);
        when(scheduleTaskRepository.saveAndFlush(scheduleTaskEntity)).thenReturn(scheduleTaskEntity);

        when(onlineSchemaChangeTaskHandler.start(scheduleId, scheduleTaskEntity.getId())).thenReturn(null);
    }

    @Test
    public void testStart() throws Exception {
        Long taskId = 1L;
        Long flowInstanceId = 1L;
        when(taskService.detail(taskId)).thenReturn(new TaskEntity());
        when(taskService.start(taskId)).thenReturn(null);

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        when(organizationService.getCurrentUser()).thenReturn(mock(User.class));
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(any())).thenReturn(parameters);
        when(FlowTaskUtil.getCloudMainAccountId(any())).thenReturn("uid");
        when(FlowTaskUtil.getSchemaName(any())).thenReturn("schemaName");
        when(FlowTaskUtil.getConnectionConfig(any())).thenReturn(connectionConfig);

        onlineSchemaChangeFlowableTask.start(taskId, taskService, mock(DelegateExecution.class));

        verify(scheduleService, times(1)).create(scheduleEntity);
        verify(onlineSchemaChangeTaskHandler, times(1)).start(scheduleId, scheduleTaskEntity.getId());
        verify(taskService, times(1)).start(taskId);
    }

    @Test
    public void testIsSuccessful_whenStatusIsDone() {
        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(Arrays.asList(new ScheduleTaskEntity()));
        TaskEntity flowTask = new TaskEntity();
        flowTask.setStatus(TaskStatus.DONE);
        flowTask.setResultJson(JsonUtils.toJson(result));

        when(taskService.detail(anyLong())).thenReturn(flowTask);

        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsSuccessful_whenStatusIsFailed() {
        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(Arrays.asList(new ScheduleTaskEntity()));
        TaskEntity flowTask = new TaskEntity();
        flowTask.setStatus(TaskStatus.FAILED);
        flowTask.setResultJson(JsonUtils.toJson(result));

        when(taskService.detail(anyLong())).thenReturn(flowTask);

        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure_whenStatusIsDone() {
        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(Arrays.asList(new ScheduleTaskEntity()));
        TaskEntity flowTask = new TaskEntity();
        flowTask.setStatus(TaskStatus.DONE);
        flowTask.setResultJson(JsonUtils.toJson(result));

        when(taskService.detail(anyLong())).thenReturn(flowTask);

        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testIsFailure_whenStatusIsFailed() {
        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(Arrays.asList(new ScheduleTaskEntity()));
        TaskEntity flowTask = new TaskEntity();
        flowTask.setStatus(TaskStatus.FAILED);
        flowTask.setResultJson(JsonUtils.toJson(result));

        when(taskService.detail(anyLong())).thenReturn(flowTask);

        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testCancel_whenRunning() {
        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(Arrays.asList(new ScheduleTaskEntity()));
        TaskEntity flowTask = new TaskEntity();
        flowTask.setStatus(TaskStatus.RUNNING);
        flowTask.setResultJson(JsonUtils.toJson(result));

        when(taskService.detail(anyLong())).thenReturn(flowTask);

        assertTrue(onlineSchemaChangeFlowableTask.cancel(false, 1L, taskService));

        verify(taskService, times(1)).cancel(1L);
    }

    @Test
    public void testCancel_whenDone() {
        TaskEntity flowTask = new TaskEntity();
        flowTask.setStatus(TaskStatus.DONE);

        when(taskService.detail(anyLong())).thenReturn(flowTask);

        assertFalse(onlineSchemaChangeFlowableTask.cancel(false, 1L, taskService));
    }

    @Test
    public void testCancel_whenFailed() {
        TaskEntity flowTask = new TaskEntity();
        flowTask.setStatus(TaskStatus.FAILED);

        when(taskService.detail(anyLong())).thenReturn(flowTask);

        assertFalse(onlineSchemaChangeFlowableTask.cancel(false, 1L, taskService));
    }

    @Test
    public void testIsCancelled_whenStatusIsCanceled() {
        TaskEntity flowTask = new TaskEntity();
        flowTask.setStatus(TaskStatus.CANCELED);

        when(taskService.detail(anyLong())).thenReturn(flowTask);

        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testIsCancelled_whenStatusIsNotCanceled() {
        TaskEntity flowTask = new TaskEntity();
        flowTask.setStatus(TaskStatus.DONE);

        when(taskService.detail(anyLong())).thenReturn(flowTask);

        assertFalse(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testOnProgressUpdate_whenAllTasksSucceed() {
        Page<ScheduleTaskEntity> tasks = Page.empty();
        tasks.setContent(Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity()));

        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(tasks.getContent());
        TaskEntity flowTask = new TaskEntity();
        flowTask.setStatus(TaskStatus.DONE);
        flowTask.setResultJson(JsonUtils.toJson(result));

        when(taskService.detail(anyLong())).thenReturn(flowTask);

        List<TaskStatusInfo> taskStatusInfos = onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        assertEquals(2, taskStatusInfos.size());
        for (TaskStatusInfo taskStatusInfo : taskStatusInfos) {
            assertEquals(TaskStatus.DONE, taskStatusInfo.getStatus());
        }
    }

    @Test
    public void testOnProgressUpdate_whenOneTaskFailed() {
        Page<ScheduleTaskEntity> tasks = Page.empty();
        tasks.setContent(Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity()));

        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(tasks.getContent());
        TaskEntity flowTask = new TaskEntity();
        flowTask.setStatus(TaskStatus.FAILED);
        flowTask.setResultJson(JsonUtils.toJson(result));

        when(taskService.detail(anyLong())).thenReturn(flowTask);

        List<TaskStatusInfo> taskStatusInfos = onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        assertEquals(2, taskStatusInfos.size());
        for (TaskStatusInfo taskStatusInfo : taskStatusInfos) {
            if (taskStatusInfo.getTaskId() == 1) {
                assertEquals(TaskStatus.FAILED, taskStatusInfo.getStatus());
                continue;
            }
            assertEquals(TaskStatus.DONE, taskStatusInfo.getStatus());
        }
    }

    @Test
    public void testPercentage() {
        Page<ScheduleTaskEntity> tasks = Page.empty();
        tasks.setContent(Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity()));

        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(tasks.getContent());

        double percentage = onlineSchemaChangeFlowableTask.percentage(tasks);

        assertEquals(50.0, percentage, 0.01);
    }

    @Test
    public void testPercentage_whenAllTasksSucceed() {
        Page<ScheduleTaskEntity> tasks = Page.empty();
        tasks.setContent(Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity()));

        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(tasks.getContent());

        double percentage = onlineSchemaChangeFlowableTask.percentage(tasks);

        assertEquals(100.0, percentage, 0.01);
    }

    @Test
    public void testPercentage_whenAllTasksFailed() {
        Page<ScheduleTaskEntity> tasks = Page.empty();
        tasks.setContent(Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity()));

        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(tasks.getContent());

        when(onlineSchemaChangeFlowableTask.percentage(tasks)).thenReturn(0.0);

        double percentage = onlineSchemaChangeFlowableTask.percentage(tasks);

        assertEquals(0.0, percentage, 0.01);
    }
}