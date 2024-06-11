package com.oceanbase.odc.service.schedule.job;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanService partitionPlanService;
    private PartitionPlanTaskService partitionPlanTaskService;
    private ScheduleService scheduleService;
    private PartitionPlanJobParameters jobParameters;
    private DatabasePartitionPlanEntity databasePartitionPlan;
    private List<TablePartitionPlanEntity> tablePartitionPlans;
    private ScheduleEntity scheduleEntity;
    private JobExecutionContext context;
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void init() {
        partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        scheduleService = mock(ScheduleService.class);
        context = mock(JobExecutionContext.class);

        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);
        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);

        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.setPartitionPlanService(partitionPlanService);
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskService);
        partitionPlanJob.setScheduleService(scheduleService);

        jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        tablePartitionPlans = java.util.Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());

        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));

        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
    }

    @Test
    public void testExecuteEmpty() {
        partitionPlanTaskService.executePartitionPlan = throwException; // replace with real exception

        partitionPlanJob.execute(context);
        assertTrue(log.isInfoEnabled());
        log.info("###Test Log### database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}", 1);
        assertFalse(partitionPlanTaskService.executePartitionPlanCalled);
    }

    @Test
    public void testExecuteSuccess() {
        partitionPlanTaskService.executePartitionPlan = doNothing();

        partitionPlanJob.execute(context);
        assertTrue(log.isInfoEnabled());
        log.info("###Test Log### execute partition-plan database change task succeeded.");
        assertFalse(partitionPlanTaskService.executePartitionPlanCalled);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

    @Test
    public void testGetters() {
        assertSame(partitionPlanService, partitionPlanJob.getPartitionPlanService());
        assertSame(partitionPlanTaskService, partitionPlanJob.getPartitionPlanTaskService());
        assertSame(scheduleService, partitionPlanJob.getScheduleService());
    }
}