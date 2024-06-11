package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class PartitionPlanJobTest {

    private final PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
    private final PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
    private final ScheduleService scheduleService = mock(ScheduleService.class);
    private final JobExecutionContext context = mock(JobExecutionContext.class);

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        PartitionPlanJob job = new PartitionPlanJob();
        job.interrupt();
    }

    @Test
    public void execute() {
        PartitionPlanJob job = Mockito.spy(new PartitionPlanJob());
        
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity(scheduleId, "jobParametersJson");
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(2L);

        TablePartitionPlanEntity tablePartitionPlanEntity = new TablePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlanEntityList = new ArrayList<>();
        tablePartitionPlanEntityList.add(tablePartitionPlanEntity);

        DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
        databasePartitionPlanEntity.setConfigEnabled(false);

        when(ScheduleService.nullSafeGetById(any(Long.class))).thenReturn(scheduleEntity);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class)).thenReturn(jobParameters);
        when(partitionPlanService.getDatabasePartitionPlanById(any(Long.class))).thenReturn(databasePartitionPlanEntity);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(any(Long.class))).thenReturn(tablePartitionPlanEntityList);
        

        job.execute(context);

        verify(scheduleService, times(1)).terminate(scheduleEntity);

        databasePartitionPlanEntity.setConfigEnabled(true);
        tablePartitionPlanEntityList.clear();

        job.execute(context);
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(databasePartitionPlanEntity.getFlowInstanceId(), tablePartitionPlanEntityList);
    }
}
