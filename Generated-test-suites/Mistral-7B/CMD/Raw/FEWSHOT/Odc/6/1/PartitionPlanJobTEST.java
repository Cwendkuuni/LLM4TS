package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

@Slf4j
public class PartitionPlanJobTest {

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private SpringContextUtil springContextUtil;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Test
    public void execute_scheduleEntityNull_doNotProcess() {
        when(scheduleService.nullSafeGetById((Long) anyObject()))
                .thenThrow(new RuntimeException("Schedule not found."));

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().getString("scheduleId")).thenReturn("scheduleId");

        partitionPlanJob.execute(context);

        verify(log, times(1)).warn("Schedule not found,scheduleId={}", "scheduleId");
        verify(scheduleService, never()).terminate(any(ScheduleEntity.class));
    }

    @Test
    public void execute_databasePartitionPlanNull_doNotProcess() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);

        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(jsonUtils.fromJson(anyString(), eq(PartitionPlanJobParameters.class)))
                .thenReturn(new PartitionPlanJobParameters());

        DatabasePartitionPlanEntity databasePartitionPlan = null;
        partitionPlanJob.execute(mock(JobExecutionContext.class));

        verify(log, times(1)).info(
                "database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId=1");
        verify(scheduleService, times(1)).terminate(scheduleEntity);
        verify(partitionPlanService, never()).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService, never()).getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void execute_databasePartitionPlanEnableAndTablePlansEmpty_doNotProcess() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");

        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(jsonUtils.fromJson(anyString(), eq(PartitionPlanJobParameters.class)))
                .thenReturn(new PartitionPlanJobParameters());

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(Collections.emptyList());

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        verify(log, times(1)).info(
                "database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId=1");
        verify(scheduleService, times(1)).terminate(scheduleEntity);
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(1L);
        verify(partitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(1L);
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void execute_databasePartitionPlanEnableAndTablePlansNotEmpty_process() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");

        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(jsonUtils.fromJson(anyString(), eq(PartitionPlanJobParameters.class)))
                .thenReturn(new PartitionPlanJobParameters());

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(),
                new TablePartitionPlanEntity());

        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        when(partitionPlanTaskService.executePartitionPlan(anyLong(), anyList()))
                .thenReturn(new VoidPartitionPlanResponse());

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        verify(log, never()).warn("Schedule not found,scheduleId=1");
        verify(log, never()).warn("Create partition-plan database change task failed.");
        verify(log, times(1)).info(
                "database partition plan is enable and there are tables to process,start to process,scheduleId=1");
        verify(scheduleService, times(1)).terminate(scheduleEntity);
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(1L);
        verify(partitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(1L);
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(1L, tablePartitionPlans);
    }

    @Test
    public void interrupt_noException() {
        partitionPlanJob.interrupt();

        verify(log, never()).warn("Stop partition plan job failed.");
    }
}