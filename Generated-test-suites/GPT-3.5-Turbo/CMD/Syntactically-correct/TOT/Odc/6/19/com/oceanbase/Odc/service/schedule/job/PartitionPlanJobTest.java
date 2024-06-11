
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @Test
    public void testExecute_ValidScheduleId() {
        // Test when a valid scheduleId is provided
        // Create mock objects
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = mock(PartitionPlanJobParameters.class);
        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(JsonUtils.fromJson(anyString(), any())).thenReturn(jobParameters);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

        // Execute the method
        partitionPlanJob.execute(context);

        // Verify the calls
        verify(scheduleService).nullSafeGetById(anyLong());
        verify(partitionPlanService).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService).getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(partitionPlanTaskService).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void testExecute_InvalidScheduleId() {
        // Test when an invalid scheduleId is provided
        // Create mock objects
        JobExecutionContext context = mock(JobExecutionContext.class);

        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new RuntimeException());

        // Execute the method
        partitionPlanJob.execute(context);

        // Verify the call
        verify(scheduleService).nullSafeGetById(anyLong());
        verify(log).warn(anyString(), anyLong);
    }

    @Test
    public void testInterrupt() {
        // Test the interrupt method
        assertThrows(UnExpectedException.class, () -> {
            partitionPlanJob.interrupt();
        });
    }
}
