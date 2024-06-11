import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OnlineSchemaChangeFlowableTaskTest {

 @MockBean
 private ScheduleTaskRepository scheduleTaskRepository;
 @MockBean
 private ScheduleService scheduleService;
 @MockBean
 private ScheduleTaskService scheduleTaskService;
 @MockBean
 private QuartzJobService quartzJobService;
 @MockBean
 private OnlineSchemaChangeTaskHandler taskHandler;
 @MockBean
 private OscTaskCompleteHandler completeHandler;
 @MockBean
 private OrganizationService organizationService;

 @Autowired
 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

 private static final String CHECK_TASK_CRON_EXPRESSION = "0/10 * * * * ?";
 private static final String JOB_TYPE = "ONLINE_SCHEMA_CHANGE_COMPLETE";
 private static final String JOB_GROUP = JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name();
 private static final String SCHEDULE_ID = "1";
 private static final String FLOW_INSTANCE_ID = "1";
 private static final String TASK_ID = "1";
 private static final OnlineSchemaChangeParameters ONLINE_SCHEMA_CHANGE_PARAMETERS = new OnlineSchemaChangeParameters();
 private static final ScheduleEntity SCHEDULE_ENTITY = new ScheduleEntity();
 private static final ScheduleTaskEntity SCHEDULE_TASK_ENTITY = new ScheduleTaskEntity();
 private static final ConnectionConfig CONNECTION_CONFIG = Mockito.mock(ConnectionConfig.class);
 private DelegateExecution delegateExecution;

 @BeforeEach
 public void setUp() {
 delegateExecution = Mockito.mock(DelegateExecution.class);
 ReflectionTestUtils.setFieldValue(onlineSchemaChangeFlowableTask, "scheduleTaskRepository", scheduleTaskRepository);
 ReflectionTestUtils.setFieldValue(onlineSchemaChangeFlowableTask, "scheduleService", scheduleService);
 ReflectionTestUtils.setFieldValue(onlineSchemaChangeFlowableTask, "scheduleTaskService", scheduleTaskService);
 ReflectionTestUtils.setFieldValue(onlineSchemaChangeFlowableTask, "quartzJobService", quartzJobService);
 ReflectionTestUtils.setFieldValue(onlineSchemaChangeFlowableTask, "taskHandler", taskHandler);
 ReflectionTestUtils.setFieldValue(onlineSchemaChangeFlowableTask, "completeHandler", completeHandler);
 ReflectionTestUtils.setFieldValue(onlineSchemaChangeFlowableTask, "organizationService", organizationService);
 }

 @Test
 public void testStart() throws Exception {
 // Given
 when(scheduleService.create(SCHEDULE_ENTITY)).thenReturn(SCHEDULE_ENTITY);
 when(scheduleTaskService.listTask(Pageable.unpaged(), SCHEDULE_ID)).thenReturn(Page.empty());
 when(taskHandler.start(SCHEDULE_ID, SCHEDULE_TASK_ENTITY.getId())).thenReturn(null);

 // When
 onlineSchemaChangeFlowableTask.start(FLOW_INSTANCE_ID, new TaskService() {
 @Override
 public void start(String id) {
 // empty implementation
 }
 }, delegateExecution);

 // Then
 verify(scheduleService, times(1)).create(SCHEDULE_ENTITY);
 verify(scheduleTaskService, times(1)).listTask(Pageable.unpaged(), SCHEDULE_ID);
 verify(taskHandler, times(1)).start(SCHEDULE_ID, SCHEDULE_TASK_ENTITY.getId());
 }

 @Test
 public void testIsSuccessful() {
 // Given
 ReflectionTestUtils.setFieldValue(onlineSchemaChangeFlowableTask, "status", TaskStatus.DONE);

 // When
 // Then
 assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
 }

 @Test
 public void testIsFailure() {
 // Given
 ReflectionTestUtils.setFieldValue(onlineSchemaChangeFlowableTask, "status", TaskStatus.FAILED);

 // When
 // Then
 assertTrue(onlineSchemaChangeFlowableTask.isFailure());
 }

 @Test
 public void testOnFailure() {
 // Given
 when(taskService.detail(TASK_ID)).thenReturn(new TaskEntity());

 // When
 onlineSchemaChangeFlowableTask.onFailure(TASK_ID, new TaskService() {
 @Override
 public void onFailure(String id, Throwable cause) {
 // empty implementation
 }
 });

 // Then
 verify(log, times(1)).warn(String.format("Online schema change task failed, taskId=%s", TASK_ID));
 }

 @Test
 public void testOnSuccessful() {
 // Given
 when(taskService.detail(TASK_ID)).thenReturn(new TaskEntity());

 // When
 onlineSchemaChangeFlowableTask.onSuccessful(TASK_ID, new TaskService() {
 @Override
 public void update(TaskEntity task) {
 // empty implementation
 }
 });

 // Then
 verify(log, times(1)).info(String.format("Online schema change task succeed, taskId=%s", TASK_ID));
 verify(taskService, times(1)).update(Mockito.any(TaskEntity.class));
 }

 @Test
 public void testOnTimeout() {
 // Given
 when(taskService.detail(TASK_ID)).thenReturn(new TaskEntity());

 // When
 onlineSchemaChangeFlowableTask.onTimeout(TASK_ID, new TaskService() {
 @Override
 public void onTimeout(String id) {
 // empty implementation
 }
 });

 // Then
 verify(log, times(1)).warn(String.format("Online schema change task timeout, taskId=%s", TASK_ID));
 }

 @Test
 public void testProgressUpdate() {
 // Given
 Page<ScheduleTaskEntity> tasks = ListUtils.emptyIfNull(ListUtils.emptyList()).toImmutablePage(Pageable.unpaged());

 when(scheduleTaskService.listTask(Pageable.unpaged(), SCHEDULE_ID)).thenReturn(tasks);
 double expectedPercentage = 33.3;
 when(singleTaskPercentage(Mockito.any(ScheduleTaskEntity.class))).thenReturn(expectedPercentage);

 // When
 onlineSchemaChangeFlowableTask.onProgressUpdate(FLOW_INSTANCE_ID, new TaskService() {
 @Override
 public void update(TaskEntity task) {
 // empty implementation
 }
 });

 // Then
 verify(taskService, times(1)).update(Mockito.any(TaskEntity.class));
 TaskEntity flowTask = Mockito.any(TaskEntity.class);
 verify(flowTask).setProgressPercentage(expectedPercentage * 100);
 }

 @Test
 public void testCancel() {
 // Given
 when(scheduleTaskService.listTask(Pageable.unpaged(), SCHEDULE_ID)).thenReturn(Page.empty());

 // When
 onlineSchemaChangeFlowableTask.cancel(true, FLOW_INSTANCE_ID, new TaskService() {
 @Override
 public void cancel(String id) {
 // empty implementation
 }
 });

 // Then
 verify(taskHandler, times(1)).terminate(SCHEDULE_ID, SCHEDULE_TASK_ENTITY.getId());
 verify(onlineSchemaChangeFlowableTask, times(1)).status = TaskStatus.CANCELED;
 verify(taskService, times(1)).cancel(FLOW_INSTANCE_ID);
 }

 @Test
 public void testIsCancelled() {
 // Given
 ReflectionTestUtils.setFieldValue(onlineSchemaChangeFlowableTask, "status", TaskStatus.CANCELED);

 // When
 // Then
 assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
 }
}