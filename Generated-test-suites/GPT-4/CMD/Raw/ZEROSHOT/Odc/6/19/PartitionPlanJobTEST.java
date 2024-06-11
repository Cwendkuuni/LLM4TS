
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

    @Mock
    private JobExecutionContext context;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setup() {
        partitionPlanJob = new PartitionPlanJob(partitionPlanTaskService, partitionPlanService, scheduleService);
    }

    @Test
    public void testExecute() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        long scheduleID = 1L;
        scheduleEntity.setId(scheduleID);
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleID);
        when(scheduleService.nullSafeGetById(scheduleID)).thenReturn(scheduleEntity);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class)).thenReturn(jobParameters);
        DatabasePartitionPlanEntity dbPartitionPlan = new DatabasePartitionPlanEntity();
        dbPartitionPlan.setFlowInstanceId("flow1");
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId())).thenReturn(dbPartitionPlan);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId())).thenReturn
                (tablePartitionPlans);

        partitionPlanJob.execute(context);

        verify(scheduleService, times(1)).nullSafeGetById(scheduleID);
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(dbPartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test
    public void testInterrupt() {
        exceptionRule.expect(UnExpectedException.class);
        partitionPlanJob.interrupt();
    }

}
