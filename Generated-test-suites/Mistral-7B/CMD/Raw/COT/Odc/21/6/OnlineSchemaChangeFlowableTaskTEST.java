package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
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
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
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
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult;

@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private QuartzJobService quartzJobService;

    @Mock
    private TaskService taskService;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private User user;

    @Mock
    private TaskEntity taskEntity;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        // Given
        when(connectionConfig.id()).thenReturn(1L);
        when(onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema")).thenReturn(
                ListUtils.emptyList());

        // When
        OnlineSchemaChangeContextHolder.clear();
        long taskId = 1L;
        when(taskService.start(taskId)).thenReturn(null);
        User creator = new User();
        creator.setId(1L);
        creator.setOrganizationId(1L);
        when(FlowTaskUtil.getTaskCreator(delegateExecution)).thenReturn(creator);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(delegateExecution)).thenReturn(onlineSchemaChangeParameters);
        when(FlowTaskUtil.getConnectionConfig(delegateExecution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(delegateExecution)).thenReturn("schema");
        when(organizationService.getUserById(1L)).thenReturn(creator);
        OnlineSchemaChangeContextHolder.trace(1L, taskId, 1L);
        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setId(1L);
        scheduleEntity = schedule;
        when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);

        // Then
        OnlineSchemaChangeTaskResult result = onlineSchemaChangeFlowableTask.start(taskId, taskService, delegateExecution);
        assertNull(result);
        verify(taskService, times(1)).start(taskId);
        verify(organizationService, times(1)).getUserById(1L);
        verify(scheduleService, times(1)).create(scheduleEntity);
        verifyZeroInteractions(taskService, scheduleService, scheduleTaskRepository, quartzJobService,
                scheduleTaskService, oscTaskCompleteHandler);
    }

    @Test
    public void testIsSuccessful() {
        // Given
        TaskStatus taskStatus = TaskStatus.DONE;

        // When
        TaskStatus result = onlineSchemaChangeFlowableTask.isSuccessful();

        // Then
        assertEquals(taskStatus, result);
    }

    @Test
    public void testIsFailure() {
        // Given
        TaskStatus taskStatus = TaskStatus.FAILED;

        // When
        TaskStatus result = onlineSchemaChangeFlowableTask.isFailure();

        // Then
        assertEquals(taskStatus, result);
    }

    @Test
    public void testOnFailure() {
        // Given
        long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        // Then
        verify(taskService, times(1)).cancel(taskId);
    }

    @Test
    public void testOnSuccessful() {
        // Given
        long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        // Then
        verify(taskService, times(1)).update(taskEntity);
    }

    @Test
    public void testOnTimeout() {
        // Given
        long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        // Then
        verifyNoInteractions(taskService);
    }

    @Test
    public void testProgressUpdate() throws Exception {
        // Given
        Page<ScheduleTaskEntity> tasks = new PageImpl<ScheduleTaskEntity>(List.of(scheduleTaskEntity));
        when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(tasks);
        double percentage = 0.5;

        // When
        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);

        // Then
        verify(onlineSchemaChangeFlowableTask, times(1)).progressStatusUpdate(tasks);
        verify(onlineSchemaChangeFlowableTask, times(1)).singleTaskPercentage(scheduleTaskEntity);
        verify(taskService, times(1)).update(taskEntity);
        verifyZeroInteractions(scheduleTaskService);

        assertEquals(TaskStatus.DONE, onlineSchemaChangeFlowableTask.status);
    }

    @Test
    public void testSingleTaskPercentage() {
        // Given
        TaskStatus status = TaskStatus.RUNNING;

        // When
        double result = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity);

        // Then
        assertEquals(0.0, result, 0.001);
        scheduleTaskEntity.setStatus(TaskStatus.DONE);
        verify(onlineSchemaChangeFlowableTask, times(1)).singleTaskPercentage(scheduleTaskEntity);
        assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testCancel() {
        // Given
        boolean mayInterruptIfRunning = true;
        long taskId = 1L;

        // When
        boolean result = onlineSchemaChangeFlowableTask.cancel(mayInterruptIfRunning, taskId, taskService);

        // Then
        verify(taskService, times(1)).cancel(taskId);
        verify(taskHandler, times(1)).terminate(1L, scheduleTaskEntity.getId());
        verifyZeroInteractions(scheduleTaskRepository, quartzJobService, scheduleTaskService);
        assertTrue(result);
    }

    @Test
    public void testIsCancelled() {
        // Given
        TaskStatus taskStatus = TaskStatus.CANCELED;

        // When
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        // Then
        assertEquals(taskStatus, onlineSchemaChangeFlowableTask.status);
        assertEquals(taskStatus, result);
    }

    @Test
    public void testCreateScheduleEntity() {
        // Given
        when(connectionConfig.id()).thenReturn(1L);
        when(onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema"))
                .thenReturn(ListUtils.emptyList());

        // When
        ScheduleEntity result = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig,
                onlineSchemaChangeParameters, "schema");

        // Then
        assertEquals(ScheduleStatus.ENABLED, result.getStatus());
        assertEquals("schema", result.getDatabaseName());
        assertEquals(1L, result.getProjectId());
        assertEquals(1L, result.getDatabaseId());
        assertEquals(1L, result.getCreatorId());
        assertEquals(1L, result.getOrganizationId());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, result.getJobType());
        assertEquals(JsonUtils.toJson(onlineSchemaChangeParameters), result.getJobParametersJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, result.getMisfireStrategy());
        verifyZeroInteractions(scheduleService);
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        // Given
        when(scheduleRepository.saveAndFlush(scheduleTaskEntity)).thenReturn(scheduleTaskEntity);

        // When
        ScheduleTaskEntity result = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L,
                new OnlineSchemaChangeScheduleTaskParameters());

        // Then
        assertEquals(TaskStatus.PREPARING, result.getStatus());
        assertEquals("1", result.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), result.getJobGroup());
        assertEquals(0.0, result.getProgressPercentage(), 0.001);
        verifyZeroInteractions(scheduleService, scheduleTaskService, quartzJobService);
    }
}