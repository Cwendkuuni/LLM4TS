package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

import org.mockito.Mock;
import org.mockito.Mockito;

public class PartitionPlanJobTest {

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Test
    public void testExecute() throws SchedulerException {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.execute(jobExecutionContext);

        // Add your assertions here

    }

    @Test
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        UnExpectedException exception = null;
        try {
            partitionPlanJob.interrupt();
        } catch (UnExpectedException e) {
            exception = e;
        }

        assertNotNull(exception);
    }
}