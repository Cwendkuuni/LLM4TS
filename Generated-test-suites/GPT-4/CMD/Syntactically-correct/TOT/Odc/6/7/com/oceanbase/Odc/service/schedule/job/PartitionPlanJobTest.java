package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {
    
    @Mock
    private PartitionPlanService partitionPlanService;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;
    @Mock
    private JobExecutionContext jobExecutionContext;

    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanJob = new PartitionPlanJob(partitionPlanService, scheduleService, partitionPlanTaskService);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void execute() {
        when(ScheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        DatabasePartitionPlanEntity databasePartitionPlanEntity = mock(DatabasePartitionPlanEntity.class);
        when(partitionPlanService.getDatabasePartitionPlanById(any())).thenReturn(databasePartitionPlanEntity);
        List<TablePartitionPlanEntity> tablePartitionPlanEntities = Collections.singletonList(mock(TablePartitionPlanEntity.class));
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(any())).thenReturn(tablePartitionPlanEntities);
        partitionPlanJob.execute(jobExecutionContext);
        verify(partitionPlanTaskService,times(1)).executePartitionPlan(any(),any());
        
    }
    
    @Test(expected = UnExpectedException.class)
    public void interrupt() {
        partitionPlanJob.interrupt();
    }

}
