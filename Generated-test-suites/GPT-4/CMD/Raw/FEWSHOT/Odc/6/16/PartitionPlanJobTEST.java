
package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import java.util.ArrayList;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {
    
    
    @Mock
    private PartitionPlanTaskService partitionPlanTaskServiceMock;
    
    @Mock
    private PartitionPlanService partitionPlanServiceMock;
    
    @Mock
    private ScheduleService scheduleServiceMock;
    
    @Mock
    private JobExecutionContext contextMock;
    
    @Mock
    private ScheduleEntity scheduleEntityMock;
    
    @Mock
    private DatabasePartitionPlanEntity databasePartitionPlanEntityMock;
    
    @Mock
    private PartitionPlanJobParameters partitionPlanJobParametersMock;
    
    @Test
    public void testExecute() {
        
        when(ScheduleTaskUtils.getScheduleId(contextMock)).thenReturn(1L);
       
        String json = "{\"databasePartitionPlanId\":1}";
        when(scheduleEntityMock.getJobParametersJson()).thenReturn(json);
        
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlanEntityMock);
        
        when(databasePartitionPlanEntityMock.isConfigEnabled()).thenReturn(true);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(new ArrayList());
        
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.execute(contextMock);
        
        verify(scheduleServiceMock, times(1)).terminate(scheduleEntityMock);
    }
    
    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.interrupt();
    }
}
