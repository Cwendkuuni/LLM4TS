
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Test
    public void testExecuteWhenNoSchedulePresent() {
        // Assuming the ScheduleTaskUtils.getScheduleId(context) returns some Long value
        when(scheduleService.nullSafeGetById(anyLong()))
            .thenThrow(new RuntimeException("Schedule not found"));
        try {
            partitionPlanJob.execute(null);
            fail("Expect RuntimeException");
        } catch (RuntimeException rex) {
            // pass
        }
    }

    @Test
    public void testExecuteWithValidScheduleButNoPartitionPlans() {
        // Assuming the ScheduleTaskUtils.getScheduleId(context) returns some Long value
        when(scheduleService.nullSafeGetById(anyLong()))
            .thenReturn(new ScheduleEntity());
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
            .thenReturn(new DatabasePartitionPlanEntity());
        // Testing scenario when no partition plans are present
        partitionPlanJob.execute(null);
        verify(scheduleService, times(1)).terminate(any());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}
