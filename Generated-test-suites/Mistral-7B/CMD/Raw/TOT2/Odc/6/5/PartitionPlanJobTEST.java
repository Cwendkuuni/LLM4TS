package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleEntity;
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
 partitionPlanServiceMock = Mockito.mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = Mockito.mock(PartitionPlanTaskService.class);
 scheduleServiceMock = Mockito.mock(ScheduleService.class);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
 }

 @Test
 public void testExecute_databasePartitionPlanIsDisabled_scheduleIsTerminated() throws Exception {
 // Given
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 when(scheduleServiceMock.nullSafeGetById(Long.parseLong("1"))).thenReturn(scheduleEntity);

 // When
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 verify(scheduleServiceMock).terminate(scheduleEntity);
 }

 @Test
 public void testExecute_noTableToProcess_scheduleIsTerminated() throws Exception {
 // Given
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = Collections.emptyList();
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 when(scheduleServiceMock.nullSafeGetById(Long.parseLong("1"))).thenReturn(scheduleEntity);

 // When
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 verify(scheduleServiceMock).terminate(scheduleEntity);
 }

 @Test
 public void testExecute_databasePartitionPlanIsEnabledAndTableToProcess_partitionPlanIsExecuted() throws Exception {
 // Given
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(Mockito.mock(TablePartitionPlanEntity.class),
 Mockito.mock(TablePartitionPlanEntity.class));
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 when(scheduleServiceMock.nullSafeGetById(Long.parseLong("1"))).thenReturn(scheduleEntity);

 // When
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
 tablePartitionPlans);
 }
}