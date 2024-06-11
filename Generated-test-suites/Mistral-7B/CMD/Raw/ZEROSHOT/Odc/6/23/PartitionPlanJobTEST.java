package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

 private static final String DATABASE_PARTITION_PLAN_ID = "1";
 private static final String FLOW_INSTANCE_ID = "1";
 private static final String SCHEDULE_ID = "1";
 private static final String SCHEDULE_ENTITY_JSON = "{}";
 private static final String JOB_PARAMETERS_JSON = "{}";

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
 public void setUp() {
 DoMock.mockStatic(SpringContextUtil.class);
 when(SpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
 when(SpringContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
 when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
 when(partitionPlanService.getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID)).thenReturn(new DatabasePartitionPlanEntity());
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID)).thenReturn( Arrays.asList(new TablePartitionPlanEntity()));
 when(scheduleService.nullSafeGetById(SCHEDULE_ID)).thenReturn(new ScheduleEntity());
 when(JsonUtils.fromJson(SCHEDULE_ENTITY_JSON, ScheduleEntity.class)).thenReturn(new ScheduleEntity());
 when(JsonUtils.fromJson(SCHEDULE_ENTITY_JSON, PartitionPlanJobParameters.class)).thenReturn(new PartitionPlanJobParameters());
 when(scheduleTaskUtils.getScheduleId(any())).thenReturn(SCHEDULE_ID);
 }

 @Test
 public void testExecute() throws Exception {
 // Arrange
 JobExecutionContext context = mock(JobExecutionContext.class);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobParametersJson(SCHEDULE_ENTITY_JSON);
 when(scheduleService.nullSafeGetById(SCHEDULE_ID)).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn(SCHEDULE_ENTITY_JSON);
 when(JsonUtils.fromJson(SCHEDULE_ENTITY_JSON, PartitionPlanJobParameters.class)).thenReturn(jobParameters);
 when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(new DatabasePartitionPlanEntity());
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
 .thenReturn(Arrays.asList(new TablePartitionPlanEntity()));

 // Act
 partitionPlanJob.execute(context);

 // Assert
 verify(scheduleService).terminate(scheduleEntity);
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() {
 // Act and Assert
 partitionPlanJob.interrupt();
 }
}