import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.DelegatingConnection;
import org.apache.commons.dbcp2.DelegatingConnectionFactory;
import org.flowable.common.engine.impl.interceptor.TestCommandExecutor;
import org.flowable.engine.impl.test.AbstractFlowableTestCase;
import org.flowable.engine.impl.test.FlowableTestUtil;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

@SpringBootTest
@ActiveProfiles("test")
public class OnlineSchemaChangeFlowableTaskTest extends AbstractFlowableTestCase {

 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
 @Autowired private ScheduleTaskRepository scheduleTaskRepository;
 @Autowired private TaskService taskService;
 @Autowired private OrganizationService organizationService;
 @Autowired private QuartzJobService quartzJobService;
 @Autowired private ScheduleService scheduleService;
 @Autowired private ScheduleTaskService scheduleTaskService;
 @Value("${test.onlineSchemaChangeFlowableTask.connectionName}") private String connectionName;
 @Value("${test.onlineSchemaChangeFlowableTask.connectionUrl}") private String connectionUrl;
 @Value("${test.onlineSchemaChangeFlowableTask.connectionUsername}") private String connectionUsername;
 @Value("${test.onlineSchemaChangeFlowableTask.connectionPassword}") private String connectionPassword;
 @Value("${test.onlineSchemaChangeFlowableTask.schemaName}") private String schemaName;

