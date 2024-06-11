import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.google.gson.Gson;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import lombok.extern.slf4j.Slf4j;

@RunWith(MockitoJUnit.MockitoJUnitRunner.class)
@Slf4j
public class PartitionPlanJobTest {

 @Mock
 private PartitionPlanService partitionPlanService;

 @Mock
 private PartitionPlanTaskService partitionPlanTaskService;

 @Mock
 private ScheduleService scheduleService;

 @Mock
 private SpringContextUtil springContextUtil;

 @Mock
 private ScheduleTaskUtils scheduleTaskUtils;

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 @Rule
 public MockitoRule mockitoRule = MockitoJUnit.rule();

 private DatabasePartitionPlanEntity databasePartitionPlan;
 private TablePartitionPlanEntity tablePartitionPlan;
 private ScheduleEntity scheduleEntity;
 private PartitionPlanJobParameters jobParameters;

 @Before
 public void setUp() {
 Gson gson = new Gson();
 databasePartitionPlan = new DatabasePartitionPlanEntity();
 tablePartitionPlan = new TablePartitionPlanEntity();
 scheduleEntity = new ScheduleEntity();
 jobParameters = new PartitionPlanJobParameters();
 scheduleEntity.setJobParametersJson(gson.toJson(jobParameters));
 }

 // Test for initialize Dependencies
 @org.junit.Test
 public void testInitializeDependencies() {
 // when
 partitionPlanJob = new PartitionPlanJob();

 // then
 partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
 partitionPlanJob.partitionPlanService = partitionPlanService;
 partitionPlanJob.scheduleService = scheduleService;
 }

 // Test for execute method
 @org.junit.Test
 public void testExecute() throws SchedulerException {
 // Given
 Long scheduleId = 1L;
 when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
 when(partitionPlanService.getDatabasePartitionPlanById(any(Long.class))).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(any(Long.class)))
 .thenReturn(java.util.Arrays.asList(tablePartitionPlan));
 when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
 when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
 when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
 when(scheduleTaskUtils.getScheduleId(any(JobExecutionContext.class))).thenReturn(scheduleId);

 // When
 partitionPlanJob.execute(any(JobExecutionContext.class));

 // Then
 verify(partitionPlanService).getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId());
 verify(partitionPlanService).getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId());
 verify(partitionPlanTaskService).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
 tablePartitionPlans);
 }

 // Test for interrupt method
 @org.junit.Test(expected = UnExpectedException.class)
 public void testInterrupt() {
 // When
 partitionPlanJob.interrupt();

 // Then
 // No verification is needed here as the method does not call any mocked method
 }
}