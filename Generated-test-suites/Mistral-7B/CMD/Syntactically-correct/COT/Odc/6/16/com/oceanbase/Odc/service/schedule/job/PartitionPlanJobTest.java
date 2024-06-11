package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import com.oceanbase.odc.service.schedule.exception.ScheduleException;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

@Slf4j
public class PartitionPlanJobTest {

 @Mock
 private ScheduleService scheduleService;

 @Mock
 private PartitionPlanService partitionPlanService;

 @Mock
 private PartitionPlanTaskService partitionPlanTaskService;

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 private JobExecutionContext context;

 @Before
 public void setup() {
 context = mock(JobExecutionContext.class);
 when(scheduleService.nullSafeGetById((Long) anyInt())).thenReturn(new ScheduleEntity());
 when(scheduleService.terminate(any(ScheduleEntity.class))).thenThrow(new SchedulerException());
 when(partitionPlanService.getDatabasePartitionPlanById((Long) anyInt()))
 .thenReturn(new DatabasePartitionPlanEntity());
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId((Long) anyInt()))
 .thenReturn(Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity()));
 }

 @Test
 public void testExecute_NoScheduleFound() {
 // given
 partitionPlanJob.execute(context);

 // then
 verify(log, times(1)).warn("Schedule not found,scheduleId={}", anyInt());
 }

 @Test
 public void testExecute_DatabasePartitionPlanDisabled() {
 // given
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(false);
 when(partitionPlanService.getDatabasePartitionPlanById((Long) anyInt()))
 .thenReturn(databasePartitionPlan);

 // when
 partitionPlanJob.execute(context);

 // then
 verify(log, times(1)).info(
 "database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}", anyInt());
 verify(scheduleService, times(1)).terminate(any(ScheduleEntity.class));
 }

 @Test
 public void testExecute_DatabasePartitionPlanEnabled() {
 // given
 when(partitionPlanService.getDatabasePartitionPlanById((Long) anyInt()))
 .thenReturn(new DatabasePartitionPlanEntity());
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId((Long) anyInt()))
 .thenReturn(Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity()));

 // when
 partitionPlanJob.execute(context);

 // then
 verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() {
 // when
 partitionPlanJob.interrupt();

 // then
 verify(log, times(1)).warn("Unexpected error.");
 }
}