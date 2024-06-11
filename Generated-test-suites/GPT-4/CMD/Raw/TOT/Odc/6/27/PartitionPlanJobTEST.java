import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {
    private PartitionPlanJob partitionPlanJob;
    private JobExecutionContext mockJobExecutionContext;
    private PartitionPlanTaskService mockPartitionPlanTaskService;
    private PartitionPlanService mockPartitionPlanService;
    private ScheduleService mockScheduleService;

    @Before
    public void setUp() {
        mockJobExecutionContext = mock(JobExecutionContext.class);
        mockPartitionPlanTaskService = mock(PartitionPlanTaskService.class);
        mockPartitionPlanService = mock(PartitionPlanService.class);
        mockScheduleService = mock(ScheduleService.class);
        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute() {
        when(ScheduleTaskUtils.getScheduleId(mockJobExecutionContext)).thenReturn(10L);
        //...setup necessary when-thenReturn sequences to simulate the behavior.
        partitionPlanJob.execute(mockJobExecutionContext);
        //...perform assertions and verifications
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
        //...perform assertions and verifications
    }

    @Test
    public void testExecuteExceptions() {
        when(ScheduleTaskUtils.getScheduleId(mockJobExecutionContext)).thenThrow(SchedulerException.class);
        partitionPlanJob.execute(mockJobExecutionContext);
        //...perform assertions and verifications
    }
}