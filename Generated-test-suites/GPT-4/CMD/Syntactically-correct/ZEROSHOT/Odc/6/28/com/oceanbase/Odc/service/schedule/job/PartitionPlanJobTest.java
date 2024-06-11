package com.oceanbase.odc.service.schedule.job;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.schedule.ScheduleService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Collections;
import static org.mockito.Mockito.*;

import org.quartz.JobExecutionContext;

@RunWith(JUnit4.class)
public class PartitionPlanJobTest {

    @Test
    public void executeTest() throws Exception {
        // Setup
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        JobExecutionContext executionContext = mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = mock(PartitionPlanJobParameters.class);
        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        TablePartitionPlanEntity tablePartitionPlan = mock(TablePartitionPlanEntity.class);
        List<TablePartitionPlanEntity> entities = Collections.singletonList(tablePartitionPlan);

        when(executionContext.getMergedJobDataMap()).thenReturn(new JobDataMap());
        when(executionContext.getJobDetail()).thenReturn(new JobDetail());

        // Execute the method
        partitionPlanJob.execute(executionContext);

        // Verify the results
        verify(partitionPlanJob, atLeastOnce()).execute(executionContext);
    }

    @Test(expected = UnExpectedException.class)
    public void interruptTest() {
        // Setup
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        // Execute the method
        partitionPlanJob.interrupt();
    }
}