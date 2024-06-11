import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.RandomUtils;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.impl.test.FlowableTestBase;
import org.flowable.engine.impl.test.annotation.FlowableTest;
import org.flowable.engine.impl.test.annotation.RegisterExtension;
import org.flowable.engine.impl.test.extension.FlowableTestExtension;
import org.flowable.engine.impl.test.util.TestConnectionFactory;
import org.flowable.engine.test.delegate.TestDelegateExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@FlowableTest
@RegisterExtension(FlowableTestExtension.class)
public class OnlineSchemaChangeFlowableTaskTest extends FlowableTestBase {

 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
 private TestConnectionFactory testConnectionFactory;
 private OrganizationService organizationService;
 private QuartzJobService quartzJobService;
 private ScheduleTaskService scheduleTaskService;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ScheduleService scheduleService;
 private BaseODCFlowTaskDelegate<?, ?> baseODCFlowTaskDelegate;
 private TaskService taskService;

 @BeforeEach
 public void setup() {
 testConnectionFactory = new TestConnectionFactory();
 organizationService = mock(OrganizationService.class);
 quartzJobService = mock(QuartzJobService.class);
 scheduleTaskService = mock(ScheduleTaskService.class);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 scheduleService = mock(ScheduleService.class);
 baseODCFlowTaskDelegate = mock(BaseODCFlowTaskDelegate.class);
 taskService = Mockito.spy(new TaskServiceImpl(context()));
 context().setBean("taskService", taskService);
 context().setBean("onlineSchemaChangeFlowableTask", OnlineSchemaChangeFlowableTask.class);
 onlineSchemaChangeFlowableTask = (OnlineSchemaChangeFlowableTask) context().getBean("onlineSchemaChangeFlowableTask");
 }

 @Test
 public void testStartMethod() {
 // Given
 Long taskId = 1L;
 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setId(taskId);
 TaskStatus expectedStatus = TaskStatus.RUNNING;
 ConnectionConfig connectionConfig = testConnectionFactory.createConnectionConfig();
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 String schema = "test_schema";
 OnlineSchemaChangeContextHolder.init(taskEntity.getId(), taskEntity.getOrganizationId(), taskEntity.getCreatorId());
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(scheduleEntity);
 when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(null);
 when(quartzJobService.start(anyLong(), anyLong())).thenReturn(true);
 when(taskService.detail(taskId)).thenReturn(taskEntity);
 when(organizationService.getUserById(taskEntity.getCreatorId())).thenReturn(Mockito.mock(User.class));
 when(organizationService.getOrganizationById(taskEntity.getOrganizationId())).thenReturn(Mockito.mock(User.class));

 // When
 OnlineSchemaChangeContextHolder.trace(taskEntity.getCreatorId(), taskId, taskEntity.getOrganizationId());
 OnlineSchemaChangeParameters parameter = OnlineSchemaChangeParameters.builder().build();
 OnlineSchemaChangeContextHolder.setOnlineSchemaChangeParameters(parameter);
 OnlineSchemaChangeContextHolder.setContinueOnError(true);
 OnlineSchemaChangeContextHolder.setSchemaName(schema);
 OnlineSchemaChangeContextHolder.setConnectionConfig(connectionConfig);
 OnlineSchemaChangeContextHolder.setUid(FlowTaskUtil.getCloudMainAccountId(any()));

 onlineSchemaChangeFlowableTask.start(taskId, taskService, context().getExecution());

 // Then
 verify(taskService, times(1)).start(taskId);
 verify(quartzJobService, times(1)).start(scheduleEntity.getId(), tasks.get(0).getId());
 assertEquals(expectedStatus, taskEntity.getStatus());
 }

 @Test
 public void testIsSuccessfulMethod() {
 // Given
 Long taskId = 1L;
 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setId(taskId);
 taskEntity.setStatus(TaskStatus.DONE);

 // When
 boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

 // Then
 assertTrue(result);
 }

 @Test
 public void testIsFailureMethod() {
 // Given
 Long taskId = 1L;
 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setId(taskId);
 taskEntity.setStatus(TaskStatus.FAILED);

 // When
 boolean result = onlineSchemaChangeFlowableTask.isFailure();

 // Then
 assertTrue(result);
 }

 @Test
 public void testOnFailureMethod() {
 // Given
 Long taskId = 1L;
 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setId(taskId);

 // When
 onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

 // Then
 verify(taskService, times(1)).onFailure(taskId, any());
 }

 @Test
 public void testOnSuccessfulMethod() {
 // Given
 Long taskId = 1L;
 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setId(taskId);

 // When
 onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

 // Then
 verify(taskService, times(1)).onSuccessful(taskId, any());
 updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
 }

 @Test
 public void testOnTimeoutMethod() {
 // Given
 Long taskId = 1L;
 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setId(taskId);

 // When
 onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

 // Then
 verify(taskService, times(1)).onTimeout(taskId, any());
 }

 @Test
 public void testCancelMethod() {
 // Given
 Long taskId = 1L;
 Long taskInstanceId = 1L;
 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setId(taskId);
 taskEntity.setStatus(TaskStatus.RUNNING);
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
 when(scheduleTaskRepository.listTask(any(Pageable.class), eq(scheduleEntity.getId()))).thenReturn(Collections.singletonList(scheduleTaskEntity));
 when(scheduleTaskService.listTask(any(Pageable.class), eq(scheduleEntity.getId()))).thenReturn(Collections.singletonList(scheduleTaskEntity));
 when(scheduleService.getScheduleEntityById(eq(scheduleEntity.getId()))).thenReturn(scheduleEntity);
 when(taskService.cancel(eq(taskId), anyBoolean(), any(TaskService.class))).thenReturn(true);

 // When
 boolean result = onlineSchemaChangeFlowableTask.cancel(true, taskId, taskService);

 // Then
 assertTrue(result);
 verify(taskService, times(1)).cancel(taskId, true, any());
 }

 @Test
 public void testIsCancelledMethod() {
 // Given
 Long taskId = 1L;
 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setId(taskId);
 taskEntity.setStatus(TaskStatus.CANCELED);

 // When
 boolean result = onlineSchemaChangeFlowableTask.isCancelled();

 // Then
 assertTrue(result);
 }

 @Test
 public void testCreateScheduleEntityMethod() {
 // Given
 ConnectionConfig connectionConfig = testConnectionFactory.createConnectionConfig();
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 String schema = "test_schema";

 // When
 ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schema);

 // Then
 assertNotNull(scheduleEntity);
 }

 @Test
 public void testCreateScheduleTaskEntityMethod() {
 // Given
 Long scheduleId = 1L;
 OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = OnlineSchemaChangeScheduleTaskParameters.builder().build();

 // When
 ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, onlineSchemaChangeScheduleTaskParameters);

 // Then
 assertNotNull(scheduleTaskEntity);
 }
}