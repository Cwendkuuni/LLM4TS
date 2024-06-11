package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import lombok.extern.slf4j.Slf4j;

/**
 * PartitionPlanJob Test Cases
 *
 * @author tinker
 */
@Slf4j
public class PartitionPlanJobTest {

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private SpringContextUtil springContextUtil;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_WhenScheduleNotFound_ThenLogWarningAndReturn() {
        // Arrange
        UUID scheduleId = UUID.randomUUID();
        when(scheduleService.nullSafeGetById(scheduleId)).thenThrow(new Exception());

        // Act
        partitionPlanJob.execute(mock(JobExecutionContext.class));

        // Assert
        verify(log, times(1)).warn("Schedule not found,scheduleId={}", scheduleId);
        verify(scheduleService, times(0)).terminate(any(ScheduleEntity.class));
    }

    @Test
    public void testExecute_WhenScheduleFoundButPartitionPlanDisabled_ThenLogInfoAndStopJob() {
        // Arrange
        UUID scheduleId = UUID.randomUUID();
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(false);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity());

        // Act
        partitionPlanJob.execute(mock(JobExecutionContext.class));

        // Assert
        verify(log, times(1)).info(
                "database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                scheduleId);
        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_WhenScheduleFoundAndPartitionPlanEnabled_ThenExecutePartitionPlan() {
        // Arrange
        UUID scheduleId = UUID.randomUUID();
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity());

        doNothing().when(partitionPlanTaskService).executePartitionPlan(anyLong(), anyList());

        // Act
        partitionPlanJob.execute(mock(JobExecutionContext.class));

        // Assert
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        // Act
        partitionPlanJob.interrupt();

        // Assert - expected exception
    }
}