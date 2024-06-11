package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

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
 public void testExecute_ValidSchedule_ShouldExecutePartitionPlan() {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());

 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenThrow(new SchedulerException());

 partitionPlanJob.execute(mock(JobExecutionContext.class));

 verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(anyLong(), anyList());
 }

 @Test(expected = UnExpectedException.class)
 public void testExecute_ScheduleNotFound_ShouldTerminateJob() {
 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenThrow(new Exception());

 partitionPlanJob.execute(mock(JobExecutionContext.class));

 verify(scheduleServiceMock, times(1)).terminate(any());
 }

 @Test
 public void testExecute_DatabasePartitionPlanDisabled_ShouldTerminateJob() {
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());

 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(new ScheduleEntity());
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(new PartitionPlanJobParameters()));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

 partitionPlanJob.execute(mock(JobExecutionContext.class));

 verify(scheduleServiceMock, times(1)).terminate(any());
 }

 @Test
 public void testExecute_EmptyTablePartitionPlans_ShouldTerminateJob() {
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList();

 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(new ScheduleEntity());
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(new PartitionPlanJobParameters()));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

 partitionPlanJob.execute(mock(JobExecutionContext.class));

 verify(scheduleServiceMock, times(1)).terminate(any());
 }
}