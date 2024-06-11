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
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@Slf4j
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanService partitionPlanServiceMock;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private ScheduleService scheduleServiceMock;

 @InjectMocks
 private PartitionPlanJob testee;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanJob.partitionPlanService = partitionPlanServiceMock;
 partitionPlanJob.partitionPlanTaskService = partitionPlanTaskServiceMock;
 partitionPlanJob.scheduleService = scheduleServiceMock;
 }

 @Test
 public void testExecute_ValidSchedule_PartitionPlanSuccess() {
 // given
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 JsonUtils.setJsonString(scheduleEntity, "jobParametersJson", JsonUtils.toJsonString(jobParameters));

 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = List.of(new TablePartitionPlanEntity());

 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);

 when(scheduleServiceMock.nullSafeGetById(scheduleId))
 .thenReturn(scheduleEntity);

 // when
 testee.execute(mock(JobExecutionContext.class));

 // then
 verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(eq(databasePartitionPlan.getFlowInstanceId()),
 eq(tablePartitionPlans));
 }

 @Test(expected = SchedulerException.class)
 public void testExecute_ScheduleNotFound_TerminateSchedule() {
 // given
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);

 when(scheduleServiceMock.nullSafeGetById(scheduleId))
 .thenThrow(new SchedulerException());

 // when and then
 testee.execute(mock(JobExecutionContext.class));
 verify(scheduleServiceMock, times(1)).terminate(scheduleEntity);
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() {
 // when
 testee.interrupt();

 // then
 verify(testee, times(1)).interrupt();
 }
}