
package com.oceanbase.odc.service.schedule.job;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;
    
    @Mock
    private PartitionPlanService partitionPlanService;
    
    @Mock
    private ScheduleService scheduleService;
    
    @Mock
    private JobExecutionContext context;

    private PartitionPlanJob underTest;

    @Before
    public void setUp() {
        underTest = new PartitionPlanJob();
    }

    @Test(expected=UnExpectedException.class)
    public void testInterrupt() {
       underTest.interrupt();
    }

    @Test
    public void testExecuteWithScheduleNotFound() {
        Mockito.when(context.getJobDetail()).thenReturn(null);
        underTest.execute(context);
        Mockito.verify(partitionPlanTaskService, Mockito.times(0)).executePartitionPlan(Mockito.anyString(), Mockito.anyList());
    }
    
    @Test
    public void testExecuteWithDisabledDatabasePartitionPlanOrEmptyTablePartitionPlans() {
        mockScheduleAndEntities(false);
        underTest.execute(context);
        Mockito.verify(partitionPlanTaskService, Mockito.times(0)).executePartitionPlan(Mockito.anyString(), Mockito.anyList());
    }
    
    @Test
    public void testExecuteWithEnabledDatabasePartitionPlanAndNonEmptyTablePartitionPlans() {
        mockScheduleAndEntities(true);
        underTest.execute(context);
        Mockito.verify(partitionPlanTaskService, Mockito.times(1)).executePartitionPlan(Mockito.anyString(), Mockito.anyList());
    }
    
    @Test(expected=RuntimeException.class)
    public void testExecuteWithSchedulerExceptionSetting() {
        mockScheduleAndEntities(true);
        Mockito.doThrow(RuntimeException.class).when(scheduleService).terminate(Mockito.any(ScheduleEntity.class));
        underTest.execute(context);
    }
    
    private void mockScheduleAndEntities(boolean isConfigEnabled)
    {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("");
        
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(isConfigEnabled);
        
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());
        
        Mockito.when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        Mockito.when(partitionPlanService.getDatabasePartitionPlanById(Mockito.anyLong())).thenReturn(databasePartitionPlan);
        Mockito.when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(Mockito.anyLong())).thenReturn(tablePartitionPlans);
    }
}
