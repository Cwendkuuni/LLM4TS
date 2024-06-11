
package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.quartz.JobExecutionContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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

import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {
    
    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleService mockScheduleService;

    @Mock
	private PartitionPlanService mockPartitionPlanService;

	@Mock
    private PartitionPlanTaskService mockPartitionPlanTaskService;

    private PartitionPlanJob partitionPlanJob;

    @Mock
    private DatabasePartitionPlanEntity mockDatabasePartitionPlanEntity;

    @Mock
    private TablePartitionPlanEntity mockTablePartitionPlanEntity;

    @Before
    public void setup() {
        this.partitionPlanJob = new PartitionPlanJob();

    }

    @Test
    public void testExecute() throws SchedulerException {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));

        when(context.getMergedJobDataMap()).thenReturn(mockMergedJobDataMap);
        when(mockScheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(mockDatabasePartitionPlanEntity.isConfigEnabled()).thenReturn(true);
        when(mockPartitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId())).thenReturn(mockDatabasePartitionPlanEntity);
        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId())).thenReturn(Collections.singletonList(mockTablePartitionPlanEntity));

        partitionPlanJob.execute(context);

        verify(mockPartitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}
