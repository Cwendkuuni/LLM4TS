package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

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
 private PartitionPlanService partitionPlanServiceMock;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private PartitionPlanJobParameters jobParameters;
 private ScheduleEntity scheduleEntity;
 private ScheduleService scheduleServiceMock;

 @Before
 public void setup() {
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanServiceMock = Mockito.mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = Mockito.mock(PartitionPlanTaskService.class);
 scheduleServiceMock = Mockito.mock(ScheduleService.class);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);

 jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));
 }

 @Test
 public void testExecute_validScheduleEntity() {
 // Given
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(new DatabasePartitionPlanEntity());
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(Arrays.asList(new TablePartitionPlanEntity()));
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);

 // When
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(Mockito.anyLong(), Mockito.anyList());
 }

 @Test(expected = UnExpectedException.class)
 public void testExecute_scheduleNotfound() {
 // Given
 when(scheduleServiceMock.nullSafeGetById(1L)).thenThrow(new Exception());

 // When & Then
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));
 }

 @Test
 public void testExecute_databasePartitionPlanDisabled() {
 // Given
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(false);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);

 // When
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 verify(scheduleServiceMock, times(1)).terminate(scheduleEntity);
 }

 @Test
 public void testExecute_noTableToProcess() {
 // Given
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList();
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);

 // When
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 verify(scheduleServiceMock, times(1)).terminate(scheduleEntity);
 }

 @Test(expected = RuntimeException.class)
 public void testInterrupt() {
 // Given

 // When
 partitionPlanJob.interrupt();

 // Then
 verify(log, times(1)).warn("Interrupt PartitionPlanJob is not supported.");
 throw new RuntimeException("Interrupt PartitionPlanJob is not supported.");
 }

 @Test
 public void testGetBean() {
 ArgumentCaptor<PartitionPlanJob> argumentCaptor = ArgumentCaptor.forClass(PartitionPlanJob.class);
 verify(SpringContextUtil, times(1)).getBean(argumentCaptor.capture());
 assertEquals(partitionPlanJob, argumentCaptor.getValue());
 }
}