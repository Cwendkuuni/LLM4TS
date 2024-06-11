import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private JobExecutionContext jobContext;
    private ScheduleService scheduleService;

    @Before
    public void setUp() {
        partitionPlanJob = new PartitionPlanJob();
        jobContext = mock(JobExecutionContext.class);
        scheduleService = mock(ScheduleService.class);
    }

    @Test
    public void testExecute() throws SchedulerException {
        // Typical use case
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":1}");
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlanEntity);
        List<TablePartitionPlanEntity> tablePartitionPlanEntities = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlanEntities);

        partitionPlanJob.execute(jobContext);

        // Edge cases
        // Disable database partition plan
        databasePartitionPlanEntity.setConfigEnabled(false);
        partitionPlanJob.execute(jobContext);

        // Empty table partition plans
        databasePartitionPlanEntity.setConfigEnabled(true);
        tablePartitionPlanEntities.clear();
        partitionPlanJob.execute(jobContext);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}