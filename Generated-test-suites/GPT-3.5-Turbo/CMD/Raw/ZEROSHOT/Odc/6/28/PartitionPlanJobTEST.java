import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {
    
    @InjectMocks
    private PartitionPlanJob partitionPlanJob;
    
    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;
    
    @Mock
    private PartitionPlanService partitionPlanService;
    
    @Mock
    private ScheduleService scheduleService;
    
    @Before
    public void setUp() {
        partitionPlanJob = new PartitionPlanJob();
    }
    
    @Test
    public void testExecute() {
        // Mock data
        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        
        ScheduleEntity scheduleEntity = new ScheduleEntity(); // mock ScheduleEntity object
        
        // Mock behavior
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        
        // Execute the method
        partitionPlanJob.execute(context);
        
        // Verify the behavior
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(scheduleService, times(1)).terminate(scheduleEntity);
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
    }
    
    @Test
    public void testInterrupt() {
        try {
            partitionPlanJob.interrupt();
            fail("Expected UnExpectedException to be thrown");
        } catch (UnExpectedException e) {
            // Expected behavior
        }
    }
}