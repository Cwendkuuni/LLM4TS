package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.FlowStatusReason;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.flow.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.JobType;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.TriggerType;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskInstance;

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
    private OscTaskCompleteHandler taskHandler;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);
        String schema = "testSchema";
        String uid = "testUid";

        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);

        assertEquals(connectionConfig.id(), scheduleEntity.getConnectionId());
        assertEquals(schema, scheduleEntity.getDatabaseName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(false, scheduleEntity.isAllowConcurrent());
        assertEquals(creatorId, scheduleEntity.getCreatorId());
        assertEquals(organizationId, scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals(creatorId, scheduleEntity.getModifierId());
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
        triggerConfig.setCronExpression(checkTaskCronExpression);
        assertEquals(JsonUtils.toJson(triggerConfig), scheduleEntity.getTriggerConfigJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, scheduleEntity.getMisfireStrategy());
        assertEquals(JsonUtils.toJson(parameter), scheduleEntity.getJobParametersJson());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = mock(OnlineSchemaChangeScheduleTaskParameters.class);

        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals(scheduleId.toString(), scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage(), 0.0);
        assertEquals(JsonUtils.toJson(param), scheduleTaskEntity.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(param)), scheduleTaskEntity.getResultJson());
        assertNull(scheduleTaskEntity.getFireTime());
    }

    @Test
    public void testIsSuccessful() {
        TaskStatus status = TaskStatus.DONE;
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());

        status = TaskStatus.FAILED;
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());

        status = TaskStatus.RUNNING;
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        TaskStatus status = TaskStatus.DONE;
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());

        status = TaskStatus.FAILED;
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());

        status = TaskStatus.RUNNING;
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testIsCancelled() {
        TaskStatus status = TaskStatus.DONE;
        assertFalse(onlineSchemaChangeFlowableTask.isCancelled());

        status = TaskStatus.CANCELED;
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testOnFailure() {
        Long taskId = 1L;
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(this.onlineSchemaChangeFlowableTask);

        doThrow(new RuntimeException("Test failure"))
                .when(onlineSchemaChangeFlowableTask)
                .onFailure(taskId, taskService);

        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        verify(taskService).update(argThat(t -> t.getId().equals(taskId) && t.getStatus().equals(FlowStatus.EXECUTION_FAILED) && t.getErrorMessage().equals("Test failure")));
    }

    @Test
    public void testOnSuccessful() {
        Long taskId = 1L;
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(this.onlineSchemaChangeFlowableTask);

        doCallRealMethod().when(onlineSchemaChangeFlowableTask).onSuccessful(taskId, taskService);

        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        verify(taskService, times(1)).update(argThat(t -> t.getId().equals(taskId) && t.getStatus().equals(FlowStatus.EXECUTION_SUCCEEDED)));
    }

    @Test
    public void testOnTimeout() {
        Long taskId = 1L;
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(this.onlineSchemaChangeFlowableTask);

        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        verify(taskService, times(1)).update(argThat(t -> t.getId().equals(taskId) && t.getStatus().equals(FlowStatus.EXECUTION_TIMED_OUT)));
    }

    @Test
    public void testOnProgressUpdate() {
        Long taskId = 1L;
        List<ScheduleTaskEntity> tasks = createMockTasks();
        Page<ScheduleTaskEntity> page = new PageImpl<>(tasks);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(this.onlineSchemaChangeFlowableTask);

        doCallRealMethod().when(onlineSchemaChangeFlowableTask).progressStatusUpdate(page);

        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

        verify(taskService, times(1)).update(argThat(t -> t.getId().equals(taskId) && t.getProgressPercentage() > 0 && t.getStatus().equals(TaskStatus.DONE)));
    }

    @Test
    public void testCancel() {
        Long taskId = 1L;
        boolean mayInterruptIfRunning = true;
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(this.onlineSchemaChangeFlowableTask);

        doReturn(true).when(onlineSchemaChangeFlowableTask).cancel(mayInterruptIfRunning, taskId, taskService);

        assertTrue(onlineSchemaChangeFlowableTask.cancel(mayInterruptIfRunning, taskId, taskService));

        verify(taskService, times(1)).cancel(taskId);
        verify(taskHandler, times(1)).terminate(scheduleId, scheduleTaskEntity.getId());
    }

    @Test
    public void testStart() throws Exception {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        ProcessInstance processInstance = mock(ProcessInstance.class);
        DelegateExecution execution = mock(DelegateExecution.class);

        when(taskService.start(taskId)).thenReturn(processInstance);
        when(processInstance.getId()).thenReturn(taskId);
        when(execution.getId()).thenReturn(taskId);
        when(execution.getVariable("onlineSchemaChangeParameters")).thenReturn(FlowTaskUtil.getOnlineSchemaChangeParameter(execution));
        when(execution.getVariable("connectionConfig")).thenReturn(FlowTaskUtil.getConnectionConfig(execution));

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(this.onlineSchemaChangeFlowableTask);

        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

        verify(taskService, times(1)).start(taskId);
        verify(taskService, times(1)).update(argThat(t -> t.getId().equals(taskId) && t.getStatus().equals(TaskStatus.RUNNING)));
        verify(taskService, times(1)).detail(taskId);
        verify(taskService, times(1)).save(argThat(t -> t.getId().equals(taskId) && t.getStatus().equals(TaskStatus.RUNNING) && t.getResultJson() == null && t.getProgressPercentage() == 0));
        verifyNoMoreInteractions(taskService);
        verify(taskHandler, times(1)).start(scheduleId, tasks.get(0).getId());
    }

    private List<ScheduleTaskEntity> createMockTasks() {
        OnlineSchemaChangeScheduleTaskParameters param1 = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        OnlineSchemaChangeScheduleTaskParameters param2 = mock(OnlineSchemaChangeScheduleTaskParameters.class);

        ScheduleTaskEntity scheduleTaskEntity1 = new ScheduleTaskEntity();
        scheduleTaskEntity1.setStatus(TaskStatus.RUNNING);
        scheduleTaskEntity1.setId(1L);
        scheduleTaskEntity1.setParametersJson(JsonUtils.toJson(param1));

        ScheduleTaskEntity scheduleTaskEntity2 = new ScheduleTaskEntity();
        scheduleTaskEntity2.setStatus(TaskStatus.FAILED);
        scheduleTaskEntity2.setId(2L);
        scheduleTaskEntity2.setParametersJson(JsonUtils.toJson(param2));

        return List.of(scheduleTaskEntity1, scheduleTaskEntity2);
    }
}