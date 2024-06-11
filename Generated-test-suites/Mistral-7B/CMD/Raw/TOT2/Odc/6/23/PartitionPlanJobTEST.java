package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleService;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

@Slf4j
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private PartitionPlanService partitionPlanServiceMock;
 private ScheduleService scheduleServiceMock;

 @Before
 public void setup() {
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanTaskServiceMock = Mockito.mock(PartitionPlanTaskService.class);
 partitionPlanServiceMock = Mockito.mock(PartitionPlanService.class);
 scheduleServiceMock = Mockito.mock(ScheduleService.class);
 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
 SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
 }

 @Test
 public void testExecute_ValidScheduleEntity_PartitionPlanExecuted() throws Exception {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(UUID.randomUUID().toString());
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(UUID.randomUUID().toString());
 String jobParametersJson = JsonUtils.toJsonString(jobParameters);
 scheduleEntity.setJobParametersJson(jobParametersJson);
 ScheduleTaskUtils scheduleTaskUtils = new ScheduleTaskUtils();
 JobExecutionContext context = scheduleTaskUtils.createContext(scheduleEntity);

 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setId(UUID.randomUUID().toString());
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 tablePartitionPlans.add(new TablePartitionPlanEntity());

 when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(tablePartitionPlans);

 partitionPlanJob.execute(context);

 ArgumentCaptor<List<TablePartitionPlanEntity>> tablePartitionPlansCaptor = ArgumentCaptor.forClass(
 List.class);
 verify(partitionPlanTaskServiceMock).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
 tablePartitionPlansCaptor.capture());
 List<TablePartitionPlanEntity> actualTablePartitionPlans = tablePartitionPlansCaptor.getValue();
 assertEquals(tablePartitionPlans, actualTablePartitionPlans);
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_ScheduleNotFound_PartitionPlanJobTerminated() throws Exception {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(UUID.randomUUID().toString());
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 String jobParametersJson = JsonUtils.toJsonString(jobParameters);
 scheduleEntity.setJobParametersJson(jobParametersJson);
 ScheduleTaskUtils scheduleTaskUtils = new ScheduleTaskUtils();
 JobExecutionContext context = scheduleTaskUtils.createContext(scheduleEntity);

 when(scheduleServiceMock.nullSafeGetById(anyLong()))
 .thenThrow(new SchedulerException("Schedule not found"));

 partitionPlanJob.execute(context);

 verify(scheduleServiceMock).terminate(scheduleEntity);
 }
}