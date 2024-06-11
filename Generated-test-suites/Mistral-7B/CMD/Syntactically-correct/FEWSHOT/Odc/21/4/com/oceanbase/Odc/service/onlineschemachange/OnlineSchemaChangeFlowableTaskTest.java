package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.cli.util.TestUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeTaskHandler;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @Autowired
    private QuartzJobService quartzJobService;
    @Autowired
    private OnlineSchemaChangeTaskHandler taskHandler;
    @Autowired
    private OscTaskCompleteHandler completeHandler;
    @Autowired
    private OrganizationService organizationService;

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private ScheduleEntity scheduleEntity;
    private TaskEntity taskEntity;
    private DelegateExecution delegateExecution;

    @Before
    public void setup() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        onlineSchemaChangeParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
        scheduleEntity = Mockito.mock(ScheduleEntity.class);
        taskEntity = Mockito.mock(TaskEntity.class);
        delegateExecution = Mockito.mock(DelegateExecution.class);

        when(delegateExecution.getVariables()).thenReturn(Map.of());
        when(scheduleTaskRepository.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(quartzJobService.createJobDetail(any(), any(), any()))
                .thenReturn(Mockito.mock(org.quartz.JobDetail.class));
        when(quartzJobService.createTrigger(any(), any(), any(), any(), any()))
                .thenReturn(Mockito.mock(org.quartz.Trigger.class));
        when(taskService.detail(anyLong())).thenReturn(taskEntity);

        OnlineSchemaChangeContextHolder.set(1L, 1L, 1L);
    }

    @Test
    public void testStart() throws Exception {
        when(FlowTaskUtil.getTaskCreator(delegateExecution)).thenReturn(TestUtils.newUserBuilder().build());
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(delegateExecution)).thenReturn(onlineSchemaChangeParameters);
        when(FlowTaskUtil.getConnectionConfig(delegateExecution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(delegateExecution)).thenReturn("test");

        onlineSchemaChangeFlowableTask.start(1L, Mockito.mock(TaskService.class), delegateExecution);

        verify(taskService, times(1)).start(1L);
        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
    }

    @Test
    public void testIsSuccessful() {
        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() throws Exception {
        onlineSchemaChangeFlowableTask.onFailure(1L, Mockito.mock(TaskService.class));
        verify(log, times(1)).warn("Online schema change task failed, taskId={}", 1L);
    }

    @Test
    public void testOnSuccessful() throws Exception {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        onlineSchemaChangeFlowableTask.onSuccessful(1L, Mockito.mock(TaskService.class));
        verify(log, times(1)).info("Online schema change task succeed, taskId={}", 1L);
        verify(taskService, times(1)).update(taskEntity);
    }

    @Test
    public void testOnTimeout() throws Exception {
        onlineSchemaChangeFlowableTask.onTimeout(1L, Mockito.mock(TaskService.class));
        verify(log, times(1)).warn("Online schema change task timeout, taskId={}", 1L);
    }

    @Test
    public void testProgressUpdate() throws Exception {
        Page<ScheduleTaskEntity> tasks = Mockito.mock(Page.class);
        when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(tasks);

        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);
        verify(onlineSchemaChangeFlowableTask, times(1)).setStatus(TaskStatus.CANCELED);

        // Test with successful tasks
        tasks = Mockito.mock(Page.class);
        when(tasks.getSize()).thenReturn(2);
        List<ScheduleTaskEntity> scheduleTasks = List.of(
                Mockito.mock(ScheduleTaskEntity.class, "task1"),
                Mockito.mock(ScheduleTaskEntity.class, "task2")
        );
        when(tasks.getContent()).thenReturn(scheduleTasks);
        when(scheduleTasks.get(0).getStatus()).thenReturn(TaskStatus.DONE);
        when(scheduleTasks.get(1).getStatus()).thenReturn(TaskStatus.FAILED);

        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);
        verify(onlineSchemaChangeFlowableTask, times(1)).setStatus(TaskStatus.DONE);

        // Test with failed tasks
        tasks = Mockito.mock(Page.class);
        when(tasks.getSize()).thenReturn(2);
        scheduleTasks = List.of(
                Mockito.mock(ScheduleTaskEntity.class, "task1"),
                Mockito.mock(ScheduleTaskEntity.class, "task2")
        );
        when(tasks.getContent()).thenReturn(scheduleTasks);
        when(scheduleTasks.get(0).getStatus()).thenReturn(TaskStatus.FAILED);
        when(scheduleTasks.get(1).getStatus()).thenReturn(TaskStatus.FAILED);

        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);
        verify(onlineSchemaChangeFlowableTask, times(1)).setStatus(TaskStatus.FAILED);

        // Test with canceled task
        tasks = Mockito.mock(Page.class);
        when(tasks.getSize()).thenReturn(1);
        scheduleTasks = List.of(Mockito.mock(ScheduleTaskEntity.class, "task1"));
        when(tasks.getContent()).thenReturn(scheduleTasks);
        when(scheduleTasks.get(0).getStatus()).thenReturn(TaskStatus.CANCELED);

        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);
        verify(onlineSchemaChangeFlowableTask, times(1)).setStatus(TaskStatus.CANCELED);

        // Test with tasks in progress
        tasks = Mockito.mock(Page.class);
        when(tasks.getSize()).thenReturn(1);
        scheduleTasks = List.of(Mockito.mock(ScheduleTaskEntity.class, "task1"));
        when(tasks.getContent()).thenReturn(scheduleTasks);
        when(scheduleTasks.get(0).getStatus()).thenReturn(TaskStatus.RUNNING);

        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);
        verify(onlineSchemaChangeFlowableTask, times(1)).setStatus(TaskStatus.RUNNING);
    }

    @Test
    public void testSingleTaskPercentage() {
        assertEquals(0.0, onlineSchemaChangeFlowableTask.singleTaskPercentage(null));
        assertEquals(0.0, onlineSchemaChangeFlowableTask.singleTaskPercentage(Mockito.mock(ScheduleTaskEntity.class, "task1")), 0.0);
        assertEquals(0.5, onlineSchemaChangeFlowableTask.singleTaskPercentage(Mockito.mock(ScheduleTaskEntity.class, "task1")), 0.0);
        assertEquals(1.0, onlineSchemaChangeFlowableTask.singleTaskPercentage(Mockito.mock(ScheduleTaskEntity.class, "task1")), 0.0);
    }

    @Test
    public void testCancel() throws Exception {
        when(delegateExecution.isInterruptInProgress()).thenReturn(false);
        when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(Mockito.mock(Page.class));

        onlineSchemaChangeFlowableTask.cancel(true, 1L, Mockito.mock(TaskService.class));
        verify(log, times(1)).warn("Online schema change task canceled, taskId={}", 1L);
        verify(taskHandler, times(1)).terminate(scheduleId, 1L);
        verify(onlineSchemaChangeFlowableTask, times(1)).setStatus(TaskStatus.CANCELED);
        verify(taskService, times(1)).cancel(1L);
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        String schema = "test";
        OnlineSchemaChangeContextHolder.set(1L, 1L, 1L);

        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schema);

        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(TaskStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(1L, scheduleEntity.getConnectionId());
        assertEquals("test", scheduleEntity.getDatabaseName());
        assertEquals(1L, scheduleEntity.getCreatorId());
        assertEquals(1L, scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals(connectionConfig.getId(), scheduleEntity.getConnectionId());
        assertEquals(JsonUtils.toJson(onlineSchemaChangeParameters), scheduleEntity.getJobParametersJson());
        assertNotNull(scheduleEntity.getCronExpression());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        long scheduleId = 1L;

        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, onlineSchemaChangeScheduleTaskParameters);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals(onlineSchemaChangeScheduleTaskParameters, JsonUtils.parse(scheduleTaskEntity.getParametersJson(), OnlineSchemaChangeScheduleTaskParameters.class));
    }
}