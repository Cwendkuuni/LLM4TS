import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.quartz.JobExecutionContext;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private JobExecutionContext context;

    @Mock
    private PartitionPlanJobParameters jobParameters;

    @Mock
    private DatabasePartitionPlanEntity databasePartitionPlan;

    @Mock
    private ScheduleEntity scheduleEntity;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() throws Exception {

    }

    @Test(expected = UnExpectedException.class)
    public void interruptTest() {
        partitionPlanJob.interrupt();
    }

    @Test
    public void executeTest_allPaths() {
        when(scheduleEntity.getJobParametersJson()).thenReturn("");
        when(jobParameters.getDatabasePartitionPlanId()).thenReturn(1L);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(new ArrayList<TablePartitionPlanEntity>());
        partitionPlanJob.execute(context);
        verify(scheduleService, times(1)).nullSafeGetById(Mockito.any());
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(Mockito.anyLong());
        verify(partitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(Mockito.anyLong());
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(Mockito.anyString(), Mockito.anyList());
    }

    @Test
    public void executeTest_configIsDisabled() {
        when(scheduleEntity.getJobParametersJson()).thenReturn("");
        when(jobParameters.getDatabasePartitionPlanId()).thenReturn(1L);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);
        partitionPlanJob.execute(context);
        verify(scheduleService, times(1)).nullSafeGetById(Mockito.any());
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(Mockito.anyLong());
        verify(partitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(Mockito.anyLong());
        verify(scheduleService, times(1)).terminate(Mockito.any());
    }

    @Test
    public void executeTest_noTablePartitionPlans() {
        when(scheduleEntity.getJobParametersJson()).thenReturn("");
        when(jobParameters.getDatabasePartitionPlanId()).thenReturn(1L);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(new ArrayList<TablePartitionPlanEntity>());
        partitionPlanJob.execute(context);
        verify(scheduleService, times(1)).nullSafeGetById(Mockito.any());
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(Mockito.anyLong());
        verify(partitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(Mockito.anyLong());
        verify(scheduleService, times(1)).terminate(Mockito.any());
    }
}