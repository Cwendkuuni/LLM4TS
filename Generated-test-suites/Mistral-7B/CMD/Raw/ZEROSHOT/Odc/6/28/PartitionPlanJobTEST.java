import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.google.gson.Gson;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanService partitionPlanServiceMock;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private ScheduleService scheduleServiceMock;
 private ScheduleEntity scheduleEntityMock;

 @Before
 public void setUp() {
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 scheduleEntityMock = mock(ScheduleEntity.class);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
 }

 @Test
 public void testExecuteJobWithValidInput() throws Exception {
 // given
 Long scheduleId = 1L;
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = List.of(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(databasePartitionPlan.getId());
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 scheduleEntity.setJobParametersJson(new Gson().toJson(jobParameters));
 when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);

 // when
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 assertNotNull(scheduleEntity);
 assertNotNull(databasePartitionPlan);
 assertNotNull(tablePartitionPlans);
 assertEquals(StringUtils.EMPTY, StringUtils.trim(scheduleEntity.getJobStatus()));
 }

 @Test
 public void testExecuteJobWithInvalidInput() throws Exception {
 // given
 Long scheduleId = 1L;
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(false);
 List<TablePartitionPlanEntity> tablePartitionPlans = List.of();
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(databasePartitionPlan.getId());
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 scheduleEntity.setJobParametersJson(new Gson().toJson(jobParameters));
 when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenThrow(new Exception("failed to get valid table"));

 // when
 partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 assertNull(scheduleEntity);
 assertNull(databasePartitionPlan);
 assertNull(tablePartitionPlans);
 }

 @Test
 public void testInterrupt() throws Exception {
 // when
 partitionPlanJob.interrupt();

 // then
 // do nothing
 }

 @Test(expected = UnExpectedException.class)
 public void testInterruptWithException() throws Exception {
 // when
 partitionPlanJob.interrupt();

 // then
 // throw exception
 MethodUtils.invokeMethod(partitionPlanJob, "interrupt");
 }
}