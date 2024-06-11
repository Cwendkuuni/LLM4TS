import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mockito.Mockito;

public class PartitionPlanJobTest {

    private ScheduleService mockScheduleService = Mockito.mock(ScheduleService.class);
    private PartitionPlanTaskService mockPartitionPlanTaskService = Mockito.mock(PartitionPlanTaskService.class);
    private PartitionPlanService mockPartitionPlanService = Mockito.mock(PartitionPlanService.class);

    @Test
    public void testExecute() {
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        
        PartitionPlanJob job = new PartitionPlanJob();
        job.setScheduleService(mockScheduleService);
        job.setPartitionPlanTaskService(mockPartitionPlanTaskService);
        job.setPartitionPlanService(mockPartitionPlanService);

        // Test when scheduleService.getById() returns a valid scheduleEntity
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        Mockito.when(mockScheduleService.getById(Mockito.anyLong())).thenReturn(scheduleEntity);

        // Test when there are valid databasePartitionPlan and tablePartitionPlans
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        Mockito.when(mockPartitionPlanService.getDatabasePartitionPlanById(Mockito.anyLong())).thenReturn(Mockito.mock(DatabasePartitionPlanEntity.class));
        Mockito.when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(Mockito.anyLong())).thenReturn(Mockito.mock(List.class));

        // Mock executePartitionPlan method to test if it gets called
        job.execute(context);
        Mockito.verify(mockPartitionPlanTaskService, Mockito.times(1)).executePartitionPlan(Mockito.anyLong(), Mockito.anyList());

        // Test when databasePartitionPlan is disabled or no tablePartitionPlans
        DatabasePartitionPlanEntity disabledDatabasePlan = Mockito.mock(DatabasePartitionPlanEntity.class);
        List<TablePartitionPlanEntity> emptyTablePlans = Mockito.mock(List.class);
        Mockito.when(disabledDatabasePlan.isConfigEnabled()).thenReturn(false);
        Mockito.when(mockPartitionPlanService.getDatabasePartitionPlanById(Mockito.anyLong())).thenReturn(disabledDatabasePlan);
        Mockito.when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(Mockito.anyLong())).thenReturn(emptyTablePlans);

        // Test if ScheduleService.terminate() gets called when databasePartitionPlan is disabled
        job.execute(context);
        Mockito.verify(mockScheduleService, Mockito.times(1)).terminate(Mockito.any());

        // Test when an exception is thrown during executePartitionPlan
        Mockito.when(mockPartitionPlanTaskService.executePartitionPlan(Mockito.anyLong(), Mockito.anyList())).thenThrow(new RuntimeException("Test Exception"));

        // Test if an exception is caught and logged
        job.execute(context); // No assertion needed, just check if the log statement is reached
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        PartitionPlanJob job = new PartitionPlanJob();
        job.interrupt(); // This method should throw UnExpectedException
    }
}