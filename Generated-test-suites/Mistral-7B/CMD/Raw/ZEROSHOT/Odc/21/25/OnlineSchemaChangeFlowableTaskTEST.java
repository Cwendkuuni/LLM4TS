package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
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
import com.oceanbase.odc.service.flow.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
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
import com.oceanbase.odc.service.schedule.model.ScheduleTaskDto;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskStatusDto;

import lombok.extern.slf4j.Slf4j;
import mockito.invocation.InvocationOnMock;

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

    @Autowired
    private TaskService taskService;

    @Autowired
    private OscTaskCompleteHandler completeHandler;

    @org.junit.jupiter.api.BeforeEach
    public void setUp() {
        // initialize mocks
        TaskStatusDto taskStatusDto = new TaskStatusDto();
        taskStatusDto.setStatus(TaskStatus.RUNNING.name());
        when(taskService.detail(anyLong())).thenReturn(new TaskEntity().setStatus(taskStatusDto));

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId("test_connection_id");
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        parameter.setConnectionConfig(connectionConfig);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);

        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setScheduleTaskRepository(scheduleTaskRepository);
        task.setScheduleService(scheduleService);
        task.setScheduleTaskService(scheduleTaskService);
        task.setQuartzJobService(quartzJobService);
        task.setTaskHandler(mock(OnlineSchemaChangeTaskHandler.class));
        task.setCompleteHandler(completeHandler);
        task.setOrganizationService(organizationService);
        task.setCheckTaskCronExpression("0/10 * * * * ?");

        this.task = task;
    }

    private OnlineSchemaChangeFlowableTask task;

    @org.junit.jupiter.api.Test
    public void testStart() throws Exception {
        Long taskId = 1L;
        DelegateExecution execution = mock(DelegateExecution.class);

        when(execution.getId()).thenReturn(taskId);
        when(execution.getVariables()).thenReturn(Collections.singletonMap("uid", "test_uid"));
        when(execution.getProcessInstanceId()).thenReturn(taskId.toString());
        when(execution.getVariable("onlineSchemaChangeParameters")).thenReturn(parameter);
        when(execution.getVariable("connectionConfig")).thenReturn(connectionConfig);
        when(execution.getVariable("schema")).thenReturn("test_schema");

        task.start(taskId, taskService, execution);

        verify(taskService, times(1)).start(taskId);
        verify(task.getTaskHandler(), times(1)).start(eq(1L), eq(1L));
    }

    @org.junit.jupiter.api.Test
    public void testIsSuccessful() {
        assertEquals(TaskStatus.RUNNING, task.status);
    }

    @org.junit.jupiter.api.Test
    public void testIsFailure() {
        task.status = TaskStatus.FAILED;
        assertEquals(TaskStatus.FAILED, task.status);
    }

    @org.junit.jupiter.api.Test
    public void testOnFailure() throws Exception {
        Long taskId = 1L;
        when(taskService.detail(taskId)).thenReturn(new TaskEntity().setStatus(new TaskStatusDto().setStatus(TaskStatus.FAILED.name())));
        task.onFailure(taskId, taskService);
        verify(log, times(1)).warn("Online schema change task failed, taskId={}", taskId);
    }

    @org.junit.jupiter.api.Test
    public void testOnSuccessful() throws Exception {
        Long taskId = 1L;
        task.onSuccessful(taskId, taskService);
        verify(taskService, times(1)).updateStatus(taskId, FlowStatus.EXECUTION_SUCCEEDED.name());
    }

    @org.junit.jupiter.api.Test
    public void testOnTimeout() {
        task.onTimeout(1L, taskService);
        verify(log, times(1)).warn("Online schema change task timeout, taskId={}", 1L);
    }

    @org.junit.jupiter.api.Test
    public void testOnProgressUpdate() throws Exception {
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setStatus(TaskStatus.RUNNING);
        scheduleTask.setProgressPercentage(50);
        tasks.add(scheduleTask);

        Page<ScheduleTaskEntity> page = new PageImpl<>(tasks);
        when(scheduleTaskService.listTask(any(Pageable.class), eq(1L))).thenReturn(page);

        task.progressStatusUpdate(page);
        verify(task, times(1)).percentage = (double) (50 * 100.0 / tasks.size());

        TaskEntity flowTask = new TaskEntity();
        flowTask.setId(1L);
        flowTask.setStatus(taskStatusDto);
        when(taskService.detail(1L)).thenReturn(flowTask);

        task.onProgressUpdate(1L, taskService);
        verify(taskService, times(1)).update(flowTask);
        assertEquals(task.percentage, flowTask.getProgressPercentage());
    }

    @org.junit.jupiter.api.Test
    public void testCancel() throws Exception {
        Long taskId = 1L;
        when(taskService.detail(taskId)).thenReturn(new TaskEntity().setStatus(new TaskStatusDto().setStatus(TaskStatus.RUNNING.name())));

        task.cancel(true, taskId, taskService);
        verify(quartzJobService, times(1)).terminate(eq(1L), eq(1L));
        verify(task, times(1)).status = TaskStatus.CANCELED;
        verify(taskService, times(1)).cancel(taskId);
    }

    @org.junit.jupiter.api.Test
    public void testIsCancelled() {
        task.status = TaskStatus.CANCELED;
        assertTrue(task.isCancelled());
    }

    @org.junit.jupiter.api.Test
    public void testCreateScheduleEntity() {
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        ScheduleEntity scheduleEntity = task.createScheduleEntity(connectionConfig, parameter, "test_schema");

        assertEquals(connectionConfig.getId(), scheduleEntity.getConnectionId());
        assertEquals("test_schema", scheduleEntity.getDatabaseName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals(creatorId, scheduleEntity.getCreatorId());
        assertEquals(organizationId, scheduleEntity.getOrganizationId());

        TriggerConfig triggerConfig = JsonUtils.parseJson(scheduleEntity.getTriggerConfigJson(), TriggerConfig.class);
        assertEquals(TriggerStrategy.CRON, triggerConfig.getTriggerStrategy());
        assertEquals("0/10 * * * * ?", triggerConfig.getCronExpression());

        OnlineSchemaChangeScheduleTaskParameters scheduleTaskParameters = JsonUtils.parseJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeScheduleTaskParameters.class);
        assertEquals(connectionConfig, scheduleTaskParameters.getConnectionConfig());
        assertEquals("test_schema", scheduleTaskParameters.getSchema());
    }

    @org.junit.jupiter.api.Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTaskEntity = task.createScheduleTaskEntity(1L, param);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("test_job_name", scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage());
        assertEquals(JsonUtils.toJson(param), scheduleTaskEntity.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(param)), scheduleTaskEntity.getResultJson());
        assertEquals(new Date(), scheduleTaskEntity.getFireTime());
    }
}