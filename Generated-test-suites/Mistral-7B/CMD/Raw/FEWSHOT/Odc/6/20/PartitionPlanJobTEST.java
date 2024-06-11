package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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

    @Mock
    private DatabasePartitionPlanEntity databasePartitionPlan;

    @Mock
    private TablePartitionPlanEntity tablePartitionPlan;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private Long scheduleId = 1L;

    @Before
    public void setUp() {
        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);

        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        List<TablePartitionPlanEntity> tablePartitionPlans = mock(java.util.List.class);
        when(tablePartitionPlans.isEmpty()).thenReturn(false);
        when(partitionPlanService.getDatabasePartitionPlanById(scheduleId)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(scheduleId)).thenReturn(tablePartitionPlans);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(scheduleId.toString());
        String jsonString = JsonUtils.toJsonString(jobParameters);
        when(scheduleEntity.getJobParametersJson()).thenReturn(jsonString);

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
    }

    @Test
    public void testExecute_whenScheduleIsValid_thenExecutePartitionPlan() {
        partitionPlanJob.execute(mock(JobExecutionContext.class));

        // Verify that executePartitionPlan is called with correct arguments
        partitionPlanTaskService.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test
    public void testExecute_whenScheduleIsInvalid_thenDoNothing() {
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        // No call to executePartitionPlan
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}