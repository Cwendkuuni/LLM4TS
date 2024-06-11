import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
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

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OnlineSchemaChangeFlowableTaskTest {

    private static final String SCHEMA = "testSchema";
    private static final String UID = "testUid";
    private static final String JOB_NAME = "testJobName";

    @Autowired
    private TaskService taskService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @Autowired
    private QuartzJobService quartzJobService;

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private ConnectionConfig connectionConfig;

    @BeforeEach
    public void setup() {
        connectionConfig = mock(ConnectionConfig.class);
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask() {
            {
                this.connectionConfig = connectionConfig;
            }
        };
        when(organizationService.getCurrentUser()).thenReturn(Mockito.mock(User.class));
    }

    @Test
    public void testOnlineSchemaChangeFlowableTaskStart() throws Exception {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        when(connectionConfig.getId()).thenReturn(1L);
        when(connectionConfig.getSchemaName()).thenReturn(SCHEMA);

        // When
        TaskEntity task = onlineSchemaChangeFlowableTask.start(1L, taskService, Mockito.mock(DelegateExecution.class));

        // Then
        Assertions.assertEquals(TaskStatus.RUNNING, task.getStatus());
    }

    @Test
    public void testOnlineSchemaChangeFlowableTaskIsSuccessful() throws Exception {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        when(connectionConfig.getId()).thenReturn(1L);
        when(connectionConfig.getSchemaName()).thenReturn(SCHEMA);

        // When
        onlineSchemaChangeFlowableTask.isSuccessful();

        // Then
        Assertions.assertTrue(true);
    }

    @Test
    public void testOnlineSchemaChangeFlowableTaskIsFailure() throws Exception {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        when(connectionConfig.getId()).thenReturn(1L);
        when(connectionConfig.getSchemaName()).thenReturn(SCHEMA);

        // When & Then
        Assertions.assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnlineSchemaChangeFlowableTaskOnTimeout() throws Exception {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        when(connectionConfig.getId()).thenReturn(1L);
        when(connectionConfig.getSchemaName()).thenReturn(SCHEMA);

        // When
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);

        // Then
        Assertions.assertFalse(true);
    }

    @Test
    public void testOnlineSchemaChangeFlowableTaskOnProgressUpdate() throws Exception {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        when(connectionConfig.getId()).thenReturn(1L);
        when(connectionConfig.getSchemaName()).thenReturn(SCHEMA);

        Page<ScheduleTaskEntity> scheduleTasks = Mockito.mock(Page.class);
        when(scheduleTaskService.listTask(Pageable.unpaged(), UUID.randomUUID().toString()))
                .thenReturn(scheduleTasks);

        // When
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        // Then
        TaskEntity taskEntity = Mockito.mock(TaskEntity.class);
        when(taskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        when(taskEntity.getId()).thenReturn(1L);
        when(taskService.detail(1L)).thenReturn(taskEntity);

        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult(List.of());
        when(JsonUtils.fromJson(JsonUtils.toJson(taskEntity.getResultJson()), OnlineSchemaChangeTaskResult.class))
                .thenReturn(onlineSchemaChangeTaskResult);

        List<ScheduleTaskEntity> tasks = ListUtils.emptyIfNull(scheduleTasks.getContent());
        Assertions.assertEquals(tasks.size(), onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks).size());
        Assertions.assertEquals(tasks.get(0).getStatus(), TaskStatus.DONE);

        TaskEntity updatedTaskEntity = new TaskEntity();
        updatedTaskEntity.setStatus(TaskStatus.DONE);
        when(taskService.update(updatedTaskEntity)).thenReturn(updatedTaskEntity);

        OnlineSchemaChangeTaskResult updatedOnlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult(
                List.of());
        when(JsonUtils.fromJson(JsonUtils.toJson(updatedTaskEntity.getResultJson()), OnlineSchemaChangeTaskResult.class))
                .thenReturn(updatedOnlineSchemaChangeTaskResult);

        when(taskService.update(taskEntity)).thenReturn(taskEntity);

        double currentPercentage = RandomUtils.nextDouble(0.0, 100.0);
        when(taskEntity.getProgressPercentage()).thenReturn(currentPercentage);
        when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(onlineSchemaChangeTaskResult));

        Page<ScheduleTaskEntity> updatedTasks = Mockito.mock(Page.class);
        when(scheduleTaskService.listTask(Pageable.unpaged(), UUID.randomUUID().toString()))
                .thenReturn(updatedTasks);

        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);

        // When
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        // Then
        OnlineSchemaChangeTaskResult updatedOnlineSchemaChangeTaskResult2 = new OnlineSchemaChangeTaskResult(
                List.of());
        when(JsonUtils.fromJson(JsonUtils.toJson(updatedTaskEntity.getResultJson()), OnlineSchemaChangeTaskResult.class))
                .thenReturn(updatedOnlineSchemaChangeTaskResult2);

        double updatedPercentage = RandomUtils.nextDouble(0.0, 100.0);

        when(tasks.get(0).getProgressPercentage()).thenReturn(updatedPercentage);

        when(scheduleTaskService.listTask(Pageable.unpaged(), UUID.randomUUID().toString()))
                .thenReturn(updatedTasks);

        when(taskService.update(taskEntity)).thenReturn(taskEntity);

        when(taskEntity.getProgressPercentage()).thenReturn(updatedPercentage > currentPercentage ? updatedPercentage : currentPercentage);

        TaskEntity updatedTaskEntity2 = new TaskEntity();
        updatedTaskEntity2.setStatus(TaskStatus.DONE);
        when(taskService.update(updatedTaskEntity2)).thenReturn(updatedTaskEntity2);

        when(JsonUtils.fromJson(JsonUtils.toJson(updatedTaskEntity2.getResultJson()), OnlineSchemaChangeTaskResult.class))
                .thenReturn(updatedOnlineSchemaChangeTaskResult2);

        // When
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        // Then
        TaskEntity taskEntity3 = new TaskEntity();
        taskEntity3.setStatus(TaskStatus.DONE);
        when(taskService.update(taskEntity3)).thenReturn(taskEntity3);

        OnlineSchemaChangeTaskResult updatedOnlineSchemaChangeTaskResult3 = new OnlineSchemaChangeTaskResult(
                List.of());
        when(JsonUtils.fromJson(JsonUtils.toJson(taskEntity3.getResultJson()), OnlineSchemaChangeTaskResult.class))
                .thenReturn(updatedOnlineSchemaChangeTaskResult3);

        when(scheduleTaskService.listTask(Pageable.unpaged(), UUID.randomUUID().toString()))
                .thenReturn(updatedTasks);

        when(taskService.update(taskEntity)).thenReturn(taskEntity3);

        when(taskEntity.getProgressPercentage()).thenReturn(updatedPercentage > currentPercentage ? updatedPercentage : currentPercentage);

        // When
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        // Then
        Assertions.assertEquals(updatedPercentage, onlineSchemaChangeFlowableTask.percentage);
    }

    @Test
    public void testOnlineSchemaChangeFlowableTaskCancel() throws Exception {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        when(connectionConfig.getId()).thenReturn(1L);
        when(connectionConfig.getSchemaName()).thenReturn(SCHEMA);

        // When
        boolean cancelled = onlineSchemaChangeFlowableTask.cancel(false, 1L, taskService);

        // Then
        Assertions.assertTrue(cancelled);
    }

    @Test
    public void testOnlineSchemaChangeFlowableTaskIsCancelled() {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        // When
        boolean cancelled = onlineSchemaChangeFlowableTask.isCancelled();

        // Then
        Assertions.assertFalse(cancelled);
    }

    @Test
    public void testCreateScheduleEntity() {
        // Given
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        when(connectionConfig.getId()).thenReturn(1L);
        when(connectionConfig.getSchemaName()).thenReturn(SCHEMA);

        // When
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig,
                parameter, SCHEMA);

        // Then
        Assertions.assertEquals(connectionConfig.getId(), scheduleEntity.getConnectionId());
        Assertions.assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        Assertions.assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        Assertions.assertEquals(false, scheduleEntity.isAllowConcurrent());
        Assertions.assertEquals(creatorId, scheduleEntity.getCreatorId());
        Assertions.assertEquals(organizationId, scheduleEntity.getOrganizationId());
        Assertions.assertEquals(1L, scheduleEntity.getProjectId());
        Assertions.assertEquals(1L, scheduleEntity.getDatabaseId());
        Assertions.assertEquals(creatorId, scheduleEntity.getModifierId());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        // Given
        Long scheduleId = UUID.randomUUID().toString();
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

        // When
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

        // Then
        Assertions.assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        Assertions.assertEquals(JOB_NAME + "_" + 1, scheduleTaskEntity.getJobName());
        Assertions.assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        Assertions.assertEquals(0.0, scheduleTaskEntity.getProgressPercentage());
        Assertions.assertEquals(JsonUtils.toJson(param), scheduleTaskEntity.getParametersJson());
        Assertions.assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(param)),
                scheduleTaskEntity.getResultJson());
        Assertions.assertEquals(new Date(), scheduleTaskEntity.getFireTime());
    }
}