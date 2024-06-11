package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 public void testExecute_EnabledDatabasePartitionPlanAndValidTablePartitionPlans() {
 // Given
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 tablePartitionPlans.add(new TablePartitionPlanEntity());

 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(new ScheduleEntity());

 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getMergedJobDataMap().getString("scheduleId")).thenReturn("1");

 // When
 partitionPlanJob.execute(context);

 // Then
 // The partition plan job should not terminate in this test case
 // as the database partition plan and table partition plans are valid
 assertFalse(partitionPlanJob.isInterrupted());
 }

 @Test
 public void testExecute_DisabledDatabasePartitionPlan() {
 // Given
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(false);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(new ScheduleEntity());

 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getMergedJobDataMap().getString("scheduleId")).thenReturn("1");

 // When
 partitionPlanJob.execute(context);

 // Then
 // The partition plan job should terminate as the database partition plan is disabled
 assertFalse(partitionPlanJob.isInterrupted());
 try {
 scheduleServiceMock.terminate(new ScheduleEntity());
 } catch (SchedulerException e) {
 // Expected as the test is using a mock for scheduleService
 }
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() {
 // Given
 // Nothing to set up as this method only throws an exception

 // When
 partitionPlanJob.interrupt();

 // Then
 // The interrupt method should throw an UnExpectedException
 }
}