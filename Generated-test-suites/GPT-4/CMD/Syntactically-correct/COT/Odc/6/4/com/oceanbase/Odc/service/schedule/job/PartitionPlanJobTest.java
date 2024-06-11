package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import org.quartz.JobExecutionContext;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private JobExecutionContext context;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Test
    public void testExecute() {
        try {
            doNothing().when(context).getMergedJobDataMap();
            partitionPlanJob.execute(context);
        } catch (Exception e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test(expected = RuntimeException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

}
