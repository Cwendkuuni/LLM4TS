
package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import java.util.ArrayList;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private JobExecutionContext jobExecutionContextMock;
    private PartitionPlanService partitionPlanServiceMock;
    private ScheduleService scheduleServiceMock;
    private PartitionPlanTaskService partitionPlanTaskServiceMock;

    @Before
    public void init() {
        jobExecutionContextMock = Mockito.mock(JobExecutionContext.class);
        partitionPlanServiceMock = Mockito.mock(PartitionPlanService.class);
        scheduleServiceMock = Mockito.mock(ScheduleService.class);
        partitionPlanTaskServiceMock = Mockito.mock(PartitionPlanTaskService.class);

        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecuteAllTasks() {
        when(jobExecutionContextMock.getScheduleId()).thenReturn(1L);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        // set other needed fields as per your needs
        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters params = new PartitionPlanJobParameters();
        // set other needed fields as per your needs
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class)).thenReturn(params);

        DatabasePartitionPlanEntity dbEntity = new DatabasePartitionPlanEntity();
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(params.getDatabasePartitionPlanId())).thenReturn(dbEntity);

        List<TablePartitionPlanEntity> tableEntities = new ArrayList<>();
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(params.getDatabasePartitionPlanId())).thenReturn(tableEntities);

        partitionPlanJob.execute(jobExecutionContextMock);
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteAllTasks_WithException() {
        when(jobExecutionContextMock.getScheduleId()).thenReturn(1L);
        ScheduleEntity entity = new ScheduleEntity();
        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(entity);
        doThrow(new SchedulerException()).when(scheduleServiceMock).terminate(entity);
        partitionPlanJob.execute(jobExecutionContextMock);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}
