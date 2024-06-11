package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
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
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.metadb.task.TaskService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTaskTest;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity;
import com.oceanbase.odc.service.task.TaskService;

public class OnlineSchemaChangeFlowableTaskTest {

 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
 @Autowired
 private ScheduleTaskRepository scheduleTaskRepository;
 @Autowired
 private TaskService taskService;
 @Autowired
 private OrganizationService organizationService;
 @Autowired
 private QuartzJobService quartzJobService;
 @Autowired
 private ScheduleService scheduleService;
 @Autowired
 private ScheduleTaskService scheduleTaskService;
 @Autowired
 private OscTaskCompleteHandler oscTaskCompleteHandler;

 @Before
 public void setup() {
 onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 }

 @Test
 public void testStart() throws Exception {
 // Given
 ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
 OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
 String schema = "test_schema";
 parameter.setConnectionConfig(connectionConfig);
 parameter.setSchema(schema);

 DelegateExecution execution = mock(DelegateExecution.class);
 FlowTaskUtil flowTaskUtil = mock(FlowTaskUtil.class);
 when(flowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
 when(flowTaskUtil.getCloudMainAccountId(execution)).thenReturn("uid");
 when(flowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
 when(flowTaskUtil.getSchemaName(execution)).thenReturn(schema);
 when(flowTaskUtil.getTaskCreator(execution)).thenReturn(new User());

 OnlineSchemaChangeContextHolder.clear();

 // When
 Optional<TaskStatus> status = onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

 // Then
 assertEquals(TaskStatus.RUNNING, status.get());
 }

 @Test(expected = Exception.class)
 public void testStart_with_exception() throws Exception {
 // Given
 ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
 OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
 String schema = "test_schema";
 parameter.setConnectionConfig(connectionConfig);
 parameter.setSchema(schema);

 DelegateExecution execution = mock(DelegateExecution.class);
 FlowTaskUtil flowTaskUtil = mock(FlowTaskUtil.class);
 when(flowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
 when(flowTaskUtil.getCloudMainAccountId(execution)).thenThrow(new Exception());
 when(flowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
 when(flowTaskUtil.getSchemaName(execution)).thenReturn(schema);
 when(flowTaskUtil.getTaskCreator(execution)).thenReturn(new User());

 OnlineSchemaChangeContextHolder.clear();

 // When & Then
 onlineSchemaChangeFlowableTask.start(1L, taskService, execution);
 }

 @Test
 public void testIsSuccessful() {
 // Given
 Date startTime = new Date();
 Date endTime = new Date();
 endTime.setTime(startTime.getTime() + 1000);
 OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
 parameter.setConnectionConfig(new ConnectionConfig());
 parameter.setSchema("test_schema");
 parameter.setContinueOnError(true);
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
 onlineSchemaChangeFlowableTask.setScheduleId(1L);
 onlineSchemaChangeFlowableTask.setPercentage(100.0);

 // When
 boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

 // Then
 assertEquals(true, result);
 }

 @Test
 public void testIsFailure() {
 // Given
 Date startTime = new Date();
 Date endTime = new Date();
 endTime.setTime(startTime.getTime() + 1000);
 OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
 parameter.setConnectionConfig(new ConnectionConfig());
 parameter.setSchema("test_schema");
 parameter.setContinueOnError(true);
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
 onlineSchemaChangeFlowableTask.setPercentage(100.0);

 // When
 boolean result = onlineSchemaChangeFlowableTask.isFailure();

 // Then
 assertEquals(false, result);
 }

 @Test
 public void testOnFailure() throws Exception {
 // Given
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

 // When
 onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

 // Then
 // Not reached
 }

 @Test
 public void testOnSuccessful() throws Exception {
 // Given
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

 // When
 onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

 // Then
 // Not reached
 }

 @Test
 public void testOnTimeout() throws Exception {
 // Given
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

 // When
 onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);

 // Then
 // Not reached
 }

 @Test
 public void testOnProgressUpdate() throws Exception {
 // Given
 ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
 OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
 parameter.setConnectionConfig(connectionConfig);
 parameter.setSchema("test_schema");
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);
 onlineSchemaChangeFlowableTask.setScheduleId(1L);
 onlineSchemaChangeFlowableTask.setPercentage(50.0);

 Page<ScheduleTaskEntity> tasks = mock(Page.class);
 when(scheduleTaskService.listTask(Pageable.unpaged(), eq(1L))).thenReturn(tasks);

 // When
 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

 // Then
 verify(taskService, times(1)).update(any(TaskEntity.class));
 }

 @Test
 public void testCreateScheduleEntity() {
 // Given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
 String schema = "test_schema";
 parameter.setConnectionConfig(connectionConfig);
 parameter.setSchema(schema);

 // When
 ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);

 // Then
 assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
 assertEquals(FlowStatus.EXECUTION_SUCCEEDED, scheduleEntity.getStatus());
 }

 @Test
 public void testCreateScheduleTaskEntity() {
 // Given
 Long scheduleId = 1L;
 OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
 // When
 ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

 // Then
 assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
 }
}