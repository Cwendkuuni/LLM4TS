import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class PartitionPlanJobTest {

    private PartitionPlanTaskService partitionPlanTaskService;
    private PartitionPlanService partitionPlanService;
    private ScheduleService scheduleService;
    private PartitionPlanJob partitionPlanJob;
    private JobExecutionContext jobContext;

    @Before
    public void setUp() {
        partitionPlanTaskService = Mockito.mock(PartitionPlanTaskService.class);
        partitionPlanService = Mockito.mock(PartitionPlanService.class);
        scheduleService = Mockito.mock(ScheduleService.class);
        SpringContextUtil.setBean("PartitionPlanTaskService", partitionPlanTaskService);
        SpringContextUtil.setBean("PartitionPlanService", partitionPlanService);
        SpringContextUtil.setBean("ScheduleService", scheduleService);
        partitionPlanJob = new PartitionPlanJob();
        jobContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute() throws SchedulerException {
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("");
        Mockito.when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        Mockito.when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        Mockito.when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(List.of(tablePartitionPlan));
        Mockito.when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        partitionPlanJob.execute(jobContext);
        Mockito.verify(partitionPlanTaskService, Mockito.times(1)).executePartitionPlan(Mockito.anyLong(), Mockito.anyList());
    }

    @Test
    public void testInterrupt() {
        try {
            partitionPlanJob.interrupt();
        } catch (RuntimeException e) {
            assertTrue(e instanceof RuntimeException);
        }
    }
}