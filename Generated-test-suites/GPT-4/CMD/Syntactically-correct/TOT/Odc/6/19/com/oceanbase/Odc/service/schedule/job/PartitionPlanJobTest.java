package com.oceanbase.odc.service.schedule.job;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {
    private PartitionPlanJob partitionPlanJob;
    private JobExecutionContext mockContext;

    @Before
    public void setup() {
        partitionPlanJob = new PartitionPlanJob();
        mockContext = mock(JobExecutionContext.class);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

    @Test
    public void testExecuteWithException() {
        doThrow(new SchedulerException()).when(mockContext).put(anyString(), any());

        try {
            partitionPlanJob.execute(mockContext);
        } catch (Exception ignored) {}

    }

    @Test
    public void testScheduleEntityFoundWithoutThrowingException() {
        doNothing().when(mockContext).put(anyString(), any());

        try {
            partitionPlanJob.execute(mockContext);
        } catch (Exception e) {
            Assert.fail();
        }

    }

}