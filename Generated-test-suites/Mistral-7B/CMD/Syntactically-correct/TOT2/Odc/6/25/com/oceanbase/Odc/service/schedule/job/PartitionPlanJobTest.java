import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verify;

@Slf4j
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;

 @Mock
 private PartitionPlanTaskService partitionPlanTaskService;

 @Mock
 private PartitionPlanService partitionPlanService;

 @Mock
 private ScheduleService scheduleService;

 @InjectMocks
 private PartitionPlanJob testee;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 partitionPlanJob = new PartitionPlanJob();
 testee = new PartitionPlanJob();
 testee.partitionPlanTaskService = partitionPlanTaskService;
 testee.partitionPlanService = partitionPlanService;
 testee.scheduleService = scheduleService;
 }

 @Test
 public void testExecute_whenScheduleEntityExists_shouldExecutePartitionPlan() {
 // Given
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 List<TablePartitionPlanEntity> tablePartitionPlans = Mockito.mock(List.class);

 given(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 given(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 given(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 given(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);
 given(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

 // When
 testee.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 verify(partitionPlanTaskService).executePartitionPlan(eq(databasePartitionPlan.getFlowInstanceId()), eq(tablePartitionPlans));
 }

 @Test
 public void testExecute_whenScheduleEntityNotExists_shouldDoNothing() {
 // Given
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 given(scheduleService.nullSafeGetById(anyLong())).thenReturn(null);

 // When
 testee.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 verify(log, times(1)).warn("Schedule not found,scheduleId={}", anyLong());
 }

 @Test
 public void testInterrupt_shouldThrowUnexpectedException() {
 // Given

 // When & Then
 Mockito.verify(testee).interrupt();
 then(testee).should().throwException(new UnExpectedException());
 }
}