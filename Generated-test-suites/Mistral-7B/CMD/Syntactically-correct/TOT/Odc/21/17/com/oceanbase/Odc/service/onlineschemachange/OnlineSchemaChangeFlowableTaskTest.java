import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.runtime.ProcessInstance;
import org. flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableTestRule;
import org.flowable.engine.test.mock.MockDelegateExecution;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
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
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Deployment(resources = { "classpath:/com/oceanbase/odc/service/onlineschemachange/OnlineSchemaChangeFlowableTaskTest.bpmn20.xml" })
@Rule
public class OnlineSchemaChangeFlowableTaskTest {

 private static final String SCHEDULE_ID = "scheduleId";
 private static final String JOB_NAME = "jobName";
 private static final String ONLINE_SCHEMA_CHANGE_TASK_NAME = "onlineSchemaChangeTaskName";
 private static final String CONNECTION_ID = "connectionId";
 private static final String SCHEMA_NAME = "schemaName";
 private static final String ORGANIZATION_ID = "organizationId";
 private static final boolean CONTINUE_ON_ERROR = false;
 private static final int NUM_OF_SUB_TASKS = 3;

 @Autowired
 private TaskService taskService;
 @Autowired
 private ScheduleTaskRepository scheduleTaskRepository;
 @Autowired
 private OrganizationService organizationService;
 @Autowired
 private ScheduleService scheduleService;

 @Before
 public void setup() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setId(CONNECTION_ID);
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeParameters.setConnectionConfig(connectionConfig);
 onlineSchemaChangeParameters.setContinueOnError(CONTINUE_ON_ERROR);
 OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
 onlineSchemaChangeScheduleTaskParameters.setParameters(onlineSchemaChangeParameters);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(SCHEDULE_ID);
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 scheduleEntity.setStatus(ScheduleStatus.ENABLED);
 scheduleEntity.setAllowConcurrent(false);
 scheduleEntity.setCreatorId(1L);
 scheduleEntity.setOrganizationId(ORGANIZATION_ID);
 scheduleEntity.setDatabaseName(SCHEMA_NAME);
 TriggerConfig triggerConfig = new TriggerConfig();
 triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
 triggerConfig.setCronExpression("0/10 * * * * ?");
 scheduleEntity.setTriggerConfigJson(JsonUtils.toJson(triggerConfig));
 scheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(onlineSchemaChangeScheduleTaskParameters));
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
 scheduleTaskEntity.setJobName(JOB_NAME);
 scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
 scheduleTaskEntity.setProgressPercentage(0.0);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJson(onlineSchemaChangeScheduleTaskParameters));
 scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(onlineSchemaChangeScheduleTaskParameters)));
 scheduleTaskEntity.setFireTime(new Date());
 scheduleEntity.setTasks(ListUtils.emptyIfNull(List.of(scheduleTaskEntity)));
 when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(scheduleEntity);
 when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
 }

 @Test
 public void testStart() throws Exception {
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 MockDelegateExecution execution = new MockDelegateExecution();
 ProcessInstance processInstance = mock(ProcessInstance.class);
 when(taskService.detail(anyLong())).thenReturn(processInstance);
 BaseODCFlowTaskDelegate.ExecutionDelegate executionDelegate = onlineSchemaChangeFlowableTask.new ExecutionDelegate();
 executionDelegate.setDelegateExecution(execution);
 onlineSchemaChangeFlowableTask.start(1L, taskService, executionDelegate);
 assertNotNull(processInstance.getId());
 assertEquals(TaskStatus.RUNNING, processInstance.getStatus());
 }

 @Test
 public void testIsSuccessful() {
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
 }

 @Test
 public void testIsFailure() {
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 assertFalse(onlineSchemaChangeFlowableTask.isFailure());
 }

 @Test
 public void testCancel() throws Exception {
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 when(taskService.cancel(anyLong())).thenReturn(true);
 onlineSchemaChangeFlowableTask.cancel(false, 1L, taskService);
 assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
 }

 @Test
 public void testIsCancelled() {
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 assertFalse(onlineSchemaChangeFlowableTask.isCancelled());
 }

 @Test
 public void testOnlineSchemaChangeTaskSuccess() throws Exception {
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 when(taskService.list(any(Pageable.class))).thenReturn(Page.empty());
 when(scheduleTaskRepository.findAll()).thenReturn(List.of(scheduleTaskEntity));
 when(taskService.detail(anyLong())).thenReturn(mock(ProcessInstance.class));
 when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(Page.of(List.of(scheduleTaskEntity), Pageable.unpaged()));
 when(taskHandler.start(anyLong(), anyLong())).thenReturn(true);
 when(completeHandler.handle(any())).thenAnswer(invocation -> {});
 onlineSchemaChangeFlowableTask.setScheduleId(SCHEDULE_ID);
 onlineSchemaChangeFlowableTask.setPercentage(0);
 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
 assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
 assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
 assertEquals(TaskStatus.DONE, onlineSchemaChangeFlowableTask.getStatus());
 }

 @Test
 public void testOnlineSchemaChangeTaskFailure() throws Exception {
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 when(taskService.list(any(Pageable.class))).thenReturn(Page.empty());
 when(scheduleTaskRepository.findAll()).thenReturn(List.of(scheduleTaskEntity));
 when(taskService.detail(anyLong())).thenReturn(mock(ProcessInstance.class));
 when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(Page.of(List.of(scheduleTaskEntity), Pageable.unpaged()));
 when(taskHandler.start(anyLong(), anyLong())).thenReturn(false);
 onlineSchemaChangeFlowableTask.setScheduleId(SCHEDULE_ID);
 onlineSchemaChangeFlowableTask.setPercentage(0);
 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
 assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
 assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
 assertEquals(TaskStatus.FAILED, onlineSchemaChangeFlowableTask.getStatus());
 }
}