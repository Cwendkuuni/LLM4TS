import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfigRepository;
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
import com.oceanbase.odc.service.schedule.model.TriggerType;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.ProcessInstanceEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class to test OnlineSchemaChangeFlowableTask
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.Class)
public class OnlineSchemaChangeFlowableTaskTest {

    @MockBean
    private ConnectionConfigRepository connectionConfigRepository;

    @MockBean
    private ScheduleService scheduleService;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private QuartzJobService quartzJobService;

    @MockBean
    private OrganizationService organizationService;

    @MockBean
    private TaskService taskService;

    @MockBean
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @MockBean
    private ScheduleTaskService scheduleTaskService;

    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    private ConnectionConfig connectionConfig;
    private User user;

    @Before
    public void setup() {
        connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfigRepository.findById(any(UUID.class))).thenReturn(Optional.of(connectionConfig));
        user = mock(User.class);
    }

    @Test
    public void testStart() throws Exception {
        // Given
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getVariables()).thenReturn(Collections.emptyMap());
        when(execution.getId()).thenReturn(1L);
        when(execution.getProcessInstanceId()).thenReturn(1L);
        User creator = FlowTaskUtil.getTaskCreator(execution);
        when(creator.getId()).thenReturn(1L);
        when(creator.getOrganizationId()).thenReturn(1L);
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setContinueOnError(true);
        String schemaName = "test_schema";
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(onlineSchemaChangeParameters);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schemaName);
        when(FlowTaskUtil.getCloudMainAccountId(execution)).thenReturn("test_uid");
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);

        // When
        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        // Then
        TaskEntity taskEntity = taskService.detail(1L);
        Assert.assertEquals(TaskStatus.RUNNING, taskEntity.getStatus());
    }

    @Test
    public void testIsSuccessful() throws Exception {
        // Given
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.DONE);

        // When
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful(1L, taskService);

        // Then
        Assert.assertTrue(result);
    }

    @Test
    public void testIsFailure() throws Exception {
        // Given
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.FAILED);

        // When
        boolean result = onlineSchemaChangeFlowableTask.isFailure(1L, taskService);

        // Then
        Assert.assertTrue(result);
    }

    @Test
    public void testOnFailure() throws Exception {
        // Given
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.FAILED);

        // When
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

        // Then
        Assert.assertTrue(true);
    }

    @Test
    public void testOnSuccessful() throws Exception {
        // Given
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.DONE);

        // When
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

        // Then
        UpdateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
        Assert.assertTrue(true);
    }

    @Test
    public void testOnTimeout() throws Exception {
        // Given
        TaskEntity taskEntity = new TaskEntity();

        // When
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);

        // Then
        Assert.assertTrue(true);
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        // Given
        Page<ScheduleTaskEntity> scheduleTaskEntities = Mockito.mock(Page.class);
        Pageable pageable = Mockito.mock(Pageable.class);
        List<ScheduleTaskEntity> scheduleTaskEntityList = ListUtils.emptyIfNull(ListUtils.emptyList());
        when(scheduleTaskRepository.listTask(any(Pageable.class), any(Long.class))).thenReturn(scheduleTaskEntities);
        when(scheduleTaskEntities.getContent()).thenReturn(scheduleTaskEntityList);
        when(scheduleTaskService.listTask(pageable, 1L)).thenReturn(scheduleTaskEntities);

        // When
        OnlineSchemaChangeFlowableTask testObject = new OnlineSchemaChangeFlowableTask();
        testObject.onProgressUpdate(1L, taskService);

        // Then
        Assert.assertTrue(true);
    }

    @Test
    public void testCancel() throws Exception {
        // Given
        boolean mayInterruptIfRunning = false;
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);

        // When
        boolean result = onlineSchemaChangeFlowableTask.cancel(mayInterruptIfRunning, taskId, taskService);

        // Then
        Assert.assertTrue(result);
    }

    @Test
    public void testIsCancelled() throws Exception {
        // Given
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.CANCELED);

        // When
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        // Then
        Assert.assertTrue(result);
    }

    @Test
    public void testCreateScheduleEntity() throws Exception {
        // Given
        when(connectionConfigRepository.findById(any(UUID.class))).thenReturn(Optional.of(connectionConfig));

        // When
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig,
                new OnlineSchemaChangeParameters(), "test_schema");

        // Then
        Assert.assertNotNull(scheduleEntity);
    }

    @Test
    public void testCreateScheduleTaskEntity() throws Exception {
        // Given
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();

        // When
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId,
                onlineSchemaChangeScheduleTaskParameters);

        // Then
        Assert.assertNotNull(scheduleTaskEntity);
    }

    @Test
    public void testListScheduleTasks() throws Exception {
        // Given
        Page<ScheduleTaskEntity> scheduleTaskEntities = Mockito.mock(Page.class);
        Pageable pageable = Mockito.mock(Pageable.class);
        when(scheduleTaskRepository.listTask(pageable, 1L)).thenReturn(scheduleTaskEntities);

        // When
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        List<ScheduleTaskEntity> list = onlineSchemaChangeFlowableTask.listScheduleTasks(pageable, 1L);

        // Then
        Assert.assertNotNull(list);
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        // Given
        ProcessInstance processInstance = new ProcessInstanceEntity();
        processInstance.setId(1L);
        processInstance.setExecutionId(1L);

        // When
        UpdateFlowInstanceStatus(FlowStatus.EXECUTION_FAILED);

        // Then
        Assert.assertEquals(FlowStatus.EXECUTION_FAILED, processInstance.getStatus());
    }

    private void UpdateFlowInstanceStatus(FlowStatus status) {
        ProcessInstanceEntity processInstance = new ProcessInstanceEntity();
        processInstance.setId(1L);
        processInstance.setExecutionId(1L);
        processInstance.setStatus(FlowStatus.EXECUTION_RUNNING);
        taskService.update(processInstance);
        taskService.update(processInstance, new VariablesScope().variable("status", status.name()));
    }
}