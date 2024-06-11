package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleEntity;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.PartitionPlanJob;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanService partitionPlanService;
    private PartitionPlanTaskService partitionPlanTaskService;
    private ScheduleService scheduleService;
    private PartitionPlanJob partitionPlanJob;
    private DatabasePartitionPlanEntity databasePartitionPlan;
    private List<TablePartitionPlanEntity> tablePartitionPlans;

    @Before
    public void setup() {
        partitionPlanService = Mockito.mock(PartitionPlanService.class);
        partitionPlanTaskService = Mockito.mock(PartitionPlanTaskService.class);
        scheduleService = Mockito.mock(ScheduleService.class);
        partitionPlanJob = new PartitionPlanJob();
        databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
        tablePartitionPlans = new ArrayList<>();
        SpringContextUtil springContextUtil = Mockito.mock(SpringContextUtil.class);
        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
        partitionPlanJob.springContextUtil = springContextUtil;
    }

    @Test
    public void testExecute_DatabasePartitionPlanIsDisableOrNoTableNeedToProcess_StopPartitionPlanJob() {
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlanToBe = Mockito.mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlanToBe.isConfigEnabled()).thenReturn(false);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlanToBe);

        PartitionPlanJobParameters jobParameters1 = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlanToBe1 = Mockito.mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlanToBe1.isConfigEnabled()).thenReturn(true);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlanToBe1);

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(new ScheduleEntity());

        partitionPlanJob.execute(null);
        verify(scheduleService, times(1)).terminate(any(ScheduleEntity.class));
    }

    @Test
    public void testExecute_DatabasePartitionPlanIsEnableAndTableNeedToProcess_ExecutePartitionPlan() {
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlanToBe = Mockito.mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlanToBe.isConfigEnabled()).thenReturn(true);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlanToBe);

        TablePartitionPlanEntity tablePartitionPlanToBe = Mockito.mock(TablePartitionPlanEntity.class);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
        when(tablePartitionPlans.get(0)).thenReturn(tablePartitionPlanToBe);

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(new ScheduleEntity());

        partitionPlanJob.execute(null);
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
    }
}