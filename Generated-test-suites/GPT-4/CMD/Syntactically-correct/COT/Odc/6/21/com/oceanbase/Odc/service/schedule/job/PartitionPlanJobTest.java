package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.service.schedule.job.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import org.quartz.JobExecutionContext;

public class PartitionPlanJobTest {

    @InjectMocks
    PartitionPlanJob partitionPlanJob;

    @Mock
    PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    PartitionPlanService partitionPlanService;

    @Mock
    ScheduleService scheduleService;

    @Mock
    JobExecutionContext context;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        final Long scheduleId = anyLong();
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);

        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters parameters = mock(PartitionPlanJobParameters.class);
        DatabasePartitionPlanEntity dbPartitionPlanEntity = mock(DatabasePartitionPlanEntity.class);

        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class)).thenReturn(parameters);
        when(partitionPlanService.getDatabasePartitionPlanById(parameters.getDatabasePartitionPlanId())).thenReturn(dbPartitionPlanEntity);

        partitionPlanJob.execute(context);

        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(parameters.getDatabasePartitionPlanId());
        verify(scheduleService, times(1)).nullSafeGetById(scheduleId);
    }

    @Test
    public void testInterrupt() {
        assertThrows(UnExpectedException.class, () -> {
            partitionPlanJob.interrupt();
        });
    }

    @Test
    public void testExecuteWithoutSchedule() {
        final Long scheduleId = anyLong();
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(null);

        partitionPlanJob.execute(context);

        verify(partitionPlanService, times(0)).getDatabasePartitionPlanById(anyLong());
    }
    @Test
    public void testExecuteWithoutDatabasePartitionPlan() {
        final Long scheduleId = anyLong();
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);

        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters parameters = mock(PartitionPlanJobParameters.class);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class)).thenReturn(parameters);
        when(partitionPlanService.getDatabasePartitionPlanById(parameters.getDatabasePartitionPlanId())).thenReturn(null);

        partitionPlanJob.execute(context);

        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(parameters.getDatabasePartitionPlanId());
    }
}
