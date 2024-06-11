package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.util.ArrayList;

public class PartitionPlanJobTest {

    private PartitionPlanTaskService partitionPlanTaskService;
    private PartitionPlanService partitionPlanService;
    private ScheduleService scheduleService;
    private PartitionPlanJob partitionPlanJob;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        partitionPlanTaskService = Mockito.mock(PartitionPlanTaskService.class);
        partitionPlanService = Mockito.mock(PartitionPlanService.class);
        scheduleService = Mockito.mock(ScheduleService.class);
        jobExecutionContext = Mockito.mock(JobExecutionContext.class);

        partitionPlanJob = new PartitionPlanJob();

        Mockito.doReturn(partitionPlanTaskService).when(partitionPlanJob, "partitionPlanTaskService");
        Mockito.doReturn(partitionPlanService).when(partitionPlanJob, "partitionPlanService");
        Mockito.doReturn(scheduleService).when(partitionPlanJob, "scheduleService");
    }

    @Test
    public void testExecute() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");

        Mockito.doReturn(scheduleEntity).when(scheduleService).nullSafeGetById(Mockito.any());

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        Mockito.doReturn(jobParameters).when(partitionPlanService).getDatabasePartitionPlanById(Mockito.any());

        DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
        databasePartitionPlanEntity.setConfigEnabled(true);
        Mockito.doReturn(databasePartitionPlanEntity).when(partitionPlanService).getDatabasePartitionPlanById(Mockito.any());

        Mockito.doReturn(new ArrayList<TablePartitionPlanEntity>()).when(partitionPlanService).getValidTablePlanByDatabasePartitionPlanId(Mockito.any());

        partitionPlanJob.execute(jobExecutionContext);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteException() throws SchedulerException {
        Mockito.doThrow(SchedulerException.class).when(scheduleService).terminate(Mockito.any());
        partitionPlanJob.execute(jobExecutionContext);
    }

    @Test
    public void testExecuteExceptionPath() {
        Mockito.doThrow(RuntimeException.class).when(partitionPlanService).getDatabasePartitionPlanById(Mockito.any());
        try {
            partitionPlanJob.execute(jobExecutionContext);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }
}
