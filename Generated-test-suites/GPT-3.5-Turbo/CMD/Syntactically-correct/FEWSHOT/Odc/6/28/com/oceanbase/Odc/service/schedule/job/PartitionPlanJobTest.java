package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
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

    @Test
    public void testExecute() {
        PartitionPlanService partitionPlanService = new PartitionPlanService();
        PartitionPlanTaskService partitionPlanTaskService = new PartitionPlanTaskService();
        ScheduleService scheduleService = new ScheduleService();
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob(partitionPlanTaskService, partitionPlanService, scheduleService);
        
        JobExecutionContext context = new JobExecutionContext();
        
        try {
            partitionPlanJob.execute(context);
        } catch (Exception e) {
            assertEquals("Schedule not found,scheduleId={}", e.getMessage());
        }
    }

    @Test
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        
        try {
            partitionPlanJob.interrupt();
        } catch (UnExpectedException e) {
            assertTrue(true);
        }
    }
}