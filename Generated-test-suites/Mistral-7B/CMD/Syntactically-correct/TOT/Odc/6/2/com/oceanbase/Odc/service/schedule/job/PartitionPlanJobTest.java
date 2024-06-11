package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

import org.junit.Before;
import org.junit.Test;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanService partitionPlanServiceMock;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private PartitionPlanJobParameters jobParameters;
 private ScheduleEntity scheduleEntity;
 private ScheduleService scheduleServiceMock;

 @Before
 public void setUp() {
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 scheduleEntity = new ScheduleEntity();
 jobParameters = new PartitionPlanJobParameters();

 partitionPlanJob = new PartitionPlanJob();
 partitionPlanJob.setPartitionPlanService(partitionPlanServiceMock);
 partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskServiceMock);
 partitionPlanJob.setScheduleService(scheduleServiceMock);
 }

 @Test
 public void testExecute_ValidScheduleEntity_ExecutesPartitionPlan() throws Exception {
 // Given
when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters).getBytes());
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(new DatabasePartitionPlanEntity());
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L))
 .thenReturn(List.of(new TablePartitionPlanEntity()));

 // When
 partitionPlanJob.execute(mock(JobExecutionContext.class));

 // Then
 verify(partitionPlanTaskServiceMock).executePartitionPlan(anyLong(), anyList());
 }

 @Test(expected = SchedulerException.class)
 public void testExecute_NonExistentScheduleEntity_ThrowsSchedulerException() throws Exception {
 // Given
 when(scheduleServiceMock.nullSafeGetById(1L)).thenThrow(new SchedulerException());

 // When & Then
 partitionPlanJob.execute(mock(JobExecutionContext.class));
 }

 @Test
 public void testExecute_DisabledDatabasePartitionPlan_StopsJob() throws Exception {
 // Given
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters).getBytes());
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 databasePartitionPlan.setConfigEnabled(false);

 // When
 partitionPlanJob.execute(mock(JobExecutionContext.class));

 // Then
 verify(scheduleServiceMock).terminate(scheduleEntity);
 }

 @Test
 public void testExecute_EmptyTablePartitionPlans_StopsJob() throws Exception {
 // Given
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters).getBytes());
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(List.of());

 // When
 partitionPlanJob.execute(mock(JobExecutionContext.class));

 // Then
 verify(scheduleServiceMock).terminate(scheduleEntity);
 }

 @Test
 public void testInterrupt() {
 // Given

 // When & Then
 partitionPlanJob.interrupt();
 }
}