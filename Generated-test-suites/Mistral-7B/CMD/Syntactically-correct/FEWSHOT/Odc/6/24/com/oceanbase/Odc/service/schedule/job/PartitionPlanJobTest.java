package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
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
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanTaskService partitionPlanTaskServiceMock;

    private PartitionPlanService partitionPlanServiceMock;

    private ScheduleService scheduleServiceMock;

    @Before
    public void setup() {
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        scheduleServiceMock = mock(ScheduleService.class);

        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskServiceMock;
        partitionPlanJob.partitionPlanService = partitionPlanServiceMock;
        partitionPlanJob.scheduleService = scheduleServiceMock;
    }

    @Test
    public void testExecute() throws Exception {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntityMock = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        when(scheduleEntityMock.getId()).thenReturn(scheduleId);
        when(scheduleEntityMock.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntityMock);

        DatabasePartitionPlanEntity databasePartitionPlanMock = mock(DatabasePartitionPlanEntity.class);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlanMock);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        // When
        partitionPlanJob.execute(new JobExecutionContext());

        // Then
        assertNotNull(partitionPlanJob.partitionPlanTaskService);
        assertNotNull(partitionPlanJob.partitionPlanService);
        assertNotNull(partitionPlanJob.scheduleService);

        verify(partitionPlanServiceMock).getDatabasePartitionPlanById(1L);
        verify(partitionPlanServiceMock).getValidTablePlanByDatabasePartitionPlanId(1L);
        verify(scheduleServiceMock).nullSafeGetById(scheduleId);

        assertFalse(databasePartitionPlanMock.isConfigEnabled());
        assertFalse(tablePartitionPlans.isEmpty());

        verify(partitionPlanTaskServiceMock).executePartitionPlan(databasePartitionPlanMock.getFlowInstanceId(),
                tablePartitionPlans);
    }

    @Test
    public void testExecuteWhenDatabasePartitionPlanIsDisabled() throws Exception {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntityMock = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        when(scheduleEntityMock.getId()).thenReturn(scheduleId);
        when(scheduleEntityMock.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntityMock);

        DatabasePartitionPlanEntity databasePartitionPlanMock = mock(DatabasePartitionPlanEntity.class);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlanMock);

        // When
        partitionPlanJob.execute(new JobExecutionContext());

        // Then
        verify(partitionPlanServiceMock, never()).getValidTablePlanByDatabasePartitionPlanId(1L);
        verify(scheduleServiceMock, never()).terminate(scheduleEntityMock);
    }

    @Test
    public void testExecuteWhenNoTableNeedToProcess() throws Exception {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntityMock = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        when(scheduleEntityMock.getId()).thenReturn(scheduleId);
        when(scheduleEntityMock.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntityMock);

        DatabasePartitionPlanEntity databasePartitionPlanMock = mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlanMock.isConfigEnabled()).thenReturn(true);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlanMock);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        // When
        partitionPlanJob.execute(new JobExecutionContext());

        // Then
        verify(partitionPlanServiceMock, never()).getValidTablePlanByDatabasePartitionPlanId(1L);
        verify(scheduleServiceMock, never()).terminate(scheduleEntityMock);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        // When
        partitionPlanJob.interrupt();

        // Then
        // This method should not be called as it throws an UnExpectedException
    }
}