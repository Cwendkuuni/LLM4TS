import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OnlineSchemaChangeFlowableTaskTest {

 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
 private ScheduleTaskRepository scheduleTaskRepository;
 private TaskService taskService;
 private QuartzJobService quartzJobService;
 private ScheduleService scheduleService;
 private ScheduleTaskService scheduleTaskService;
 private OrganizationService organizationService;

 @BeforeEach
 public void setup() {
 this.onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 this.scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 this.taskService = mock(TaskService.class);
 this.quartzJobService = mock(QuartzJobService.class);
 this.scheduleService = mock(ScheduleService.class);
 this.scheduleTaskService = mock(ScheduleTaskService.class);
 this.organizationService = mock(OrganizationService.class);

 this.onlineSchemaChangeFlowableTask.setScheduleTaskRepository(this.scheduleTaskRepository);
 this.onlineSchemaChangeFlowableTask.setTaskService(this.taskService);
 this.onlineSchemaChangeFlowableTask.setQuartzJobService(this.quartzJobService);
 this.onlineSchemaChangeFlowableTask.setScheduleService(this.scheduleService);
 this.onlineSchemaChangeFlowableTask.setScheduleTaskService(this.scheduleTaskService);
 this.onlineSchemaChangeFlowableTask.setOrganizationService(this.organizationService);
 }

 @AfterEach
 public void cleanup() {
 if (this.onlineSchemaChangeFlowableTask != null) {
 this.onlineSchemaChangeFlowableTask = null;
 }
 }

 @Test
 public void testStartMethod() throws Exception {
 // Given
 User creator = Mockito.mock(User.class);
 ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
 OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
 String schema = "test";
 when(this.taskService.detail(any(Long.class))).thenReturn(Mockito.mock(TaskEntity.class));
 when(this.organizationService.getUserById(any(Long.class))).thenReturn(creator);
 when(this.scheduleService.create(any(ScheduleEntity.class))).thenReturn(Mockito.mock(ScheduleEntity.class));
 when(this.quartzJobService.scheduleJob(any(JobType.class), any(String.class), any(String.class), any(Date.class), any(TriggerConfig.class), any(MisfireStrategy.class)))
 .thenReturn(Mockito.mock(String.class));
 when(this.scheduleTaskService.generateSubTaskParameters(any(ConnectionConfig.class), any(String.class)))
 .thenReturn(Arrays.asList(new OnlineSchemaChangeScheduleTaskParameters()));

 // When
 this.onlineSchemaChangeFlowableTask.start(1L, this.taskService, Mockito.mock(DelegateExecution.class));

 // Then
 Mockito.verify(this.taskService, Mockito.times(1)).start(1L);
 assertNotNull(this.onlineSchemaChangeFlowableTask.getStatus());
 assertNotNull(this.onlineSchemaChangeFlowableTask.getScheduleId());
 assertNotNull(this.onlineSchemaChangeFlowableTask.getCreatorId());
 assertNotNull(this.onlineSchemaChangeFlowableTask.getOrganizationId());
 assertFalse(this.onlineSchemaChangeFlowableTask.isCancelled());
 }

 @Test
 public void testIsFailure() {
 // Given
 this.onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);

 // Then
 assertTrue(this.onlineSchemaChangeFlowableTask.isFailure());
 }

 @Test
 public void testIsSuccessful() {
 // Given
 this.onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);

 // Then
 assertTrue(this.onlineSchemaChangeFlowableTask.isSuccessful());
 }

 @Test
 public void testCancelMethod() {
 // Given
 when(this.taskService.cancel(any(Long.class), any(TaskService.class))).thenReturn(true);

 // When
 boolean cancel = this.onlineSchemaChangeFlowableTask.cancel(true, 1L, this.taskService);

 // Then
 assertTrue(cancel);
 }

 @Test
 public void testIsCancelled() {
 // Given
 this.onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);

 // Then
 assertTrue(this.onlineSchemaChangeFlowableTask.isCancelled());
 }

 @Test
 public void testListTasksMethod() {
 // Given
 Pageable pageable = Pageable.unpaged();
 Page<ScheduleTaskEntity> tasks = Page.empty();
 when(this.scheduleTaskService.listTask(pageable, this.onlineSchemaChangeFlowableTask.getScheduleId()))
 .thenReturn(tasks);

 // When
 List<ScheduleTaskEntity> result = this.onlineSchemaChangeFlowableTask.listTasks();

 // Then
 assertNull(result);
 }

 @Test
 public void testListTasksWithDataMethod() {
 // Given
 Page<ScheduleTaskEntity> tasks = Page.of(new ScheduleTaskEntity(), new ScheduleTaskEntity());
 when(this.scheduleTaskService.listTask(Pageable.unpaged(), this.onlineSchemaChangeFlowableTask.getScheduleId()))
 .thenReturn(tasks);

 // When
 List<ScheduleTaskEntity> result = this.onlineSchemaChangeFlowableTask.listTasks();

 // Then
 assertEquals(2, result.size());
 }

 @Test
 public void testUpdateFlowInstanceStatusMethod() {
 // Given
 when(this.taskService.update(any(TaskEntity.class))).thenReturn(Mockito.mock(TaskEntity.class));

 // When
 this.onlineSchemaChangeFlowableTask.updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);

 // Then
 Mockito.verify(this.taskService, Mockito.times(1)).update(any(TaskEntity.class));
 }

 @Test
 public void testProgressStatusUpdateMethod() {
 // Given
 Page<ScheduleTaskEntity> tasks = Page.of(new ScheduleTaskEntity(), new ScheduleTaskEntity(), new ScheduleTaskEntity());

 // When
 this.onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);

 // Then
 assertEquals(TaskStatus.DONE, this.onlineSchemaChangeFlowableTask.getStatus());
 }

 @Test
 public void testSingleTaskPercentageMethod() {
 // Given
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setStatus(TaskStatus.RUNNING);
 scheduleTask.setProgressPercentage(75);

 // When
 double result = this.onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTask);

 // Then
 assertEquals(0.75, result);
 }

 @Test
 public void testCreateScheduleEntityMethod() {
 // Given
 ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
 OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
 String schema = "test";

 // When
 ScheduleEntity result = this.onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);

 // Then
 assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, result.getJobType());
 assertNotNull(result.getDatabaseName());
 assertNotNull(result.getConnectionId());
 assertNotNull(result.getJobParametersJson());
 assertNotNull(result.getTriggerConfigJson());
 }

 @Test
 public void testCreateScheduleTaskEntityMethod() {
 // Given
 Long scheduleId = 1L;
 OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

 // When
 ScheduleTaskEntity result = this.onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

 // Then
 assertNotNull(result.getJobName());
 assertNotNull(result.getJobGroup());
 }

 @Test
 public void testOnFailureMethod() {
 // Given
 when(this.taskService.cancel(any(Long.class), any(TaskService.class))).thenThrow(new RuntimeException("Test error"));

 // When
 assertThrows(RuntimeException.class, () -> this.onlineSchemaChangeFlowableTask.onFailure(1L, this.taskService));
 }

 @Test
 public void testOnSuccessfulMethod() {
 // Given
 when(this.taskService.update(any(TaskEntity.class))).thenReturn(Mockito.mock(TaskEntity.class));

 // When
 this.onlineSchemaChangeFlowableTask.onSuccessful(1L, this.taskService);

 // Then
 Mockito.verify(this.taskService, Mockito.times(1)).update(any(TaskEntity.class));
 }

 @Test
 public void testOnTimeoutMethod() {
 // Given

 // When
 this.onlineSchemaChangeFlowableTask.onTimeout(1L, this.taskService);

 // Then
 // Empty
 }

 @Test
 public void testOnProgressUpdateMethodWithEmptyList() {
 // Given
 Page<ScheduleTaskEntity> tasks = Page.empty();

 // When
 this.onlineSchemaChangeFlowableTask.onProgressUpdate(1L, this.taskService);

 // Then
 // Empty
 }

 @Test
 public void testOnProgressUpdateMethodWithData() {
 // Given
 Page<ScheduleTaskEntity> tasks = Page.of(new ScheduleTaskEntity(), new ScheduleTaskEntity(), new ScheduleTaskEntity());

 // When
 this.onlineSchemaChangeFlowableTask.onProgressUpdate(1L, this.taskService);

 // Then
 assertEquals(TaskStatus.DONE, this.onlineSchemaChangeFlowableTask.getStatus());
 }

 @Test
 public void testCancelMethodWithRunningTask() {
 // Given
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setStatus(TaskStatus.RUNNING);
 when(this.quartzJobService.terminate(any(String.class), anyString())).thenReturn(false);

 // When
 boolean cancel = this.onlineSchemaChangeFlowableTask.cancel(true, 1L, this.taskService);

 // Then
 assertFalse(cancel);
 }

 @Test
 public void testCancelMethodWithSuccessTask() {
 // Given
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setStatus(TaskStatus.DONE);
 when(this.quartzJobService.terminate(any(String.class), anyString())).thenReturn(true);

 // When
 boolean cancel = this.onlineSchemaChangeFlowableTask.cancel(true, 1L, this.taskService);

 // Then
 assertFalse(cancel);
 }

 @Test
 public void testCancelMethodWithFailedTask() {
 // Given
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setStatus(TaskStatus.FAILED);
 when(this.quartzJobService.terminate(any(String.class), anyString())).thenReturn(true);

 // When
 boolean cancel = this.onlineSchemaChangeFlowableTask.cancel(true, 1L, this.taskService);

 // Then
 assertTrue(cancel);
 }

}