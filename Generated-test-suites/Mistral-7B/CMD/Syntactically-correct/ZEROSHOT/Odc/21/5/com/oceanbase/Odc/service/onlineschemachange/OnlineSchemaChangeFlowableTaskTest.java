package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
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
import com.oceanbase.odc.service.schedule.model.ScheduleTaskResult;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskResult;

public class OnlineSchemaChangeFlowableTaskTest {

 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
 private TaskService taskService;
 private ScheduleTaskService scheduleTaskService;
 private OrganizationService organizationService;
 private QuartzJobService quartzJobService;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ScheduleService scheduleService;
 private OscTaskCompleteHandler oscTaskCompleteHandler;
 private BaseODCFlowTaskDelegate<Void> delegate;
 private ConnectionConfig connectionConfig;
 private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
 private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;
 private ScheduleEntity scheduleEntity;
 private ScheduleTaskEntity scheduleTaskEntity;

 @Before
 public void setup() {
 ObjectMapper objectMapper = new ObjectMapper();
 onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 taskService = mock(TaskService.class);
 scheduleTaskService = mock(ScheduleTaskService.class);
 organizationService = mock(OrganizationService.class);
 quartzJobService = mock(QuartzJobService.class);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 scheduleService = mock(ScheduleService.class);
 oscTaskCompleteHandler = mock(OscTaskCompleteHandler.class);
 delegate = new BaseODCFlowTaskDelegate<Void>() {
 @Override
 public void start(Long taskId, TaskService taskService, DelegateExecution execution) throws Exception {
 super.start(taskId, taskService, execution);
 }

 @Override
 public boolean isSuccessful() {
 return false;
 }

 @Override
 public boolean isFailure() {
 return false;
 }

 @Override
 public void onFailure(Long taskId, TaskService taskService) {
 super.onFailure(taskId, taskService);
 }

 @Override
 public void onSuccessful(Long taskId, TaskService taskService) {
 super.onSuccessful(taskId, taskService);
 }

 @Override
 public void onTimeout(Long taskId, TaskService taskService) {
 super.onTimeout(taskId, taskService);
 }

 @Override
 public void onProgressUpdate(Long taskId, TaskService taskService) {
 super.onProgressUpdate(taskId, taskService);
 }

 @Override
 public boolean cancel(boolean mayInterruptIfRunning, Long taskId, TaskService taskService) {
 return super.cancel(mayInterruptIfRunning, taskId, taskService);
 }
 };
 onlineSchemaChangeFlowableTask.setTaskService(taskService);
 onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
 onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
 onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
 onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
 onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
 onlineSchemaChangeFlowableTask.setOscTaskCompleteHandler(oscTaskCompleteHandler);
 connectionConfig = new ConnectionConfig();
 connectionConfig.setId(1L);
 onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeParameters.setId(1L);
 onlineSchemaChangeParameters.setName("test");
 onlineSchemaChangeParameters.setContinueOnError(true);
 onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
 onlineSchemaChangeScheduleTaskParameters.setId(1L);
 onlineSchemaChangeScheduleTaskParameters.setConnectionId(1L);
 onlineSchemaChangeScheduleTaskParameters.setOnlineSchemaChangeParameters(onlineSchemaChangeParameters);
 onlineSchemaChangeScheduleTaskParameters.setDatabaseName("test");
 scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 scheduleEntity.setStatus(ScheduleStatus.ENABLED);
 scheduleEntity.setAllowConcurrent(false);
 scheduleEntity.setCreatorId(1L);
 scheduleEntity.setOrganizationId(1L);
 scheduleEntity.setProjectId(1L);
 scheduleEntity.setDatabaseId(1L);
 scheduleEntity.setModifierId(scheduleEntity.getCreatorId());
 scheduleEntity.setTriggerConfigJson(JsonUtils.toJson(new TriggerConfig()));
 scheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(onlineSchemaChangeScheduleTaskParameters));
 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
 scheduleTaskEntity.setJobName(scheduleEntity.getId().toString());
 scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
 scheduleTaskEntity.setProgressPercentage(0.0);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJson(onlineSchemaChangeScheduleTaskParameters));
 scheduleTaskEntity.setResultJson(JsonUtils.toString(new OnlineSchemaChangeScheduleTaskResult(onlineSchemaChangeScheduleTaskParameters)));
 scheduleTaskEntity.setFireTime(new Date());
 }

 @Test
 public void testStart() throws Exception {
 DelegateExecution execution = mock(DelegateExecution.class);
 when(taskService.detail(1L)).thenReturn(new TaskEntity());
 when(taskService.start(1L)).thenReturn(true);
 when(quartzJobService.scheduleJob(scheduleEntity)).thenReturn(true);
 when(scheduleTaskRepository.saveAndFlush(scheduleTaskEntity)).thenReturn(scheduleTaskEntity);
 when(oscTaskCompleteHandler.start(scheduleId, scheduleTaskEntity.getId())).thenReturn(true);
 ArgumentCaptor<TaskStatus> taskStatusArgumentCaptor = ArgumentCaptor.forClass(TaskStatus.class);
 onlineSchemaChangeFlowableTask.start(1L, taskService, execution);
 verify(taskService).start(1L);
 verify(quartzJobService).scheduleJob(scheduleEntity);
 verify(scheduleTaskRepository).saveAndFlush(scheduleTaskEntity);
 verify(oscTaskCompleteHandler).start(scheduleId, scheduleTaskEntity.getId());
 assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
 }

 @Test
 public void testIsSuccessful() {
 assertEquals(false, onlineSchemaChangeFlowableTask.isSuccessful());
 }

 @Test
 public void testIsFailure() {
 assertEquals(false, onlineSchemaChangeFlowableTask.isFailure());
 }

 @Test
 public void testOnFailure() {
 Long taskId = 1L;
 onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);
 verify(taskService).cancel(true, taskId, taskService);
 }

 @Test
 public void testOnSuccessful() throws Exception {
 Long taskId = 1L;
 OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult();
 onlineSchemaChangeTaskResult.setTasks(Arrays.asList(new ScheduleTaskResult()));
 Page<ScheduleTaskEntity> page = new PageImpl<>(Arrays.asList(scheduleTaskEntity));
 when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(page);
 when(taskService.detail(taskId)).thenReturn(new TaskEntity());
 List<Double> percentages = Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5);
 double totalPercentage = 1.0;
 when(singleTaskPercentage(scheduleTaskEntity)).thenReturn(percentages.get(0));
 OnlineSchemaChangeContextHolder.clear();
 onlineSchemaChangeFlowableTask.progressStatusUpdate(page);
 OnlineSchemaChangeTaskResult result = onlineSchemaChangeFlowableTask.getResult();
 assertEquals(onlineSchemaChangeTaskResult, result);
 assertEquals(TaskStatus.DONE, onlineSchemaChangeFlowableTask.getStatus());
 }

 @Test
 public void testCancel() {
 boolean mayInterruptIfRunning = true;
 Long taskId = 1L;
 onlineSchemaChangeFlowableTask.cancel(mayInterruptIfRunning, taskId, taskService);
 verify(taskService).cancel(mayInterruptIfRunning, taskId, taskService);
 }

 @Test
 public void testIsCancelled() {
 OnlineSchemaChangeFlowableTask cancelledTask = new OnlineSchemaChangeFlowableTask();
 cancelledTask.setStatus(TaskStatus.CANCELED);
 assertTrue(cancelledTask.isCancelled());
 }

}