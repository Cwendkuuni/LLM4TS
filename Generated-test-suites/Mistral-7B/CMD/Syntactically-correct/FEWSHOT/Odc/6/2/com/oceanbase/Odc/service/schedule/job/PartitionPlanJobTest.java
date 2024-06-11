package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
    private PartitionPlanService partitionPlanServiceMock;
    private PartitionPlanTaskService partitionPlanTaskServiceMock;
    private ScheduleService scheduleServiceMock;
    private ScheduleEntity scheduleEntityMock;

    @Before
    public void setup() {
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        scheduleServiceMock = mock(ScheduleService.class);
        scheduleEntityMock = mock(ScheduleEntity.class);
        SpringContextUtil springContextUtilMock = mock(SpringContextUtil.class);

        when(springContextUtilMock.getBean(PartitionPlanService.class)).thenReturn(partitionPlanServiceMock);
        when(springContextUtilMock.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskServiceMock);
        when(springContextUtilMock.getBean(ScheduleService.class)).thenReturn(scheduleServiceMock);

        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.setSpringContextUtil(springContextUtilMock);
    }

    @Test
    public void testExecute_ScheduleEntityIsNull_Returns() {
        Long scheduleId = 1L;
        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(null);
        partitionPlanJob.execute(new JobExecutionContext());
        verify(log, times(1)).warn("Schedule not found,scheduleId={}", scheduleId);
    }

    @Test
    public void testExecute_ScheduleEntityIsValid_PartitionPlanIsDisabledOrEmpty_StopsJob() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(false);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(new JobExecutionContext());

        verify(log, times(1)).info(
                "database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                scheduleId);
        verify(scheduleServiceMock, times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_ScheduleEntityIsValid_PartitionPlanIsEnabledAndTableToProcess_ExecutesTask() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        tablePartitionPlan.setId(1L);
        tablePartitionPlans.add(tablePartitionPlan);

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(new PartitionPlanJobParameters()));
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        doNothing().when(partitionPlanTaskServiceMock).executePartitionPlan(anyLong(), anyList());

        partitionPlanJob.execute(new JobExecutionContext());

        verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(anyLong(), anyList());
    }
}