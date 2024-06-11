import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class PartitionPlanJobTest {

 private static final String SCHEDULE_ID = "1";
 private static final String JOB_PARAMETERS_JSON = "{}";
 private static final Long DATABASE_PARTITION_PLAN_ID = 1L;
 private static final String FLOW_INSTANCE_ID = "1";

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 @Mock
 private PartitionPlanTaskService partitionPlanTaskService;

 @Mock
 private PartitionPlanService partitionPlanService;

 @Mock
 private ScheduleService scheduleService;

 @Test
 public void testExecute_whenScheduleIsNotFound_thenLogAndReturn() {
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 Mockito.when(scheduleService.nullSafeGetById(SCHEDULE_ID))
 .thenThrow(new Exception());

 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));
 Mockito.verify(log).warn("Schedule not found,scheduleId=" + SCHEDULE_ID);
 }

 @Test
 public void testExecute_whenDatabasePartitionPlanIsDisabled_thenStopJob() throws Exception {
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 Mockito.when(scheduleService.nullSafeGetById(SCHEDULE_ID)).thenReturn(scheduleEntity);
 Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn(JOB_PARAMETERS_JSON);
 Mockito.when(partitionPlanService.getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID))
 .thenReturn(databasePartitionPlan);
 Mockito.when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID))
 .thenReturn(null);

 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));
 Mockito.verify(log).info(
 "database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId=" + SCHEDULE_ID);
 }

 @Test
 public void testExecute_whenDatabasePartitionPlanIsEnabled_thenCallExecutePartitionPlan() throws Exception {
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 TablePartitionPlanEntity tablePartitionPlan = Mockito.mock(TablePartitionPlanEntity.class);
 List<TablePartitionPlanEntity> tablePartitionPlans = Mockito.mock(List.class);
 Mockito.when(scheduleService.nullSafeGetById(SCHEDULE_ID)).thenReturn(scheduleEntity);
 Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn(JOB_PARAMETERS_JSON);
 Mockito.when(partitionPlanService.getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID))
 .thenReturn(databasePartitionPlan);
 Mockito.when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID))
 .thenReturn(tablePartitionPlans);
 Mockito.when(tablePartitionPlans.isEmpty()).thenReturn(true);
 Mockito.when(tablePartitionPlans.get(0)).thenReturn(tablePartitionPlan);

 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));
 Mockito.verify(partitionPlanTaskService).executePartitionPlan(FLOW_INSTANCE_ID, tablePartitionPlans);
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() {
 partitionPlanJob.interrupt();
 }
}