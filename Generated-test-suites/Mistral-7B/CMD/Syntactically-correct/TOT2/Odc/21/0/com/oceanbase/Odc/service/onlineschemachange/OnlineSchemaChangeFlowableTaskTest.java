import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.test.DelegationServiceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.support.EncodedFile;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
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
import com.oceanbase.odc.service.task.mock.TestTaskService;
import com.oceanbase.odc.service.test.BaseFlowableTest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.Class, methodMode = DirtiesContext.Method)
public class OnlineSchemaChangeFlowableTaskTest extends BaseFlowableTest {

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
    @Autowired
    private JpaMetamodelMappingContext mappingContext;

    private BaseODCFlowTaskDelegate<Void> onlineSchemaChangeFlowableTask;
    private TestTaskService taskService;
    private User creator;
    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @BeforeEach
    public void init() {
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        EncodedResource resource = new EncodedResource(getClass(), "/application-test.yml");
        yamlPropertiesFactoryBean.setResources(ListUtils.singletonList(resource));
        SpringApplicationContext context = new SpringApplicationBuilder()
                .properties(yamlPropertiesFactoryBean.getObject())
                .run(getClass().getPackage().getName().replaceAll("\\.", "\\/"));
        taskService = context.getBean(TestTaskService.class);
        creator = organizationService.createUser("test_creator", "test_password", "test_email@test.com", 1L);
        connectionConfig = FlowTaskUtil.getConnectionConfigFromYaml("test_connection");
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setContinueOnError(true);
    }

    @Test
    public void testStartTaskSuccess() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = onlineSchemaChangeTaskHandler(onlineSchemaChangeScheduleTaskParameters);
        Assertions.assertNotNull(onlineSchemaChangeTaskResult);
        Assertions.assertEquals(TaskStatus.DONE, onlineSchemaChangeTaskResult.getStatus());
    }

    @Test
    public void testStartTaskFailed() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = onlineSchemaChangeTaskHandler(onlineSchemaChangeScheduleTaskParameters);
        Assertions.assertNotNull(onlineSchemaChangeTaskResult);
        Assertions.assertEquals(TaskStatus.FAILED, onlineSchemaChangeTaskResult.getStatus());
    }

    @Test
    public void testStartTaskCanceled() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = onlineSchemaChangeTaskHandler(onlineSchemaChangeScheduleTaskParameters);
        Assertions.assertNotNull(onlineSchemaChangeTaskResult);
        Assertions.assertEquals(TaskStatus.CANCELED, onlineSchemaChangeTaskResult.getStatus());
    }

    @Test
    public void testStartTaskTimeout() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = onlineSchemaChangeTaskHandler(onlineSchemaChangeScheduleTaskParameters);
        Assertions.assertNotNull(onlineSchemaChangeTaskResult);
        Assertions.assertEquals(TaskStatus.TIMEOUT, onlineSchemaChangeTaskResult.getStatus());
    }

    @Test
    public void testStartTaskProgressUpdate() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult1 = onlineSchemaChangeTaskHandler(onlineSchemaChangeScheduleTaskParameters);
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult2 = onlineSchemaChangeTaskHandler(onlineSchemaChangeScheduleTaskParameters);
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult3 = onlineSchemaChangeTaskHandler(onlineSchemaChangeScheduleTaskParameters);

        Page<ScheduleTaskEntity> scheduleTasks = getScheduledTasks();
        Assertions.assertEquals(3, scheduleTasks.getSize());
        progressStatusUpdate(scheduleTasks);
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = getLastTaskResult(scheduleTasks);
        Assertions.assertNotNull(onlineSchemaChangeTaskResult);
        Assertions.assertEquals(TaskStatus.DONE, onlineSchemaChangeTaskResult.getStatus());
    }

    private void progressStatusUpdate(Page<ScheduleTaskEntity> scheduleTasks) throws Exception {
        for (ScheduleTaskEntity scheduleTask : scheduleTasks) {
            DelegateExecution execution = Mockito.mock(DelegateExecution.class);
            OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
            onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);
            onlineSchemaChangeFlowableTask.setCreatorId(creator.getId());
            onlineSchemaChangeFlowableTask.setOrganizationId(creator.getOrganizationId());
            onlineSchemaChangeFlowableTask.scheduleId = scheduleTask.getId();
            onlineSchemaChangeFlowableTask.start(null, taskService, execution);
            scheduleTaskService.updateProgress(scheduleTask.getId(), 50);
            onlineSchemaChangeFlowableTask.onProgressUpdate(taskService.getExecutionById(scheduleTask.getId()).getId(), taskService);
            scheduleTaskService.updateProgress(scheduleTask.getId(), 100);
        }
    }

    private OnlineSchemaChangeTaskResult onlineSchemaChangeTaskHandler(OnlineSchemaChangeScheduleTaskParameters param) throws Exception {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setConnectionConfig(connectionConfig);
        onlineSchemaChangeFlowableTask.setOnlineSchemaChangeParameters(onlineSchemaChangeParameters);
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = onlineSchemaChangeFlowableTask.start(null, taskService, null);
        return onlineSchemaChangeTaskResult;
    }

    private Page<ScheduleTaskEntity> getScheduledTasks() {
        return scheduleTaskService.listTask(Pageable.unpaged(), 1L);
    }

    private OnlineSchemaChangeTaskResult getLastTaskResult(Page<ScheduleTaskEntity> scheduleTasks) {
        return JsonUtils.parseObject(scheduleTasks.get(scheduleTasks.getSize() - 1).getResultJson(), OnlineSchemaChangeTaskResult.class);
    }
}