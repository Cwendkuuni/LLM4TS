package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
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
import com.oceanbase.odc.service.schedule.Model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;
 private PartitionPlanService partitionPlanServiceMock;
 private PartitionPlanTaskService partitionPlanTaskServiceMock;
 private ScheduleService scheduleServiceMock;
 private ScheduleEntity scheduleEntityMock;
 private JobExecutionContext contextMock;

 @Before
 public void setUp() {
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 contextMock = mock(JobExecutionContext.class);
 }

 @Test
 public void testExecute_whenScheduleIsNotNullAndDatabasePartitionPlanIsNotNullAndTablePartitionPlansIsNotNull_thenExecutePartitionPlan() {
 // Given
 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntityMock);
 when(scheduleEntityMock.getJobParametersJson()).thenReturn("{}");
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(new DatabasePartitionPlanEntity());
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(new ArrayList<>());
 when(partitionPlanServiceMock.isConfigEnabled(anyLong())).thenReturn(true);

 // When
 partitionPlanJob.execute(contextMock);

 // Then
 // Add assertions here to check the execution of executePartitionPlan method
 }

 @Test
 public void testExecute_whenScheduleIsNull_thenDoNotExecutePartitionPlan() {
 // Given
 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenThrow(new Exception());

 // When
 partitionPlanJob.execute(contextMock);

 // Then
 // Add assertions here to check that executePartitionPlan method was not called
 }

 @Test
 public void testExecute_whenDatabasePartitionPlanIsNull_thenDoNotExecutePartitionPlan() {
 // Given
 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntityMock);
 when(scheduleEntityMock.getJobParametersJson()).thenReturn("{}");
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(null);

 // When
 partitionPlanJob.execute(contextMock);

 // Then
 // Add assertions here to check that executePartitionPlan method was not called
 }

 @Test(expected = UnExpectedException.class)
 public void testExecute_whenTablePartitionPlansIsEmpty_thenThrowUnExpectedException() {
 // Given
 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntityMock);
 when(scheduleEntityMock.getJobParametersJson()).thenReturn("{}");
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(new DatabasePartitionPlanEntity());
 when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(new ArrayList<>());

 // When and Then
 partitionPlanJob.execute(contextMock);
 }

 @Test
 public void testExecute_whenDatabasePartitionPlanIsDisabled_thenDoNotExecutePartitionPlan() {
 // Given
 when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntityMock);
 when(scheduleEntityMock.getJobParametersJson()).thenReturn("{}");
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(new DatabasePartitionPlanEntity());
 when(partitionPlanServiceMock.isConfigEnabled(anyLong())).thenReturn(false);

 // When
 partitionPlanJob.execute(contextMock);

 // Then
 // Add assertions here to check that executePartitionPlan method was not called
 }

 @Test
 public void testInterrupt() {
 // Given

 // When
 partitionPlanJob.interrupt();

 // Then
 // Add assertions here to check that interrupt method does not throw an exception
 }
}