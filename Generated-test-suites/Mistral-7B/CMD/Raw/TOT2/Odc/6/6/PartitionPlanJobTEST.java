package com.oceanbase.odc.service.schedule.job;

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
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

@Slf4j
public class PartitionPlanJobTest {

 @Mock
 private PartitionPlanService partitionPlanService;

 @Mock
 private PartitionPlanTaskService partitionPlanTaskService;

 @Mock
 private ScheduleService scheduleService;

 @Mock
 private JobExecutionContext context;

 @InjectMocks
 private PartitionPlanJob job;

 @Before
 public void setUp() {
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanService);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskService);
 SpringContextUtil.setBean(ScheduleService.class, scheduleService);
 }

 @Test
 public void testExecute_ValidSchedule_Success() {
 // Given
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());

 when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

 // When
 job.execute(context);

 // Then
 verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), eq(tablePartitionPlans));
 }

 @Test(expected = UnExpectedException.class)
 public void testExecute_ScheduleNotFound_ThrowsException() {
 // Given
 when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new SchedulerException());

 // When & Then
 job.execute(context);
 }

 @Test
 public void testExecute_DatabasePartitionPlanDisabled_StopJob() {
 // Given
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(false);
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());

 when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

 // When
 job.execute(context);

 // Then
 verify(scheduleService, times(1)).terminate(scheduleEntity);
 }

 @Test(expected = SchedulerException.class)
 public void testExecute_PartitionPlanTaskServiceException_TerminateJob() {
 // Given
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());

 when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
 when(partitionPlanTaskService.executePartitionPlan(anyLong(), eq(tablePartitionPlans))).thenThrow(new SchedulerException());

 // When
 job.execute(context);

 // Then
 verify(scheduleService, times(1)).terminate(scheduleEntity);
 }
}