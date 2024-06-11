import org.apache.commons.collections4.ListUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.iam.OrganizationService;
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
import com.oceanbase.odc.service.task.model.TaskResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OnlineSchemaChangeFlowableTaskTest {

 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
 private BaseODCFlowTaskDelegate baseODCFlowTaskDelegateMock;
 private OrganizationService organizationServiceMock;
 private QuartzJobService quartzJobServiceMock;
 private ScheduleTaskRepository scheduleTaskRepositoryMock;
 private ScheduleTaskService scheduleTaskServiceMock;
 private ScheduleService scheduleServiceMock;
 private TaskService taskServiceMock;
 private ConnectionConfig connectionConfigMock;
 private OnlineSchemaChangeParameters onlineSchemaChangeParametersMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 baseODCFlowTaskDelegateMock = mock(BaseODCFlowTaskDelegate.class);
 organizationServiceMock = mock(OrganizationService.class);
 quartzJobServiceMock = mock(QuartzJobService.class);
 scheduleTaskRepositoryMock = mock(ScheduleTaskRepository.class);
 scheduleTaskServiceMock = mock(ScheduleTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 taskServiceMock = mock(TaskService.class);
 connectionConfigMock = mock(ConnectionConfig.class);
 onlineSchemaChangeParametersMock = mock(OnlineSchemaChangeParameters.class);
 onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

 onlineSchemaChangeFlowableTask.setBaseODCFlowTaskDelegate(baseODCFlowTaskDelegateMock);
 onlineSchemaChangeFlowableTask.setOrganizationService(organizationServiceMock);
 onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobServiceMock);
 onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepositoryMock);
 onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskServiceMock);
 onlineSchemaChangeFlowableTask.setScheduleService(scheduleServiceMock);
 onlineSchemaChangeFlowableTask.setTaskService(taskServiceMock);
 }

 @Test
 public void testStart() throws Exception {
 // Given
 ConnectionConfig connectionConfig = Mockito.spy(connectionConfigMock);
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = Mockito.spy(onlineSchemaChangeParametersMock);
 String schema = "test_schema";
 when(baseODCFlowTaskDelegateMock.getConnectionConfig()).thenReturn(connectionConfig);
 when(baseODCFlowTaskDelegateMock.getOnlineSchemaChangeParameter(Mockito.any())).thenReturn(onlineSchemaChangeParameters);
 when(organizationServiceMock.getCreator(Mockito.anyLong())).thenReturn(Mockito.mock(User.class));
 when(quartzJobServiceMock.createJob(Mockito.any(), Mockito.any()))
 .thenAnswer(invocation -> invocation.getArgument(0));
 when(scheduleTaskServiceMock.listTask(Mockito.any(Pageable.class), Mockito.eq(onlineSchemaChangeFlowableTask.getScheduleId()))).thenReturn(Mockito.mock(Page.class));
 when(taskServiceMock.detail(Mockito.anyLong())).thenReturn(Mockito.mock(TaskEntity.class));
 when(taskServiceMock.start(Mockito.anyLong())).thenReturn(null);

 // When
 onlineSchemaChangeFlowableTask.start(1L, taskServiceMock, Mockito.mock(DelegateExecution.class));

 // Then
 verify(taskServiceMock).start(1L);
 }

 @Test
 public void testIsSuccessful() {
 // Given
 OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult(ListUtils.emptyList());
 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setResultJson(JsonUtils.toJson(onlineSchemaChangeTaskResult));
 taskEntity.setStatus(TaskStatus.DONE);

 // When
 TaskStatus result = onlineSchemaChangeFlowableTask.isSuccessful();

 // Then
 assertEquals(TaskStatus.DONE, result);
 }

 @Test
 public void testIsFailure() {
 // Given
 OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult(ListUtils.emptyList());
 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setResultJson(JsonUtils.toJson(onlineSchemaChangeTaskResult));
 taskEntity.setStatus(TaskStatus.FAILED);

 // When
 TaskStatus result = onlineSchemaChangeFlowableTask.isFailure();

 // Then
 assertEquals(TaskStatus.FAILED, result);
 }

 @Test
 public void testIsCancelled() {
 // Given
 OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult(ListUtils.emptyList());
 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setResultJson(JsonUtils.toJson(onlineSchemaChangeTaskResult));
 taskEntity.setStatus(TaskStatus.CANCELED);

 // When
 boolean result = onlineSchemaChangeFlowableTask.isCancelled();

 // Then
 assertTrue(result);
 }

 @Test
 public void testCancel() throws Exception {
 // Given
 TaskEntity taskEntity = Mockito.mock(TaskEntity.class);
 when(taskServiceMock.detail(Mockito.anyLong())).thenReturn(taskEntity);
 when(taskServiceMock.cancel(Mockito.anyBoolean(), Mockito.anyLong(), Mockito.eq(taskServiceMock))).thenReturn(true);

 // When
 boolean result = onlineSchemaChangeFlowableTask.cancel(true, 1L, taskServiceMock);

 // Then
 assertTrue(result);
 }

 @Test
 public void testOnTimeout() {
 // Given
 // When and Then
 // These methods do not need to be tested as they do not contain any logic
 }

 @Test
 public void testOnProgressUpdate() {
 // Given
 Page<ScheduleTaskEntity> scheduleTasks = Mockito.mock(Page.class);
 when(scheduleTaskServiceMock.listTask(Mockito.any(Pageable.class), Mockito.eq(onlineSchemaChangeFlowableTask.getScheduleId()))).thenReturn(scheduleTasks);
 when(scheduleTasks.getSize()).thenReturn(2);

 // When
 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskServiceMock);

 // Then
 TaskResult result = JsonUtils.parseObject(JsonUtils.fromJsonString(taskServiceMock.detail(1L).getResultJson()), TaskResult.class);
 assertEquals(TaskStatus.DONE, result.getStatus());
 }

 @Test
 public void testCreateScheduleEntity() {
 // Given

 // When
 ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfigMock, onlineSchemaChangeParametersMock, schema);

 // Then
 assertNotNull(scheduleEntity);
 }

 @Test
 public void testCreateScheduleTaskEntity() {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
 when(scheduleTaskRepositoryMock.saveAndFlush(Mockito.any())).thenReturn(scheduleTaskEntity);
 OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class);

 // When
 ScheduleTaskEntity result = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(onlineSchemaChangeFlowableTask.getScheduleId(), onlineSchemaChangeScheduleTaskParameters);

 // Then
 assertNotNull(result);
 verify(scheduleTaskRepositoryMock).saveAndFlush(Mockito.any());
 }
}