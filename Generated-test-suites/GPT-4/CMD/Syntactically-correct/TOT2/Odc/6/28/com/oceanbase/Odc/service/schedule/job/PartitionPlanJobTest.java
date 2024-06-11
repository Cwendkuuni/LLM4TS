
package com.oceanbase.odc.service.schedule.job;

import org.junit.BeforeClass;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @Mock
    private static JobExecutionContext context;

    private static PartitionPlanJob partitionPlanJob;

    @BeforeClass
    public static void setup() {
        MockitoAnnotations.initMocks(PartitionPlanJobTest.class);
        partitionPlanJob = new PartitionPlanJob();
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_ScheduleNotFoundException() {
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        partitionPlanJob.execute(context);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_ScheduleTerminateException() {
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        partitionPlanJob.execute(context);
    }

    @Test
    public void testExecute_successfulScheduleExecution() {
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        partitionPlanJob.execute(context);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

}
