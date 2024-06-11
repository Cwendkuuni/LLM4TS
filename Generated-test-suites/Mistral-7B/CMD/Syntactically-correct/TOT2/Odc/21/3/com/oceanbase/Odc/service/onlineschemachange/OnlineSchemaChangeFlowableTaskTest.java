import org.apache.commons.collections4.ListUtils;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.SpringTest;
import org.springframework.test.context.junit.jupiter.SpringTestMethodSource;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeTaskHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;

@SpringBootTest
@ActiveProfiles("test")
@SpringTest
@SpringTestMethodSource(OnlineSchemaChangeFlowableTaskTest.class)
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
    private OnlineSchemaChangeTaskHandler onlineSchemaChangeTaskHandler;
    @Autowired
    @Qualifier("onlineSchemaChangeFlowableTask")
    private BaseODCFlowTaskDelegate<Void> onlineSchemaChangeFlowableTask;
    @Autowired
    private OrganizationService organizationService;

    @BeforeAll
    public static void setupMocks() {
        MockitoAnnotations.initMocks(OnlineSchemaChangeFlowableTest.class);
    }

    @Test
    public void testStart() {
        // Given
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = Mockito.mock(OnlineSchemaChangeParameters.class);
        String schema = "testSchema";
        Mockito.when(connectionConfig.getId()).thenReturn(1L);
        Mockito.when(parameter.generateSubTaskParameters(connectionConfig, schema)).thenReturn(Collections.singletonList(Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class)));

        OnlineSchemaChangeFlowableTask testee = (OnlineSchemaChangeFlowableTask) onlineSchemaChangeFlowableTask;
        TestableOnlineSchemaChangeFlowableTask testableTestee = new TestableOnlineSchemaChangeFlowableTask(testee);

        // When
        testableTestee.start(1L, Mockito.mock(TaskService.class), Mockito.mock(DelegateExecution.class));

        // Then
        verify(quartzJobService).start(testableTestee.getScheduleId(), testableTestee.getTasks().get(0).getId());
        verify(testee).status = TaskStatus.RUNNING;
    }

    @Test
    public void testIsSuccessful() {
        // Given
        OnlineSchemaChangeFlowableTask testee = new OnlineSchemaChangeFlowableTask();
        testee.status = TaskStatus.DONE;

        // Then
        assertTrue(testee.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        // Given
        OnlineSchemaChangeFlowableTask testee = new OnlineSchemaChangeFlowableTask();
        testee.status = TaskStatus.FAILED;

        // Then
        assertTrue(testee.isFailure());
    }

    @Test
    public void testIsCancelled() {
        // Given
        OnlineSchemaChangeFlowableTask testee = new OnlineSchemaChangeFlowableTask();
        testee.status = TaskStatus.CANCELED;

        // Then
        assertTrue(testee.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        // Given
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = Mockito.mock(OnlineSchemaChangeParameters.class);
        String schema = "testSchema";

        ScheduleEntity expectedSchedule = new ScheduleEntity();
        expectedSchedule.setConnectionId(connectionConfig.getId());
        expectedSchedule.setDatabaseName(schema);
        expectedSchedule.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        expectedSchedule.setStatus(ScheduleStatus.ENABLED);
        expectedSchedule.setAllowConcurrent(false);
        expectedSchedule.setCreatorId(1L);
        expectedSchedule.setOrganizationId(1L);
        expectedSchedule.setProjectId(1L);
        expectedSchedule.setDatabaseId(1L);
        expectedSchedule.setModifierId(1L);

        ScheduleEntity actualSchedule = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);

        // Then
        assertEquals(expectedSchedule.getConnectionId(), actualSchedule.getConnectionId());
        assertEquals(expectedSchedule.getDatabaseName(), actualSchedule.getDatabaseName());
        assertEquals(expectedSchedule.getJobType(), actualSchedule.getJobType());
        assertEquals(expectedSchedule.getStatus(), actualSchedule.getStatus());
        assertEquals(expectedSchedule.getAllowConcurrent(), actualSchedule.getAllowConcurrent());
        assertEquals(expectedSchedule.getCreatorId(), actualSchedule.getCreatorId());
        assertEquals(expectedSchedule.getOrganizationId(), actualSchedule.getOrganizationId());
        assertEquals(expectedSchedule.getProjectId(), actualSchedule.getProjectId());
        assertEquals(expectedSchedule.getDatabaseId(), actualSchedule.getDatabaseId());
        assertEquals(expectedSchedule.getModifierId(), actualSchedule.getModifierId());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        // Given
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class);

        ScheduleTaskEntity expectedScheduleTask = new ScheduleTaskEntity();
        expectedScheduleTask.setStatus(TaskStatus.PREPARING);
        expectedScheduleTask.setJobName(scheduleId.toString());
        expectedScheduleTask.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        expectedScheduleTask.setProgressPercentage(0.0);
        expectedScheduleTask.setParametersJson(JsonUtils.toJson(param));
        expectedScheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(param)));
        expectedScheduleTask.setFireTime(new Date());

        ScheduleTaskEntity actualScheduleTask = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

        // Then
        assertEquals(expectedScheduleTask.getStatus(), actualScheduleTask.getStatus());
        assertEquals(expectedScheduleTask.getJobName(), actualScheduleTask.getJobName());
        assertEquals(expectedScheduleTask.getJobGroup(), actualScheduleTask.getJobGroup());
        assertEquals(expectedScheduleTask.getProgressPercentage(), actualScheduleTask.getProgressPercentage());
        assertEquals(expectedScheduleTask.getParametersJson(), actualScheduleTask.getParametersJson());
        assertEquals(expectedScheduleTask.getResultJson(), actualScheduleTask.getResultJson());
        assertEquals(expectedScheduleTask.getFireTime(), actualScheduleTask.getFireTime());
    }

    @Test
    public void testOnlineSchemaChangeFlowableTask_start_success() {
        // Given
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = Mockito.mock(OnlineSchemaChangeParameters.class);
        String schema = "testSchema";
        when(parameter.generateSubTaskParameters(connectionConfig, schema)).thenReturn(Collections.singletonList(Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class)));

        OnlineSchemaChangeFlowableTask testee = (OnlineSchemaChangeFlowableTask) onlineSchemaChangeFlowableTask;
        TestableOnlineSchemaChangeFlowableTask testableTestee = new TestableOnlineSchemaChangeFlowableTask(testee);

        // When
        testableTestee.start(1L, Mockito.mock(TaskService.class), Mockito.mock(DelegateExecution.class));

        // Then
        verify(quartzJobService).start(testableTestee.getScheduleId(), testableTestee.getTasks().get(0).getId());
        verify(testee).status = TaskStatus.RUNNING;
    }

    @Test
    public void testOnlineSchemaChangeFlowableTask_start_failure() {
        // Given
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = Mockito.mock(OnlineSchemaChangeParameters.class);
        String schema = "testSchema";
        when(parameter.generateSubTaskParameters(connectionConfig, schema)).thenThrow(new RuntimeException("Test exception"));

        OnlineSchemaChangeFlowableTask testee = (OnlineSchemaChangeFlowableTask) onlineSchemaChangeFlowableTask;
        TestableOnlineSchemaChangeFlowableTask testableTestee = new TestableOnlineSchemaChangeFlowableTask(testee);

        // When
        testableTestee.start(1L, Mockito.mock(TaskService.class), Mockito.mock(DelegateExecution.class));

        // Then
        verify(testee).status = TaskStatus.FAILED;
    }

    @Test
    public void testOnlineSchemaChangeFlowableTask_cancel() {
        // Given
        OnlineSchemaChangeFlowableTask testee = new OnlineSchemaChangeFlowableTask();
        ScheduleTaskEntity scheduleTask = Mockito.mock(ScheduleTaskEntity.class);
        testee.status = TaskStatus.RUNNING;
        testee.scheduleId = 1L;
        testee.tasks = ListUtils.singletonList(scheduleTask);

        // When
        testee.cancel(true, 1L, Mockito.mock(TaskService.class));

        // Then
        verify(quartzJobService).terminate(testee.getScheduleId(), scheduleTask.getId());
        verify(testee).status = TaskStatus.CANCELED;
    }

    @Test
    public void testOnlineSchemaChangeFlowableTask_isCancelled() {
        // Given
        OnlineSchemaChangeFlowableTask testee = new OnlineSchemaChangeFlowableTask();
        testee.status = TaskStatus.CANCELED;

        // Then
        assertTrue(testee.isCancelled());
    }
}

class TestableOnlineSchemaChangeFlowableTask extends OnlineSchemaChangeFlowableTask {
    private List<ScheduleTaskEntity> tasks;
    private Long scheduleId;

    public TestableOnlineSchemaChangeFlowableTask(BaseODCFlowTaskDelegate<Void> testee) {
        super();
        this.tasks = Collections.emptyList();
        this.scheduleId = 0L;
        this.status = TaskStatus.RUNNING;
    }

    public TestableOnlineSchemaChangeFlowableTask(OnlineSchemaChangeFlowableTask testee) {
        super();
        this.tasks = testee.tasks;
        this.scheduleId = testee.scheduleId;
        this.status = testee.status;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public List<ScheduleTaskEntity> getTasks() {
        return tasks;
    }
}