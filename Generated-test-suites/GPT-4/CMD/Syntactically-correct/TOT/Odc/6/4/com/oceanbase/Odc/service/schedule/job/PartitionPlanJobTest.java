package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private JobExecutionContext jobExecutionContext;


    @Test(expected = RuntimeException.class)
    public void test_execute_ScheduleServiceTerminateThrowsSchedulerException_RuntimeExceptionIsThrown() {

        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(new ScheduleEntity());

		//simulate exception when calling terminate
        doThrow(SchedulerException.class).when(scheduleService).terminate(any());

        partitionPlanJob.execute(jobExecutionContext);
    }

    @Test(expected = UnExpectedException.class)
    public void test_interrupt_ThrowsUnExpectedException() {
        partitionPlanJob.interrupt();
    }
}