import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanService partitionPlanService;
 private PartitionPlanTaskService partitionPlanTaskService;
 private ScheduleService scheduleService;

 @Before
 public void setup() {
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanService = mock(PartitionPlanService.class);
 partitionPlanTaskService = mock(PartitionPlanTaskService.class);
 scheduleService = mock(ScheduleService.class);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanService);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskService);
 SpringContextUtil.setBean(ScheduleService.class, scheduleService);
 partitionPlanJob.partitionPlanService = partitionPlanService;
 partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
 partitionPlanJob.scheduleService = scheduleService;
 }

 @Test
 public void testExecute_validScheduleEntityAndPartitions_executePartitionPlan() throws Exception {
 // given
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setId(1L);
databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = Mockito.mockList(TablePartitionPlanEntity.class);

 when(partitionPlanService.getDatabasePartitionPlanById(ArgumentMatchers.eq(1L)))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(ArgumentMatchers.eq(1L)))
 .thenReturn(tablePartitionPlans);
 when(scheduleService.nullSafeGetById(ArgumentMatchers.eq(1L)))
 .thenReturn(scheduleEntity);

 // when
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 Mockito.verify(partitionPlanTaskService).executePartitionPlan(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(tablePartitionPlans));
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_invalidScheduleEntityOrDisabledPartition_doNotExecutePartitionPlan() throws Exception {
 // given
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setId(1L);
databasePartitionPlan.setConfigEnabled(false);
 List<TablePartitionPlanEntity> tablePartitionPlans = Mockito.mockList(TablePartitionPlanEntity.class);

 when(partitionPlanService.getDatabasePartitionPlanById(ArgumentMatchers.eq(1L)))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(ArgumentMatchers.eq(1L)))
 .thenReturn(tablePartitionPlans);
 when(scheduleService.nullSafeGetById(ArgumentMatchers.eq(1L)))
 .thenThrow(new Exception());

 // when, then
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));
 }

 @Test
 public void testExecute_scheduleNotFound_terminateSchedule() throws Exception {
 // given
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setId(1L);
databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = Mockito.mockList(TablePartitionPlanEntity.class);

 when(partitionPlanService.getDatabasePartitionPlanById(ArgumentMatchers.eq(1L)))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(ArgumentMatchers.eq(1L)))
 .thenReturn(tablePartitionPlans);
 when(scheduleService.nullSafeGetById(ArgumentMatchers.eq(1L)))
 .thenThrow(new SchedulerException());

 // when
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 Mockito.verify(scheduleService).terminate(ArgumentMatchers.eq(scheduleEntity));
 }
}