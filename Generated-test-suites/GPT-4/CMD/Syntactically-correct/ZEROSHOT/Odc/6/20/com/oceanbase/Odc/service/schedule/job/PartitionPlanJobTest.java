package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.quartz.JobExecutionContext;

import  com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import  com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import  com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import  com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import  com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import  com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import  com.oceanbase.odc.service.schedule.ScheduleService;
import  com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import  com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import java.util.Collections;
import static org.mockito.Mockito.*;

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
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        ScheduleEntity scheduleEntityMock = mock(ScheduleEntity.class);
        databasePartitionPlanEntityMock = mock(DatabasePartitionPlanEntity.class);
        tablePartitionPlanEntityMock = mock(TablePartitionPlanEntity.class);

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntityMock);
        when(scheduleEntityMock.getJobParametersJson()).thenReturn("json");
        when(JsonUtils.fromJson("json", PartitionPlanJobParameters.class)).thenReturn(new PartitionPlanJobParameters());

        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlanEntityMock);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(Collections.singletonList(tablePartitionPlanEntityMock));

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() throws UnExpectedException {
        partitionPlanJob.interrupt();
    }
}
