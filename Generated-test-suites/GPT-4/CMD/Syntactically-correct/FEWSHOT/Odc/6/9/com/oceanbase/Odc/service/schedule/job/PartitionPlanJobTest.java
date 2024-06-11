package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

public class PartitionPlanJobTest {
    @Test
    public void testExecute() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getMergedJobDataMap()).willReturn(null);
        when(ScheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);

        try {
            partitionPlanJob.execute(jobExecutionContext);
        } catch (Exception e) {
            fail("Execution should not throw exception");
        }
    }

    @Test
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        try {
            partitionPlanJob.interrupt();
            fail("Expected UnExpectedException to be thrown");
        } catch (UnExpectedException e) {
            assertNotNull(e);
        }
    }
}