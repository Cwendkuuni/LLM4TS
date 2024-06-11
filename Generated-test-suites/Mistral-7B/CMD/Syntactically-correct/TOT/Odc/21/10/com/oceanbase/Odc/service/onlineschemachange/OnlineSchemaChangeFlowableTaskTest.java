package com.oceanbase.odc.service.onlineschemachange;

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
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleExecution;
import com.oceanbase.odc.service.task.TaskService;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private OrganizationService organizationService;
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
    private OscTaskCompleteHandler taskHandler;
    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStartTaskWithSuccess() {
        // given
        User creator = new User();
        creator.setId(1L);
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        String schema = "test_schema";
        when(organizationService.getCreator(anyLong())).thenReturn(creator);
        when(scheduleService.create(any()))
                .thenAnswer((Answer<ScheduleEntity>) invocation -> createScheduleEntity(connectionConfig, parameters, schema));
        when(scheduleTaskService.listTask(any(), any()))
                .thenReturn(createListScheduleTaskEntities(status -> status == TaskStatus.RUNNING));
        when(taskHandler.start(anyLong(), anyLong()))
                .thenReturn(true);
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getVariable("onlineSchemaChangeParameters")).thenReturn(parameters);
        when(execution.getVariable("connectionConfig")).thenReturn(connectionConfig);
        when(execution.getVariable("schemaName")).thenReturn(schema);

        // when
        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        // then
        verify(taskService, times(1)).start(1L);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    public void testStartTaskWithError() {
        // given
        User creator = new User();
        creator.setId(1L);
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        String schema = "test_schema";
        when(organizationService.getCreator(anyLong())).thenReturn(creator);
        when(scheduleService.create(any()))
                .thenAnswer((Answer<ScheduleEntity>) invocation -> createScheduleEntity(connectionConfig, parameters, schema));
        when(scheduleTaskService.listTask(any(), any()))
                .thenReturn(createListScheduleTaskEntities(status -> status == TaskStatus.RUNNING));
        when(taskHandler.start(anyLong(), anyLong()))
                .thenReturn(false);
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getVariable("onlineSchemaChangeParameters")).thenReturn(parameters);
        when(execution.getVariable("connectionConfig")).thenReturn(connectionConfig);
        when(execution.getVariable("schemaName")).thenReturn(schema);

        // when, then
        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);
        verify(taskService, times(1)).cancel(1L, eq(true));
        verifyNoMoreInteractions(taskService);
    }

    @Test
    public void testIsSuccessful() {
        // given
        TaskEntity task = new TaskEntity();
        task.setStatus(TaskStatus.DONE);

        // when
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void testIsFailure() {
        // given
        TaskEntity task = new TaskEntity();
        task.setStatus(TaskStatus.FAILED);

        // when
        boolean result = onlineSchemaChangeFlowableTask.isFailure();

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void testOnSuccessful() {
        // given
        Long taskId = 1L;

        // when
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        // then
        verify(taskService, times(1)).update(any());
        verifyNoMoreInteractions(taskService);
    }

    @Test
    public void testOnFailure() {
        // given
        Long taskId = 1L;

        // when
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        // then
        verify(taskService, times(1)).update(any());
        verifyNoMoreInteractions(taskService);
    }

    @Test
    public void testCancel() {
        // given
        boolean mayInterruptIfRunning = true;
        Long taskId = 1L;

        // when
        boolean result = onlineSchemaChangeFlowableTask.cancel(mayInterruptIfRunning, taskId, taskService);

        // then
        verify(taskService, times(1)).cancel(taskId, eq(true));
        verifyNoMoreInteractions(taskService);
    }

    @Test
    public void testIsCancelled() {
        // given
        TaskEntity task = new TaskEntity();
        task.setStatus(TaskStatus.CANCELED);

        // when
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        // then
        assertThat(result).isTrue();
    }

    private ScheduleEntity createScheduleEntity(ConnectionConfig connectionConfig,
                                               OnlineSchemaChangeParameters parameter, String schema) {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setConnectionId(connectionConfig.getId());
        scheduleEntity.setDatabaseName(schema);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setStatus(ScheduleStatus.ENABLED);
        scheduleEntity.setAllowConcurrent(false);
        scheduleEntity.setCreatorId(creator.getId());
        scheduleEntity.setOrganizationId(creator.getOrganizationId());
        scheduleEntity.setProjectId(1L);
        scheduleEntity.setDatabaseId(1L);
        scheduleEntity.setModifierId(scheduleEntity.getCreatorId());
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
        triggerConfig.setCronExpression(checkTaskCronExpression);
        scheduleEntity.setTriggerConfigJson(JsonUtils.toJson(triggerConfig));
        scheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(parameter));
        return scheduleService.create(scheduleEntity);
    }

    private List<ScheduleTaskEntity> createListScheduleTaskEntities(Predicate<TaskStatus> statusPredicate) {
        return List.of(new ScheduleTaskEntity(), new ScheduleTaskEntity(), new ScheduleTaskEntity());
    }

    @SuppressWarnings("unchecked")
    private Predicate<TaskStatus> anyStatus() {
        return (TaskStatus status) -> true;
    }

    private Predicate<TaskStatus> status(TaskStatus expectedStatus) {
        return (TaskStatus status) -> status.equals(expectedStatus);
    }

    private class Predicate<T> {
        private T test;

        public Predicate(T test) {
            this.test = test;
        }

        public boolean test(T t) {
            return test.equals(t);
        }

        public Predicate<T> and(Predicate<? super T> other) {
            return (T t) -> test.equals(t) && other.test(t);
        }

        public Predicate<T> negate() {
            return (T t) -> !test.equals(t);
        }

        public Predicate<T> or(Predicate<? super T> other) {
            return (T t) -> test.equals(t) || other.test(t);
        }
    }
}