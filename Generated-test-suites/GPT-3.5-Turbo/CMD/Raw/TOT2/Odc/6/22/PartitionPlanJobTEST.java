import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import lombok.extern.slf4j.Slf4j;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class PartitionPlanJobTest {

    @Mock
    JobExecutionContext context;

    @Mock
    PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    PartitionPlanService partitionPlanService;

    @Mock
    ScheduleService scheduleService;

    @Test
    public void testExecute() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        
        Mockito.when(SpringContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        Mockito.when(SpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        Mockito.when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        Mockito.when(scheduleService.nullSafeGetById(Mockito.anyLong())).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        String json = "{}";
        jobParameters = JsonUtils.fromJson(json, PartitionPlanJobParameters.class);
        DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
        TablePartitionPlanEntity tablePartitionPlanEntity = new TablePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlanEntities = new ArrayList<>();
        tablePartitionPlanEntities.add(tablePartitionPlanEntity);

        Mockito.when(partitionPlanService.getDatabasePartitionPlanById(Mockito.anyLong())).thenReturn(databasePartitionPlanEntity);
        Mockito.when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(Mockito.anyLong())).thenReturn(tablePartitionPlanEntities);

        // Test for database partition plan is disable or no table need to process
        databasePartitionPlanEntity.setConfigEnabled(false);
        partitionPlanJob.execute(context);
        assertFalse(tablePartitionPlanEntities.isEmpty());
        
        // Test for create partition-plan database change task failed
        Mockito.doThrow(new RuntimeException()).when(scheduleService).terminate(scheduleEntity);
        partitionPlanJob.execute(context);
        Mockito.verify(scheduleService, Mockito.times(2)).terminate(scheduleEntity);

        // Test for exception handling in executePartitionPlan method
        Mockito.doThrow(new Exception()).when(partitionPlanTaskService).executePartitionPlan(Mockito.anyLong(), Mockito.anyList());
        partitionPlanJob.execute(context);
        Mockito.verify(partitionPlanTaskService, Mockito.times(1)).executePartitionPlan(Mockito.anyLong(), Mockito.anyList());
    }

    @Test
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        try {
            partitionPlanJob.interrupt();
            fail("Expected UnExpectedException");
        } catch (UnExpectedException e) {
            // As expected
        }
    }
}