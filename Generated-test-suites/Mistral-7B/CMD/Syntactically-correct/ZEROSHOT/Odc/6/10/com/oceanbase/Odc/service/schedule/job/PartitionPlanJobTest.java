import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.google.gson.Gson;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

@SuppressWarnings("unchecked")
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
 public void testExecute_ValidSchedule_ShouldPartitionDatabaseAndTables() {
 // Given
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(UUID.randomUUID().toString());
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobParametersJson(new Gson().toJson(jobParameters));
 when(scheduleServiceMock.nullSafeGetById(anyLong()))
 .thenReturn(scheduleEntity);

 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(UUID.randomUUID().toString());
 databasePartitionPlan.setConfigEnabled(true);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyString()))
 .thenReturn(databasePartitionPlan);

 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 TablePartitionPlanEntity tablePartitionPlan1 = new TablePartitionPlanEntity();
 tablePartitionPlan1.setId(UUID.randomUUID().toString());
 tablePartitionPlan1.setDatabasePartitionPlanId(databasePartitionPlan.getId());
 tablePartitionPlan1.setTableName("test_table1");
 tablePartitionPlans.add(tablePartitionPlan1);
 TablePartitionPlanEntity tablePartitionPlan2 = new TablePartitionPlanEntity();
 tablePartitionPlan2.setId(UUID.randomUUID().toString());
 tablePartitionPlan2.setDatabasePartitionPlanId(databasePartitionPlan.getId());
 tablePartitionPlan2.setTableName("test_table2");
 tablePartitionPlans.add(tablePartitionPlan2);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyString()))
 .thenReturn(tablePartitionPlans);

 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getMergedJobDataMap().getString("scheduleId"))
 .thenReturn("1");

 // When
 partitionPlanJob.execute(context);

 // Then
 verify(partitionPlanTaskServiceMock).executePartitionPlan(anyString(), eq(tablePartitionPlans));
 }

 @Test
 public void testExecute_InvalidSchedule_ShouldNotPartitionDatabaseAndTables() {
 // Given
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 when(scheduleServiceMock.nullSafeGetById(anyLong()))
 .thenReturn(scheduleEntity);

 // When
 partitionPlanJob.execute(mock(JobExecutionContext.class));

 // Then
 // No partitioning call
 verify(partitionPlanTaskServiceMock, never()).executePartitionPlan(anyString(), eq(anyList()));
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() {
 // Given

 // When
 partitionPlanJob.interrupt();

 // Then
 // UnexpectedException is thrown
 }
}