package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

 private static final Long DATABASE_PARTITION_PLAN_ID = 1L;
 private static final Long SCHEDULE_ID = 1L;
 private static final Long FLOW_INSTANCE_ID = 1L;

 @Mock
 private PartitionPlanService partitionPlanService;

 @Mock
 private PartitionPlanTaskService partitionPlanTaskService;

 @Mock
 private ScheduleService scheduleService;

 @Mock
 private SpringContextUtil springContextUtil;

 @Mock
 private ScheduleTaskUtils scheduleTaskUtils;

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
 when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
 when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
 when(scheduleTaskUtils.getScheduleId(any())).thenReturn(SCHEDULE_ID);
 }

 @Test
 public void testExecute_databasePartitionPlanDisabled_shouldReturnAndStopJob() {
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(DATABASE_PARTITION_PLAN_ID);
 databasePartitionPlan.setConfigEnabled(false);

 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(SCHEDULE_ID);
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));

 when(partitionPlanService.getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID)).thenReturn(databasePartitionPlan);
 when(scheduleService.nullSafeGetById(SCHEDULE_ID)).thenReturn(scheduleEntity);

 partitionPlanJob.execute(null);

 verify(scheduleService, times(1)).terminate(scheduleEntity);
 }

 @Test
 public void testExecute_noTableToProcess_shouldReturnAndStopJob() {
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(DATABASE_PARTITION_PLAN_ID);
 databasePartitionPlan.setConfigEnabled(true);

 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList();

 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(SCHEDULE_ID);
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));

 when(partitionPlanService.getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID)).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID)).thenReturn(tablePartitionPlans);
 when(scheduleService.nullSafeGetById(SCHEDULE_ID)).thenReturn(scheduleEntity);

 partitionPlanJob.execute(null);

 verify(scheduleService, times(1)).terminate(scheduleEntity);
 }

 @Test
 public void testExecute_databasePartitionPlanEnabledAndTableToProcess_shouldCallExecutePartitionPlan() {
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(DATABASE_PARTITION_PLAN_ID);
 databasePartitionPlan.setConfigEnabled(true);

 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity());

 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(SCHEDULE_ID);
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));

 when(partitionPlanService.getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID)).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID)).thenReturn(tablePartitionPlans);
 when(scheduleService.nullSafeGetById(SCHEDULE_ID)).thenReturn(scheduleEntity);

 doNothing().when(partitionPlanTaskService).executePartitionPlan(FLOW_INSTANCE_ID, tablePartitionPlans);

 partitionPlanJob.execute(null);

 verify(partitionPlanTaskService, times(1)).executePartitionPlan(FLOW_INSTANCE_ID, tablePartitionPlans);
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() {
 partitionPlanJob.interrupt();
 }
}