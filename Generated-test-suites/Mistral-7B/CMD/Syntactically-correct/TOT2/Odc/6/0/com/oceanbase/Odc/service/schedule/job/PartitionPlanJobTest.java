import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
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
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

@Slf4j
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 @InjectMocks
 private PartitionPlanJobTestHelper partitionPlanJobTestHelper;
 @InjectMocks
 private PartitionPlanService partitionPlanServiceMock;
 @InjectMocks
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 @InjectMocks
 private ScheduleService scheduleServiceMock;
 @InjectMocks
 private ScheduleTaskUtils scheduleTaskUtilsMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 partitionPlanJob = new PartitionPlanJob();
 }

 @Test
 public void testExecute_validScheduleEntity_shouldExecutePartitionPlan() {
 // Given
 ScheduleEntity scheduleEntity = partitionPlanJobTestHelper.getValidScheduleEntity();
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 when(scheduleServiceMock.nullSafeGetById(Long.valueOf(scheduleEntity.getId()))).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(Long.valueOf(jobParameters.getDatabasePartitionPlanId()))).thenReturn(new DatabasePartitionPlanEntity());
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(Long.valueOf(jobParameters.getDatabasePartitionPlanId()))).thenReturn(partitionPlanTestHelper.getValidTablePartitionPlans());

 // When
 partitionPlanJob.execute(mock(JobExecutionContext.class));

 // Then
 verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(anyLong(), anyList());
 }

 @Test(expected = SchedulerException.class)
 public void testExecute_scheduleNotFound_shouldThrowSchedulerException() {
 // Given
 ScheduleEntity scheduleEntity = partitionPlanJobTestHelper.getScheduleEntityWithIdNotFound();

 // When
 try {
 partitionPlanJob.execute(mock(JobExecutionContext.class));
 fail("Exception expected");
 } catch (SchedulerException e) {
 // Then
 assertEquals("Schedule not found,scheduleId=" + scheduleEntity.getId(), e.getMessage());
 }
 }

 @Test
 public void testExecute_databasePartitionPlanDisableOrEmptyTablePartitionPlans_shouldNotExecutePartitionPlan() {
 // Given
 ScheduleEntity scheduleEntity = partitionPlanJobTestHelper.getScheduleEntityWithDatabasePartitionPlanDisableOrEmptyTablePartitionPlans();

 // When
 partitionPlanJob.execute(mock(JobExecutionContext.class));

 // Then
 verify(scheduleServiceMock, times(1)).terminate(scheduleEntity);
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt_shouldThrowUnexpectedException() {
 // When
 partitionPlanJob.interrupt();
 }
}