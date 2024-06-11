import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.TestJobBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

@RunWith(Quartz.class)
@Slf4j
public class PartitionPlanJobTest {

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 private PartitionPlanService partitionPlanServiceMock;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private ScheduleService scheduleServiceMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
 }

 @Test
 public void testExecute() throws SchedulerException, Exception {
 // Given
 String scheduleIdStr = "1";
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(Long.parseLong(scheduleIdStr));
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 List<TablePartitionPlanEntity> tablePartitionPlans = new java.util.ArrayList<>();

 when(scheduleServiceMock.nullSafeGetById(Long.parseLong(scheduleIdStr)))
 .thenReturn(scheduleEntity);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);

 JobExecutionContext context = new TestJobBuilder()
 .newJob(PartitionPlanJob.class)
 .withIdentity("PartitionPlanJob", "PartitionPlanGroup")
 .usingJobData("scheduleId", scheduleIdStr)
 .build();

 // When
 partitionPlanJob.execute(context);

 // Then
 verify(partitionPlanServiceMock, times(1)).getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId());
 verify(partitionPlanServiceMock, times(1)).getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId());
 verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);

 assertEquals("database partition plan is enable and there are tables need to process,start to execute partition-plan job,scheduleId=" + scheduleIdStr, log.getInfo());
 }

 @Test
 public void testExecute_DatabasePartitionPlanIsDisabled() throws SchedulerException, Exception {
 // Given
 String scheduleIdStr = "1";
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(Long.parseLong(scheduleIdStr));
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(false);
 List<TablePartitionPlanEntity> tablePartitionPlans = new java.util.ArrayList<>();

 when(scheduleServiceMock.nullSafeGetById(Long.parseLong(scheduleIdStr)))
 .thenReturn(scheduleEntity);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);

 JobExecutionContext context = new TestJobBuilder()
 .newJob(PartitionPlanJob.class)
 .withIdentity("PartitionPlanJob", "PartitionPlanGroup")
 .usingJobData("scheduleId", scheduleIdStr)
 .build();

 // When
 partitionPlanJob.execute(context);

 // Then
 verify(partitionPlanServiceMock, times(1)).getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId());
 verify(partitionPlanServiceMock, times(1)).getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId());
 verifyZeroInteractions(partitionPlanTaskServiceMock);
 verify(scheduleServiceMock, times(1)).terminate(scheduleEntity);

 assertEquals("database partition plan is disable or no tables need to process,start to stop partition-plan job,scheduleId=" + scheduleIdStr, log.getInfo());
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() {
 partitionPlanJob.interrupt();
 }
}