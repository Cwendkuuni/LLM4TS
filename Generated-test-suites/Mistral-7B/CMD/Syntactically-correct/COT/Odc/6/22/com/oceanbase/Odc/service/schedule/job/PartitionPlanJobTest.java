package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

@Slf4j
public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private SpringContextUtil springContextUtil;

    @Mock
    private ScheduleTaskUtils scheduleTaskUtils;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private JobExecutionContext context;
    private ScheduleEntity scheduleEntity;
    private PartitionPlanJobParameters jobParameters;
    private DatabasePartitionPlanEntity databasePartitionPlan;
    private List<TablePartitionPlanEntity> tablePartitionPlans;

    @Before
    public void setup() {
        context = mock(JobExecutionContext.class);
        scheduleEntity = mock(ScheduleEntity.class);
        jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(UUID.randomUUID().toString());
        databasePartitionPlan = new DatabasePartitionPlanEntity();
        tablePartitionPlans = new ArrayList<>();
    }

    @Test
    public void testExecute_ScheduleNotFound() {
        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new Exception());
        partitionPlanJob.execute(context);
        verify(log, times(1)).warn("Schedule not found,scheduleId={}", anyLong());
    }

    @Test
    public void testExecute_DatabasePartitionPlanDisabledOrNoTableToProcess() {
        DatabasePartitionPlanEntity disabledDatabasePartitionPlan = new DatabasePartitionPlanEntity();
        disabledDatabasePartitionPlan.setConfigEnabled(false);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(anyString())).thenReturn(disabledDatabasePartitionPlan);
        partitionPlanJob.execute(context);
        verify(log, times(1)).info(
                "database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                anyLong());
        try {
            verify(scheduleService, times(1)).terminate(scheduleEntity);
        } catch (SchedulerException e) {
            fail("Should not throw SchedulerException in this test case.");
        }
    }

    @Test
    public void testExecute_PartitionPlanTaskExecutionSuccess() {
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(anyString())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyString()))
                .thenReturn(tablePartitionPlans);
        when(partitionPlanTaskService.executePartitionPlan(anyString(), anyList()))
                .thenReturn(new Object());
        partitionPlanJob.execute(context);
        verify(log, times(0)).warn("Create partition-plan database change task failed.");
    }

    @Test
    public void testExecute_PartitionPlanTaskExecutionFailed() {
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(anyString())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyString()))
                .thenReturn(tablePartitionPlans);
        when(partitionPlanTaskService.executePartitionPlan(anyString(), anyList()))
                .thenThrow(new Exception());
        partitionPlanJob.execute(context);
        verify(log, times(1)).warn("Create partition-plan database change task failed.");
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}