package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.JsonNode;
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
 private ScheduleService scheduleServiceMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);

 partitionPlanJob = new PartitionPlanJob();
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);

 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
 }

 @Test
 public void testExecute_ValidSchedule_PartitionPlanIsExecuted() {
 // Given
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(UUID.randomUUID().toString());
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));

 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(UUID.randomUUID().toString());
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 tablePartitionPlans.add(new TablePartitionPlanEntity());

 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);

 // When
 partitionPlanJob.execute(createJobExecutionContext(scheduleId));

 // Then
 verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
 tablePartitionPlans);
 }

 @Test
 public void testExecute_InvalidSchedule_PartitionPlanIsNotExecuted() {
 // Given
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(UUID.randomUUID().toString());
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));

 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(UUID.randomUUID().toString());
 databasePartitionPlan.setConfigEnabled(false);

 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);

 // When
 partitionPlanJob.execute(createJobExecutionContext(scheduleId));

 // Then
 verify(partitionPlanTaskServiceMock, never()).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
 tablePartitionPlans);
 }

 @Test
 public void testExecute_NullSchedule_PartitionPlanIsNotExecuted() {
 // Given
 Long scheduleId = 1L;
 when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(null);

 // When
 partitionPlanJob.execute(createJobExecutionContext(scheduleId));

 // Then
 verify(partitionPlanTaskServiceMock, never()).executePartitionPlan(anyLong(), anyList());
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt_Interrupted() {
 // Given
 // When
 partitionPlanJob.interrupt();

 // Then
 // Thrown
 }

 private JobExecutionContext createJobExecutionContext(Long scheduleId) {
 ScheduleTaskUtils scheduleTaskUtils = new ScheduleTaskUtils();
 return scheduleTaskUtils.createJobExecutionContext(scheduleId);
 }
}