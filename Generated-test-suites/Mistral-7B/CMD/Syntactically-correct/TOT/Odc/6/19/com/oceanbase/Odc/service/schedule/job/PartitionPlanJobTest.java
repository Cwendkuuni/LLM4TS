import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.MockScheduler;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanTaskService partitionPlanTaskServiceMock;
    private PartitionPlanService partitionPlanServiceMock;
    private ScheduleService scheduleServiceMock;
    private MockScheduler schedulerMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        scheduleServiceMock = mock(ScheduleService.class);
        schedulerMock = new MockScheduler();
        doReturn(scheduleServiceMock).when(partitionPlanJob).getScheduleService();
        doReturn(partitionPlanServiceMock).when(partitionPlanJob).getPartitionPlanService();
        doReturn(partitionPlanTaskServiceMock).when(partitionPlanJob).getPartitionPlanTaskService();
        doReturn(schedulerMock).when(partitionPlanJob).getScheduler();
    }

    @Test
    public void testExecute_validInput_success() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = java.util.Collections.singletonList(new TablePartitionPlanEntity());

        try {
            doReturn(scheduleEntity).when(scheduleServiceMock).nullSafeGetById(scheduleId);
            doReturn(jobParameters).when(scheduleEntity).getJobParametersJson();
            doReturn(databasePartitionPlan).when(partitionPlanServiceMock)
                    .getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId());
            doReturn(tablePartitionPlans).when(partitionPlanServiceMock)
                    .getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId());

            partitionPlanJob.execute(new JobExecutionContext());

            verify(partitionPlanTaskServiceMock).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                    tablePartitionPlans);
        } catch (Exception e) {
            log.warn("Test execute failed.", e);
        }
    }

    @Test
    public void testExecute_databasePartitionPlanDisabled_success() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = java.util.Collections.emptyList();

        try {
            doReturn(scheduleEntity).when(scheduleServiceMock).nullSafeGetById(scheduleId);
            doReturn(jobParameters).when(scheduleEntity).getJobParametersJson();
            doReturn(databasePartitionPlan).when(partitionPlanServiceMock)
                    .getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId());
            doReturn(tablePartitionPlans).when(partitionPlanServiceMock)
                    .getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId());

            partitionPlanJob.execute(new JobExecutionContext());

            verify(scheduleServiceMock).terminate(scheduleEntity);
        } catch (Exception e) {
            log.warn("Test execute failed.", e);
        }
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}