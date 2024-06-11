import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

public class PartitionPlanJobTest {

    private PartitionPlanTaskService partitionPlanTaskServiceMock;
    private PartitionPlanService partitionPlanServiceMock;
    private ScheduleService scheduleServiceMock;
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        scheduleServiceMock = mock(ScheduleService.class);
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskServiceMock;
        partitionPlanJob.partitionPlanService = partitionPlanServiceMock;
        partitionPlanJob.scheduleService = scheduleServiceMock;
    }

    @Test
    public void testExecuteWithValidScheduleId() throws SchedulerException {
        JobExecutionContext context = mock(JobExecutionContext.class);
        long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        partitionPlanJob.execute(context);

        verify(scheduleServiceMock, times(1)).nullSafeGetById(scheduleId);
        verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void testExecuteWithInvalidScheduleId() throws SchedulerException {
        JobExecutionContext context = mock(JobExecutionContext.class);
        long scheduleId = 2L;
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenThrow(new RuntimeException());

        partitionPlanJob.execute(context);

        verify(scheduleServiceMock, times(1)).nullSafeGetById(scheduleId);
        verify(partitionPlanTaskServiceMock, times(0)).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void testInterrupt() {
        try {
            partitionPlanJob.interrupt();
            fail("Expected UnExpectedException to be thrown");
        } catch (UnExpectedException e) {
            // expected
        }
    }
}