
package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.service.partitionplan.impl.PartitionPlanServiceImpl;
import com.oceanbase.odc.service.partitionplan.impl.PartitionPlanTaskServiceImpl;
import com.oceanbase.odc.service.quartz.impl.ScheduleServiceImpl;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskServiceImpl partitionPlanTaskService;

    @Mock
    private PartitionPlanServiceImpl partitionPlanService;

    @Mock
    private ScheduleServiceImpl scheduleService;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Test(expected = RuntimeException.class)
    public void testExecute_withException() {
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        Mockito.when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1000L);
        Mockito.when(scheduleService.nullSafeGetById(1000L)).thenThrow(new RuntimeException());

        partitionPlanJob.execute(context);
    }

    @Test
    public void testExecute_success() {
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = new ScheduleEntity();

        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1000L);
        when(scheduleService.nullSafeGetById(1000L)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(1000L)).thenReturn(null);

        partitionPlanJob.execute(context);
    }

    @Test
    public void testExecute_terminateJob() {
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("scheduleParameters");

        Mockito.when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1000L);
        Mockito.when(scheduleService.nullSafeGetById(1000L)).thenReturn(scheduleEntity);
        doNothing().when(scheduleService).terminate(scheduleEntity);

        partitionPlanJob.execute(context);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}
