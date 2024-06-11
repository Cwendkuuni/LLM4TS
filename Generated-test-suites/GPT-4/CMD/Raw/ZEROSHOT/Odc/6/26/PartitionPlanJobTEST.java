
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @Mock
    private JobExecutionContext context;
    @Mock
    private ScheduleTaskUtils scheduleTaskUtils;
    @Mock
    private ScheduleEntity scheduleEntity;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;
    @Mock
    private PartitionPlanService partitionPlanService;
    @Mock
    private DatabasePartitionPlanEntity databasePartitionPlanEntity;
    @Mock
    private PartitionPlanJobParameters partitionPlanJobParameters;

    private PartitionPlanJob partitionPlanJob;
    private final Long scheduleId = 1L;
    private final Long databasePartitionPlanId = 1L;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        partitionPlanJob = new PartitionPlanJob();
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn("");
        when(JsonUtils.fromJson(anyString(), eq(PartitionPlanJobParameters.class))).thenReturn(partitionPlanJobParameters);
        when(partitionPlanJobParameters.getDatabasePartitionPlanId()).thenReturn(databasePartitionPlanId);
        when(partitionPlanService.getDatabasePartitionPlanById(databasePartitionPlanId)).thenReturn(databasePartitionPlanEntity);
    }

    @Test
    public void testExecute() throws SchedulerException {
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlanEntity = new TablePartitionPlanEntity();
        tablePartitionPlans.add(tablePartitionPlanEntity);

        when(databasePartitionPlanEntity.isConfigEnabled()).thenReturn(true);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(databasePartitionPlanId)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        verify(scheduleService, never()).terminate(scheduleEntity);
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(any(), any());
    }

    @Test
    public void testInterrupt() {
        try {
            partitionPlanJob.interrupt();
        } catch (Exception e) {
            assert(e instanceof UnExpectedException);
            return;
        }
        fail("Expected UnExpectedException");
    }
}
