import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private PartitionPlanService partitionPlanServiceMock;
 private ScheduleService scheduleServiceMock;

 @InjectMocks
 private PartitionPlanJob testee;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanJob.partitionPlanTaskService = partitionPlanTaskServiceMock;
 partitionPlanJob.partitionPlanService = partitionPlanServiceMock;
 partitionPlanJob.scheduleService = scheduleServiceMock;
 }

 @Test
 public void testExecuteWithValidInput() {
 // Given
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setFlowInstanceId(1L);
 List<TablePartitionPlanEntity> tablePartitionPlans = java.util.Collections.emptyList();
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));
 when(scheduleServiceMock.getById(1L)).thenReturn(scheduleEntity);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

 // When
 testee.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
 tablePartitionPlans);
 verifyNoMoreInteractions(partitionPlanServiceMock);
 verifyNoMoreInteractions(scheduleServiceMock);
 }

 // Add test cases for edge cases and error scenarios here

}