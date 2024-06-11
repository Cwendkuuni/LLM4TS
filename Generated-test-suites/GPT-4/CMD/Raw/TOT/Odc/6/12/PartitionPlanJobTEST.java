import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Mock
    private PartitionPlanTaskService mockPartitionPlanTaskService;

    @Mock
    private PartitionPlanService mockPartitionPlanService;

    @Mock
    private ScheduleService mockScheduleService;

    @Mock
    private JobExecutionContext mockJobExecutionContext;

    @Before
    public void setUp() throws Exception{
        doReturn(1L).when(mockScheduleService).nullSafeGetById(anyLong());

        doReturn("aJson").when(mockScheduleEntity).getJobParametersJson();
        doReturn(mockDatabasePartitionPlan).when(mockPartitionPlanService).getDatabasePartitionPlanById(any());
        doReturn(mockTable).when(mockPartitionPlanService).getValidTablePlanByDatabasePartitionPlanId(any());

        doNothing().when(mockScheduleService).terminate(any());
        doNothing().when(mockPartitionPlanTaskService).executePartitionPlan(any(), any());
    }

    @Test(expected= RuntimeException.class)
    public void execute_scheduleServiceThrowsException_exceptionCaught() {
        doThrow(RuntimeException.class).when(mockScheduleService).nullSafeGetById(any());

        partitionPlanJob.execute(mockJobExecutionContext);
    }

    @Test(expected= RuntimeException.class)
    public void execute_scheduleServiceTerminateThrowsException_exceptionCaught() {
        doThrow(RuntimeException.class).when(mockScheduleService).terminate(any());

        partitionPlanJob.execute(mockJobExecutionContext);
    }

    @Test
    public void execute_noErrors_methodFinishCorrectly() {
        partitionPlanJob.execute(mockJobExecutionContext);

        verify(mockScheduleService, times(1)).nullSafeGetById(any());
        verify(mockScheduleService, times(1)).terminate(any());
        verify(mockPartitionPlanService, times(1)).getDatabasePartitionPlanById(any());
        verify(mockPartitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(any());
        verify(mockPartitionPlanTaskService, times(1)).executePartitionPlan(any(), any());
    }

    @Test(expected= UnExpectedException.class)
    public void interrupt_always_throwsUnExpectedException() {
        partitionPlanJob.interrupt();
    }
}