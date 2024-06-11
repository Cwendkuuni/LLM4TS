package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.Model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJobParametersTest;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanService partitionPlanServiceMock;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private ScheduleService scheduleServiceMock;

 @Before
 public void setup() {
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 partitionPlanJob = new PartitionPlanJob();
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
 }

 @Test
 public void testExecute_ValidParameters_Success() throws Exception {
 // Given
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(UUID.randomUUID().toString());
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setFlowInstanceId(UUID.randomUUID().toString());
 databasePartitionPlan.setConfigEnabled(true);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
 tablePartitionPlans.add(tablePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);
 when(scheduleServiceMock.nullSafeGetById(anyLong()))
 .thenReturn(new ScheduleEntity());

 // When
 partitionPlanJob.execute(mock(JobExecutionContext.class));

 // Then
 // Add verification code here
 }

 @Test(expected = UnExpectedException.class)
 public void testExecute_ScheduleNotFound_Exception() throws Exception {
 // Given
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(UUID.randomUUID().toString());
 when(scheduleServiceMock.nullSafeGetById(anyLong()))
 .thenThrow(new Exception());

 // When & Then
 partitionPlanJob.execute(mock(JobExecutionContext.class));
 }

 @Test
 public void testExecute_DisabledPartitionPlan_Return() throws Exception {
 // Given
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(UUID.randomUUID().toString());
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setFlowInstanceId(UUID.randomUUID().toString());
 databasePartitionPlan.setConfigEnabled(false);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(new ArrayList<>());

 // When
 partitionPlanJob.execute(mock(JobExecutionContext.class));

 // Then
 // Add verification code here
 }

 @Test
 public void testExecute_NoTablesToProcess_Return() throws Exception {
 // Given
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(UUID.randomUUID().toString());
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setFlowInstanceId(UUID.randomUUID().toString());
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(new ArrayList<>());

 // When
 partitionPlanJob.execute(mock(JobExecutionContext.class));

 // Then
 // Add verification code here
 }

 @Test(expected = SchedulerException.class)
 public void testTerminate_Failed() throws Exception {
 // Given
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 when(scheduleServiceMock.terminate(scheduleEntity)).thenThrow(new SchedulerException());

 // When & Then
 partitionPlanJob.terminate();
 }
}