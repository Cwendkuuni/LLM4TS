
package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @Mock
    private JobExecutionContext context;
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
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void execute_withValidScheduleID_doesNotThrowsException() {
        Long scheduleId = 1L;
        String parametersJson = "{ \"databasePartitionPlanId\": 1 }";
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(parametersJson);

        DatabasePartitionPlanEntity dbPartitionPlan = new DatabasePartitionPlanEntity();
        dbPartitionPlan.setConfigEnabled(true);
        dbPartitionPlan.setFlowInstanceId(1L);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        when(context.getMergedJobDataMap()).thenReturn(null);
        when(scheduleService.nullSafeGetById(eq(scheduleId))).thenReturn(scheduleEntity);
        doReturn(dbPartitionPlan).when(partitionPlanService).getDatabasePartitionPlanById(eq(1L));
        doReturn(tablePartitionPlans).when(partitionPlanService).getValidTablePlanByDatabasePartitionPlanId(eq(1L));

        partitionPlanJob.execute(context);

        verify(partitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(eq(1L));
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(any(Long.class), eq(tablePartitionPlans));
    }

    @Test(expected = UnExpectedException.class)
    public void interrupt_withException_throwsUnExpectedExceptionHandler() {
        partitionPlanJob.interrupt();
    }
}
