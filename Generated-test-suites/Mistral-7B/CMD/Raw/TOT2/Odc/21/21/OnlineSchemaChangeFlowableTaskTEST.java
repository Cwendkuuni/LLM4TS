import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.RandomUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleEntityTestBuilder;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntityTestBuilder;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskEntityTestBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    private BaseODCFlowTaskDelegate<Void> onlineSchemaChangeFlowableTask;
    private OrganizationService organizationService;
    private ConnectionConfig connectionConfig;
    private User user;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private ScheduleTaskService scheduleTaskService;
    private QuartzJobService quartzJobService;
    private TaskService taskService;
    private ScheduleService scheduleService;
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @Before
    public void setup() {
        // Initialize mocks
        organizationService = Mockito.mock(OrganizationService.class);
        connectionConfig = new ConnectionConfig();
        user = new User();
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        scheduleTaskService = Mockito.mock(ScheduleTaskService.class);
        quartzJobService = Mockito.mock(QuartzJobService.class);
        taskService = Mockito.mock(TaskService.class);
        scheduleService = Mockito.mock(ScheduleService.class);
        oscTaskCompleteHandler = Mockito.mock(OscTaskCompleteHandler.class);

        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask() {
            {
                this.organizationService = organizationService;
                this.connectionConfig = connectionConfig;
                this.onlineSchemaChangeParameters = onlineSchemaChangeParameters;
                this.scheduleTaskService = scheduleTaskService;
                this.quartzJobService = quartzJobService;
                this.taskService = taskService;
                this.scheduleService = scheduleService;
                this.oscTaskCompleteHandler = oscTaskCompleteHandler;
            }
        };
    }

    @Test
    public void testStart() {
        // Given
        DelegateExecution delegateExecution = Mockito.mock(DelegateExecution.class);
        Mockito.when(delegateExecution.getVariables()).thenReturn(Mockito.mapOf("flowInstanceId", 1L));
        Long taskId = 1L;
        Mockito.when(taskService.start(taskId)).thenReturn(true);

        // When
        onlineSchemaChangeFlowableTask.start(taskId, taskService, delegateExecution);

        // Then
        Mockito.verify(taskService, Mockito.times(1)).start(taskId);
        ExecutionEntity executionEntity = Mockito.mock(ExecutionEntity.class);
        Mockito.when(delegateExecution.getExecution()).thenReturn(executionEntity);
        Mockito.verify(executionEntity, Mockito.times(1)).setStatus(TaskStatus.RUNNING.name());
    }

    @Test
    public void testIsSuccessful() {
        // Given
        TaskEntity taskEntity = TaskEntityTestBuilder.builder()
                .status(TaskStatus.DONE)
                .build();

        // When
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful(taskEntity.getId(), taskService);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsFailure() {
        // Given
        TaskEntity taskEntity = TaskEntityTestBuilder.builder()
                .status(TaskStatus.FAILED)
                .build();

        // When
        boolean result = onlineSchemaChangeFlowableTask.isFailure(taskEntity.getId(), taskService);

        // Then
        assertTrue(result);
    }

    @Test
    public void testOnFailure() {
        // Given
        Long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        // Then
        Mockito.verify(taskService, Mockito.times(1)).cancel(taskId);
    }

    @Test
    public void testOnSuccessful() {
        // Given
        Long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        // Then
        Mockito.verify(taskService, Mockito.times(1)).update(Mockito.any(TaskEntity.class));
    }

    @Test
    public void testOnTimeout() {
        // Given
        Long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        // Then
        // No verification is needed as onTimeout does not interact with any mocks
    }

    @Test
    public void testProgressUpdate() {
        // Given
        Page<ScheduleTaskEntity> scheduleTasks = Pageable.unpaged(ListUtils.emptyIfNull(ListUtils.addingAll(
                ScheduleTaskEntityTestBuilder.builder()
                        .status(TaskStatus.DONE)
                        .build(),
                ScheduleTaskEntityTestBuilder.builder()
                        .status(TaskStatus.FAILED)
                        .build())));

        // When
        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

        // Then
        TaskEntity taskEntity = Mockito.mock(TaskEntity.class);
        Mockito.when(taskService.detail(taskId)).thenReturn(taskEntity);
        Mockito.when(taskEntity.getStatus()).thenReturn(TaskStatus.FAILED);
        double expectedPercentage = 50.0;
        Mockito.when(scheduleTasks.getTotalElements()).thenReturn(2);
        Mockito.when(scheduleTasks.getSize()).thenReturn(2);
        Double result = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTasks.get(0));
        assertEquals(expectedPercentage, result.doubleValue(), 0.01);
        Mockito.verify(taskEntity, Mockito.times(1)).setResultJson(Mockito.anyString());
        Mockito.verify(taskEntity, Mockito.times(1)).setProgressPercentage(expectedPercentage);
        Mockito.verify(taskEntity, Mockito.times(1)).setStatus(TaskStatus.DONE.name());
        Mockito.verify(taskService, Mockito.times(1)).update(taskEntity);
    }

    @Test
    public void testCancel() {
        // Given
        boolean mayInterruptIfRunning = false;
        Long taskId = 1L;

        // When
        boolean result = onlineSchemaChangeFlowableTask.cancel(mayInterruptIfRunning, taskId, taskService);

        // Then
        assertTrue(result);
        Mockito.verify(taskService, Mockito.times(1)).cancel(taskId);
    }

    @Test
    public void testIsCancelled() {
        // Given
        TaskEntity taskEntity = TaskEntityTestBuilder.builder()
                .status(TaskStatus.CANCELED)
                .build();

        // When
        boolean result = onlineSchemaChangeFlowableTask.isCancelled(taskEntity.getId(), taskService);

        // Then
        assertTrue(result);
    }

    @Test
    public void testCreateScheduleEntity() {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        ScheduleEntity expectedScheduleEntity = ScheduleEntityTestBuilder.builder()
                .connectionId(connectionConfig.id())
                .databaseName(schema)
                .jobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE)
                .status(ScheduleStatus.ENABLED)
                .build();

        // When
        ScheduleEntity actualScheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig,
                onlineSchemaChangeParameters, schema);

        // Then
        assertEquals(expectedScheduleEntity, actualScheduleEntity);
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        // Given
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();

        // When
        ScheduleTaskEntity actualScheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId,
                onlineSchemaChangeScheduleTaskParameters);

        // Then
        assertNotNull(actualScheduleTaskEntity);
        Mockito.verify(scheduleTaskService, Mockito.times(1)).saveAndFlush(actualScheduleTaskEntity);
    }
}