import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionException;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @Test
    public void testExecute() {
        // Preparation of needed data and mocking the services 
        JobExecutionContext jobexecutionContext = mock(JobExecutionContext.class);
        PartitionPlanJob job = new PartitionPlanJob();

        doThrow(new IllegalArgumentException())
                .when(scheduleService)
                .nullSafeGetById(isA(Long.class));

        doThrow(new SchedulerException())
                .when(scheduleService)
                .terminate(isA(ScheduleEntity.class));

        doThrow(new NullPointerException())
                .when(partitionPlanService)
                .getDatabasePartitionPlanById(any());

        doThrow(new ArrayIndexOutOfBoundsException())
                .when(partitionPlanService)
                .getValidTablePlanByDatabasePartitionPlanId(any());

        doThrow(new UnExpectedException())
                .when(partitionPlanTaskService)
                .executePartitionPlan(any(), any());

        // Execution
        job.execute(jobexecutionContext);
        Mockito.verify(partitionPlanService).nullSafeGetById(anyLong());
        Mockito.verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(any());
        Mockito.verify(partitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(any());

        job.interrupt();
        Mockito.verifyNoMoreInteractions(partitionPlanService);
    }
}