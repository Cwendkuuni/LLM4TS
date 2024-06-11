package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
 }

 @Test
 public void testExecute_ValidSchedule_PartitionPlanExecuted() {
 // Arrange
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(Mockito.mock(TablePartitionPlanEntity.class),
 Mockito.mock(TablePartitionPlanEntity.class));
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 JsonUtils.setJsonValue(scheduleEntity, "jobParametersJson", JsonUtils.toJsonString(jobParameters));
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

 // Act
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 // Assert
 verify(partitionPlanServiceMock).getDatabasePartitionPlanById(1L);
 verify(partitionPlanServiceMock).getValidTablePlanByDatabasePartitionPlanId(1L);
 verify(partitionPlanTaskServiceMock).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
 tablePartitionPlans.toArray(new TablePartitionPlanEntity[0]));
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_ScheduleNotFound_JobTerminated() {
 // Arrange
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 doThrow(new Exception("Test Exception")).when(scheduleServiceMock).terminate(scheduleEntity);

 // Act & Assert
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() {
 // Act & Assert
 partitionPlanJob.interrupt();
 }
}