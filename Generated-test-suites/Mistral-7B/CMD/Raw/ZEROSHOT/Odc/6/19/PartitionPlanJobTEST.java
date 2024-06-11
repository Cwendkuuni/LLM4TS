import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

 private static final long SCHEDULE_ID = 1L;
 private static final long DATABASE_PARTITION_PLAN_ID = 1L;

 @Mock
 private PartitionPlanService partitionPlanService;

 @Mock
 private PartitionPlanTaskService partitionPlanTaskService;

 @Mock
 private ScheduleService scheduleService;

 @Mock
 private JobExecutionContext context;

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanService);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskService);
 SpringContextUtil.setBean(ScheduleService.class, scheduleService);
 }

 @Test
 public void testExecute() throws SchedulerException, Exception {
 // Given
 DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
 doReturn(databasePartitionPlan).when(partitionPlanService).getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID);
 List<TablePartitionPlanEntity> tablePartitionPlans = mock(List.class);
 doReturn(tablePartitionPlans).when(partitionPlanService).getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID);
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
 when(context.getMergedJobDataMap().getString("scheduleId")).thenReturn(Long.toString(SCHEDULE_ID));
 when(scheduleService.nullSafeGetById(SCHEDULE_ID)).thenReturn(scheduleEntity);

 // When
 partitionPlanJob.execute(context);

 // Then
 // You can add assertions here to verify the behavior of the execute method.
 }

 @Test
 public void testInterrupt() {
 // You can add test cases for the interrupt method here.
 }

 @Test
 public void testExecuteWhenDatabasePartitionPlanIsDisabled() throws SchedulerException, Exception {
 // Given
 DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
 doReturn(databasePartitionPlan).when(partitionPlanService).getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID);
 List<TablePartitionPlanEntity> tablePartitionPlans = mock(List.class);
 doReturn(tablePartitionPlans).when(partitionPlanService).getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID);
 DatabasePartitionPlanEntity databasePartitionPlanWithDisabledConfig = new DatabasePartitionPlanEntity();
 databasePartitionPlanWithDisabledConfig.setId(DATABASE_PARTITION_PLAN_ID);
 databasePartitionPlanWithDisabledConfig.setConfigEnabled(false);
 doReturn(databasePartitionPlanWithDisabledConfig).when(partitionPlanService).getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID);

 // When
 partitionPlanJob.execute(context);

 // Then
 // The partition-plan job should be stopped since the database partition plan is disabled.
 // You can check the scheduleService.terminate(scheduleEntity) call here.
 }

 @Test
 public void testExecuteWhenNoTableToProcess() throws SchedulerException, Exception {
 // Given
 DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
 doReturn(databasePartitionPlan).when(partitionPlanService).getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID);
 List<TablePartitionPlanEntity> emptyTablePartitionPlans = mock(List.class);
 doReturn(emptyTablePartitionPlans).when(partitionPlanService).getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID);

 // When
 partitionPlanJob.execute(context);

 // Then
 // The partition-plan job should be stopped since there are no tables to process.
 // You can check the scheduleService.terminate(scheduleEntity) call here.
 }
}