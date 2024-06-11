package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.PartitionPlanJob;
import com.oceanbase.odc.service.schedule.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.test.util.TestScheduler;
import com.oceanbase.odc.service.schedule.test.util.TestUtils;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

@Slf4j
public class PartitionPlanJobTest {

    private TestScheduler scheduler;
    private ScheduleEntity scheduleEntity;
    private PartitionPlanService partitionPlanService;
    private PartitionPlanTaskService partitionPlanTaskService;
    private ScheduleService scheduleService;
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setup() {
        scheduler = new TestScheduler();
        scheduleEntity = TestUtils.createScheduleEntity();
        partitionPlanService = Mockito.mock(PartitionPlanService.class);
        partitionPlanTaskService = Mockito.mock(PartitionPlanTaskService.class);
        scheduleService = Mockito.mock(ScheduleService.class);
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.scheduleService = scheduleService;
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
    }

    @Test
    public void testExecute_scheduleFound() {
        // given
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(ArgumentMatchers.anyLong()))
                .thenReturn(TestUtils.createDatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(ArgumentMatchers.anyLong()))
                .thenReturn(Arrays.asList(TestUtils.createTablePartitionPlanEntity()));

        // when
        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        // then
        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_scheduleNotFound() {
        // given
        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new Exception());

        // when & then
        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));
        verify(log, times(1)).warn("Schedule not found,scheduleId={}", Arguments.anyLong());
    }

    @Test(expected = UnExpectedException.class)
    public void testExecute_interrupted() {
        // given
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        doThrow(new UnExpectedException())
                .when(partitionPlanTaskService)
                .executePartitionPlan(ArgumentMatchers.anyString(), ArgumentMatchers.anyList());

        // when
        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        // then
        verify(log, times(1)).warn("Create partition-plan database change task failed.");
    }

    @Test(expected = SchedulerException.class)
    public void testTerminate_schedulerException() {
        // given
        doThrow(new SchedulerException("test"))
                .when(scheduleService)
                .terminate(scheduleEntity);

        // when
        partitionPlanJob.terminate();

        // then
        verify(log, times(1)).warn("Stop partition plan job failed.");
    }

    @Test
    public void testGetters() {
        // given

        // when

        // then
        assertNotNull(partitionPlanJob.partitionPlanTaskService);
        assertNotNull(partitionPlanJob.partitionPlanService);
        assertNotNull(partitionPlanJob.scheduleService);
    }
}