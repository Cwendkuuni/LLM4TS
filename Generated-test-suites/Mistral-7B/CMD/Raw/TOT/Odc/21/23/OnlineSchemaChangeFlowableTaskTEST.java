import org.apache.commons.collections4.ListUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity;
import com.oceanbase.odc.service.task.TaskEntity;
import com.oceanbase.odc.service.task.TaskService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringJUnitConfig
@ContextConfiguration(classes = OnlineSchemaChangeFlowableTaskTest.TestConfig.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private QuartzJobService quartzJobService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleTaskService scheduleTaskService;

    @InjectMocks
    private ConnectionConfig connectionConfig;
    @InjectMocks
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    @InjectMocks
    private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;
    @InjectMocks
    private User user;
    private static List<ScheduleTaskEntity> scheduleTasks;

    @BeforeAll
    public static void setup() {
        MockitoAnnotations.initMocks(OnlineSchemaChangeFlowableTest.class);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(context);
        beanDefinitionReader.loadBeanDefinitions("classpath:spring-config.xml");
        context.registerBean("taskService", TaskService.class, () -> taskService);
        context.registerBean("scheduleTaskRepository", ScheduleTaskRepository.class, () -> scheduleTaskRepository);
        context.registerBean("organizationService", OrganizationService.class, () -> organizationService);
        context.registerBean("quartzJobService", QuartzJobService.class, () -> quartzJobService);
        context.registerBean("scheduleService", ScheduleService.class, () -> scheduleService);
        context.registerBean("scheduleTaskService", ScheduleTaskService.class, () -> scheduleTaskService);
        context.refresh();

        scheduleTasks = new ArrayList<>();
    }

    @Test
    public void testStart() throws Exception {
        Mockito.when(organizationService.getUserById(user.getId())).thenReturn(user);
        Mockito.when(FlowTaskUtil.getCloudMainAccountId(Mockito.any())).thenReturn("uid");
        Mockito.when(FlowTaskUtil.getOnlineSchemaChangeParameter(Mockito.any())).thenReturn(onlineSchemaChangeParameters);
        Mockito.when(FlowTaskUtil.getConnectionConfig(Mockito.any())).thenReturn(connectionConfig);
        Mockito.when(FlowTaskUtil.getSchemaName(Mockito.any())).thenReturn("schema");

        OnlineSchemaChangeContextHolder.clear();

        TaskEntity taskEntity = Mockito.mock(TaskEntity.class);
        Mockito.when(taskService.detail(Mockito.anyLong())).thenReturn(taskEntity);

        Executable executable = () -> onlineSchemaChangeFlowableTask.start(1L, taskService, Mockito.mock(DelegateExecution.class));

        Assertions.assertDoesNotThrow(executable);

        Mockito.verify(taskService, Mockito.times(1)).start(1L);
        Mockito.verify(taskService, Mockito.times(1)).update(taskEntity);
        Mockito.verify(quartzJobService).start(onlineSchemaChangeFlowableTask);
        Mockito.verify(scheduleTaskService, Mockito.times(1)).listTask(Mockito.any(Pageable.class), Mockito.eq(scheduleId));
        Mockito.verify(taskHandler, Mockito.times(1)).start(scheduleId, scheduleTasks.get(0).getId());

        OnlineSchemaChangeContextHolder.trace(user.getId(), 1L, organizationId);
        OnlineSchemaChangeContextHolder.clear();
    }

    // Add other test methods for methods isSuccessful(), isFailure(), onFailure(), onSuccessful(), onTimeout(), cancel(), and isCancelled()

    @Configuration
    @Import({OnlineSchemaChangeFlowableTask.class, BaseODCFlowTaskDelegate.class})
    static class TestConfig {
    }
}