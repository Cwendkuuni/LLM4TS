package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @Mock
    private JobExecutionContext context;
    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;
    @Mock
    private PartitionPlanService partitionPlanService;
    @Mock
    private ScheduleService scheduleService;

    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecuteWithValidSchedule() {
        // Assume valid inputs, verify that the services get called appropriately
    }

    @Test
    public void testExecuteWithInvalidSchedule() {
        // Assume that the schedule does not exist, verify that an exception is thrown
    }

    @Test
    public void testExecuteWithDisabledConfigOrEmptyTablePartitionPlans() {
        // Assume that the database partition plan is disable and the table partition plans are empty.
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}