package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

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

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private PartitionPlanTaskService partitionPlanTaskService;
    private PartitionPlanService partitionPlanService;
    private ScheduleService scheduleService;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);
        jobExecutionContext = mock(JobExecutionContext.class);

        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
    }

    @Test
    public void testExecute() throws SchedulerException {
        // Mock data
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");

        DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlanEntity);

        List<TablePartitionPlanEntity> tablePartitionPlanEntities = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlanEntities);

        // Test execute method
        partitionPlanJob.execute(jobExecutionContext);

        // Assertions
        verify(scheduleService, times(1)).nullSafeGetById(scheduleId);
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(scheduleService, times(1)).terminate(scheduleEntity);
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void testInterrupt() {
        // Test interrupt method
        try {
            partitionPlanJob.interrupt();
        } catch (UnExpectedException e) {
            assertNotNull(e);
        }
    }
}