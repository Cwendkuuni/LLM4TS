package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

@Slf4j
public class PartitionPlanJobTest {

 @Mock
 private PartitionPlanService partitionPlanService;

 @Mock
 private PartitionPlanTaskService partitionPlanTaskService;

 @Mock
 private ScheduleService scheduleService;

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 SpringContextUtil.registerSingleton(PartitionPlanService.class, partitionPlanService);
 SpringContextUtil.registerSingleton(PartitionPlanTaskService.class, partitionPlanTaskService);
 SpringContextUtil.registerSingleton(ScheduleService.class, scheduleService);
 }

 @Test
 public void testExecute() {
 // Given
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());
 // When
 when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
 when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);
 // When executing the job
 partitionPlanJob.execute(mock(JobExecutionContext.class));
 // Then
 verify(partitionPlanTaskService).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() {
 // When interrupting the job
 partitionPlanJob.interrupt();
 // Then an exception should be thrown
 }

 @Test
 public void testExecutePartitionPlanFailure() {
 // Given
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());
 // When
 when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
 when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);
 when(partitionPlanTaskService.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans))
 .thenThrow(new RuntimeException());
 // When executing the job
 partitionPlanJob.execute(mock(JobExecutionContext.class));
 // Then the job should not stop
 verify(scheduleService, never()).terminate(scheduleEntity);
 }

 @Test
 public void testDatabasePartitionPlanDisabled() {
 // Given
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(false);
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());
 // When
 when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
 when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);
 // When executing the job
 partitionPlanJob.execute(mock(JobExecutionContext.class));
 // Then the job should not execute partition plan and stop
 verify(partitionPlanTaskService, never()).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
 verify(scheduleService).terminate(scheduleEntity);
 }

 @Test
 public void testEmptyTablePartitionPlans() {
 // Given
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList();
 // When
 when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
 when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);
 // When executing the job
 partitionPlanJob.execute(mock(JobExecutionContext.class));
 // Then the job should not execute partition plan and stop
 verify(partitionPlanTaskService, never()).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
 verify(scheduleService).terminate(scheduleEntity);
 }
}