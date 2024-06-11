
package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.quartz.JobExecutionContext;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    JobExecutionContext context;

    @Mock
    ScheduleService scheduleService;

    @Mock
    PartitionPlanService partitionPlanService;

    @Mock
    PartitionPlanTaskService partitionPlanTaskService;

    @Before
    public void setUp() {
        // Initialize mocks here, if needed...
    }

    @Test
    public void testExecute() {
        // prepare
        PartitionPlanJob job = new PartitionPlanJob();

        // act
        job.execute(context);

        // assert
        verify(scheduleService, times(1)).nullSafeGetById(anyLong());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        // prepare
        PartitionPlanJob job = new PartitionPlanJob();

        // act
        job.interrupt();
    }
}
