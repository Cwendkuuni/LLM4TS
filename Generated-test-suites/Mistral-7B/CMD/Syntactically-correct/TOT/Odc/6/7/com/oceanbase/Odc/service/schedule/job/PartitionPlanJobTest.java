import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

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

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanService partitionPlanServiceMock;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private ScheduleService scheduleServiceMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);

 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);

 partitionPlanJob.partitionPlanTaskService = partitionPlanTaskServiceMock;
 partitionPlanJob.partitionPlanService = partitionPlanServiceMock;
 partitionPlanJob.scheduleService = scheduleServiceMock;
 }

 @Test
 public void testExecute_ValidScheduleEntityAndDatabasePartitionPlan_ShouldExecutePartitionPlan() {
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
 DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
 TablePartitionPlanEntity tablePartitionPlan = mock(TablePartitionPlanEntity.class);

 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn("{\"databasePartitionPlanId\":1}");
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(asList(tablePartitionPlan));
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

 partitionPlanJob.execute(mock(JobExecutionContext.class));

 verify(partitionPlanTaskServiceMock).executePartitionPlan(anyLong(), isA(List.class));
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_ScheduleNotFound_ShouldLogWarningAndTerminateSchedule() {
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);

 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenThrow(new Exception());

 partitionPlanJob.execute(mock(JobExecutionContext.class));

 verify(scheduleServiceMock).terminate(scheduleEntity);
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_DatabasePartitionPlanDisabledOrNoValidTablePartitionPlan_ShouldLogInfoAndStopJob() {
 DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);

 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(mock(ScheduleEntity.class));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

 partitionPlanJob.execute(mock(JobExecutionContext.class));

 verify(scheduleServiceMock).terminate(any(ScheduleEntity.class));
 }
}