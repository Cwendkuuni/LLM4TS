
package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.any;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {
    @Mock
    private JobExecutionContext context;
    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;
    @Mock
    private PartitionPlanService partitionPlanService;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private ScheduleEntity scheduleEntity;

    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanJob = new PartitionPlanJob();
        assertNotNull(partitionPlanJob);
    }

    @Test
    public void testExecute() {
        Long scheduleId = 1L;
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        databasePartitionPlan.setFlowInstanceId(1L);
        TablePartitionPlanEntity tablePartitionPlanEntity = new TablePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = Collections.singletonList(tablePartitionPlanEntity);
        when(context.getMergedJobDataMap()).thenReturn(ScheduleTaskUtils.getJobDataMap(scheduleId));
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class)).thenReturn(jobParameters);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId())).thenReturn(tablePartitionPlans);
        partitionPlanJob.execute(context);
        verify(partitionPlanService, atLeastOnce()).getDatabasePartitionPlanById(any());
        verify(partitionPlanService, atLeastOnce()).getValidTablePlanByDatabasePartitionPlanId(any());
        verify(partitionPlanTaskService, atLeastOnce()).executePartitionPlan(any(), any());
    }

    @Test
    public void testInterrupt() {
        try {
            partitionPlanJob.interrupt();
            fail("Expected Exception has not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
        }
    }
}
