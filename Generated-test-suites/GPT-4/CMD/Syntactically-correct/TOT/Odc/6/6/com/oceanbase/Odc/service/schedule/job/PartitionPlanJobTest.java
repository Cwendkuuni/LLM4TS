package com.oceanbase.odc.service.schedule.job;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import org.quartz.JobExecutionContext;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        partitionPlanJob = new PartitionPlanJob();
    }

    @Test(expected = RuntimeException.class)
    public void testExecute() {
        when(scheduleService.nullSafeGetById(any(Long.class))).thenThrow(Exception.class);
        partitionPlanJob.execute(jobExecutionContext);
    }

    @Test
    public void testInterrupt() {
        assertThatThrownBy(() -> 
            partitionPlanJob.interrupt()
        ).isInstanceOf(UnExpectedException.class);
    }
}