package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

@Slf4j
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private PartitionPlanService partitionPlanServiceMock;
 private ScheduleService scheduleServiceMock;

 @Before
 public void setup() {
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
 }

 @Test
 public void testExecute_whenDatabasePartitionPlanIsDisabled_shouldStopJob() {
 // Given
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(false);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(databasePartitionPlan.getId());
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));

 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(scheduleServiceMock.nullSafeGetById(Long.parseLong("1"))).thenReturn(scheduleEntity);

 // When
 partitionPlanJob.execute(new JobExecutionContext());

 // Then
 assertFalse(scheduleEntity.isRunning());
 }

 @Test
 public void testExecute_whenTablePartitionPlansAreEmpty_shouldStopJob() {
 // Given
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(true);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(databasePartitionPlan.getId());
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);

 // When
 partitionPlanJob.execute(new JobExecutionContext());

 // Then
 assertFalse(scheduleEntity.isRunning());
 }

 @Test(expected = UnExpectedException.class)
 public void testExecute_whenExceptionOccurs_shouldThrowException() {
 // Given
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(true);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(databasePartitionPlan.getId());
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);
 when(partitionPlanTaskServiceMock.executePartitionPlan(Long.parseLong("1"), tablePartitionPlans))
 .thenThrow(new RuntimeException());

 // When & Then
 partitionPlanJob.execute(new JobExecutionContext());
 }

 @Test
 public void testExecute_whenAllIsWell_shouldExecutePartitionPlan() {
 // Given
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(true);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(databasePartitionPlan.getId());
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);

 // When
 partitionPlanJob.execute(new JobExecutionContext());

 // Then
 assertNotNull(scheduleEntity.getLastModified());
 }
}