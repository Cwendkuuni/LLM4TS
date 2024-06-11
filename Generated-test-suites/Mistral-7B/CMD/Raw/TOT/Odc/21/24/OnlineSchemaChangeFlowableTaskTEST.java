package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.iam.OrganizationService;
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

import lombok.extern.slf4j.Slf4j;

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
    private OnlineSchemaChangeTaskHandler taskHandler;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask task;

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private ScheduleEntity scheduleEntity;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ExecutionEntity executionEntity;
    private Page<ScheduleTaskEntity> tasks;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        connectionConfig = new ConnectionConfig();
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        scheduleEntity = new ScheduleEntity();
        scheduleTaskEntity = new ScheduleTaskEntity();
        executionEntity = new ExecutionEntity();
        tasks = new PageImpl<>(ListUtils.emptyIfNull(List.of(scheduleTaskEntity)));

        when(scheduleTaskRepository.saveAndFlush(scheduleTaskEntity)).thenReturn(scheduleTaskEntity);
        when(scheduleTaskService.listTask(any(Pageable.class), eq(scheduleId))).thenReturn(tasks);
        when(quartzJobService.terminate(anyLong(), anyLong())).thenReturn(true);
    }

    @Test
    public void testStart() {
        TaskService taskService = mock(TaskService.class);

        when(taskService.start(anyLong())).thenReturn(true);

        when(task.start(anyLong(), eq(taskService), any())).thenCallRealMethod();

        Long taskId = 1L;
        task.start(taskId, taskService, executionEntity);

        verify(taskService, times(1)).start(taskId);
    }

    @Test
    public void testIsSuccessful() {
        when(task.isSuccessful()).thenCallRealMethod();

        assertTrue(task.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        task.status = TaskStatus.FAILED;

        when(task.isFailure()).thenCallRealMethod();

        assertTrue(task.isFailure());
    }

    @Test
    public void testOnFailure() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);

        when(taskService.cancel(taskId)).thenReturn(true);

        when(task.onFailure(taskId, eq(taskService))).thenCallRealMethod();

        task.onFailure(taskId, taskService);

        verify(taskService, times(1)).cancel(taskId);
    }

    @Test
    public void testOnSuccessful() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);

        when(taskService.update(any(TaskEntity.class))).thenCallRealMethod();

        when(task.onSuccessful(taskId, eq(taskService))).thenCallRealMethod();

        task.onSuccessful(taskId, taskService);

        ArgumentCaptor<TaskEntity> taskEntityArgumentCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskService, times(1)).update(taskEntityArgumentCaptor.capture());
        TaskEntity capturedTaskEntity = taskEntityArgumentCaptor.getValue();
        assertEquals("The taskEntity passed to update should be the same as the instance variable", task, capturedTaskEntity);
    }

    @Test
    public void testOnTimeout() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);

        when(taskService.cancel(taskId)).thenReturn(true);

        when(task.onTimeout(taskId, eq(taskService))).thenCallRealMethod();

        task.onTimeout(taskId, taskService);

        verify(taskService, times(1)).cancel(taskId);
    }

    @Test
    public void testOnProgressUpdate() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);

        when(taskService.update(any(TaskEntity.class))).thenCallRealMethod();

        when(scheduleTaskService.listTask(any(Pageable.class), eq(scheduleId))).thenReturn(tasks);

        when(task.singleTaskPercentage(eq(scheduleTaskEntity))).thenReturn(1.0);

        when(task.progressStatusUpdate(tasks)).thenCallRealMethod();

        when(task.onProgressUpdate(taskId, eq(taskService))).thenCallRealMethod();

        task.percentage = 0.0;
        task.onProgressUpdate(taskId, taskService);

        ArgumentCaptor<TaskEntity> taskEntityArgumentCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskService, times(1)).update(taskEntityArgumentCaptor.capture());
        TaskEntity capturedTaskEntity = taskEntityArgumentCaptor.getValue();
        assertEquals("The taskEntity passed to update should be the same as the instance variable", task, capturedTaskEntity);
    }

    @Test
    public void testCancel() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);

        when(taskService.cancel(taskId)).thenReturn(true);

        when(task.cancel(false, taskId, eq(taskService))).thenCallRealMethod();

        task.cancel(false, taskId, taskService);

        verify(taskService, times(1)).cancel(taskId);
    }

    @Test
    public void testIsCancelled() {
        task.status = TaskStatus.CANCELED;

        when(task.isCancelled()).thenCallRealMethod();

        assertTrue(task.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        when(organizationService.getCreator()).thenReturn(new User());

        ScheduleEntity expectedScheduleEntity = createScheduleEntity();

        when(task.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, "schema")).thenCallRealMethod();

        ScheduleEntity actualScheduleEntity = task.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, "schema");

        assertEquals("The created scheduleEntity should be the same as the expected scheduleEntity", expectedScheduleEntity, actualScheduleEntity);
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        ScheduleTaskEntity expectedScheduleTaskEntity = createScheduleTaskEntity();

        when(task.createScheduleTaskEntity(scheduleId, onlineSchemaChangeScheduleTaskParameters())).thenCallRealMethod();

        ScheduleTaskEntity actualScheduleTaskEntity = task.createScheduleTaskEntity(scheduleId, onlineSchemaChangeScheduleTaskParameters());

        assertEquals("The created scheduleTaskEntity should be the same as the expected scheduleTaskEntity", expectedScheduleTaskEntity, actualScheduleTaskEntity);
    }

    @Test
    public void testSingleTaskPercentage() {
        ScheduleTaskEntity scheduleTaskEntity = createScheduleTaskEntity();
        double expectedPercentage = 1.0;

        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        double actualPercentage = task.singleTaskPercentage(scheduleTaskEntity);

        assertEquals("The percentage should be the same as the expected percentage", expectedPercentage, actualPercentage, 0.0);
    }

    @Test
    public void testProgressStatusUpdate() {
        Page<ScheduleTaskEntity> tasks = new PageImpl<>(ListUtils.emptyIfNull(List.of(scheduleTaskEntity, scheduleTaskEntity, scheduleTaskEntity)));

        int successfulTask = 2;
        int failedTask = 1;
        boolean canceled = false;

        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        task.progressStatusUpdate(tasks);

        assertEquals("The status should be TaskStatus.DONE", TaskStatus.DONE, task.status);
    }

    private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters() {
        return new OnlineSchemaChangeScheduleTaskParameters();
    }

    private ScheduleEntity createScheduleEntity() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setConnectionId(connectionConfig.id());
        scheduleEntity.setDatabaseName("schema");
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
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(onlineSchemaChangeParameters));
        return scheduleEntity;
    }

    private ScheduleTaskEntity createScheduleTaskEntity() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
        scheduleTaskEntity.setJobName("scheduleId");
        scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        scheduleTaskEntity.setProgressPercentage(0.0);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(onlineSchemaChangeScheduleTaskParameters()));
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(onlineSchemaChangeScheduleTaskParameters())));
        scheduleTaskEntity.setFireTime(new Date());
        return scheduleTaskEntity;
    }
}