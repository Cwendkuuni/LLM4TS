package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;
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
    public void testExecute() throws Exception {
        PartitionPlanJob job = new PartitionPlanJob();

        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{\n" +
                "  \"databasePartitionPlanId\": 1\n" +
                "}");
        scheduleEntity.setId(1L);

        ScheduleService scheduleService = Mockito.mock(ScheduleService.class);
        Mockito.when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);

        PartitionPlanService partitionPlanService = Mockito.mock(PartitionPlanService.class);
        Mockito.when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(new DatabasePartitionPlanEntity());

        PartitionPlanTaskService partitionPlanTaskService = Mockito.mock(PartitionPlanTaskService.class);

        SpringContextUtil springContextUtil = Mockito.mock(SpringContextUtil.class);
        Mockito.when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
        Mockito.when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        Mockito.when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);

        job.execute(context);

        // Add assertions here
    }

    @Test
    public void testInterrupt() {
        PartitionPlanJob job = new PartitionPlanJob();

        try {
            job.interrupt();
        } catch (UnExpectedException e) {
            assertNotNull(e);
        }
    }
}