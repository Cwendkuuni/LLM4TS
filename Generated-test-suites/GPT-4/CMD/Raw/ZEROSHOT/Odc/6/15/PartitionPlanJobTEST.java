
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private JobExecutionContext context;
    
    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;
    
    @Mock
    private PartitionPlanService partitionPlanService;
    
    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Test
    public void testExecute() {
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(new ScheduleEntity());

        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(new DatabasePartitionPlanEntity());

        List<TablePartitionPlanEntity> tablePartitionPlanEntities =
                Collections.singletonList(new TablePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(
                tablePartitionPlanEntities);
        
        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, times(1)).executePartitionPlan(any(UUID.class),
                anyList());
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteWithException() {
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);

        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new SchedulerException());

        partitionPlanJob.execute(context);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}

