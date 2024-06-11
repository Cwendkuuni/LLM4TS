package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskEntity;
import com.oceanbase.odc.service.task.TaskService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class OnlineSchemaChangeFlowableTaskTest {

 @Mock
 private TaskService taskService;
 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @Mock
 private QuartzJobService quartzJobService;
 @Mock
 private ConnectionConfig connectionConfig;
 @Mock
 private OrganizationService organizationService;
 @Mock
 private ScheduleService scheduleService;
 @Mock
 private ScheduleTaskService scheduleTaskService;

 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
 private User user;
 private ConnectionConfig connectionConfigMock;
 private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
 private String schemaName;
 private long flowInstanceId;
 private long taskId;
 private long scheduleId;
 private long creatorId;
 private long organizationId;
 private boolean continueOnError;

 @BeforeEach
 public void setup() {
 user = Mockito.mock(User.class);
 connectionConfigMock = Mockito.mock(ConnectionConfig.class);
 onlineSchemaChangeParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
 schemaName = "testSchema";
 flowInstanceId = RandomUtils.nextLong();
 taskId = RandomUtils.nextLong();
 creatorId = RandomUtils.nextLong();
 organizationId = RandomUtils.nextLong();
 continueOnError = true;
 onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask() {
 @Override
 @Autowired
 public TaskService getTaskService() {
 return taskService;
 }

 @Override
 @Autowired
 public ScheduleTaskRepository getScheduleTaskRepository() {
 return scheduleTaskRepository;
 }

 @Override
 @Autowired
 public QuartzJobService getQuartzJobService() {
 return quartzJobService;
 }

 @Override
 @Autowired
 public OnlineSchemaChangeTaskHandler getTaskHandler() {
 return null;
 }

 @Override
 @Autowired
 public OscTaskCompleteHandler getCompleteHandler() {
 return null;
 }

 @Override
 @Autowired
 public OrganizationService getOrganizationService() {
 return organizationService;
 }

 @Override
 public ScheduleEntity createScheduleEntity(ConnectionConfig connectionConfig, OnlineSchemaChangeParameters parameter, String schema) {
 return Mockito.mock(ScheduleEntity.class);
 }

 @Override
 public ScheduleTaskEntity createScheduleTaskEntity(Long scheduleId, OnlineSchemaChangeScheduleTaskParameters param) {
 return Mockito.mock(ScheduleTaskEntity.class);
 }

 @Override
 public long getCreatorId() {
 return creatorId;
 }

 @Override
 public long getOrganizationId() {
 return organizationId;
 }

 @Override
 public boolean isContinueOnError() {
 return continueOnError;
 }

 @Override
 public void setStatus(TaskStatus status) {
 this.status = status;
 }

 @Override
 public void setScheduleId(long scheduleId) {
 this.scheduleId = scheduleId;
 }

 @Override
 public void updateFlowInstanceStatus(FlowStatus status) {
 // no-op
 }
 };
 }

 @Test
 public void testStart() throws Exception {
 ProcessInstance processInstance = Mockito.mock(ProcessInstance.class);
 DelegateExecution execution = Mockito.mock(DelegateExecution.class);
 user.setId(creatorId);
 Mockito.when(connectionConfig.getId()).thenReturn(1L);
 Mockito.when(organizationService.getUserById(creatorId)).thenReturn(user);
 Mockito.when(FlowTaskUtil.getFlowInstanceId(execution)).thenReturn(flowInstanceId);
 Mockito.when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(onlineSchemaChangeParameters);
 Mockito.when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfigMock);
 Mockito.when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schemaName);
 Mockito.when(taskService.detail(ArgumentMatchers.anyLong())).thenReturn(Mockito.mock(TaskEntity.class));
 Page<ScheduleTaskEntity> tasks = Mockito.mock(Page.class);
 Mockito.when(scheduleTaskService.listTask(ArgumentMatchings.any(Pageable.class), ArgumentMatchings.eq(scheduleId))).thenReturn(tasks);
 Pageable pageable = Mockito.mock(Pageable.class);
 Mockito.when(pageable.getPageSize()).thenReturn(10);
 Mockito.when(pageable.getPageNumber()).thenReturn(0);
 Mockito.when(scheduleTaskRepository.saveAndFlush(ArgumentMatchings.any(ScheduleTaskEntity.class))).thenReturn(Mockito.mock(ScheduleTaskEntity.class));
 Mockito.when(quartzJobService.scheduleJob(ArgumentMatchings.anyString(), ArgumentMatchings.anyString(), ArgumentMatchings.anyString(), ArgumentMatchings.anyString(), ArgumentMatchings.anyString(), ArgumentMatchings.anyMap(), ArgumentMatchings.anyMap())).thenReturn(true);
 Mockito.when(taskService.update(ArgumentMatchings.any(TaskEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

 onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

 Assertions.assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
 Mockito.verify(taskService, Mockito.times(1)).start(taskId);
 }

 @Test
 public void testIsSuccessful() {
 Assertions.assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
 }

 @Test
 public void testIsFailure() {
 Assertions.assertFalse(onlineSchemaChangeFlowableTask.isFailure());
 }

 @Test
 public void testCancel() throws Exception {
 long taskIdToCancel = RandomUtils.nextLong();
 Mockito.when(taskService.cancel(taskIdToCancel)).thenReturn(true);
 onlineSchemaChangeFlowableTask.cancel(true, taskIdToCancel, taskService);

 Assertions.assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
 Mockito.verify(taskService, Mockito.times(1)).cancel(taskIdToCancel);
 }

 @Test
 public void testOnProgressUpdate() {
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 taskEntity.setStatus(TaskStatus.RUNNING);
 taskEntity.setProgressPercentage(30);
 Page<ScheduleTaskEntity> tasks = Mockito.mock(Page.class);
 tasks.setContent(Collections.singletonList(taskEntity));
 Mockito.when(scheduleTaskService.listTask(ArgumentMatchings.any(Pageable.class), ArgumentMatchings.eq(scheduleId))).thenReturn(tasks);

 onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

 Assertions.assertEquals(50.0, onlineSchemaChangeFlowableTask.getPercentage());
 }

 @Test
 public void testCreateScheduleEntity() {
 Mockito.when(scheduleService.create(ArgumentMatchings.any(ScheduleEntity.class))).thenReturn(Mockito.mock(ScheduleEntity.class));

 OnlineSchemaChangeParameters onlineSchemaChangeParametersMock = Mockito.mock(OnlineSchemaChangeParameters.class);
 OnlineSchemaChangeContextHolder.trace(creatorId, flowInstanceId, organizationId);
 ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfigMock, onlineSchemaChangeParametersMock, schemaName);

 OnlineSchemaChangeContextHolder.clear();

 Mockito.verify(scheduleService, Mockito.times(1)).create(ArgumentMatchings.any(ScheduleEntity.class));
 }

 @Test
 public void testCreateScheduleTaskEntity() {
 OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class);

 ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, onlineSchemaChangeScheduleTaskParameters);

 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).saveAndFlush(ArgumentMatchings.any(ScheduleTaskEntity.class));
 }
}