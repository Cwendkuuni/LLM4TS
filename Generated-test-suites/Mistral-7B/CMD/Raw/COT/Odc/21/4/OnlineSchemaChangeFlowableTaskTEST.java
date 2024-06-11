package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.context.FlowableEngineContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity.Status;
import com.oceanbase.odc.service.task.TaskService;

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
    private FlowableEngineContext engineContext;

    @Mock
    private TaskService taskService;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Mock
    private OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult;

    @Mock
    private OnlineSchemaChangeScheduleTaskResult onlineSchemaChangeScheduleTaskResult;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private User user;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        OnlineSchemaChangeContextHolder.setUp();
    }

    @Test
    public void testStart() throws Exception {
        // given
        Mockito.when(engineContext.getTaskService()).thenReturn(taskService);
        Mockito.when(taskService.start(Long.class)).thenReturn(1L);
        Mockito.when(organizationService.getById(Long.class)).thenReturn(user);
        Mockito.when(FlowTaskUtil.getOnlineSchemaChangeParameter(delegateExecution)).thenReturn(onlineSchemaChangeParameters);
        Mockito.when(FlowTaskUtil.getConnectionConfig(delegateExecution)).thenReturn(connectionConfig);
        Mockito.when(FlowTaskUtil.getSchemaName(delegateExecution)).thenReturn("schemaName");

        // when
        onlineSchemaChangeFlowableTask.start(1L, taskService, delegateExecution);

        // then
        ArgumentCaptor<Long> taskIdCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(taskService).start(taskIdCaptor.capture());
        assertEquals(1L, taskIdCaptor.getValue());
    }

    @Test
    public void testIsSuccessful() {
        // given
        TaskStatus status = TaskStatus.DONE;

        // when
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

        // then
        assertEquals(true, result);
    }

    @Test
    public void testIsFailure() {
        // given
        TaskStatus status = TaskStatus.FAILED;

        // when
        boolean result = onlineSchemaChangeFlowableTask.isFailure();

        // then
        assertEquals(true, result);
    }

    @Test
    public void testOnFailure() throws Exception {
        // given
        Long taskId = 1L;
        Mockito.when(taskService.detail(taskId)).thenThrow(new RuntimeException("Test error"));

        // when
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        // then
        Mockito.verify(taskService).update(argThat(new IsTaskWithStatus(TaskStatus.FAILED, taskId)));
    }

    @Test
    public void testOnSuccessful() throws Exception {
        // given
        Long taskId = 1L;
        Mockito.when(taskService.detail(taskId)).thenReturn(new TaskEntity());
        Mockito.when(taskService.update(argThat(new IsTaskWithStatus(TaskStatus.EXECUTION_SUCCEEDED, taskId)))).thenReturn(1L);

        // when
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        // then
        Mockito.verify(taskService).update(argThat(new IsTaskWithStatus(TaskStatus.EXECUTION_SUCCEEDED, taskId)));
    }

    @Test
    public void testOnTimeout() throws Exception {
        // given
        Long taskId = 1L;

        // when
        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        // then
        Mockito.verifyZeroInteractions(taskService);
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        // given
        Long taskId = 1L;
        List<ScheduleTaskEntity> scheduleTasks = createScheduleTasks();
        Page<ScheduleTaskEntity> page = new PageImpl<>(scheduleTasks);
        Pageable pageable = PageRequest.unpaged();
        Mockito.when(scheduleTaskService.listTask(pageable, eq(scheduleId))).thenReturn(page);

        // when
        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

        // then
        for (ScheduleTaskEntity task : scheduleTasks) {
            verify(taskService).update(argThat(new IsTaskWithStatus(Status.DONE, task.getId())));
        }
    }

    @Test
    public void testCancel() throws Exception {
        // given
        Long taskId = 1L;
        boolean result = onlineSchemaChangeFlowableTask.cancel(true, taskId, taskService);

        // then
        Mockito.verify(taskService).cancel(taskId);
        Mockito.verify(taskService).update(argThat(new IsTaskWithStatus(TaskStatus.CANCELED, taskId)));
        assertEquals(true, result);
    }

    @Test
    public void testIsCancelled() {
        // given
        TaskStatus status = TaskStatus.CANCELED;

        // when
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        // then
        assertEquals(true, result);
    }

    @Test
    public void testCreateScheduleEntity() throws Exception {
        // given
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = mock(OnlineSchemaChangeParameters.class);
        String schema = "schemaName";
        TaskStatus status = TaskStatus.ENABLED;
        ScheduleStatus scheduleStatus = ScheduleStatus.ENABLED;
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schema);

        // then
        assertEquals(connectionConfig.getId(), scheduleEntity.getConnectionId());
        assertEquals(schema, scheduleEntity.getDatabaseName());
        assertEquals(TaskStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(scheduleStatus, scheduleEntity.getStatus());
        assertEquals(Long.class, scheduleEntity.getClass());
    }

    @Test
    public void testCreateScheduleTaskEntity() throws Exception {
        // given
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        Status status = Status.PREPARING;

        // when
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

        // then
        assertEquals(status, scheduleTaskEntity.getStatus());
        assertEquals(Long.class, scheduleTaskEntity.getClass());
    }

    private List<ScheduleTaskEntity> createScheduleTasks() {
        return Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity(), new ScheduleTaskEntity());
    }
}