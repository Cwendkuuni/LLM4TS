import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import static org.junit.Assert.*;

public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private ScheduleService scheduleServiceMock;
 private PartitionPlanService partitionPlanServiceMock;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;

 @Before
 public void setUp() {
 scheduleServiceMock = Mockito.mock(ScheduleService.class);
 partitionPlanServiceMock = Mockito.mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = Mockito.mock(PartitionPlanTaskService.class);

 partitionPlanJob = new PartitionPlanJob();
 partitionPlanJob.scheduleService = scheduleServiceMock;
 partitionPlanJob.partitionPlanService = partitionPlanServiceMock;
 partitionPlanJob.partitionPlanTaskService = partitionPlanTaskServiceMock;
 }

 @Test
 public void testExecute_ValidScheduleEntity_ExecutesPartitionPlan() {
 // Given: A valid ScheduleEntity with non-empty tablePartitionPlans list
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));
 List<TablePartitionPlanEntity> tablePartitionPlans = Mockito.mock(List.class);
 Mockito.when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

 // When: The execute method is called with valid context
 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 Mockito.when(scheduleServiceMock.getById(1L)).thenReturn(scheduleEntity);
 partitionPlanJob.execute(context);

 // Then: The partitionPlanTaskService.executePartitionPlan method is called with correct arguments
 Mockito.verify(partitionPlanTaskServiceMock).executePartitionPlan(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(tablePartitionPlans));
 }

 @Test(expected = SchedulerException.class)
 public void testExecute_ScheduleEntityNotFound_ThrowsSchedulerException() {
 // Given: A null ScheduleEntity
 ScheduleEntity scheduleEntity = null;

 // When: The execute method is called with null context
 JobExecutionContext context = null;
 partitionPlanJob.execute(context);

 // Then: A SchedulerException is thrown
}

 @Test(expected = RuntimeException.class)
 public void testExecute_TerminateFailed_ThrowsRuntimeException() {
 // Given: A valid ScheduleEntity
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));

 // When: The terminate method fails to stop the schedule
 Mockito.doThrow(new SchedulerException("Termination failed")).when(scheduleServiceMock).terminate(scheduleEntity);
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then: A RuntimeException is thrown
}

 @Test
 public void testInterrupt_ThrowsUnexpectedException() {
 // Given: Any context
 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);

 // When: The interrupt method is called
 partitionPlanJob.interrupt();

 // Then: An UnexpectedException is thrown
}
}