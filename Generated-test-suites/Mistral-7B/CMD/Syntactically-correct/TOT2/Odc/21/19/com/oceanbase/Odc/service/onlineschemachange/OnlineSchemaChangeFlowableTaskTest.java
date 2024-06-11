import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.test.FlowableTestRule;
import org.flowable.engine.test.mock.MockDelegateExecution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.task.model.TaskEntity;
import com.oceanbase.odc.service.task.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.test.TestHelper;
import com.oceanbase.odc.service.test.TestHelper.TestResource;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

    @Rule
    public FlowableTestRule flowableTestRule = new FlowableTestRule();

    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private QuartzJobService quartzJobService;

    @Autowired
    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TestHelper.init();
    }

    @Test
    public void testOnlineSchemaChangeFlowableTaskSuccess() throws Exception {
        ConnectionConfig connectionConfig = TestResource.getConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        parameter.setContinueOnError(true);
        String schema = "test_schema";

        ScheduleEntity schedule = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);
        ScheduleTaskEntity scheduleTask = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(schedule.getId(),
                new OnlineSchemaChangeScheduleTaskParameters());

        MockDelegateExecution execution = TestHelper.getMockDelegateExecution();
        execution.putVariable("ONLINE_SCHEMA_CHANGE_PARAMETERS", parameter);
        execution.putVariable("CONNECTION_CONFIG", connectionConfig);
        execution.putVariable("SCHEDULE", schedule);
        execution.putVariable("ScheduleTaskEntity", scheduleTask);
        execution.putVariable("SCHEDULE_TASK_ID", scheduleTask.getId());
        execution.putVariable("ONLINE_SCHEMA_CHANGE_SCHEDULE_TASK_RESULT", new OnlineSchemaChangeScheduleTaskResult(
                new OnlineSchemaChangeTaskResult(ListUtils.emptyList())));

        onlineSchemaChangeFlowableTask.start(null, taskService, execution);

        Assert.assertEquals(TaskStatus.DONE, taskService.detail(execution.getId()).getStatus());
    }

    @Test
    public void testOnlineSchemaChangeFlowableTaskException() throws Exception {
        ConnectionConfig connectionConfig = TestResource.getConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        parameter.setContinueOnError(false);
        String schema = "test_schema";

        ScheduleEntity schedule = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);
        ScheduleTaskEntity scheduleTask = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(schedule.getId(),
                new OnlineSchemaChangeScheduleTaskParameters());

        MockDelegateExecution execution = TestHelper.getMockDelegateExecution();
        execution.putVariable("ONLINE_SCHEMA_CHANGE_PARAMETERS", parameter);
        execution.putVariable("CONNECTION_CONFIG", connectionConfig);
        execution.putVariable("SCHEDULE", schedule);
        execution.putVariable("ScheduleTaskEntity", scheduleTask);
        execution.putVariable("SCHEDULE_TASK_ID", scheduleTask.getId());
        execution.putVariable("ONLINE_SCHEMA_CHANGE_SCHEDULE_TASK_RESULT", new OnlineSchemaChangeScheduleTaskResult(
                new OnlineSchemaChangeTaskResult(ListUtils.emptyList())));

        onlineSchemaChangeFlowableTask.start(null, taskService, execution);

        Assert.assertEquals(TaskStatus.FAILED, taskService.detail(execution.getId()).getStatus());
    }

    @Test
    public void testOnlineSchemaChangeFlowableTaskCancel() throws Exception {
        ConnectionConfig connectionConfig = TestResource.getConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "test_schema";

        ScheduleEntity schedule = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);
        ScheduleTaskEntity scheduleTask = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(schedule.getId(),
                new OnlineSchemaChangeScheduleTaskParameters());

        onlineSchemaChangeFlowableTask.cancel(true, null, taskService);

        Assert.assertEquals(TaskStatus.CANCELED, taskService.detail(null).getStatus());
    }

    @Test
    public void testScheduleTaskFailure() throws Exception {
        ConnectionConfig connectionConfig = TestResource.getConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "test_schema";

        ScheduleEntity schedule = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);
        ScheduleTaskEntity scheduleTask = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(schedule.getId(),
                new OnlineSchemaChangeScheduleTaskParameters());

        MockDelegateExecution execution = TestHelper.getMockDelegateExecution();
        execution.putVariable("ONLINE_SCHEMA_CHANGE_PARAMETERS", parameter);
        execution.putVariable("CONNECTION_CONFIG", connectionConfig);
        execution.putVariable("SCHEDULE", schedule);
        execution.putVariable("ScheduleTaskEntity", scheduleTask);
        execution.putVariable("SCHEDULE_TASK_ID", scheduleTask.getId());
        execution.putVariable("ONLINE_SCHEMA_CHANGE_SCHEDULE_TASK_RESULT", new OnlineSchemaChangeScheduleTaskResult(
                new OnlineSchemaChangeTaskResult(ListUtils.emptyList())));

        onlineSchemaChangeFlowableTask.start(null, taskService, execution);

        scheduleTask.setStatus(TaskStatus.FAILED);
        scheduleTaskRepository.saveAndFlush(scheduleTask);

        onlineSchemaChangeFlowableTask.progressUpdate(null, taskService);

        Assert.assertEquals(TaskStatus.FAILED, taskService.detail(execution.getId()).getStatus());
    }

    @Test
    public void testScheduleTaskSuccess() throws Exception {
        ConnectionConfig connectionConfig = TestResource.getConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "test_schema";

        ScheduleEntity schedule = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setStatus(TaskStatus.DONE);
        scheduleTask.setId(1L);
        scheduleTaskRepository.saveAndFlush(scheduleTask);

        MockDelegateExecution execution = TestHelper.getMockDelegateExecution();
        execution.putVariable("ONLINE_SCHEMA_CHANGE_PARAMETERS", parameter);
        execution.putVariable("CONNECTION_CONFIG", connectionConfig);
        execution.putVariable("SCHEDULE", schedule);
        execution.putVariable("ScheduleTaskEntity", scheduleTask);
        execution.putVariable("SCHEDULE_TASK_ID", scheduleTask.getId());
        execution.putVariable("ONLINE_SCHEMA_CHANGE_SCHEDULE_TASK_RESULT", new OnlineSchemaChangeScheduleTaskResult(
                new OnlineSchemaChangeTaskResult(ListUtils.emptyList())));

        onlineSchemaChangeFlowableTask.start(null, taskService, execution);

        onlineSchemaChangeFlowableTask.progressUpdate(null, taskService);

        Assert.assertEquals(TaskStatus.DONE, taskService.detail(execution.getId()).getStatus());
    }

    @Test
    public void testOnlineSchemaChangeFlowableTaskComplete() throws Exception {
        ConnectionConfig connectionConfig = TestResource.getConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "test_schema";

        ScheduleEntity schedule = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTaskRepository.saveAndFlush(scheduleTask);

        TaskEntity task = new TaskEntity();
        task.setId(1L);
        task.setStatus(TaskStatus.RUNNING);
        taskService.save(task);

        MockDelegateExecution execution = TestHelper.getMockDelegateExecution();
        execution.putVariable("ONLINE_SCHEMA_CHANGE_PARAMETERS", parameter);
        execution.putVariable("CONNECTION_CONFIG", connectionConfig);
        execution.putVariable("SCHEDULE", schedule);
        execution.putVariable("ScheduleTaskEntity", scheduleTask);
        execution.putVariable("SCHEDULE_TASK_ID", scheduleTask.getId());
        execution.putVariable("ONLINE_SCHEMA_CHANGE_SCHEDULE_TASK_RESULT", new OnlineSchemaChangeScheduleTaskResult(
                new OnlineSchemaChangeTaskResult(ListUtils.emptyList())));

        onlineSchemaChangeFlowableTask.start(null, taskService, execution);
        onlineSchemaChangeFlowableTask.onSuccessful(null, taskService);

        Assert.assertEquals(TaskStatus.EXECUTION_SUCCEEDED, task.getStatus());
    }

    @Test
    public void testOnlineSchemaChangeFlowableTaskTimeout() throws Exception {
        ConnectionConfig connectionConfig = TestResource.getConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "test_schema";

        ScheduleEntity schedule = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTaskRepository.saveAndFlush(scheduleTask);

        TaskEntity task = new TaskEntity();
        task.setId(1L);
        task.setStatus(TaskStatus.RUNNING);
        taskService.save(task);

        MockDelegateExecution execution = TestHelper.getMockDelegateExecution();
        execution.putVariable("ONLINE_SCHEMA_CHANGE_PARAMETERS", parameter);
        execution.putVariable("CONNECTION_CONFIG", connectionConfig);
        execution.putVariable("SCHEDULE", schedule);
        execution.putVariable("ScheduleTaskEntity", scheduleTask);
        execution.putVariable("SCHEDULE_TASK_ID", scheduleTask.getId());
        execution.putVariable("ONLINE_SCHEMA_CHANGE_SCHEDULE_TASK_RESULT", new OnlineSchemaChangeScheduleTaskResult(
                new OnlineSchemaChangeTaskResult(ListUtils.emptyList())));

        onlineSchemaChangeFlowableTask.start(null, taskService, execution);
        Thread.sleep(1000);
        onlineSchemaChangeFlowableTask.onTimeout(null, taskService);

        Assert.assertEquals(TaskStatus.EXECUTION_TIMEDOUT, task.getStatus());
    }

    @Test
    public void testOnlineSchemaChangeFlowableTaskError() throws Exception {
        String randomSchema = RandomStringUtils.randomAlphanumeric(10);
        ConnectionConfig connectionConfig = TestResource.getConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        parameter.setContinueOnError(true);
        parameter.setSchema(randomSchema);

        ScheduleEntity schedule = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, randomSchema);
        ScheduleTaskEntity scheduleTask = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(schedule.getId(),
                new OnlineSchemaChangeScheduleTaskParameters());

        MockDelegateExecution execution = TestHelper.getMockDelegateExecution();
        execution.putVariable("ONLINE_SCHEMA_CHANGE_PARAMETERS", parameter);
        execution.putVariable("CONNECTION_CONFIG", connectionConfig);
        execution.putVariable("SCHEDULE", schedule);
        execution.putVariable("ScheduleTaskEntity", scheduleTask);
        execution.putVariable("SCHEDULE_TASK_ID", scheduleTask.getId());
        execution.putVariable("ONLINE_SCHEMA_CHANGE_SCHEDULE_TASK_RESULT", new OnlineSchemaChangeScheduleTaskResult(
                new OnlineSchemaChangeTaskResult(ListUtils.emptyList())));

        onlineSchemaChangeFlowableTask.start(null, taskService, execution);
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = JsonUtils.parseObject(execution.getVariable("ONLINE_SCHEMA_CHANGE_TASK_RESULT"), OnlineSchemaChangeTaskResult.class);
        Assert.assertNotNull(onlineSchemaChangeTaskResult);
        Assert.assertEquals(randomSchema, onlineSchemaChangeTaskResult.getSchema());

        scheduleTask.setStatus(TaskStatus.FAILED);
        scheduleTaskRepository.saveAndFlush(scheduleTask);

        onlineSchemaChangeFlowableTask.progressUpdate(null, taskService);

        OnlineSchemaChangeTaskResult newOnlineSchemaChangeTaskResult = JsonUtils.parseObject(execution.getVariable("ONLINE_SCHEMA_CHANGE_TASK_RESULT"), OnlineSchemaChangeTaskResult.class);
        Assert.assertNotNull(newOnlineSchemaChangeTaskResult);
        Assert.assertEquals(randomSchema, newOnlineSchemaChangeTaskResult.getSchema());
        Assert.assertEquals(ListUtils.emptyList(), newOnlineSchemaChangeTaskResult.getErrors());
        Assert.assertTrue(newOnlineSchemaChangeTaskResult.isSuccess());
    }
}