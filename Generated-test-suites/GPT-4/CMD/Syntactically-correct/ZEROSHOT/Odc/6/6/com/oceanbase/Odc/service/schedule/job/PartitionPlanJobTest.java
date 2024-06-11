package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

public class PartitionPlanJobTest {
  private PartitionPlanJob partitionPlanJob;
  private PartitionPlanService partitionPlanServiceMock;
  private PartitionPlanTaskService partitionPlanTaskServiceMock;
  private ScheduleService scheduleServiceMock;
  private JobExecutionContext contextMock;

  @Before
  public void setup() {
    partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
    partitionPlanServiceMock = mock(PartitionPlanService.class);
    scheduleServiceMock = mock(ScheduleService.class);
    contextMock = mock(JobExecutionContext.class);
    partitionPlanJob = new PartitionPlanJob();
  }

  @Test
  public void testExecute() throws SchedulerException {
    ScheduleEntity entity = new ScheduleEntity();
    entity.setJobParametersJson("{}");

    when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(entity);
    when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(new DatabasePartitionPlanEntity());
    when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(new ArrayList<TablePartitionPlanEntity>());

    partitionPlanJob.execute(contextMock);
  }

  @Test
  public void testExecuteWithExecutingException() throws SchedulerException {
    ScheduleEntity entity = new ScheduleEntity();
    entity.setJobParametersJson("{}");

    when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(entity);
    when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(new DatabasePartitionPlanEntity());
    when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(new ArrayList<TablePartitionPlanEntity>());
    doThrow(new RuntimeException()).when(partitionPlanTaskServiceMock).executePartitionPlan(anyLong(), new ArrayList<TablePartitionPlanEntity>());

    partitionPlanJob.execute(contextMock);
  }

  @Test(expected = UnExpectedException.class)
  public void testInterrupt() {
    partitionPlanJob.interrupt();
  }
}
