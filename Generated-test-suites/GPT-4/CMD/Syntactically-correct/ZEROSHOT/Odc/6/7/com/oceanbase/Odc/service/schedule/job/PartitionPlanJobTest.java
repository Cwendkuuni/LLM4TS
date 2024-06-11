package com.oceanbase.odc.service.schedule.job;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.Collections;
import org.quartz.JobExecutionException;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;

public class PartitionPlanJobTest {

    @Mock
    PartitionPlanTaskService mockPartitionPlanTaskService;
    
    @Mock
    PartitionPlanService mockPartitionPlanService;
    
    @Mock
    ScheduleService mockScheduleService;
    
    @InjectMocks
    PartitionPlanJob partitionPlanJob;
    
    @Mock
    JobExecutionContext mockContext;
    
    @Mock
    ScheduleEntity mockScheduleEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() throws SchedulerException, JobExecutionException {
        DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
        databasePartitionPlanEntity.setConfigEnabled(true);
        databasePartitionPlanEntity.setFlowInstanceId("SampleFlow");
        
        TablePartitionPlanEntity tablePartitionPlanEntity = new TablePartitionPlanEntity();
        
        when(mockPartitionPlanService.getDatabasePartitionPlanById(anyString())).thenReturn(databasePartitionPlanEntity);
        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyString())).thenReturn(Arrays.asList(tablePartitionPlanEntity));
        when(mockScheduleService.nullSafeGetById(anyLong())).thenReturn(mockScheduleEntity);

        partitionPlanJob.execute(mockContext);
        
        verify(mockPartitionPlanTaskService).executePartitionPlan(anyString(), anyList());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}