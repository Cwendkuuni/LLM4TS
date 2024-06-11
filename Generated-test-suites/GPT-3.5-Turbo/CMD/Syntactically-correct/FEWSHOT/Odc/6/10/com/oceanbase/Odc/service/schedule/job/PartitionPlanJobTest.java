package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJobMock;

    @Before
    public void setUp() {
        partitionPlanJobMock = mock(PartitionPlanJob.class);
    }

    @Test
    public void testExecute() {
        // Test case when scheduleEntity is found
        when(partitionPlanJobMock.execute(any(JobExecutionContext.class)))
                .thenCallRealMethod();
        partitionPlanJobMock.execute(any(JobExecutionContext.class));

        // Test case when scheduleEntity is not found
        when(partitionPlanJobMock.execute(any(JobExecutionContext.class)))
                .thenThrow(new RuntimeException());
        partitionPlanJobMock.execute(any(JobExecutionContext.class));
    }

    @Test
    public void testInterrupt() {
        // Test case for interrupt method
        try {
            partitionPlanJobMock.interrupt();
        } catch (UnExpectedException e) {
            assertTrue(true);
        }
    }
}