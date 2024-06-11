import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;

public class PartitionPlanJobTest {

    private PartitionPlanTaskService mockPartitionPlanTaskService;
    private PartitionPlanService mockPartitionPlanService;
    private ScheduleService mockScheduleService;
    private JobExecutionContext mockJobExecutionContext;
    private DatabasePartitionPlanEntity mockDatabasePartitionPlanEntity;
    private TablePartitionPlanEntity mockTablePartitionPlanEntity;
    private ScheduleEntity mockScheduleEntity;
    
    private PartitionPlanJob partitionPlanJob;
    
    @Before
    public void setup() {
        mockPartitionPlanTaskService = mock(PartitionPlanTaskService.class);
        mockPartitionPlanService = mock(PartitionPlanService.class);
        mockScheduleService = mock(ScheduleService.class);
        mockJobExecutionContext = mock(JobExecutionContext.class);
        mockDatabasePartitionPlanEntity = mock(DatabasePartitionPlanEntity.class);
        mockTablePartitionPlanEntity = mock(TablePartitionPlanEntity.class);
        mockScheduleEntity = mock(ScheduleEntity.class);
        
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanTaskService = mockPartitionPlanTaskService;
        partitionPlanJob.partitionPlanService = mockPartitionPlanService;
        partitionPlanJob.scheduleService = mockScheduleService;
    }

    @Test
    public void testExecute() throws SchedulerException {
        // Given
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(mockJobExecutionContext)).thenReturn(scheduleId);
        when(mockScheduleService.nullSafeGetById(scheduleId)).thenReturn(mockScheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        when(JsonUtils.fromJson(mockScheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
        .thenReturn(jobParameters);
        
        when(mockPartitionPlanService.getDatabasePartitionPlanById(anyLong()))
        .thenReturn(mockDatabasePartitionPlanEntity);
        
        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
        .thenReturn(List.of(mockTablePartitionPlanEntity));
        
        when(mockDatabasePartitionPlanEntity.isConfigEnabled()).thenReturn(true);
        
        // When
        partitionPlanJob.execute(mockJobExecutionContext);
        
        // Then
        verify(mockPartitionPlanTaskService).executePartitionPlan(anyLong(), anyList());
        
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_SchedulerException() throws SchedulerException {
        // Given
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(mockJobExecutionContext)).thenReturn(scheduleId);
        when(mockScheduleService.nullSafeGetById(scheduleId)).thenReturn(mockScheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        when(JsonUtils.fromJson(mockScheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
        .thenReturn(jobParameters);
        
        when(mockPartitionPlanService.getDatabasePartitionPlanById(anyLong()))
        .thenReturn(mockDatabasePartitionPlanEntity);
        
        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
        .thenReturn(List.of(mockTablePartitionPlanEntity));
        
        when(mockDatabasePartitionPlanEntity.isConfigEnabled()).thenReturn(false);
        
        // When
        partitionPlanJob.execute(mockJobExecutionContext);
        
        // Then
        verify(mockScheduleService).terminate(mockScheduleEntity);
         
    }

    @Test
    public void testInterrupt() {
        // When
        try {
            partitionPlanJob.interrupt();
            fail("Expected UnExpectedException");
        } catch (UnExpectedException e) {
            // Expected
        }
    }

}