package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;
    
    @Mock
    private PartitionPlanService partitionPlanService;
    
    @Mock
    private ScheduleService scheduleService;
    
    @InjectMocks
    private PartitionPlanJob partitionPlanJob;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
  
    @Test
    public void testExecute() {
        // mocks
        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
        DatabasePartitionPlanEntity databasePartitionPlanEntity = mock(DatabasePartitionPlanEntity.class);
        TablePartitionPlanEntity tablePartitionPlanEntity = mock(TablePartitionPlanEntity.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
     
        // Return list with one mock object
        ArrayList<TablePartitionPlanEntity> tablePartitionPlansList = new ArrayList();
        tablePartitionPlansList.add(tablePartitionPlanEntity); 
      
        when(jobExecutionContext.getMergedJobDataMap().getLong("ScheduleId")).thenReturn(Long.valueOf(1));
        when(scheduleService.nullSafeGetById(Long.valueOf(1))).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(Long.valueOf(1))).thenReturn(databasePartitionPlanEntity);
        when(databasePartitionPlanEntity.isConfigEnabled()).thenReturn(true);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(Long.valueOf(1))).thenReturn(tablePartitionPlansList);
        
        // Execute the test method
        partitionPlanJob.execute(jobExecutionContext);
        
        // verify the methods being called
        verify(scheduleService).nullSafeGetById(Long.valueOf(1));
        verify(databasePartitionPlanEntity).isConfigEnabled();
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}
