import org.junit.Test;
import org.junit.Before;
import static org.mockito.Mockito.*;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.schedule.ScheduleService;

public class PartitionPlanJobTest {

    PartitionPlanTaskService partitionPlanTaskService;
    PartitionPlanService partitionPlanService;
    ScheduleService scheduleService;
    JobExecutionContext context;
    
    @Before
    public void setUp(){
    
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);
        context = mock(JobExecutionContext.class);
        
        when(partitionPlanTaskService.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(partitionPlanService.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(scheduleService.getBean(ScheduleService.class)).thenReturn(scheduleService);
        
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute_typicalCase() {
        Long scheduleId = 1L; // A typical scenario would stub the ScheduleId
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService,times(1)).executePartitionPlan( any(), any());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt_exceptionCase() {
        partitionPlanJob.interrupt();
    }
}