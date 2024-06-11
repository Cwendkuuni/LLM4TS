import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanService partitionPlanServiceMock;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private ScheduleService scheduleServiceMock;

 @Before
 public void setUp() {
 partitionPlanServiceMock = Mockito.mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = Mockito.mock(PartitionPlanTaskService.class);
 scheduleServiceMock = Mockito.mock(ScheduleService.class);
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanJob.partitionPlanService = partitionPlanServiceMock;
 partitionPlanJob.partitionPlanTaskService = partitionPlanTaskServiceMock;
 partitionPlanJob.scheduleService = scheduleServiceMock;
 }

 @Test
 public void testExecute() {
 // Given
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 List<TablePartitionPlanEntity> tablePartitionPlans = Mockito.mock(List.class);

 Mockito.when(scheduleServiceMock.nullSafeGetById(ArgumentMatchers.anyLong())).thenReturn(scheduleEntity);
 Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 Mockito.when(partitionPlanServiceMock.getDatabasePartitionPlanById(ArgumentMatchers.anyLong())).thenReturn(databasePartitionPlan);
 Mockito.when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(ArgumentMatchers.anyLong()))
 .thenReturn(tablePartitionPlans);

 // When
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 Mockito.verify(partitionPlanTaskServiceMock, Mockito.times(1)).executePartitionPlan(
 ArgumentMatchers.anyLong(), ArgumentMatchers.anyList());
 }

 @Test(expected = JobExecutionException.class)
 public void testExecuteWithException() {
 // Given
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 List<TablePartitionPlanEntity> tablePartitionPlans = Mockito.mock(List.class);

 Mockito.when(scheduleServiceMock.nullSafeGetById(ArgumentMatchers.anyLong())).thenReturn(scheduleEntity);
 Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 Mockito.when(partitionPlanServiceMock.getDatabasePartitionPlanById(ArgumentMatchers.anyLong())).thenReturn(databasePartitionPlan);
 Mockito.when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(ArgumentMatchers.anyLong()))
 .thenThrow(new RuntimeException("Test exception"));

 // When & Then
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));
 }

 // Add test cases for other methods and edge cases here
}