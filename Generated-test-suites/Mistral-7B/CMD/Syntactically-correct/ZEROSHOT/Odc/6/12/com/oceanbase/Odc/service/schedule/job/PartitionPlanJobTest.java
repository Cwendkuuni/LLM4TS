package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;

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
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
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
 partitionPlanServiceMock = mock(PartitionPlanService.class, Answers.RETURNS_DEEP_STUBS);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class, Answers.RETURNS_DEEP_STUBS);
 scheduleServiceMock = mock(ScheduleService.class, Answers.RETURNS_DEEP_STUBS);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
 }

 @Test
 public void testExecute_whenScheduleIsNotExist_thenDoNothing() {
 doAnswer(invocationOnMock -> {}).when(scheduleServiceMock).nullSafeGetById(anyLong());

 partitionPlanJob.execute(mock(JobExecutionContext.class));
 }

 @Test
 public void testExecute_whenDatabasePartitionPlanIsNotEnabledOrNoTable_thenStopJob() throws SchedulerException {
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(false);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

 doAnswer(invocationOnMock -> {}).when(scheduleServiceMock).terminate(any(ScheduleEntity.class));

 partitionPlanJob.execute(mock(JobExecutionContext.class));

 assertFalse(partitionPlanJob.isAlive());
 }

 @Test
 public void testExecute_whenDatabasePartitionPlanIsEnabledAndTableExists_thenExecutePartitionPlan() throws Exception {
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 tablePartitionPlans.add(new TablePartitionPlanEntity());

 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
 when(partitionPlanTaskServiceMock.executePartitionPlan(anyLong(), anyList()))
 .thenAnswer(invocationOnMock -> null);

 doAnswer(invocationOnMock -> {}).when(scheduleServiceMock).terminate(any(ScheduleEntity.class));

 partitionPlanJob.execute(mock(JobExecutionContext.class));

 assertNull(partitionPlanJob.isAlive());
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() {
 partitionPlanJob.interrupt();
 }
}