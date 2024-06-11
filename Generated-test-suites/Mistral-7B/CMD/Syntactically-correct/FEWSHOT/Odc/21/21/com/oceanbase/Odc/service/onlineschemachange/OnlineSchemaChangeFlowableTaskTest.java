package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.cli.util.DateParser;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")
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
    private OscTaskCompleteHandler taskHandler;
    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "testSchema";
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);
        assertEquals("testSchema", scheduleEntity.getDatabaseName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(false, scheduleEntity.isAllowConcurrent());
        assertEquals(creatorId, scheduleEntity.getCreatorId());
        assertEquals(organizationId, scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
        triggerConfig.setCronExpression("0/10 * * * * ?");
        assertEquals(JsonUtils.toJson(triggerConfig), scheduleEntity.getTriggerConfigJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, scheduleEntity.getMisfireStrategy());
        assertEquals(JsonUtils.toJson(parameter), scheduleEntity.getJobParametersJson());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);
        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("1", scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage());
        assertEquals(JsonUtils.toJson(param), scheduleTaskEntity.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(param)), scheduleTaskEntity.getResultJson());
        assertNull(scheduleTaskEntity.getFireTime());
    }

    @Test
    public void testStart() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "testSchema";
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);
        List<OnlineSchemaChangeScheduleTaskParameters> tasks = parameter.generateSubTaskParameters(connectionConfig, schema);
        Page<ScheduleTaskEntity> tasksPage = new PageImpl<>(tasks.stream().map(param -> createScheduleTaskEntity(scheduleId, param)).collect(Collectors.toList()));

        doReturn(tasksPage).when(scheduleTaskService).listTask(any(Pageable.class), eq(scheduleId));
        doNothing().when(quartzJobService).addJob(any(), any(), any());
        doNothing().when(taskHandler).start(eq(scheduleId), eq(tasks.get(0).getId()));

        onlineSchemaChangeFlowableTask.start(0L, mock(TaskService.class), mock(DelegateExecution.class));
        verify(quartzJobService, times(1)).addJob(any(), any(), any());
        verify(taskHandler, times(1)).start(eq(scheduleId), eq(tasks.get(0).getId()));
    }

    @Test(expected = Exception.class)
    public void testStart_tasksFailed() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "testSchema";
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);
        List<OnlineSchemaChangeScheduleTaskParameters> tasks = parameter.generateSubTaskParameters(connectionConfig, schema);
        Page<ScheduleTaskEntity> tasksPage = new PageImpl<>(tasks.stream().map(param -> createScheduleTaskEntity(scheduleId, param)).collect(Collectors.toList()));

        doReturn(tasksPage).when(scheduleTaskService).listTask(any(Pageable.class), eq(scheduleId));
        doThrow(new RuntimeException("test error")).when(taskHandler).start(eq(scheduleId), eq(tasks.get(0).getId()));

        onlineSchemaChangeFlowableTask.start(0L, mock(TaskService.class), mock(DelegateExecution.class));
        verify(quartzJobService, never()).addJob(any(), any(), any());
    }

    private ScheduleTaskEntity createScheduleTaskEntity(Long scheduleId, OnlineSchemaChangeScheduleTaskParameters param) {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        return scheduleTaskEntity;
    }

    @Test
    public void testProgressUpdate() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        List<ScheduleTaskEntity> tasks = mock(List.class);
        Page<ScheduleTaskEntity> tasksPage = new PageImpl<>(tasks);

        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasksPage);
        verify(onlineSchemaChangeFlowableTask, times(1)).progressStatusUpdate(tasks);
    }

    @Test
    public void testProgressUpdate_emptyTasks() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        Page<ScheduleTaskEntity> tasksPage = new PageImpl<>(Collections.emptyList());

        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasksPage);
        verify(onlineSchemaChangeFlowableTask, never()).progressStatusUpdate(tasksPage.getContent());
    }

    @Test
    public void testSingleTaskPercentage() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
        double percentage = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity);
        assertEquals(0.0, percentage);

        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
        scheduleTaskEntity.setProgressPercentage(50);
        percentage = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity);
        assertEquals(0.5, percentage);
    }

    @Test
    public void testSingleTaskPercentage_default() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        double percentage = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity);
        assertEquals(1.0, percentage);
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        assertFalse(onlineSchemaChangeFlowableTask.isCancelled());

        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }
}