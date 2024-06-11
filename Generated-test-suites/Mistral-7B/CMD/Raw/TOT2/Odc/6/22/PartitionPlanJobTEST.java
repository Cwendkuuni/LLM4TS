import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
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

@Slf4j
public class PartitionPlanJobTest {

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 private PartitionPlanService partitionPlanServiceMock;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private ScheduleService scheduleServiceMock;
 private ScheduleEntity scheduleEntityMock;
 private JobExecutionContext jobExecutionContextMock;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);

 partitionPlanServiceMock = mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 scheduleEntityMock = mock(ScheduleEntity.class);
 jobExecutionContextMock = mock(JobExecutionContext.class);

 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
 }

 @Test
 public void testExecute_ValidSchedule() throws Exception {
 // Given
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 List<TablePartitionPlanEntity> tablePartitionPlans = new java.util.ArrayList<>();
 TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
 tablePartitionPlans.add(tablePartitionPlan);

 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
 when(jobExecutionContextMock.getMergedJobDataMap().get("scheduleId")).thenReturn(1L);

 // When
 partitionPlanJob.execute(jobExecutionContextMock);

 // Then
 verify(partitionPlanServiceMock, times(1)).getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId());
 verify(partitionPlanServiceMock, times(1)).getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId());
 verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
 }

 @Test(expected = SchedulerException.class)
 public void testExecute_ScheduleNotFound() throws Exception {
 // Given
 doThrow(new SchedulerException("Schedule not found")).when(scheduleServiceMock).nullSafeGetById(anyLong());

 // When
 partitionPlanJob.execute(jobExecutionContextMock);

 // Then
 verify(scheduleServiceMock, times(1)).nullSafeGetById(anyLong());
 verifyNoInteractions(partitionPlanServiceMock, partitionPlanTaskServiceMock);
 }

 @Test
 public void testExecute_DatabasePartitionPlanDisabled() throws Exception {
 // Given
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(false);

 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntityMock);
 when(scheduleEntityMock.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(new PartitionPlanJobParameters()));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(new java.util.ArrayList<>());

 // When
 partitionPlanJob.execute(jobExecutionContextMock);

 // Then
 verify(scheduleServiceMock, times(1)).nullSafeGetById(anyLong());
 verify(partitionPlanServiceMock, times(1)).getDatabasePartitionPlanById(anyLong());
 verifyZeroInteractions(partitionPlanTaskServiceMock);
 verify(scheduleServiceMock, times(1)).terminate(scheduleEntityMock);
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_PartitionPlanTaskFailed() throws Exception {
 // Given
 doThrow(new JobExecutionException("Partition plan task failed")).when(partitionPlanTaskServiceMock).executePartitionPlan(anyLong(), anyList());

 // When
 partitionPlanJob.execute(jobExecutionContextMock);

 // Then
 verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(anyLong(), anyList());
 verify(scheduleServiceMock, times(1)).terminate(scheduleEntityMock);
 }

 @Test
 public void testInterrupt() throws Exception {
 // Given
 doThrow(new UnExpectedException()).when(partitionPlanJob).interrupt();

 // When, Then
 verify(partitionPlanJob, times(1)).interrupt();
 }
}