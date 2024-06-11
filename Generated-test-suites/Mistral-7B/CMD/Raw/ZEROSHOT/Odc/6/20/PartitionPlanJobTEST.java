import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

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

import static org.mockito.Mockito.*;

@Slf4j
public class PartitionPlanJobTest {

 private static final String SCHEDULE_ID = "1";
 private static final String JOB_PARAMETERS_JSON = "{}";

 @Rule
 public final StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 private PartitionPlanService partitionPlanService;
 private PartitionPlanTaskService partitionPlanTaskService;
 private ScheduleService scheduleService;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 partitionPlanService = mock(PartitionPlanService.class);
 partitionPlanTaskService = mock(PartitionPlanTaskService.class);
 scheduleService = mock(ScheduleService.class);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanService);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskService);
 SpringContextUtil.setBean(ScheduleService.class, scheduleService);
 doReturn(new ScheduleEntity()).when(scheduleService).nullSafeGetById(SCHEDULE_ID);
 doReturn(new DatabasePartitionPlanEntity()).when(partitionPlanService).getDatabasePartitionPlanById(anyLong());
 doReturn(asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity())).when(partitionPlanService)
 .getValidTablePlanByDatabasePartitionPlanId(anyLong());
 }

 @After
 public void tearDown() throws SchedulerException {
 schedulerFactory.shutdown();
 }

 @Test
 public void testExecute() {
 Long databasePartitionPlanId = 1L;
 PartitionPlanJobParameters jobParameters = new Gson().fromJson(JOB_PARAMETERS_JSON, PartitionPlanJobParameters.class);
 jobParameters.setDatabasePartitionPlanId(databasePartitionPlanId);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobParametersJson(JOB_PARAMETERS_JSON);
 scheduleEntity.setId(SCHEDULE_ID);
 when(scheduleService.nullSafeGetById(SCHEDULE_ID)).thenReturn(scheduleEntity);
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(databasePartitionPlanId);
 databasePartitionPlan.setConfigEnabled(true);
 databasePartitionPlan.setFlowInstanceId("flowInstanceId");
 when(partitionPlanService.getDatabasePartitionPlanById(databasePartitionPlanId)).thenReturn(databasePartitionPlan);
 List<TablePartitionPlanEntity> tablePartitionPlans = asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(databasePartitionPlanId)).thenReturn(tablePartitionPlans);

 partitionPlanJob.execute(mock(JobExecutionContext.class));

 verify(partitionPlanTaskService, times(1)).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
 }

 @Test
 public void testExecute_DatabasePartitionPlanDisabled() {
 Long databasePartitionPlanId = 1L;
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(databasePartitionPlanId);
 databasePartitionPlan.setConfigEnabled(false);
 databasePartitionPlan.setFlowInstanceId("flowInstanceId");
 when(partitionPlanService.getDatabasePartitionPlanById(databasePartitionPlanId)).thenReturn(databasePartitionPlan);

 partitionPlanJob.execute(mock(JobExecutionContext.class));

 verify(scheduleService, times(1)).terminate(any(ScheduleEntity.class));
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() {
 partitionPlanJob.interrupt();
 }
}