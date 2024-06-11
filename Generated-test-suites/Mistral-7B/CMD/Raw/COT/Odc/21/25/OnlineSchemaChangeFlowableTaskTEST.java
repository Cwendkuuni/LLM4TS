package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
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
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

/**
 * Test class for OnlineSchemaChangeFlowableTask
 */
@Slf4j
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
    private OrganizationService organizationService;

    @Mock
    private TaskService taskService;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private DelegateExecution execution;

    @Mock
    private User user;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask testee;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart_normalFlow() {
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        when(execution.getVariables()).thenReturn(Map.of("onlineSchemaChangeParameters", parameter));

        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.id()).thenReturn(1L);
        when(taskService.detail(anyLong())).thenReturn(new ExecutionEntity());

        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getId()).thenReturn(1L);
        when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);

        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        when(scheduleTaskEntity.getId()).thenReturn(1L);

        when(scheduleTaskRepository.saveAndFlush(scheduleTaskEntity)).thenReturn(scheduleTaskEntity);

        when(taskService.listTask(Pageable.unpaged(), 1L)).thenReturn(Page.empty());

        when(taskService.update(any(TaskEntity.class))).thenReturn(true);

        testee.start(1L, taskService, execution);

        verify(taskService, times(1)).start(1L);
        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test(expected = Exception.class)
    public void testStart_exception() {
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        when(execution.getVariables()).thenReturn(Map.of("onlineSchemaChangeParameters", parameter));

        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.id()).thenThrow(new RuntimeException());

        testee.start(1L, taskService, execution);

        verify(taskService, never()).start(anyLong());
    }

    @Test
    public void testIsSuccessful_normalFlow() {
        taskee.status = TaskStatus.DONE;
        assertTrue(testee.isSuccessful());
    }

    @Test
    public void testIsSuccessful_failedFlow() {
        taskee.status = TaskStatus.FAILED;
        assertFalse(testee.isSuccessful());
    }

    @Test
    public void testIsFailure_normalFlow() {
        taskee.status = TaskStatus.FAILED;
        assertTrue(testee.isFailure());
    }

    @Test
    public void testIsFailure_doneFlow() {
        taskee.status = TaskStatus.DONE;
        assertFalse(testee.isFailure());
    }

    @Test
    public void testOnFailure_normalFlow() {
        testee.onFailure(1L, taskService);
        verify(taskService, times(1)).cancel(anyBoolean(), anyLong(), eq(taskService));
    }

    @Test
    public void testOnSuccessful_normalFlow() {
        testee.onSuccessful(1L, taskService);
        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testOnTimeout_normalFlow() {
        testee.onTimeout(1L, taskService);
        verify(taskService, times(1)).cancel(anyBoolean(), anyLong(), eq(taskService));
    }

    @Test
    public void testOnProgressUpdate_normalFlow() {
        Page<ScheduleTaskEntity> tasks = mock(Page.class);
        when(tasks.getSize()).thenReturn(2);
        when(tasks.getContent()).thenReturn(Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity()));

        when(taskService.detail(anyLong())).thenReturn(new ExecutionEntity());

        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(tasks);

        testee.progressStatusUpdate(tasks);

        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProgressStatusUpdate_emptyTasks() {
        Page<ScheduleTaskEntity> tasks = mock(Page.class);
        when(tasks.getSize()).thenReturn(0);
        testee.progressStatusUpdate(tasks);
    }

    @Test
    public void testSingleTaskPercentage_normalFlow() {
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        when(scheduleTaskEntity.getProgressPercentage()).thenReturn(50);

        double actual = testee.singleTaskPercentage(scheduleTaskEntity);
        assertEquals(0.5, actual, 0.001);
    }

    @Test
    public void testSingleTaskPercentage_preparingFlow() {
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.PREPARING);

        double actual = testee.singleTaskPercentage(scheduleTaskEntity);
        assertEquals(0.0, actual, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingleTaskPercentage_nullTask() {
        testee.singleTaskPercentage(null);
    }

    @Test
    public void testCancel_normalFlow() {
        when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(Page.empty());
        testee.cancel(true, 1L, taskService);
        verify(taskService, times(1)).cancel(true, anyLong(), eq(taskService));
    }

    @Test
    public void testCancel_runningTask() {
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(scheduleTaskEntity);

        when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(Page.of(scheduleTaskEntity));

        testee.cancel(true, 1L, taskService);
        verify(taskService, times(1)).cancel(true, 1L, eq(taskService));
        verify(quartzJobService, times(1)).terminate(1L, 1L);
    }

    @Test
    public void testIsCancelled_normalFlow() {
        testee.status = TaskStatus.CANCELED;
        assertTrue(testee.isCancelled());
    }

    @Test
    public void testIsCancelled_nonCancelledFlow() {
        testee.status = TaskStatus.DONE;
        assertFalse(testee.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity_normalFlow() {
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setConnectionId(connectionConfig.getId());
        scheduleEntity.setDatabaseName(schema);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setStatus(ScheduleStatus.ENABLED);
        scheduleEntity.setAllowConcurrent(false);
        scheduleEntity.setCreatorId(1L);
        scheduleEntity.setOrganizationId(1L);
        scheduleEntity.setProjectId(1L);
        scheduleEntity.setDatabaseId(1L);
        scheduleEntity.setModifierId(scheduleEntity.getCreatorId());

        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
        triggerConfig.setCronExpression("0/10 * * * * ?");
        scheduleEntity.setTriggerConfigJson(JsonUtils.toJson(triggerConfig));
        scheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(parameter));

        ScheduleEntity actual = testee.createScheduleEntity(connectionConfig, parameter, schema);

        assertEquals(scheduleEntity.getId(), actual.getId());
        assertEquals(scheduleEntity.getConnectionId(), actual.getConnectionId());
        assertEquals(scheduleEntity.getDatabaseName(), actual.getDatabaseName());
        assertEquals(scheduleEntity.getJobType(), actual.getJobType());
        assertEquals(scheduleEntity.getStatus(), actual.getStatus());
        assertEquals(scheduleEntity.isAllowConcurrent(), actual.isAllowConcurrent());
        assertEquals(scheduleEntity.getCreatorId(), actual.getCreatorId());
        assertEquals(scheduleEntity.getOrganizationId(), actual.getOrganizationId());
        assertEquals(scheduleEntity.getProjectId(), actual.getProjectId());
        assertEquals(scheduleEntity.getDatabaseId(), actual.getDatabaseId());
        assertEquals(scheduleEntity.getModifierId(), actual.getModifierId());
        assertEquals(scheduleEntity.getTriggerConfigJson(), actual.getTriggerConfigJson());
        assertEquals(scheduleEntity.getMisfireStrategy(), actual.getMisfireStrategy());
        assertEquals(scheduleEntity.getJobParametersJson(), actual.getJobParametersJson());
    }

    @Test
    public void testCreateScheduleTaskEntity_normalFlow() {
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters scheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
        scheduleTaskEntity.setJobName(scheduleId.toString());
        scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        scheduleTaskEntity.setProgressPercentage(0.0);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(scheduleTaskParameters));
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(scheduleTaskParameters)));
        scheduleTaskEntity.setFireTime(new Date());

        ScheduleTaskEntity actual = testee.createScheduleTaskEntity(scheduleId, scheduleTaskParameters);

        assertEquals(scheduleTaskEntity.getStatus(), actual.getStatus());
        assertEquals(scheduleTaskEntity.getJobName(), actual.getJobName());
        assertEquals(scheduleTaskEntity.getJobGroup(), actual.getJobGroup());
        assertEquals(scheduleTaskEntity.getProgressPercentage(), actual.getProgressPercentage());
        assertEquals(scheduleTaskEntity.getParametersJson(), actual.getParametersJson());
        assertEquals(scheduleTaskEntity.getResultJson(), actual.getResultJson());
        assertEquals(scheduleTaskEntity.getFireTime(), actual.getFireTime());
    }
}