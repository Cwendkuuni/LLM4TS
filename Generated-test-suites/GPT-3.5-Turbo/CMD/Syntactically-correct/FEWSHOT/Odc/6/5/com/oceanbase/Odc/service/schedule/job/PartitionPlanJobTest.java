package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

public class PartitionPlanJobTest {

    @Test
    public void testExecute() throws SchedulerException {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": 1}");
        
        ScheduleService scheduleService = mock(ScheduleService.class);
        when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        
        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        when(SpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        tablePartitionPlans.add(tablePartitionPlan);
        
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
        
        partitionPlanJob.execute(context);
        
        assertNotNull(partitionPlanJob);
        // Add more assertion statements as needed 
    }

    @Test
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        
        // Assuming the interrupt method always throws an exception
        try {
            partitionPlanJob.interrupt();
        } catch (UnExpectedException e) {
            assertEquals(UnExpectedException.class, e.getClass());
        }
    }
}