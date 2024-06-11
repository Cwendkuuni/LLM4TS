import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.tools.migrator.common.exception.UnExpectedException;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private JobExecutionContext context;
    
    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;
    
    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private DatabasePartitionPlanEntity databasePartitionPlan;

    @Test
    public void testExecute() {
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        
        PartitionPlanJob job = new PartitionPlanJob();
        job.execute(context);

        verify(scheduleService, times(1)).nullSafeGetById(anyLong());
        verify(partitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void testInterrupt() {
        PartitionPlanJob job = new PartitionPlanJob();
        
        try {
            job.interrupt();
            fail("Expected UnExpectedException");
        } catch (UnExpectedException e) {
            // Expected
        }
    }

    // Add more test cases for edge cases and exception scenarios

}