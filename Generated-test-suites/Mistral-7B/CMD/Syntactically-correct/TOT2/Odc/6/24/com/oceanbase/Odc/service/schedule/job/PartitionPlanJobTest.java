import org.junit.Before;
import org.junit.Test;
import org.mockito. ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

@Slf4j
public class PartitionPlanJobTest {

 @Mock
 private PartitionPlanService partitionPlanService;

 @Mock
 private PartitionPlanTaskService partitionPlanTaskService;

 @Mock
 private ScheduleService scheduleService;

 @Mock
 private JobExecutionContext context;

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecute_validScheduleAndDatabasePartitionPlan() throws Exception {
 // Given
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = new java.util.ArrayList<>();
 tablePartitionPlans.add(new TablePartitionPlanEntity());
 given(scheduleService.getById(ArgumentMatchers.eq(1L))).willReturn(scheduleEntity);
 given(partitionPlanService.getDatabasePartitionPlanById(ArgumentMatchers.eq(1L))).willReturn(databasePartitionPlan);
 given(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(ArgumentMatchers.eq(1L)))
 .willReturn(tablePartitionPlans);
 given(context.getMergedJobDataMap().get("scheduleId")).willReturn(1L);

 // When
 partitionPlanJob.execute(context);

 // Then
 then(partitionPlanTaskService).should().executePartitionPlan(anyLong(), ArgumentMatchers.eq(tablePartitionPlans));
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_disabledDatabasePartitionPlan() throws Exception {
 // Given
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(false);
 List<TablePartitionPlanEntity> tablePartitionPlans = new java.util.ArrayList<>();
 given(scheduleService.getById(ArgumentMatchers.eq(1L))).willReturn(scheduleEntity);
 given(partitionPlanService.getDatabasePartitionPlanById(ArgumentMatchers.eq(1L)))
 .willReturn(databasePartitionPlan);
 given(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(ArgumentMatchers.eq(1L)))
 .willReturn(tablePartitionPlans);
 given(context.getMergedJobDataMap().get("scheduleId")).willReturn(1L);

 // When
 partitionPlanJob.execute(context);

 // Then
 then(scheduleService).should().terminate(ArgumentMatchers.eq(scheduleEntity));
 }

 @Test(expected = SchedulerException.class)
 public void testExecute_scheduleNotFound() throws Exception {
 // Given
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));
 given(scheduleService.getById(ArgumentMatchers.eq(1L))).willThrow(new SchedulerException());
 given(context.getMergedJobDataMap().get("scheduleId")).willReturn(1L);

 // When & Then
 assertThrows(SchedulerException.class, () -> partitionPlanJob.execute(context));
 }
}