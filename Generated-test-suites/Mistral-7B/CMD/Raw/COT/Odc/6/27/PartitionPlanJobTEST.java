package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import com.google.common.collect.Lists;

@Slf4j
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanService partitionPlanService;
    private PartitionPlanTaskService partitionPlanTaskService;
    private ScheduleService scheduleService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        partitionPlanService = SpringContextUtil.getBean(PartitionPlanService.class);
        partitionPlanTaskService = SpringContextUtil.getBean(PartitionPlanTaskService.class);
        scheduleService = SpringContextUtil.getBean(ScheduleService.class);
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskService);
        partitionPlanJob.setPartitionPlanService(partitionPlanService);
        partitionPlanJob.setScheduleService(scheduleService);
    }

    @Test
    public void testExecute_scheduleEntityIsNull_shouldNotExecuteJob() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = null;
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = Lists.newArrayList();

        try {
            partitionPlanJob.execute(new JobExecutionContext());
            fail("execute should not be called when scheduleEntity is null.");
        } catch (Exception e) {
            //expected
        }

        assertEquals("Schedule not found,scheduleId=" + scheduleId, log.getLastLog().getMessage());
    }

    @Test
    public void testExecute_databasePartitionPlanIsNull_shouldLogAndReturn() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = null;
        List<TablePartitionPlanEntity> tablePartitionPlans = Lists.newArrayList();

        try {
            partitionPlanJob.execute(new JobExecutionContext());
        } catch (Exception e) {
            //expected
        }

        assertEquals("database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId="
                + scheduleId, log.getLastLog().getMessage());
    }

    @Test
    public void testExecute_tablePartitionPlansIsNull_shouldLogAndReturn() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = null;

        try {
            partitionPlanJob.execute(new JobExecutionContext());
        } catch (Exception e) {
            //expected
        }

        assertEquals("database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId="
                + scheduleId, log.getLastLog().getMessage());
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_partitionPlanTaskServiceException_shouldStopJob() throws SchedulerException {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = Lists.newArrayList();

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        partitionPlanJob.execute(new JobExecutionContext());

        String expected = "database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId="
                + scheduleId;
        verify(log, times(2)).info(expected);

        ScheduleEntity scheduleEntityToTerminate = new ScheduleEntity();
        scheduleEntityToTerminate.setId(scheduleId);
        partitionPlanJob.scheduleService.terminate(scheduleEntityToTerminate);

        fail("Expected exception was not thrown.");
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
        fail("Unexpected exception should be thrown.");
    }
}