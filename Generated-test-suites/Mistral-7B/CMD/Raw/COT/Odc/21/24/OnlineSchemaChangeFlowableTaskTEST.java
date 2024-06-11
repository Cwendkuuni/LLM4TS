package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
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
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.task.model.TaskResult;
import com.oceanbase.odc.service.task.model.TaskStatusUpdateResponse;

@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private TaskService taskService;
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
    private ConnectionConfig connectionConfig;
    @Mock
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    @Mock
    private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;

    @Mock
    private ProcessInstance processInstance;
    @Mock
    private DelegateExecution delegateExecution;
    @Mock
    private User user;

    private List<ScheduleTaskEntity> scheduleTaskEntities;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(connectionConfig.getId()).thenReturn(1L);
        when(onlineSchemaChangeParameters.generateSubTaskParameters(any(ConnectionConfig.class), anyString()))
                .thenReturn(new ArrayList<>());

        when(organizationService.getUserById(anyLong())).thenReturn(user);
        when(user.getId()).thenReturn(1L);
        when(user.getOrganizationId()).thenReturn(1L);

        scheduleTaskEntities = new ArrayList<>();
    }

    @Test
    public void testStart() throws Exception {
        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(new ArrayList<>());

        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(mockPage(new ArrayList<>()));
        when(taskService.start(anyLong())).thenReturnVoid();
        when(taskService.detail(anyLong())).thenReturn(new TaskEntity());
        when(quartzJobService.scheduleJob(anyString(), anyString(), anyLong(), anyString(), anyString(), anyString(),
                any(TriggerConfig.class), anyLong())).thenReturn(1L);
        when(taskHandler.start(anyLong(), anyLong())).thenReturn(true);
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());

        onlineSchemaChangeFlowableTask.start(1L, taskService, delegateExecution);
        verify(taskService, times(1)).start(1L);
        verify(taskHandler, times(1)).start(anyLong(), anyLong());
        verifyNoMoreInteractions(taskService, taskHandler);

        verify(quartzJobService, times(1)).scheduleJob(eq("OnlineSchemaChangeFlowableTask"), eq("OnlineSchemaChangeFlowableTask"),
                eq(1L), anyString(), anyString(), eq(null), any(TriggerConfig.class), eq(1L));
        verify(scheduleTaskRepository, times(1)).saveAndFlush(any(ScheduleTaskEntity.class));

        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
    }

    @Test
    public void testIsSuccessful() {
        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(new ArrayList<>());
        when(onlineSchemaChangeFlowableTask.getStatus()).thenReturn(TaskStatus.DONE);
        assertEquals(true, onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(new ArrayList<>());
        when(onlineSchemaChangeFlowableTask.getStatus()).thenReturn(TaskStatus.FAILED);
        assertEquals(true, onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() throws Exception {
        when(taskService.cancel(anyLong(), anyBoolean())).thenReturn(true);
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
        verify(taskService, times(1)).cancel(1L, true);
    }

    @Test
    public void testOnSuccessful() throws Exception {
        TaskResult result = new TaskResult();
        result.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeTaskResult(new ArrayList<>())));
        when(taskService.detail(anyLong())).thenReturn(new TaskEntity());

        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
        verify(taskService, times(1)).update(any(TaskEntity.class));
        verifyNoMoreInteractions(taskService);

        assertEquals(TaskStatus.DONE, onlineSchemaChangeFlowableTask.getStatus());
        verify(taskService, times(1)).update(argThat(arg -> ((TaskEntity) arg).getStatus().equals(TaskStatus.DONE)));
    }

    @Test
    public void testOnTimeout() throws Exception {
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
        verifyNoInteractions(taskService);
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        TaskStatusUpdateResponse response = new TaskStatusUpdateResponse();
        response.setStatus(TaskStatus.DONE);
        response.setProgressPercentage(100);

        when(taskService.update(any(TaskEntity.class))).thenReturn(response);
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult(new ArrayList<>());
        onlineSchemaChangeTaskResult.setTasks(scheduleTaskEntities);
        when(onlineSchemaChangeFlowableTask.getStatus()).thenReturn(TaskStatus.RUNNING);
        when(onlineSchemaChangeFlowableTask.getPercentage()).thenReturn(50.0);
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(mockPage(scheduleTaskEntities));

        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
        verify(taskService, times(1)).update(any(TaskEntity.class));
        verifyNoMoreInteractions(taskService);

        assertEquals(TaskStatus.DONE, onlineSchemaChangeFlowableTask.getStatus());
        verify(taskService, times(1)).update(argThat(arg -> {
            TaskStatus status = ((TaskEntity) arg).getStatus();
            TaskResult result = ((TaskEntity) arg).getResult();
            assertEquals(TaskStatus.DONE, status);
            assertEquals(onlineSchemaChangeTaskResult, result);
        }));
    }

    @Test
    public void testCancel() {
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(mockPage(scheduleTaskEntities));
        when(taskHandler.terminate(anyLong(), anyLong())).thenReturn(true);
        when(taskService.cancel(anyLong(), anyBoolean())).thenReturn(true);
        onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);
        verify(taskService, times(1)).cancel(1L, true);
        verify(taskHandler, times(1)).terminate(anyLong(), anyLong());
        verifyNoMoreInteractions(taskService, taskHandler);

        assertEquals(TaskStatus.CANCELED, onlineSchemaChangeFlowableTask.getStatus());
    }

    @Test
    public void testIsCancelled() {
        when(onlineSchemaChangeFlowableTask.getStatus()).thenReturn(TaskStatus.CANCELED);
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test(expected = Exception.class)
    public void testCreateScheduleEntityWithNullConnectionConfig() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        String schema = "schemaName";

        assertThrows(Exception.class, () -> onlineSchemaChangeFlowableTask.createScheduleEntity(null, onlineSchemaChangeParameters, schema));
    }

    @Test(expected = Exception.class)
    public void testCreateScheduleEntityWithNullParameter() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "schemaName";

        assertThrows(Exception.class, () -> onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, null, schema));
    }

    @Test(expected = Exception.class)
    public void testCreateScheduleEntityWithNullSchema() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();

        assertThrows(Exception.class, () -> onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, null));
    }

    @Test
    public void testCreateScheduleEntity() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        String schema = "schemaName";

        ScheduleEntity result = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schema);
        verify(scheduleService, times(1)).create(result);
        verifyNoMoreInteractions(scheduleService);

        assertEquals(1L, result.getId());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, result.getJobType());
        assertEquals(SchemaStatus.ENABLED, result.getStatus());
        assertEquals(1L, result.getCreatorId());
        assertEquals(1L, result.getOrganizationId());
        assertEquals(1L, result.getProjectId());
        assertEquals(1L, result.getDatabaseId());
        assertEquals("OnlineSchemaChangeFlowableTask", result.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), result.getJobGroup());
        assertEquals(0.0, result.getProgressPercentage());
        assertEquals(JsonUtils.toJson(onlineSchemaChangeParameters), result.getJobParametersJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, result.getMisfireStrategy());
        assertEquals(TriggerStrategy.CRON, result.getTriggerStrategy());
        assertEquals(checkTaskCronExpression, result.getTriggerConfigJson());
    }

    @Test(expected = Exception.class)
    public void testCreateScheduleTaskEntityWithNullParam() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = null;

        assertThrows(Exception.class, () -> onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, onlineSchemaChangeScheduleTaskParameters));
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();

        ScheduleTaskEntity result = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, onlineSchemaChangeScheduleTaskParameters);
        verify(scheduleTaskRepository, times(1)).saveAndFlush(result);
        verifyNoMoreInteractions(scheduleTaskRepository);

        assertEquals(1L, result.getId());
        assertEquals(TaskStatus.PREPARING, result.getStatus());
        assertEquals("OnlineSchemaChangeFlowableTask", result.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), result.getJobGroup());
        assertEquals(0.0, result.getProgressPercentage());
        assertEquals(JsonUtils.toJson(onlineSchemaChangeScheduleTaskParameters), result.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(onlineSchemaChangeScheduleTaskParameters)), result.getResultJson());
        assertEquals(new Date(), result.getFireTime());
    }

    private Page<ScheduleTaskEntity> mockPage(List<ScheduleTaskEntity> list) {
        return new PageImpl<>(list);
    }
}