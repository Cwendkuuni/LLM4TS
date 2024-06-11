import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@SpringBootTest
@SpringJUnitConfig(OnlineSchemaChangeFlowableTaskTest.TestConfig.class)
@ActiveProfiles("test")
public class OnlineSchemaChangeFlowableTaskTest {

 private static final String TEST_SCHEMA = "test_schema";
 private static final String TEST_UID = "test_uid";

 @Autowired
 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
 @Autowired
 private QuartzJobService quartzJobService;
 @Autowired
 private ScheduleTaskService scheduleTaskService;
 @Autowired
 private OrganizationService organizationService;
 @Value("${checkTaskCronExpression}")
 private String checkTaskCronExpression;

 @BeforeEach
 public void setup() {
 // Initialize mocks
 quartzJobService = mock(QuartzJobService.class);
 scheduleTaskService = mock(ScheduleTaskService.class);
 organizationService = mock(OrganizationService.class);

 // Initialize test variables
 OnlineSchemaChangeParameters testParameter = new OnlineSchemaChangeParameters();
 testParameter.setContinueOnError(true);
 }

 @Test
 void testStart() {
 // Given
 ProcessInstance processInstance = Mockito.mock(ProcessInstance.class);
 User testCreator = Mockito.mock(User.class);
 TaskEntity testFlowTask = Mockito.mock(TaskEntity.class);
 given(quartzJobService.createScheduleEntity(any(ConnectionConfig.class), any(OnlineSchemaChangeParameters.class), TEST_SCHEMA))
 .willReturn(Mockito.mock(ScheduleEntity.class));
 given(organizationService.getUserById(anyLong()))
 .willReturn(testCreator);
 given(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class)))
 .willReturn(Mockito.mock(ScheduleTaskEntity.class));
 given(scheduleTaskService.listTask(any(Pageable.class), anyLong())).willReturn(Page.empty());
 given(taskService.start(anyLong()))
 .willAnswer(invocation -> {
 TaskEntity returnedTaskEntity = invocation.getArgument(0);
 returnedTaskEntity.setStatus(TaskStatus.RUNNING);
 return null;
 });
 given(taskService.detail(anyLong()))
 .willReturn(testFlowTask);

 // When
 onlineSchemaChangeFlowableTask.start(1L, taskService, processInstance);

 // Then
 then(quartzJobService).should().createScheduleEntity(any(ConnectionConfig.class), any(OnlineSchemaChangeParameters.class), TEST_SCHEMA);
 then(taskService).should().start(1L);
 }

 @Test
 void testIsSuccessful() {
 // Given
 TaskEntity testFlowTask = Mockito.mock(TaskEntity.class);
 given(taskService.detail(anyLong()))
 .willReturn(testFlowTask);
 given(testFlowTask.getStatus())
 .willReturn(TaskStatus.DONE);

 // When
 boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

 // Then
 Assertions.assertTrue(result);
 }

 @Test
 void testIsFailure() {
 // Given
 TaskEntity testFlowTask = Mockito.mock(TaskEntity.class);
 given(taskService.detail(anyLong()))
 .willReturn(testFlowTask);
 given(testFlowTask.getStatus())
 .willReturn(TaskStatus.FAILED);

 // When
 boolean result = onlineSchemaChangeFlowableTask.isFailure();

 // Then
 Assertions.assertTrue(result);
 }

 @Test
 void testOnFailure() {
 // Given
 ProcessInstance processInstance = Mockito.mock(ProcessInstance.class);
 given(taskService.detail(anyLong()))
 .willReturn(Mockito.mock(TaskEntity.class));
 given(taskService.cancel(anyBoolean(), anyLong(), any()))
 .willAnswer(invocation -> null);

 // When
 onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

 // Then
 then(taskService).should().cancel(true, 1L, any());
 }

 @Test
 void testOnSuccessful() {
 // Given
 ProcessInstance processInstance = Mockito.mock(ProcessInstance.class);
 given(taskService.detail(anyLong()))
 .willReturn(Mockito.mock(TaskEntity.class));
 given(taskService.update(any()))
 .willAnswer(invocation -> null);
 given(updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED))
 .willReturn(true);

 // When
 onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

 // Then
 then(taskService).should().update(any());
 then(taskService).should().update(any());
 }

 @Test
 void testOnTimeout() {
 // Given
 ProcessInstance processInstance = Mockito.mock(ProcessInstance.class);
 given(taskService.detail(anyLong()))
 .willReturn(Mockito.mock(TaskEntity.class));

 // When
 onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);

 // Then
 // No verification needed as onTimeout doesn't have any interaction with mocks
 }

 @ParameterizedTest
 @MethodSource("providerForSingleTaskPercentage")
 void testOnProgressUpdate(double expectedPercentage, double taskPercentage) {
 // Given
 TaskEntity testFlowTask = Mockito.mock(TaskEntity.class);
 given(testFlowTask.getStatus())
 .willReturn(TaskStatus.RUNNING);
 given(taskService.detail(anyLong()))
 .willReturn(testFlowTask);
 given(singleTaskPercentage(any()))
 .willReturn(taskPercentage);

 // When
 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

 // Then
 then(taskService).should().update(any());
 Assertions.assertEquals(expectedPercentage, onlineSchemaChangeFlowableTask.percentage);
 }

 @Test
 void testCancel() {
 // Given
 ProcessInstance processInstance = Mockito.mock(ProcessInstance.class);
 given(taskService.detail(anyLong()))
 .willReturn(Mockito.mock(TaskEntity.class));
 given(quartzJobService.terminate(anyLong(), anyLong()))
 .willAnswer(invocation -> null);

 // When
 onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

 // Then
 then(taskService).should().cancel(true, 1L, any());
 then(quartzJobService).should().terminate(anyLong(), anyLong());
 }

 @Test
 void testIsCancelled() {
 // Given
 TaskEntity testFlowTask = Mockito.mock(TaskEntity.class);
 given(testFlowTask.getStatus())
 .willReturn(TaskStatus.CANCELED);

 // When
 boolean result = onlineSchemaChangeFlowableTask.isCancelled();

 // Then
 Assertions.assertTrue(result);
 }

 @Test
 void testCreateScheduleEntity() {
 // Given
 ConnectionConfig testConnectionConfig = Mockito.mock(ConnectionConfig.class);
 OnlineSchemaChangeParameters testParameter = Mockito.mock(OnlineSchemaChangeParameters.class);
 String testSchema = "test_schema";

 // When
 ScheduleEntity result = onlineSchemaChangeFlowableTask.createScheduleEntity(testConnectionConfig, testParameter, testSchema);

 // Then
 Assertions.assertNotNull(result);
 }

 @Test
 void testCreateScheduleTaskEntity() {
 // Given
 Long scheduleId = 1L;
 OnlineSchemaChangeScheduleTaskParameters testParam = Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class);

 // When
 ScheduleTaskEntity result = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, testParam);

 // Then
 Assertions.assertNotNull(result);
 }

 private static Stream<Arguments> providerForSingleTaskPercentage() {
 return Stream.of(
 Arguments.arguments(0.0, 0),
 Arguments.arguments(100.0, 100),
 Arguments.arguments(50.0, 50)
 );
 }
}

class TestConfig {
}