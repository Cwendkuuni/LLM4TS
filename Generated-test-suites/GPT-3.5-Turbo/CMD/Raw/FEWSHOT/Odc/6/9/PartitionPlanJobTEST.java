package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
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
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setup() {
        when(SpringContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(SpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
    }

    @Test
    public void testExecute() throws SchedulerException {
        JobExecutionContext context = mock(JobExecutionContext.class);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        String jobParametersJson = JsonUtils.toJson(jobParameters);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(jobParametersJson);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);

        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService).executePartitionPlan(anyLong(), eq(tablePartitionPlans));
        verify(scheduleService).terminate(eq(scheduleEntity));
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

    @Test
    public void testExecute_NullSchedule() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(scheduleService.nullSafeGetById(1L)).thenThrow(new RuntimeException());

        partitionPlanJob.execute(context);

        assertNull(scheduleService);
    }

    @Test
    public void testExecute_DisablePlan() throws SchedulerException {
        JobExecutionContext context = mock(JobExecutionContext.class);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        String jobParametersJson = JsonUtils.toJson(jobParameters);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(jobParametersJson);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);

        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

        partitionPlanJob.execute(context);

        verify(scheduleService).terminate(eq(scheduleEntity));
    }
}