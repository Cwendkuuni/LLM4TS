package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

public class PartitionPlanJobTest {

    private PartitionPlanTaskService taskServiceMock;
    private PartitionPlanService planServiceMock;
    private ScheduleService scheduleServiceMock;
    private JobExecutionContext contextMock;
    private PartitionPlanJob job;

    @Before
    public void setup() {
        // Mock the dependencies
        taskServiceMock = mock(PartitionPlanTaskService.class);
        planServiceMock = mock(PartitionPlanService.class);
        scheduleServiceMock = mock(ScheduleService.class);
        contextMock = mock(JobExecutionContext.class);

        // Inject the mock dependencies
        job = new PartitionPlanJob(taskServiceMock, planServiceMock, scheduleServiceMock);
    }
  
    @Test
    public void testExecute(){
        ScheduleEntity scheduleEntityMock = mock(ScheduleEntity.class);
        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntityMock);
        
        DatabasePartitionPlanEntity databasePartitionPlanEntityMock = mock(DatabasePartitionPlanEntity.class);
        when(planServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlanEntityMock);
        // Assuming the config to be enabled
        when(databasePartitionPlanEntityMock.isConfigEnabled()).thenReturn(true);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        when(scheduleEntityMock.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        
        try {
            job.execute(contextMock);
            verify(taskServiceMock, times(1)).executePartitionPlan(anyLong(), anyList());
        } catch (Exception e) {
            // If exception occurs fail the test
            fail("Unexpected exception");
        }
    }

    @Test(expected=UnExpectedException.class)
    public void testInterrupt(){
        job.interrupt();
    }
}
