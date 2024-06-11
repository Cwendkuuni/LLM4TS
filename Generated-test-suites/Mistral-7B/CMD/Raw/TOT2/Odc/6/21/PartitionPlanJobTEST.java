package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import com.oceanbase.odc.service.schedule.PartitionPlanJob;
import com.oceanbase.odc.service.schedule.util.TestScheduleEntityUtil;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

@Slf4j
public class PartitionPlanJobTest {

 @Mock
 private PartitionPlanService partitionPlanService;

 @Mock
 private PartitionPlanTaskService partitionPlanTaskService;

 @Mock
 private ScheduleService scheduleService;

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecute_ValidSchedule_Success() throws Exception {
 // Given
 ScheduleEntity scheduleEntity = TestScheduleEntityUtil.createValidScheduleEntity();
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));
 when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setFlowInstanceId(1L);
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

 // When
 partitionPlanJob.execute(mock(JobExecutionContext.class));

 // Then
 verify(scheduleService, times(1)).terminate(scheduleEntity);
 verify(partitionPlanTaskService, times(1)).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
 }

 @Test
 public void testExecute_InvalidDatabasePartitionPlan_Disabled_NoTablePartitionPlans_SkipJob() throws Exception {
 // Given
 ScheduleEntity scheduleEntity = TestScheduleEntityUtil.createValidScheduleEntity();
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));
 when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setFlowInstanceId(1L);
 databasePartitionPlan.setConfigEnabled(false);
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList();
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

 // When
 partitionPlanJob.execute(mock(JobExecutionContext.class));

 // Then
 verify(scheduleService, times(1)).terminate(scheduleEntity);
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt_ExpectedException() {
 // Given
 // When
 partitionPlanJob.interrupt();

 // Then
 // Verify
 }

 @Test
 public void testInterrupt_UnexpectedException() {
 // Given
 // When & Then
 doThrow(new RuntimeException()).when(partitionPlanJob).interrupt();
 assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
 }
}