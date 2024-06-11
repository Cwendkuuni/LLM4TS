package com.oceanbase.odc.service.schedule.job;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

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
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: tinker
 * @Date: 2023/8/21 10:37
 * @Description:
 */

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanService partitionPlanService;
    private PartitionPlanTaskService partitionPlanTaskService;
    private ScheduleService scheduleService;
    private PartitionPlanJobParameters jobParameters;
    private DatabasePartitionPlanEntity databasePartitionPlan;
    private List<TablePartitionPlanEntity> tablePartitionPlans;
    private ScheduleEntity scheduleEntity;
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setup() {
        partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        scheduleService = mock(ScheduleService.class);

        jobParameters = new PartitionPlanJobParameters();
        databasePartitionPlan = new DatabasePartitionPlanEntity();
        tablePartitionPlans = mock(List.class);
        scheduleEntity = new ScheduleEntity();

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        partitionPlanJob.scheduleService = scheduleService;
    }

    @Test
    public void testExecute() {
        Long scheduleId = 1L;
        when(scheduleEntity.getId()).thenReturn(scheduleId);

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        verify(scheduleService).terminate(scheduleEntity);
        verify(partitionPlanService, never()).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService, never()).getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void testExecute_databasePartitionPlanIsEnabled_tablePartitionPlansNotEmpty() {
        databasePartitionPlan.setConfigEnabled(true);

        when(partitionPlanTaskService.executePartitionPlan(anyLong(), anyList())).thenAnswer(invocation -> {
        });

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        verify(scheduleService, never()).terminate(scheduleEntity);
        verify(partitionPlanService).getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId());
        verify(partitionPlanService).getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId());
        verify(partitionPlanTaskService).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}