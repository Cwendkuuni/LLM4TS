
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    PartitionPlanTaskService mockPartitionPlanTaskService = mock(PartitionPlanTaskService.class);
    PartitionPlanService mockPartitionPlanService = mock(PartitionPlanService.class);
    ScheduleService mockScheduleService = mock(ScheduleService.class);

    PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

    @Test
    public void testExecute() {
        // Create test data
        JobExecutionContext mockContext = mock(JobExecutionContext.class);
        ScheduleEntity mockScheduleEntity = mock(ScheduleEntity.class);
        Long scheduleId = 1L;
        
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        
        DatabasePartitionPlanEntity mockDatabasePartitionPlanEntity = mock(DatabasePartitionPlanEntity.class);
        when(mockPartitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(mockDatabasePartitionPlanEntity);
        
        List<TablePartitionPlanEntity> mockTablePartitionPlanEntities = new ArrayList<>();
        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(mockTablePartitionPlanEntities);
        
        // Mock scheduleEntity
        when(mockScheduleService.nullSafeGetById(scheduleId)).thenReturn(mockScheduleEntity);
        
        // Test the execute method
        partitionPlanJob.execute(mockContext);
        
        // Verify certain methods are called
        verify(mockPartitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
        verify(mockScheduleService, times(1)).terminate(mockScheduleEntity);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_Exception() throws SchedulerException {
        JobExecutionContext mockContext = mock(JobExecutionContext.class);
        ScheduleEntity mockScheduleEntity = mock(ScheduleEntity.class);
        Long scheduleId = 1L;
        
        // Mock scheduleEntity
        when(mockScheduleService.nullSafeGetById(scheduleId)).thenThrow(new Exception());
        
        // Test the execute method to simulate exception
        partitionPlanJob.execute(mockContext);
    }

    @Test
    public void testInterrupt() {
        // Test the interrupt method to simulate UnExpectedException
        assertThrows(UnExpectedException.class, () -> {
            partitionPlanJob.interrupt();
        });
    }

}
