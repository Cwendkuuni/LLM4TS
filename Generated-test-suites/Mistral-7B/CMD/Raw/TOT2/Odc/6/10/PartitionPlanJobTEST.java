import org.apache.commons.lang3.RandomUtils;import org.junit.Before;import org.junit.Test;import org.quartz.JobExecutionContext;import org.quartz.JobExecutionException;import static org.mockito.Mockito.*;import static org.junit.Assert.*;

import com.oceanbase.odc.common.json.JsonUtils;import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;import com.oceanbase.odc.metadb.schedule.ScheduleEntity;import com.oceanbase.odc.service.common.util.SpringContextUtil;import com.oceanbase.odc.service.partitionplan.PartitionPlanService;import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;import com.oceanbase.odc.service.schedule.ScheduleService;import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

@Slf4j
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanService partitionPlanServiceMock;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private ScheduleService scheduleServiceMock;

 @Before
 public void setup() {
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 partitionPlanJob = new PartitionPlanJob() {
 @Override
 public void execute(JobExecutionContext context) {
 super.execute(context);
 }

 @Override
 public void interrupt() {
 super.interrupt();
 }

 @Override
 public PartitionPlanService getPartitionPlanService() {
 return partitionPlanServiceMock;
 }

 @Override
 public PartitionPlanTaskService getPartitionPlanTaskService() {
 return partitionPlanTaskServiceMock;
 }

 @Override
 public ScheduleService getScheduleService() {
 return scheduleServiceMock;
 }
 };
 }

 @Test
 public void testExecuteNormal() {
 // given
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = List.of(new TablePartitionPlanEntity());

 when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);

 // when
 partitionPlanJob.execute(mock(JobExecutionContext.class));

 // then
 verify(partitionPlanTaskServiceMock).executePartitionPlan(eq(databasePartitionPlan.getFlowInstanceId()), eq(tablePartitionPlans));
 }

 @Test(expected = JobExecutionException.class)
 public void testExecuteException() {
 // given
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(false);
 List<TablePartitionPlanEntity> tablePartitionPlans = List.of(new TablePartitionPlanEntity());

 when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenThrow(new RuntimeException());

 // when & then
 partitionPlanJob.execute(mock(JobExecutionContext.class));
 }

 @Test
 public void testInterrupt() {
 // given
 // when & then
 assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
 }
}