import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskServiceMock);
        partitionPlanJob.setPartitionPlanService(partitionPlanServiceMock);
        partitionPlanJob.setScheduleService(scheduleServiceMock);
    }

    @Test
    public void testExecute_SuccessfulExecution() {
        JobExecutionContext context = mock(JobExecutionContext.class);

        // Mocking required data
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        when(scheduleServiceMock.getById(1L)).thenReturn(new ScheduleEntity());
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(new DatabasePartitionPlanEntity());
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(List.of(new TablePartitionPlanEntity()));

        // Testing the execution method
        partitionPlanJob.execute(context);

        // Verifying the method calls
        verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void testInterrupt_ExceptionThrown() {
        assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
    }

}