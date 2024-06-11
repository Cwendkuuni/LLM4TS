package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

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

    @Test
    public void testExecute() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        PartitionPlanJob job = new PartitionPlanJob();
        assertNotNull(job);

        try {
            job.execute(context);
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testInterrupt() {
        PartitionPlanJob job = new PartitionPlanJob();
        assertNotNull(job);

        try {
            job.interrupt();
        } catch (UnExpectedException e) {
            assertNotNull(e);
        }
    }
}