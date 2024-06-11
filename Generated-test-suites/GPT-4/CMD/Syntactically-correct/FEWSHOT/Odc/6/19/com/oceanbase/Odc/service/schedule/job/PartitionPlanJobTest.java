package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() throws SchedulerException {
        MockitoAnnotations.initMocks(this);
        partitionPlanJob = new PartitionPlanJob();

        when(context.getJobInstance()).thenReturn(partitionPlanJob);
        when(partitionPlanService.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(partitionPlanService.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(partitionPlanService.getBean(ScheduleService.class)).thenReturn(scheduleService);
    }

    @Test
    public void testExecute() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":1}");

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        databasePartitionPlan.setFlowInstanceId(1L);

        when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(databasePartitionPlan);

        TablePartitionPlanEntity tablePartitionPlanEntity = new TablePartitionPlanEntity();
        tablePartitionPlanEntity.setValid(true);

        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(databasePartitionPlan.getId()))
                .thenReturn(Arrays.asList(tablePartitionPlanEntity));

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

}