import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstances;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class OnlineSchemaChangeFlowableTaskTest {

 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
 @MockBean
 private QuartzJobService quartzJobService;
 @MockBean
 private ScheduleTaskRepository scheduleTaskRepository;
 @MockBean
 private TaskService taskService;
 @MockBean
 private OrganizationService organizationService;
 @MockBean
 private ScheduleService scheduleService;
 @MockBean
 private ScheduleTaskService scheduleTaskService;

 @Autowired
 private FlowableTestUtil flowableTestUtil;

 @BeforeEach
 public void init() {
 onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
 onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
 onlineSchemaChangeFlowableTask.setTaskService(taskService);
 onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
 onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
 onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
 }

 @Test
 public void testStart() throws Exception {
 // given
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setId("connectionId");
 OnlineSchemaChangeContextHolder.setConnectionConfig(connectionConfig);
 OnlineSchemaChangeContextHolder.setCreatorId(1L);
 OnlineSchemaChangeContextHolder.setOrganizationId(1L);
 OnlineSchemaChangeContextHolder.setFlowInstanceId(1L);

 doReturn(new ProcessInstance()).when(taskService).detail(1L);
 doReturn(new ProcessInstances()).when(taskService).listRunningProcessInstances();
 doReturn(connectionConfig).when(organizationService).getConnectionConfig("connectionId");
 doReturn(Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class)).when(onlineSchemaChangeFlowableTask)
 .generateScheduleTaskParameters(any(ConnectionConfig.class), anyString());
 doReturn(Mockito.mock(ScheduleEntity.class)).when(scheduleService).create(any(ScheduleEntity.class));
 doReturn(Mockito.mock(ScheduleTaskEntity.class)).when(scheduleTaskRepository).saveAndFlush(any(ScheduleTaskEntity.class));

 // when
 onlineSchemaChangeFlowableTask.start(1L, taskService, Mockito.mock(DelegateExecution.class));

 // then
 verify(quartzJobService).start(anyLong(), anyLong());
 }

 @Test
 public void testIsSuccessful() {
 // given
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = Mockito.spy(new OnlineSchemaChangeFlowableTask());
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);

 // when & then
 Assertions.assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
 }

 @Test
 public void testIsFailure() {
 // given
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = Mockito.spy(new OnlineSchemaChangeFlowableTask());
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);

 // when & then
 Assertions.assertTrue(onlineSchemaChangeFlowableTask.isFailure());
 }

 @Test
 public void testIsCancelled() {
 // given
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = Mockito.spy(new OnlineSchemaChangeFlowableTask());
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);

 // when & then
 Assertions.assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
 }

 @Test
 public void testOnFailure() {
 // given
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = Mockito.spy(new OnlineSchemaChangeFlowableTask());
 doThrow(new RuntimeException("Test error")).when(taskService).cancel(1L, true, taskService);
 OnlineSchemaChangeContextHolder.setFlowInstanceId(1L);

 // when
 onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

 // then
 verify(taskService, times(1)).cancel(1L, true, taskService);
 }

 @Test
 public void testOnSuccessful() {
 // given
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = Mockito.spy(new OnlineSchemaChangeFlowableTask());
 ProcessInstance processInstance = Mockito.mock(ProcessInstance.class);
 doReturn(processInstance).when(taskService).detail(1L);
 doReturn(processInstance).when(taskService).update(any(ProcessInstance.class));
 OnlineSchemaChangeContextHolder.setFlowInstanceId(1L);

 // when
 onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

 // then
 verify(taskService, times(1)).update(processInstance);
 }

 @Test
 public void testOnTimeout() {
 // given
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = Mockito.spy(new OnlineSchemaChangeFlowableTask());
 // when
 onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);

 // then
 // no action required
 }

 @Test
 public void testCancel() throws Exception {
 // given
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = Mockito.spy(new OnlineSchemaChangeFlowableTask());
 long scheduleId = 1L;
 doReturn(true).when(onlineSchemaChangeFlowableTask).cancel(false, 1L, taskService);
 doReturn(1L).when(scheduleTaskService).listTask(any(Pageable.class), eq(scheduleId)).getSize();
 doReturn(ListUtils.emptyIfNull(ListUtils.emptyList())).when(scheduleTaskService).listTask(any(Pageable.class), eq(scheduleId));
 doReturn(null).when(scheduleService).findById(scheduleId);

 // when
 onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

 // then
 verify(taskService, times(1)).cancel(1L, true, taskService);
 verify(quartzJobService, times(1)).terminate(anyLong(), anyString());
 }

 @Test
 public void testIsCancelledWithRunningTask() {
 // given
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
 doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(1L);
 doReturn(TaskStatus.RUNNING).when(scheduleTaskEntity).getStatus();

 // when
 boolean isCancelled = onlineSchemaChangeFlowableTask.isCancelled();

 // then
 Assertions.assertTrue(isCancelled);
 }

 @Test
 public void testGenerateScheduleTaskParameters() {
 // given
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setId("connectionId");
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeParameters.setContinueOnError(false);
 OnlineSchemaChangeScheduleTaskParameters expectedScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters("userId", "databaseName", "schemaName");
 String schemaName = "schemaName";

 // when
 OnlineSchemaChangeScheduleTaskParameters actualScheduleTaskParameters = onlineSchemaChangeFlowableTask
 .generateScheduleTaskParameters(connectionConfig, schemaName);

 // then
 Assertions.assertEquals(expectedScheduleTaskParameters, actualScheduleTaskParameters);
 }

 @Test
 public void testCreateScheduleEntity() throws Exception {
 // given
 ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 String schemaName = RandomStringUtils.randomAlphanumeric(10);
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

 // when
 ScheduleEntity actualScheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schemaName);
 ScheduleEntity expectedScheduleEntity = new ScheduleEntity();
 expectedScheduleEntity.setConnectionId(connectionConfig.getId());
 expectedScheduleEntity.setDatabaseName(schemaName);
 expectedScheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 expectedScheduleEntity.setStatus(ScheduleStatus.ENABLED);
 expectedScheduleEntity.setAllowConcurrent(false);
 expectedScheduleEntity.setCreatorId(1L);
 expectedScheduleEntity.setOrganizationId(1L);

 // then
 Assertions.assertEquals(expectedScheduleEntity, actualScheduleEntity);
 }

 @Test
 public void testCreateScheduleTaskEntity() throws Exception {
 // given
 Long scheduleId = 1L;
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters("userId", "databaseName", "schemaName");

 // when
 ScheduleTaskEntity actualScheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, onlineSchemaChangeScheduleTaskParameters);
 ScheduleTaskEntity expectedScheduleTaskEntity = new ScheduleTaskEntity();
 expectedScheduleTaskEntity.setStatus(TaskStatus.PREPARING);
 expectedScheduleTaskEntity.setJobName(scheduleId.toString());
 expectedScheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
 expectedScheduleTaskEntity.setProgressPercentage(0.0);
 expectedScheduleTaskEntity.setParametersJson(JsonUtils.toJson(onlineSchemaChangeScheduleTaskParameters));
 expectedScheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(onlineSchemaChangeScheduleTaskParameters)));
 expectedScheduleTaskEntity.setFireTime(new Date());

 // then
 Assertions.assertEquals(expectedScheduleTaskEntity, actualScheduleTaskEntity);
 }
}