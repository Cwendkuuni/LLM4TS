import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@ suppressWarnings("unchecked")
public class PartitionPlanJobTest {

 private PartitionPlanService partitionPlanService;
 private PartitionPlanTaskService partitionPlanTaskService;
 private ScheduleService scheduleService;
 private PartitionPlanJob partitionPlanJob;
 private JobExecutionContext context;

 @Before
 public void setup() {
 partitionPlanService = Mockito.mock(PartitionPlanService.class);
 partitionPlanTaskService = Mockito.mock(PartitionPlanTaskService.class);
 scheduleService = Mockito.mock(ScheduleService.class);
 partitionPlanJob = new PartitionPlanJob();
 context = Mockito.mock(JobExecutionContext.class);
 }

 @Test
 public void testExecute_validSchedule_partitionsCreated() {
 // Given
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 List<TablePartitionPlanEntity> tablePartitionPlans = Mockito.mock(List.class);

 Mockito.when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
 Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
 Mockito.when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 Mockito.when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);
 Mockito.when(tablePartitionPlans.isEmpty()).thenReturn(false);

 partitionPlanJob.execute(context);

 // When
 ArgumentCaptor<Long> scheduleIdCaptor = ArgumentCaptor.forClass(Long.class);
 verify(scheduleService).terminate(scheduleEntity);
 // Add your verification logic for partitionPlanService and partitionPlanTaskService here
 }

 // Add more test cases for edge cases and error scenarios here
}