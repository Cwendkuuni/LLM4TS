package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

    private PartitionPlanTaskService partitionPlanTaskService;
    private PartitionPlanService partitionPlanService;
    private ScheduleService scheduleService;
    private JobExecutionContext jobContext;
    private ScheduleEntity mockedScheduleEntity;
    private PartitionPlanJobParameters jobParameters;
    private DatabasePartitionPlanEntity databasePartitionPlan;
    private TablePartitionPlanEntity tablePartitionPlan;

    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setup() {
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);

        jobContext = mock(JobExecutionContext.class);
        mockedScheduleEntity = mock(ScheduleEntity.class);
        jobParameters = new PartitionPlanJobParameters();
        databasePartitionPlan = new DatabasePartitionPlanEntity();
        tablePartitionPlan = new TablePartitionPlanEntity();

        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute() throws SchedulerException {
        when(ScheduleTaskUtils.getScheduleId(jobContext)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(mockedScheduleEntity);
        when(mockedScheduleEntity.getJobParametersJson()).thenReturn("");
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(List.of(tablePartitionPlan));
        
        partitionPlanJob.execute(jobContext);

        // Add assertions here
    }

    @Test
    public void testInterrupt() {
        assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
    }
}