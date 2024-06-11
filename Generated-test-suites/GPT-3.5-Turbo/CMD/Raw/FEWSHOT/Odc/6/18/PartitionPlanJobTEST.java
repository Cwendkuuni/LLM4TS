import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.schedule.ScheduleService;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import java.util.Arrays;
import java.util.List;

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
        JobExecutionContext context = mock(JobExecutionContext.class);

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(new ScheduleEntity());
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(new DatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(Arrays.asList(new TablePartitionPlanEntity()));

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void testInterrupt() {
        try {
            partitionPlanJob.interrupt();
        } catch (UnExpectedException e) {
            assertNotNull(e);
        }
    }
}