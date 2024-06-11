import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private PartitionPlanService partitionPlanServiceMock;
 private ScheduleService scheduleServiceMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);

 partitionPlanJob = new PartitionPlanJob();
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 scheduleServiceMock = mock(ScheduleService.class);

 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);

 partitionPlanJob.partitionPlanTaskService = partitionPlanTaskServiceMock;
 partitionPlanJob.partitionPlanService = partitionPlanServiceMock;
 partitionPlanJob.scheduleService = scheduleServiceMock;
 }

 @Test
 public void testExecute_validSchedule() throws Exception {
 // Given
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));

 DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(true);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

 List<TablePartitionPlanEntity> tablePartitionPlans = mock(List.class);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

 JobExecutionContext context = mock(JobExecutionContext.class);
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 when(context.getMergedJobDataMap()).thenReturn(new JobDataMap());

 // When
 partitionPlanJob.execute(context);

 // Then
 // Add verification logic here to check if the methods called in the test are working as expected.
 // For example:
 assertEquals(1L, databasePartitionPlan.getId());
 assertNotNull(databasePartitionPlan);
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_disableDatabasePartitionPlan() throws Exception {
 // Given
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));

 DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(false);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

 // When & Then
 partitionPlanJob.execute(mock(JobExecutionContext.class));
 }

 @Test(expected = SchedulerException.class)
 public void testExecute_scheduleNotFound() throws Exception {
 // Given
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));
 when(scheduleServiceMock.nullSafeGetById(1L)).thenThrow(new SchedulerException());

 // When & Then
 partitionPlanJob.execute(mock(JobExecutionContext.class));
 }
}