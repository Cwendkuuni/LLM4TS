package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Mockito;
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
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
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
    private OrganizationService organizationService;

    private BaseODCFlowTaskDelegate<Void> testee;
    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters parameter;

    @Before
    public void setup() {
        testee = mock(OnlineSchemaChangeFlowableTask.class);
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        parameter = Mockito.mock(OnlineSchemaChangeParameters.class);
    }

    @Test
    public void testStart() throws Exception {
        when(taskService.detail(anyLong())).thenReturn(new TaskEntity());
        when(organizationService.getById(anyLong())).thenReturn(Mockito.mock(User.class));
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(any(DelegateExecution.class)))
                .thenReturn(parameter);
        when(FlowTaskUtil.getConnectionConfig(any(DelegateExecution.class))).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(any(DelegateExecution.class))).thenReturn("testSchema");
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());
        when(quartzJobService.scheduleJob(anyString(), anyString(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(true);
        when(taskHandler.start(anyLong(), anyLong())).thenReturn(true);

        testee.start(1L, Mockito.mock(TaskService.class), Mockito.mock(DelegateExecution.class));

        verify(taskService, times(1)).start(1L);
        verify(quartzJobService, times(1)).scheduleJob(anyString(), anyString(), anyString(), anyLong(), anyLong(), anyLong());
        verify(taskHandler, times(1)).start(anyLong(), anyLong());
    }

    @Test
    public void testIsSuccessful() {
        testee.status = TaskStatus.DONE;
        assertTrue(testee.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        testee.status = TaskStatus.FAILED;
        assertTrue(testee.isFailure());
    }

    @Test
    public void testOnFailure() {
        when(taskService.detail(anyLong())).thenThrow(new Exception());

        testee.start(1L, Mockito.mock(TaskService.class), Mockito.mock(DelegateExecution.class));

        verify(log, times(1)).warn("Online schema change task failed, taskId={}", 1);
    }

    @Test
    public void testOnSuccessful() {
        when(taskService.detail(anyLong())).thenReturn(new TaskEntity());
        testee.status = TaskStatus.DONE;
        testee.updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);

        testee.onSuccessful(1L, Mockito.mock(TaskService.class));

        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testOnTimeout() {
        when(taskService.detail(anyLong())).thenReturn(new TaskEntity());

        testee.onTimeout(1L, Mockito.mock(TaskService.class));

        verify(log, times(1)).warn("Online schema change task timeout, taskId={}", 1);
    }

    @Test
    public void testOnProgressUpdate() {
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        tasks.add(new ScheduleTaskEntity());
        when(scheduleTaskService.listTask(any(Pageable.class), eq(scheduleId))).thenReturn(Mockito.mock(Page.class));

        testee.progressStatusUpdate(tasks);

        verify(testee, times(1)).percentage = 100;
    }

    @Test
    public void testSingleTaskPercentage() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
        scheduleTaskEntity.setProgressPercentage(50);

        double percentage = testee.singleTaskPercentage(scheduleTaskEntity);

        assertEquals(0.5, percentage, 0.0);
    }

    @Test
    public void testProgressStatusUpdate() {
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        tasks.add(new ScheduleTaskEntity());
        tasks.add(new ScheduleTaskEntity());

        testee.progressStatusUpdate(tasks);

        verify(testee, times(1)).status = TaskStatus.FAILED;
    }

    @Test
    public void testCancel() {
        when(scheduleTaskService.listTask(any(Pageable.class), eq(scheduleId))).thenReturn(Mockito.mock(Page.class));

        testee.cancel(true, 1L, Mockito.mock(TaskService.class));

        verify(testee, times(1)).status = TaskStatus.CANCELED;
        verify(taskService, times(1)).cancel(anyLong());
    }

    @Test
    public void testIsCancelled() {
        testee.status = TaskStatus.CANCELED;
        assertTrue(testee.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        when(organizationService.getById(anyLong())).thenReturn(Mockito.mock(User.class));

        OnlineSchemaChangeEntity scheduleEntity = testee.createScheduleEntity(connectionConfig, parameter, "testSchema");

        assertEquals(connectionConfig.getId(), scheduleEntity.getConnectionId());
        assertEquals("testSchema", scheduleEntity.getDatabaseName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(false, scheduleEntity.isAllowConcurrent());
        assertEquals(creatorId, scheduleEntity.getCreatorId());
        assertEquals(organizationId, scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals(creatorId, scheduleEntity.getModifierId());
        assertEquals(JsonUtils.toJson(new TriggerConfig()), scheduleEntity.getTriggerConfigJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, scheduleEntity.getMisfireStrategy());
        assertEquals(JsonUtils.toJson(parameter), scheduleEntity.getJobParametersJson());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeTaskEntity scheduleTaskEntity = testee.createScheduleTaskEntity(scheduleId, parameter);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("testJobName", scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage(), 0.0);
        assertEquals(JsonUtils.toJson(parameter), scheduleTaskEntity.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeTaskResult(parameter)), scheduleTaskEntity.getResultJson());
        assertEquals(new Date(), scheduleTaskEntity.getFireTime());
    }
}