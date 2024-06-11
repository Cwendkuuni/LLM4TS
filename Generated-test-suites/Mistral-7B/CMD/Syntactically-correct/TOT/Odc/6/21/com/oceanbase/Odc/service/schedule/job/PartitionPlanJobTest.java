import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private PartitionPlanService partitionPlanServiceMock;
 private ScheduleService scheduleServiceMock;

 @Before
 public void setup() {
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanTaskServiceMock = Mockito.mock(PartitionPlanTaskService.class);
 partitionPlanServiceMock = Mockito.mock(PartitionPlanService.class);
 scheduleServiceMock = Mockito.mock(ScheduleService.class);

 partitionPlanJob.partitionPlanTaskService = partitionPlanTaskServiceMock;
 partitionPlanJob.partitionPlanService = partitionPlanServiceMock;
 partitionPlanJob.scheduleService = scheduleServiceMock;
 }

 @Test
 public void execute_ValidSchedule_ShouldCallPartitionPlanTaskService() {
 // given
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 List<TablePartitionPlanEntity> tablePartitionPlans = Mockito.mock(List.class);

 when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
 when(tablePartitionPlans.isEmpty()).thenReturn(false);

 // when
 partitionPlanJob.execute(new JobExecutionContext());

 // then
 verify(partitionPlanTaskServiceMock).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
 }

 @Test
 public void execute_InvalidSchedule_ShouldNotCallPartitionPlanTaskService() {
 // given
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 List<TablePartitionPlanEntity> tablePartitionPlans = Mockito.mock(List.class);

 when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

 // when
 partitionPlanJob.execute(new JobExecutionContext());

 // then
 verifyZeroInteractions(partitionPlanTaskServiceMock);
 }

 @Test
 public void interrupt_ShouldNotBeInterrupted() {
 // given

 // when & then
 assertFalse(partitionPlanJob.interrupt());
 }

 @Test(expected = UnExpectedException.class)
 public void interrupt_ShouldThrowUnExpectedException() {
 // given

 // when
 partitionPlanJob.interrupt();

 // then
 // This exception should be thrown
 }
}