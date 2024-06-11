package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanService partitionPlanServiceMock;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private ScheduleService scheduleServiceMock;

 @Before
 public void setUp() {
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanServiceMock = Mockito.mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = Mockito.mock(PartitionPlanTaskService.class);
 scheduleServiceMock = Mockito.mock(ScheduleService.class);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
 }

 @Test
 public void testExecute_validScheduleEntity_success() {
 // given
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(UUID.randomUUID().toString());
 String jsonString = JsonUtils.toJsonString(jobParameters);
 when(scheduleEntity.getJobParametersJson()).thenReturn(jsonString);
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyString())).thenReturn(databasePartitionPlan);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyString()))
 .thenReturn(tablePartitionPlans);

 // when
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(
 anyLong(), eq(tablePartitionPlans));
 }

 @Test(expected = UnExpectedException.class)
 public void testExecute_nullScheduleEntity_exception() {
 // given
 when(scheduleServiceMock.nullSafeGetById(anyLong()))
 .thenThrow(new NullPointerException());

 // when, then
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));
 }

 @Test
 public void testExecute_databasePartitionPlanDisable_stopJob() {
 // given
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyString())).thenReturn(databasePartitionPlan);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

 // when
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 // then, verify schedule is terminated
 verify(scheduleServiceMock, times(1)).terminate(eq(scheduleEntity));
 }

 @Test
 public void testExecute_emptyTablePartitionPlans_stopJob() {
 // given
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyString())).thenReturn(databasePartitionPlan);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyString())).thenReturn(tablePartitionPlans);
 when(tablePartitionPlans.isEmpty()).thenReturn(true);

 // when
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 // then, verify schedule is terminated
 verify(scheduleServiceMock, times(1)).terminate(eq(scheduleEntity));
 }
}