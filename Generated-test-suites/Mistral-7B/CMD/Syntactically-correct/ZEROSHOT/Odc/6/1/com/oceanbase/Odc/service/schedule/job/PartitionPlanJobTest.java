package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
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

    @Mock
    private ScheduleEntity scheduleEntity;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private Long databasePartitionPlanId = 1L;
    private Long scheduleId = 1L;
    private PartitionPlanJobParameters jobParameters;

    @Before
    public void init() {
        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);

        jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(databasePartitionPlanId);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        databasePartitionPlan.setFlowInstanceId(1L);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        when(partitionPlanService.getDatabasePartitionPlanById(databasePartitionPlanId))
                .thenReturn(Optional.of(databasePartitionPlan));
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(databasePartitionPlanId))
                .thenReturn(tablePartitionPlans);

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
    }

    @Test
    public void testExecute_databasePartitionPlanIsDisabledOrNoTableToProcess() {
        // arrange
        DatabasePartitionPlanEntity dbPartitionPlan = new DatabasePartitionPlanEntity();
        dbPartitionPlan.setConfigEnabled(false);

        // act
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.execute(null);

        // assert
        assertFalse(StringUtils.isBlank(partitionPlanJob.getLog()));
        assertNull(partitionPlanJob.getScheduleEntity());
    }

    @Test
    public void testExecute_databasePartitionPlanIsEnabledAndTableToProcess() {
        // act
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        // assert
        assertTrue(StringUtils.isNotBlank(partitionPlanJob.getLog()));
        assertTrue(StringUtils.isNotBlank(partitionPlanJob.getScheduleEntity().getJobParametersJson()));
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}