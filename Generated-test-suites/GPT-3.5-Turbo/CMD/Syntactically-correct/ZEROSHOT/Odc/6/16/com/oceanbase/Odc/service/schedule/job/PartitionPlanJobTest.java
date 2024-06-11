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

public class PartitionPlanJobTest {

    private PartitionPlanTaskService partitionPlanTaskService;
    private PartitionPlanService partitionPlanService;
    private ScheduleService scheduleService;
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);
        SpringContextUtil.setMockBeans(partitionPlanTaskService, partitionPlanService, scheduleService);

        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute() throws SchedulerException {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        tablePartitionPlans.add(tablePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        verify(scheduleService).terminate(scheduleEntity);
        verify(partitionPlanTaskService).executePartitionPlan(anyLong(), eq(tablePartitionPlans));
    }

    @Test
    public void testInterrupt() {
        assertThrows(UnExpectedException.class, () -> {
            partitionPlanJob.interrupt();
        });
    }
}