
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.junit.Assert;
import java.util.Arrays;
import java.util.ArrayList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    private PartitionPlanService partitionPlanService;
    private ScheduleService scheduleService;
    private PartitionPlanTaskService partitionPlanTaskService;
    private JobExecutionContext jobExecutionContext;
    private PartitionPlanJob partitionPlanJob;

    @BeforeEach
    public void setUp() {
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        jobExecutionContext = mock(JobExecutionContext.class);
        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute() {
        when(ScheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(any(Long.class))).thenReturn(mock(ScheduleEntity.class));
        
        DatabasePartitionPlanEntity mockDBPartPlan = mock(DatabasePartitionPlanEntity.class);
        when(mockDBPartPlan.isConfigEnabled()).thenReturn(true);
        
        TablePartitionPlanEntity mockTablePartPlan = mock(TablePartitionPlanEntity.class);
        List<TablePartitionPlanEntity> tableList = new ArrayList<TablePartitionPlanEntity>();
        tableList.add(mockTablePartPlan);
        
        when(partitionPlanService.getDatabasePartitionPlanById(any(Long.class))).thenReturn(mockDBPartPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(any(Long.class))).thenReturn(tableList);
        
        partitionPlanJob.execute(jobExecutionContext);
        
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(any(String.class), anyList());
    }
    
    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}
