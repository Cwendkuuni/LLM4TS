import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
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

    private PartitionPlanTaskService mockPartitionPlanTaskService;
    private PartitionPlanService mockPartitionPlanService;
    private ScheduleService mockScheduleService;
    private JobExecutionContext mockJobExecutionContext;

    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        mockPartitionPlanTaskService = mock(PartitionPlanTaskService.class);
        mockPartitionPlanService = mock(PartitionPlanService.class);
        mockScheduleService = mock(ScheduleService.class);
        mockJobExecutionContext = mock(JobExecutionContext.class);

        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute() throws SchedulerException {
        // Test typical case
        when(mockPartitionPlanService.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(new DatabasePartitionPlanEntity());

        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(List.of(new TablePartitionPlanEntity()));

        partitionPlanJob.execute(mockJobExecutionContext);

        // Add test cases for edge cases
        // Test when database partition plan is disabled
        DatabasePartitionPlanEntity disabledDatabasePartitionPlan = new DatabasePartitionPlanEntity();
        disabledDatabasePartitionPlan.setConfigEnabled(false);

        when(mockPartitionPlanService.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(disabledDatabasePartitionPlan);

        doNothing().when(mockScheduleService).terminate(any(ScheduleEntity.class));

        partitionPlanJob.execute(mockJobExecutionContext);

        // Test when no tables need to be processed
        when(mockPartitionPlanService.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(new DatabasePartitionPlanEntity());
        
        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(List.of());

        partitionPlanJob.execute(mockJobExecutionContext);

        // Add test cases for exception handling
        // Test when exception is thrown
        when(mockPartitionPlanService.getDatabasePartitionPlanById(anyLong()))
                .thenThrow(new RuntimeException());

        partitionPlanJob.execute(mockJobExecutionContext);
    }

    @Test
    public void testInterrupt() {
        assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
    }

}