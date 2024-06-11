import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.MockitoScheduler;
import org.quartz.SchedulerException;

import com.google.gson.Gson;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleEntity;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import lombok.extern.slf4j.Slf4j;
import static org.mockito.Mockito.*;

@Slf4j
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private MockitoScheduler scheduler;
 private JobExecutionContext context;
 private PartitionPlanService partitionPlanServiceMock;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private ScheduleService scheduleServiceMock;

 @Before
 public void setUp() {
 partitionPlanJob = new PartitionPlanJob();
 scheduler = new MockitoScheduler();
 context = mock(JobExecutionContext.class);
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
 }

 @Test
 public void testExecute_ValidScheduleAndPartitionPlan() throws SchedulerException, Exception {
 // Given
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setFlowInstanceId("123456");
 databasePartitionPlan.setConfigEnabled(true);
 TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
 List<TablePartitionPlanEntity> tablePartitionPlans = List.of(tablePartitionPlan);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobParametersJson(new Gson().toJson(new PartitionPlanJobParameters()));
 scheduleEntity.setScheduleId(1L);
 when(scheduleServiceMock.getById(1L)).thenReturn(scheduleEntity);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

 // When
 partitionPlanJob.execute(context);

 // Then
 verify(partitionPlanTaskServiceMock).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
 tablePartitionPlans);
 }

 @Test(expected = UnExpectedException.class)
 public void testExecute_DisabledDatabasePartitionPlan() throws SchedulerException, Exception {
 // Given
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setFlowInstanceId("123456");
 databasePartitionPlan.setConfigEnabled(false);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobParametersJson(new Gson().toJson(new PartitionPlanJobParameters()));
 scheduleEntity.setScheduleId(1L);
 when(scheduleServiceMock.getById(1L)).thenReturn(scheduleEntity);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(Collections.emptyList());

 // When
 partitionPlanJob.execute(context);

 // Then
 verifyNoInteractions(partitionPlanTaskServiceMock);
 }

 @Test
 public void testInterrupt() {
 // Given

 // When
 partitionPlanJob.interrupt();
 // Then
 // Nothing to verify as this method should not be called in this test
 }
}