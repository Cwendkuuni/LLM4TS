import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.util.Arrays;

public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskService mockPartitionPlanTaskService;

    @Mock
    private PartitionPlanService mockPartitionPlanService;

    @Mock
    private ScheduleService mockScheduleService;

    @Mock
    private JobExecutionContext context;

    @InjectMocks
    private PartitionPlanJob partitionPlanJobUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_ThrowsException() {
        Long scheduleId = 1L;
        
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(mockScheduleService.nullSafeGetById(scheduleId)).thenThrow(new RuntimeException());
        
        partitionPlanJobUnderTest.execute(context);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_ThrowsSchedulerException() {
        DatabasePartitionPlanEntity dbPartitionPlan = new DatabasePartitionPlanEntity();

        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        when(mockScheduleService.nullSafeGetById(anyLong())).thenReturn(new ScheduleEntity());
        when(mockPartitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(dbPartitionPlan);
        
        partitionPlanJobUnderTest.execute(context);
    }

    @Test
    public void testExecute_Success() {
        DatabasePartitionPlanEntity dbPartitionPlan = new DatabasePartitionPlanEntity();
        dbPartitionPlan.setConfigEnabled(true);
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();

        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        when(mockScheduleService.nullSafeGetById(anyLong())).thenReturn(new ScheduleEntity());
        when(mockPartitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(dbPartitionPlan);
        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(Arrays.asList(tablePartitionPlan));
        
        partitionPlanJobUnderTest.execute(context);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJobUnderTest.interrupt();
    }
}