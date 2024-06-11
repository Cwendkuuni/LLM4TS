
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;

public class PartitionPlanJobTest {

    @Mock
    private JobExecutionContext context;
    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;
    @Mock
    private PartitionPlanService partitionPlanService;
    @Mock
    private ScheduleService scheduleService;
    
    @InjectMocks
    PartitionPlanJob partitionPlanJob;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }
  
    @Test
    public void testExecute() {
        try {
            Long scheduleId = Long.valueOf(1);
            when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
            ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
            when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
            doNothing().when(partitionPlanTaskService).executePartitionPlan(anyLong(), anyList());

            partitionPlanJob.execute(context);
        } catch(Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

}
