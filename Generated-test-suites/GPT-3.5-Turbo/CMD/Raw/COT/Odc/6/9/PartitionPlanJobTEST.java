
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private JobExecutionContext jobContext;
    private PartitionPlanTaskService mockPartitionPlanTaskService;
    private PartitionPlanService mockPartitionPlanService;
    private ScheduleService mockScheduleService;
    
    @Before
    public void setup() {
        mockPartitionPlanTaskService = mock(PartitionPlanTaskService.class);
        mockPartitionPlanService = mock(PartitionPlanService.class);
        mockScheduleService = mock(ScheduleService.class);
        
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanTaskService = mockPartitionPlanTaskService;
        partitionPlanJob.partitionPlanService = mockPartitionPlanService;
        partitionPlanJob.scheduleService = mockScheduleService;
        
        jobContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute_ValidSchedule() throws SchedulerException {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(mockScheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        
        jobParameters.setDatabasePartitionPlanId(1L);
        databasePartitionPlan.setConfigEnabled(true);
        tablePartitionPlan.setId(1L);
        
        when(mockPartitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(List.of(tablePartitionPlan));
        
        partitionPlanJob.execute(jobContext);
        
        verify(mockPartitionPlanTaskService).executePartitionPlan(eq(databasePartitionPlan.getFlowInstanceId()), eq(List.of(tablePartitionPlan)));
    }

    @Test
    public void testExecute_DisabledDatabasePartitionPlan() throws SchedulerException {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(mockScheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        
        jobParameters.setDatabasePartitionPlanId(1L);
        databasePartitionPlan.setConfigEnabled(false);
        
        when(mockPartitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(List.of(tablePartitionPlan));
        
        partitionPlanJob.execute(jobContext);
        
        verify(mockScheduleService).terminate(any(ScheduleEntity.class));
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}
