import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanService partitionPlanServiceMock;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private ScheduleService scheduleServiceMock;

 @Before
 public void setUp() {
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
 }

 @Test
 public void testExecute_ScheduleEntityIsNull() {
 ScheduleEntity scheduleEntity = null;
 partitionPlanJob.execute(null);
 }

 @Test
 public void testExecute_ScheduleEntityHasNoJobParameters() {
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = null;
 when(scheduleEntity.getJobParametersJson()).thenReturn(null);
 partitionPlanJob.execute(null);
 }

 @Test
 public void testExecute_InvalidJson_PartitionPlanJobParameters() {
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 String jsonString = "{\"databasePartitionPlanId\":\"1\", \"jobParameters\":{}";
 when(scheduleEntity.getJobParametersJson()).thenReturn(jsonString);
 partitionPlanJob.execute(null);
 }

 @Test
 public void testExecute_DatabasePartitionPlanIdNotFound() {
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 DatabasePartitionPlanEntity databasePartitionPlan = null;
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 partitionPlanJob.execute(null);
 }

 @Test
 public void testExecute_TablePartitionPlansEmpty() {
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(null);
 partitionPlanJob.execute(null);
 }

 @Test
 public void testExecute_PartitionPlanTaskServiceExecuteSuccess() {
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 List<TablePartitionPlanEntity> tablePartitionPlans = Mockito.mock(List.class);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
 when(partitionPlanTaskServiceMock.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
 tablePartitionPlans)).thenReturn(true);
 partitionPlanJob.execute(null);
 }

 @Test(expected = SchedulerException.class)
 public void testExecute_ScheduleServiceTerminateFailed() {
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(new PartitionPlanJobParameters()));
 partitionPlanJob.execute(null);
 when(scheduleServiceMock.terminate(scheduleEntity)).thenThrow(new SchedulerException());
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_UnexpectedException() {
 ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(new PartitionPlanJobParameters()));
 partitionPlanJob.execute(null);
 when(partitionPlanTaskServiceMock.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
 tablePartitionPlans)).thenThrow(new RuntimeException());
 }

 @Test
 public void testInterrupt_UnexpectedException() {
 partitionPlanJob.interrupt();
 }
}