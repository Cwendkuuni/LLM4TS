package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanService partitionPlanService;
    private PartitionPlanTaskService partitionPlanTaskService;
    private ScheduleService scheduleService;
    private PartitionPlanJob partitionPlanJob;
    private JobExecutionContext context;

    @Before
    public void setUp() {
        partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        scheduleService = mock(ScheduleService.class);
        context = mock(JobExecutionContext.class);
        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);
        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.setSpringContextUtil(springContextUtil);
    }

    @Test
    public void testExecute() throws SchedulerException, Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = createScheduleEntity(scheduleId);
        PartitionPlanJobParameters jobParameters = createJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = createDatabasePartitionPlan();
        List<TablePartitionPlanEntity> tablePartitionPlans = createTablePartitionPlans();

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

        partitionPlanJob.execute(context);

        partitionPlanTaskService.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans);

        verifyExecute();
    }

    @Test
    public void testExecute_databasePartitionPlanIsDisable() throws SchedulerException, Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = createScheduleEntity(scheduleId);
        PartitionPlanJobParameters jobParameters = createJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = createDatabasePartitionPlan();
        List<TablePartitionPlanEntity> tablePartitionPlans = createTablePartitionPlans();

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

        partitionPlanJob.execute(context);

        verifyNotExecute();
    }

    @Test
    public void testExecute_noTableToProcess() throws SchedulerException, Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = createScheduleEntity(scheduleId);
        PartitionPlanJobParameters jobParameters = createJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = createDatabasePartitionPlan();
        List<TablePartitionPlanEntity> tablePartitionPlans = Collections.emptyList();

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        verifyNotExecute();
    }

    @Test
    public void testInterrupt() {
        assertNull(partitionPlanJob.interrupt());
    }

    private ScheduleEntity createScheduleEntity(Long scheduleId) {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        return scheduleEntity;
    }

    private PartitionPlanJobParameters createJobParameters() {
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        return jobParameters;
    }

    private DatabasePartitionPlanEntity createDatabasePartitionPlan() {
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId(1L);
        databasePartitionPlan.setConfigEnabled(true);
        return databasePartitionPlan;
    }

    private List<TablePartitionPlanEntity> createTablePartitionPlans() {
        TablePartitionPlanEntity tablePartitionPlan1 = new TablePartitionPlanEntity();
        tablePartitionPlan1.setId(1L);
        TablePartitionPlanEntity tablePartitionPlan2 = new TablePartitionPlanEntity();
        tablePartitionPlan2.setId(2L);
        return Arrays.asList(tablePartitionPlan1, tablePartitionPlan2);
    }

    private void verifyExecute() {
        // Add your verification code here
    }

    private void verifyNotExecute() {
        // Add your verification code here
        try {
            scheduleService.terminate(scheduleEntity);
        } catch (SchedulerException e) {
            log.warn("Stop partition plan job failed.", e);
            throw new RuntimeException(e);
        }
    }
}