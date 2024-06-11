package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

public class PartitionPlanJobTest {

    private PartitionPlanTaskService partitionPlanTaskService;
    private PartitionPlanService partitionPlanService;
    private ScheduleService scheduleService;

    @Before
    public void setUp() {
        partitionPlanTaskService = new PartitionPlanTaskService();
        partitionPlanService = new PartitionPlanService();
        scheduleService = new ScheduleService();
    }

    @Test
    public void testExecute() throws SchedulerException {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        // Mock JobExecutionContext
        JobExecutionContext context = new JobExecutionContext();

        // Test when scheduleId is not found
        assertThrows(Exception.class, () -> partitionPlanJob.execute(context));

        // Test when database partition plan is disabled
        // Add your test logic here

        // Test when no table needs to be processed
        // Add your test logic here

        // Test when everything is valid
        // Add your test logic here
    }

    @Test
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
    }
}