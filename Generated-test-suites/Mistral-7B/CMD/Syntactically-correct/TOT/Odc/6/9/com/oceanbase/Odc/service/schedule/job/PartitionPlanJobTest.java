import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
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
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import static org.mockito.Mockito.*;

@Slf4j
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private PartitionPlanService partitionPlanServiceMock;
 private ScheduleService scheduleServiceMock;

 @InjectMocks
 private PartitionPlanJob testee;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 testee = new PartitionPlanJob();
 testee.partitionPlanTaskService = partitionPlanTaskServiceMock;
 testee.partitionPlanService = partitionPlanServiceMock;
 testee.scheduleService = scheduleServiceMock;
 }

 @Test
 public void testExecute_ValidScheduleEntity_Success() throws Exception {
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
 List<TablePartitionPlanEntity> tablePartitionPlans = mock(List.class);
 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
 doNothing().when(scheduleServiceMock).terminate(scheduleEntity);
 doNothing().when(partitionPlanTaskServiceMock).executePartitionPlan(anyLong(), anyList());

 testee.execute(mock(JobExecutionContext.class));

 verify(partitionPlanTaskServiceMock).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
 }

 @Test
 public void testExecute_ScheduleEntityNull_Ignored() throws Exception {
 doThrow(new Exception()).when(scheduleServiceMock).nullSafeGetById(anyLong());

 testee.execute(mock(JobExecutionContext.class));

 verify(scheduleServiceMock, never()).terminate(any());
 }

 @Test(expected = UnExpectedException.class)
 public void testExecute_Interrupted_ExceptionThrown() throws Exception {
 doThrow(new InterruptedException()).when(testee).interrupt();

 testee.execute(mock(JobExecutionContext.class));
 }

 @Test
 public void testExecute_DatabasePartitionPlanDisabled_Ignored() throws Exception {
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(false);

 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);

 testee.execute(mock(JobExecutionContext.class));

 verify(scheduleServiceMock, never()).terminate(any());
 }

 @Test
 public void testExecute_EmptyTablePartitionPlans_Ignored() throws Exception {
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

 testee.execute(mock(JobExecutionContext.class));

 verify(scheduleServiceMock, never()).terminate(any());
 }

 @Test(expected = SchedulerException.class)
 public void testInterrupt_Interrupted_SchedulerExceptionThrown() throws Exception {
 doThrow(new SchedulerException()).when(scheduleServiceMock).terminate(any());

 testee.interrupt();
 }
}