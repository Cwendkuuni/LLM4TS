package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

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

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

 private static final String SCHEDULE_ID = UUID.randomUUID().toString();
 private static final String SCHEDULE_NAME = "testPartitionPlanJob";

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 private PartitionPlanService partitionPlanService;
 private PartitionPlanTaskService partitionPlanTaskService;
 private ScheduleService scheduleService;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 partitionPlanService = SpringContextUtil.getBean(PartitionPlanService.class);
 partitionPlanTaskService = SpringContextUtil.getBean(PartitionPlanTaskService.class);
 scheduleService = SpringContextUtil.getBean(ScheduleService.class);
 }

 @Test
 public void testExecute() throws Exception {
 // given
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity());

 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(Long.parseLong(SCHEDULE_ID));
 scheduleEntity.setName(SCHEDULE_NAME);
 scheduleEntity.setJobParametersJson(new Gson().toJson(jobParameters));

 // when
 partitionPlanJob.execute(createMockJobContext(scheduleEntity));

 // then
 assertTrue(scheduleService.isTerminated(scheduleEntity));
 }

 @Test
 public void testExecuteWhenDatabasePartitionPlanIsDisabled() throws Exception {
 // given
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(false);
 List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity());

 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(Long.parseLong(SCHEDULE_ID));
 scheduleEntity.setName(SCHEDULE_NAME);
 scheduleEntity.setJobParametersJson(new Gson().toJson(jobParameters));

 // when
 partitionPlanJob.execute(createMockJobContext(scheduleEntity));

 // then
 assertTrue(scheduleService.isTerminated(scheduleEntity));
 }

 @Test
 public void testExecuteWhenNoTableToProcess() throws Exception {
 // given
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(1L);
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = Collections.emptyList();

 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(Long.parseLong(SCHEDULE_ID));
 scheduleEntity.setName(SCHEDULE_NAME);
 scheduleEntity.setJobParametersJson(new Gson().toJson(jobParameters));

 // when
 partitionPlanJob.execute(createMockJobContext(scheduleEntity));

 // then
 assertTrue(scheduleService.isTerminated(scheduleEntity));
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() throws Exception {
 // when
 partitionPlanJob.interrupt();

 // then
 fail("Interrupt should not be invoked");
 }

 private JobExecutionContext createMockJobContext(ScheduleEntity scheduleEntity) {
 return Mockito.mock(JobExecutionContext.class);
 }
}