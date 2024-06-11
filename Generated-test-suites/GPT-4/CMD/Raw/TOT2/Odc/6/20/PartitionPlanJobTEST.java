
package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.schedule.ScheduleService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;
    @Mock
    private PartitionPlanService partitionPlanService;
    @Mock
    private ScheduleService scheduleService;
    @InjectMocks
    private PartitionPlanJob partitionPlanJob;
    @Mock
    private JobExecutionContext context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() {
        // action
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        // assert
        assertNotNull(partitionPlanJob);
    }

    // Assumed testing executes the execute method
    @Test
    public void testExecute() {
        partitionPlanJob.execute(context);
        verify(partitionPlanTaskService, times(1)).execute();
    }

    // Testing interrupt method
    @Test(expected = RuntimeException.class)
    public void testInterruptTask(){
        partitionPlanJob.interrupt();
    }
}
