package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
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
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

/**
 * OnlineSchemaChangeFlowableTaskTest
 *
 * @author yaobin
 * @date 2023-05-23
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
    private OnlineSchemaChangeTaskHandler taskHandler;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private TaskService taskService;

    @Mock
    private DelegateExecution execution;

    @Mock
    private ConnectionConfig connectionConfig;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        when(taskService.start(anyLong())).thenReturn(null);
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(new User());
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(new OnlineSchemaChangeParameters());
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn("schemaName");
        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong()))
                .thenReturn(Pageable.emptyPagination(new ArrayList<>()));
        when(taskHandler.start(anyLong(), anyLong())).thenReturn(true);

        onlineSchemaChangeFlowableTask.start(anyLong(), taskService, execution);

        verify(taskService, times(1)).start(anyLong());
        verify(FlowTaskUtil, times(1)).getTaskCreator(execution);
        verify(FlowTaskUtil, times(1)).getOnlineSchemaChangeParameter(execution);
        verify(FlowTaskUtil, times(1)).getConnectionConfig(execution);
        verify(FlowTaskUtil, times(1)).getSchemaName(execution);
        verify(scheduleTaskService, times(1)).listTask(Pageable.unpaged(), anyLong());
        verify(taskHandler, times(1)).start(anyLong(), anyLong());
    }

    @Test
    public void testIsSuccessful() {
        when(taskService.detail(anyLong())).thenReturn(new TaskEntity());
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.DONE);
        when(taskService.detail(anyLong())).thenReturn(taskEntity);

        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());

        taskEntity.setStatus(TaskStatus.FAILED);
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        when(taskService.detail(anyLong())).thenReturn(new TaskEntity());
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.FAILED);

        assertTrue(onlineSchemaChangeFlowableTask.isFailure());

        taskEntity.setStatus(TaskStatus.DONE);
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        long taskId = 1L;
        when(taskService.detail(taskId)).thenReturn(new TaskEntity());

        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        verify(log, times(1)).warn("Online schema change task failed, taskId={}", taskId);
        verify(taskService, times(1)).detail(taskId);
    }

    @Test
    public void testOnSuccessful() {
        long taskId = 1L;
        when(taskService.detail(taskId)).thenReturn(new TaskEntity());

        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        verify(log, times(1)).info("Online schema change task succeed, taskId={}", taskId);
        verify(taskService, times(1)).detail(taskId);
        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testOnTimeout() {
        long taskId = 1L;
        when(taskService.detail(taskId)).thenReturn(new TaskEntity());

        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        verify(log, times(1)).warn("Online schema change task timeout, taskId={}", taskId);
        verify(taskService, times(1)).detail(taskId);
    }

    @Test
    public void testOnProgressUpdate() {
        long taskId = 1L;
        Page<ScheduleTaskEntity> tasks = Mockito.mock(Page.class);
        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(tasks);

        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

        verify(tasks, times(1)).stream();
        verify(tasks, times(1)).map(any(ScheduleTaskEntity.class));

        OnlineSchemaChangeTaskResult taskResult = new OnlineSchemaChangeTaskResult(new ArrayList<>());
        when(taskService.detail(taskId)).thenReturn(new TaskEntity());
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.DONE);
        taskEntity.setResultJson(JsonUtils.toJson(taskResult));
        when(taskService.detail(taskId)).thenReturn(taskEntity);

        List<ScheduleTaskEntity> scheduleTasks = new ArrayList<>();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setStatus(TaskStatus.RUNNING);
        scheduleTask.setId(1L);
        scheduleTasks.add(scheduleTask);
        when(tasks.getContent()).thenReturn(scheduleTasks);

        OnlineSchemaChangeContextHolder.clear();
        double percentage = 50.0;
        when(singleTaskPercentage(scheduleTask)).thenReturn(percentage);

        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

        verify(taskService, times(1)).update(any(TaskEntity.class));
        verify(taskEntity, times(1)).setResultJson(anyString());
        verify(taskEntity, times(1)).setProgressPercentage(anyDouble());
        verify(taskService, times(1)).update(taskEntity);
        verify(taskEntity, times(1)).setStatus(TaskStatus.DONE);
    }

    @Test
    public void testCancel() {
        when(taskService.cancel(anyBoolean(), anyLong(), any(TaskService.class)))
                .thenReturn(true);

        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService));

        verify(taskService, times(1)).cancel(anyBoolean(), anyLong(), any(TaskService.class));
    }

    @Test
    public void testIsCancelled() {
        when(taskService.detail(anyLong())).thenReturn(new TaskEntity());
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.CANCELED);

        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());

        taskEntity.setStatus(TaskStatus.DONE);
        assertFalse(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "schemaName";

        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);

        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(connectionConfig.getId(), scheduleEntity.getConnectionId());
        assertEquals(schema, scheduleEntity.getDatabaseName());
        assertEquals(creatorId, scheduleEntity.getCreatorId());
        assertEquals(organizationId, scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals(creatorId, scheduleEntity.getModifierId());
        assertNotNull(scheduleEntity.getTriggerConfigJson());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("OnlineSchemaChangeFlowableTask", scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage(), 0.0);
        assertEquals(JsonUtils.toJson(param), scheduleTaskEntity.getParametersJson());
        assertNotNull(scheduleTaskEntity.getResultJson());
        assertEquals(new Date(), scheduleTaskEntity.getFireTime());
    }
}