import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.MockitoJobExecutionMock;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import com.google.gson.Gson;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanService partitionPlanServiceMock;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private ScheduleService scheduleServiceMock;

 @Before
 public void setUp() {
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
 partitionPlanJob = new PartitionPlanJob();
 }

 @Test
 public void testExecuteJob() throws Exception {
 // given
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 ScheduleEntity scheduleEntity1 = new ScheduleEntity();
 scheduleEntity1.setId(2L);
 ScheduleEntity scheduleEntity3 = new ScheduleEntity();
 ScheduleEntity scheduleEntity4 = new ScheduleEntity();
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(
 new TablePartitionPlanEntity(), new TablePartitionPlanEntity());
 when(scheduleServiceMock.nullSafeGetById(eq(1L))).thenReturn(scheduleEntity);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(eq(1L))).thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(eq(1L))).thenReturn(tablePartitionPlans);
 MockitoJobExecutionMock context = new MockitoJobExecutionMock();
 context.setMergedJobDataMap(new MockitoJobExecutionMock().getMergedJobDataMap());
 JobExecutionContext jobContext = context.mock();
 jobContext.setJobDetail(partitionPlanJob);
 context.setJobExecutionContext(jobContext);
 partitionPlanJob.execute(jobContext);
 // when
 verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(eq(1L), eq(tablePartitionPlans));
 }

 @Test(expected = SchedulerException.class)
 public void testExecuteJob_ScheduleNotFound() throws Exception {
 // given
 when(scheduleServiceMock.nullSafeGetById(eq(1L))).thenThrow(new SchedulerException());
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 ScheduleEntity scheduleEntity1 = new ScheduleEntity();
 scheduleEntity1.setId(2L);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(
 new TablePartitionPlanEntity(), new TablePartitionPlanEntity());
 MockitoJobExecutionMock context = new MockitoJobExecutionMock();
 context.setMergedJobDataMap(new MockitoJobExecutionMock().getMergedJobDataMap());
 JobExecutionContext jobContext = context.mock();
 jobContext.setJobDetail(partitionPlanJob);
 context.setJobExecutionContext(jobContext);
 // when, then
 partitionPlanJob.execute(jobContext);
 // then
 verify(scheduleServiceMock, times(1)).terminate(scheduleEntity);
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() throws Exception {
 // given
 partitionPlanJob.interrupt();
 // then
 verify(partitionPlanJob, times(1)).interrupt();
 }
}