import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.jobstore.jobo.JobDataMap;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
 }

 @Test
 public void testExecute_ValidSchedule_NoDatabasePartitionPlanOrValidTablePartitionPlans_StopJob() {
 // Given
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 List<TablePartitionPlanEntity> tablePartitionPlans = null;
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);
 when(partitionPlanTaskServiceMock.executePartitionPlan(anyLong(), any())).thenThrow(new RuntimeException());

 // When
 partitionPlanJob.execute(mock(JobExecutionContext.class));

 // Then
 assertTrue(scheduleEntity.isTerminated());
 }

 @Test
 public void testExecute_ValidSchedule_DatabasePartitionPlanAndValidTablePartitionPlans_ExecuteTask() {
 // Given
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setFlowInstanceId(1L);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 List<TablePartitionPlanEntity> tablePartitionPlans = new java.util.ArrayList<>();
 tablePartitionPlans.add(new TablePartitionPlanEntity());
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
 when(partitionPlanTaskServiceMock.executePartitionPlan(anyLong(), any()))
 .thenAnswer(invocation -> invocation.getArguments()[0]);

 // When
 partitionPlanJob.execute(mock(JobExecutionContext.class));

 // Then
 assertEquals(databasePartitionPlan.getFlowInstanceId(), 1L);
 assertFalse(scheduleEntity.isTerminated());
 }
}