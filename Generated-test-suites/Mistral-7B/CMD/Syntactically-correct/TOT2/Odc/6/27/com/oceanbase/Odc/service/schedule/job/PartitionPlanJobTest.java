import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.model.common.exception.OdcException;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

 @Mock
 private PartitionPlanTaskService partitionPlanTaskService;

 @Mock
 private PartitionPlanService partitionPlanService;

 @Mock
 private ScheduleService scheduleService;

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 @Before
 public void setup() {
 SpringContextUtil.setBean(PartitionPlanTaskService.class, this.partitionPlanTaskService);
 SpringContextUtil.setBean(PartitionPlanService.class, this.partitionPlanService);
 SpringContextUtil.setBean(ScheduleService.class, this.scheduleService);
 }

 @Test
 public void testExecuteJob_whenScheduleEntityIsNotNull_thenExecutePartitionPlan() {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 List<TablePartitionPlanEntity> tablePartitionPlans = List.of();

 Mockito.when(scheduleService.nullSafeGetById(Mockito.anyLong()))
 .thenReturn(scheduleEntity);
 Mockito.when(scheduleEntity.getJobParametersJson())
 .thenReturn(JsonUtils.toJsonString(jobParameters));
 Mockito.when(partitionPlanService.getDatabasePartitionPlanById(Mockito.anyLong()))
 .thenReturn(databasePartitionPlan);
 Mockito.when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(Mockito.anyLong()))
 .thenReturn(tablePartitionPlans);

 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 ArgumentCaptor<List<TablePartitionPlanEntity>> tablePartitionPlansArgumentCaptor = ArgumentCaptor.forClass(List.class);
 ArgumentCaptor<Long> databasePartitionPlanIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
 Mockito.verify(partitionPlanTaskService).executePartitionPlan(databasePartitionPlanIdArgumentCaptor.capture(), tablePartitionPlansArgumentCaptor.capture());

 Long databasePartitionPlanId = databasePartitionPlanIdArgumentCaptor.getValue();
 List<TablePartitionPlanEntity> tablePartitionPlansCaptured = tablePartitionPlansArgumentCaptor.getValue();

 assertEquals(jobParameters.getDatabasePartitionPlanId(), databasePartitionPlanId);
 assertEquals(tablePartitionPlans, tablePartitionPlansCaptured);
 }

 @Test
 public void testExecuteJob_whenScheduleEntityIsNull_thenReturn() {
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));
 // No action since the method returns when ScheduleEntity is null
 }

 @Test
 public void testExecuteJob_whenDatabasePartitionPlanIsDisable_thenStopPartitionPlanJob() {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(false);
 List<TablePartitionPlanEntity> tablePartitionPlans = List.of();

 Mockito.when(scheduleService.nullSafeGetById(Mockito.anyLong()))
 .thenReturn(scheduleEntity);
 Mockito.when(scheduleEntity.getJobParametersJson())
 .thenReturn(JsonUtils.toJsonString(jobParameters));
 Mockito.when(partitionPlanService.getDatabasePartitionPlanById(Mockito.anyLong()))
 .thenReturn(databasePartitionPlan);
 Mockito.when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(Mockito.anyLong()))
 .thenReturn(tablePartitionPlans);

 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 Mockito.verify(scheduleService).terminate(scheduleEntity);
 }

 @Test
 public void testExecuteJob_whenTablePartitionPlansIsEmpty_thenStopPartitionPlanJob() {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 List<TablePartitionPlanEntity> tablePartitionPlans = List.of();

 Mockito.when(scheduleService.nullSafeGetById(Mockito.anyLong()))
 .thenReturn(scheduleEntity);
 Mockito.when(scheduleEntity.getJobParametersJson())
 .thenReturn(JsonUtils.toJsonString(jobParameters));
 Mockito.when(partitionPlanService.getDatabasePartitionPlanById(Mockito.anyLong()))
 .thenReturn(databasePartitionPlan);
 Mockito.when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(Mockito.anyLong()))
 .thenReturn(tablePartitionPlans);

 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 Mockito.verify(scheduleService).terminate(scheduleEntity);
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt_whenCalled_thenThrowUnExpectedException() {
 partitionPlanJob.interrupt();
 }
}