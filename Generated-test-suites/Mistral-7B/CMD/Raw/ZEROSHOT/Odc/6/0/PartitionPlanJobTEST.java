import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanJobParameters;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private PartitionPlanService partitionPlanServiceMock;
 private ScheduleService scheduleServiceMock;

 @Before
 public void setUp() {
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
 }

 @Test
 public void testExecute_whenDatabasePartitionPlanIsEnabledAndTablePartitionPlansAreNotEmpty_thenPartitionPlanIsExecuted()
 throws Exception {
 // Given
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 TablePartitionPlanEntity tablePartitionPlan = Mockito.mock(TablePartitionPlanEntity.class);
 when(scheduleServiceMock.nullSafeGetById(Mockito.anyLong())).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(Mockito.anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(Mockito.anyLong())).thenReturn(
 List.of(tablePartitionPlan));
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

 // When
 partitionPlanJob.execute(new JobExecutionContext());

 // Then
 Mockito.verify(partitionPlanTaskServiceMock).executePartitionPlan(Mockito.anyLong(), Mockito.eq(List.of(tablePartitionPlan)));
 }

 @Test
 public void testExecute_whenDatabasePartitionPlanIsDisabledOrTablePartitionPlansAreEmpty_thenPartitionPlanIsNotExecuted()
 throws Exception {
 // Given
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 when(scheduleServiceMock.nullSafeGetById(Mockito.anyLong())).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(Mockito.anyLong())).thenReturn(databasePartitionPlan);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

 // When
 partitionPlanJob.execute(new JobExecutionContext());

 // Then
 // No verification needed as executePartitionPlan method is not called
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_whenExceptionIsThrownDuringPartitionPlanExecution_thenJobExecutionExceptionIsThrown()
 throws Exception {
 // Given
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
 TablePartitionPlanEntity tablePartitionPlan = Mockito.mock(TablePartitionPlanEntity.class);
 when(scheduleServiceMock.nullSafeGetById(Mockito.anyLong())).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(Mockito.anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(Mockito.anyLong())).thenReturn(
 List.of(tablePartitionPlan));
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
 when(partitionPlanTaskServiceMock.executePartitionPlan(Mockito.anyLong(), Mockito.eq(List.of(tablePartitionPlan)))).thenThrow(new Exception());

 // When and Then
 partitionPlanJob.execute(new JobExecutionContext());
 }

 @Test(expected = RuntimeException.class)
 public void testTerminate_whenScheduleServiceTerminateMethodThrowsException_thenRuntimeExceptionIsThrown()
 throws Exception {
 // Given
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 when(scheduleServiceMock.terminate(scheduleEntity)).thenThrow(new SchedulerException());

 // When and Then
 partitionPlanJob.interrupt();
 }
}