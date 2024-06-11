import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.mock.MockScheduler;
import org.quartz.impl.jobstore.JobKey;

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
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
@Slf4j
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanService partitionPlanServiceMock;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private ScheduleService scheduleServiceMock;
 private SchemaScheduler schedulerMock;

 @Before
 public void setUp() {
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 schedulerMock = new MockScheduler();

 partitionPlanJob = new PartitionPlanJob();
 partitionPlanJob.setPartitionPlanService(partitionPlanServiceMock);
 partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskServiceMock);
 partitionPlanJob.setScheduleService(scheduleServiceMock);
 }

 @Test
 public void testExecute_ValidScheduleEntityAndPartitionPlan() {
 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 List<TablePartitionPlanEntity> tablePartitionPlans = Mockito.mock(List.class);

 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
 when(tablePartitionPlans.isEmpty()).thenReturn(false);

 partitionPlanJob.execute(context);

 verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(anyLong(), anyList());
 verifyNoMoreInteractions(scheduleServiceMock);
 }

 @Test
 public void testExecute_InvalidScheduleEntity() {
 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);

 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenThrow(new Exception());

 partitionPlanJob.execute(context);

 verifyNoInteractions(partitionPlanTaskServiceMock);
 verify(scheduleServiceMock, times(1)).terminate(scheduleEntity);
 }

 @Test
 public void testExecute_DisabledDatabasePartitionPlan() {
 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 List<TablePartitionPlanEntity> tablePartitionPlans = Mockito.mock(List.class);

 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

 partitionPlanJob.execute(context);

 verifyNoInteractions(partitionPlanTaskServiceMock);
 verifyNoInteractions(scheduleServiceMock);
 }

 @Test
 public void testExecute_EmptyTablePartitionPlans() {
 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 List<TablePartitionPlanEntity> tablePartitionPlans = Mockito.mock(List.class);

 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
 when(tablePartitionPlans.isEmpty()).thenReturn(true);

 partitionPlanJob.execute(context);

 verifyNoInteractions(partitionPlanTaskServiceMock);
 verifyNoInteractions(scheduleServiceMock);
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() {
 partitionPlanJob.interrupt();
 }
}