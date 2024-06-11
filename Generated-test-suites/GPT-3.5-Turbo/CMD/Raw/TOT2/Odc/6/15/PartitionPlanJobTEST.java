import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;
    
    @Mock
    private PartitionPlanService partitionPlanService;
    
    @Mock
    private ScheduleService scheduleService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        // Typical use case
        // Mock scheduleId
        Long scheduleId = 1L;
        
        // Mock scheduleEntity
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        
        // Mock jobParameters
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        
        // Mock databasePartitionPlan
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        
        // Mock tablePartitionPlans
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
        
        // Expected method calls
        partitionPlanTaskService.executePartitionPlan(anyLong(), anyList());
        scheduleService.terminate(scheduleEntity);
        
        // Call the method
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.execute(jobExecutionContext);
        
        // Verify the calls
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_FailureToTerminate() throws SchedulerException {
        // Mock scheduleId
        Long scheduleId = 1L;
        
        // Mock scheduleEntity
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        
        // Mock jobParameters
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        
        // Mock databasePartitionPlan
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        
        // Mock tablePartitionPlans
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
        
        // Throw exception when terminating schedule
        doThrow(new SchedulerException()).when(scheduleService).terminate(scheduleEntity);
        
        // Call the method
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.execute(jobExecutionContext);
    }

    @Test
    public void testInterrupt() {
        // Test unexpected exception
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        assertThrows(UnExpectedException.class, partitionPlanJob::interrupt);
    }
}