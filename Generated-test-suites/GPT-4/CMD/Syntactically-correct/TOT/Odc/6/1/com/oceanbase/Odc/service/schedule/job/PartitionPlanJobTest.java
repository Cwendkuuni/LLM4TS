package com.oceanbase.odc.service.schedule.job;
import com.oceanbase.odc.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskService partitionPlanTaskServiceMock;

    @Mock
    private PartitionPlanService partitionPlanServiceMock;

    @Mock
    private ScheduleService scheduleServiceMock;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    
    @Test(expected = RuntimeException.class)
    public void execute_scheduleServiceThrowsException_expectRuntimeException() {
        JobExecutionContext contextMock = mock(JobExecutionContext.class);
        doThrow(new RuntimeException()).when(scheduleServiceMock).nullSafeGetById(any());

        partitionPlanJob.execute(contextMock);
    }
    
    @Test(expected = UnExpectedException.class)
    public void interrupt_Always_ThrowsUnExpectedException() {
	partitionPlanJob.interrupt();
    }

    // You can add more test scenarios. 
    // Testing with valid scenarios will require setting up a lot of mocked behaviors and hence, is more complex.
}