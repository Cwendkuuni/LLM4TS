
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.schedule.ScheduleService;


public class PartitionPlanJobTest {
    
    PartitionPlanJob partitionPlanJob;
    PartitionPlanTaskService mockPartitionPlanTaskService;
    PartitionPlanService mockPartitionPlanService;
    ScheduleService mockScheduleService;
    JobExecutionContext mockContext;
    
    @Before
    public void setUp() throws Exception {
        mockPartitionPlanTaskService = mock(PartitionPlanTaskService.class);
        when(mockPartitionPlanTaskService.getBean(PartitionPlanTaskService.class)).thenReturn(mockPartitionPlanTaskService);
        
        mockPartitionPlanService = mock(PartitionPlanService.class);
        when(SpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(mockPartitionPlanService);
        
        mockScheduleService = mock(ScheduleService.class);
        when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(mockScheduleService);
        
        mockContext = mock(JobExecutionContext.class);
    }
    
    @After
    public void tearDown() throws Exception {
        partitionPlanJob = null;
    }
    
    @Test
    public void testExecute() {
        try {
            partitionPlanJob.execute(mockContext);
            verify(mockScheduleService).nullSafeGetById(any(Long.class));
            verify(mockPartitionPlanTaskService).executePartitionPlan(any(String.class), any(List.class));
        } catch (Exception e) {
            fail("Exception was thrown.");
        }
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteException() {
        doThrow(new RuntimeException()).when(mockScheduleService).nullSafeGetById(any(Long.class));
        partitionPlanJob.execute(mockContext);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}
