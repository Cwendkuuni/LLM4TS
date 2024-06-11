package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskResult;

@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

 @MockBean
 private ConnectionConfig connectionConfig;

 @MockBean
 private OrganizationService organizationService;

 @MockBean
 private ScheduleService scheduleService;

 @MockBean
 private ScheduleTaskRepository scheduleTaskRepository;

 @MockBean
 private ScheduleTaskService scheduleTaskService;

 @MockBean
 private QuartzJobService quartzJobService;

 @MockBean
 private TaskService taskService;

 @MockBean
 private ProcessInstance processInstance;

 @Autowired
 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

 @Before
 public void setup() {
 // Initialize mocks
 when(organizationService.getCurrentUser()).thenReturn(Mockito.mock(User.class));
 when(connectionConfig.getId()).thenReturn(1L);
 when(connectionConfig.getUid()).thenReturn("uid");
 }

 @Test
 public void testStart() {
 // Arrange
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
 ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
 String schema = "schemaName";
 boolean continueOnError = true;

 when(connectionConfig.getConnectionName()).thenReturn("connectionName");
 when(JsonUtils.toJson(onlineSchemaChangeParameters)).thenReturn("{}");
 when(onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema))
 .thenReturn(List.of(new OnlineSchemaChangeScheduleTaskParameters()));

 DelegateExecution execution = Mockito.mock(DelegateExecution.class);

 when(flowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(onlineSchemaChangeParameters);
 when(flowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
 when(flowTaskUtil.getSchemaName(execution)).thenReturn(schema);

 // Act
 OnlineSchemaChangeTaskResult result = onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

 // Assert
 assertNotNull(result);
 verify(taskService).start(1L);
 }

 @Test
 public void testIsSuccessful() {
 // Arrange
 // Act
 // Assert
 assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
 }

 @Test
 public void testIsFailure() {
 // Arrange
 // Act
 // Assert
 assertFalse(onlineSchemaChangeFlowableTask.isFailure());
 }

 @Test
 public void testOnFailure() {
 // Arrange
 // Act
 // Assert
 verify(log).warn("Online schema change task failed, taskId={}");
 }

 @Test
 public void testOnSuccessful() {
 // Arrange
 // Act
 // Assert
 verify(log).info("Online schema change task succeed, taskId={}");
 }

 @Test
 public void testOnTimeout() {
 // Arrange
 // Act
 // Assert
 verify(log).warn("Online schema change task timeout, taskId={}");
 }

 @Test
 public void testProgressUpdate() {
 // Arrange
 List<ScheduleTaskEntity> tasks = ListUtils.emptyIfNull(List.of(Mockito.mock(ScheduleTaskEntity.class),
 Mockito.mock(ScheduleTaskEntity.class)));
 when(scheduleTaskService.listTask(Pageable.unpaged(), Mockito.anyLong())).thenReturn(Optional.of(tasks));

 when(singleTaskPercentage(Mockito.any(ScheduleTaskEntity.class)))
 .thenReturn(0.5);

 OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult(
 List.of(new OnlineSchemaChangeTaskResult("Task1", 0.5), new OnlineSchemaChangeTaskResult("Task2", 0.5)));

 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());
 doReturn(onlineSchemaChangeTaskResult).when(onlineSchemaChangeFlowableTask).getTasks();

 onlineSchemaChangeFlowableTask.percentage = 0.5;

 // Act
 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

 // Assert
 assertEquals(0.5, onlineSchemaChangeFlowableTask.percentage, 0.01);
 }

 @Test
 public void testCancel() {
 // Arrange
 // Act
 // Assert
 verify(taskService).cancel(1L);
 }

 @Test
 public void testIsCancelled() {
 // Arrange
 // Act
 // Assert
 assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
 }

 @Test
 public void testCreateScheduleEntity() {
 // Arrange
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
 String schema = "schemaName";

 when(onlineSchemaChangeParameters.getConnectionConfig()).thenReturn(connectionConfig);
 when(onlineSchemaChangeParameters.getSchema()).thenReturn(schema);

 ScheduleEntity expectedScheduleEntity = Mockito.mock(ScheduleEntity.class);
 when(scheduleService.create(expectedScheduleEntity)).thenReturn(expectedScheduleEntity);

 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

 // Act
 ScheduleEntity actualScheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig,
 onlineSchemaChangeParameters, schema);

 // Assert
 assertEquals(expectedScheduleEntity, actualScheduleEntity);
 }

 @Test
 public void testCreateScheduleTaskEntity() {
 // Arrange
 OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class);
 Long scheduleId = 1L;

 when(scheduleTaskRepository.saveAndFlush(Mockito.any(ScheduleTaskEntity.class))).thenReturn(Mockito.mock(ScheduleTaskEntity.class));

 ScheduleTaskEntity expectedScheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
 when(expectedScheduleTaskEntity.getId()).thenReturn(1L);

 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

 // Act
 ScheduleTaskEntity actualScheduleTaskEntity = onlineSchemaChangeFlowableTask
 .createScheduleTaskEntity(scheduleId, onlineSchemaChangeScheduleTaskParameters);

 // Assert
 assertEquals(expectedScheduleTaskEntity, actualScheduleTaskEntity);
 }
}