 @Before
 public void setUp() {
 TestCommandExecutor testCommandExecutor = new TestCommandExecutor();
 FlowableTestUtil.initTestEnvironment(testCommandExecutor);

 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setId(connectionName);
 connectionConfig.setUrl(connectionUrl);
 connectionConfig.setDriverClassName("com.mariadb.jdbc.Driver");
 connectionConfig.setUsername(connectionUsername);
 connectionConfig.setPassword(connectionPassword);
 connectionConfig.setJdbcUrl(connectionUrl);
 connectionConfig.setJdbcDriverClassName("com.mariadb.jdbc.Driver");

 BasicDataSource ds = new BasicDataSource();
 ds.setDriverClassName(connectionConfig.getJdbcDriverClassName());
 ds.setUrl(connectionConfig.getJdbcUrl());
 ds.setUsername(connectionConfig.getUsername());
 ds.setPassword(connectionConfig.getPassword());
 ds.setTestOnBorrow(false);

 DelegatingConnectionFactory connectionFactory = new DelegatingConnectionFactory(ds, "testConnectionFactory");
 connectionConfig.setConnectionPool(connectionFactory);

 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setConnectionConfig(connectionConfig);
 parameters.setSchemaName(schemaName);
 parameters.setContinueOnError(false);

 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
 onlineSchemaChangeFlowableTask.setTaskService(taskService);
 onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
 onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
 onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
 onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
 onlineSchemaChangeFlowableTask.setTaskHandler(mock(OscTaskCompleteHandler.class));
 onlineSchemaChangeFlowableTask.setCompleteHandler(mock(OscTaskCompleteHandler.class));
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);
 this.onlineSchemaChangeFlowableTask = onlineSchemaChangeFlowableTask;
 }

 @Deployment(resources = {"classpath:processes/OnlineSchemaChangeFlowableTask.bpmn"})
 @Test
 public void test_onlineSchemaChangeFlowableTask_success() throws Exception {
 // Given
 ProcessInstance processInstance = startProcessInstanceByKey("OnlineSchemaChangeFlowableTask");
 TaskEntity taskEntity = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = JsonUtils.parseJson(this.onlineSchemaChangeFlowableTask.getJobParametersJson(), OnlineSchemaChangeParameters.class);
 OnlineSchemaChangeTaskResult expectedResult = new OnlineSchemaChangeTaskResult(List.of());

 // When
 onlineSchemaChangeFlowableTask.setConnectionConfig(onlineSchemaChangeParameters.getConnectionConfig());
 onlineSchemaChangeFlowableTask.start(taskEntity.getId(), taskService, Mockito.mock(DelegateExecution.class));

 // Then
 OnlineSchemaChangeTaskResult result = (OnlineSchemaChangeTaskResult) taskEntity.getResult();
 assertThat(result).isNotNull();
 assertThat(result).getTasks().size().isEqualTo(0);
 assertThat(onlineSchemaChangeFlowableTask.getStatus()).isEqualTo(TaskStatus.DONE);
 }

 @Deployment(resources = {"classpath:processes/OnlineSchemaChangeFlowableTask.bpmn"})
 @Test
 public void test_onlineSchemaChangeFlowableTask_failure() throws Exception {
 // Given
 ProcessInstance processInstance = startProcessInstanceByKey("OnlineSchemaChangeFlowableTask");
 TaskEntity taskEntity = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = JsonUtils.parseJson(this.onlineSchemaChangeFlowableTask.getJobParametersJson(), OnlineSchemaChangeParameters.class);
 OnlineSchemaChangeTaskResult expectedResult = new OnlineSchemaChangeTaskResult(List.of());
 expectedResult.setErrorMessage("Test error message");

 // When
 when(this.onlineSchemaChangeFlowableTask.getTaskHandler().handle(anyInt(), anyInt(), any(DelegateExecution.class)))
 .thenThrow(new RuntimeException("Test exception"));
 onlineSchemaChangeFlowableTask.setConnectionConfig(onlineSchemaChangeParameters.getConnectionConfig());
 onlineSchemaChangeFlowableTask.start(taskEntity.getId(), taskService, Mockito.mock(DelegateExecution.class));

 // Then
 OnlineSchemaChangeTaskResult result = (OnlineSchemaChangeTaskResult) taskEntity.getResult();
 assertThat(result).isNotNull();
 assertThat(result.getErrorMessage()).isEqualTo("Test error message");
 assertThat(onlineSchemaChangeFlowableTask.getStatus()).isEqualTo(TaskStatus.FAILED);
 }

 @Deployment(resources = {"classpath:processes/OnlineSchemaChangeFlowableTask.bpmn"})
 @Test
 public void test_onlineSchemaChangeFlowableTask_cancelled() throws Exception {
 // Given
 ProcessInstance processInstance = startProcessInstanceByKey("OnlineSchemaChangeFlowableTask");
 TaskEntity taskEntity = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = JsonUtils.parseJson(this.onlineSchemaChangeFlowableTask.getJobParametersJson(), OnlineSchemaChangeParameters.class);
 OnlineSchemaChangeTaskResult expectedResult = new OnlineSchemaChangeTaskResult(List.of());
 expectedResult.setErrorMessage("Test error message");
 expectedResult.setCancelled(true);

 // When
 when(this.onlineSchemaChangeFlowableTask.getTaskHandler().handle(anyInt(), anyInt(), any(DelegateExecution.class)))
 .thenThrow(new RuntimeException("Test exception"));
 when(taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult().getResult()).thenReturn(expectedResult);
 onlineSchemaChangeFlowableTask.setConnectionConfig(onlineSchemaChangeParameters.getConnectionConfig());
 onlineSchemaChangeFlowableTask.start(taskEntity.getId(), taskService, Mockito.mock(DelegateExecution.class));
 onlineSchemaChangeFlowableTask.cancel(true, taskEntity.getId(), taskService);

 // Then
 TaskEntity task = taskService.detail(taskEntity.getId());
 assertThat(task.getStatus()).isEqualTo(TaskStatus.CANCELED);
 }

 @Test
 public void test_createScheduleEntity() {
 ScheduleEntity expectedScheduleEntity = new ScheduleEntity();
 expectedScheduleEntity.setConnectionId(connectionName);
 expectedScheduleEntity.setDatabaseName(schemaName);
 expectedScheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 expectedScheduleEntity.setStatus(ScheduleStatus.ENABLED);
 expectedScheduleEntity.setAllowConcurrent(false);
 expectedScheduleEntity.setCreatorId(1L);
 expectedScheduleEntity.setOrganizationId(1L);
 expectedScheduleEntity.setProjectId(1L);
 expectedScheduleEntity.setDatabaseId(1L);
 expectedScheduleEntity.setModifierId(expectedScheduleEntity.getCreatorId());
 TriggerConfig triggerConfig = new TriggerConfig();
 triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
 triggerConfig.setCronExpression("0/10 * * * * ?");
 expectedScheduleEntity.setTriggerConfigJson(JsonUtils.toJson(triggerConfig));
 expectedScheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
 ScheduleEntity actualScheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameters, schemaName);
 assertThat(actualScheduleEntity).isEqualTo(expectedScheduleEntity);
 }

 @Test
 public void test_createScheduleTaskEntity() {
 ScheduleTaskEntity expectedScheduleTaskEntity = new ScheduleTaskEntity();
 expectedScheduleTaskEntity.setStatus(TaskStatus.PREPARING);
 expectedScheduleTaskEntity.setJobName(connectionName + "_1");
 expectedScheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
 expectedScheduleTaskEntity.setProgressPercentage(0.0);
 expectedScheduleTaskEntity.setParametersJson(JsonUtils.toJson(parameters));
 expectedScheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(parameters)));
 expectedScheduleTaskEntity.setFireTime(new Date());
 ScheduleTaskEntity actualScheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, parameters);
 assertThat(actualScheduleTaskEntity).isEqualTo(expectedScheduleTaskEntity);
 }
}