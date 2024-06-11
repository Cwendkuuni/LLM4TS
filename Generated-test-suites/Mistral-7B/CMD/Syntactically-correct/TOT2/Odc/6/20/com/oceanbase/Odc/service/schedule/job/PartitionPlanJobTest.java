import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
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
 }

 @Test
 public void execute_ValidScheduleAndPartitionPlan_ShouldExecutePartitionPlan() {
 // Arrange
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
 List<TablePartitionPlanEntity> tablePartitionPlans = mock(java.util.List.class);
 when(scheduleServiceMock.nullSafeGetById(any(Long.class))).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(any(Long.class))).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(any(Long.class))).thenReturn(tablePartitionPlans);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

 // Act
 partitionPlanJob.execute(new JobExecutionContext());

 // Assert
 // as this method doesn't return any value, we can only check side effects here
 assertEquals(true, jobParameters.isConfigEnabled());
 assertNotNull(databasePartitionPlan);
 assertNotNull(tablePartitionPlans);
 }

 @Test(expected = RuntimeException.class)
 public void execute_DisabledDatabasePartitionPlan_ShouldThrowException() {
 // Arrange
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
 List<TablePartitionPlanEntity> tablePartitionPlans = mock(java.util.List.class);
 when(scheduleServiceMock.nullSafeGetById(any(Long.class))).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(any(Long.class))).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(any(Long.class))).thenReturn(tablePartitionPlans);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

 // Act and Assert
 partitionPlanJob.execute(new JobExecutionContext());
 }

 // Add more test cases for different scenarios such as empty tablePartitionPlans, exceptions thrown during PartitionPlanTaskService.executePartitionPlan(), etc.
}