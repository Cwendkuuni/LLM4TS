
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.junit.runner.RunWith;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
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

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private DatabasePartitionPlanEntity databasePartitionPlan;

    @Mock
    private TablePartitionPlanEntity tablePartitionPlanEntity;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Test
    public void testExecute() {
        Long fakeScheduleId = 123L;

        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(fakeScheduleId);
        when(scheduleService.nullSafeGetById(fakeScheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson())
                .thenReturn(JsonUtils.toJson(new PartitionPlanJobParameters()));
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(databasePartitionPlan);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(Arrays.asList(tablePartitionPlanEntity));

        // Execute method under test
        partitionPlanJob.execute(context);
        
        // Verify that executePartitionPlan method was called on partitionPlanTaskService
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}
