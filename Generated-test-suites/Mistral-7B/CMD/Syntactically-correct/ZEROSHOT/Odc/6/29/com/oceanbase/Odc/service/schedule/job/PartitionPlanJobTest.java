import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")

public class PartitionPlanJobTest {

 @Mock
 private PartitionPlanService partitionPlanService;

 @Mock
 private PartitionPlanTaskService partitionPlanTaskService;

 @Mock
 private ScheduleService scheduleService;

 @Mock
 private SpringContextUtil springContextUtil;

 @Mock
 private ScheduleTaskUtils scheduleTaskUtils;

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 @Before
 public void initMocks() {
 MockitoAnnotations.initMocks(this);
 given(springContextUtil.getBean(PartitionPlanService.class)).willReturn(partitionPlanService);
 given(springContextUtil.getBean(PartitionPlanTaskService.class)).willReturn(partitionPlanTaskService);
 given(springContextUtil.getBean(ScheduleService.class)).willReturn(scheduleService);
 given(springContextUtil.getBean(ScheduleTaskUtils.class)).willReturn(scheduleTaskUtils);
 }

 @Test
 public void execute_shouldExecutePartitionPlanWhenScheduleIsValid() throws Exception {
 // given
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 List<TablePartitionPlanEntity> tablePartitionPlans = java.util.Collections.emptyList();
 given(scheduleService.nullSafeGetById(ArgumentMatchers.anyLong())).willReturn(scheduleEntity);
 given(scheduleEntity.getJobParametersJson()).willReturn(JsonUtils.toJsonString(jobParameters));
 given(partitionPlanService.getDatabasePartitionPlanById(ArgumentMatchers.anyLong())).willReturn(databasePartitionPlan);
 given(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(ArgumentMatchers.anyLong())).willReturn(tablePartitionPlans);
 given(databasePartitionPlan.isConfigEnabled()).willReturn(true);
 given(partitionPlanService.getFlowInstanceId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong())).willReturn("flowInstanceId");

 // when
 partitionPlanJob.execute(new JobExecutionContext());

 // then
 verify(partitionPlanTaskService).executePartitionPlan(ArgumentMatchers.eq("flowInstanceId"), ArgumentMatchers.eq(tablePartitionPlans));
 }

 @Test(expected = UnExpectedException.class)
 public void execute_shouldNotExecutePartitionPlanWhenScheduleIsInvalid() throws Exception {
 // given
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 List<TablePartitionPlanEntity> tablePartitionPlans = java.util.Collections.emptyList();
 given(scheduleService.nullSafeGetById(ArgumentMatchers.anyLong())).willReturn(scheduleEntity);
 given(scheduleEntity.getJobParametersJson()).willReturn(JsonUtils.toJsonString(jobParameters));
 given(partitionPlanService.getDatabasePartitionPlanById(ArgumentMatchers.anyLong())).willReturn(databasePartitionPlan);
 given(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(ArgumentMatchers.anyLong())).willReturn(tablePartitionPlans);
 given(databasePartitionPlan.isConfigEnabled()).willReturn(false);
 given(scheduleService.terminate(ArgumentMatchers.any(ScheduleEntity.class))).willThrow(new SchedulerException());

 // when
 partitionPlanJob.execute(new JobExecutionContext());

 // then
 // The partition plan job should not execute the partition plan and should terminate the schedule.
 verify(scheduleService).terminate(ArgumentMatchers.any(ScheduleEntity.class));
 }

 @Test(expected = UnExpectedException.class)
 public void interrupt_shouldThrowUnExpectedException() throws Exception {
 // when
 partitionPlanJob.interrupt();

 // then
 // The partition plan job should throw an UnExpectedException when interrupted.
 }
}