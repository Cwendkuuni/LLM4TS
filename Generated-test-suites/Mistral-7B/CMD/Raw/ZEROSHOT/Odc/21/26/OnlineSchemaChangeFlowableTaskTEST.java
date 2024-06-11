package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
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
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskResult;
import com.oceanbase.odc.service.task.TaskService;
import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeFlowableTaskTest {

 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ScheduleService scheduleService;
 private ScheduleTaskService scheduleTaskService;
 private QuartzJobService quartzJobService;
 private OrganizationService organizationService;
 private TaskService taskService;
 private BaseODCFlowTaskDelegate baseODCFlowTaskDelegate;
 private OscTaskCompleteHandler oscTaskCompleteHandler;
 private ConnectionConfig connectionConfig;
 private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
 private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;
 private List<ScheduleTaskEntity> scheduleTasks;

 @Before
 public void setup() {
 ObjectMapper objectMapper = new ObjectMapper();
 baseODCFlowTaskDelegate = mock(BaseODCFlowTaskDelegate.class);
 onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 onlineSchemaChangeFlowableTask.setBaseODCFlowTaskDelegate(baseODCFlowTaskDelegate);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 scheduleService = mock(ScheduleService.class);
 scheduleTaskService = mock(ScheduleTaskService.class);
 quartzJobService = mock(QuartzJobService.class);
 organizationService = mock(OrganizationService.class);
 taskService = mock(TaskService.class);
 oscTaskCompleteHandler = mock(OscTaskCompleteHandler.class);
 connectionConfig = new ConnectionConfig();
 connectionConfig.setId(1L);
 onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
 scheduleTasks = new ArrayList<>();
 when(baseODCFlowTaskDelegate.start(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(taskService), ArgumentMatchers.any(DelegateExecution.class)))
 .thenReturn(null);
 when(organizationService.getCurrentUser()).thenReturn(new User());
 when(taskService.detail(ArgumentMatchers.anyLong())).thenReturn(new ExecutionEntity());
 when(taskService.listTask(ArgumentMatchers.any(Pageable.class), ArgumentMatchers.anyLong())).thenReturn(getPage(scheduleTasks));
 when(scheduleService.create(ArgumentMatchers.any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());
 when(scheduleTaskRepository.saveAndFlush(ArgumentMatchers.any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());
 when(quartzJobService.scheduleJob(ArgumentMatchers.eq(connectionConfig), ArgumentMatchers.eq(onlineSchemaChangeScheduleTaskParameters), ArgumentMatchers.eq(TriggerStrategy.CRON), ArgumentMatchers.isNull(), ArgumentMatchers.isNull())).thenReturn(true);
 when(quartzJobService.deleteJob(ArgumentMatchers.eq(connectionConfig), ArgumentMatchers.eq(onlineSchemaChangeScheduleTaskParameters.getJobName()), ArgumentMatchers.eq(scheduleTasks.get(0).getId()))).thenReturn(true);
 }

 @Test
 public void testStart() throws Exception {
 OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(scheduleTasks);
 when(baseODCFlowTaskDelegate.getOnlineSchemaChangeTaskResult(ArgumentMatchers.anyLong())).thenReturn(result);
 OnlineSchemaChangeContextHolder.set(1L, 1L, 1L);
 onlineSchemaChangeFlowableTask.start(1L, taskService, new DelegateExecution());
 verify(baseODCFlowTaskDelegate).start(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(taskService), ArgumentMatchers.any(DelegateExecution.class));
 verify(taskService).update(ArgumentMatchers.argThat(task -> {
 String expectedFlowInstanceId = StringUtils.isNotEmpty(result.getFlowInstanceId()) ? result.getFlowInstanceId() : "";
 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setResultJson(JsonUtils.toJson(result));
 taskEntity.setProgressPercentage(100);
 taskEntity.setFlowInstanceId(expectedFlowInstanceId);
 return taskEntity;
 }));
 }

 @Test(expected = Exception.class)
 public void testStart_fail() throws Exception {
 OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(scheduleTasks);
 when(baseODCFlowTaskDelegate.getOnlineSchemaChangeTaskResult(ArgumentMatchers.anyLong())).thenReturn(result);
 OnlineSchemaChangeContextHolder.set(1L, 1L, 1L);
 when(taskService.update(ArgumentMatchers.any(TaskEntity.class))).thenThrow(new RuntimeException());
 onlineSchemaChangeFlowableTask.start(1L, taskService, new DelegateExecution());
 verify(baseODCFlowTaskDelegate, times(2)).start(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(taskService), ArgumentMatchers.any(DelegateExecution.class));
 }

 @Test
 public void testIsSuccessful() {
 assertEquals(true, onlineSchemaChangeFlowableTask.isSuccessful());
 }

 @Test
 public void testIsFailure() {
 assertEquals(false, onlineSchemaChangeFlowableTask.isFailure());
 }

 @Test
 public void testIsCancelled() {
 OnlineSchemaChangeFlowableTask cancelledTask = new OnlineSchemaChangeFlowableTask();
 cancelledTask.setStatus(TaskStatus.CANCELED);
 assertEquals(true, cancelledTask.isCancelled());
 }

 @Test
 public void testCancel() {
 OnlineSchemaChangeFlowableTask cancelledTask = new OnlineSchemaChangeFlowableTask();
 cancelledTask.setStatus(TaskStatus.RUNNING);
 assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService));
 }

 @Test
 public void testOnProgressUpdate() {
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTasks.add(scheduleTask);
 OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(scheduleTasks);
 when(baseODCFlowableTaskDelegate.getOnlineSchemaChangeTaskResult(ArgumentMatchers.anyLong())).thenReturn(result);
 Double percentage = 0.5;
 when(scheduleTasks.get(0).getProgressPercentage()).thenReturn(percentage);
 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
 assertEquals(percentage, onlineSchemaChangeFlowableTask.getPercentage());
 verify(taskService).update(ArgumentMatchers.argThat(task -> {
 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setResultJson(JsonUtils.toJson(result));
 taskEntity.setProgressPercentage(percentage * 100);
 return taskEntity;
 }));
 }

 private Page<ScheduleTaskEntity> getPage(List<ScheduleTaskEntity> scheduleTasks) {
 Page<ScheduleTaskEntity> page = Mockito.mock(Page.class);
 when(page.getSize()).thenReturn(1);
 when(page.getContent()).thenReturn(scheduleTasks.stream().collect(Collectors.toList()));
 return page;
 }

}