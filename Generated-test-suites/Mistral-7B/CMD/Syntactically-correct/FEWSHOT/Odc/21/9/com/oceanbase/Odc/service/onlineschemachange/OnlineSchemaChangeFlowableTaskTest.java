package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.cli.util.DateUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
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
import com.oceanbase.odc.metadb.model.ConnectionConfig;
import com.oceanbase.odc.metadb.model.ScheduleEntity;
import com.oceanbase.odc.metadb.model.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleService;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.metadb.task.TaskService;
import com.oceanbase.odc.service.connection.model.ConnectionConfigFactory;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleServiceImpl;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.TriggerType;
import com.oceanbase.odc.service.task.TaskServiceImpl;

/**
 * Test class for OnlineSchemaChangeFlowableTask
 *
 * @author yaobin
 */
public class OnlineSchemaChangeFlowableTaskTest {
    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private QuartzJobService quartzJobService;

    @Mock
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @Mock
    private TaskService taskService;

    @Mock
    private FlowTaskUtil flowTaskUtil;

    @Mock
    private DelegateExecution delegateExecution;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(flowTaskUtil.getTaskCreator(delegateExecution)).thenReturn(new User());
        when(flowTaskUtil.getOnlineSchemaChangeParameter(delegateExecution)).thenReturn(onlineSchemaChangeParameters);
        when(flowTaskUtil.getConnectionConfig(delegateExecution)).thenReturn(connectionConfig);
        when(organizationService.getOrgId(connectionConfig)).thenReturn(1L);
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());
        when(scheduleRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());
        when(taskService.detail(anyLong())).thenReturn(new TaskEntity());
        when(taskService.start(anyLong())).thenReturn(true);
        when(taskService.update(any(TaskEntity.class))).thenReturn(true);
        when(taskService.cancel(anyLong())).thenReturn(true);
        when(taskService.listTask(any(Pageable.class), anyLong())).thenReturn(Page.empty());
        when(taskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(Page.of(new ScheduleTaskEntity()));
    }

    @Test
    public void testStart() throws Exception {
        when(taskService.detail(anyLong())).thenReturn(new TaskEntity());
        when(taskService.start(anyLong())).thenReturn(true);
        when(taskService.update(any(TaskEntity.class))).thenReturn(true);

        OnlineSchemaChangeTaskResult result = onlineSchemaChangeFlowableTask.start(1L, taskService, delegateExecution);
        assertEquals(null, result);

        verify(taskService).start(1L);
        verify(taskService).update(eq(new TaskEntity()), any());
    }

    @Test
    public void testIsSuccessful() {
        TaskStatus taskStatus = TaskStatus.DONE;
        when(taskService.detail(anyLong())).thenReturn(new TaskEntity().setStatus(taskStatus));
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        TaskStatus taskStatus = TaskStatus.FAILED;
        when(taskService.detail(anyLong())).thenReturn(new TaskEntity().setStatus(taskStatus));
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        long taskId = 1L;
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);
        verify(taskService).cancel(taskId);
    }

    @Test
    public void testOnSuccessful() {
        long taskId = 1L;
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);
        verify(taskService).update(eq(new TaskEntity()), any());
        verify(scheduleService).updateFlowInstanceStatus(eq(FlowStatus.EXECUTION_SUCCEEDED), anyLong());
    }

    @Test
    public void testOnTimeout() {
        long taskId = 1L;
        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);
        // no verification needed
    }

    @Test
    public void testOnProgressUpdate() {
        Page<ScheduleTaskEntity> tasks = Page.of(new ScheduleTaskEntity(), new ScheduleTaskEntity());
        when(taskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(tasks);
        when(singleTaskPercentage(any())).thenReturn(0.5);
        when(progressStatusUpdate(any())).thenReturn(TaskStatus.DONE);

        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        verify(taskService).update(any(TaskEntity.class));
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = ConnectionConfigFactory.buildConnectionConfig("test", "test", "test", "test",
                "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test",
                "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test",
                "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test",
                "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test",
                "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test",
                "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test",
                "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test",
                "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test",
                "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test",
                "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test",
                "test", "test", "test");
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig,
                onlineSchemaChangeParameters, "test");

        assertEquals("test", scheduleEntity.getDatabaseName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleEntity.getId(),
                onlineSchemaChangeScheduleTaskParameters);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("test", scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
    }

    private double singleTaskPercentage(ScheduleTaskEntity scheduleTask) {
        // Implementation of singleTaskPercentage method
    }

    private ScheduleStatus progressStatusUpdate(Page<ScheduleTaskEntity> tasks) {
        // Implementation of progressStatusUpdate method
    }
}