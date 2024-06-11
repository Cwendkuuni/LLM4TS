import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

public class PartitionPlanJobTest {

    private PartitionPlanTaskService partitionPlanTaskService;
    private PartitionPlanService partitionPlanService;
    private ScheduleService scheduleService;

    @Test
    public void testExecute_NormalCase() throws SchedulerException {
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{ \"databasePartitionPlanId\": 1 }");
        Mockito.when(scheduleService.nullSafeGetById(1)).thenReturn(scheduleEntity);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        Mockito.when(partitionPlanService.getDatabasePartitionPlanById(1)).thenReturn(databasePartitionPlan);

        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        Mockito.when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1)).thenReturn(List.of(tablePartitionPlan));

        PartitionPlanJob job = new PartitionPlanJob();
        job.execute(context);

        // Add assertions to verify the expected behavior
    }

    @Test
    public void testExecute_StopJobWhenDatabasePlanDisabled() throws SchedulerException {
        // Simulate databasePartitionPlan is disabled
        // Call execute method 
        // Assert that the job is terminated as expected
    }

    @Test
    public void testInterrupt_UnexpectedExceptionThrown() {
        PartitionPlanJob job = new PartitionPlanJob();
        assertThrows(UnExpectedException.class, () -> {
            job.interrupt();
        });
    }
}