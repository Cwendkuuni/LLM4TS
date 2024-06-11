package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import org.quartz.JobExecutionContext;
import org.junit.Test;
import org.quartz.SchedulerException;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import java.util.Collections;
import org.mockito.Mock;

public class PartitionPlanJobTest {

    @Mock
    PartitionPlanJob partitionPlanJob;

    @Mock
    JobExecutionContext context;

    @Mock
    ScheduleEntity scheduleEntity;

    @Test
    public void testExecute() {
        try {
            assertDoesNotThrow(() -> partitionPlanJob.execute(context));
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

    //  Edge cases and exception handling scenarios
    @Test(expected = SchedulerException.class)
    public void testExecuteAndTerminate() {
        when(partitionPlanJob.execute(context)).thenThrow(SchedulerException.class);
    }

    @Test
    public void testExecuteNoTablePartitionPlans() {
        when(partitionPlanJob.execute(context)).thenReturn(Collections.emptyList());
        assertDoesNotThrow(() -> partitionPlanJob.execute(context));
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteAndFail() {
        when(partitionPlanJob.execute(context)).thenThrow(RuntimeException.class);
    }

}
