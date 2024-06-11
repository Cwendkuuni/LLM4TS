package com.oceanbase.odc.service.schedule.job;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.util.ArrayList;
import java.util.List;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Mock
    private JobExecutionContext mockContext;

    @Mock
    private ScheduleEntity mockEntity;

    @Mock
    private PartitionPlanTaskService mockPartitionPlanTaskService;

    @Mock
    private PartitionPlanService mockPartitionPlanService;

    @Mock
    private ScheduleService mockScheduleService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void execute_test() throws SchedulerException {

        // Stubbing/JDK8 Lambda
        Long scheduleId = 1L;
        when(mockContext.getJobDetail().getJobDataMap().getLong("scheduleId")).thenReturn(scheduleId);
        when(mockScheduleService.nullSafeGetById(scheduleId)).thenReturn(mockEntity);

        // Test method
        partitionPlanJob.execute(mockContext);

        // Verify methods were called
        verify(mockContext).getJobDetail();
        verify(mockScheduleService).nullSafeGetById(scheduleId);
    }

    @Test(expected = UnExpectedException.class)
    public void interrupt_test() {
        partitionPlanJob.interrupt();
    }
}