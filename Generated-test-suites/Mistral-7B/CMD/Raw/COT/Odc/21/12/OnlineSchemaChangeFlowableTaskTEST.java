package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.context.FlowableEngineContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
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
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult.Status;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult.SubtaskStatus;

/**
 * Test class for OnlineSchemaChangeFlowableTask
 *
 * @author yaobin
 */
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
    private OnlineSchemaChangeTaskHandler taskHandler;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private OrganizationService organizationService;
    @Mock
    private TaskService taskService;
    @Mock
    private FlowableEngineContext flowableEngineContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() {
        DelegateExecution execution = mock(DelegateExecution.class);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "schemaName";
        when(taskService.detail(anyLong())).thenReturn(new TaskEntity());
        when(flowableEngineContext.getServiceBean("scheduleTaskService")).thenReturn(scheduleTaskService);
        when(flowableEngineContext.getServiceBean("scheduleService")).thenReturn(scheduleService);
        when(flowableEngineContext.getServiceBean("taskService")).thenReturn(taskService);
        when(flowableEngineContext.getServiceBean("quartzJobService")).thenReturn(quartzJobService);
        when(flowableEngineContext.getServiceBean("organizationService")).thenReturn(organizationService);
        when(organizationService.getCurrentUser()).thenReturn(mock(User.class));
        when(taskHandler.start(anyLong(), anyLong())).thenReturn(true);

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        verify(scheduleTaskService).create(any(ScheduleEntity.class));
        verify(quartzJobService).scheduleJob(any(), any(), any());
        verify(taskHandler).start(anyLong(), anyLong());
    }

    @Test
    public void testIsSuccessful() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.DONE);

        assertTrue(task.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.FAILED);

        assertTrue(task.isFailure());
    }

    @Test
    public void testOnFailure() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.onFailure(1L, taskService);

        verify(taskService).update(any(TaskEntity.class));
    }

    @Test
    public void testOnSuccessful() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.onSuccessful(1L, taskService);

        verify(taskService).update(any(TaskEntity.class));
    }

    @Test
    public void testOnTimeout() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.onTimeout(1L, taskService);

        // Nothing to verify since there's no external dependency
    }

    @Test
    public void testOnProgressUpdate() {
        Page<ScheduleTaskEntity> scheduleTasks = new PageImpl<>(ListUtils.emptyIfNull(List.of(new ScheduleTaskEntity())));
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();

        task.onProgressUpdate(1L, taskService);

        verify(taskService).update(any(TaskEntity.class));
    }

    @Test
    public void testCancel() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();

        assertTrue(task.cancel(true, 1L, taskService));

        // Nothing to verify since there's no external dependency
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.CANCELED);

        assertTrue(task.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "schemaName";

        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);

        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(SchemaStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(2L, scheduleEntity.getDatabaseId());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("OnlineSchemaChangeFlowableTaskTest", scheduleTaskEntity.getJobName());
        assertEquals("ONLINE_SCHEMA_CHANGE_COMPLETE", scheduleTaskEntity.getJobGroup());
    }

    @Test
    public void testSingleTaskPercentage() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);

        double percentage = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity);

        assertEquals(0.0, percentage, 0.01);

        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
        scheduleTaskEntity.setProgressPercentage(25.0);

        percentage = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity);

        assertEquals(0.25, percentage, 0.01);

        scheduleTaskEntity.setStatus(TaskStatus.DONE);

        percentage = onlineSchemaChangeFlowableTest.singleTaskPercentage(scheduleTaskEntity);

        assertEquals(1.0, percentage, 0.01);
    }

    @Test
    public void testProgressStatusUpdate() {
        Page<ScheduleTaskEntity> tasks = new PageImpl<>(ListUtils.emptyIfNull(List.of(new ScheduleTaskEntity(), new ScheduleTaskEntity())));

        onlineSchemaChangeFlowableTest.progressStatusUpdate(tasks);

        verify(onlineSchemaChangeFlowableTest, times(2)).getStatus(any(ScheduleTaskEntity.class));
        verify(onlineSchemaChangeFlowableTest, times(2)).setStatus(any(ScheduleTaskEntity.class), eq(TaskStatus.FAILED));
    }

    @Test
    public void testSingleTaskStatus() {
        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(List.of(new OnlineSchemaChangeTaskResult.SubtaskStatus("subTask1", Status.DONE), new OnlineSchemaChangeTaskResult.SubtaskStatus("subTask2", Status.FAILED)));

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        TaskStatus taskStatus = onlineSchemaChangeFlowableTask.singleTaskStatus(result);

        assertEquals(TaskStatus.FAILED, taskStatus);
    }

    @Test
    public void testListTask() {
        Pageable pageable = Pageable.unpaged();
        Page<TaskEntity> tasks = new PageImpl<>(ListUtils.emptyIfNull(List.of(new TaskEntity(), new TaskEntity())));

        when(taskService.listTask(pageable, anyLong())).thenReturn(tasks);

        Page<OnlineSchemaChangeFlowableTask> onlineSchemaChangeFlowableTasks = onlineSchemaChangeFlowableTest.listTask(pageable, 1L);

        verify(taskService, times(1)).listTask(pageable, anyLong());
        assertEquals(2, onlineSchemaChangeFlowableTasks.getTotalElements());
    }

    @Test
    public void testListTasks() {
        OnlineSchemaChangeFlowableTask firstTask = new OnlineSchemaChangeFlowableTask();
        OnlineSchemaChangeFlowableTask secondTask = new OnlineSchemaChangeFlowableTask();

        when(taskService.listAll()).thenReturn(List.of(firstTask, secondTask));

        List<OnlineSchemaChangeFlowableTask> onlineSchemaChangeFlowableTasks = onlineSchemaChangeFlowableTest.listTasks();

        verify(taskService, times(1)).listAll();
        assertEquals(2, onlineSchemaChangeFlowableTasks.size());
    }
}