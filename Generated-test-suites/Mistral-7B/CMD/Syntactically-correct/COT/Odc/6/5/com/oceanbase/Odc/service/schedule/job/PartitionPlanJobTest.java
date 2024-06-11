package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanService partitionPlanService;

    private PartitionPlanTaskService partitionPlanTaskService;

    private ScheduleService scheduleService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        partitionPlanService = SpringContextUtil.getMockBean(PartitionPlanService.class);
        partitionPlanTaskService = SpringContextUtil.getMockBean(PartitionPlanTaskService.class);
        scheduleService = SpringContextUtil.getMockBean(ScheduleService.class);
        SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanService);
        SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskService);
        SpringContextUtil.setBean(ScheduleService.class, scheduleService);
    }

    @Test
    public void testExecute_validScheduleAndPartitionPlan() {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = createMockScheduleEntity(scheduleId);
        PartitionPlanJobParameters jobParameters = createMockJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = createMockDatabasePartitionPlan();
        List<TablePartitionPlanEntity> tablePartitionPlans = createMockTablePartitionPlans();

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        // When
        partitionPlanJob.execute(createMockJobExecutionContext());

        // Then
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans);
    }

    @Test(expected = SchedulerException.class)
    public void testExecute_scheduleNotFound() {
        // Given
        Long scheduleId = 1L;

        // When
        partitionPlanJob.execute(createMockJobExecutionContext());

        // Then
        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        // Given

        // When
        partitionPlanJob.interrupt();

        // Then
        // This method should not be called in real scenario, so no verification is needed.
    }

    private ScheduleEntity createMockScheduleEntity(Long scheduleId) {
        return new ScheduleEntity() {{
            setId(scheduleId);
        }};
    }

    private PartitionPlanJobParameters createMockJobParameters() {
        return new PartitionPlanJobParameters();
    }

    private DatabasePartitionPlanEntity createMockDatabasePartitionPlan() {
        return new DatabasePartitionPlanEntity();
    }

    private List<TablePartitionPlanEntity> createMockTablePartitionPlans() {
        return new ArrayList<>();
    }

    private JobExecutionContext createMockJobExecutionContext() {
        return new JobExecutionContext() {{
            setJobDetail(new JobDetail());
            setTrigger(new Trigger());
            setMergedJobDataMap(new JobDataMap());
        }};
    }
}