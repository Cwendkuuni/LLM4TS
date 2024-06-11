import org.junit.Test;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @Test
    public void testExecute_ValidScheduleId() {
        // Arrange
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class)).thenReturn(jobParameters);
        
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
        
        // Act
        partitionPlanJob.execute(context);
        
        // Assert
        verify(partitionPlanTaskService, times(0)).executePartitionPlan(anyLong(), anyList());
        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_InvalidScheduleId() {
        // Arrange
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(1L)).thenThrow(new Exception());
        
        // Act
        partitionPlanJob.execute(context);
        
        // Assert
        verify(partitionPlanTaskService, times(0)).executePartitionPlan(anyLong(), anyList());
        verify(scheduleService, times(0)).terminate(any(ScheduleEntity.class));
    }

    @Test
    public void testInterrupt() {
        // Assert
        assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
    }

}