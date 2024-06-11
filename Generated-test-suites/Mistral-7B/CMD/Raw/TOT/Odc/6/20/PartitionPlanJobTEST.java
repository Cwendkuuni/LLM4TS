package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
 public void execute_whenScheduleIsNotFound_thenNothingHappens() throws Exception {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 when(scheduleServiceMock.nullSafeGetById(anyLong()))
 .thenThrow(new Exception("Schedule not found"));

 JobExecutionContext context = mock(JobExecutionContext.class);
 partitionPlanJob.execute(context);

 verify(log, times(1)).warn("Schedule not found,scheduleId={}", anyLong());
 }

 @Test
 public void execute_whenDatabasePartitionPlanIsDisabled_thenNothingHappens() throws Exception {
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(false);
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(UUID.randomUUID().toString());

 when(scheduleServiceMock.nullSafeGetById(anyLong()))
 .thenReturn(new ScheduleEntity());
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);

 partitionPlanJob.execute(mock(JobExecutionContext.class));

 verify(scheduleServiceMock, times(1)).terminate(any(ScheduleEntity.class));
 }

 @Test
 public void execute_whenPartitionPlanExecutionSuccess_thenSchedulerIsTerminated() throws Exception {
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(UUID.randomUUID().toString());

 when(scheduleServiceMock.nullSafeGetById(anyLong()))
 .thenReturn(new ScheduleEntity());
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);
 when(partitionPlanTaskServiceMock.executePartitionPlan(anyLong(), anyList()))
 .thenReturn(true);

 partitionPlanJob.execute(mock(JobExecutionContext.class));

 verify(scheduleServiceMock, times(1)).terminate(any(ScheduleEntity.class));
 }

 @Test(expected = UnExpectedException.class)
 public void interrupt_whenInterrupted_thenThrowsUnexpectedException() throws Exception {
 partitionPlanJob.interrupt();
 }
}