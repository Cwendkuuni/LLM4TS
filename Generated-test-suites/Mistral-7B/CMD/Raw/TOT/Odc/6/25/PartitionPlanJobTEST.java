import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.Model.PartitionPlanJobParameters;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
@Slf4j
public class PartitionPlanJobTest {

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 @Mock
 private PartitionPlanTaskService partitionPlanTaskService;

 @Mock
 private PartitionPlanService partitionPlanService;

 @Mock
 private ScheduleService scheduleService;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecute_validScheduleAndTablePartitionPlan_partitionPlanIsExecuted() {
 // Given
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setFlowInstanceId(1L);
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());

 when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);
 when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

 // When
 partitionPlanJob.execute(mock(JobExecutionContext.class));

 // Then
 verify(partitionPlanTaskService, times(1)).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecute_databasePartitionPlanIsDisabled_exceptionThrown() {
 // Given
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setFlowInstanceId(1L);
 databasePartitionPlan.setConfigEnabled(false);

 when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

 // When
 partitionPlanJob.execute(mock(JobExecutionContext.class));

 // Then (database partition plan is disabled, should not call executePartitionPlan method)
 verify(partitionPlanTaskService, never()).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
 }

 @Test(expected = SchedulerException.class)
 public void testExecute_exceptionThrownDuringScheduleTermination_exceptionThrown() {
 // Given
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setFlowInstanceId(1L);
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());

 when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);
 when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
 when(scheduleService.terminate(scheduleEntity))
 .throwing(SchedulerException.class);

 // When & Then
 partitionPlanJob.execute(mock(JobExecutionContext.class));
 verify(log, times(1)).warn("Stop partition plan job failed.");
 }

 @Test
 public void testInterrupt_unexpectedExceptionThrown() {
 // When & Then
 partitionPlanJob.interrupt();
 verify(log, times(1)).warn("Interrupt partition plan job unexpectedly.");
 }
}