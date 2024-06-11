import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskParameters;
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

@RunWith(MockitoJUnitRunner.class)
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
    private OscTaskCompleteHandler completeHandler;
    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    private User creator;
    private TaskEntity taskEntity;
    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        creator = new User();
        taskEntity = new TaskEntity();
        connectionConfig = new ConnectionConfig();
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testStart() throws Exception {
        // Given
        Mockito.when(organizationService.getCurrentUser()).thenReturn(creator);
        Mockito.when(taskService.detail(Mockito.anyLong())).thenReturn(taskEntity);
        Mockito.when(scheduleTaskRepository.saveAndFlush(Mockito.any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());
        Mockito.when(quartzJobService.scheduleJob(Mockito.anyString(), Mockito.any(JobType.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(TriggerConfig.class)))
                .thenReturn(null);

        // When
        onlineSchemaChangeFlowableTask.start(1L, taskService, new DelegateExecution() {});

        // Then
        Mockito.verify(taskService, Mockito.times(1)).start(1L);
        Assert.assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
    }

    @Test
    public void testIsSuccessful() {
        // Given

        // When

        // Then
        Assert.assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        // Given

        // When

        // Then
        Assert.assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() throws Exception {
        // Given
        Mockito.when(organizationService.getCurrentUser()).thenReturn(creator);
        Mockito.when(taskService.detail(Mockito.anyLong())).thenReturn(taskEntity);

        // When - Exception
        try {
            onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
        } catch (Exception e) {
            // Then
            Assert.assertEquals(FlowStatus.EXECUTION_FAILED, onlineSchemaChangeFlowableTask.getLastError().getStatus());
        }
    }

    @Test
    public void testOnSuccessful() throws Exception {
        // Given
        Mockito.when(organizationService.getCurrentUser()).thenReturn(creator);
        Mockito.when(taskService.detail(Mockito.anyLong())).thenReturn(taskEntity);
        Page<ScheduleTaskEntity> scheduleTasks = ListUtils.emptyIfNull(ListUtils.emptyIfNull(List.of(new ScheduleTaskEntity())));
        Mockito.when(scheduleTaskService.listTask(Mockito.any(Pageable.class), Mockito.eq(scheduleId))).thenReturn(scheduleTasks);

        // When
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

        // Then
        Mockito.verify(taskService, Mockito.times(1)).update(taskEntity);
    }

    @Test
    public void testOnTimeout() throws Exception {
        // Given

        // When

        // Then
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        // Given
        Page<ScheduleTaskEntity> scheduleTasks = ListUtils.emptyIfNull(ListUtils.emptyIfNull(List.of(new ScheduleTaskEntity())));
        Mockito.when(scheduleTaskService.listTask(Mockito.any(Pageable.class), Mockito.eq(scheduleId))).thenReturn(scheduleTasks);

        // When
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        // Then
        Mockito.verify(taskService, Mockito.times(1)).update(taskEntity);
    }

    @Test
    public void testCancel() throws Exception {
        // Given
        Mockito.when(organizationService.getCurrentUser()).thenReturn(creator);
        Mockito.when(taskService.listTasks(Mockito.any())).thenReturn(List.of(taskEntity));

        // When
        boolean cancelled = onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        // Then
        Mockito.verify(taskService, Mockito.times(1)).cancel(1L);
        Assert.assertTrue(cancelled);
    }

    private long scheduleId = RandomUtils.nextLong();
}