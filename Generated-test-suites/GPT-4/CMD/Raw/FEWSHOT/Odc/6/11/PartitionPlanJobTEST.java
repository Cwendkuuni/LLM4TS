package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

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
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;
    
    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleEntity scheduleEntity;
    
    @Mock
    private DatabasePartitionPlanEntity databasePartitionPlan;

    @Mock
    private TablePartitionPlanEntity tablePartitionPlan;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(tablePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
    }
    
    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

}