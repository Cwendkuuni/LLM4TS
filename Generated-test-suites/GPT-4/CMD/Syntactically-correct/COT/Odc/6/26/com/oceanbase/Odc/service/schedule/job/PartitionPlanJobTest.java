package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

public class PartitionPlanJobTest {
    private PartitionPlanJob partitionPlanJob;
    private ScheduleService mockScheduleService;
    private PartitionPlanTaskService mockPartitionPlanTaskService;
    private PartitionPlanService mockPartitionPlanService;
    private JobExecutionContext mockContext;
    
    @Before
    public void setup() {
        SpringContextUtil springContextUtil = new SpringContextUtil();
        ApplicationContext ctx = mock(ApplicationContext.class);
        springContextUtil.setApplicationContext(ctx);
        
        mockScheduleService = mock(ScheduleService.class);
        mockPartitionPlanTaskService = mock(PartitionPlanTaskService.class);
        mockPartitionPlanService = mock(PartitionPlanService.class);
        mockContext = mock(JobExecutionContext.class);
        
        when(ctx.getBean(PartitionPlanTaskService.class)).thenReturn(mockPartitionPlanTaskService);
        when(ctx.getBean(PartitionPlanService.class)).thenReturn(mockPartitionPlanService);
        when(ctx.getBean(ScheduleService.class)).thenReturn(mockScheduleService);
        
        partitionPlanJob = new PartitionPlanJob();
    }
    
    @Test
    public void testExecute() {
        // setup test data and mock behavior
        when(ScheduleTaskUtils.getScheduleId(mockContext)).thenReturn(1L);
        when(mockScheduleService.nullSafeGetById(anyLong())).thenReturn(mock(ScheduleEntity.class));
        when(mockPartitionPlanService.getDatabasePartitionPlanById(any())).thenReturn(
            new DatabasePartitionPlanEntity());
        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(any()))
            .thenReturn(Arrays.asList(new TablePartitionPlanEntity()));
        
        // call the method under test
        partitionPlanJob.execute(mockContext);

        // verify the behaviors
        verify(mockScheduleService, times(1)).nullSafeGetById(anyLong());
        verify(mockPartitionPlanTaskService, times(1)).executePartitionPlan(any(),any());
    }
    
    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        // call the method under test
        partitionPlanJob.interrupt();
    }
}
