
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    private PartitionPlanTaskService partitionPlanTaskService;
    private PartitionPlanService partitionPlanService;
    private ScheduleService scheduleService;

    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);

        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute() {
        // Test normal execution
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class)).thenReturn(jobParameters);

        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        // Verify the necessary method calls and log messages
        verify(scheduleService, times(1)).terminate(scheduleEntity);
        verify(partitionPlanTaskService, times(0)).executePartitionPlan(anyLong(), anyList());
        verify(log, times(1)).info(anyString());

        // Test exception scenario
        reset(log); // reset the mock log before testing exception scenario
        RuntimeException e = new RuntimeException("Test exception");
        when(partitionPlanTaskService.executePartitionPlan(anyLong(), anyList())).thenThrow(e);

        partitionPlanJob.execute(context);

        // Verify the log warning message for exception
        verify(log, times(1)).warn(anyString(), eq(e));
    }

    @Test
    public void testInterrupt() {
        // Test throwing of UnExpectedException
        assertThrows(UnExpectedException.class, () -> {
            partitionPlanJob.interrupt();
        });
    }
}

