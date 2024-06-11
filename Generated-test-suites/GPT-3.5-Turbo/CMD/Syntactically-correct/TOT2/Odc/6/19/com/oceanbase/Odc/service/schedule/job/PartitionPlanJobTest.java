
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

public class PartitionPlanJobTest {
  
  private PartitionPlanTaskService partitionPlanTaskService;
  private PartitionPlanService partitionPlanService;
  private ScheduleService scheduleService;
  private PartitionPlanJob partitionPlanJob;
  
  @Before
  public void setUp() {
    partitionPlanTaskService = mock(PartitionPlanTaskService.class);
    partitionPlanService = mock(PartitionPlanService.class);
    scheduleService = mock(ScheduleService.class);
    
    partitionPlanJob = new PartitionPlanJob();
    partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
    partitionPlanJob.partitionPlanService = partitionPlanService;
    partitionPlanJob.scheduleService = scheduleService;
  }
  
  @Test
  public void testExecute() {
    JobExecutionContext context = mock(JobExecutionContext.class);
    ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
    PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
    jobParameters.setDatabasePartitionPlanId(1L);
    DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
    List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
    
    when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
    when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
    when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
    when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
    
    partitionPlanJob.execute(context);
    
    verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
  }
  
  @Test(expected = UnExpectedException.class)
  public void testInterrupt() {
    partitionPlanJob.interrupt();
  }
}
