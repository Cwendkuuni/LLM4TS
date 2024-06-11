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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class PartitionPlanJobTest {

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private PartitionPlanService partitionPlanServiceMock;
 private ScheduleService scheduleServiceMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
 }

 // Test case for execute method with valid arguments
 @Test
 public void testExecuteValidArguments() throws Exception {
 // Given
 JobExecutionContext context = mock(JobExecutionContext.class);
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
 List<TablePartitionPlanEntity> tablePartitionPlans = mock(List.class);
 when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

 // When
 partitionPlanJob.execute(context);

 // Then
 // Add verification for the expected behavior here
 }

 // Test case for execute method with disabled database partition plan
 @Test
 public void testExecuteDisabledDatabasePartitionPlan() throws Exception {
 // Given
 // Set the databasePartitionPlan.isConfigEnabled() to false
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

 // When & Then
 // Add verification for the expected behavior here
 }

 // Test case for execute method with empty tablePartitionPlans list
 @Test
 public void testExecuteEmptyTablePartitionPlans() throws Exception {
 // Given
 // Set the tablePartitionPlans list to be empty
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(Collections.emptyList());

 // When & Then
 // Add verification for the expected behavior here
 }

 // Test case for execute method with exceptions
 @Test(expected = Exception.class)
 public void testExecuteException() throws Exception {
 // Given
 // Mock an exception in executePartitionPlan method
 when(partitionPlanTaskServiceMock.executePartitionPlan(anyLong(), anyList()))
 .thenThrow(new Exception());

 // When
 partitionPlanJob.execute(context);

 // Then
 // Add verification for the expected behavior here
 }

 // Test case for interrupt method
 @Test(expected = UnExpectedException.class)
 public void testInterrupt() {
 partitionPlanJob.interrupt();
 }
}