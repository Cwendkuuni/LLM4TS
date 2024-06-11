package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import org.quartz.JobExecutionContext;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock 
    private PartitionPlanTaskService partitionPlanTaskService;
    
    @Mock 
    private PartitionPlanService partitionPlanService;
    
    @Mock 
    private ScheduleService scheduleService;
    
    @Mock
    private JobExecutionContext context;
    
    @InjectMocks
    private PartitionPlanJob job;

    @Test
    public void testExecute_WithValidDBPartitionPlanAndTablePlan() {
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);       
        when(scheduleService.nullSafeGetById(1L)).thenReturn(mock(ScheduleEntity.class));
        
        doNothing().when(partitionPlanTaskService).executePartitionPlan(anyString(), anyList());
        
        job.execute(context);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_WithInvalidDBPartitionPlanAndTablePlan_ThrowSchedulerException() {
        when(scheduleService.nullSafeGetById(1L)).thenReturn(mock(ScheduleEntity.class));        
        when(scheduleService.terminate(any(ScheduleEntity.class))).thenReturn(mock(ScheduleEntity.class));
        
        job.execute(context);
    }
    
    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        job.interrupt();
    }
}
