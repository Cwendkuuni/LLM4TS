import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanJob.partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanJob.scheduleService = mock(ScheduleService.class);
    }

    @Test
    public void testExecute() {
        // Mock data
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        // mocking methods
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(new DatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(List.of(new TablePartitionPlanEntity()));

        // Testing execute method
        partitionPlanJob.execute(context);

        // Verify the methods are called at least once
        verify(scheduleService, atLeastOnce()).terminate(scheduleEntity);
        verify(partitionPlanTaskService, atLeastOnce()).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void testInterrupt() {
        // Testing interrupt method
        try {
            partitionPlanJob.interrupt();
            fail("Expected UnExpectedException to be thrown");
        } catch (UnExpectedException e) {
            // success
        }
    }
}