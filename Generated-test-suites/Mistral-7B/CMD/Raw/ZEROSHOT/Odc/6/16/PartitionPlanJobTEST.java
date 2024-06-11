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
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import lombok.extern.slf4j.Slf4j;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanService partitionPlanServiceMock;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private ScheduleService scheduleServiceMock;

 @InjectMocks
 private PartitionPlanJob testee;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);

 partitionPlanServiceMock = mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 testee = new PartitionPlanJob();
 testee.partitionPlanService = partitionPlanServiceMock;
 testee.partitionPlanTaskService = partitionPlanTaskServiceMock;
 testee.scheduleService = scheduleServiceMock;
 }

 @Test
 public void testExecute() throws Exception {
 // given
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 String json = JsonUtils.toJson(jobParameters);
 when(scheduleEntity.getJobParametersJson()).thenReturn(json);
 DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
 List<TablePartitionPlanEntity> tablePartitionPlans = mock(List.class);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

 // when
 JobExecutionContext context = mock(JobExecutionContext.class);
 testee.execute(context);

 // then
 // add your assertions here
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() throws JobExecutionException {
 // given

 // when & then
 testee.interrupt();
 }
}