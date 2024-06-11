import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.any;

import java.util.ArrayList;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private DatabasePartitionPlanEntity databasePartitionPlanEntity;

    @Mock
    private PartitionPlanJobParameters jobParameters;

    @Mock
    private TablePartitionPlanEntity tablePartitionPlanEntity;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected= UnExpectedException.class)
    public void testInterrupt(){
        partitionPlanJob.interrupt();
    }

    @Test
    public void testExecute() {
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(),PartitionPlanJobParameters.class)).thenReturn(jobParameters);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId())).thenReturn(databasePartitionPlanEntity);
        ArrayList<TablePartitionPlanEntity> tablePlans = new ArrayList<>();
        tablePlans.add(tablePartitionPlanEntity);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId())).thenReturn(tablePlans);
        when(databasePartitionPlanEntity.isConfigEnabled()).thenReturn(true);
        doNothing().when(partitionPlanTaskService).executePartitionPlan(databasePartitionPlanEntity.getFlowInstanceId(), tablePlans);
        partitionPlanJob.execute(context);
    }
}