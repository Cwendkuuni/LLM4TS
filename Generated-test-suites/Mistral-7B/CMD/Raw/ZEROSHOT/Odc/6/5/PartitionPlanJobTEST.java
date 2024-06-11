import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private ScheduleService scheduleService;
    private PartitionPlanService partitionPlanService;
    private PartitionPlanTaskService partitionPlanTaskService;
    private ScheduleEntity scheduleEntity;
    private JobExecutionContext context;
    private DatabasePartitionPlanEntity databasePartitionPlan;
    private List<TablePartitionPlanEntity> tablePartitionPlans;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        scheduleService = mock(ScheduleService.class);
        partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        SpringContextUtil.setBean(ScheduleService.class, scheduleService);
        SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanService);
        SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskService);
        context = mock(JobExecutionContext.class);
        databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        tablePartitionPlans = mock(List.class);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecuteNoScheduleFound() {
        when(scheduleEntity.getId()).thenThrow(new Exception());
        partitionPlanJob.execute(context);
        assertFalse(scheduleEntity.isTerminated());
    }

    @Test
    public void testExecute() {
        when(scheduleEntity.getId()).thenReturn(1L);
        partitionPlanJob.execute(context);
        assertNotNull(databasePartitionPlan);
        assertNotNull(tablePartitionPlans);
        assertTrue(databasePartitionPlan.isConfigEnabled());
        assertTrue(!tablePartitionPlans.isEmpty());
        assertFalse(scheduleEntity.isTerminated());
    }

    @Test
    public void testInterrupt() {
        partitionPlanJob.interrupt();
        assertFalse(true);
    }
}