package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

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
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
 }

 @Test
 public void testExecute_DatabasePartitionPlanNotFound() {
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 assertFalse(partitionPlanJob.execute(mock(JobExecutionContext.class)));
 }

 @Test
 public void testExecute_NoTablePartitionPlanFound() {
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(new ArrayList<>());
 assertFalse(partitionPlanJob.execute(mock(JobExecutionContext.class)));
 }

 @Test
 public void testExecute_DatabasePartitionPlanConfigDisabled() {
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(false);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
 assertFalse(partitionPlanJob.execute(mock(JobExecutionContext.class)));
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_SchedulerExceptionThrown() throws SchedulerException {
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
 when(scheduleServiceMock.terminate(scheduleEntity)).thenThrow(new SchedulerException());
 partitionPlanJob.execute(mock(JobExecutionContext.class));
 }

 @Test
 public void testExecute_Success() throws SchedulerException {
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(true);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 List<TablePartitionPlanEntity> tablepartitionPlans = new ArrayList<>();
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablepartitionPlans);
 when(partitionPlanTaskServiceMock.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablepartitionPlans)).thenReturn(true);
 assertTrue(partitionPlanJob.execute(mock(JobExecutionContext.class)));
 }

 @Test
 public void testInterrupt() {
 assertNull(partitionPlanJob.interrupt());
 }
}