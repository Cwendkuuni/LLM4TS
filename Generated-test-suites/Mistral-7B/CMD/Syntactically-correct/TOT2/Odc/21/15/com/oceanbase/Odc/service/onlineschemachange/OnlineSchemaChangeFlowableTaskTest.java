import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils; import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.Class)
public class OnlineSchemaChangeFlowableTaskTest {

 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
 @MockBean
 private ConnectionConfig connectionConfig;
 @MockBean
 private QuartzJobService quartzJobService;
 @MockBean
 private ScheduleService scheduleService;
 @MockBean
 private ScheduleTaskRepository scheduleTaskRepository;
 @MockBean
 private ScheduleTaskService scheduleTaskService;
 @MockBean
 private OrganizationService organizationService;
 @MockBean
 private TaskService taskService;
 @MockBean
 private BaseODCFlowTaskDelegate baseODCFlowTaskDelegate;
 @MockBean
 private OscTaskCompleteHandler oscTaskCompleteHandler;

 @Autowired
 private FlowTaskUtil flowTaskUtil;
 @Autowired
 private JsonUtils jsonUtils;

 @Before
 public void setUp() {
 onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
 onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
 onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
 onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
 onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
 onlineSchemaChangeFlowableTask.setTaskService(taskService);
 onlineSchemaChangeFlowableTask.setBaseODCFlowTaskDelegate(baseODCFlowTaskDelegate);
 onlineSchemaChangeFlowableTask.setOscTaskCompleteHandler(oscTaskCompleteHandler);

 when(organizationService.getUserById(Mockito.anyLong())).thenReturn(Mockito.mock(User.class));
 when(quartzJobService.create(Mockito.any(), Mockito.any())).thenReturn(1L);
 when(quartzJobService.createJob(Mockito.any(), Mockito.any())).thenReturn(1L);
 when(quartzJobService.updateJob(Mockito.any(), Mockito.any())).thenReturn(true);
 when(quartzJobService.deleteJob(Mockito.anyLong())).thenReturn(true);
 when(scheduleService.create(Mockito.any())).thenReturn(Mockito.mock(ScheduleEntity.class));
 when(scheduleTaskRepository.saveAndFlush(Mockito.any())).thenReturn(Mockito.mock(ScheduleTaskEntity.class));
 when(taskService.start(Mockito.anyLong())).thenReturn(null);
 }

 @Test
 public void testStartSuccess() throws Exception {
 // Given
 Long taskId = 1L;
 DelegateExecution execution = mock(DelegateExecution.class);
 User creator = Mockito.mock(User.class);
 OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
 ConnectionConfig connectionConfigMock = Mockito.mock(ConnectionConfig.class);
 String schema = "testSchema";
 when(execution.getVariable("param")).thenReturn(parameter);
 when(execution.getVariable("connectionConfig")).thenReturn(connectionConfigMock);
 when(organizationService.getUserById(Mockito.anyLong())).thenReturn(creator);
 when(flowTaskUtil.getCloudMainAccountId(execution)).thenReturn("uid");
 when(flowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
 when(flowTaskUtil.getSchemaName(execution)).thenReturn(schema);
 when(flowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfigMock);

 // When
 onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

 // Then
 // Add assertions here
 }

 @Test
 public void testStartFailure() throws Exception {
 // Given
 Long taskId = 1L;
 DelegateExecution execution = mock(DelegateExecution.class);
 User creator = Mockito.mock(User.class);
 OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
 ConnectionConfig connectionConfigMock = Mockito.mock(ConnectionConfig.class);
 String schema = "testSchema";
 when(execution.getVariable("param")).thenReturn(parameter);
 when(execution.getVariable("connectionConfig")).thenReturn(connectionConfigMock);
 when(organizationService.getUserById(Mockito.anyLong())).thenReturn(creator);
 when(flowTaskUtil.getCloudMainAccountId(execution)).thenReturn("uid");
 when(flowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
 when(flowTaskUtil.getSchemaName(execution)).thenReturn(schema);
 when(flowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfigMock);
 doThrow(new Exception("Test Exception")).when(taskService).start(taskId);

 // When
 // Add assertions here
 }

 @Test
 public void testIsSuccessful() {
 // Given
 TaskStatus status = TaskStatus.RUNNING;

 // When
 // Add assertions here
 }

 @Test
 public void testIsFailure() {
 // Given
 TaskStatus status = TaskStatus.FAILED;

 // When
 // Add assertions here
 }

 @Test
 public void testOnFailure() throws Exception {
 // Given
 Long taskId = 1L;

 // When
 onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

 // Then
 // Add assertions here
 }

 @Test
 public void testOnSuccessful() throws Exception {
 // Given
 Long taskId = 1L;

 // When
 onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

 // Then
 // Add assertions here
 }

 @Test
 public void testOnTimeout() {
 // Given
 Long taskId = 1L;

 // When
 onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

 // Then
 // Add assertions here
 }

 @Test
 public void testCancel() throws Exception {
 // Given
 Long taskId = 1L;

 // When
 onlineSchemaChangeFlowableTask.cancel(false, taskId, taskService);

 // Then
 // Add assertions here
 }

 @Test
 public void testIsCancelled() {
 // Given
 TaskStatus status = TaskStatus.CANCELED;

 // When
 // Add assertions here
 }

 @Test
 public void testProgressUpdate() throws Exception {
 // Given
 Long taskId = 1L;
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 ScheduleTaskEntity scheduleTaskEntity1 = Mockito.mock(ScheduleTaskEntity.class);
 ScheduleTaskEntity scheduleTaskEntity2 = Mockito.mock(ScheduleTaskEntity.class);
 List<ScheduleTaskEntity> tasks = Arrays.asList(scheduleTaskEntity1, scheduleTaskEntity2);
 when(taskService.detail(taskId)).thenReturn(taskEntity);
 when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(tasks);

 // When
 onlineSchemaChangeFlowableTask.progressUpdate(tasks, taskService);

 // Then
 // Add assertions here
 }

 @Test
 public void testCreateScheduleEntity() {
 // Given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
 String schema = "testSchema";

 // When
 ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);

 // Then
 Assert.assertNotNull(scheduleEntity);
 // Add other assertions here
 }

 @Test
 public void testCreateScheduleTaskEntity() {
 // Given
 Long scheduleId = 1L;
 OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

 // When
 ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

 // Then
 Assert.assertNotNull(scheduleTaskEntity);
 // Add other assertions here
 }
}