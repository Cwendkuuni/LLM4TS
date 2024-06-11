package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
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
import com.oceanbase.odc.service.task.model.TaskResult;

@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

    private BaseODCFlowTaskDelegate<Void> onlineSchemaChangeFlowableTask;
    private TaskService taskService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleTaskService scheduleTaskService;
    private QuartzJobService quartzJobService;
    private OrganizationService organizationService;
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @Before
    public void setup() {
        taskService = mock(TaskService.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleTaskService = mock(ScheduleTaskService.class);
        quartzJobService = mock(QuartzJobService.class);
        organizationService = mock(OrganizationService.class);
        oscTaskCompleteHandler = mock(OscTaskCompleteHandler.class);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setTaskService(taskService);
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
        onlineSchemaChangeFlowableTask.setOscTaskCompleteHandler(oscTaskCompleteHandler);

        this.onlineSchemaChangeFlowableTask = onlineSchemaChangeFlowableTask;
    }

    @Test
    public void testStart() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        TaskEntity task = Mockito.spy(new TaskEntity());

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);

        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        parameter.setContinueOnError(true);
        parameter.setSchemaName("test_schema");

        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);

        ScheduleEntity schedule = Mockito.spy(new ScheduleEntity());
        schedule.setId(1L);
        schedule.setStatus(ScheduleStatus.ENABLED);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = List.of(
                new OnlineSchemaChangeScheduleTaskParameters()
        );

        List<ScheduleTaskEntity> tasks = subTaskParameters.stream()
                .map(param -> {
                    param.setUid("test_uid");
                    return new ScheduleTaskEntity();
                }).collect(Collectors.toList());

        when(quartzJobService.startJob(eq(ScheduleJobBuilder.newJob(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name())),
                anyList(), anyMap()))
                .thenReturn(1L);

        when(taskService.start(eq(1L)))
                .thenReturn(null);

        when(taskService.detail(eq(1L)))
                .thenReturn(task);

        when(scheduleTaskService.listTask(Pageable.unpaged(), eq(1L)))
                .thenReturn(Page.empty());

        when(flowTaskUtil.getCloudMainAccountId(execution))
                .thenReturn("test_uid");

        when(flowTaskUtil.getOnlineSchemaChangeParameter(execution))
                .thenReturn(parameter);

        when(flowTaskUtil.getConnectionConfig(execution))
                .thenReturn(connectionConfig);

        when(flowTaskUtil.getSchemaName(execution))
                .thenReturn("test_schema");

        when(organizationService.findById(eq(1L)))
                .thenReturn(new User());

        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OnlineSchemaChangeContextHolder.clear();

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        verify(taskService, times(1)).start(eq(1L));
        verify(quartzJobService, times(1)).startJob(eq(ScheduleJobBuilder.newJob(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name())),
                anyList(), anyMap());
        verify(onlineSchemaChangeFlowableTask, times(1)).createScheduleEntity(eq(connectionConfig), eq(parameter), eq("test_schema"));
    }

    @Test
    public void testIsSuccessful() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = this.onlineSchemaChangeFlowableTask;
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);

        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = this.onlineSchemaChangeFlowableTask;
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);

        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() throws Exception {
        Long taskId = 1L;

        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        verify(taskService, times(1)).cancel(eq(taskId), anyBoolean());
        verify(log, times(1)).warn("Online schema change task failed, taskId={}", taskId);
    }

    @Test
    public void testOnSuccessful() throws Exception {
        Long taskId = 1L;
        TaskResult taskResult = new TaskResult();
        taskResult.setStatus(TaskStatus.DONE);

        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        verify(taskService, times(1)).update(eq(taskResult));
        verify(log, times(1)).info("Online schema change task succeed, taskId={}", taskId);
    }

    @Test
    public void testOnTimeout() throws Exception {
        Long taskId = 1L;

        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        verify(log, times(1)).warn("Online schema change task timeout, taskId={}", taskId);
    }

    @Test
    public void testProgressUpdate() throws Exception {
        Long taskId = 1L;
        Page<ScheduleTaskEntity> tasks = Page.of(List.of(new ScheduleTaskEntity(), new ScheduleTaskEntity(), new ScheduleTaskEntity()), Pageable.unpaged());

        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);

        verify(onlineSchemaChangeFlowableTask, times(1)).progressStatusUpdate(tasks.getContent());
    }

    @Test
    public void testSingleTaskPercentage() throws Exception {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
        scheduleTaskEntity.setProgressPercentage(50);

        double percentage = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity);

        assertEquals(0.5, percentage, 0.01);
    }

    @Test
    public void testCancel() throws Exception {
        boolean canceled = onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        assertTrue(canceled);
        verify(taskService, times(1)).cancel(eq(1L), anyBoolean());
        verify(log, times(1)).warn("Online schema change task canceled, taskId={}", 1L);
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = this.onlineSchemaChangeFlowableTask;
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);

        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);

        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        parameter.setContinueOnError(true);
        parameter.setSchemaName("test_schema");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setStatus(ScheduleStatus.ENABLED);

        when(organizationService.findById(eq(1L)))
                .thenReturn(new User());

        ScheduleEntity result = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, "test_schema");

        assertEquals(1L, result.getId());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, result.getJobType());
        assertEquals(ScheduleStatus.ENABLED, result.getStatus());
        assertEquals(1L, result.getCreatorId());
        assertEquals(1L, result.getOrganizationId());
        assertEquals("test_schema", result.getDatabaseName());
        assertEquals(TriggerStrategy.CRON, result.getTriggerStrategy());
        assertEquals(checkTaskCronExpression, result.getTriggerConfigJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, result.getMisfireStrategy());
        assertEquals(1L, result.getProjectId());
        assertEquals(1L, result.getDatabaseId());
        assertEquals(JsonUtils.toJson(parameter), result.getJobParametersJson());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = Mockito.spy(new OnlineSchemaChangeScheduleTaskParameters());

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);

        ScheduleTaskEntity result = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

        assertEquals(scheduleId, result.getScheduleId());
        assertEquals(TaskStatus.PREPARING, result.getStatus());
        assertEquals(1L, result.getProgressPercentage());
        assertEquals(param.getSql(), result.getParametersJson());
    }
}