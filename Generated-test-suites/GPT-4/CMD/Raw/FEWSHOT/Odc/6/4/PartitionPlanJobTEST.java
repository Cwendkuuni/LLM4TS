package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {
    @Mock
    PartitionPlanTaskService mockPartitionPlanTaskService;

    @Mock
    PartitionPlanService mockPartitionPlanService;

    @Mock
    ScheduleService mockScheduleService;

    @InjectMocks
    PartitionPlanJob partitionPlanJob;

    @Test
    public void executeTest() {
        Long scheduleId = 1L;
        DatabasePartitionPlanEntity mockDatabasePartitionPlanEntity = mock(DatabasePartitionPlanEntity.class);
        TablePartitionPlanEntity mockTablePartitionPlanEntity = mock(TablePartitionPlanEntity.class);
        ScheduleEntity mockScheduleEntity = mock(ScheduleEntity.class);
        
        when(mockScheduleEntity.getJobParametersJson()).thenReturn("{id : 1}");
        when(mockPartitionPlanService.getDatabasePartitionPlanById(any())).thenReturn(mockDatabasePartitionPlanEntity);
        List<TablePartitionPlanEntity> mockTablePartitionPlans = new ArrayList<>();
        mockTablePartitionPlans.add(mockTablePartitionPlanEntity);
        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(any())).thenReturn(mockTablePartitionPlans);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(mockScheduleService.nullSafeGetById(scheduleId)).thenReturn(mockScheduleEntity);

        partitionPlanJob.execute(context);

        verify(mockScheduleService).nullSafeGetById(scheduleId);
    }

    @Test(expected = RuntimeException.class)
    public void interruptTest() {
        partitionPlanJob.interrupt();
    }
}