package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.util.Collections;
import java.util.List;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void execute_happyPath_success() throws SchedulerException {
        //mock the necessary entities
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        DatabasePartitionPlanEntity planEntity = Mockito.mock(DatabasePartitionPlanEntity.class);
        List<TablePartitionPlanEntity> entities = Collections.singletonList(Mockito.mock(TablePartitionPlanEntity.class));
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(Mockito.any())).thenReturn(planEntity);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(Mockito.any())).thenReturn(entities);
        when(planEntity.isConfigEnabled()).thenReturn(true);
        partitionPlanJob.execute(context);
        verify(scheduleService, times(1)).nullSafeGetById(Mockito.anyLong());
        //verify other calls and add more test scenarios
    }

    @Test(expected = UnExpectedException.class)
    public void interrupt_throwsException_error(){
        partitionPlanJob.interrupt();
    }
    
    // Add more test methods based on edge cases and exception handling scenarios
}