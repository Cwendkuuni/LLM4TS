package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

/**
 * @Author: your-name
 * @Date: 2023/8/21 10:37
 * @Description: Test cases for PartitionPlanJob class
 */

@Slf4j
public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private SpringContextUtil springContextUtil;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private Long scheduleId;
    private ScheduleEntity scheduleEntity;
    private PartitionPlanJobParameters jobParameters;
    private DatabasePartitionPlanEntity databasePartitionPlan;
    private List<TablePartitionPlanEntity> tablePartitionPlans;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        scheduleId = 1L;
        scheduleEntity = mock(ScheduleEntity.class);
        jobParameters = new PartitionPlanJobParameters();
        databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        tablePartitionPlans = mock(List.class);
    }

    @After
    public void tearDown() {
        reset(partitionPlanService, partitionPlanTaskService, scheduleService, springContextUtil);
    }

    /**
     * Test execute method when schedule not found
     */
    @Test
    public void testExecuteWhenScheduleNotFound() {
        when(scheduleService.nullSafeGetById(scheduleId)).thenThrow(new Exception());
        partitionPlanJob.execute(mock(JobExecutionContext.class));
        verify(log, times(1)).warn("Schedule not found,scheduleId={}", scheduleId);
    }

    /**
     * Test execute method when schedule entity is null
     */
    @Test
    public void testExecuteWhenScheduleEntityIsNull() {
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(null);
        partitionPlanJob.execute(mock(JobExecutionContext.class));
        verify(log, times(1)).warn("Schedule not found,scheduleId={}", scheduleId);
    }

    /**
     * Test execute method when database partition plan is disabled or no table needs to process
     */
    @Test
    public void testExecuteWhenDatabasePartitionPlanDisabledOrNoTableToProcess() {
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
        doReturn(false).when(databasePartitionPlan).isConfigEnabled();
        doReturn(false).when(tablePartitionPlans).isEmpty();

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        verify(log, times(1)).info(
                "database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                scheduleId);

        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    /**
     * Test execute method when executePartitionPlan method fails
     */
    @Test
    public void testExecuteWhenExecutePartitionPlanFails() {
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
        doReturn(true).when(databasePartitionPlan).isConfigEnabled();
        doReturn(true).when(tablePartitionPlans).isEmpty();

        doThrow(new Exception()).when(partitionPlanTaskService).executePartitionPlan(anyLong(), anyList());

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        verify(log, times(1)).warn("Create partition-plan database change task failed.");
    }

    /**
     * Test execute method when everything goes well
     */
    @Test
    public void testExecuteWhenEverythingGoesWell() {
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
        doReturn(true).when(databasePartitionPlan).isConfigEnabled();
        doReturn(true).when(tablePartitionPlans).isEmpty();

        doNothing().when(partitionPlanTaskService).executePartitionPlan(anyLong(), anyList());

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        verify(log, never()).warn("Create partition-plan database change task failed.");
        verify(log, never()).warn("Schedule not found,scheduleId={}", scheduleId);
        verify(log, never()).info(
                "database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                scheduleId);
    }
}