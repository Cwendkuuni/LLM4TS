import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
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
import com.oceanbase.odc.service.task.model.Task;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = { "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa", "spring.datasource.password=sa",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect" })
@DirtiesContext(classMode = DirtiesContext.Class)
public class OnlineSchemaChangeFlowableTaskTest {

    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @Autowired
    private QuartzJobService quartzJobService;
    @Autowired
    private OrganizationService organizationService;

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setContinueOnError(false);
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testStart() throws Exception {
        ProcessInstance processInstance = Mockito.mock(ProcessInstance.class);
        Task task = new Task();
        task.setId(1L);
        Mockito.when(taskService.detail(task.getId())).thenReturn(task);
        Mockito.when(taskService.start(task.getId())).thenReturn(null);
        Mockito.when(flowTaskUtil.getTaskCreator(Mockito.any(DelegateExecution.class))).thenReturn(new User());
        Mockito.when(organizationService.getUserById(Mockito.anyLong())).thenReturn(new User());
        Mockito.when(scheduleService.create(Mockito.any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());
        Mockito.when(scheduleTaskRepository.saveAndFlush(Mockito.any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());
        Mockito.when(quartzJobService.scheduleJob(Mockito.any(String.class), Mockito.any(TriggerConfig.class),
                Mockito.any(TriggerType.class))).thenReturn(null);
        Mockito.when(taskHandler.start(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);

        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        List<OnlineSchemaChangeScheduleTaskParameters> list = ListUtils.emptyIfNull(List.of(onlineSchemaChangeScheduleTaskParameters));
        onlineSchemaChangeFlowableTask.start(1L, taskService, processInstance);

        Task taskResult = taskService.detail(task.getId());
        Assert.assertEquals(TaskStatus.DONE, taskResult.getStatus());
    }

    @Test
    public void testIsSuccessful() {
        Task task = new Task();
        task.setId(1L);
        task.setStatus(TaskStatus.DONE);
        Assert.assertTrue(onlineSchemaChangeFlowableTask.isSuccessful(task.getId(), taskService));
    }

    @Test
    public void testIsFailure() {
        Task task = new Task();
        task.setId(1L);
        task.setStatus(TaskStatus.FAILED);
        Assert.assertTrue(onlineSchemaChangeFlowableTask.isFailure(task.getId(), taskService));
    }

    @Test
    public void testOnTimeout() {
        Task task = new Task();
        task.setId(1L);
        Assert.assertTrue(onlineSchemaChangeFlowableTask.onTimeout(task.getId(), taskService));
    }

    @Test
    public void testOnProgressUpdate() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
        scheduleTaskEntity.setProgressPercentage(100);
        Page<ScheduleTaskEntity> page = Mockito.mock(Page.class);
        Mockito.when(scheduleTaskService.listTask(Pageable.unpaged(), Mockito.anyLong())).thenReturn(page);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.progressStatusUpdate(page);

        Task task = Mockito.mock(Task.class);
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setStatus(TaskStatus.RUNNING);
        taskEntity.setProgressPercentage(50);
        task.setStatus(taskEntity.getStatus());
        task.setProgressPercentage(taskEntity.getProgressPercentage());

        double percentage = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity);
        Assert.assertEquals(1, percentage, 0.01);

        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        Task taskResult = taskService.detail(1L);
        Assert.assertEquals(JsonUtils.toJson(new OnlineSchemaChangeTaskResult(List.of(scheduleTaskEntity))),
                taskResult.getResultJson());
        Assert.assertEquals(TaskStatus.DONE, taskResult.getStatus());
        Assert.assertEquals(100, taskResult.getProgressPercentage());
    }

    @Test
    public void testCancel() throws Exception {
        ProcessInstance processInstance = Mockito.mock(ProcessInstance.class);
        Task task = new Task();
        task.setId(1L);
        Mockito.when(taskService.detail(task.getId())).thenReturn(task);
        Mockito.when(taskService.cancel(task.getId())).thenReturn(null);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        boolean result = onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        Assert.assertTrue(result);

        Task taskResult = taskService.detail(1L);
        Assert.assertEquals(TaskStatus.CANCELED, taskResult.getStatus());
    }
}